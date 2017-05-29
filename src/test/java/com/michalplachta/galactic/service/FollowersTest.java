package com.michalplachta.galactic.service;

import com.michalplachta.galactic.java.values.Citizen;
import com.michalplachta.galactic.java.values.Stormtrooper;
import io.vavr.collection.List;
import io.vavr.test.Arbitrary;
import io.vavr.test.Property;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

import static com.michalplachta.galactic.java.logic.Followers.sumFollowers;
import static io.vavr.API.println;
import static org.junit.Assert.assertEquals;

public class FollowersTest {
    // PROBLEM #6: Writing tests by providing examples
    @Test
    public void listOfOneCloneAndTwoStormtroopersContainsTwoFollowers() {
        List<Stormtrooper> stormtroopers =
                List.of(new Stormtrooper("Clone 1", true),
                        new Stormtrooper("Stormtrooper 1", false),
                        new Stormtrooper("Stormtrooper 2", false));

        assertEquals(2, sumFollowers(stormtroopers));
    }

    @Test
    public void listOfClonesContainsZeroFollowers() {
        List<Stormtrooper> clones =
                List.range(0, 100).map(id -> new Stormtrooper("Clone " + id.toString(), true));

        assertEquals(0, sumFollowers(clones));
    }

    // SOLUTION #6: Defining properties and generating property-based tests
    @Test
    public void clonesAreNotCountedAsFollowers() {
        Arbitrary<Citizen> clone =
                Arbitrary.integer().map(id -> new Stormtrooper("Clone " + id.toString(), true));
        Arbitrary<Citizen> nonClone =
                Arbitrary.integer().map(id -> new Stormtrooper("Stormtrooper " + id.toString(), false));
        Arbitrary<List<Citizen>> clones = Arbitrary.list(clone);
        Arbitrary<List<Citizen>> nonClones = Arbitrary.list(nonClone);

        Property.def("sumFollowers(clones + nonClones) = size(nonClones)")
                .forAll(clones, nonClones)
                .suchThat((c, nc) -> sumFollowers(c.appendAll(nc)) == sumFollowers(nc))
                .check()
                .assertIsSatisfied();
    }
}
