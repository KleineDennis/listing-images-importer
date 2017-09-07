package com.autoscout24.listingimages.producer

import akka.NotUsed
import akka.actor.ActorSystem
import akka.event.Logging
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import com.autoscout24.listingimages.producer.model.Event
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}

import scala.concurrent.duration._
import scala.util.Random



object PlainSinkProducerMain extends App {

  implicit val system: ActorSystem = ActorSystem("PlainSinkProducerMain")
  implicit val materializer = ActorMaterializer()

  val log = Logging(system, getClass)

  log.info("Starting Producer")

  val producerSettings = ProducerSettings(system, new ByteArraySerializer, new StringSerializer)
    .withBootstrapServers("localhost:9092")


  //  val done = Source.tick(0.second, 5.second, Event.event)
  //    .map { elem =>
  //      new ProducerRecord[Array[Byte], String]("listing-images", elem)
  //    }
  //    .runWith(Producer.plainSink(producerSettings))
  //

  val done = Source.tick(0.second, 5.second, NotUsed)
    .map(_ => event())
    .map { elem =>
      new ProducerRecord[Array[Byte], String]("listing-images", elem)
    }
    .runWith(Producer.plainSink(producerSettings))


  def event() = {
    Random.nextInt(4) match {
      case 0 => produceRawListing()
      case 1 => deleteRawListing()
      case 2 => produceImages()
      case 3 => deleteImages()
    }
  }

  def produceImages() = {
    log.info("produceImages")
    Event.event
  }

  def deleteImages() = {
    log.info("deleteImages")
    Event.event
  }

  def produceRawListing() = {
    log.info("produceRawListing")
    Event.event
  }

  def deleteRawListing() = {
    log.info("deleteRawListing")
    Event.event
  }

}
//  def produceImages() = {
//    import ListingImages._
//
//    val key = Random.nextInt(10)
//    val modified = DateTime.now()
//    val caption = if (Random.nextBoolean()) Some(Random.alphanumeric.take(8).toArray.mkString) else None
//    val images = List(
//      Image(java.util.UUID.randomUUID().toString, "StandardImage", caption, "url-template"),
//      Image(java.util.UUID.randomUUID().toString, "StandardImage", caption, "url-template")
//    )
//    val deletedAt = if (Random.nextBoolean()) Some("2017-08-01 16:00:00.000Z") else None
//    val testMode = Random.nextBoolean()
//    val listing = ListingImages(key.toString, dateTimeFormatter.print(modified), deletedAt, testMode, DateTime.now().getMillis, images)
//
//    val data = Json.toJson(listing).toString.getBytes("UTF-8")
//    producer.send(new ProducerRecord("listing-images", key.toString, data))
//    producer.flush()
//  }
//
//  def deleteImages() = {
//    producer.send(new ProducerRecord("listing-images", Random.nextInt(10).toString, null))
//    producer.flush()
//  }
//
//  def produceRawListing() = {
//    val key = Random.nextInt(10)
//    val listing = RawListing(key, Random.alphanumeric.take(8).toArray.mkString)
//
//    val data = Json.toJson(listing).toString.getBytes("UTF-8")
//    producer.send(new ProducerRecord("raw-listings", listing.id.toString, data))
//    producer.flush()
//  }
//
//  def deleteRawListing() = {
//    producer.send(new ProducerRecord("raw-listings", Random.nextInt(10).toString, null))
//    producer.flush()
//  }

