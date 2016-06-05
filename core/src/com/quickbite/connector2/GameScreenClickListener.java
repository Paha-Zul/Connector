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
        Vector3 worldPos = Game.viewport.unproject(new Vector3(screenX, screenY, 0));
        this.screen.currShape = null;

        //Check if we clicked/touched on a shape. If so, record it and start dragging.
        for(GameShape shape : this.screen.gameShapeList){
            if(!shape.isLocked() && shape.isOver(worldPos.x, worldPos.y)){
                this.screen.currShape = shape;
                this.screen.lineLists[this.screen.lineCounter].add(new Vector2(shape.position.x, shape.position.y));
                this.dragging = true;
                SoundManager.playSound("pop", 0.75f);
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

        for(GameShape shape : this.screen.gameShapeList){
            //If a shape is clicked.
            if(shape.isOver(worldPos.x, worldPos.y)){
                if(shape == this.screen.currShape || shape.isLocked() || shape.isStarting() || shape.isEnding()) break;

                boolean condition = this.screen.currShape.checkValidConnection(shape);

                //Correct one? one more victory point
                if(condition) {
                    GameStats.winCounter++;


                    onShape = true;
                    this.screen.lineLists[this.screen.lineCounter].add(new Vector2(shape.position.x, shape.position.y));

                    this.screen.shapesConnected(this.screen.currShape, shape);
                    SoundManager.playSound("pop", 0.75f);
                }
                break;
            }
        }

        //If we didn't let go on a shape, restartGame the list.
        if(!onShape) this.screen.lineLists[this.screen.lineCounter] = new Array<Vector2>(200);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(this.screen.currShape == null || !this.dragging) return false;

        if(this.screen.currShape.isStarting() || this.screen.currShape.isEnding()){
            this.screen.lineLists[this.screen.lineCounter] = new Array<Vector2>(200);
            this.dragging = false;
        }

        Array<Vector2> list = this.screen.lineLists[this.screen.lineCounter];
        if(list.size == 0) return false;

        Vector3 worldPos = Game.camera.unproject(new Vector3(screenX, screenY, 0));

        if(list.get(list.size-1).dst(worldPos.x, worldPos.y) > this.disBetweenPositions){
            list.add(new Vector2(worldPos.x, worldPos.y));
            Game.executor.submit(new CheckCollision(this.screen, this.screen.gameShapeList, this.screen.lineCounter, this.screen.lineLists,
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
