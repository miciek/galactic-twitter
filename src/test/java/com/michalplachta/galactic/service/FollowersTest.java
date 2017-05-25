package com.michalplachta.galactic.service;

import com.michalplachta.galactic.java.values.Citizen;
import com.michalplachta.galactic.java.values.Stormtrooper;
import io.vavr.collection.List;
import io.vavr.test.Arbitrary;
import io.vavr.test.Property;
import org.junit.Test;

import static com.michalplachta.galactic.java.logic.Followers.sumFollowers;

public class FollowersTest {
    @Test
    public void clonesAreNotCountedAsFollowers() {
        Arbitrary<Citizen> clone = Arbitrary.integer().map(id -> new Stormtrooper("Clone " + id.toString(), true));
        Arbitrary<Citizen> nonClone = Arbitrary.integer().map(id -> new Stormtrooper("Stormtrooper " + id.toString(), false));
        Arbitrary<List<Citizen>> clones = Arbitrary.list(clone);
        Arbitrary<List<Citizen>> nonClones = Arbitrary.list(nonClone);

        Property.def("sumFollowers(clones + nonClones) = size(nonClones)")
                .forAll(clones, nonClones)
                .suchThat((c, nc) -> sumFollowers(c.appendAll(nc)) == sumFollowers(nc))
                .check()
                .assertIsSatisfied();
    }
}
