package com.michalplachta.galactic.service

import com.michalplachta.galactic.db.DbClient
import com.michalplachta.galactic.logic.Followers
import com.michalplachta.galactic.logic.Followers.sumFollowers
import com.michalplachta.galactic.values.RemoteData.Loading
import com.michalplachta.galactic.values.{ Citizen, RemoteData }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{ Failure, Success, Try }

object FollowersService {
  object Version1 {
    private var cachedFollowers: Map[String, Int] = Map.empty

    // PROBLEM #1: treating 0 as "no value yet"
    def getFollowers(citizenName: String): Int = {
      getFollowersAsync(citizenName).foreach { result ⇒
        cachedFollowers += (citizenName → result)
      }

      val cachedResult: Option[Int] = cachedFollowers.get(citizenName)
      cachedResult.getOrElse(0)
    }
  }

  object Version2 {
    private var cachedFollowers: Map[String, Int] = Map.empty

    // SOLUTION #1: explicit return type
    // PROBLEM #2: not handling failures
    def getCachedFollowers(citizenName: String): Option[Int] = {
      getFollowersAsync(citizenName).foreach { result ⇒
        cachedFollowers += (citizenName → result)
      }
      cachedFollowers.get(citizenName)
    }
  }

  object Version3 {
    private var cachedTriedFollowers: Map[String, Try[Int]] = Map.empty

    // SOLUTION #2: explicit return type
    // PROBLEM #3: cryptic return type
    def getCachedTriedFollowers(citizenName: String): Option[Try[Int]] = {
      getFollowersAsync(citizenName).onComplete { result ⇒
        cachedTriedFollowers += (citizenName → result)
      }
      cachedTriedFollowers.get(citizenName)
    }
  }

  object Version4 {
    sealed trait RemoteFollowersData
    final case class NotRequestedYet() extends RemoteFollowersData
    final case class Loading() extends RemoteFollowersData
    final case class Failed(errorMessage: String) extends RemoteFollowersData
    final case class Fetched(followers: Int) extends RemoteFollowersData

    private var cachedRemoteFollowers: Map[String, RemoteFollowersData] = Map.empty

    // SOLUTION #3: use Algebraic Data Types to describe states
    def getRemoteFollowers(citizenName: String): RemoteFollowersData = {
      if (cachedRemoteFollowers.get(citizenName).isEmpty) cachedRemoteFollowers += (citizenName → Loading())
      getFollowersAsync(citizenName).onComplete { result ⇒
        val value: RemoteFollowersData =
          result match {
            case Success(followers) ⇒ Fetched(followers)
            case Failure(t)         ⇒ Failed(t.toString)
          }
        cachedRemoteFollowers += (citizenName → value)
      }
      cachedRemoteFollowers.getOrElse(citizenName, NotRequestedYet())
    }
  }

  // VERSION 5, final
  private var cache: Map[String, RemoteData[Int]] = Map.empty

  def getFollowers(citizenName: String): RemoteData[Int] = {
    if (cache.get(citizenName).isEmpty) cache += (citizenName → Loading())
    getFollowersAsync(citizenName).onComplete { result ⇒
      val value: RemoteData[Int] =
        result match {
          case Success(followers) ⇒ RemoteData.Fetched(followers)
          case Failure(t)         ⇒ RemoteData.Failed(t.toString)
        }
      cache += (citizenName → value)
    }
    cache.getOrElse(citizenName, RemoteData.NotRequestedYet())
  }

  private def getFollowersAsync(citizenName: String): Future[Int] = {
    for {
      citizen ← DbClient.getCitizenByName(citizenName)
      followers ← DbClient.getFollowers(citizen).mapTo[List[Citizen]]
    } yield sumFollowers(followers)
  }
}
