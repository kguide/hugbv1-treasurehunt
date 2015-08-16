

# Introduction #
This page is for documentation and notes regarding project Treasure Hunt which is being worked on as a part of Hugbúnaðarverkefni 1, a course in Computer Science at the University of Iceland.

# Coding Conventions #

The following are suggestions regarding our projects coding conventions.

  * All methods and variables should use Camel-writing unless they are constants
  * Constants should be written in all caps (THIS\_IS\_A\_CONSTANT\_NAME)
  * The Android 'global variables start with m' should be avoided.
  * All code should include Java Doc commenting style. This will save a lot of time
  * Curly braces example
> > for(i=0;i<200;i++) {
> > > code block

> > }
  * Short methods ! A method should typically not be longer then 10 lines (excluding whitespace, try/catch, comments and such). If your method is 20 lines long, try to split it up.

# Architecture #
The following are classes/interfaces that we expect will be implemented.

## GUI ##
  * plash-Screen-class
  * Log-In-class
  * Welcome-Screen-class
  * Game-Play-Gui-class (This needs the be split up)
  * Advanced-Menu-class
  * Help-Menu-class
  * Settings-Menu-class
  * Find-a-Game-class
  * Select-a-Game-class
  * Your-Score-class
  * High-Score-class

## Control ##
  * Player-class
  * Game-interface
  * Game-classes

## DAL ##
  * DB-class

## Networking ##
  * Networking-class

## GPS ##
  * GPS-communication-class


# User Stories #

## Create a Game - Simple Game ##
A player logs on to our website and wants to create a game. He selects the 'Create Game' button and starts by choosing Hallgrímskirkja as the starting position. He wants the players to start within the church so he assigns a small radius for the starting position. He then selects Hlöllabátar near Ingólfstorg as his next location and then he puts three clues that the players can use to get to that location. He would like for the radius of this location to be 50 meters. The next location is in Kópavogur (just pick some house in Kópavogur) and he adds two clues that should help the players get there. This is the final destination.

## Play a Game - Simple Game ##
A player logs on using his HTC Magic Android Phone. He selects 'Play' and from a list of games he is playing he selects 'Simple Game'. He is now informed that he has finished reached 0 of a total of 3 locations in this game. He is also informed that the starting position is Hallgrímskirkja (this is revealed to him on a map). The player then goes to Hallgrímskirkja where he receives confirmation that he has reached the first location and then he receives clue A1. He doesn't understand this clue so he requests another clue, A2. He understands this clue and goes to Hlöllabátar near Ingólfstorg. There he gets a confirmation that he has reached the second location and he then receives clue B1. He understands this clue and goes to some place in Kópavogur where he is informed that he has finished the game.



## More User stories ##
More user stories will be added here later.

# Reminders, things that could be done better #
  * Currently SS method usernameExists & createUser does not return json string like the other methods
  * Use SQL transactions for the more complicated SQL functions


# Server side test strings #

This account is available for testing.
  * Login
> > user: test@test.is<br />
> > pass: test<br />
> > hash: 80db696227da17a6f90ead4d3be98149<br />
> > http://www.hgphoto.net/treasure/controller.php?method=login&username=test@test.is&password=test&request=80db696227da17a6f90ead4d3be98149<br />

  * checkIfUserExists
> > http://hgphoto.net/treasure/controller.php?method=USERNAMEEXISTS&username=test@test.is

  * createUser
> > http://hgphoto.net/treasure/controller.php?method=createUser&username=test@test.is&password=test

  * getGameInfo
> > http://hgphoto.net/treasure/controller.php?method=getGameInfo&gameId=1

  * myGames
> > http://hgphoto.net/treasure/controller.php?method=getMyGames&userId=1


  * finishedGame
> > http://hgphoto.net/treasure/controller.php?method=finishedGame&userId=1&gameId=1

  * removeUserFromGame
> > http://hgphoto.net/treasure/controller.php?method=removeUserFromGame&userId=1&gameId=1