package com.quickbite.connector2;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Paha on 1/20/2016.
 */
public class LogoScreen implements Screen{
    private Game game;

    private int logoSize = 256;
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
            Game.easyAssetManager.get("Hypp_fractal_fireworks", Music.class).play();
            Game.shapeAtlas = Game.easyAssetManager.get("shapes", TextureAtlas.class);
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
