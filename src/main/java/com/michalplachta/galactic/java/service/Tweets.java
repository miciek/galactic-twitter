package com.michalplachta.galactic.java.service;

import com.michalplachta.galactic.java.db.DbClient;
import com.michalplachta.galactic.java.service.remotedata.*;
import com.michalplachta.galactic.java.values.Citizen;
import com.michalplachta.galactic.java.values.Tweet;
import javaslang.collection.HashMap;
import javaslang.collection.List;
import javaslang.collection.Map;
import javaslang.concurrent.Future;

import static javaslang.API.*;
import static javaslang.Patterns.Failure;
import static javaslang.Patterns.Success;

public class Tweets {
    private static Map<String, RemoteData<List<Tweet>>> cachedTweets = HashMap.empty();
    private static final int maxCensorshipManipulations = 2;

    public static RemoteData<List<Tweet>> getTweetsFor(String citizenName) {
        if(cachedTweets.get(citizenName).isEmpty()) cachedTweets = cachedTweets.put(citizenName, new Loading<>());
        getTweetsAsyncFor(citizenName)
            .map(Tweets::censorTweetsUsingFilters)
            .onComplete(triedTweets -> {
                RemoteData<List<Tweet>> remoteTweets = Match(triedTweets).of(
                        Case(Success($()), Fetched::new),
                        Case(Failure($()), ex -> new Failed<>(ex.toString()))
                );

                cachedTweets = cachedTweets.put(citizenName, remoteTweets);
            });
        return cachedTweets.get(citizenName).getOrElse(new NotRequestedYet<>());
    }

    // PROBLEM #5: Convoluted logic using IFs and vars
    private static List<Tweet> censorTweets(List<Tweet> tweets) {
        return tweets.map(t -> {
            Tweet tweet = t;
            int manipulations = 0;
            // TODO
            return t;
        });
    }

    private static List<Tweet> censorTweetsUsingFilters(List<Tweet> tweets) {
        return tweets;
    }

    private static Future<List<Tweet>> getTweetsAsyncFor(String citizenName) {
        Future<? extends Citizen> futureCitizen = DbClient.findCitizenByName(citizenName);
        return futureCitizen.flatMap(DbClient::getTweetsFor);
    }

}
