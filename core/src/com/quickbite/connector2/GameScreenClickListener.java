package com.quickbite.connector2;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Paha on 1/23/2016.
 */
public class GameScreenClickListener implements InputProcessor {
    private GameScreen screen;
    public boolean dragging = false;

    private final float disBetweenPositions = 10;

    public GameScreenClickListener(GameScreen screen){
        this.screen = screen;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldPos = Game.camera.unproject(new Vector3(screenX, screenY, 0));
        this.screen.currShape = null;

        //Check if we clicked/touched on a shape. If so, record it and start dragging.
        for(GameShape shape : this.screen.shapeList){
            if(!shape.locked && shape.isOver(worldPos.x, worldPos.y)){
                this.screen.currShape = shape;
                this.screen.lists[this.screen.lineCounter].add(new Vector2(shape.position.x, shape.position.y));
                this.dragging = true;
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(this.screen.currShape == null || !this.dragging) return false;

        Vector3 worldPos = Game.camera.unproject(new Vector3(screenX, screenY, 0));
        boolean onShape = false;
        this.dragging = false;

        for(GameShape shape : this.screen.shapeList){
            //If a shape is clicked.
            if(shape.isOver(worldPos.x, worldPos.y)){
                if(shape == this.screen.currShape) break;

                boolean condition = false;
                if(GameSettings.matchType == GameSettings.MatchType.Shapes) condition = shape.getShapeType() == this.screen.currShape.getShapeType();
                else if(GameSettings.matchType == GameSettings.MatchType.Color) condition = shape.getColorID() == this.screen.currShape.getColorID();

                //Correct one? one more victory point
                if(condition) {
                    this.screen.winCounter++;

                    //Lock the shapes.
                    this.screen.currShape.locked = true;
                    shape.locked = true;
                    onShape = true;
                    this.screen.lists[this.screen.lineCounter].add(new Vector2(shape.position.x, shape.position.y));
                    this.screen.lineCounter = (this.screen.lineCounter+1)%GameSettings.numShapes;
                }
                break;
            }
        }

        //If we didn't let go on a shape, restart the list.
        if(!onShape) this.screen.lists[this.screen.lineCounter] = new Array<Vector2>(200);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(this.screen.currShape == null || !this.dragging) return false;
        Array<Vector2> list = this.screen.lists[this.screen.lineCounter];
        if(list.size == 0) return false;

        Vector3 worldPos = Game.camera.unproject(new Vector3(screenX, screenY, 0));

        if(list.get(list.size-1).dst(worldPos.x, worldPos.y) > this.disBetweenPositions){
            list.add(new Vector2(worldPos.x, worldPos.y));
            Game.executor.submit(new CheckCollision(this.screen, this.screen.shapeList, this.screen.lineCounter, this.screen.lists,
                    this.screen.currShape, worldPos.x, worldPos.y, list.get(list.size-2), list.get(list.size-1)));
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
