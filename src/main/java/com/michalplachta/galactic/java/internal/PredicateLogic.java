package com.michalplachta.galactic.java.internal;

import io.vavr.Function1;

public class PredicateLogic {
    public static <T> Function1<T, Boolean> and(Function1<T, Boolean> f1, Function1<T, Boolean> f2) {
        return Function1.of(t -> f1.apply(t) && f2.apply(t));
    }

    public static <T> Function1<T, Boolean> not(Function1<T, Boolean> f) {
        return Function1.of(t -> !f.apply(t));
    }

    public static <T> Function1<T, Boolean> always() {
        return Function1.of(t -> true);
    }
}
