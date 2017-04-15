package com.michalplachta.galactic.service

import com.michalplachta.galactic.db.DbClient
import com.michalplachta.galactic.values.Citizen

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Followers {
  private var followers: Map[String, Int] = Map.empty

  // PROBLEM #1: treating 0 as "no value yet"
  def getFollowersCount(name: String): Int = {
    updateFollowersCacheAsync(name)
    val cachedResult: Option[Int] = followers.get(name)
    cachedResult.getOrElse(0)
  }

  // SOLUTION #1: explicit return type
  def getCachedFollowersCount(name: String): Option[Int] = {
    updateFollowersCacheAsync(name)
    followers.get(name)
  }

  private def updateFollowersCacheAsync(name: String): Unit = {
    val futureCitizen: Future[Citizen] = DbClient.findCitizenByName(name)
    val futureResult: Future[Int] = futureCitizen.flatMap {
      citizen ⇒ DbClient.getFollowers(citizen).mapTo[List[Citizen]].map(_.length)
    }
    futureResult.foreach { result ⇒
      followers += (name → result)
    }
  }
}
