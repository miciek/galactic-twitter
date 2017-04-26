package com.michalplachta.galactic.java.service;

import com.michalplachta.galactic.java.db.DbClient;
import com.michalplachta.galactic.java.values.Citizen;
import javaslang.collection.HashMap;
import javaslang.collection.List;
import javaslang.collection.Map;
import javaslang.concurrent.Future;
import javaslang.control.Option;

public class Followers {
    private static Map<String, Integer> cachedFollowers = HashMap.empty();

    // PROBLEM #1: treating 0 as "no value yet"
    public static Integer getFollowers(String name) {
        updateFollowersCacheAsync(name);
        return cachedFollowers.get(name).getOrElse(0);
    }

    // SOLUTION #1: explicit return type
    // PROBLEM #2: not handling failures
    public static Option<Integer> getCachedFollowers(String name) {
        updateFollowersCacheAsync(name);
        return cachedFollowers.get(name);
    }

    private static void updateFollowersCacheAsync(String name) {
        Future<? extends Citizen> futureCitizen = DbClient.findCitizenByName(name);
        Future<List<? extends Citizen>> followersFuture = futureCitizen.flatMap(DbClient::getFollowers);
        followersFuture.forEach(newFollowers -> {
            cachedFollowers = cachedFollowers.put(name, newFollowers.length());
        });
    }
}
