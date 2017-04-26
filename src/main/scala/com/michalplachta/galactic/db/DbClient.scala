package com.michalplachta.galactic.db

import com.michalplachta.galactic.values.{ Citizen, Tweet }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * A simulation of a client for a very slow database. Some calls may also fail with an exception ;)
 */
object DbClient {
  import com.michalplachta.galactic.db.FakeData._

  def findCitizenByName(citizenName: String): Future[Citizen] = {
    citizens.find(_.name == citizenName) match {
      case Some(citizen) ⇒ simulateResponse(citizen)
      case None          ⇒ simulateBadRequest(s"citizen with name $citizenName couldn't be found")
    }
  }

  def getFollowers(citizen: Citizen): Future[List[Citizen]] = {
    val followers = citizen.name match {
      case "Darth Vader"    ⇒ siths ++ clones
      case "Luke Skywalker" ⇒ jedis ++ rebels
      case _                ⇒ citizens.slice(citizens.indexOf(citizen), citizens.size)
    }
    simulateResponse(followers)
  }

  def getTweetsFor(citizen: Citizen): Future[List[Tweet]] = {
    val tweetsForCitizen = tweets.filter(_.author != citizen)
    simulateResponse(tweetsForCitizen)
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
