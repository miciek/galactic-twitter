package com.michalplachta.galactic.java.service;

import com.michalplachta.galactic.java.db.DbClient;
import com.michalplachta.galactic.java.logic.Followers;
import com.michalplachta.galactic.java.values.Citizen;
import com.michalplachta.galactic.java.values.remotedata.*;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.concurrent.Future;
import io.vavr.control.Option;
import io.vavr.control.Try;

import static io.vavr.API.*;
import static io.vavr.Patterns.$Failure;
import static io.vavr.Patterns.$Success;

public class FollowersService {
    public static class Version1 {
        private static Map<String, Integer> cachedFollowers = HashMap.empty();

        // PROBLEM #1: treating 0 as "no value yet"
        public static Integer getFollowers(String citizenName) {
            getFollowersAsync(citizenName).forEach(followers -> {
                cachedFollowers = cachedFollowers.put(citizenName, followers);
            });
            return cachedFollowers.get(citizenName).getOrElse(0);
        }
    }

    public static class Version2 {
        private static Map<String, Integer> cachedFollowers = HashMap.empty();

        // SOLUTION #1: explicit return type
        // PROBLEM #2: not handling failures
        public static Option<Integer> getCachedFollowers(String citizenName) {
            getFollowersAsync(citizenName).forEach(followers -> {
                cachedFollowers = cachedFollowers.put(citizenName, followers);
            });
            return cachedFollowers.get(citizenName);
        }
    }

    public static class Version3 {
        private static Map<String, Try<Integer>> cachedTriedFollowers = HashMap.empty();

        // SOLUTION #2: explicit return type
        // PROBLEM #3: cryptic return type
        public static Option<Try<Integer>> getCachedTriedFollowers(String citizenName) {
            getFollowersAsync(citizenName).onComplete(triedFollowers -> {
                cachedTriedFollowers = cachedTriedFollowers.put(citizenName, triedFollowers);
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

        // SOLUTION #3: use Algebraic Data Types to describe states
        public static RemoteFollowersData getRemoteFollowers(String citizenName) {
            if (cachedRemoteFollowers.get(citizenName).isEmpty())
                cachedRemoteFollowers = cachedRemoteFollowers.put(citizenName, new Loading());
            getFollowersAsync(citizenName).onComplete(triedFollowers -> {
                RemoteFollowersData value = Match(triedFollowers).of(
                        Case($Success($()), Fetched::new),
                        Case($Failure($()), ex -> new Failed(ex.toString()))
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
        getFollowersAsync(citizenName).onComplete(triedFollowers -> {
            RemoteData<Integer> value = Match(triedFollowers).of(
                    Case($Success($()), Fetched<Integer>::new),
                    Case($Failure($()), ex -> new Failed<Integer>(ex.toString()))
            );
            cache = cache.put(citizenName, value);
        });
        return cache.get(citizenName).getOrElse(new NotRequestedYet<Integer>());
    }

    private static Future<Integer> getFollowersAsync(String citizenName) {
        Future<? extends Citizen> futureCitizen = DbClient.getCitizenByName(citizenName);
        return futureCitizen
                .flatMap(DbClient::getFollowers)
                .map(Followers::sumFollowers);
    }
}
