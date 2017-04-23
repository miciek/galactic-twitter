package com.michalplachta.galactic.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.michalplachta.galactic.service.RemoteData
import com.michalplachta.galactic.service.RemoteData._
import spray.json.{ DefaultJsonProtocol, JsObject, JsValue, RootJsonFormat }
import spray.json._

trait JsonSupportForRemoteData extends DefaultJsonProtocol with SprayJsonSupport {
  implicit def notRequestedYetJson[A: JsonFormat] = jsonFormat0(NotRequestedYet.apply[A])
  implicit def loadingJson[A: JsonFormat] = jsonFormat0(Loading.apply[A])
  implicit def failedJson[A: JsonFormat] = jsonFormat1(Failed.apply[A])
  implicit def fetchedJson[A: JsonFormat] = jsonFormat1(Fetched.apply[A])

  implicit def remoteDataJson[A: JsonFormat] = new RootJsonFormat[RemoteData[A]] {
    def write(obj: RemoteData[A]): JsValue = JsObject((obj match {
      case d: NotRequestedYet[A] ⇒ d.toJson
      case d: Loading[A]         ⇒ d.toJson
      case d: Failed[A]          ⇒ d.toJson
      case d: Fetched[A]         ⇒ d.toJson
    }).asJsObject.fields + ("state" → JsString(obj.getClass.getSimpleName)))

    def read(json: JsValue): RemoteData[A] = json.asJsObject.getFields("state") match {
      case Seq(JsString("NotRequestedYet")) ⇒ json.convertTo[NotRequestedYet[A]]
      case Seq(JsString("Loading"))         ⇒ json.convertTo[Loading[A]]
      case Seq(JsString("Failed"))          ⇒ json.convertTo[Failed[A]]
      case Seq(JsString("Fetched"))         ⇒ json.convertTo[Fetched[A]]
    }
  }
}
