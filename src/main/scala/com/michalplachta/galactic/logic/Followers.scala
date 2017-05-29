package com.michalplachta.galactic.logic

import com.michalplachta.galactic.values.Citizen
import com.michalplachta.galactic.values.Citizen.Stormtrooper

object Followers {
  // PROBLEM #4: clones are counted as followers
  def countFollowersNaive(followers: List[Citizen]): Int = {
    followers.length
  }

  // SOLUTION #4: Traversable + pattern matching
  def countFollowers(followers: List[Citizen]): Int = {
    followers.count {
      case Stormtrooper(_, true) ⇒ false
      case _                     ⇒ true
    }
  }
}
