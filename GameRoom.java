package com.example.finalgroupproject;
import jakarta.websocket.Session;
import java.util.*;
public class GameRoom {
    private String game_code;
    private Map<String, String> players = new HashMap<String, String>();
    private Set<Session> sessions = new HashSet<>();
    String gameRoomID;

    public GameRoom(String game_code, String players, Session session) {
        this.game_code = game_code;
        this.players.put(players, "");

        addPlayer(players, "", session);
    }
    public void setGame_code(String game_code) {
        this.game_code = game_code;
    }
    public void setGameRoomID(String gameRoomID) {
        this.gameRoomID = gameRoomID;
    }

    public String getGameRoomID() {
        return this.gameRoomID;
    }

    public String getGameRoom() {
        return this.game_code;
    }
    public Map<String, String> getPlayers() {
        return players;
    }

    public void addPlayer(String playerID, String name, Session session) {
        players.put(playerID, name);
        sessions.add(session);
    }

    public boolean inGame(String playerID) {
        return players.containsKey(playerID);
    }
    public boolean isEmpty() {
        return players.isEmpty();
    }

    public void setUserName(String playerID, String name, Session session) {
        if (players.containsKey(playerID)) {
            players.put(playerID, name);
        } else {
            addPlayer(playerID, name, session);
        }
    }
    public void removePlayer(String playerID) {
        players.remove(playerID);
    }
    public Set<Session> getSessions() {
        return sessions;
    }
}
