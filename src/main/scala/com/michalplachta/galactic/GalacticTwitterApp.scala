package com.michalplachta.galactic

import scala.annotation.tailrec

object GalacticTwitterApp extends App {
  @tailrec
  def getFollowersCount(): Unit = {
    println("Enter Citizen's name: ")
    val name = io.StdIn.readLine()
    println(s"Getting followers for $name")
    val followersCount = Followers.getFollowersCount(name)
    println(s"$name has $followersCount followers!")
    getFollowersCount()
  }

  getFollowersCount()
}
