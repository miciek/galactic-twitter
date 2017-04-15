package com.michalplachta.galactic

import com.michalplachta.galactic.service.Followers

import scala.annotation.tailrec

object GalacticTwitterApp extends App {
  @tailrec
  def getFollowersCount(): Unit = {
    println("Enter Citizen's name: ")
    val name = io.StdIn.readLine()
    println(s"Getting followers for $name")
    val followersCount = Followers.getCachedFollowersCount(name)
    println(s"$name has ${followersCount.getOrElse("N/A")} followers!")
    getFollowersCount()
  }

  getFollowersCount()
}
