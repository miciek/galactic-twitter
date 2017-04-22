package com.michalplachta.galactic.service

import com.michalplachta.galactic.db.DbClient
import com.michalplachta.galactic.service.RemoteData.{ Failed, Fetched, NotRequestedYet }
import com.michalplachta.galactic.values.Citizen.{ Jedi, Rebel, Sith, Stormtrooper }
import com.michalplachta.galactic.values.{ Citizen, Tweet }

import scala.concurrent.Future
import scala.util.{ Failure, Success }
import scala.concurrent.ExecutionContext.Implicits.global

object Tweets {
  private var cachedTweetsFor: Map[String, RemoteData[List[Tweet]]] = Map.empty

  def getTweetsFor(citizenName: String): RemoteData[List[Tweet]] = {
    val futureTweets: Future[List[Tweet]] = for {
      citizen ← DbClient.findCitizenByName(citizenName)
      tweets ← DbClient.getTweetsFor(citizen)
    } yield censorTweetsUsingFilters(tweets, citizen)
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

  // PROBLEM #5: Convoluted logic using IFs
  def censorTweets(tweets: List[Tweet], citizen: Citizen): List[Tweet] = {
    tweets.map { t ⇒
      var tweet = t
      tweet = if (isProDarkSide(tweet)) addMoreDarkSide(tweet) else tweet
      tweet = if (isProLightSide(tweet)) replaceForceWithDarkSide(tweet) else tweet
      tweet = if (isGeneralWisdom(tweet)) addMoreDarkSide(replaceForceWithDarkSide(tweet)) else tweet
      tweet = if (isProRebellion(tweet) && !isJoke(tweet)) hailEmpire(tweet) else tweet
      tweet = if (isProEmpire(tweet) && isJoke(tweet)) trashRebellion(tweet) else tweet
      tweet = if (!isProRebellion(tweet) && !isProEmpire(tweet)) makeJoke(addEvenMoreForce(addMoreForce(tweet))) else tweet
      tweet = if (isProEmpire(tweet)) addMoreDarkSide(tweet) else tweet
      sacrificeForEmpire(tweet)
    }
  }

  case class CensorFilter(shouldManipulate: Tweet ⇒ Boolean, manipulate: Tweet ⇒ Tweet)

  implicit class PredicateWithLogic[A](p1: A ⇒ Boolean) {
    def and(p2: A ⇒ Boolean): A ⇒ Boolean = {
      a ⇒ p1(a) && p2(a)
    }

    def andNot(p2: A ⇒ Boolean): A ⇒ Boolean = {
      a ⇒ p1(a) && !p2(a)
    }
  }

  def not[A](p: A ⇒ Boolean): A ⇒ Boolean = a ⇒ !p(a)

  def always[A]: A ⇒ Boolean = _ ⇒ true

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

  // SOLUTION #5: Logic as data, fold data to run the logic
  def censorTweetsUsingFilters(tweets: List[Tweet], citizen: Citizen): List[Tweet] = {
    tweets.map { tweet ⇒
      censorFilters.foldLeft(tweet) { (tweet, filter) ⇒
        if (filter.shouldManipulate(tweet))
          filter.manipulate(tweet)
        else
          tweet
      }
    }
  }

  private def isProLightSide(tweet: Tweet): Boolean = tweet.text.split("the Force").length > tweet.text.split("DarkSide").length

  private def isProDarkSide(tweet: Tweet): Boolean = tweet.text.split("the Force").length < tweet.text.split("DarkSide").length

  private def isProEmpire(tweet: Tweet): Boolean = {
    tweet.author match {
      case Sith(_)            ⇒ true
      case Stormtrooper(_, _) ⇒ true
      case _                  ⇒ false
    }
  }

  private def isProRebellion(tweet: Tweet): Boolean = {
    tweet.author match {
      case Jedi(_)  ⇒ true
      case Rebel(_) ⇒ true
      case _        ⇒ false
    }
  }

  private def isGeneralWisdom(tweet: Tweet): Boolean = tweet.author.name == "Yoda"
  private def isJoke(tweet: Tweet): Boolean = tweet.author.name == "Han Solo"

  private def addMoreDarkSide(tweet: Tweet) = Tweet(tweet.text + " Follow the Dark Side!", tweet.author)
  private def hailEmpire(tweet: Tweet) = Tweet(tweet.text + " Empire Strikes Back!", tweet.author)
  private def sacrificeForEmpire(tweet: Tweet) = Tweet(tweet.text + " For the Order and the Republic, I will give anything and everything, including my life.", tweet.author)
  private def trashRebellion(tweet: Tweet) = Tweet(tweet.text + " Crash the Resistance! Republic will die!", tweet.author)
  private def makeJoke(tweet: Tweet) = Tweet(tweet.text + " And I'm not really interested in your opinion, 3PO.", tweet.author)
  private def addMoreForce(tweet: Tweet) = Tweet("May the Force be with you! " + tweet.text, tweet.author)
  private def addEvenMoreForce(tweet: Tweet) = Tweet(tweet.text + " I'm one with the Force, the Force is with me.", tweet.author)
  private def replaceForceWithDarkSide(tweet: Tweet) = Tweet(tweet.text.replaceAll("Force", "Dark Side"), tweet.author)
}
