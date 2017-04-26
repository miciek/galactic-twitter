package com.michalplachta.galactic.java.values;

import java.util.Objects;

public class Tweet {
    public final String text;
    public final Citizen author;

    public Tweet(String text, Citizen author) {
        this.text = text;
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tweet)) return false;
        Tweet tweet = (Tweet) o;
        return Objects.equals(text, tweet.text) &&
                Objects.equals(author, tweet.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, author);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Tweet{");
        sb.append("text='").append(text).append('\'');
        sb.append(", author=").append(author);
        sb.append('}');
        return sb.toString();
    }
}
