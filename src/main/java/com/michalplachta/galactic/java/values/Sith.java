package com.michalplachta.galactic.java.values;

public class Sith extends Citizen {
    public Sith(String name) {
        super(name);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Sith{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
