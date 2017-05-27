package com.michalplachta.galactic

import com.michalplachta.galactic.db.FakeData
import com.michalplachta.galactic.logic.TweetCensorship
import org.scalatest.{ Matchers, WordSpec }

class TweetCensorshipSpec extends WordSpec with Matchers {
  "censored Tweets" should {
    "be the same when using imperative and functional approach" in {
      val censoredImperatively = TweetCensorship.censorTweets(FakeData.tweets)
      val censoredFunctionally = TweetCensorship.censorTweetsUsingFilters(TweetCensorship.empireFilters)(FakeData.tweets)

      censoredFunctionally should equal(censoredImperatively)
    }
  }
}
