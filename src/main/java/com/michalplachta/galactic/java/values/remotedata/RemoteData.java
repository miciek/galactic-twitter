package com.michalplachta.galactic.java.values.remotedata;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import javaslang.algebra.Functor;

import java.util.function.Function;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.PROPERTY, property="state")
@JsonSubTypes({
        @JsonSubTypes.Type(value=NotRequestedYet.class, name="notRequestedYet"),
        @JsonSubTypes.Type(value=Loading.class, name="loading"),
        @JsonSubTypes.Type(value=Failed.class, name="failed"),
        @JsonSubTypes.Type(value=Fetched.class, name="fetched")
})
public interface RemoteData<A> extends Functor<A> {
    <U> RemoteData<U> map(Function<? super A, ? extends U> f);
}

