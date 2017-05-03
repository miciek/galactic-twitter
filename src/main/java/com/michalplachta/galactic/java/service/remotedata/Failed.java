package com.michalplachta.galactic.java.service.remotedata;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Failed")
public class Failed<A> implements RemoteData<A> {
    public final String errorMessage;

    public Failed(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

