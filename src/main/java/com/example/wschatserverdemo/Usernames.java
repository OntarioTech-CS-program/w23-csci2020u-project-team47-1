package com.example.wschatserverdemo;

import java.util.HashMap;
import java.util.Map;

public class Usernames {
    private Map<String, String> usernames = new HashMap<>();

    public void put(String userID, String username) {
        usernames.put(userID, username);
    }

    public String get(String userID) {
        return usernames.get(userID);
    }

    public void remove(String userID) {
        usernames.remove(userID);
    }
}
