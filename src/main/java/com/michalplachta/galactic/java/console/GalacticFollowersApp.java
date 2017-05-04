package com.michalplachta.galactic.java.console;

import com.michalplachta.galactic.java.service.FollowersService;
import com.michalplachta.galactic.java.service.FollowersService.Version4.RemoteFollowersData;
import javaslang.control.Option;
import javaslang.control.Try;

import java.util.Scanner;
import java.util.function.Function;

import static javaslang.API.*;
import static javaslang.Patterns.Failure;
import static javaslang.Patterns.Success;
import static com.michalplachta.galactic.java.internal.RemoteFollowersDataPatterns.*;

public class GalacticFollowersApp {
    private static void runFollowersApp(Function<String, String> getFollowersDescription) {
        System.out.println("Enter Citizen's name: ");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        System.out.println(String.format("Getting followers for %s", name));
        String followersDescription = getFollowersDescription.apply(name);
        System.out.println(String.format("%s has %s followers!", name, followersDescription));
        runFollowersApp(getFollowersDescription);
    }

    private static String getAndDescribe(String name) {
        return FollowersService.Version1.getFollowers(name).toString();
    }

    private static String getAndDescribeUsingCache(String name) {
        Option<Integer> cachedFollowers = FollowersService.Version2.getCachedFollowers(name);
        return cachedFollowers.map(Object::toString).getOrElse("(not available)");
    }

    private static String getAndDescribeUsingCacheWithFailures(String name) {
        Option<Try<Integer>> cachedTriedFollowers = FollowersService.Version3.getCachedTriedFollowers(name);
        return cachedTriedFollowers.map(triedFollowers -> Match(triedFollowers).of(
            Case(Success($()), Object::toString),
            Case(Failure($()), errorMessage -> String.format("(failed to get followers: %s)", errorMessage))
        )).getOrElse("(not available)");
    }

    private static String getAndDescribeUsingADTs(String name) {
        RemoteFollowersData remoteFollowers = FollowersService.Version4.getRemoteFollowers(name);
        return Match(remoteFollowers).of(
                Case(NotRequestedYet(), "(not requested yet)"),
                Case(Loading(), "(loading...)"),
                Case(Fetched($()), Object::toString),
                Case(Failed($()), errorMessage -> String.format("(failed to get followers: %s)", errorMessage))
        );
    }

    public static void main(String[] args) {
        runFollowersApp(GalacticFollowersApp::getAndDescribeUsingADTs);
    }
}
