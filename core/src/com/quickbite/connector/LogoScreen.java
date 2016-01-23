package com.quickbite.connector;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
    }

    @Override
    public void render(float delta) {
        this.currCounter++;
        if(this.currCounter >= this.currNextTime){
            this.state++;
            if(this.state == 1) this.currNextTime = this.idleTime*60f;
            else if(this.state == 2) this.currNextTime = this.fadeOutTime*60f;
            this.currCounter = 0;

        }else{
            if(this.state == 0) this.currAlpha += this.alphaFadeInAmount;
            else if(this.state == 1) this.currAlpha = 1;
            else if(this.state == 2) this.currAlpha -= this.alphaFadeOutAmount;
            else if(this.state == 3) this.game.setScreen(new MainMenu(this.game));

            if(this.currAlpha > 1) this.currAlpha = 1;
            if(this.currAlpha < 0) this.currAlpha = 0;
        }

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
