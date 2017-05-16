package com.michalplachta.galactic.db

import com.michalplachta.galactic.values.Citizen._
import com.michalplachta.galactic.values.{ Citizen, Tweet }

object FakeData {
  val clones = List.range(1, 100).map("Clone Trooper #" + _).map(Stormtrooper(_, cloned = true))
  val siths = List("Darth Vader", "Emperor", "Ben Solo").map(Sith)
  val jedis = List("Luke Skywalker", "Obi-Wan Kenobi", "Yoda").map(Jedi)
  val rebels = List("Princess Leia", "Han Solo").map(Rebel)
  val citizens: List[Citizen] = siths ++ jedis ++ rebels ++ clones
  val tweets: List[Tweet] = List(
    Tweet("Who's the more foolish: the fool, or the fool who follows him?", Jedi("Obi-Wan Kenobi")),
    Tweet("I am Luke's father.", Sith("Darth Vader")),
    Tweet("It's true. All of it. The Dark Side, the Jedi. They're real.", Rebel("Han Solo")),
    Tweet("Patience. Use the Force. Think.", Jedi("Obi-Wan Kenobi")),
    Tweet("Fear is the path to the Dark Side. Fear leads to anger. Anger leads to hate. Hate leads to suffering.", Jedi("Yoda")),
    Tweet("I've got a bad feeling about this.", Rebel("Han Solo")),
    Tweet("She is strong with the Force.", Sith("Ben Solo")),
    Tweet("Yes, your thoughts betray you.", Sith("Darth Vader")),
    Tweet("I think that R2 unit we bought may have been stolen.", Jedi("Luke Skywalker")),
    Tweet("In time, you will learn to trust your feelings. Then, you will be invincible.", Sith("Emperor")),
    Tweet("The fear of loss is a path to the Dark Side.", Jedi("Yoda")),
    Tweet("Help me Obi-Wan Kenobi, you're my only hope!", Rebel("Princess Leia")),
    Tweet("I'll show you the Dark Side.", Sith("Ben Solo")),
    Tweet("You want to close Galactic Twitter, go home and rethink your life.", Jedi("Obi-Wan Kenobi")),
    Tweet("But beware of the Dark Side. Anger, fear, aggression. The Dark Side of the Force are they.", Jedi("Yoda")),
    Tweet("Give yourself to the Dark Side. It is the only way you can save your friends.", Sith("Darth Vader")),
    Tweet("A Jedi gains power through understanding and a Sith gains understanding through power.", Sith("Emperor")),
    Tweet("May the Force be with you.", Rebel("Princess Leia")),
    Tweet("The Force is strong with this one.", Sith("Darth Vader")),
    Tweet("Use the Force, Luke.", Jedi("Obi-Wan Kenobi")),
    Tweet("I'll drop my weapon.", Stormtrooper("JB-007", cloned = false)),
    Tweet("Don't move!", Stormtrooper("Random Stormtrooper", cloned = false)),
    Tweet("Once more the Sith will rule the galaxy, and we shall have peace.", Sith("Emperor"))
  )
}
