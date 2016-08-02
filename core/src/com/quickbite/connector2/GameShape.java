package com.quickbite.connector2;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.quickbite.connector2.screens.GameScreen;

/**
 * Created by Paha on 1/8/2016.
 */
public class GameShape {
    public static GameScreen gameScreen;

    public Rectangle bounds;
    public Vector2 position;
    private boolean locked = false, starting = true, ending = false, dead = false;

    private int shapeType, size, lineNumber = -1;
    private Color color;
    private float currRotation, currScale;
    private float lifeTime = -1; //For the 'challenge' game mode.
    public ICallback onDeadCallback, onEndingCallback;

    private float opacity = 1f;
    private float scaleSpeed = 0.5f;
    private float rotationSpeed = 20f;

    public GameShape(Vector2 position, int shape, int size, Color color){
        final float bonus = 1.5f;

        this.position = new Vector2(position.x, position.y);
        this.shapeType = shape;
        this.bounds = new Rectangle(this.position.x - 32*bonus, this.position.y - 32*bonus, 64*bonus, 64*bonus);
        this.color = new Color(color); //We make a copy of it to not change the original color object.
        this.size = size;

        this.starting = true;
    }

    public GameShape(Vector2 position, int shape, int size, Color color, float lifeTime){
        this(position, shape, size, color);
        this.lifeTime = lifeTime;
    }

    public GameShape(Vector2 position, int shape, int size, Color color, float lifeTime, ICallback onEndingCallback, ICallback onDeadCallback){
        this(position, shape, size, color, lifeTime);
        this.onDeadCallback = onDeadCallback;
        this.onEndingCallback = onEndingCallback;
    }

    public void render(SpriteBatch batch, float delta){
        TextureRegion region;
        batch.setColor(this.color);
        if(this.locked) region = GameData.shapesGlow[this.getShapeType()];
        else region = GameData.shapeTextures[this.getShapeType()];
        batch.draw(region, -Game.viewport.getWorldWidth()/2f + this.position.x - size*0.5f, -Game.viewport.getWorldHeight()/2f + this.position.y - size*0.5f,
                size*0.5f, size*0.5f, this.size, this.size, this.currScale, this.currScale, this.currRotation);

        if(starting){
            this.currScale = GH.lerpValue(this.currScale, 0, 1, scaleSpeed);
            this.currRotation += rotationSpeed;
            if(this.currScale >= 1f){
                this.setStarted();
            }
        }else if(ending){
            this.currScale = GH.lerpValue(this.currScale, 1, 0, scaleSpeed);
            this.currRotation += rotationSpeed;
            if(this.currScale <= 0f){
                this.currRotation = 0;
                this.currScale = 0f;
                this.ending = false;
                this.dead = true;
                if(this.onDeadCallback != null) this.onDeadCallback.run();
            }
        }else{
            if(lifeTime > 0){
                lifeTime -= delta;
                if(lifeTime <= 0){
                    this.ending = true;
                    if(this.onEndingCallback != null) this.onEndingCallback.run();
                    if(lineNumber >= 0) gameScreen.lineLists[lineNumber] = new Array<Vector2>(200);
                }
            }
        }
    }

    public void setStarted(){
        this.starting = false;
        this.currScale = 1f;
        this.currRotation = 0f;
    }

    public void setEnding(){
        this.ending = true;
    }

    public void setLocked(boolean locked){
        this.locked = locked;
    }

    public void setLifeTime(float lifeTime){
        this.lifeTime = lifeTime;
    }

    public void setLineNumber(int lineNumber){
        this.lineNumber = lineNumber;
    }

    public void setScaleSpeed(float speed){
        this.scaleSpeed = speed;
        this.rotationSpeed = (3.14f*4f)/scaleSpeed;
    }

    public void setOpacity(float opacity){
        this.color.a = opacity;
    }

    public boolean isOver(float mouseX, float mouseY){
        return this.bounds.contains(mouseX, mouseY);
    }

    public int getShapeType(){
        return this.shapeType;
    }

    public Color getColor(){
        return this.color;
    }

    public boolean isStarting(){
        return this.starting;
    }

    public boolean isEnding(){
        return this.ending;
    }

    public boolean isLocked(){
        return this.locked;
    }

    public boolean isDead(){
        return this.dead;
    }

    public float getLifetime(){
        return lifeTime;
    }

    public int getLineNumber(){
        return lineNumber;
    }

    public boolean checkValidConnection(GameShape otherShape){
        boolean condition = false;
        if(!this.starting && !this.ending && !otherShape.starting && !otherShape.ending && !this.locked && !otherShape.locked) {
            if (GameSettings.matchType == GameSettings.MatchType.Shapes)
                condition = this.getShapeType() == otherShape.getShapeType();
            else if (GameSettings.matchType == GameSettings.MatchType.Color)
                condition = this.getColor().equals(otherShape.getColor());
        }

        return condition;
    }

    public boolean checkInvalidIntersection(GameShape otherShape){
        boolean condition = false;
        if(!this.starting && !this.ending && !otherShape.starting && !otherShape.ending) {
            if (GameSettings.matchType == GameSettings.MatchType.Shapes)
                condition = this.getShapeType() == otherShape.getShapeType();
            else if (GameSettings.matchType == GameSettings.MatchType.Color) {
                condition = this.getColor().equals(otherShape.getColor());
            }
        }

        return condition;
    }

    @Override
    public String toString() {
        return shapeType+"/"+color;
    }
}
