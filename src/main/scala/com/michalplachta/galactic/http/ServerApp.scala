package com.michalplachta.galactic.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.{HttpApp, Route}
import com.michalplachta.galactic.service.Followers
import spray.json._

object ServerApp extends App with RemoteDataJsonSupport with SprayJsonSupport {
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
