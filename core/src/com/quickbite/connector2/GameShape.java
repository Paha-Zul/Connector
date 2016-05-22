package com.quickbite.connector2;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Paha on 1/8/2016.
 */
public class GameShape {
    public Vector2 position;
    public boolean locked = false, starting = false;

    private int shapeType, colorID;
    public Rectangle bounds;

    public GameShape(Vector2 position, int shape, int colorID){
        final float bonus = 1.5f;

        this.position = new Vector2(position.x, position.y);
        this.shapeType = shape;

        this.bounds = new Rectangle(this.position.x - 32*bonus, this.position.y - 32*bonus, 64*bonus, 64*bonus);

        this.colorID = colorID;

        this.starting = true;
    }

    public boolean isOver(float mouseX, float mouseY){
        return this.bounds.contains(mouseX, mouseY);
    }

    public int getShapeType(){
        return this.shapeType;
    }

    public int getColorID(){
        return this.colorID;
    }
}
