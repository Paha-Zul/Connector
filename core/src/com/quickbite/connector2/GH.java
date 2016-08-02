package com.quickbite.connector2;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Paha on 1/24/2016.
 */
public class GH {
    /**
     * Lerps a float value from start to target.
     * @param curr The current value of the lerp.
     * @param start The start value of the lerp.
     * @param target The target value of the lerp.
     * @param seconds The time in seconds for the lerp to happen.
     * @return The value of the lerp after the calculated tick amount.
     */
    public static float lerpValue(float curr, float start, float target, float seconds){
        float amt = (Math.abs(start - target)/seconds)/60f;
        if(start < target) {
            curr += amt;
            if(curr >= target) curr = target;
        }else {
            curr -= amt;
            if(curr <= target) curr = target;
        }

        return curr;
    }

    public static String getLostReason(){
        String reason = "";
        if(GameStats.roundOverReason == GameStats.RoundOver.HitLine)
            reason = "Hit Line";
        if(GameStats.roundOverReason == GameStats.RoundOver.HitShape)
            reason = "Hit Wrong Shape";
        if(GameStats.roundOverReason == GameStats.RoundOver.OutOfTime)
            reason = "Out of Time";

        return reason;
    }

    public static void shuffleArray(Object[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = MathUtils.random(i);
            // Simple swap
            Object a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public static Texture createPixel(Color color){
        return createPixel(color, 1, 1);
    }

    public static Texture createPixel(Color color, int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color.r, color.g, color.b, color.a);
        pixmap.fillRectangle( 0, 0, width, height );
        Texture pixmaptex = new Texture(pixmap);
        pixmap.dispose();

        return pixmaptex;
    }

    public static void submitGameSettingsEvent(){
        String ID = "";
        switch(GameSettings.gameType){
            case Practice:
                ID = Constants.EVENT_PRACTICE;
                break;
            case Fastest:
                ID = Constants.EVENT_BEST;
                break;
            case Timed:
                ID = Constants.EVENT_TIMED;
                break;
            case Frenzy:
                ID = Constants.EVENT_FRENZY;
                break;
            case Nothing:
                break;
        }

        Game.resolver.submitEvent(ID, "");

        ID = "";
        switch(GameSettings.numShapes){
            case 3:
                ID = Constants.EVENT_3;
                break;
            case 4:
                ID = Constants.EVENT_4;
                break;
            case 5:
                ID = Constants.EVENT_5;
                break;
            case 6:
                ID = Constants.EVENT_6;
                break;
        }

        Game.resolver.submitEvent(ID, "");
    }

    /**
     * Calculates a score based on some parameters
     * @param gameType The GameType of the match
     * @param avgTime The average time in milliseconds (3000 = 3 seconds)
     * @param bestTime The best time in milliseconds (3000 = 3 seconds)
     * @param numShapes The number of shapes for the match
     * @param currRound The current round the match ended on (mostly for time attack match)
     * @param successfulRounds The number of successful rounds (mostly for best out of 10)
     * @return A pair that holds the leaderboard ID and the score.
     */
    public static Pair<String, Integer> calcScore(GameSettings.GameType gameType, double avgTime, double bestTime, int numShapes, int currRound, int successfulRounds){
        String leaderboard = "";
        int score = 0;
        //If the game type is best out of 10
        if(gameType == GameSettings.GameType.Fastest) {
            if (avgTime == 0) score = 0;
            else
                score = (int) (successfulRounds*10f + (10f / (avgTime / 1000f))*10f + (numShapes-2)*75 + (10f/(bestTime/1000f))*10f);

            leaderboard = Constants.LEADERBOARD_BEST;

            //If the game type is time attack
        }else if(gameType == GameSettings.GameType.Timed){
            if(avgTime == 0 || currRound == 0) score = 0;
            else score = (int)((currRound-1)*10 + (10f/(avgTime/1000))*10 + (numShapes-2)*75);

            leaderboard = Constants.LEADERBOARD_TIMED;

            //If the game type is challenge.
        }else if(gameType == GameSettings.GameType.Frenzy){
//            if(GameStats.avgTime == 0) GameStats.currScore = 0;
            score = (int)(125*(successfulRounds));

            leaderboard = Constants.LEADERBOARD_FRENZY;
        }

//        saveScore(leaderboard, GameSettings.gameType, GameStats.currScore);
        return new Pair<String, Integer>(leaderboard, score);
    }

    public static String getCurrentLeaderboardTableID(){
        String id = "";
        switch(GameSettings.gameType){
            case Fastest:
                id = Constants.LEADERBOARD_BEST;
                break;
            case Timed:
                id = Constants.LEADERBOARD_TIMED;
                break;
            case Frenzy:
                id = Constants.LEADERBOARD_FRENZY;
                break;
        }

        return id;
    }

    /**
     * Using the current configuration of game settings, compiles a string to represent
     * the game state.
     * @return A String that represents 'gameType:matchingType:colorType:numShapes'
     */
    public static String getCurrGameConfig(){
        String config = "startingGame";

        switch(GameSettings.gameType){
            case Practice:
                config += ":practice";
                break;
            case Fastest:
                config += ":best";
                break;
            case Timed:
                config += ":timed";
                break;
            case Frenzy:
                config += ":frenzy";
                break;
            default:
                config += ":none";
        }

        switch(GameSettings.matchType){
            case Shapes:
                config += ":shapes";
                break;
            case Color:
                config += ":colors";
                break;
            default:
                config += ":none";
        }

        switch(GameSettings.colorType){
            case Normal:
                config += ":same";
                break;
            case Random:
                config += ":random";
                break;
            default:
                config += ":none";
                break;
        }

        switch(GameSettings.numShapes){
            case 3:
                config += ":3";
                break;
            case 4:
                config += ":4";
                break;
            case 5:
                config += ":5";
                break;
            case 6:
                config += ":6";
                break;
            default:
                config += ":0";
                break;
        }

        return config;
    }
}
