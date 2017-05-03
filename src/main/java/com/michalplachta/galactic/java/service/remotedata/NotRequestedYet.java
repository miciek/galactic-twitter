package com.michalplachta.galactic.java.service.remotedata;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("NotRequestedYet")
public class NotRequestedYet<A> implements RemoteData<A> {}

