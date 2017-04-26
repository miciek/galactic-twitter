package com.michalplachta.galactic

import com.michalplachta.galactic.db.{FakeData => ScalaFakeData}
import com.michalplachta.galactic.java.db.{FakeData => JavaFakeData}
import com.michalplachta.galactic.java.values.{Civil, Jedi, Rebel, Sith, Stormtrooper, Citizen => JavaCitizen, Tweet => JavaTweet}
import com.michalplachta.galactic.values.{Citizen => ScalaCitizen, Tweet => ScalaTweet}
import org.scalatest.{Matchers, WordSpec}

import scala.collection.JavaConverters._

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
        t => new JavaTweet(t.text, scalaCitizenToJavaCitizen(t.author))
      }
      javaTweets should equal(scalaTweets)
    }
  }

  private def scalaCitizenToJavaCitizen(citizen: ScalaCitizen): JavaCitizen = {
    citizen match {
      case ScalaCitizen.Civil(name) => new Civil(name)
      case ScalaCitizen.Jedi(name) => new Jedi(name)
      case ScalaCitizen.Rebel(name) => new Rebel(name)
      case ScalaCitizen.Sith(name) => new Sith(name)
      case ScalaCitizen.Stormtrooper(name, isCloned) => new Stormtrooper(name, isCloned)
    }
  }
}
