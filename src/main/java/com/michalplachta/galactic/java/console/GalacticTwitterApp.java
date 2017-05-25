package com.michalplachta.galactic.java.console;

import com.michalplachta.galactic.java.service.FollowersService;
import com.michalplachta.galactic.java.service.TweetsService;
import com.michalplachta.galactic.java.values.remotedata.RemoteData;
import com.michalplachta.galactic.java.values.Tweet;
import io.vavr.collection.List;

import java.util.Scanner;

import static com.michalplachta.galactic.java.internal.RemoteDataPatterns.*;
import static io.vavr.API.*;

public class GalacticTwitterApp {
    private static void runConsoleTwitter() {
        System.out.println("---\nEnter Citizen's name: ");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        System.out.println(String.format("Getting followers for %s", name));
        String followersText = getFollowersText(name);
        String tweets = getTweetWall(name);
        System.out.println(String.format("%s has %s followers!", name, followersText));
        System.out.println(String.format("%s's Tweet Wall: %s", name, tweets));
        runConsoleTwitter();
    }

    private static String getFollowersText(String citizenName) {
        RemoteData remoteFollowers = FollowersService.getFollowers(citizenName);
        return Match(remoteFollowers).of(
                Case(NotRequestedYet(), "(not requested yet)"),
                Case(Loading(), "(loading...)"),
                Case(Fetched($()), Object::toString),
                Case(Failed($()), errorMessage -> String.format("(failed to get followers: %s)", errorMessage))
        );
    }

    private static String getTweetWall(String citizenName) {
        RemoteData remoteTweets = TweetsService.getTweetsFor(citizenName);
        return Match(remoteTweets).of(
                Case(NotRequestedYet(), "(not requested yet)"),
                Case(Loading(), "(loading...)"),
                Case(Fetched($()), (List<Tweet> tweets) -> tweets.foldLeft("", (wall, t) -> String.format("%s\n%s: %s", wall, t.author.name, t.text))),
                Case(Failed($()), errorMessage -> String.format("(failed to get tweets: %s)", errorMessage))
        );
    }

    public static void main(String[] args) {
        runConsoleTwitter();
    }
}
