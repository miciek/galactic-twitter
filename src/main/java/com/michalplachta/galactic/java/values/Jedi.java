package com.michalplachta.galactic.java.values;

public class Jedi extends Citizen {
    public Jedi(String name) {
        super(name);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Jedi{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

