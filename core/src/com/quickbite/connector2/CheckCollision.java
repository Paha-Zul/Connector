package com.quickbite.connector2;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.quickbite.connector2.screens.GameScreen;

/**
 * Created by Paha on 1/9/2016.
 * A Runnable created to check the intersection between a line segment and all other lines/shapes.
 * This is the brute force method which may be a little slower so we use a Runnable to do it.
 * We could have handled this better with a quadtree or grid, but the game is too simple to make it that complicated.
 */
public class CheckCollision implements Runnable{
    private GameScreen game;
    private Array<GameShape> shapes;
    private Array<Vector2>[] pointLists;
    private GameShape currShape;
    private float mouseX, mouseY;
    private Vector2 prev, curr;
    private int listCounter;

    public CheckCollision(GameScreen game, Array<GameShape> shapes, int listCounter, Array<Vector2>[] pointLists, GameShape currShape, float mouseX, float mouseY, Vector2 prev, Vector2 curr){
        this.game = game;
        this.shapes = shapes;
        this.pointLists = pointLists;
        this.currShape = currShape;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.prev = prev;
        this.curr = curr;
        this.listCounter = listCounter;
    }

    @Override
    public void run() {
        Vector2 intersection = new Vector2();

        for(int i=0;i<this.shapes.size;i++){
            GameShape shape = this.shapes.get(i);
            if(shape.isStarting() || shape.isEnding()) continue; //If starting or ending at the time, pass.

            boolean intersect = Intersector.intersectSegmentCircle(this.prev, this.curr, shape.position, 1024);

            boolean condition = this.currShape.checkInvalidIntersection(shape);

            if(intersect && !condition){
                this.game.setRoundOver(true, GameStats.RoundOver.HitShape);
                return;
            }
        }

        for(int i=0;i<this.pointLists.length;i++){
            if(i == this.listCounter) continue;
            Array<Vector2> list = this.pointLists[i];

            for(int j=0;j<list.size-1;j++){
                Vector2 _curr = list.get(j);
                Vector2 _next = list.get(j+1);

                boolean intersect = Intersector.intersectSegments(_curr, _next, this.prev, this.curr, intersection);

                if(intersect){
                    this.game.setRoundOver(true, GameStats.RoundOver.HitLine);
                    return;
                }
            }

        }
    }
}
