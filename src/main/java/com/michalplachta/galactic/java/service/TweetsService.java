package com.michalplachta.galactic.java.service;

import com.michalplachta.galactic.java.db.DbClient;
import com.michalplachta.galactic.java.logic.TweetCensorship;
import com.michalplachta.galactic.java.values.remotedata.*;
import com.michalplachta.galactic.java.values.Citizen;
import com.michalplachta.galactic.java.values.Tweet;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.concurrent.Future;

import static io.vavr.API.*;
import static io.vavr.Patterns.$Failure;
import static io.vavr.Patterns.$Success;

public class TweetsService {
    private static Map<String, RemoteData<List<Tweet>>> cachedTweets = HashMap.empty();

    public static RemoteData<List<Tweet>> getTweetsFor(String citizenName) {
        if (cachedTweets.get(citizenName).isEmpty()) cachedTweets = cachedTweets.put(citizenName, new Loading<>());
        getTweetsAsync(citizenName)
                .map(TweetCensorship::censorTweetsUsingFilters)
                .onComplete(triedTweets -> {
                    RemoteData<List<Tweet>> remoteTweets = Match(triedTweets).of(
                            Case($Success($()), Fetched::new),
                            Case($Failure($()), ex -> new Failed<>(ex.toString()))
                    );

                    cachedTweets = cachedTweets.put(citizenName, remoteTweets);
                });
        return cachedTweets.get(citizenName).getOrElse(new NotRequestedYet<>());
    }

    private static Future<List<Tweet>> getTweetsAsync(String citizenName) {
        Future<? extends Citizen> futureCitizen = DbClient.getCitizenByName(citizenName);
        return futureCitizen.flatMap(DbClient::getTweetsFor);
    }
}
