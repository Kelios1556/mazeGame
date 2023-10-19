> This is a Java-implemented Maze Game. I will upload some demo pictures later here.

# Implementation

## Classes and Data Structures

### Classes

<details open>
  <summary> <b> World </b> (click to expand/hide) </summary>

  This class represents the world of the game.

  <b> Fields </b>

1. `public TETile[][] world;` The array storing the information of the world.

2. `public Player player;` The player instance of the game.
  
3. `public Player player2 = null;` The second player (if any).

4. `private int roomNum;` The number of rooms in the world, generated randomly.

5. `private ArrayList<Room> rooms;` The array storing all instances of rooms.
  
6. `private char theme;` The theme of the world.
  
   > 'r' means vigorous (red)
   >
   > 'y' means luminous (yellow)
   >
   > 'g' means vivid (green)
   >
   > 'b' means genuine (blue)
   >
   > 'p' means violet
   >
   > 'w' means simple (white)

7. `public Long seed;` The seed of the game.

8. `public Random random;` 

7. `private static final int len = 100;` The column number of the world.
  
8. `private static final int wid = 100;` The row number of the world.

***

</details>


<details open>
  <summary> <b> Player </b> (click to expand/hide) </summary>

  This class represents the player of the game.

  <b> Fields </b>

1. `private int x;` The x-coordinate of the player.
  
2. `private int y;` The y-coordinate of the player.
  
3. `private String appearence = "$";` The character represents the avatar, can be chosen by the user. The default (if not chosen) is "$".

4. `private String name = "Jedi";` The name of the avatar, can be designed by the user. The default (if no input is offered) is "Jedi".
  
5. `private int color = ...;` The color of the avatar, can be chosen by the user. The default (if not chosen) is ....

***

</details>


<details open>
  <summary> <b> Room </b> (click to expand/hide) </summary>

  This class represents the room of the world.

  <b> Fields </b>
    
1. `private int x;` The x-coordinate of the room.
  
2. `private int y;` The y-coordinate of the room.
  
   > The position is ggenerated randomly.
  
3. `private int doorX;` The x-coordinate of the room door.
  
4. `private int doorY;` The y-coordinate of the room door.
  
   > The door is embeded in the wall surrounding the room, with its position generated randomly.
  
5. `private int len;` The half of column number of the room.
  
6. `private int wid;` The half of row number of the room.
  
   > The size is generated randomly.
  
7. `private int ...;`

***

</details>


### Data Structures

## Algorithms

## Persistence

```
proj3
    ├── PlusWorld
    │   ├── BoringWorldDemo.java
    │   └── RandomWorldDemo.java
    ├── README.md
    ├── byow
    │   ├── Core
    │   │   ├── Engine.java
    │   │   ├── Main.java
    │   │   └── RandomUtils.java
    │   ├── InputDemo
    │   │   ├── DemoInputSource.java
    │   │   ├── InputSource.java
    │   │   ├── KeyboardInputSource.java
    │   │   ├── RandomInputSource.java
    │   │   └── StringInputDevice.java
    │   └── TileEngine
    │       ├── TERenderer.java
    │       ├── TETile.java
    │       └── Tileset.java
    └── byowTools
        ├── RandomUtils.java
        └── TileEngine
            ├── TERenderer.java
            ├── TETile.java
            └── Tileset.java
```
