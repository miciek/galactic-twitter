# Galactic Twitter
This project serves as a practical example of strongly-typed functional approach to creating simple DB-backed asynchronous service. There are two implementations: one in [Java](src/main/java) and one in [Scala](src/main/scala). You can use them to compare functional features in both languages.

The project is a foundation for my talk about functional programming: ["Pragmatist's Guide to Functional Geekery"](http://michalplachta.com/talks/). Slides can be found on [Speaker Deck](https://speakerdeck.com/miciek/pragmatists-guide-to-functional-geekery).

## Background
The application simulates some of [Twitter](http://twitter.com) for a [Star Wars](http://www.starwars.com) based domain.

### Followers functionality
Every [Citizen](src/main/scala/com/michalplachta/galactic/values/Citizen.scala) may have 0 or more followers.

### Tweet Wall functionality
Every [Citizen](src/main/scala/com/michalplachta/galactic/values/Citizen.scala) is able to view [Tweets](src/main/scala/com/michalplachta/galactic/values/Tweet.scala) on their Tweet Wall.

### Tweet censorship functionality
Depending on who rules the galaxy, the tweets may or may not need to be censored and manipulated.

## Running the applications
There are two two types of applications: console apps and HTTP server apps. All applications are implemented in both Java and Scala with exactly the same set of functionalities. 

- `sbt "run-main com.michalplachta.galactic.java.http.ServerApp"` [[ServerApp.java](src/main/java/com/michalplachta/galactic/java/http/ServerApp.java)] - HTTP server with `/followers/{citizenName}` and `/tweets/{citizenName}` endpoints (*Java version*)
- `sbt "run-main com.michalplachta.galactic.java.console.GalacticTwitterApp"`  [[GalacticTwitterApp.java](src/main/java/com/michalplachta/galactic/java/console/GalacticTwitterApp.java)] - console-based **Galactic Twitter** (*Java version*)
- `sbt "run-main com.michalplachta.galactic.java.console.GalacticFollowersApp"`  [[GalacticFollowersApp.java](src/main/java/com/michalplachta/galactic/java/console/GalacticFollowersApp.java)] - simplified console-based **Galactic Twitter** (displaying only followers) (*Java version*)
- `sbt "run-main com.michalplachta.galactic.http.ServerApp"` [[ServerApp.scala](src/main/scala/com/michalplachta/galactic/http/ServerApp.scala)] - HTTP server with `/followers/{citizenName}` and `/tweets/{citizenName}` endpoints (*Scala version*)
- `sbt "run-main com.michalplachta.galactic.console.GalacticTwitterApp"` [[GalacticTwitterApp.scala](src/main/scala/com/michalplachta/galactic/console/GalacticTwitterApp.scala)] - console-based **Galactic Twitter** (*Scala version*)
- `sbt "run-main com.michalplachta.galactic.console.GalacticFollowersApp"` [[GalacticFollowersApp.scala](src/main/scala/com/michalplachta/galactic/console/GalacticFollowersApp.scala)] - simplified console-based **Galactic Twitter** (displaying only followers) (*Scala version*)

## Showcased problems and approaches
### Problem #1: Zero as "no value yet"
See comments in both [FollowersService.java](src/main/java/com/michalplachta/galactic/java/service/FollowersService.java) and [FollowersService.scala](src/main/scala/com/michalplachta/galactic/service/FollowersService.scala) files.

### Problem #2: Not handling failures 
See comments in both [FollowersService.java](src/main/java/com/michalplachta/galactic/java/service/FollowersService.java) and [FollowersServiceService.scala](src/main/scala/com/michalplachta/galactic/service/FollowersService.scala) files.

### Problem #3: Cryptic return types
See comments in both [FollowersService.java](src/main/java/com/michalplachta/galactic/java/service/FollowersService.java) and [FollowersServiceService.scala](src/main/scala/com/michalplachta/galactic/service/FollowersService.scala) files.

### Problem #4: Handling lists of Algebraic Data Types
See comments in both [Followers.java](src/main/java/com/michalplachta/galactic/java/logic/Followers.java) and [Followers.scala](src/main/scala/com/michalplachta/galactic/logic/Followers.scala) files.

### Problem #5: Convoluted logic using IFs and vars
See comments in both [TweetCensorship.java](src/main/java/com/michalplachta/galactic/java/logic/TweetCensorship.java) and [TweetCensorship.scala](src/main/scala/com/michalplachta/galactic/logic/TweetCensorship.scala) files.

### Problem #6: Writing tests by providing examples
See comments in [FollowersTest.java](src/test/java/com/michalplachta/galactic/service/FollowersTest.java).

### Problem #7: Pure values tangled with JSON annotations
See the problem in [RemoteData.java](src/main/java/com/michalplachta/galactic/java/values/remotedata/RemoteData.java) and potential solution using typeclasses in [RemoteData.scala](src/main/scala/com/michalplachta/galactic/values/RemoteData.scala) and [RemoteDataJsonSupport.scala](src/main/scala/com/michalplachta/galactic/http/RemoteDataJsonSupport.scala).


