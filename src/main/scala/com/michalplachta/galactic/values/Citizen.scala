package com.michalplachta.galactic.values

sealed trait Citizen { val name: String }

object Citizen {
  case class Jedi(name: String) extends Citizen

  case class Sith(name: String) extends Citizen

  case class Stormtrooper(name: String, cloned: Boolean) extends Citizen

  case class Rebel(name: String) extends Citizen

  case class Civilian(name: String) extends Citizen
}
