package com.michalplachta.galactic.java.values.remotedata;

import java.util.function.Function;

public class Failed<A> implements RemoteData<A> {
    public final String errorMessage;

    public Failed(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public <U> RemoteData<U> map(Function<? super A, ? extends U> f) {
        return new Failed<>(this.errorMessage);
    }
}

