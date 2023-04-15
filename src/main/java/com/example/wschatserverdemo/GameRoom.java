package com.example.wschatserverdemo;

import jakarta.websocket.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class GameRoom {
    private String roomID;
    private List<Session> players = new ArrayList<>();
    private Map<String, String> playerChoices = new HashMap<>();
    private Map<String, Boolean> playerReady = new HashMap<>();

    public GameRoom(String roomID) {
        this.roomID = roomID;
    }

    public void addPlayer(Session session) {
        players.add(session);
    }

    public String getRoomID() {
        return roomID;
    }

    public List<Session> getPlayers() {
        return players;
    }

    public void setPlayerChoice(String userID, String choice) {
        playerChoices.put(userID, choice);
    }

    public void setPlayerReady(String userID) {
        playerReady.put(userID, true);
    }

    public boolean areAllChoicesMade() {
        for (Session player : players) {
            if (!playerChoices.containsKey(player.getId())) {
                return false;
            }
        }
        return true;
    }

    public boolean areAllPlayersReady() {
        for (Session player : players) {
            if (!playerReady.containsKey(player.getId())) {
                return false;
            }
        }
        return true;
    }

    public String getGameResult() {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("R", 0);
        scores.put("P", 0);
        scores.put("S", 0);
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
}
