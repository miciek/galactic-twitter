package com.michalplachta.galactic.console

import com.michalplachta.galactic.values.RemoteData._
import com.michalplachta.galactic.service.{ FollowersService, TweetsService }
import com.michalplachta.galactic.values.{ RemoteData, Tweet }

import scala.annotation.tailrec
import scala.io.StdIn

object GalacticTwitterApp extends App {
  @tailrec
  def runConsoleTwitter(): Unit = {
    println("---\nEnter Citizen's name: ")
    val name = StdIn.readLine()
    println(s"Getting followers for $name")
    val followersText = getFollowersText(name)
    val tweets = getTweetWall(name)
    println(s"$name has $followersText followers!")
    println(s"$name's Tweet Wall: $tweets")
    runConsoleTwitter()
  }

  def getFollowersText(citizenName: String): String = {
    val remoteFollowers: RemoteData[Int] = FollowersService.getFollowers(citizenName)
    remoteFollowers match {
      case NotRequestedYet()    ⇒ "(not requested yet)"
      case Loading()            ⇒ "(loading...)"
      case Fetched(count)       ⇒ count.toString
      case Failed(errorMessage) ⇒ s"(failed to get followers: $errorMessage)"
    }
  }

  def getTweetWall(citizenName: String): String = {
    val remoteTweets: RemoteData[List[Tweet]] = TweetsService.getTweetsFor(citizenName)
    remoteTweets match {
      case NotRequestedYet()    ⇒ "(not requested yet)"
      case Loading()            ⇒ "(loading...)"
      case Fetched(tweets)      ⇒ tweets.foldLeft("")((wall, t) ⇒ s"$wall\n${t.author.name}: ${t.text}")
      case Failed(errorMessage) ⇒ s"(failed to get tweets: $errorMessage)"
    }
  }

  runConsoleTwitter()
}
