package com.michalplachta.galactic.java.logic;

import com.michalplachta.galactic.java.values.Citizen;
import javaslang.collection.List;

import static com.michalplachta.galactic.java.internal.CitizenPatterns.Stormtrooper;
import static javaslang.API.*;

public class Followers {
    // PROBLEM #4: clones are counted as followers
    public static int sumFollowersNaive(List<? extends Citizen> followers) {
        return followers.size();
    }

    // SOLUTION #4: Traversable + pattern matching
    public static int sumFollowers(List<? extends Citizen> followers) {
        return followers.count(f -> Match(f).of(
                Case(Stormtrooper($(), $(true)), false),
                Case($(), true)
        ));
    }
}
