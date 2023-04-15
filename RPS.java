// Importing necessary packages
package com.example.rpsgroupproject;

import java.io.IOException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

// Controller class for Rock-Paper-Scissors game
@Path("/")
public class RPS {

    // Initializing variables
    public static String player1Choice = "empty";
    public static String player2Choice = "empty";
    public static String gameId = "noId";
    public static String winner = "Not ready. Refresh page in a while.";

    // Player 1 GET method
    @Path("/scores/player1")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getPlayer1(@QueryParam("getPlayer1Choice") String getPlayer1Choice) throws IOException {
        // Check if game is taken by PVC (Player vs Computer)
        if (gameId != "PVC") {
            // Set player 1 choice
            player1Choice = getPlayer1Choice;
            // Check if player 2 has made a choice
            if (player2Choice != "empty") {
                // Set game mode to PVP (Player vs Player)
                gameId = "PVP";
                // Determine the winner and reset choices
                winner = WhoIsTheWinner.theWinnerIs(player1Choice, player2Choice, gameId);
                ScoreBean.resetChoice();
            }
        } else {
            return "Game is taken by PVC. Reset game.";
        }
        return winner + "<br><br>" + getScoreBean();
    }

    // Player 2 GET method
    @Path("/scores/player2")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getPlayer2(@QueryParam("getPlayer2Choice") String getPlayer2Choice) throws IOException {
        // Check if game is taken by PVC (Player vs Computer)
        if (gameId != "PVC") {
            // Set player 2 choice
            player2Choice = getPlayer2Choice;
            // Check if player 1 has made a choice
            if (player1Choice != "empty") {
                // Set game mode to PVP (Player vs Player)
                gameId = "PVP";
                // Determine the winner and reset choices
                winner = WhoIsTheWinner.theWinnerIs(player1Choice, player2Choice, gameId);
                ScoreBean.resetChoice();
            }
        } else {
            return "Game is taken by PVC. Reset game.";
        }
        return winner + "<br><br>" + getScoreBean();
    }

    // GET method for player vs computer game
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getPlayer(@QueryParam("getPlayer1Choice") String getPlayer1Choice) throws IOException {
        // Check if game is not already in progress
        if (gameId != "PVP") {
            // Set player 1 and computer choices
            player1Choice = getPlayer1Choice;
            player2Choice = ComputerChoice.setComputerChoice();
            // If computer has made a choice, set game ID, determine winner and reset choices
            if (player2Choice != "empty") {
                gameId = "PVC";
                winner = WhoIsTheWinner.theWinnerIs(player1Choice, player2Choice, gameId);
                ScoreBean.resetChoice();
            }
        } else {
            // Return error message if game is already in progress
            return "Game is taken by PVP. Reset game.";
        }
        // Return winner and current score
        return winner + "<br><br>" + getScoreBean();
    }

    // POST method to increase player 1's score
    @Path("/scores/scoresPlayer1")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public static int increaseScoresPlayer1() {
        ScoreBean.SCORESPLAYER1++;
        return ScoreBean.SCORESPLAYER1;
    }

    // POST method to increase player 2's score
    @Path("/scores/scoresPlayer2")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public static int increaseScoresPlayer2() {
        ScoreBean.SCORESPLAYER2++;
        return ScoreBean.SCORESPLAYER2;
    }

    // POST method to increase ties count
    @Path("/scores/ties")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public static int increaseTies() {
        ScoreBean.TIES++;
        return ScoreBean.TIES;
    }

    // POST method to reset game
    @POST
    @Path("/reset")
    public String resetGame(@QueryParam("resetValue") int resetValue) throws IOException {
        // Reset game with specified value
        ScoreBean.resetGame(resetValue);
        // Return current score
        return getScoreBean();
    }

    // GET method to get current score in JSON format
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getScoreBean() {
        String pattern = "{\"scoresPlayer1\":\"%s\", \"scoresPlayer2\":\"%s\", \"ties\":\"%s\"}";
        return String.format(pattern, ScoreBean.SCORESPLAYER1, ScoreBean.SCORESPLAYER2, ScoreBean.TIES);
    }

    // PUT method to update current score
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public String update(@QueryParam("scoresPlayer1") int scoresPlayer1,
                         @QueryParam("scoresPlayer2") int scoresPlayer2,
                         @QueryParam("ties") int ties) {
        // Update current score
        ScoreBean.SCORESPLAYER1 = scoresPlayer1;
        ScoreBean.SCORESPLAYER2 = scoresPlayer2;
        ScoreBean.TIES = ties;
        // Return updated score in JSON format
        String pattern = "{\"scoresPlayer1\":\"%s\", \"scoresPlayer2\":\"%s\", \"ties\":\"%s\"}";
        return String.format(pattern, ScoreBean.SCORESPLAYER1, ScoreBean.SCORESPLAYER2, ScoreBean.TIES);
    }
}