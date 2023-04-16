# INTERACTIVE CLIENT ROOM
> Course: CSCI 2020U: Software Systems Development and Integration

**The following is the backend for the game logic**
## Game.java
* The Game class is annotated with @ServerEndpoint which indicates that it is a WebSocket server endpoint.
* The endpoint's URI is defined using the value parameter of the annotation: /ws/game/{roomID}. The roomID is a path parameter.
* The class has two instance variables: gameRooms is a Map that stores instances of GameRoom, and usernames is a Map that associates user IDs with usernames.
* The @OnMessage annotation indicates that the method handleMessage is called when a message is received from a client.
* The handleMessage method takes two parameters: the message received from the client, and the Session object representing the client's session.
* The method first retrieves the user ID from the session.
* If the user ID is associated with a username, the method checks whether the user is already in a game room. If so, it checks whether the game is ready to start, and handles the user's message accordingly.
* If the user ID is not associated with a username, it associates the user ID with the received message as the username.
* The handleMessage method throws an IOException if there is an error sending a message to a client.
* The method sends a message to all clients in a game room using the getBasicRemote method of the Session object.
* The method removes the game room entry from the gameRooms map when the game is over.


## GameRoom.java
* This is a Java class named GameRoom that represents a game room in a multiplayer rock-paper-scissors game. Here's a brief explanation of the code:
* The class has four instance variables: roomID (a string representing the room ID), players (a list of Session objects representing the players in the room), playerChoices (a map that stores the choices made by each player), and playerReady (a map that stores the readiness status of each player).
* The class has a constructor that takes a roomID parameter and initializes the roomID instance variable.
* The class has methods to add a player to the room (addPlayer), get the room ID (getRoomID), get the list of players (getPlayers), set a player's choice (setPlayerChoice), set a player's readiness status (setPlayerReady), check if all players have made their choices (areAllChoicesMade), check if all players are ready (areAllPlayersReady), and determine the result of the game (getGameResult).
* The areAllChoicesMade method iterates through the list of players and checks if each player has made a choice by looking up their ID in the playerChoices map. If any player has not made a choice, the method returns false. Otherwise, it returns true.
* The areAllPlayersReady method iterates through the list of players and checks if each player is ready by looking up their ID in the playerReady map. If any player is not ready, the method returns false. Otherwise, it returns true.
* The getGameResult method determines the result of the game based on the number of players and the choices made by each player. It first initializes a map called scores to keep track of the number of times each choice was made. It then iterates through the list of players, gets each player's choice from the playerChoices map, and increments the corresponding score in the scores map. Finally, it checks the scores to determine the result of the game (e.g., if two players chose rock and one player chose scissors, the method returns "Two players tie").
* The isReady method checks if all players in the room are ready. It does this by checking if the playerReady map contains any value that is false. If there is no false value, the method returns true. Otherwise, it returns false.

## Usernames

* This Java class named Usernames that contains a HashMap to store user IDs and usernames. The HashMap is a data structure that stores data in key-value pairs. In this case, the key is the user ID and the value is the username.
* The Usernames class has three methods: put(), get(), and remove(). The put() method takes two parameters, a user ID and a username, and adds them to the HashMap using the put() method of the HashMap. The get() method takes a user ID as a parameter and returns the associated username using the get() method of the HashMap. The remove() method takes a user ID as a parameter and removes the associated key-value pair from the HashMap using the remove() method of the HashMap.



