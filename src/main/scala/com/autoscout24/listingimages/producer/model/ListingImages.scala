package com.autoscout24.listingimages.producer.model

import org.joda.time.format.DateTimeFormat
import play.api.libs.json.Json

/**
  * Real listing images like produced by the listing-images-ddb-lamba
  */
case class ListingImages(
                          listingId: String,
                          lastModified: String,
                          deletedAt: Option[String],
                          testMode: Boolean,
                          version: Long,
                          images: List[Image]
                        )

object ListingImages {
  implicit val imageFormat = Json.format[Image]
  implicit val listingImagesFormat = Json.format[ListingImages]

  val dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZoneUTC()
}

/**
  * Real Image like produced by the listing-images-ddb-lamba
  */
case class Image(
                  imageId: String,
                  imageType: String,
                  caption: Option[String],
                  urlTemplate: String
                )



object Event {
  val event =
  """
    |{
    |  "listingId": "6E877B51-B334-4E59-8478-FEBE528CBEE9",
    |  "lastModified": "2017-02-07T12:52:03.540Z",
    |  "images": [
    |    {
    |      "imageType": "StandardImage",
    |      "imageId": "324557f6-f8f9-11e6-993d-27f423f8f8ec",
    |      "listingId": "6E877B51-B334-4E59-8478-FEBE528CBEE9",
    |      "caption": "Description of the image",
    |      "urlTemplate": "https://pics.autoscout24.de/images/listings/324557f6-f8f9-11e6-993d-27f423f8f8ec/{size}.{format}",
    |      "original": {
    |        "height": 1024,
    |        "width": 768,
    |        "md5Checksum": "d7e1e4ef3ef826b41243da31e7d8e9a2",
    |        "contentType": "image/jpeg",
    |        "filename": "fancy_car.jpg",
    |        "href": "https://s3-eu-west-1.amazonaws.com/324557f6-f8f9-11e6-993d-27f423f8f8ec.jpg"
    |      }
    |    },
    |    {
    |      "imageType": "ThreeSixtyVRImage",
    |      "imageId": "6E877B51-B334-4E59-8478-FEBE528CBEE9",
    |      "listingId": "a29372b4-f8f8-11e6-8672-5f7d30838417",
    |      "caption": "Description of the image",
    |      "href": "https://as24-listing-images-360-eu-west-1.s3.amazonaws.com/324557f6-f8f9-11e6-993d-27f423f8f8ec.jpg",
    |      "urlTemplate": "https://pics.autoscout24.de/listing-images-360/324557f6-f8f9-11e6-993d-27f423f8f8ec/{size}.{format}"
    |    },
    |    {
    |      "imageType": "Car360Image",
    |      "imageId": "6E877B51-B334-4E59-8478-FEBE528CBEE9",
    |      "listingId": "a29372b4-f8f8-11e6-8672-5f7d30838417",
    |      "caption": "Description of the image",
    |      "spinId": "508e1778d729490e91866d71cea8f08d"
    |    }
    |  ]
    |}
  """.stripMargin
}
