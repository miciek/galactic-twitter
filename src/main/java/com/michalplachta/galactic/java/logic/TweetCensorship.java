package com.michalplachta.galactic.java.logic;

import com.michalplachta.galactic.java.values.Tweet;
import javaslang.collection.List;

import java.util.function.Function;

import static com.michalplachta.galactic.java.internal.PredicateLogic.and;
import static com.michalplachta.galactic.java.internal.PredicateLogic.not;
import static com.michalplachta.galactic.java.logic.CensorshipFunctions.*;

public class TweetCensorship {
    private static final int maxCensorshipManipulations = 2;

    // PROBLEM #5: Convoluted logic using IFs and vars
    public static List<Tweet> censorTweet(List<Tweet> tweets) {
        return tweets.map(originalTweet -> {
            Tweet tweet = originalTweet;
            int manipulations = 0;
            if (isProDarkSide(tweet)) {
                tweet = addMoreDarkSide(tweet);
                manipulations += 1;
            }
            if (isProLightSide(tweet) && manipulations < maxCensorshipManipulations) {
                tweet = replaceForceWithDarkSide(tweet);
                manipulations += 1;
            }
            if (isGeneralWisdom(tweet) && manipulations < maxCensorshipManipulations) {
                tweet = addMoreDarkSide(replaceForceWithDarkSide(tweet));
                manipulations += 1;
            }
            if (isProRebellion(tweet) && !isJoke(tweet) && manipulations < maxCensorshipManipulations) {
                tweet = hailEmpire(tweet);
                manipulations += 1;
            }
            if (isProEmpire(tweet) && isJoke(tweet) && manipulations < maxCensorshipManipulations) {
                tweet = trashRebellion(tweet);
                manipulations += 1;
            }
            if (!isProRebellion(tweet) && !isProEmpire(tweet) && manipulations < maxCensorshipManipulations) {
                tweet = makeJoke(addEvenMoreForce(addMoreForce(tweet)));
                manipulations += 1;
            }
            if (isProEmpire(tweet) && manipulations < maxCensorshipManipulations) {
                tweet = addMoreDarkSide(tweet);
                manipulations += 1;
            }
            if (manipulations < maxCensorshipManipulations) {
                tweet = sacrificeForEmpire(tweet);
                manipulations += 1;
            }
            return tweet;
        });
    }

    private static class CensorFilter {
        final Function<Tweet, Boolean> shouldManipulate;
        final Function<Tweet, Tweet> manipulate;

        private CensorFilter(Function<Tweet, Boolean> shouldManipulate, Function<Tweet, Tweet> manipulate) {
            this.shouldManipulate = shouldManipulate;
            this.manipulate = manipulate;
        }

        private CensorFilter(Function<Tweet, Tweet> manipulate) {
            this.shouldManipulate = t -> true;
            this.manipulate = manipulate;
        }
    }

    private static class CensorStatus {
        final Tweet tweet;
        final int manipulations;

        private CensorStatus(Tweet tweet, int manipulations) {
            this.tweet = tweet;
            this.manipulations = manipulations;
        }
    }

    private static final List<CensorFilter> censorFilters = List.of(
            new CensorFilter(isProDarkSide, addMoreDarkSide),
            new CensorFilter(isProLightSide, replaceForceWithDarkSide),
            new CensorFilter(isGeneralWisdom, replaceForceWithDarkSide.andThen(addMoreDarkSide)),
            new CensorFilter(and(isProRebellion, not(isJoke)), hailEmpire),
            new CensorFilter(and(isProEmpire, isJoke), trashRebellion),
            new CensorFilter(and(not(isProRebellion), not(isProEmpire)), addMoreForce.andThen(addEvenMoreForce).andThen(makeJoke)),
            new CensorFilter(isProEmpire, addMoreDarkSide),
            new CensorFilter(sacrificeForEmpire)
    );

    // SOLUTION #5: Logic as data, accumulator as state, folding data to run the logic
    public static List<Tweet> censorTweetsUsingFilters(List<Tweet> tweets) {
        return tweets.map(originalTweet -> {
            CensorStatus initialStatus = new CensorStatus(originalTweet, 0);
            return censorFilters.foldLeft(initialStatus, (status, filter) -> {
                if(filter.shouldManipulate.apply(status.tweet) && status.manipulations < maxCensorshipManipulations)
                    return new CensorStatus(filter.manipulate.apply(status.tweet), status.manipulations + 1);
                else
                    return status;
            });
        }).map(status -> status.tweet);
    }
}
