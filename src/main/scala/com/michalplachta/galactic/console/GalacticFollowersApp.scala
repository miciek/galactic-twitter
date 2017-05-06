package com.michalplachta.galactic.console

import com.michalplachta.galactic.service.FollowersService
import com.michalplachta.galactic.service.FollowersService.Version4._

import scala.annotation.tailrec
import scala.util.{ Failure, Success }

object GalacticFollowersApp extends App {
  @tailrec
  def runFollowersApp(getFollowersText: String ⇒ String): Unit = {
    println("---\nEnter Citizen's name: ")
    val citizenName = io.StdIn.readLine()
    println(s"Getting followers for $citizenName")
    val followersDescription = getFollowersText(citizenName)
    println(s"$citizenName has $followersDescription followers!")
    runFollowersApp(getFollowersText)
  }

  def getAndDescribe(citizenName: String): String = {
    FollowersService.Version1.getFollowers(citizenName).toString
  }

  def getAndDescribeUsingCache(citizenName: String): String = {
    val cachedFollowers = FollowersService.Version2.getCachedFollowers(citizenName)
    cachedFollowers.map(_.toString).getOrElse("(not available)")
  }

  def getAndDescribeUsingCacheWithFailures(citizenName: String): String = {
    val cachedTriedFollowers = FollowersService.Version3.getCachedTriedFollowers(citizenName)
    cachedTriedFollowers map {
      case Success(followers)    ⇒ followers.toString
      case Failure(errorMessage) ⇒ s"(failed to get followers: $errorMessage)"
    } getOrElse "(not available)"
  }

  def getAndDescribeUsingADTs(citizenName: String): String = {
    val remoteFollowers: RemoteFollowersData = FollowersService.Version4.getRemoteFollowers(citizenName)
    remoteFollowers match {
      case NotRequestedYet()    ⇒ "(not requested yet)"
      case Loading()            ⇒ "(loading...)"
      case Fetched(followers)   ⇒ followers.toString
      case Failed(errorMessage) ⇒ s"(failed to get followers: $errorMessage)"
    }
  }

  runFollowersApp(getAndDescribeUsingADTs)
}
