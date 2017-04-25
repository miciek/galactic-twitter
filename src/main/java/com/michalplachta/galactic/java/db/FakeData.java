package com.michalplachta.galactic.java.db;

import com.michalplachta.galactic.java.values.Citizen;
import javaslang.collection.List;

class FakeData {
    final static List<Citizen> citizens = List.of(new Citizen("Luke Skywalker"));
}
