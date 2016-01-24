package com.quickbite.connector;

import com.badlogic.gdx.Screen;

/**
 * Created by Paha on 1/8/2016.
 */
public class MainMenu implements Screen {
    Game game;

    public MainMenu(Game game){
        this.game = game;

        GUIManager.MainMenuGUI.inst().makeGUI(game, this);
    }



    @Override
    public void show() {
        //this.game.setScreen(new GameScreen(this.game));
    }

    @Override
    public void render(float delta) {

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

    }
}
