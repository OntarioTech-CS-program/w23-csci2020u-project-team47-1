package com.example.rpsgroupproject;

import java.io.IOException;

public class WhoIsTheWinner {

    // Method for winner
    public static String theWinnerIs(String player1Choice, String player2Choice, String gameId) throws IOException {
        String winner = "Not ready. Refresh page in a while.";
        String rock = "rock";
        String paper = "paper";
        String scissors = "scissors";

        // if both players make the same choice
        if (player1Choice.equals(player2Choice) || player2Choice.equals(player1Choice)) {
            if (gameId == "PVP") {
                winner = "It´s score draw.<br><br>Player 1: " + player1Choice + "<br><br>Player 2: " + player2Choice;
            }
            else if (gameId == "PVC") {
                winner = "It´s score draw.<br><br>You: " + player1Choice + "<br><br>Computer: " + player2Choice;
            }
            RPS.increaseTies(); // increment the number of ties
        }
        // if player1 wins
        else if (player1Choice.equals(rock) && player2Choice.equals(scissors) ||
                player1Choice.equals(paper) && player2Choice.equals(rock) ||
                player1Choice.equals(scissors) && player2Choice.equals(paper)) {
            if (gameId == "PVP") {
                winner = "Player 1 wins.<br><br>Player 1: " + player1Choice + "<br><br>Player 2: " + player2Choice;
            }
            else if (gameId == "PVC") {
                winner = "You win.<br><br>You: " + player1Choice + "<br><br>Computer: " + player2Choice;
            }
            RPS.increaseScoresPlayer1(); // increment the score of player1
        }
        // if player2 wins
        else if (player1Choice.equals(rock) && player2Choice.equals(paper) ||
                player1Choice.equals(paper) && player2Choice.equals(scissors) ||
                player1Choice.equals(scissors) && player2Choice.equals(rock)) {
            if (gameId == "PVP") {
                winner = "Player 2 wins.<br><br>Player 1: " + player1Choice + "<br><br>Player 2: " + player2Choice;
            }
            else if (gameId == "PVC") {
                winner = "Computer wins.<br><br>You: " + player1Choice + "<br><br>Computer: " + player2Choice;
            }
            RPS.increaseScoresPlayer2(); // increment the score of player2
        }
        return winner;
    }

}
