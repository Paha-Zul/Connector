package com.quickbite.connector2;

/**
 * Created by Paha on 5/2/2016.
 * Where game stats are held like average time, reason we messed up, etc...
 */
public class GameStats {
    public enum RoundOver {HitShape, HitLine, OutOfTime, Won}

    static int currRound, maxRounds=10, successfulRounds, currScore, winCounter;
    static float roundTime, roundTimeDecreaseAmount = 1, roundTimeStart = 10;
    static double startTime, endTime, bestTime, avgTime;
    static boolean failedLastRound;
    static RoundOver roundOverReason;

}
