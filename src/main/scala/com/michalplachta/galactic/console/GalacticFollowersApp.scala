package com.michalplachta.galactic.console

import com.michalplachta.galactic.service.FollowersService
import com.michalplachta.galactic.service.FollowersService.Version4._

import scala.annotation.tailrec
import scala.util.{ Failure, Success }

object GalacticFollowersApp extends App {
  @tailrec
  def runFollowersApp(getFollowersText: String ⇒ String): Unit = {
    println("Enter Citizen's name: ")
    val name = io.StdIn.readLine()
    println(s"Getting followers for $name")
    val followersDescription = getFollowersText(name)
    println(s"$name has $followersDescription followers!")
    runFollowersApp(getFollowersText)
  }

  def getAndDescribe(name: String): String = {
    FollowersService.Version1.getFollowers(name).toString
  }

  def getAndDescribeUsingCache(name: String): String = {
    val cachedFollowers = FollowersService.Version2.getCachedFollowers(name)
    cachedFollowers.map(_.toString).getOrElse("(not available)")
  }

  def getAndDescribeUsingCacheWithFailures(name: String): String = {
    val cachedTriedFollowers = FollowersService.Version3.getCachedTriedFollowers(name)
    cachedTriedFollowers map {
      case Success(followers)    ⇒ followers.toString
      case Failure(errorMessage) ⇒ s"(failed to get followers: $errorMessage)"
    } getOrElse "(not available)"
  }

  def getAndDescribeUsingADTs(name: String): String = {
    val remoteFollowers: RemoteFollowersData = FollowersService.Version4.getRemoteFollowers(name)
    remoteFollowers match {
      case NotRequestedYet()    ⇒ "(not requested yet)"
      case Loading()            ⇒ "(loading...)"
      case Fetched(count)       ⇒ count.toString
      case Failed(errorMessage) ⇒ s"(failed to get followers: $errorMessage)"
    }
  }

  runFollowersApp(getAndDescribeUsingADTs)
}
