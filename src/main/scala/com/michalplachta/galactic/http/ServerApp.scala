package com.michalplachta.galactic.http

import akka.http.scaladsl.server.{ HttpApp, Route }
import com.michalplachta.galactic.service.Followers
import com.michalplachta.galactic.service.Followers.Fetched

object ServerApp extends App {
  object WebServer extends HttpApp {
    def route: Route =
      path("followers" / Segment) { citizenName ⇒
        get {
          complete {
            Followers.getRemoteFollowers(citizenName) match {
              case Fetched(followers) ⇒ followers.toString
              case _                  ⇒ "not yet"
            }
          }
        }
      }
  }

  WebServer.startServer("localhost", 8080)
}
