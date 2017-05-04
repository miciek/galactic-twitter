package com.michalplachta.galactic.java.service;

import com.michalplachta.galactic.java.values.Tweet;
import javaslang.Function1;

import java.security.cert.CertPathBuilderSpi;

import static com.michalplachta.galactic.java.internal.CitizenPatterns.*;
import static javaslang.API.*;

public class Censorship {
    public static boolean isProLightSide(Tweet tweet) {
        return tweet.text.split("the Force").length > tweet.text.split("Dark Side").length;
    }

    public static boolean isProDarkSide(Tweet tweet) {
        return tweet.text.split("the Force").length < tweet.text.split("Dark Side").length;
    }

    public static boolean isProEmpire(Tweet tweet) {
        return Match(tweet.author).of(
                Case(Sith($()), true),
                Case(Stormtrooper($(), $()), true),
                Case($(), false)
        );
    }

    public static boolean isProRebellion(Tweet tweet) {
        return Match(tweet.author).of(
                Case(Jedi($()), true),
                Case(Rebel($()), true),
                Case($(), false)
        );
    }

    public static boolean isGeneralWisdom(Tweet tweet) {
        return tweet.author.name.equals("Yoda");
    }

    public static boolean isJoke(Tweet tweet) {
        return tweet.author.name.equals("Han Solo");
    }

    public static Tweet addMoreDarkSide(Tweet tweet) {
        return new Tweet(tweet.text + " Follow the Dark Side!", tweet.author);
    }

    public static Tweet hailEmpire(Tweet tweet) {
        return new Tweet(tweet.text + " Empire Strikes Back!", tweet.author);
    }

    public static Tweet sacrificeForEmpire(Tweet tweet) {
        return new Tweet(tweet.text + " For the Order and the Republic, I will give anything and everything, including my life.", tweet.author);
    }

    public static Tweet trashRebellion(Tweet tweet) {
        return new Tweet(tweet.text + " Crash the Resistance! Republic will die!", tweet.author);
    }

    public static Tweet makeJoke(Tweet tweet) {
        return new Tweet(tweet.text + " And I'm not really interested in your opinion, 3PO.", tweet.author);
    }

    public static Tweet addMoreForce(Tweet tweet) {
        return new Tweet("May the Force be with you! " + tweet.text, tweet.author);
    }

    public static Tweet addEvenMoreForce(Tweet tweet) {
        return new Tweet(tweet.text + " I'm one with the Force, the Force is with me.", tweet.author);
    }

    public static Tweet replaceForceWithDarkSide(Tweet tweet) {
        return new Tweet(tweet.text.replaceAll("Force", "Dark Side"), tweet.author);
    }

    // to make Java version of Tweets service less verbose:
    public static final Function1<Tweet, Boolean> isProLightSide = Censorship::isProLightSide;
    public static final Function1<Tweet, Boolean> isProDarkSide = Censorship::isProDarkSide;
    public static final Function1<Tweet, Boolean> isProEmpire = Censorship::isProEmpire;
    public static final Function1<Tweet, Boolean> isProRebellion = Censorship::isProRebellion;
    public static final Function1<Tweet, Boolean> isGeneralWisdom = Censorship::isGeneralWisdom;
    public static final Function1<Tweet, Boolean> isJoke = Censorship::isJoke;
    public static final Function1<Tweet, Tweet> addMoreDarkSide = Censorship::addMoreDarkSide;
    public static final Function1<Tweet, Tweet> hailEmpire = Censorship::hailEmpire;
    public static final Function1<Tweet, Tweet> sacrificeForEmpire = Censorship::sacrificeForEmpire;
    public static final Function1<Tweet, Tweet> trashRebellion = Censorship::trashRebellion;
    public static final Function1<Tweet, Tweet> makeJoke = Censorship::makeJoke;
    public static final Function1<Tweet, Tweet> addMoreForce = Censorship::addMoreForce;
    public static final Function1<Tweet, Tweet> addEvenMoreForce = Censorship::addEvenMoreForce;
    public static final Function1<Tweet, Tweet> replaceForceWithDarkSide = Censorship::replaceForceWithDarkSide;
}
