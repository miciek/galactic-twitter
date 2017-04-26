package com.michalplachta.galactic.java.values;

public class Rebel extends Citizen {
    public Rebel(String name) {
        super(name);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Rebel{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
