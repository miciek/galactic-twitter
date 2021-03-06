package com.michalplachta.galactic.logic

import com.michalplachta.galactic.internal.PredicateLogic._
import com.michalplachta.galactic.logic.CensorshipFunctions._
import com.michalplachta.galactic.values.Tweet

object TweetCensorship {
  private val maxCensorshipManipulations = 2

  // PROBLEM #5: Convoluted logic using IFs and vars
  def censorTweets(tweets: List[Tweet]): List[Tweet] = {
    tweets.map { originalTweet ⇒
      var tweet = originalTweet
      var manipulations = 0
      if (isProDarkSide(tweet)) {
        tweet = addMoreDarkSide(tweet)
        manipulations += 1
      }
      if (isProLightSide(tweet) && manipulations < maxCensorshipManipulations) {
        tweet = replaceForceWithDarkSide(tweet)
        manipulations += 1
      }
      if (isGeneralWisdom(tweet) && manipulations < maxCensorshipManipulations) {
        tweet = addMoreDarkSide(replaceForceWithDarkSide(tweet))
        manipulations += 1
      }
      if (isProRebellion(tweet) && !isJoke(tweet) && manipulations < maxCensorshipManipulations) {
        tweet = hailEmpire(tweet)
        manipulations += 1
      }
      if (isProEmpire(tweet) && isJoke(tweet) && manipulations < maxCensorshipManipulations) {
        tweet = trashRebellion(tweet)
        manipulations += 1
      }
      if (!isProRebellion(tweet) && !isProEmpire(tweet) && manipulations < maxCensorshipManipulations) {
        tweet = makeJoke(addEvenMoreForce(addMoreForce(tweet)))
        manipulations += 1
      }
      if (isProEmpire(tweet) && manipulations < maxCensorshipManipulations) {
        tweet = addMoreDarkSide(tweet)
        manipulations += 1
      }
      if (manipulations < maxCensorshipManipulations) {
        tweet = sacrificeForEmpire(tweet)
        manipulations += 1
      }
      tweet
    }
  }

  case class CensorFilter(shouldManipulate: Tweet ⇒ Boolean, manipulate: Tweet ⇒ Tweet)
  case class CensorStatus(tweet: Tweet, manipulations: Int)

  val empireFilters: List[CensorFilter] = List(
    CensorFilter(isProDarkSide, addMoreDarkSide),
    CensorFilter(isProLightSide, replaceForceWithDarkSide),
    CensorFilter(isGeneralWisdom, (replaceForceWithDarkSide _).andThen(addMoreDarkSide)),
    CensorFilter((isProRebellion _).andNot(isJoke), hailEmpire),
    CensorFilter((isProEmpire _).and(isJoke), trashRebellion),
    CensorFilter(not(isProRebellion).andNot(isProEmpire), (addMoreForce _).andThen(addEvenMoreForce).andThen(makeJoke)),
    CensorFilter(isProEmpire, addMoreDarkSide),
    CensorFilter(always, sacrificeForEmpire)
  )

  // SOLUTION #5: Logic as data, accumulator as state, folding data to run the logic
  def censorTweetsUsingFilters(censorFilters: List[CensorFilter])(tweets: List[Tweet]): List[Tweet] = {
    tweets.map { originalTweet ⇒
      val initialStatus = CensorStatus(originalTweet, 0)
      censorFilters.foldLeft(initialStatus) { (status, filter) ⇒
        if (filter.shouldManipulate(status.tweet) && status.manipulations < maxCensorshipManipulations)
          CensorStatus(filter.manipulate(status.tweet), status.manipulations + 1)
        else
          status
      }
    } map (_.tweet)
  }
}
