package com.michalplachta.galactic.service

import com.michalplachta.galactic.values.Citizen.{ Jedi, Rebel, Sith, Stormtrooper }
import com.michalplachta.galactic.values.Tweet

object Censorship {
  def isProLightSide(tweet: Tweet): Boolean = tweet.text.split("the Force").length > tweet.text.split("Dark Side").length

  def isProDarkSide(tweet: Tweet): Boolean = tweet.text.split("the Force").length < tweet.text.split("Dark Side").length

  def isProEmpire(tweet: Tweet): Boolean = {
    tweet.author match {
      case Sith(_)            ⇒ true
      case Stormtrooper(_, _) ⇒ true
      case _                  ⇒ false
    }
  }

  def isProRebellion(tweet: Tweet): Boolean = {
    tweet.author match {
      case Jedi(_)  ⇒ true
      case Rebel(_) ⇒ true
      case _        ⇒ false
    }
  }

  def isGeneralWisdom(tweet: Tweet): Boolean = tweet.author.name == "Yoda"
  def isJoke(tweet: Tweet): Boolean = tweet.author.name == "Han Solo"

  def addMoreDarkSide(tweet: Tweet) = Tweet(tweet.text + " Follow the Dark Side!", tweet.author)
  def hailEmpire(tweet: Tweet) = Tweet(tweet.text + " Empire Strikes Back!", tweet.author)
  def sacrificeForEmpire(tweet: Tweet) = Tweet(tweet.text + " For the Order and the Republic, I will give anything and everything, including my life.", tweet.author)
  def trashRebellion(tweet: Tweet) = Tweet(tweet.text + " Crash the Resistance! Republic will die!", tweet.author)
  def makeJoke(tweet: Tweet) = Tweet(tweet.text + " And I'm not really interested in your opinion, 3PO.", tweet.author)
  def addMoreForce(tweet: Tweet) = Tweet("May the Force be with you! " + tweet.text, tweet.author)
  def addEvenMoreForce(tweet: Tweet) = Tweet(tweet.text + " I'm one with the Force, the Force is with me.", tweet.author)
  def replaceForceWithDarkSide(tweet: Tweet) = Tweet(tweet.text.replaceAll("Force", "Dark Side"), tweet.author)
}
