// Import necessary libraries
package com.example.wschatserverdemo;

import java.util.HashMap;
import java.util.Map;

// Define class for storing user IDs and usernames
public class Usernames {
    // Create a private HashMap to store user IDs and usernames
    private Map<String, String> usernames = new HashMap<>();

    // Method to add a user ID and username to the HashMap
    public void put(String userID, String username) {
        usernames.put(userID, username);
    }

    // Method to get the username associated with a specific user ID from the HashMap
    public String get(String userID) {
        return usernames.get(userID);
    }

    // Method to remove a user ID and username from the HashMap
    public void remove(String userID) {
        usernames.remove(userID);
    }
}
