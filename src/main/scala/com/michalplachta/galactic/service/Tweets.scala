package com.michalplachta.galactic.service

import com.michalplachta.galactic.db.DbClient
import com.michalplachta.galactic.internal.PredicateLogic._
import com.michalplachta.galactic.service.Censorship._
import com.michalplachta.galactic.service.RemoteData.{ Failed, Fetched, NotRequestedYet }
import com.michalplachta.galactic.values.Tweet

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{ Failure, Success }

object Tweets {
  private var cachedTweetsFor: Map[String, RemoteData[List[Tweet]]] = Map.empty
  val maxCensorshipManipulations = 2

  def getTweetsFor(citizenName: String): RemoteData[List[Tweet]] = {
    val futureTweets: Future[List[Tweet]] = for {
      citizen ← DbClient.findCitizenByName(citizenName)
      tweets ← DbClient.getTweetsFor(citizen)
    } yield censorTweetsUsingFilters(tweets)

    futureTweets.onComplete { result ⇒
      val value: RemoteData[List[Tweet]] =
        result match {
          case Success(followers) ⇒ Fetched(followers)
          case Failure(t)         ⇒ Failed(t)
        }
      cachedTweetsFor += (citizenName → value)
    }

    cachedTweetsFor.getOrElse(citizenName, NotRequestedYet())
  }

  // PROBLEM #5: Convoluted logic using IFs and vars
  def censorTweets(tweets: List[Tweet]): List[Tweet] = {
    tweets.map { t ⇒
      var tweet = t
      var manipulations = 0
      if (isProDarkSide(tweet)) {
        tweet = addMoreDarkSide(tweet)
        manipulations += 1
      }
      if (isProLightSide(tweet) && manipulations < maxCensorshipManipulations) {
        tweet = replaceForceWithDarkSide(tweet)
        manipulations += 1
      }
      if (isGeneralWisdom(tweet) && manipulations < maxCensorshipManipulations) {
        tweet = addMoreDarkSide(replaceForceWithDarkSide(tweet))
        manipulations += 1
      }
      if (isProRebellion(tweet) && !isJoke(tweet) && manipulations < maxCensorshipManipulations) {
        tweet = hailEmpire(tweet)
        manipulations += 1
      }
      if (isProEmpire(tweet) && isJoke(tweet) && manipulations < maxCensorshipManipulations) {
        tweet = trashRebellion(tweet)
        manipulations += 1
      }
      if (!isProRebellion(tweet) && !isProEmpire(tweet) && manipulations < maxCensorshipManipulations) {
        tweet = makeJoke(addEvenMoreForce(addMoreForce(tweet)))
        manipulations += 1
      }
      if (isProEmpire(tweet) && manipulations < maxCensorshipManipulations) {
        tweet = addMoreDarkSide(tweet)
        manipulations += 1
      }
      if (manipulations < maxCensorshipManipulations) {
        tweet = sacrificeForEmpire(tweet)
        manipulations += 1
      }
      tweet
    }
  }

  case class CensorFilter(shouldManipulate: Tweet ⇒ Boolean, manipulate: Tweet ⇒ Tweet)
  case class CensorStatus(tweet: Tweet, manipulations: Int)

  val censorFilters: List[CensorFilter] = List(
    CensorFilter(isProDarkSide, addMoreDarkSide),
    CensorFilter(isProLightSide, replaceForceWithDarkSide),
    CensorFilter(isGeneralWisdom, (replaceForceWithDarkSide _).andThen(addMoreDarkSide)),
    CensorFilter((isProRebellion _).andNot(isJoke), hailEmpire),
    CensorFilter((isProEmpire _).and(isJoke), trashRebellion),
    CensorFilter(not(isProRebellion).andNot(isProEmpire), (addMoreForce _).andThen(addEvenMoreForce).andThen(makeJoke)),
    CensorFilter(isProEmpire, addMoreDarkSide),
    CensorFilter(always, sacrificeForEmpire)
  )

  // SOLUTION #5: Logic as data, accumulator as state, folding data to run the logic
  def censorTweetsUsingFilters(tweets: List[Tweet]): List[Tweet] = {
    tweets.map { originalTweet ⇒
      val initialStatus = CensorStatus(originalTweet, 0)
      censorFilters.foldLeft(initialStatus) { (status, filter) ⇒
        if (filter.shouldManipulate(status.tweet) && status.manipulations < maxCensorshipManipulations)
          CensorStatus(filter.manipulate(status.tweet), status.manipulations + 1)
        else
          status
      }
    } map (_.tweet)
  }
}
