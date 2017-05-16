package com.michalplachta.galactic.java.db;

import com.michalplachta.galactic.java.values.*;
import javaslang.collection.List;

public class FakeData {
    public final static List<Citizen> clones = List.range(1, 100).map(n -> new Stormtrooper("Clone Trooper #" + n, true));
    public final static List<Citizen> siths = List.of("Darth Vader", "Emperor", "Ben Solo").map(Sith::new);
    public final static List<Citizen> jedis = List.of("Luke Skywalker", "Obi-Wan Kenobi", "Yoda").map(Jedi::new);
    public final static List<Citizen> rebels = List.of("Princess Leia", "Han Solo").map(Rebel::new);
    public final static List<Citizen> citizens = siths.appendAll(jedis).appendAll(rebels).appendAll(clones);
    public final static List<Tweet> tweets = List.of(
            new Tweet("Who's the more foolish: the fool, or the fool who follows him?", new Jedi("Obi-Wan Kenobi")),
            new Tweet("I am Luke's father.", new Sith("Darth Vader")),
            new Tweet("It's true. All of it. The Dark Side, the Jedi. They're real.", new Rebel("Han Solo")),
            new Tweet("Patience. Use the Force. Think.", new Jedi("Obi-Wan Kenobi")),
            new Tweet("Fear is the path to the Dark Side. Fear leads to anger. Anger leads to hate. Hate leads to suffering.", new Jedi("Yoda")),
            new Tweet("I've got a bad feeling about this.", new Rebel("Han Solo")),
            new Tweet("She is strong with the Force.", new Sith("Ben Solo")),
            new Tweet("Yes, your thoughts betray you.", new Sith("Darth Vader")),
            new Tweet("I think that R2 unit we bought may have been stolen.", new Jedi("Luke Skywalker")),
            new Tweet("In time, you will learn to trust your feelings. Then, you will be invincible.", new Sith("Emperor")),
            new Tweet("The fear of loss is a path to the Dark Side.", new Jedi("Yoda")),
            new Tweet("Help me Obi-Wan Kenobi, you're my only hope!", new Rebel("Princess Leia")),
            new Tweet("I'll show you the Dark Side.", new Sith("Ben Solo")),
            new Tweet("You want to close Galactic Twitter, go home and rethink your life.", new Jedi("Obi-Wan Kenobi")),
            new Tweet("But beware of the Dark Side. Anger, fear, aggression. The Dark Side of the Force are they.", new Jedi("Yoda")),
            new Tweet("Give yourself to the Dark Side. It is the only way you can save your friends.", new Sith("Darth Vader")),
            new Tweet("A Jedi gains power through understanding and a Sith gains understanding through power.", new Sith("Emperor")),
            new Tweet("May the Force be with you.", new Rebel("Princess Leia")),
            new Tweet("The Force is strong with this one.", new Sith("Darth Vader")),
            new Tweet("Use the Force, Luke.", new Jedi("Obi-Wan Kenobi")),
            new Tweet("I'll drop my weapon.", new Stormtrooper("JB-007", false)),
            new Tweet("Don't move!", new Stormtrooper("Random Stormtrooper", false)),
            new Tweet("Once more the Sith will rule the galaxy, and we shall have peace.", new Sith("Emperor")));
}
