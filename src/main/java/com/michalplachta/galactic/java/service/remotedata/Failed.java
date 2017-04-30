package com.michalplachta.galactic.java.service.remotedata;

public class Failed<A> implements RemoteData<A> {
    public final String errorMessage;

    public Failed(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

