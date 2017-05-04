package com.michalplachta.galactic.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.{ HttpApp, Route }
import com.michalplachta.galactic.service.{ Followers, TweetsService }
import spray.json._

object ServerApp extends App with RemoteDataJsonSupport with TweetJsonSupport with SprayJsonSupport {
  object WebServer extends HttpApp {
    def route: Route =
      path("followers" / Segment) { citizenName ⇒
        get {
          complete(Followers.getFollowers(citizenName).toJson)
        }
      } ~
        path("tweets" / Segment) { citizenName ⇒
          get {
            complete(TweetsService.getTweetsFor(citizenName).toJson)
          }
        }
  }

  WebServer.startServer("localhost", 8080)
}
