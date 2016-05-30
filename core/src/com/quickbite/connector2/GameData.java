package com.quickbite.connector2;

import com.badlogic.gdx.graphics.Color;
import javafx.geometry.Insets;

import java.util.HashMap;

/**
 * Created by Paha on 5/29/2016.
 */
public class GameData {
    public static HashMap<String, Color> colorMap = new HashMap<String, Color>();
    public final static Insets padding = new Insets(10f, 0f, 10f, 10f);

    static{
        colorMap.put("Red", Color.RED);
        colorMap.put("Blue", new Color(56f/255f, 122f/255f, 244f/255f, 1f));
        colorMap.put("Green", Color.GREEN);
        colorMap.put("Gold", Color.GOLD);
        colorMap.put("Red", Color.RED);
        colorMap.put("Cyan", Color.CYAN);
        colorMap.put("Orange", Color.ORANGE);
    }
}
