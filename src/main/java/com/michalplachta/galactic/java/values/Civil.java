package com.michalplachta.galactic.java.values;

public class Civil extends Citizen {
    public Civil(String name) {
        super(name);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Civil{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
