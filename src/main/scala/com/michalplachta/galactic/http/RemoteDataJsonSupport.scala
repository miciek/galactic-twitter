package com.michalplachta.galactic.http

import com.michalplachta.galactic.values.RemoteData
import com.michalplachta.galactic.values.RemoteData._
import spray.json.{ JsObject, JsValue, _ }

// NOTE: this is done manually for demonstration purposes, see CitizenJsonSupport to see helper functions usage
trait RemoteDataJsonSupport extends DefaultJsonProtocol {
  implicit def remoteDataJson[A: JsonWriter] = new RootJsonWriter[RemoteData[A]] {
    def write(remoteData: RemoteData[A]): JsValue = remoteData match {
      case _: NotRequestedYet[A] ⇒ JsObject("state" → JsString("NotRequestedYet"))
      case _: Loading[A]         ⇒ JsObject("state" → JsString("Loading"))
      case o: Failed[A]          ⇒ JsObject("state" → JsString("Failed"), "errorMessage" → JsString(o.errorMessage))
      case o: Fetched[A]         ⇒ JsObject("state" → JsString("Fetched"), "data" → o.data.toJson)
    }
  }
}
