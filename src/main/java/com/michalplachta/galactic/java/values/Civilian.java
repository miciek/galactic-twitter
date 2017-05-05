package com.michalplachta.galactic.java.values;

public class Civilian extends Citizen {
    public Civilian(String name) {
        super(name);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Civilian{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
