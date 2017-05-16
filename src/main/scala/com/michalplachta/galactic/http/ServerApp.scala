package com.michalplachta.galactic.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.{ HttpApp, Route }
import com.michalplachta.galactic.service.{ FollowersService, TweetsService }
import com.michalplachta.galactic.values.{ RemoteData, Tweet }

object ServerApp extends App with RemoteDataJsonSupport with TweetJsonSupport with SprayJsonSupport {
  object WebServer extends HttpApp {
    def route: Route =
      path("followers" / Segment) { citizenName ⇒
        get {
          complete {
            val remoteFollowers: RemoteData[Int] = FollowersService.getFollowers(citizenName)
            remoteFollowers
          }
        }
      } ~
        path("tweets" / Segment) { citizenName ⇒
          get {
            complete {
              val remoteTweets: RemoteData[List[Tweet]] = TweetsService.getTweetsFor(citizenName)
              remoteTweets
            }
          }
        }
  }

  WebServer.startServer("localhost", 8080)
}
