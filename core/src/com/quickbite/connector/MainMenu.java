package com.quickbite.connector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Paha on 1/8/2016.
 */
public class MainMenu implements Screen {
    Game game;
    private Table table;

    private TextButton start, quit, colorSame, colorRandom, matchShape, matchColor, modePractice, modeBest, modeTimed;

    public MainMenu(Game game){
        this.game = game;

        this.table = new Table();

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(new Texture("defaultButton_normal.png")));
        style.down = new TextureRegionDrawable(new TextureRegion(new Texture("defaultButton_down.png")));
        style.font = Game.defaultFont;

        start = new TextButton("Start", style);
        quit = new TextButton("Quit", style);
        colorSame = new TextButton("Same Color", style);
        colorRandom = new TextButton("Random Color", style);
        matchShape = new TextButton("Match Shapes", style);
        matchColor = new TextButton("Match Colors", style);
        modePractice = new TextButton("Practice", style);
        modeBest = new TextButton("Best", style);
        modeTimed = new TextButton("Timed", style);

        final Game gameVar = game;

        start.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                table.clear();
                table.add(colorSame).size(200, 75);
                table.row().padTop(75);
                table.add(colorRandom).size(200, 75);
            }
        });

        quit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        colorSame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                table.clear();
                table.add(matchShape).size(200, 75);
                table.row().padTop(75);
                table.add(matchColor).size(200, 75);
                GameSettings.colorType = GameSettings.ColorType.Normal;
            }
        });

        colorRandom.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                table.clear();
                table.add(matchShape).size(200, 75);
                table.row().padTop(75);
                table.add(matchColor).size(200, 75);
                GameSettings.colorType = GameSettings.ColorType.Random;
            }
        });

        matchShape.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                table.clear();
                table.add(modePractice).size(200, 75);
                table.row().padTop(75);
                table.add(modeBest).size(200, 75);
                table.row().padTop(75);
                table.add(modeTimed).size(200, 75);
                GameSettings.matchType = GameSettings.MatchType.Shapes;
            }
        });

        matchColor.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                table.clear();
                table.add(modePractice).size(200, 75);
                table.row().padTop(75);
                table.add(modeBest).size(200, 75);
                table.row().padTop(75);
                table.add(modeTimed).size(200, 75);
                GameSettings.matchType = GameSettings.MatchType.Color;
            }
        });

        modePractice.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Game.stage.clear();
                GameSettings.gameType = GameSettings.GameType.Practice;
                gameVar.setScreen(new GameScreen(gameVar));
            }
        });

        modeBest.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Game.stage.clear();
                GameSettings.gameType = GameSettings.GameType.Fastest;
                gameVar.setScreen(new GameScreen(gameVar));
            }
        });

        modeTimed.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Game.stage.clear();
                GameSettings.gameType = GameSettings.GameType.Timed;
                gameVar.setScreen(new GameScreen(gameVar));
            }
        });

        this.table.add(start).size(200, 75);
        this.table.row().padTop(75);
        this.table.add(quit).size(200, 75);

        this.table.setFillParent(true);

        Game.stage.addActor(this.table);
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
