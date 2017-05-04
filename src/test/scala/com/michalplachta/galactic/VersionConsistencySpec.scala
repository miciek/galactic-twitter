package com.michalplachta.galactic

import com.michalplachta.galactic.db.{ FakeData ⇒ ScalaFakeData }
import com.michalplachta.galactic.java.db.{ FakeData ⇒ JavaFakeData }
import com.michalplachta.galactic.java.logic.{ TweetCensorship ⇒ JavaCensorship }
import com.michalplachta.galactic.java.values.{ Civil, Jedi, Rebel, Sith, Stormtrooper, Citizen ⇒ JavaCitizen, Tweet ⇒ JavaTweet }
import com.michalplachta.galactic.logic.{ TweetCensorship ⇒ ScalaCensorship }
import com.michalplachta.galactic.values.{ Citizen ⇒ ScalaCitizen }

import org.scalatest.{ Matchers, WordSpec }

import scala.collection.JavaConverters._

// testing that Java and Scala versions do the same things
class VersionConsistencySpec extends WordSpec with Matchers {
  "Java and Scala versions" should {
    "have the same Citizen lists" in {
      val javaCitizens: Seq[JavaCitizen] = JavaFakeData.citizens.toJavaList().asScala
      val scalaCitizens: Seq[JavaCitizen] = ScalaFakeData.citizens.map(scalaCitizenToJavaCitizen)
      javaCitizens should equal(scalaCitizens)
    }

    "have the same Tweet lists" in {
      val javaTweets: Seq[JavaTweet] = JavaFakeData.tweets.toJavaList().asScala
      val scalaTweets: Seq[JavaTweet] = ScalaFakeData.tweets.map {
        t ⇒ new JavaTweet(t.text, scalaCitizenToJavaCitizen(t.author))
      }
      javaTweets should equal(scalaTweets)
    }

    "have the same censored Tweet lists" in {
      val javaCensoredTweets: Seq[JavaTweet] = JavaCensorship.censorTweetsUsingFilters(JavaFakeData.tweets).toJavaList().asScala
      val scalaCensoredTweets: Seq[JavaTweet] = ScalaCensorship.censorTweetsUsingFilters(ScalaFakeData.tweets).map {
        t ⇒ new JavaTweet(t.text, scalaCitizenToJavaCitizen(t.author))
      }
      javaCensoredTweets should equal(scalaCensoredTweets)
    }

    "have the same censored Tweet lists (when censored imperatively)" in {
      val javaCensoredTweets: Seq[JavaTweet] = JavaCensorship.censorTweets(JavaFakeData.tweets).toJavaList().asScala
      val scalaCensoredTweets: Seq[JavaTweet] = ScalaCensorship.censorTweets(ScalaFakeData.tweets).map {
        t ⇒ new JavaTweet(t.text, scalaCitizenToJavaCitizen(t.author))
      }
      javaCensoredTweets should equal(scalaCensoredTweets)
    }
  }

  private def scalaCitizenToJavaCitizen(citizen: ScalaCitizen): JavaCitizen = {
    citizen match {
      case ScalaCitizen.Civil(name)                  ⇒ new Civil(name)
      case ScalaCitizen.Jedi(name)                   ⇒ new Jedi(name)
      case ScalaCitizen.Rebel(name)                  ⇒ new Rebel(name)
      case ScalaCitizen.Sith(name)                   ⇒ new Sith(name)
      case ScalaCitizen.Stormtrooper(name, isCloned) ⇒ new Stormtrooper(name, isCloned)
    }
  }
}
