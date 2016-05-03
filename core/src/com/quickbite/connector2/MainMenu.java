package com.quickbite.connector2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.TimeUtils;
import com.quickbite.connector2.gui.MainMenuGUI;

/**
 * Created by Paha on 1/8/2016.
 */
public class MainMenu implements Screen {
    Game game;

    private double nextTick, interval = 500; //in millis

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
            if(Game.resolver.getSignedInGPGS()){
                MainMenuGUI.loginGPG.setDisabled(true);
                MainMenuGUI.leaderboards.setDisabled(false);
            } else if (Game.resolver.getSignedInGPGS()) {
                MainMenuGUI.loginGPG.setDisabled(false);
                MainMenuGUI.leaderboards.setDisabled(true);
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
