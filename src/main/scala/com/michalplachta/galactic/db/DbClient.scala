package com.michalplachta.galactic.db

import com.michalplachta.galactic.values.Citizen
import com.michalplachta.galactic.values.Citizen._

import scala.concurrent.Future
import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * A simulation of a client for a very slow database. Some calls may also fail with an exception ;)
 */
object DbClient {
  private val clones = List.range(1, 100).map("Clone Trooper #" + _).map(Stormtrooper(_, cloned = true))
  private val siths = List("Darth Vader", "Emperor", "Ben Solo").map(Sith)
  private val jedis = List("Luke Skywalker", "Obi-wan Kenobi").map(Jedi)
  private val rebels = List("Princess Leia", "Han Solo").map(Rebel)
  private val citizens = siths ++ jedis ++ rebels ++ clones

  def findCitizenByName(name: String): Future[Citizen] = {
    citizens.find(_.name == name) match {
      case Some(citizen) ⇒ simulateResponse(citizen)
      case None          ⇒ simulateBadRequest(s"citizen with name $name couldn't be found")
    }
  }

  def getFollowers(citizen: Citizen): Future[List[Citizen]] = {
    val followers = citizen.name match {
      case "Darth Vader"    ⇒ siths ++ clones
      case "Luke Skywalker" ⇒ jedis ++ rebels
      case _                ⇒ citizens.slice(Random.nextInt(citizens.size), citizens.size)
    }
    simulateResponse(followers)
  }

  private def simulateResponse[R](result: R): Future[R] = {
    Future {
      Thread.sleep(4000)
      result
    }
  }

  private def simulateBadRequest[R](error: String): Future[R] = {
    Future {
      Thread.sleep(2000)
      throw new Exception(s"Bad Request: $error")
    }
  }
}
