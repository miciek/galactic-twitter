package com.michalplachta.galactic.java.logic;

import com.michalplachta.galactic.java.values.Tweet;
import io.vavr.Function1;

import static com.michalplachta.galactic.java.internal.CitizenPatterns.*;
import static io.vavr.API.*;

class CensorshipFunctions {
    static boolean isProLightSide(Tweet tweet) {
        return tweet.text.split("the Force").length > tweet.text.split("Dark Side").length;
    }

    static boolean isProDarkSide(Tweet tweet) {
        return tweet.text.split("the Force").length < tweet.text.split("Dark Side").length;
    }

    static boolean isProEmpire(Tweet tweet) {
        return Match(tweet.author).of(
                Case(Sith($()), true),
                Case(Stormtrooper($(), $()), true),
                Case($(), false)
        );
    }

    static boolean isProRebellion(Tweet tweet) {
        return Match(tweet.author).of(
                Case(Jedi($()), true),
                Case(Rebel($()), true),
                Case($(), false)
        );
    }

    static boolean isGeneralWisdom(Tweet tweet) {
        return tweet.author.name.equals("Yoda");
    }

    static boolean isJoke(Tweet tweet) {
        return tweet.author.name.equals("Han Solo");
    }

    static Tweet addMoreDarkSide(Tweet tweet) {
        return new Tweet(tweet.text + " Follow the Dark Side!", tweet.author);
    }

    static Tweet hailEmpire(Tweet tweet) {
        return new Tweet(tweet.text + " Empire Strikes Back!", tweet.author);
    }

    static Tweet sacrificeForEmpire(Tweet tweet) {
        return new Tweet(tweet.text + " For the Order and the Republic, I will give anything and everything, including my life.", tweet.author);
    }

    static Tweet trashRebellion(Tweet tweet) {
        return new Tweet(tweet.text + " Crash the Resistance! Republic will die!", tweet.author);
    }

    static Tweet makeJoke(Tweet tweet) {
        return new Tweet(tweet.text + " And I'm not really interested in your opinion, 3PO.", tweet.author);
    }

    static Tweet addMoreForce(Tweet tweet) {
        return new Tweet("May the Force be with you! " + tweet.text, tweet.author);
    }

    static Tweet addEvenMoreForce(Tweet tweet) {
        return new Tweet(tweet.text + " I'm one with the Force, the Force is with me.", tweet.author);
    }

    static Tweet replaceForceWithDarkSide(Tweet tweet) {
        return new Tweet(tweet.text.replaceAll("Force", "Dark Side"), tweet.author);
    }

    // to make Java version of Tweets service less verbose:
    static final Function1<Tweet, Boolean> isProLightSide = CensorshipFunctions::isProLightSide;
    static final Function1<Tweet, Boolean> isProDarkSide = CensorshipFunctions::isProDarkSide;
    static final Function1<Tweet, Boolean> isProEmpire = CensorshipFunctions::isProEmpire;
    static final Function1<Tweet, Boolean> isProRebellion = CensorshipFunctions::isProRebellion;
    static final Function1<Tweet, Boolean> isGeneralWisdom = CensorshipFunctions::isGeneralWisdom;
    static final Function1<Tweet, Boolean> isJoke = CensorshipFunctions::isJoke;
    static final Function1<Tweet, Tweet> addMoreDarkSide = CensorshipFunctions::addMoreDarkSide;
    static final Function1<Tweet, Tweet> hailEmpire = CensorshipFunctions::hailEmpire;
    static final Function1<Tweet, Tweet> sacrificeForEmpire = CensorshipFunctions::sacrificeForEmpire;
    static final Function1<Tweet, Tweet> trashRebellion = CensorshipFunctions::trashRebellion;
    static final Function1<Tweet, Tweet> makeJoke = CensorshipFunctions::makeJoke;
    static final Function1<Tweet, Tweet> addMoreForce = CensorshipFunctions::addMoreForce;
    static final Function1<Tweet, Tweet> addEvenMoreForce = CensorshipFunctions::addEvenMoreForce;
    static final Function1<Tweet, Tweet> replaceForceWithDarkSide = CensorshipFunctions::replaceForceWithDarkSide;
}
