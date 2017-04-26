package com.michalplachta.galactic.java.values;

import java.util.Objects;

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
