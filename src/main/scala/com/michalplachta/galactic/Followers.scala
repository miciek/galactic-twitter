package com.michalplachta.galactic

import com.michalplachta.galactic.db.DbClient
import com.michalplachta.galactic.values.Citizen

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._

import scala.concurrent.ExecutionContext.Implicits.global

object Followers {
  def getFollowersCount(name: String): Int = {
    val futureCitizen: Future[Citizen] = DbClient.findCitizenByName(name)
    val citizen = Await.result(futureCitizen, 5.seconds)
    val futureResult: Future[Int] = DbClient.getFollowers(citizen).mapTo[List[Citizen]].map(_.length)
    Await.result(futureResult, 5.seconds)
  }
}
