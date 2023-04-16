package com.example.wschatserverdemo;

import jakarta.websocket.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class GameRoom {
    // Define the instance variables for the GameRoom class.
    private String roomID;
    private List<Session> players = new ArrayList<>();
    private Map<String, String> playerChoices = new HashMap<>();
    private Map<String, Boolean> playerReady = new HashMap<>();

    // Define a constructor for the GameRoom class that takes a roomID parameter.
    public GameRoom(String roomID) {
        this.roomID = roomID;
    }

    // Define a method to add a player to the GameRoom.
    public void addPlayer(Session session) {
        players.add(session);
    }

    // Define a method to get the roomID of the GameRoom.
    public String getRoomID() {
        return roomID;
    }

    // Define a method to get a List of the players in the GameRoom.
    public List<Session> getPlayers() {
        return players;
    }

    // Define a method to set a player's choice in the game.
    public void setPlayerChoice(String userID, String choice) {
        playerChoices.put(userID, choice);
    }

    // Define a method to set a player's readiness status.
    public void setPlayerReady(String userID) {
        playerReady.put(userID, true);
    }

    // Define a method to check if all players have made their choices.
    public boolean areAllChoicesMade() {
        for (Session player : players) {
            if (!playerChoices.containsKey(player.getId())) {
                return false;
            }
        }
        return true;
    }

    // Define a method to check if all players are ready.
    public boolean areAllPlayersReady() {
        for (Session player : players) {
            if (!playerReady.containsKey(player.getId())) {
                return false;
            }
        }
        return true;
    }

    // Define a method to determine the result of the game.
    public String getGameResult() {
        // Create a Map to keep track of the scores.
        Map<String, Integer> scores = new HashMap<>();
        scores.put("R", 0);
        scores.put("P", 0);
        scores.put("S", 0);

        // Count the number of times each choice was made by the players.
        for (Session player : players) {
            String choice = playerChoices.get(player.getId());
            scores.put(choice, scores.get(choice) + 1);
        }
        int numPlayers = players.size();
        if (numPlayers == 2) {
            int numRock = scores.get("R");
            int numPaper = scores.get("P");
            if (numRock == 1 && numPaper == 1) {
                return "Tie";
            } else if (numRock == 2 || numPaper == 2) {
                return "Player 1 wins";
            } else {
                return "Player 2 wins";
            }
        } else if (numPlayers == 3) {
            int numRock = scores.get("R");
            int numPaper = scores.get("P");
            int numScissors = scores.get("S");
            if (numRock == 1 && numPaper == 1 && numScissors == 1) {
                return "Tie";
            } else if (numRock == 2 || numPaper == 2 || numScissors == 2) {
                return "Two players tie";
            } else {
                return "One player wins";
            }
        } else {
            return "Invalid number of players";
        }
    }

    public boolean isReady() {
        boolean flag = !playerReady.containsValue(false);
        if (flag == true) {
            return true;
        }
        return false;
    }
}
