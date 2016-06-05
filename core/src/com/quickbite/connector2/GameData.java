package com.quickbite.connector2;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * Created by Paha on 5/29/2016.
 * Holds the data mainly for the GameScreen. Can also be used for other instances (like main menu?)
 */
public class GameData {
    public static HashMap<String, Color> colorMap = new HashMap<String, Color>();
    public static final Padding playAreaPadding = new Padding(0f, 25f, 25f, 25f);
    public static float sizeOfSpots, sizeOfShapes;
    public static Array<ParticleEffect> particleEffects = new Array<ParticleEffect>();
    public static ParticleEffectPool explosionEffectPool;

    static{
        colorMap.put("Red", Color.RED);
        colorMap.put("Blue", new Color(56f/255f, 122f/255f, 244f/255f, 1f));
        colorMap.put("Green", Color.GREEN);
        colorMap.put("Gold", Color.GOLD);
        colorMap.put("Red", Color.RED);
        colorMap.put("Cyan", Color.CYAN);
        colorMap.put("Orange", Color.ORANGE);

        int n = GameSettings.numShapes == 0 ? 6 : 3 + GameSettings.numShapes;
        sizeOfSpots = (Game.camera.viewportWidth - playAreaPadding.getLeft() - playAreaPadding.getRight())/n;
        sizeOfSpots = (sizeOfSpots*10 - 1)/10f;
        sizeOfShapes = (Game.camera.viewportWidth - playAreaPadding.getLeft() - playAreaPadding.getRight())/n;
    }

    public static class Padding{
        private float top, right, bottom, left;

        public Padding(float top, float right, float bottom, float left){
            this.top = top;
            this.right = right;
            this.bottom = bottom;
            this.left = left;
        }

        public float getTop(){
            return top;
        }

        public float getRight(){
            return right;
        }

        public float getBottom(){
            return bottom;
        }

        public float getLeft(){
            return left;
        }
    }
}
