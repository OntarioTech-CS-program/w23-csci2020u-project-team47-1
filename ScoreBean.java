package com.example.rpsgroupproject;

import java.io.IOException;

// Declare a public class named ScoreBean
public class ScoreBean {

    // Declare public static integer variables named SCORESPLAYER1, SCORESPLAYER2, and TIES
    public static int SCORESPLAYER1, SCORESPLAYER2, TIES;

    // Declare a public static method named resetGame that takes an integer argument and throws an IOException
    // The method resets the values of SCORESPLAYER1, SCORESPLAYER2, and TIES to the resetValue
    // The method sets the values of two static variables named RPS.gameId and RPS.winner
    // The method calls the resetChoice() method
    public static void resetGame(int resetValue) throws IOException {
        ScoreBean.SCORESPLAYER1 = resetValue;
        ScoreBean.SCORESPLAYER2 = resetValue;
        ScoreBean.TIES = resetValue;
        RPS.gameId = "noId";
        RPS.winner = "Not ready. Refresh page in a while.";
        resetChoice();
    }

    // Declare a public static method named resetChoice
    // The method sets the values of two static variables named RPS.player1Choice and RPS.player2Choice to "empty"
    public static void resetChoice() {
        RPS.player1Choice = "empty";
        RPS.player2Choice = "empty";
    }
}


