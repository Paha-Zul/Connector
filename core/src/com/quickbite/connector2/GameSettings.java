package com.quickbite.connector2;

/**
 * Created by Paha on 1/12/2016.
 */
public class GameSettings {
    public enum ColorType {Normal, Random}
    public enum MatchType {Shapes, Color}
    public enum GameType {Practice, Fastest, Timed}

    public static int numShapes = 4;
    public static ColorType colorType = ColorType.Random;
    public static MatchType matchType = MatchType.Color;
    public static GameType gameType = GameType.Practice;

}
