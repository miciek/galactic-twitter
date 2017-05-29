package com.michalplachta.galactic.java.livecoding;

import com.michalplachta.galactic.java.console.GalacticFollowersApp;
import com.michalplachta.galactic.java.db.DbClient;
import com.michalplachta.galactic.java.service.FollowersService;
import com.michalplachta.galactic.java.values.Citizen;
import io.vavr.API;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Traversable;
import io.vavr.concurrent.Future;
import io.vavr.control.Try;

import java.util.Scanner;

import static io.vavr.API.*;
import static io.vavr.Patterns.$Failure;
import static io.vavr.Patterns.$Success;
import static java.lang.String.format;

/**
 * Live coding solution for PROBLEM #2: not handling failures.
 * Note: this class contains duplicate code and is only used for presentation purposes.
 *
 * @see FollowersService
 * @see GalacticFollowersApp
 */
class TryLiveCoding {
    private static Map<String, Integer> cachedFollowers = HashMap.empty();

    private static Future<Integer> getFollowersAsync(String citizenName) {
        Future<? extends Citizen> futureCitizen = DbClient.getCitizenByName(citizenName);
        return futureCitizen
                .flatMap(DbClient::getFollowers)
                .map(Traversable::size);
    }

//    private static Integer getFollowersSync(String citizenName) {
//        Citizen citizen = DbClient.getCitizenByName(citizenName).get();
//        return DbClient.getFollowers(citizen).get().size();
//    }

    public static void main(String[] args) {
        System.out.println("---\nEnter Citizen's name: ");
        Scanner scanner = new Scanner(System.in);
        String citizenName = scanner.nextLine();
        System.out.println(format("Getting followers for %s", citizenName));

        Future<Integer> futureFollowers = getFollowersAsync(citizenName);
        futureFollowers.forEach(followers ->
                cachedFollowers = cachedFollowers.put(citizenName, followers));

        String followersText = cachedFollowers.get(citizenName).toString();
        System.out.println(format("%s has %s followers!", citizenName, followersText));

        // run again!
        main(args);
    }
}
