package com.quickbite.connector2;

/**
 * Created by Paha on 1/12/2016.
 */
public class GameSettings {
    public enum ColorType {Normal, Random, Nothing}
    public enum MatchType {Shapes, Color, Nothing}
    public enum GameType {Practice, Fastest, Timed, Frenzy, Nothing}

    public static int numShapes = 0;
    public static ColorType colorType = ColorType.Random;
    public static MatchType matchType = MatchType.Color;
    public static GameType gameType = GameType.Practice;

    public static void reset(){
        colorType = ColorType.Nothing;
        matchType = MatchType.Nothing;
        gameType = GameType.Nothing;
        numShapes = 0;
    }

    public static boolean checkAllSelected(){
        return colorType != ColorType.Nothing && matchType != MatchType.Nothing && gameType != GameType.Nothing && numShapes != 0;
    }

}
