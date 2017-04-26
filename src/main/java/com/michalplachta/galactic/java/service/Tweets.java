package com.michalplachta.galactic.java.service;

import com.michalplachta.galactic.java.db.DbClient;
import com.michalplachta.galactic.java.values.Citizen;
import com.michalplachta.galactic.java.values.Tweet;
import javaslang.collection.HashMap;
import javaslang.collection.List;
import javaslang.collection.Map;
import javaslang.concurrent.Future;

public class Tweets {
    private static Map<String, List<Tweet>> cachedTweets = HashMap.empty();

    public static List<Tweet> getTweetsFor(String citizenName) {
        updateTweetsCacheAsync(citizenName);
        return cachedTweets.get(citizenName).getOrElse(List.empty());
    }

    private static void updateTweetsCacheAsync(String name) {
        Future<? extends Citizen> futureCitizen = DbClient.findCitizenByName(name);
        Future<List<Tweet>> futureTweets = futureCitizen.flatMap(DbClient::getTweetsFor);
        futureTweets.forEach(newTweets -> {
            cachedTweets = cachedTweets.put(name, newTweets);
        });
    }
}
