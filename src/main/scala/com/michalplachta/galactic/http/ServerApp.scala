package com.michalplachta.galactic.http

import akka.http.scaladsl.server.{ HttpApp, Route }
import com.michalplachta.galactic.service.Followers
import spray.json._

object ServerApp extends App with JsonSupportForRemoteData {
  object WebServer extends HttpApp {
    def route: Route =
      path("followers" / Segment) { citizenName ⇒
        get {
          complete(Followers.getRemoteFollowersGeneric(citizenName).toJson)
        }
      }
  }

  WebServer.startServer("localhost", 8080)
}
