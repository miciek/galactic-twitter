package com.michalplachta.galactic

import com.michalplachta.galactic.service.RemoteData._
import com.michalplachta.galactic.service.{ Followers, RemoteData, TweetsService }
import com.michalplachta.galactic.values.Tweet

import scala.annotation.tailrec

object GalacticTwitterApp extends App {
  @tailrec
  def runConsoleTwitter(): Unit = {
    println("Enter Citizen's name: ")
    val name = io.StdIn.readLine()
    println(s"Getting followers for $name")
    val followersDescription = getFollowersText(name)
    val tweets = getTweetWall(name)
    println(s"$name has $followersDescription followers!")
    println(s"$name's Tweet Wall: $tweets")
    runConsoleTwitter()
  }

  def getFollowersText(name: String): String = {
    val remoteFollowers: RemoteData[Int] = Followers.getFollowers(name)
    remoteFollowers match {
      case NotRequestedYet()    ⇒ "(not requested yet)"
      case Loading()            ⇒ "(loading...)"
      case Fetched(count)       ⇒ count.toString
      case Failed(errorMessage) ⇒ s"(failed to get followers: $errorMessage)"
    }
  }

  def getTweetWall(name: String): String = {
    val remoteFollowers: RemoteData[List[Tweet]] = TweetsService.getTweetsFor(name)
    remoteFollowers match {
      case NotRequestedYet()    ⇒ "(not requested yet)"
      case Loading()            ⇒ "(loading...)"
      case Fetched(tweets)      ⇒ tweets.foldLeft("")((wall, t) ⇒ s"$wall\n${t.author.name}: ${t.text}")
      case Failed(errorMessage) ⇒ s"(failed to get tweets: $errorMessage)"
    }
  }

  runConsoleTwitter()
}
