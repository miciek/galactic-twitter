package com.michalplachta.galactic.http

import com.michalplachta.galactic.values.Citizen
import com.michalplachta.galactic.values.Citizen._
import spray.json._

trait CitizenJsonSupport extends DefaultJsonProtocol {
  implicit val jediJson = jsonFormat1(Jedi)
  implicit val sithJson = jsonFormat1(Sith)
  implicit val stormtrooperJson = jsonFormat2(Stormtrooper)
  implicit val rebelJson = jsonFormat1(Rebel)
  implicit val civilJson = jsonFormat1(Civilian)

  implicit val citizenJson = new RootJsonFormat[Citizen] {
    def write(obj: Citizen): JsValue = JsObject((obj match {
      case jedi: Jedi                 ⇒ jedi.toJson
      case sith: Sith                 ⇒ sith.toJson
      case stormtrooper: Stormtrooper ⇒ stormtrooper.toJson
      case rebel: Rebel               ⇒ rebel.toJson
      case civil: Civilian            ⇒ civil.toJson
    }).asJsObject.fields + ("type" → JsString(obj.getClass.getSimpleName)))

    def read(json: JsValue): Citizen = json.asJsObject.getFields("type") match {
      case Seq(JsString("Jedi"))         ⇒ json.convertTo[Jedi]
      case Seq(JsString("Sith"))         ⇒ json.convertTo[Sith]
      case Seq(JsString("Stormtrooper")) ⇒ json.convertTo[Stormtrooper]
      case Seq(JsString("Rebel"))        ⇒ json.convertTo[Rebel]
      case Seq(JsString("Civilian"))     ⇒ json.convertTo[Civilian]
    }
  }
}
