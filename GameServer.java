package com.example.finalgroupproject;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.example.finalgroupproject.ResourceAPI.loadGameRoomHistory;
import static com.example.finalgroupproject.ResourceAPI.saveGameRoomHistory;

@ServerEndpoint(value = "/ws/{roomName}")
public class GameServer {
    // <roomName, history>
    private static Map<String, String> gameHistorylist = new HashMap<String, String>();
    // <roomID, chatRoom>
    private static Map<String, GameRoom> gameRooms = new HashMap<>();
    //   list of active rooms
    public static List<String> activeGameRooms = new ArrayList<>();

    @OnOpen
    public void open(@PathParam("roomName") String roomName, Session session) throws IOException, EncodeException {
        // join the room
        joinRoom(roomName, session);
    }
    @OnClose
    public void close(Session session) throws IOException, EncodeException {
        // leave the room
        leaveRoom(session);
    }

    @OnMessage
    public void handleMessage(String comm, Session session) throws IOException, EncodeException {
        handleUserMessage(comm, session);
    }

    private void joinRoom(String roomName, Session session) throws IOException, EncodeException {
        // get the chat room
        GameRoom gameRoom = getOrCreateGameRoom(roomName, session);
        // add the user to the chat room
        addPlayerToGameRoom(gameRoom, session);
// load the chat room history
        String history = loadGameRoomHistory(roomName);
        // send the chat room history
        sendGameHistory(history, session, roomName);

        if (!gameHistorylist.containsKey(roomName)) {
            // add the room to the history list
            gameHistorylist.put(roomName, "\\n" + roomName + " room Created.~S~");
        }

        session.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server ): Welcome to Rock Paper Scissors. Please state your username to begin.\"}");// send a welcome message to the user

        if (!activeGameRooms.contains(roomName)) {
            // add the room to the active rooms list
            activeGameRooms.add(roomName);
        }
    }
    private String generateUniqueroomName() {
        return GameRoomGenerator.generatingRandomUpperAlphanumericString(5);
    }

    private GameRoom getOrCreateGameRoom(String roomName, Session session) {
        GameRoom gameRoom = null;
        for (GameRoom existingGameRoom : gameRooms.values()) {
            if (existingGameRoom.getGameRoom().equals(roomName)) {
                gameRoom = existingGameRoom;
                break;
            }
        }

        if (gameRoom == null) {
            gameRoom = new GameRoom(roomName, session.getId(), session);
            gameRoom.setGameRoomID(generateUniqueroomName());
            gameRooms.put(gameRoom.getGameRoomID(), gameRoom);
        }
        return gameRoom;
    }


    private void addPlayerToGameRoom(GameRoom gameRoom, Session session) {
        // add the user to the chat room
        gameRoom.addPlayer(session.getId(), "", session);
    }

    private void sendGameHistory(String history, Session session, String roomName) throws IOException, EncodeException {
        if (history != null && !(history.isBlank())) {
            // split the history into messages
            String arr[] = history.split("~S~");
            for (String message : arr) {
                // send the message to the user
                session.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"" + message + "\"}");
            }
            // add the room to the history list
            gameHistorylist.put(roomName, history + " \\n " + roomName + " room resumed.~S~");
        }
    }
    private void leaveRoom(Session session) throws IOException, EncodeException {
        // get the user id
        String playerId = session.getId();
        // find the chat room by the user id
        GameRoom gameRoom = findGameRoomByPlayerId(playerId);

        if (gameRoom != null) {
            // Get the room name
            String roomName = gameRoom.getGameRoom();
            //   get the username
            String username = gameRoom.getPlayers().get(playerId);
            // remove the user from the chat room
            gameRoom.removePlayer(playerId);

            if (gameRoom.isEmpty()) {
                // save the chat room history
                saveGameRoomHistory(roomName, gameHistorylist.get(roomName));
                // remove the room from the active rooms list
                activeGameRooms.remove(roomName);
                // Remove the chat room from the map
                gameRooms.remove(gameRoom.getGameRoomID());
            }
// update the room history
            updateGameRoomHistory(roomName, username + " left the chat room.~S~");
            // broadcast the goodbye message
            goodbyeBroadcast(gameRoom, session, "(Server): " + username + " left the chat room.");

            // Remove the session from the chat room
            gameRoom.getSessions().remove(session);
        }
    }


    private GameRoom findGameRoomByPlayerId(String playerId) {
        for (GameRoom gameRoom : gameRooms.values()) {
            if (gameRoom.inGame(playerId)) {
                return gameRoom;
            }
        }
        return null;
    }

    private void updateGameRoomHistory(String roomName, String message) {
        // get the room history
        String logHistory = gameHistorylist.get(roomName);
        // update the room history
        gameHistorylist.put(roomName, logHistory + "\\n" + message);
    }

    private void broadcastMessageToPeersInRoom(GameRoom gameRoom, Session session, String message) throws IOException, EncodeException {
        for (Session peer : session.getOpenSessions()) {
            if (gameRoom.inGame(peer.getId())) {
                // send the message to the user
                peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"" + message + "\"}");
            }
        }
    }

    private void welcomeBroadcast(GameRoom gameRoom, Session session, String message) throws IOException, EncodeException {
        for (Session peer : session.getOpenSessions()) {
            if (gameRoom.inGame(peer.getId())) {
                // send the message to the user
                System.out.println("Sending welcome message to " + peer.getId());
                // send the message to the user
                peer.getBasicRemote().sendText("{\"type\": \"userJoin\", \"message\":\"" + message + "\"}");
                if (!peer.getId().equals(session.getId())) {
                    // send the message to the user
                    peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"" + message + "\"}");
                }
            }
        }
    }

    private void goodbyeBroadcast(GameRoom gameRoom, Session session, String message) throws IOException, EncodeException {
        for (Session peer : session.getOpenSessions()) {
            if (gameRoom.inGame(peer.getId())) {
                // send the message to the user
                peer.getBasicRemote().sendText("{\"type\": \"userLeave\", \"message\":\"" + message + "\"}");
                // send the message to the user
                peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"" + message + "\"}");
                //ASdasdnhbkjhashbndbkjsad
            }
        }
    }

    private void handleUserMessage(String comm, Session session) throws IOException, EncodeException {
        JSONObject jsonmsg = new JSONObject(comm);
        String type = (String) jsonmsg.get("type");
        String message = (String) jsonmsg.get("msg");

        String playerId = session.getId();
        // find the chat room by the user id
        GameRoom gameRoom = findGameRoomByPlayerId(playerId);

        if (gameRoom == null) {
            return;
        }

        if (gameRoom.getPlayers() != null && !gameRoom.getPlayers().get(playerId).isEmpty()) {
            // handle the chat message
            handleChatMessage(gameRoom, session, playerId, message);
        } else {
            // handle the username message
            handleUsernameMessage(gameRoom, session, playerId, message);

        }
// get the room name
        String roomName = gameRoom.getGameRoom();
        // save the chat room history
        saveGameRoomHistory(roomName, gameHistorylist.get(roomName));
    }

    private void handleChatMessage(GameRoom gameRoom, Session session, String userID, String message) throws IOException, EncodeException {
        String roomName = gameRoom.getGameRoom();
        String username = gameRoom.getPlayers().get(userID);
// update the room history
        updateGameRoomHistory(roomName, "(" + username + "): " + message + "~S~");
        // broadcast the message
        broadcastMessageToPeersInRoom(gameRoom, session, "(" + username + "): " + message);
    }

    private void handleUsernameMessage(GameRoom gameRoom, Session session, String userID, String message) throws IOException, EncodeException {
        // set the username
        gameRoom.setUserName(userID, message, session);

        session.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server ): Welcome, " + message + "!\"}");
        // get the room name
        String roomName = gameRoom.getGameRoom();
        // update the room history
        updateGameRoomHistory(roomName, message + " joined the chat room.~S~");
        // broadcast the welcome message
        welcomeBroadcast(gameRoom, session, "(Server): " + message + " joined the chat room.");
    }

    //Function to get the usernames of the users in a chatroom
    public static List<String> getUsernames(String roomName) {
        // Create a list to store the usernames
        List<String> usernames = new ArrayList<>();
        // Create a chatroom to store the target chatroom
        GameRoom targetChatRoom = null;
        for (GameRoom gameRoom : gameRooms.values()) {
            if (gameRoom.getGameRoom().equals(roomName)) {
                // Set the target chatroom to the chatroom with the same name as the roomname
                targetChatRoom = gameRoom;
                break;
            }
        }
        if (targetChatRoom != null) {
            for (String userID : targetChatRoom.getPlayers().keySet()) {
                // Don't put UUID in the list
                if (targetChatRoom.getPlayers().get(userID).length() > 25) {
                    continue;
                }
                usernames.add(targetChatRoom.getPlayers().get(userID));
                System.out.println(targetChatRoom.getPlayers().get(userID) + " THIS IS THE USERNAME" + targetChatRoom.getPlayers().keySet() + " THIS IS THE KEYSET");
            }
        }
        return usernames;
    }

}
