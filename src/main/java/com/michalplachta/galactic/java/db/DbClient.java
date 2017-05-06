package com.michalplachta.galactic.java.db;

import com.michalplachta.galactic.java.values.Citizen;
import com.michalplachta.galactic.java.values.Tweet;
import javaslang.collection.List;
import javaslang.concurrent.Future;
import javaslang.concurrent.Promise;

import static com.michalplachta.galactic.java.db.FakeData.*;
import static javaslang.API.*;

/**
 * A simulation of a client for a very slow database. Some calls may also fail with an exception ;)
 */
public class DbClient {
    public static Future<? extends Citizen> getCitizenByName(String citizenName) {
        return citizens.find(citizen -> citizen.name.equals(citizenName))
                .map(DbClient::simulateResponse)
                .getOrElse(simulateBadRequest(String.format("citizen with name %s couldn't be found", citizenName)));
    }

    public static Future<List<? extends Citizen>> getFollowers(Citizen citizen) {
        List<Citizen> followers = Match(citizen.name).of(
                Case($("Darth Vader"), () -> siths.appendAll(clones)),
                Case($("Luke Skywalker"), () -> jedis.appendAll(rebels)),
                Case($(), () -> citizens.slice(citizens.indexOf(citizen), citizens.length()))
        );
        return simulateResponse(followers);
    }

    public static Future<List<Tweet>> getTweetsFor(Citizen citizen) {
        List<Tweet> tweetsForCitizen = tweets.filter(t -> !t.author.equals(citizen));
        return simulateResponse(tweetsForCitizen);
    }

    private static <R> Future<R> simulateResponse(R result) {
        Promise<R> promise = Promise.make();
        Future.run(() -> {
            Thread.sleep(4000);
            promise.success(result);
        });
        return promise.future();
    }

    private static <R> Future<R> simulateBadRequest(String error) {
        Promise<R> promise = Promise.make();
        Future.run(() -> {
            Thread.sleep(2000);
            promise.failure(new Exception("Bad Request: " + error));
        });
        return promise.future();
    }
}
