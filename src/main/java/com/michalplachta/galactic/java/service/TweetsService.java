package com.michalplachta.galactic.java.service;

import com.michalplachta.galactic.java.db.DbClient;
import com.michalplachta.galactic.java.logic.TweetCensorship;
import com.michalplachta.galactic.java.values.remotedata.*;
import com.michalplachta.galactic.java.values.Citizen;
import com.michalplachta.galactic.java.values.Tweet;
import javaslang.collection.HashMap;
import javaslang.collection.List;
import javaslang.collection.Map;
import javaslang.concurrent.Future;

import static javaslang.API.*;
import static javaslang.Patterns.Failure;
import static javaslang.Patterns.Success;

public class TweetsService {
    private static Map<String, RemoteData<List<Tweet>>> cachedTweets = HashMap.empty();

    public static RemoteData<List<Tweet>> getTweetsFor(String citizenName) {
        if (cachedTweets.get(citizenName).isEmpty()) cachedTweets = cachedTweets.put(citizenName, new Loading<>());
        getTweetsAsync(citizenName)
                .map(TweetCensorship::censorTweetsUsingFilters)
                .onComplete(triedTweets -> {
                    RemoteData<List<Tweet>> remoteTweets = Match(triedTweets).of(
                            Case(Success($()), Fetched::new),
                            Case(Failure($()), ex -> new Failed<>(ex.toString()))
                    );

                    cachedTweets = cachedTweets.put(citizenName, remoteTweets);
                });
        return cachedTweets.get(citizenName).getOrElse(new NotRequestedYet<>());
    }

    private static Future<List<Tweet>> getTweetsAsync(String citizenName) {
        Future<? extends Citizen> futureCitizen = DbClient.findCitizenByName(citizenName);
        return futureCitizen.flatMap(DbClient::getTweetsFor);
    }
}
