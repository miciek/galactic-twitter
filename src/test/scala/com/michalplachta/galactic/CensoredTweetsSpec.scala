package com.michalplachta.galactic

import com.michalplachta.galactic.db.FakeData
import com.michalplachta.galactic.service.Tweets
import com.michalplachta.galactic.values.Citizen.Sith
import org.scalatest.{ Matchers, WordSpec }

class CensoredTweetsSpec extends WordSpec with Matchers {
  "censored Tweets" should {
    "be the same when using imperative and functional approach" in {
      val censoredImperatively = Tweets.censorTweets(FakeData.tweets, Sith("Darth Vader"))
      val censoredFunctionally = Tweets.censorTweetsUsingFilters(FakeData.tweets, Sith("Darth Vader"))

      censoredFunctionally should equal(censoredImperatively)
    }
  }
}
