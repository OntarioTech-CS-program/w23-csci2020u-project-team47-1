package com.example.wschatserverdemo;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@ServerEndpoint(value="/ws/game/{roomID}")
public class Game
{
    private Map<String, GameRoom> gameRooms = new HashMap<>();
    private Map<String, String> usernames = new HashMap<>();


    @OnMessage
    public void handleMessage(String message, Session session) throws IOException {
        String userID = session.getId();
        if (usernames.containsKey(userID)) {
            String username = usernames.get(userID);
            if (gameRooms.containsKey(userID)) {
                GameRoom gameRoom = gameRooms.get(userID);
                if (gameRoom.isReady()) {
                    String choice = message.toUpperCase();
                    if (choice.equals("R") || choice.equals("P") || choice.equals("S")) {
                        gameRoom.setPlayerChoice(userID, choice);
                        if (gameRoom.areAllChoicesMade()) {
                            String result = gameRoom.getGameResult();
                            for (Session peer : gameRoom.getPlayers()) {
                                peer.getBasicRemote().sendText("{\"type\": \"game-result\", \"result\": \"" + result + "\"}");
                            }
                            // Remove the gameRoom entry from the gameRooms map
                            for (Map.Entry<String, GameRoom> entry : gameRooms.entrySet()) {
                                if (entry.getValue().equals(gameRoom)) {
                                    gameRooms.remove(entry.getKey());
                                    break;
                                }
                            }
                        }
                    } else {
                        gameRoom.setPlayerReady(userID);
                        if (gameRoom.areAllPlayersReady()) {
                            for (Session peer : gameRoom.getPlayers()) {
                                peer.getBasicRemote().sendText("{\"type\": \"game-start\"}");
                            }
                        }
                    }
                }
            }
        } else {
            usernames.put(userID, message);
        }
    }

}

