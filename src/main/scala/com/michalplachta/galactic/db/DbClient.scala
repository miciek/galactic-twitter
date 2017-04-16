package com.michalplachta.galactic.db

import com.michalplachta.galactic.values.Citizen

import scala.concurrent.Future
import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * A simulation of a client for a very slow database. Some calls may also fail with an exception ;)
 */
object DbClient {
  private val clones = List.range(1, 100).map("Stormtrooper #" + _).map(Citizen)
  private val mainCharacters = List("Darth Vader", "Luke Skywalker", "Princess Leia", "Han Solo", "Ben Solo", "Emperor", "Obi-wan Kenobi").map(Citizen)
  private val citizens = mainCharacters ++ clones

  def findCitizenByName(name: String): Future[Citizen] = {
    citizens.find(_.name == name) match {
      case Some(citizen) ⇒ simulateResponse(citizen)
      case None          ⇒ simulateBadRequest(s"citizen with name $name couldn't be found")
    }
  }

  def getFollowers(citizen: Citizen): Future[List[Citizen]] = {
    val followers = citizen.name match {
      case "Darth Vader"    ⇒ clones
      case "Luke Skywalker" ⇒ mainCharacters
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
