package com.michalplachta.galactic.http

import com.michalplachta.galactic.values.Tweet
import spray.json.DefaultJsonProtocol

trait TweetJsonSupport extends DefaultJsonProtocol with CitizenJsonSupport {
  implicit val tweetJson = jsonFormat2(Tweet)
}
