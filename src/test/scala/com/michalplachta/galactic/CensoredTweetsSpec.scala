package com.michalplachta.galactic

import com.michalplachta.galactic.db.FakeData
import com.michalplachta.galactic.service.Tweets
import org.scalatest.{ Matchers, WordSpec }

class CensoredTweetsSpec extends WordSpec with Matchers {
  "censored Tweets" should {
    "be the same when using imperative and functional approach" in {
      val censoredImperatively = Tweets.censorTweets(FakeData.tweets)
      val censoredFunctionally = Tweets.censorTweetsUsingFilters(FakeData.tweets)

      censoredFunctionally should equal(censoredImperatively)
    }
  }
}
