package com.michalplachta.galactic.java.internal;

import com.michalplachta.galactic.java.values.Citizen;
import com.michalplachta.galactic.java.values.Stormtrooper;
import javaslang.API;
import javaslang.Tuple;
import javaslang.Tuple2;

public class CitizenPatterns {
    public static Tuple2<String, Boolean> Stormtrooper(Stormtrooper stormtrooper) {
        return Tuple.of(stormtrooper.name, stormtrooper.isCloned);
    }
    public static <_1 extends String, _2 extends Boolean> API.Match.Pattern2<? extends Citizen, _1, _2> Stormtrooper(API.Match.Pattern<_1, ?> p1, API.Match.Pattern<_2, ?> p2) {
        return API.Match.Pattern2.of(Stormtrooper.class, p1, p2, CitizenPatterns::Stormtrooper);
    }
}
