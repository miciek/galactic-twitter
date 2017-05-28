package com.michalplachta.galactic.service;

import com.michalplachta.galactic.java.logic.TweetCensorship.CensorFilter;
import com.michalplachta.galactic.java.values.*;
import io.vavr.collection.List;
import io.vavr.test.Arbitrary;
import io.vavr.test.Property;
import org.junit.Test;

import java.util.function.Function;

import static com.michalplachta.galactic.java.logic.TweetCensorship.censorTweetsUsingFilters;

public class TweetCensorshipTest {
    @Test
    public void emptyFiltersDoNotManipulateTweets() {
        Arbitrary<Tweet> aTweet = Arbitrary.integer().flatMap(randomInt -> aCitizen().map(c -> new Tweet("Tweet " + randomInt.toString(), c)));
        Arbitrary<List<Tweet>> listOfTweets = Arbitrary.list(aTweet);

        List<CensorFilter> emptyFilters = List.empty();

        Property.def("Empty filters do not manipulate tweets")
                .forAll(listOfTweets)
                .suchThat(tweets -> censorTweetsUsingFilters(emptyFilters).apply(tweets).equals(tweets))
                .check()
                .assertIsSatisfied();
    }

    @Test
    public void filtersWithFalseConditionDoNotManipulateTweets() {
        Arbitrary<Tweet> aTweet = Arbitrary.integer().flatMap(randomInt -> aCitizen().map(c -> new Tweet("Tweet " + randomInt.toString(), c)));
        Arbitrary<List<Tweet>> listOfTweets = Arbitrary.list(aTweet);

        Arbitrary<CensorFilter> aFalseFilter =
                Arbitrary.integer()
                         .map(randomInt -> "false" + randomInt.toString())
                         .map(appendValue -> new CensorFilter(t -> false, t -> new Tweet(t.text + appendValue, t.author)));
        Arbitrary<List<CensorFilter>> listOfFalseFilters = Arbitrary.list(aFalseFilter);

        Property.def("Filters with false condition do not manipulate tweets")
                .forAll(listOfTweets, listOfFalseFilters)
                .suchThat((tweets, falseFilters) -> censorTweetsUsingFilters(falseFilters).apply(tweets).equals(tweets))
                .check()
                .assertIsSatisfied();
    }

    @Test
    public void twoFiltersWithTrueConditionAlwaysManipulateTweets() {
        Function<Tweet, Tweet> repeatLastCharacter = t -> new Tweet(t.text + t.text.charAt(t.text.length() - 1), t.author);
        Function<Tweet, Tweet> repeatFirstCharacter = t -> new Tweet(t.text.charAt(0) + t.text, t.author);

        Arbitrary<Tweet> aTweet = Arbitrary.integer().flatMap(randomInt -> aCitizen().map(c -> new Tweet("Tweet " + randomInt.toString(), c)));
        Arbitrary<List<Tweet>> listOfTweets = Arbitrary.list(aTweet);
        Function<List<Tweet>, List<Tweet>> manipulated = tweets -> tweets.map(repeatFirstCharacter).map(repeatLastCharacter);

        List<CensorFilter> censorFilters = List.of(new CensorFilter(t -> true, repeatLastCharacter), new CensorFilter(t -> true, repeatFirstCharacter));

        Property.def("Two filters with true condition always manipulate tweets")
                .forAll(listOfTweets)
                .suchThat(tweets -> censorTweetsUsingFilters(censorFilters).apply(tweets).equals(manipulated.apply(tweets)))
                .check()
                .assertIsSatisfied();
    }

    private static Arbitrary<Citizen> aCitizen() {
        Arbitrary<Citizen> civilian = Arbitrary.integer().map(randomInt -> new Civilian("Civilian " + randomInt.toString()));
        Arbitrary<Citizen> jedi = Arbitrary.integer().map(randomInt -> new Jedi("Jedi " + randomInt.toString()));
        Arbitrary<Citizen> rebel = Arbitrary.integer().map(randomInt -> new Rebel("Rebel " + randomInt.toString()));
        Arbitrary<Citizen> sith = Arbitrary.integer().map(randomInt -> new Sith("Sith " + randomInt.toString()));
        Arbitrary<Citizen> clone = Arbitrary.integer().map(randomInt -> new Stormtrooper("Clone " + randomInt.toString(), true));
        Arbitrary<Citizen> stormtrooper = Arbitrary.integer().map(randomInt -> new Stormtrooper("Stormtrooper " + randomInt.toString(), false));
        return civilian.intersperse(jedi).intersperse(rebel).intersperse(sith).intersperse(clone).intersperse(stormtrooper);
    }
}
