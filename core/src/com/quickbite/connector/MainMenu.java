package com.quickbite.connector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Paha on 1/8/2016.
 */
public class MainMenu implements Screen {
    Game game;
    private Table table;

    private TextButton start, quit, colorSame, colorRandom, matchShape, matchColor, modePractice, modeBest, modeTimed, startGame;
    private TextButton threeShapes, fourShapes, fiveShapes, sixShapes;

    public MainMenu(Game game){
        this.game = game;

        this.table = new Table();

        TextButton.TextButtonStyle regularStyle = new TextButton.TextButtonStyle();
        regularStyle.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_normal", Texture.class)));
        regularStyle.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_down", Texture.class)));
        regularStyle.checked = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_down", Texture.class)));
        regularStyle.font = Game.defaultFont;

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_clear", Texture.class)));
        style.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_green", Texture.class)));
        style.checked = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_green", Texture.class)));
        style.font = Game.defaultFont;

        start = new TextButton("Start", regularStyle);
        quit = new TextButton("Quit", regularStyle);

        threeShapes = new TextButton("3", style);
        fourShapes = new TextButton("4", style);
        fiveShapes = new TextButton("5", style);
        sixShapes = new TextButton("6", style);

        colorSame = new TextButton("Same", style);
        colorRandom = new TextButton("Random", style);

        matchShape = new TextButton("Shapes", style);
        matchColor = new TextButton("Colors", style);

        modePractice = new TextButton("Practice", style);
        modeBest = new TextButton("Best", style);
        modeTimed = new TextButton("Timed", style);

        startGame = new TextButton("Start", regularStyle);

        final Game gameVar = game;

        start.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                table.clear();
                choicesMenu();
            }
        });

        quit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        threeShapes.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameSettings.numShapes = 3;
                fourShapes.setChecked(false);
                fiveShapes.setChecked(false);
                sixShapes.setChecked(false);
            }
        });

        fourShapes.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameSettings.numShapes = 4;
                threeShapes.setChecked(false);
                fiveShapes.setChecked(false);
                sixShapes.setChecked(false);
            }
        });

        fiveShapes.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameSettings.numShapes = 5;
                threeShapes.setChecked(false);
                fourShapes.setChecked(false);
                sixShapes.setChecked(false);
            }
        });

        sixShapes.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameSettings.numShapes = 6;
                threeShapes.setChecked(false);
                fourShapes.setChecked(false);
                fiveShapes.setChecked(false);
            }
        });

        colorSame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameSettings.colorType = GameSettings.ColorType.Normal;
                colorRandom.setChecked(false);
            }
        });

        colorRandom.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameSettings.colorType = GameSettings.ColorType.Random;
                colorSame.setChecked(false);
            }
        });

        matchShape.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameSettings.matchType = GameSettings.MatchType.Shapes;
                matchColor.setChecked(false);
            }
        });

        matchColor.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameSettings.matchType = GameSettings.MatchType.Color;
                matchShape.setChecked(false);
            }
        });

        modePractice.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameSettings.gameType = GameSettings.GameType.Practice;
                modeTimed.setChecked(false);
                modeBest.setChecked(false);
            }
        });

        modeBest.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameSettings.gameType = GameSettings.GameType.Fastest;
                modeTimed.setChecked(false);
                modePractice.setChecked(false);
            }
        });

        modeTimed.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameSettings.gameType = GameSettings.GameType.Timed;
                modeBest.setChecked(false);
                modePractice.setChecked(false);
            }
        });

        startGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Game.stage.clear();
                gameVar.setScreen(new GameScreen(gameVar));
            }
        });

        this.table.add(start).size(200, 75);
        this.table.row().padTop(75);
        this.table.add(quit).size(200, 75);

        this.table.setFillParent(true);
        //this.table.debug();

        Game.stage.addActor(this.table);
    }

    private void choicesMenu(){
        Label.LabelStyle titleStyle = new Label.LabelStyle(Game.defaultFont, Color.WHITE);
        titleStyle.background = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("pixel", Texture.class)));

        Label.LabelStyle style = new Label.LabelStyle(Game.defaultFont, Color.WHITE);

        Label numShapesLabel = new Label("Num Shapes", titleStyle);
        numShapesLabel.setAlignment(Align.center);

        Label colorLabel = new Label("Color", titleStyle);
        colorLabel.setAlignment(Align.center);

        Label matchLabel = new Label("Matching", titleStyle);
        matchLabel.setAlignment(Align.center);

        Label modeLabel = new Label("Mode", titleStyle);
        modeLabel.setAlignment(Align.center);

        Table numShapesTable = new Table();
        numShapesTable.add(this.threeShapes).size(50 ,50);
        numShapesTable.add(this.fourShapes).size(50 ,50);
        numShapesTable.add(this.fiveShapes).size(50 ,50);
        numShapesTable.add(this.sixShapes).size(50 ,50);

        Table colorTable = new Table();
        colorTable.add(this.colorSame).size(125, 50);
        colorTable.add(this.colorRandom).size(125, 50);

        Table matchTable = new Table();
        matchTable.add(this.matchShape).size(125, 50);
        matchTable.add(this.matchColor).size(125, 50);

        Table modeTable = new Table();
        modeTable.add(this.modePractice).size(125, 50).padRight(1);
        modeTable.add(this.modeBest).size(125, 50).padRight(1);
        modeTable.add(this.modeTimed).size(125, 50).padRight(1);

        this.table.add(numShapesLabel).expandX().fillX().center().padBottom(10).height(40);
        this.table.row();
        this.table.add(numShapesTable).expandX();
        this.table.row().padTop(75);

        this.table.add(colorLabel).expandX().fillX().center().padBottom(10).height(40);
        this.table.row();
        this.table.add(colorTable).expandX();
        this.table.row().padTop(75);

        this.table.add(matchLabel).expandX().fillX().center().padBottom(10).height(40);
        this.table.row().padTop(10);
        this.table.add(matchTable).expandX();
        this.table.row().padTop(75);

        this.table.add(modeLabel).expandX().fillX().center().padBottom(10).height(40);
        this.table.row().padTop(10);
        this.table.add(modeTable).expandX();
        this.table.row().padTop(75);

        this.table.add(this.startGame).size(125, 50);

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
