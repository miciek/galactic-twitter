package com.michalplachta.galactic.java.service;

import com.michalplachta.galactic.java.db.DbClient;
import com.michalplachta.galactic.java.logic.Followers;
import com.michalplachta.galactic.java.values.remotedata.*;
import com.michalplachta.galactic.java.values.Citizen;
import javaslang.collection.HashMap;
import javaslang.collection.Map;
import javaslang.concurrent.Future;
import javaslang.control.Option;
import javaslang.control.Try;

import static javaslang.API.*;
import static javaslang.Patterns.Failure;
import static javaslang.Patterns.Success;

public class FollowersService {
    public static class Version1 {
        private static Map<String, Integer> cachedFollowers = HashMap.empty();

        // PROBLEM #1: treating 0 as "no value yet"
        public static Integer getFollowers(String citizenName) {
            getFollowersAsync(citizenName).forEach(result -> {
                cachedFollowers = cachedFollowers.put(citizenName, result);
            });
            return cachedFollowers.get(citizenName).getOrElse(0);
        }
    }

    public static class Version2 {
        private static Map<String, Integer> cachedFollowers = HashMap.empty();

        // SOLUTION #1: explicit return type
        // PROBLEM #2: not handling failures
        public static Option<Integer> getCachedFollowers(String citizenName) {
            getFollowersAsync(citizenName).forEach(result -> {
                cachedFollowers = cachedFollowers.put(citizenName, result);
            });
            return cachedFollowers.get(citizenName);
        }
    }

    public static class Version3 {
        private static Map<String, Try<Integer>> cachedTriedFollowers = HashMap.empty();

        // SOLUTION #2: explicit return type
        // PROBLEM #4: cryptic return type
        public static Option<Try<Integer>> getCachedTriedFollowers(String citizenName) {
            getFollowersAsync(citizenName).onComplete(result -> {
                cachedTriedFollowers = cachedTriedFollowers.put(citizenName, result);
            });
            return cachedTriedFollowers.get(citizenName);
        }
    }

    public static class Version4 {
        public interface RemoteFollowersData {}
        public static class NotRequestedYet implements RemoteFollowersData {}
        public static class Loading implements RemoteFollowersData {}
        public static class Failed implements RemoteFollowersData {
           public final String errorMessage;

            public Failed(String errorMessage) {
                this.errorMessage = errorMessage;
            }
        }
        public static class Fetched implements RemoteFollowersData {
            public final Integer followers;

            public Fetched(Integer followers) {
                this.followers = followers;
            }
        }

        private static Map<String, RemoteFollowersData> cachedRemoteFollowers = HashMap.empty();

        // SOLUTION #4: use Algebraic Data Types to describe states
        public static RemoteFollowersData getRemoteFollowers(String citizenName) {
            if (cachedRemoteFollowers.get(citizenName).isEmpty())
                cachedRemoteFollowers = cachedRemoteFollowers.put(citizenName, new Loading());
            getFollowersAsync(citizenName).onComplete(result -> {
                RemoteFollowersData value = Match(result).of(
                        Case(Success($()), Fetched::new),
                        Case(Failure($()), ex -> new Failed(ex.toString()))
                );
                cachedRemoteFollowers = cachedRemoteFollowers.put(citizenName, value);
            });
            return cachedRemoteFollowers.get(citizenName).getOrElse(new NotRequestedYet());
        }
    }

    // VERSION 5, final
    private static Map<String, RemoteData<Integer>> cache = HashMap.empty();

    public static RemoteData<Integer> getFollowers(String citizenName) {
        if (cache.get(citizenName).isEmpty())
            cache = cache.put(citizenName, new Loading<Integer>());
        getFollowersAsync(citizenName).onComplete(result -> {
            RemoteData<Integer> value = Match(result).of(
                    Case(Success($()), Fetched<Integer>::new),
                    Case(Failure($()), ex -> new Failed<Integer>(ex.toString()))
            );
            cache = cache.put(citizenName, value);
        });
        return cache.get(citizenName).getOrElse(new NotRequestedYet<Integer>());
    }

    private static Future<Integer> getFollowersAsync(String name) {
        Future<? extends Citizen> futureCitizen = DbClient.findCitizenByName(name);
        return futureCitizen.flatMap(DbClient::getFollowers).map(Followers::sumFollowers);
    }
}
