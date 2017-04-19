package com.michalplachta.galactic.service

import com.michalplachta.galactic.db.DbClient
import com.michalplachta.galactic.values.Citizen
import com.michalplachta.galactic.values.Citizen.Stormtrooper

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{ Failure, Success, Try }

object Followers {
  sealed trait RemoteFollowersData
  final case class NotRequestedYet() extends RemoteFollowersData
  final case class Loading() extends RemoteFollowersData
  final case class Failed(throwable: Throwable) extends RemoteFollowersData
  final case class Fetched(followers: Int) extends RemoteFollowersData

  private var cachedFollowers: Map[String, Int] = Map.empty
  private var cachedTriedFollowers: Map[String, Try[Int]] = Map.empty
  private var cachedRemoteFollowers: Map[String, RemoteFollowersData] = Map.empty

  // PROBLEM #1: treating 0 as "no value yet"
  def getFollowers(name: String): Int = {
    updateFollowersCacheAsync(name)
    val cachedResult: Option[Int] = cachedFollowers.get(name)
    cachedResult.getOrElse(0)
  }

  // SOLUTION #1: explicit return type
  // PROBLEM #2: not handling failures
  def getCachedFollowers(name: String): Option[Int] = {
    updateFollowersCacheAsync(name)
    cachedFollowers.get(name)
  }

  // SOLUTION #2: explicit return type
  // PROBLEM #4: cryptic return type
  def getCachedTriedFollowers(name: String): Option[Try[Int]] = {
    updateFollowersCacheAsync(name)
    cachedTriedFollowers.get(name)
  }

  // SOLUTION #4: use Abstract Data Types to describe states
  def getRemoteFollowers(name: String): RemoteFollowersData = {
    updateFollowersCacheAsync(name)
    cachedRemoteFollowers.getOrElse(name, NotRequestedYet())
  }

  // PROBLEM #3: clones are counted as followers
  // SOLUTION #3: Traversable + pattern matching
  private def sumFollowers(allFollowers: List[Citizen]): Int = {
    // allFollowers.length
    allFollowers.count {
      case Stormtrooper(_, true) ⇒ false
      case _                     ⇒ true
    }
  }

  private def updateFollowersCacheAsync(name: String): Unit = {
    if (cachedRemoteFollowers.get(name).isEmpty) cachedRemoteFollowers += (name → Loading())
    val futureCitizen: Future[Citizen] = DbClient.findCitizenByName(name)
    val futureResult: Future[Int] = futureCitizen.flatMap {
      citizen ⇒ DbClient.getFollowers(citizen).mapTo[List[Citizen]].map(sumFollowers)
    }
    futureResult.foreach { result ⇒
      cachedFollowers += (name → result)
    }
    futureResult.onComplete { result ⇒
      cachedTriedFollowers += (name → result)
      val value: RemoteFollowersData =
        result match {
          case Success(followers) ⇒ Fetched(followers)
          case Failure(t)         ⇒ Failed(t)
        }
      cachedRemoteFollowers += (name → value)
    }
  }
}
