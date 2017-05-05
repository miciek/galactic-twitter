package com.michalplachta.galactic.java.values.remotedata;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.function.Function;

@JsonTypeName("Fetched")
public class Fetched<A> implements RemoteData<A> {
    public final A data;

    public Fetched(A data) {
        this.data = data;
    }

    @Override
    public <U> RemoteData<U> map(Function<? super A, ? extends U> f) {
        return new Fetched<>(f.apply(this.data));
    }
}

