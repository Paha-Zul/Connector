package com.quickbite.connector2.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.quickbite.connector2.Game;
import com.quickbite.connector2.GameData;
import com.quickbite.connector2.SoundManager;

/**
 * Created by Paha on 1/20/2016.
 * Acts as the loading screen. Displays the company logo while loading assets.
 */
public class LogoScreen implements Screen{
    private Game game;

    private boolean logoDone = false;

    public LogoScreen(Game game){
        this.game = game;
    }

    @Override
    public void show() {
        Texture logoTex = new Texture("art/load/Logo-DarkBackground.png");
        logoTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion logo = new TextureRegion(logoTex);

        Image logoImage = new Image(logo);
        logoImage.getColor().a = 0f;
        logoImage.setSize(256, 256);
        logoImage.setOrigin(Align.center);
        logoImage.setPosition(Game.viewport.getWorldWidth()/2f - 128f, Game.viewport.getWorldHeight()/2f - 128f);

        logoImage.addAction(Actions.sequence(Actions.fadeIn(1f), Actions.delay(1.5f), Actions.fadeOut(1f), new Action() {
            @Override
            public boolean act(float delta) {
                logoDone = true;
                return true;
            }
        }));

        logoImage.addAction(Actions.scaleTo(1.1f, 1.1f, 4f));

        Game.stage.addActor(logoImage);
    }

    @Override
    public void render(float delta) {
        boolean assetsLoaded = Game.easyAssetManager.update();
        if(assetsLoaded && logoDone){
            Game.easyAssetManager.finishLoading();
            SoundManager.playMusic();
            Game.shapeAtlas = Game.easyAssetManager.get("shapes", TextureAtlas.class);
            Game.UIAtlas = Game.easyAssetManager.get("UI", TextureAtlas.class);
            Game.defaultHugeFont = Game.easyAssetManager.get("default", BitmapFont.class);
            Game.defaultHugeFont.getData().markupEnabled = true;
            Game.defaultHugeFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            GameData.init();
            game.setScreen(new MainMenu(game));
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

    }
}
