package com.michalplachta.galactic

import com.michalplachta.galactic.service.Followers

import scala.annotation.tailrec
import scala.util.{ Failure, Success, Try }

object GalacticTwitterApp extends App {
  @tailrec
  def printFollowersCount(): Unit = {
    println("Enter Citizen's name: ")
    val name = io.StdIn.readLine()
    println(s"Getting followers for $name")
    val triedFollowersCount: Option[Try[Int]] = Followers.getCachedTriedFollowers(name)
    val followersCount: Option[String] = triedFollowersCount.map {
      case Success(count) ⇒ count.toString
      case Failure(ex)    ⇒ s"(failed to update: ${ex.toString})"
    }
    println(s"$name has ${followersCount.getOrElse("(updating...)")} followers!")
    printFollowersCount()
  }

  printFollowersCount()
}
