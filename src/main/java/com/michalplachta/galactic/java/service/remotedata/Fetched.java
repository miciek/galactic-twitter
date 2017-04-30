package com.michalplachta.galactic.java.service.remotedata;

public class Fetched<A> implements RemoteData<A> {
    public final A data;

    public Fetched(A data) {
        this.data = data;
    }
}

