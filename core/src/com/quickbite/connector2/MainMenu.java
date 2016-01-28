package com.quickbite.connector2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * Created by Paha on 1/8/2016.
 */
public class MainMenu implements Screen {
    Game game;

    public MainMenu(Game game){
        this.game = game;
    }

    @Override
    public void show() {
        GUIManager.MainMenuGUI.inst().makeGUI(game, this);

        //We put this here to reset the input processor from the GameScreen
        // when we come back to the main menu.
        Gdx.input.setInputProcessor(Game.stage);
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
