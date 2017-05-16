package com.michalplachta.galactic.java.values;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Objects;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.PROPERTY, property="type")
@JsonSubTypes({
        @JsonSubTypes.Type(value=Civilian.class, name="civilian"),
        @JsonSubTypes.Type(value=Jedi.class, name="Jedi"),
        @JsonSubTypes.Type(value=Rebel.class, name="rebel"),
        @JsonSubTypes.Type(value=Sith.class, name="Sith"),
        @JsonSubTypes.Type(value=Stormtrooper.class, name="stormtrooper"),
})
public abstract class Citizen {
    public final String name;

    public Citizen(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Citizen)) return false;
        Citizen citizen = (Citizen) o;
        return Objects.equals(name, citizen.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Citizen{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
