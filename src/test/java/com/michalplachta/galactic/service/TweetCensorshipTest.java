package com.michalplachta.galactic.service;

import com.michalplachta.galactic.java.logic.TweetCensorship.CensorFilter;
import com.michalplachta.galactic.java.values.Citizen;
import com.michalplachta.galactic.java.values.Jedi;
import com.michalplachta.galactic.java.values.Tweet;
import io.vavr.collection.List;
import io.vavr.test.Arbitrary;
import io.vavr.test.Property;
import org.junit.Test;

import static com.michalplachta.galactic.java.logic.TweetCensorship.censorTweetsUsingFilters;

public class TweetCensorshipTest {
    @Test
    public void emptyFiltersDoNotModifyTweets() {
        Arbitrary<Citizen> citizen = Arbitrary.integer().map(randomInt -> new Jedi("Jedi " + randomInt.toString()));
        Arbitrary<Tweet> tweet = Arbitrary.integer().flatMap(randomInt -> citizen.map(c -> new Tweet("Tweet " + randomInt.toString(), c)));
        Arbitrary<List<Tweet>> listOfTweets = Arbitrary.list(tweet);
        List<CensorFilter> emptyFilters = List.empty();

        Property.def("Empty filters do not modify tweets")
                .forAll(listOfTweets)
                .suchThat(tweets -> censorTweetsUsingFilters(emptyFilters).apply(tweets).equals(tweets))
                .check()
                .assertIsSatisfied();
    }
}
