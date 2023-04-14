package com.example.finalgroupproject;

import jakarta.websocket.Session;
import java.util.*;

public class GameRoom {
    private String game_code;
    private Map<String, Player> players = new HashMap<String, Player>();
    private Set<Session> sessions = new HashSet<>();
    private Map<String, Performance> playerRanks = new HashMap<>();

    String gameRoomID;

    public GameRoom(String game_code, Player player, Session session) {
        this.game_code = game_code;
        this.players.put(player.getId(), player);

        addPlayer(player, session);
    }

    public Map<String, Player> getPlayers() {
        return players;
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

    public void addPlayer(Player player, Session session) {
        players.put(player.getId(), player);
        sessions.add(session);
        setPlayerRank(player.getId(), player.getGamesWon(), player.getGamesLost());
    }

    public boolean inGame(String playerID) {
        return players.containsKey(playerID);
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public void setUserName(String playerID, String name, Session session) {
        if (players.containsKey(playerID)) {
            Player player = players.get(playerID);
            player.setName(name);
        } else {
            Player player = new Player(playerID, name, 0);
            addPlayer(player, session);
        }
    }

    public void removePlayer(String playerID) {
        players.remove(playerID);
    }

    public Set<Session> getSessions() {
        return sessions;
    }
}
