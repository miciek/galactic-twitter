# Galactic Twitter

This app serves as a practical example of strongly-typed functional approach to creating simple DB-backed asynchronous service.

## Showcased problems and approaches

### Problem #1: Zero as "no value yet"
### Problem #2: Not handling failures 
### Problem #3: Handling lists of Algebraic Data Types
### Problem #4: Cryptic return types
### Problem #5: Convoluted logic using IFs and vars  

## Running the applications
There are two two types of applications: console apps and HTTP server apps. Each of them are implemented in both Java and Scala with exactly the same set of functionalities. 

- `sbt "run-main com.michalplachta.galactic.java.http.ServerApp"` - HTTP server with `/followers/{citizenName}` and `/tweets/{citizenName}` endpoints (*Java version*)
- `sbt "run-main com.michalplachta.galactic.java.console.GalacticTwitterApp"` - console-based **Galactic Twitter** (*Java version*)
- `sbt "run-main com.michalplachta.galactic.java.console.GalacticFollowersApp"` - simplified console-based **Galactic Twitter** (displaying only followers) (*Java version*)
- `sbt "run-main com.michalplachta.galactic.http.ServerApp"` - HTTP server with `/followers/{citizenName}` and `/tweets/{citizenName}` endpoints (*Scala version*)
- `sbt "run-main com.michalplachta.galactic.console.GalacticTwitterApp"` - console-based **Galactic Twitter** (*Scala version*)
- `sbt "run-main com.michalplachta.galactic.console.GalacticFollowersApp"` - simplified console-based **Galactic Twitter** (displaying only followers) (*Scala version*)
