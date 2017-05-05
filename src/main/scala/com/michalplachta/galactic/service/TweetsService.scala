package com.michalplachta.galactic.service

import com.michalplachta.galactic.db.DbClient
import com.michalplachta.galactic.logic.TweetCensorship.censorTweetsUsingFilters
import com.michalplachta.galactic.values.RemoteData.{ Failed, Fetched, Loading, NotRequestedYet }
import com.michalplachta.galactic.values.{ RemoteData, Tweet }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{ Failure, Success }

object TweetsService {
  private var cachedTweets: Map[String, RemoteData[List[Tweet]]] = Map.empty

  def getTweetsFor(citizenName: String): RemoteData[List[Tweet]] = {
    if (cachedTweets.get(citizenName).isEmpty) cachedTweets += (citizenName → Loading())
    getTweetsAsync(citizenName)
      .map(censorTweetsUsingFilters)
      .onComplete { triedTweets ⇒
        val remoteTweets: RemoteData[List[Tweet]] =
          triedTweets match {
            case Success(newTweets) ⇒ Fetched(newTweets)
            case Failure(t)         ⇒ Failed(t.toString)
          }
        cachedTweets += (citizenName → remoteTweets)
      }

    cachedTweets.getOrElse(citizenName, NotRequestedYet())
  }

  private def getTweetsAsync(citizenName: String): Future[List[Tweet]] = {
    for {
      citizen ← DbClient.findCitizenByName(citizenName)
      tweets ← DbClient.getTweetsFor(citizen)
    } yield tweets
  }
}
