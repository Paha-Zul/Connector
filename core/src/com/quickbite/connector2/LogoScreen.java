package com.quickbite.connector2;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Paha on 1/20/2016.
 */
public class LogoScreen implements Screen{
    private TextureRegion logo;
    private int state = 0;
    private Game game;

    private int logoSize = 256;
    private float fadeCounter, currAlpha, alphaFadeInAmount, alphaFadeOutAmount;
    private float fadeInTime=0.4f, fadeOutTime=0.4f, idleTime=0.7f, currCounter=0, currNextTime;

    public LogoScreen(Game game){
        this.game = game;
    }

    @Override
    public void show() {
        this.logo = new TextureRegion(Game.easyAssetManager.get("Logo-DarkBackground", Texture.class));
        this.alphaFadeInAmount = (1f/60f)/fadeInTime;
        this.alphaFadeOutAmount = (1f/60f)/fadeOutTime;
        this.currNextTime = this.fadeInTime*60f;

        Image logoImage = new Image(logo);
        logoImage.getColor().a = 0f;
        logoImage.setSize(256, 256);
        logoImage.setOrigin(Align.center);
        logoImage.setPosition(Game.viewport.getWorldWidth()/2f - 128f, Game.viewport.getWorldHeight()/2f - 128f);

        logoImage.addAction(Actions.sequence(Actions.fadeIn(1f), Actions.delay(1.5f), Actions.fadeOut(1f), new Action() {
            @Override
            public boolean act(float delta) {
                game.setScreen(new MainMenu(game));
                return true;
            }
        }));
        logoImage.addAction(Actions.scaleTo(1.1f, 1.1f, 4f));

        Game.stage.addActor(logoImage);
    }

    @Override
    public void render(float delta) {
        this.currCounter++;

        Game.batch.begin();

        Color color = Game.batch.getColor();
        Game.batch.setColor(color.r, color.g, color.b, this.currAlpha);
        Game.batch.draw(this.logo, 0 - logoSize/2, 0 - logoSize/2, logoSize, logoSize);
        Game.batch.setColor(color);

        Game.batch.end();
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
