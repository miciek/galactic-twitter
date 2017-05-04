package com.michalplachta.galactic.java.http;

import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import com.michalplachta.galactic.java.service.Followers;
import com.michalplachta.galactic.java.service.Tweets;
import com.michalplachta.galactic.java.service.remotedata.RemoteData;
import com.michalplachta.galactic.java.values.Tweet;
import javaslang.Value;

import java.util.List;
import java.util.function.Function;

import static akka.http.javadsl.server.Directives.*;
import static akka.http.javadsl.unmarshalling.StringUnmarshallers.STRING;

public class ServerApp {
    private static Route appRoute() {
        Function<String, Route> getFollowers = citizenName -> {
            RemoteData<Integer> followers = Followers.getFollowers(citizenName);
            return complete(StatusCodes.OK, followers, Jackson.marshaller());
        };

        Function<String, Route> getTweets = citizenName -> {
            RemoteData<List<Tweet>> followers = Tweets.getTweetsFor(citizenName).map(Value::toJavaList);
            return complete(StatusCodes.OK, followers, Jackson.marshaller());
        };

        return route(
                pathPrefix("followers",
                        () -> path(STRING,
                                citizenName -> route(get(() -> getFollowers.apply(citizenName)))
                        )
                ),
                pathPrefix("tweets",
                        () -> path(STRING,
                                citizenName -> route(get(() -> getTweets.apply(citizenName)))
                        )
                ));
    }

    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create();
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        final ConnectHttp host = ConnectHttp.toHost("localhost:8080");

        Http.get(system).bindAndHandle(appRoute().flow(system, materializer), host, materializer);

        System.console().readLine("Type RETURN to exit...");
        system.terminate();
    }
}
