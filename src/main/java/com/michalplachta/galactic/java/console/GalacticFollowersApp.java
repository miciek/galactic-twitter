package com.michalplachta.galactic.java.console;

import com.michalplachta.galactic.java.service.FollowersService;
import com.michalplachta.galactic.java.service.FollowersService.Version4.RemoteFollowersData;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.Scanner;
import java.util.function.Function;

import static com.michalplachta.galactic.java.internal.RemoteFollowersDataPatterns.*;
import static io.vavr.API.*;
import static io.vavr.Patterns.$Failure;
import static io.vavr.Patterns.$Success;

public class GalacticFollowersApp {
    private static void runFollowersApp(Function<String, String> getFollowersText) {
        System.out.println("---\nEnter Citizen's name: ");
        Scanner scanner = new Scanner(System.in);
        String citizenName = scanner.nextLine();
        System.out.println(String.format("Getting followers for %s", citizenName));
        String followersText = getFollowersText.apply(citizenName);
        System.out.println(String.format("%s has %s followers!", citizenName, followersText));
        runFollowersApp(getFollowersText);
    }

    private static String getAndDescribe(String citizenName) {
        return FollowersService.Version1.getFollowers(citizenName).toString();
    }

    private static String getAndDescribeUsingCache(String citizenName) {
        Option<Integer> cachedFollowers = FollowersService.Version2.getCachedFollowers(citizenName);
        return cachedFollowers.map(Object::toString).getOrElse("(loading...)");
    }

    private static String getAndDescribeUsingCacheWithFailures(String citizenName) {
        Option<Try<Integer>> cachedTriedFollowers = FollowersService.Version3.getCachedTriedFollowers(citizenName);
        return cachedTriedFollowers.map(triedFollowers -> Match(triedFollowers).of(
            Case($Success($()), Object::toString),
            Case($Failure($()), errorMessage -> String.format("(failed to get followers: %s)", errorMessage))
        )).getOrElse("(loading...)");
    }

    private static String getAndDescribeUsingADTs(String citizenName) {
        RemoteFollowersData remoteFollowers = FollowersService.Version4.getRemoteFollowers(citizenName);
        return Match(remoteFollowers).of(
                Case(NotRequestedYet(), "(not requested yet)"),
                Case(Loading(), "(loading...)"),
                Case(Fetched($()), Object::toString),
                Case(Failed($()), errorMessage -> String.format("(failed to get followers: %s)", errorMessage))
        );
    }

    public static void main(String[] args){
        runFollowersApp(GalacticFollowersApp::getAndDescribeUsingADTs);
    }
}
