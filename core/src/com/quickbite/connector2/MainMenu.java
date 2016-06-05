package com.quickbite.connector2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.quickbite.connector2.gui.MainMenuGUI;

/**
 * Created by Paha on 1/8/2016.
 */
public class MainMenu implements Screen {
    private Game game;

    private double nextTick, interval = 500; //in millis

    private Array<GameShape> gameShapeList = new Array<GameShape>(2);
    private final float timeBetweenConnections = 1f, timeToConnect = 0.5f, shapeLifeTime = 0.8f;
    private float timeBetweenShapesCounter, timeToConnectCounter, timeBeforeStartCounter;
    private boolean connecting = false;

    public MainMenu(Game game){
        this.game = game;
    }

    @Override
    public void show() {
        MainMenuGUI.makeGUI(game, this);

        //We put this here to reset the input processor from the GameScreen
        // when we come back to the main menu.
        Gdx.input.setInputProcessor(Game.stage);

        Game.adInterface.showAdmobBannerAd();
    }

    @Override
    public void render(float delta) {
        double time = TimeUtils.millis();

        //Check if we need to enable/disable some buttons
        if(time >= this.nextTick){
            this.nextTick =  time + interval;
            MainMenuGUI.changeLoginButton(Game.resolver.getSignedInGPGS());
        }

//        showShapePlay(delta);
    }

    private void showShapePlay(float delta){
        //If our shape list is empty, we need to begin the new shapes at some point
        if(gameShapeList.size == 0){
            //If our time between shapes counter is greater than the limit, spawn new shapes!
            if(timeBetweenShapesCounter >= timeBetweenConnections){
                timeBetweenShapesCounter = 0;

                //Make the shapes.
                final GameShape shape1 = new GameShape(new Vector2(100, 100), 0, 80, GameData.colorMap.get("Blue"), shapeLifeTime);
                final GameShape shape2 = new GameShape(new Vector2(400, 600), 0, 80, GameData.colorMap.get("Red"), shapeLifeTime);

                //Assign callbacks.
                shape1.onDeadCallback = new ICallback() {
                    @Override
                    public void run() {
                        gameShapeList.removeValue(shape1, true);
                    }
                };

                shape2.onDeadCallback = new ICallback() {
                    @Override
                    public void run() {
                        gameShapeList.removeValue(shape2, true);
                    }
                };

                //Add
                gameShapeList.add(shape1);
                gameShapeList.add(shape2);

            }else
                timeBetweenShapesCounter+=delta;

        //Otherwise we should deal with the current shapes.
        }else{
            if(timeBeforeStartCounter >= 1f && !connecting){
                timeBeforeStartCounter = 0f;
                connecting = true;
            }else
                timeBeforeStartCounter += delta;

            if(connecting){
                //If we are just starting.
                if(timeToConnectCounter <= 0){

                //If we are in the middle
                }else if(timeToConnectCounter < timeToConnect){

                //If we are ending
                }else if(timeToConnectCounter >= timeToConnect){

                }

                timeToConnectCounter+=delta;
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        Game.adInterface.hideAdmobBannerAd();
    }
}
