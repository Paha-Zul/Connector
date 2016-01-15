package com.quickbite.connector;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Paha on 1/9/2016.
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
            boolean intersect = Intersector.intersectSegmentCircle(this.prev, this.curr, shape.position, 1024);

            boolean condition = false;
            if(GameSettings.matchType == GameSettings.MatchType.Shapes) condition = shape.getShapeType() == this.currShape.getShapeType();
            else if(GameSettings.matchType == GameSettings.MatchType.Color) condition = shape.getColorID() == this.currShape.getColorID();

            if(intersect && !condition){
                this.game.setGameOver(true);
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
                    this.game.setGameOver(true);
                    return;
                }
            }

        }
    }
}
