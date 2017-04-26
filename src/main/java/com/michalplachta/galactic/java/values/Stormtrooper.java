package com.michalplachta.galactic.java.values;

import java.util.Objects;

public class Stormtrooper extends Citizen {
    public final boolean isCloned;

    public Stormtrooper(String name, boolean isCloned) {
        super(name);
        this.isCloned = isCloned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stormtrooper)) return false;
        if (!super.equals(o)) return false;
        Stormtrooper that = (Stormtrooper) o;
        return isCloned == that.isCloned;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isCloned);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Stormtrooper{");
        sb.append("name='").append(name).append('\'');
        sb.append(", isCloned=").append(isCloned);
        sb.append('}');
        return sb.toString();
    }
}
