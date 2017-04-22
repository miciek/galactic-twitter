package com.michalplachta.galactic

import com.michalplachta.galactic.service.Followers.{ Failed, Fetched, Loading, NotRequestedYet }
import com.michalplachta.galactic.service.{ Followers, RemoteData, Tweets }
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
    val remoteFollowers: Followers.RemoteFollowersData = Followers.getRemoteFollowers(name)
    remoteFollowers match {
      case NotRequestedYet() ⇒ "(not requested yet)"
      case Loading()         ⇒ "(loading...)"
      case Fetched(count)    ⇒ count.toString
      case Failed(ex)        ⇒ s"(failed to get followers: ${ex.toString})"
    }
  }

  def getTweetWall(name: String): String = {
    val remoteFollowers: RemoteData[List[Tweet]] = Tweets.getTweetsFor(name)
    remoteFollowers match {
      case RemoteData.NotRequestedYet() ⇒ "(not requested yet)"
      case RemoteData.Loading()         ⇒ "(loading...)"
      case RemoteData.Fetched(tweets)   ⇒ tweets.foldLeft("")((wall, t) ⇒ s"$wall\n${t.author.name}: ${t.text}")
      case RemoteData.Failed(ex)        ⇒ s"(failed to get tweets: ${ex.toString})"
    }
  }

  runConsoleTwitter()
}
