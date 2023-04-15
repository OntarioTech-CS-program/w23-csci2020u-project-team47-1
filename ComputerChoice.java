package com.example.rpsgroupproject;

public class ComputerChoice {

    // Method to randomly set the computer's choice of rock, paper, or scissors
    public static String setComputerChoice() {
        // Initialize a default message in case there's an error
        String randomCompChoice = "No computer choice.";
        // Generate a random number between 1 and 3 (inclusive)
        int randomChoice = (int)(1 + Math.random()*3);
        // Assign the computer's choice based on the random number generated
        if (randomChoice == 1) {
            randomCompChoice = "rock";
        }
        else if (randomChoice == 2) {
            randomCompChoice = "paper";
        }
        else if (randomChoice == 3) {
            randomCompChoice = "scissors";
        }
        // Return the computer's choice
        return randomCompChoice;
    }

}
