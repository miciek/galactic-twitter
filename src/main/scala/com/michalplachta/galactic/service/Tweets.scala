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
    } yield censorTweetsFor(tweets, citizen)
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

  def censorTweetsFor(tweets: List[Tweet], citizen: Citizen): List[Tweet] = {
    tweets.map { t ⇒
      if (isProDarkSide(t))
        addMoreDarkSide(t)
      else if (isProLightSide(t))
        replaceForceWithDarkSide(t)
      else if (isGeneralWisdom(t))
        addMoreDarkSide(replaceDarkSideWithForce(t))
      else if (isProRebellion(t.author) && !isJoke(t))
        hailEmpire(t)
      else if (isProEmpire(t.author) && isJoke(t))
        trashRebellion(t)
      else if (!isProRebellion(t.author) && !isProEmpire(t.author))
        makeJoke(addEvenMoreForce(addMoreForce(t)))
      else if (isProEmpire(t.author))
        addMoreDarkSide(t)
      else
        sacrificeForEmpire(t)
    }
  }

  private def isProLightSide(tweet: Tweet): Boolean = tweet.text.split("the Force").length > tweet.text.split("DarkSide").length

  private def isProDarkSide(tweet: Tweet): Boolean = tweet.text.split("the Force").length < tweet.text.split("DarkSide").length

  private def isProEmpire(citizen: Citizen): Boolean = {
    citizen match {
      case Sith(_)            ⇒ true
      case Stormtrooper(_, _) ⇒ true
      case _                  ⇒ false
    }
  }

  private def isProRebellion(citizen: Citizen): Boolean = {
    citizen match {
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
  private def replaceDarkSideWithForce(tweet: Tweet) = Tweet(tweet.text.replaceAll("Dark Side", "Force"), tweet.author)
  private def replaceForceWithDarkSide(tweet: Tweet) = Tweet(tweet.text.replaceAll("Force", "Dark Side"), tweet.author)
}
