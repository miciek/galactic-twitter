package com.michalplachta.galactic.java.internal;

import com.michalplachta.galactic.java.service.remotedata.*;
import javaslang.API;
import javaslang.Tuple;
import javaslang.Tuple1;

// NOTE: Some patterns can also be autogenerated using javaslang-match
public class RemoteDataPatterns {
    public static API.Match.Pattern0<? extends RemoteData> NotRequestedYet() {
        return API.Match.Pattern0.of(NotRequestedYet.class);
    }

    public static API.Match.Pattern0<? extends RemoteData> Loading() {
        return API.Match.Pattern0.of(Loading.class);
    }

    public static <A> Tuple1<A> Fetched(Fetched<A> fetched) {
        return Tuple.of(fetched.data);
    }

    public static <A> API.Match.Pattern1<? extends RemoteData, A> Fetched(API.Match.Pattern<A, ?> p1) {
        return API.Match.Pattern1.of(Fetched.class, p1, (Fetched<A> f) -> Fetched(f));
    }

    public static Tuple1<String> Failed(Failed failed) {
        return Tuple.of(failed.errorMessage);
    }

    public static API.Match.Pattern1<? extends RemoteData, String> Failed(API.Match.Pattern<String, ?> p1) {
        return API.Match.Pattern1.of(Failed.class, p1, RemoteDataPatterns::Failed);
    }
}
