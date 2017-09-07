package com.autoscout24.listingimages.consumer

import java.util.concurrent.atomic.AtomicLong

import akka.Done
import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken, RawHeader}
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}
import pdi.jwt.{Jwt, JwtAlgorithm}
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._


object PlainSourceConsumerMain extends App {

  implicit val system = ActorSystem("PlainSourceConsumerMain")
  implicit val materializer = ActorMaterializer()

  val log = Logging(system, getClass)

  log.info("Starting Consumer")

  val bootstrap_servers = ConfigFactory.load.getString("bootstrap.servers")
  val topic = ConfigFactory.load.getString("topics")

  val consumerSettings = ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer)
    .withBootstrapServers(bootstrap_servers)
    .withGroupId(topic)
    .withClientId(topic)
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest") //"latest", "earliest", "none"
//    .withProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100")
//    .withProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")


  val db = new DB(log)
  db.loadOffset().foreach { fromOffset =>
    val partition = 0
    val subscription = Subscriptions.assignmentWithOffset(new TopicPartition(topic, partition) -> fromOffset)
    val done = Consumer.plainSource(consumerSettings, subscription)
                .mapAsync(1)(db.save)
                .runWith(Sink.ignore)
  }

}


class DB(val log: LoggingAdapter) (implicit materializer: ActorMaterializer, implicit val system: ActorSystem) {

  case class Image(listingId: String, imageId: String, imageType: String)
  case class Listing(listingId: String, images: Seq[Image])

  implicit val imageReads: Reads[Image] = (
    (JsPath \ "listingId").read[String] and
      (JsPath \ "imageId").read[String] and
      (JsPath \ "imageType").read[String]
    )(Image.apply _)

  implicit val listingReads: Reads[Listing] = (
    (JsPath \ "listingId").read[String] and
      (JsPath \ "images").read[Seq[Image]]
    )(Listing.apply _)

  private val offset = new AtomicLong


  def save(record: ConsumerRecord[Array[Byte], String]): Future[Done] = {
//    log.debug(s"DB.save: ${record.value}")

    val json: JsValue = Json.parse(record.value)
    val result: JsResult[Listing] = json.validate[Listing]

    result match {
      case s: JsSuccess[Listing] => {
        val listing: Listing = s.get
        val listingId = listing.listingId
        val images = listing.images

        val ret = images.filter(p => List("ThreeSixtyVRImage", "Car360Image").contains(p.imageType))

        ret.foreach { img =>
          val imageId = img.imageId
          val imageType = img.imageType
          val kind =
            if (imageType.equals("ThreeSixtyVRImage")) "inside"
            else if (imageType.equals("Car360Image")) "outside"
            else ""

          val uri = s"https://myarea.autoscout24.de/api/car360/$listingId/$kind/$imageId"
          val secretKey = ConfigFactory.load.getString("jwt.secret")
          val token = Jwt.encode("""{"user":1}""", secretKey, JwtAlgorithm.HS256)

          val request = HttpRequest()
            .withMethod(HttpMethods.POST)
            .withUri(uri)
            .withHeaders(Authorization(OAuth2BearerToken(token)))

          val responseFuture: Future[HttpResponse] = Http().singleRequest(request = request /*, log = log*/)
          log.debug(s"DB.save responseFuture: ${responseFuture.map(_.status)}")
        }

        offset.set(record.offset)
        Future.successful(Done)

      }
      case e: JsError => {
        // error handling flow
        Future.successful(Done)
      }
    }
  }

  def loadOffset(): Future[Long] =
    Future.successful(offset.get)

  def update(data: String): Future[Done] = {
    log.debug(s"DB.update: $data")
    Future.successful(Done)
  }
}

