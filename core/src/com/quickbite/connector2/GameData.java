package com.quickbite.connector2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
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
    public static Array<GameShape> gameShapeList = new Array<GameShape>();
    public static TextureRegion[] shapeTextures;
    public static TextureRegion[] shapesGlow;

    static{
        colorMap.put("Red", Color.RED);
        colorMap.put("Blue", new Color(56f/255f, 122f/255f, 244f/255f, 1f));
        colorMap.put("Green", Color.GREEN);
        colorMap.put("Gold", Color.GOLD);
        colorMap.put("Red", Color.RED);
        colorMap.put("Cyan", Color.CYAN);
        colorMap.put("Orange", Color.ORANGE);

        shapeTextures = new TextureRegion[6];
        shapeTextures[0] = Game.shapeAtlas.findRegion("Star");
        shapeTextures[1] = Game.shapeAtlas.findRegion("Square");
        shapeTextures[2] = Game.shapeAtlas.findRegion("Circle");
        shapeTextures[3] = Game.shapeAtlas.findRegion("Diamond");
        shapeTextures[4] = Game.shapeAtlas.findRegion("Triangle");
        shapeTextures[5] = Game.shapeAtlas.findRegion("Hexagon");

        shapesGlow = new TextureRegion[6];
        shapesGlow[0] = Game.shapeAtlas.findRegion("Star_glow");
        shapesGlow[1] = Game.shapeAtlas.findRegion("Square_glow");
        shapesGlow[2] = Game.shapeAtlas.findRegion("Circle_glow");
        shapesGlow[3] = Game.shapeAtlas.findRegion("Diamond_glow");
        shapesGlow[4] = Game.shapeAtlas.findRegion("Triangle_glow");
        shapesGlow[5] = Game.shapeAtlas.findRegion("Hexagon_glow");

        ParticleEffect effect = new ParticleEffect();
        effect.load(Gdx.files.internal("particles/explosion.p"), Gdx.files.internal("particles/"));

        GameData.explosionEffectPool = new ParticleEffectPool(effect, 4, 10);
    }

    public static void gameInit(){
        int n = GameSettings.numShapes == 0 ? 6 : 3 + GameSettings.numShapes;
        sizeOfSpots = (Game.camera.viewportWidth - playAreaPadding.getLeft() - playAreaPadding.getRight())/n;
        sizeOfSpots = (sizeOfSpots*10 - 1)/10f;
        sizeOfShapes = (Game.camera.viewportWidth - playAreaPadding.getLeft() - playAreaPadding.getRight())/n;
    }

    public static Color getRandomColor(){
        return colorMap.values().toArray(new Color[colorMap.size()-1])[MathUtils.random(colorMap.size()-1)];
    }

    public static void reset(){
        particleEffects = new Array<ParticleEffect>();
        gameShapeList = new Array<GameShape>();
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
