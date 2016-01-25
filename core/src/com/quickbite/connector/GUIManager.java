package com.quickbite.connector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Paha on 1/23/2016.
 */
public class GUIManager {

    public static class GameScreenGUI {
        public Table mainTable = new Table();
        public Image gameOverImage;
        public Label roundLabel, avgTimeLabel, colorTypeLabel, matchTypeLabel, gameTypeLabel, timerLabel;
        public TextButton restartButton, mainMenuButton;
        public ImageButton backButton;


        /* Starting screen stuff */
        private int state = 0, innerState = 0;
        private Label startingColorType, startingMatchType, startingGameType;
        private Table startingTable;
        private Image firstShape, secondShape, overlay;
        private float waitTime = 0;


        private static GameScreenGUI instance;

        public void makeGUI(final Game game, final GameScreen gameScreen) {
            this.mainTable = new Table();
            this.mainTable.setFillParent(true);
            Game.stage.addActor(this.mainTable);

            TextureRegion arrow = new TextureRegion(Game.easyAssetManager.get("leftArrow", Texture.class));

            ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
            imageButtonStyle.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
            imageButtonStyle.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_down", Texture.class)));
            imageButtonStyle.imageUp = new TextureRegionDrawable(arrow);
            imageButtonStyle.imageDown = new TextureRegionDrawable(arrow);

            this.backButton = new ImageButton(imageButtonStyle);
            this.backButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Game.stage.clear();
                    game.setScreen(new MainMenu(game));
                }
            });

            this.backButton.setSize(64, 32);
            this.backButton.setPosition(Gdx.graphics.getWidth() / 2 - 32, Gdx.graphics.getHeight() - 32);

            TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
            style.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
            style.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_down", Texture.class)));
            style.font = Game.defaultFont;

        /* The restart and main menu button for when the game ends */

            this.restartButton = new TextButton("Restart", style);
            this.mainMenuButton = new TextButton("Main Menu", style);

            this.restartButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    gameScreen.restart();
                }
            });
            this.mainMenuButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Game.stage.clear();
                    game.setScreen(new MainMenu(game));
                }
            });

        /* The labels for information about the game*/

            String colorType = "Colors: Normal", matchType = "Matching: Shapes", gameType = "Practice";

            if (GameSettings.colorType == GameSettings.ColorType.Random)
                colorType = "Colors: Random";

            if (GameSettings.matchType == GameSettings.MatchType.Color)
                matchType = "Matching: Colors";

            if (GameSettings.gameType == GameSettings.GameType.Fastest)
                gameType = "Mode: Fastest";
            else if (GameSettings.gameType == GameSettings.GameType.Timed)
                gameType = "Mode; TimeAttack";

            Label.LabelStyle labelStyle = new Label.LabelStyle(Game.defaultFont, Color.WHITE);
            Label.LabelStyle titleLabelStyle = new Label.LabelStyle(Game.defaultLargeFont, Color.WHITE);

            this.colorTypeLabel = new Label(colorType, labelStyle);
            this.matchTypeLabel = new Label(matchType, labelStyle);
            this.gameTypeLabel = new Label(gameType, labelStyle);

            Table labelTable = new Table();
            labelTable.left().top();
            labelTable.setFillParent(true);

            labelTable.add(colorTypeLabel).left();
            labelTable.row();
            labelTable.add(matchTypeLabel).left();
            labelTable.row();
            labelTable.add(gameTypeLabel).left();

            if (GameSettings.gameType == GameSettings.GameType.Fastest) {
                Table otherTable = new Table();
                otherTable.setFillParent(true);
                otherTable.right().top();
                labelStyle = new Label.LabelStyle(Game.defaultFont, Color.WHITE);

                this.avgTimeLabel = new Label("avg-time: 0", labelStyle);
                this.avgTimeLabel.setAlignment(Align.center);
                this.avgTimeLabel.setSize(100, 50);
                otherTable.add(avgTimeLabel);
                otherTable.row();

                this.roundLabel = new Label("0 / 0 / 0", labelStyle);
                this.roundLabel.setAlignment(Align.center);
                this.roundLabel.setSize(100, 50);
                otherTable.add(roundLabel);

                Game.stage.addActor(otherTable);
            }

            if (GameSettings.gameType == GameSettings.GameType.Timed) {
                this.timerLabel = new Label("", titleLabelStyle);
                this.timerLabel.setAlignment(Align.center);
                this.timerLabel.setSize(100, 50);
                this.timerLabel.setPosition(Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() - 75);
                Game.stage.addActor(this.timerLabel);
            }

            Game.stage.addActor(this.backButton);
            Game.stage.addActor(labelTable);

            this.makeStartingGUI(game, gameScreen);
        }

        public void makeStartingGUI(final Game game, final GameScreen scren){
            String numShapes, colorType="Colors: Normal", matchType="Matching: Shapes", gameType="Practice!";

            if(GameSettings.colorType == GameSettings.ColorType.Random)
                colorType = "Colors: Random";

            if(GameSettings.matchType == GameSettings.MatchType.Color)
                matchType = "Matching: Colors";

            if(GameSettings.gameType == GameSettings.GameType.Fastest)
                gameType = "Best out of 10!";
            else if(GameSettings.gameType == GameSettings.GameType.Timed)
                gameType = "Time Attack!";

            Label.LabelStyle style = new Label.LabelStyle(Game.defaultLargeFont, Color.WHITE);

            this.startingColorType = new Label(colorType, style);
            this.startingColorType.setAlignment(Align.center);
            this.startingColorType.setColor(1, 1, 1, 0);

            this.startingMatchType = new Label(matchType, style);
            this.startingMatchType.setAlignment(Align.center);
            this.startingMatchType.setColor(1, 1, 1, 0);

            this.startingGameType = new Label(gameType, style);
            this.startingGameType.setAlignment(Align.center);
            this.startingGameType.setColor(1, 1, 1, 0);

//            this.overlay = new Image(new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("whitePixel", Texture.class))));
//            this.overlay.setColor(0.2f, 0.2f, 0.2f, 0.8f);
//            this.overlay.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//            this.overlay.setPosition(0,0);

            Table shapeTable = new Table();

            if(GameSettings.matchType == GameSettings.MatchType.Shapes){
                this.firstShape = new Image(new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("Square", Texture.class))));
                this.secondShape = new Image(new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("Square", Texture.class))));
                this.firstShape.setColor(Color.YELLOW);
                this.secondShape.setColor(Color.RED);
            }else{
                this.firstShape = new Image(new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("Star", Texture.class))));
                this.secondShape = new Image(new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("Square", Texture.class))));
                this.firstShape.setColor(Color.RED);
                this.secondShape.setColor(Color.RED);
            }

            this.firstShape.getColor().a = 0;
            this.secondShape.getColor().a = 0;

            shapeTable.add(this.firstShape).padRight(60);
            shapeTable.add(this.secondShape);

            this.startingTable = new Table();
            this.startingTable.setFillParent(true);
            this.startingTable.debugAll();

            this.startingTable.add(this.startingColorType).expandX().fillX();
            this.startingTable.row().padTop(20);
            this.startingTable.add(this.startingMatchType).expandX().fillX();
            this.startingTable.row().padTop(20);
            this.startingTable.add(shapeTable);
            this.startingTable.row().padTop(20);
            this.startingTable.add(this.startingGameType).expandX().fillX();
        }

        public boolean showStartingScreen(float delta){
            //First, show color type (random, same)
            if(this.state == 0){
                //Game.stage.addActor(this.overlay);
                Game.stage.addActor(this.startingTable);
                this.state++;
            }else if(this.state == 1){
                //Show colors
                this.startingColorType.getColor().a = GH.lerpValue(this.startingColorType.getColor().a, 0, 1, 0.5f);
                if(this.startingColorType.getColor().a >= 1) this.state++;

            }else if(this.state == 2){
                //Show matching
                this.startingMatchType.getColor().a = GH.lerpValue(this.startingMatchType.getColor().a, 0, 1, 0.5f);
                if(this.startingMatchType.getColor().a >= 1) this.state++;

            }else if(this.state == 3){
                float a;
                Color color;
                //Show example
                if(this.innerState == 0) {
                    color = this.firstShape.getColor();
                    this.firstShape.getColor().a = GH.lerpValue(color.a, 0, 1, 0.5f);

                    color = this.secondShape.getColor();
                    this.secondShape.getColor().a = GH.lerpValue(color.a, 0, 1, 0.5f);
                    if (this.secondShape.getColor().a >= 1) this.state++;
                }else{
                    this.innerState = 0;
                    this.state++;
                }

            }else if(this.state == 4){
                //Show game type
                float a = GH.lerpValue(this.startingGameType.getColor().a, 0, 1, 0.5f);
                this.startingGameType.setColor(1, 1, 1, a);
                if(a >= 1) this.state++;

            }else if(this.state == 5){
                this.waitTime = GH.lerpValue(this.waitTime, 0, 1, 1);
                if(this.waitTime >= 1) {
                    this.startingTable.remove();
                    //this.overlay.remove();
                    return true;
                }
                //Remove example and such?
            }


            //Second, show what we are matching (shapes/colors)

            //Third, show two shapes, if matching random color shapes, 2 diff shapes with different colors.
            //Otherwise, 2 same shapes with same/random colors, either is fine...

            //Lastly, display game type (Practice, Best out of 10, TimeAttack)

            return false;
        }

        public void roundEndedGUI(){
            this.gameOverImage.remove();
        }

        public void gameOverGUI(){
            this.mainTable.add(this.restartButton).size(200f, 75f);
            this.mainTable.row().padTop(50f);
            this.mainTable.add(this.mainMenuButton).size(200f, 75f);
        }

        public void roundOverGUI(TextureRegion gameOverTexture){
            this.gameOverImage = new Image(gameOverTexture);

            //Add the game over image.
            this.gameOverImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
            this.gameOverImage.setPosition(0f, Gdx.graphics.getHeight()/2f - this.gameOverImage.getHeight()/2f);
            Game.stage.addActor(this.gameOverImage);
        }

        public static GameScreenGUI inst(){
            if(instance == null) instance = new GameScreenGUI();
            return instance;
        }
    }

    public static class MainMenuGUI{
        private static MainMenuGUI instance;

        public Table table;

        public TextButton start, quit, colorSame, colorRandom, matchShape, matchColor, modePractice, modeBest, modeTimed, startGame;
        public TextButton threeShapes, fourShapes, fiveShapes, sixShapes;

        public void makeGUI(final Game game, final MainMenu mainMenu){
            this.table = new Table();

            TextButton.TextButtonStyle regularStyle = new TextButton.TextButtonStyle();
            regularStyle.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
            regularStyle.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_down", Texture.class)));
            regularStyle.checked = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_down", Texture.class)));
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
                    game.setScreen(new GameScreen(game));
                }
            });

            this.table.add(start).size(200, 75);
            this.table.row().padTop(75);
            this.table.add(quit).size(200, 75);

            this.table.setFillParent(true);
            //this.mainTable.debug();

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

        public static MainMenuGUI inst(){
            if(instance == null) instance = new MainMenuGUI();
            return instance;
        }

    }
}
