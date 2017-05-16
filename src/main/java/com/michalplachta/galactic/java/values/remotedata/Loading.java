package com.michalplachta.galactic.java.values.remotedata;

import java.util.function.Function;

public class Loading<A> implements RemoteData<A> {
    @Override
    public <U> RemoteData<U> map(Function<? super A, ? extends U> f) {
        return new Loading<>();
    }
}

