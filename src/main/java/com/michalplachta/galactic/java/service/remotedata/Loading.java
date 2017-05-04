package com.michalplachta.galactic.java.service.remotedata;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.function.Function;

@JsonTypeName("Loading")
public class Loading<A> implements RemoteData<A> {
    @Override
    public <U> RemoteData<U> map(Function<? super A, ? extends U> f) {
        return new Loading<>();
    }
}

