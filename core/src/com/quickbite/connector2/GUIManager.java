package com.quickbite.connector2;

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
import com.badlogic.gdx.utils.Array;

/**
 * Created by Paha on 1/23/2016.
 */
public class GUIManager {

    public static class GameScreenGUI {
        public Table mainTable = new Table();
        public Image gameOverImage;
        public Label avgTimeLabel, colorTypeLabel, matchTypeLabel, gameTypeLabel;
        public Label roundsSurvivedLabel, bestTimeLabel, lostReasonLabel, avgTimeLabel2, scoreLabel;
        public TextButton restartButton, mainMenuButton;
        public ImageButton backButton;


        /* Starting screen stuff */
        private int state = 0, innerState = 0;
        private Label startingColorType, startingMatchType, startingGameType, topCenterLabel;
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
                    gameScreen.dispose();
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
                    gameScreen.dispose();
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

            this.scoreLabel = new Label("", titleLabelStyle);
            this.scoreLabel.setAlignment(Align.center);

            this.bestTimeLabel = new Label("", titleLabelStyle);
            this.bestTimeLabel.setAlignment(Align.center);

            this.lostReasonLabel = new Label("", titleLabelStyle);
            this.lostReasonLabel.setAlignment(Align.center);

            this.avgTimeLabel2 = new Label("", titleLabelStyle);
            this.avgTimeLabel2.setAlignment(Align.center);

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

                Game.stage.addActor(otherTable);
            }

            this.topCenterLabel = new Label("", titleLabelStyle);
            this.topCenterLabel.setAlignment(Align.center);
            this.topCenterLabel.setSize(100, 50);
            this.topCenterLabel.setPosition(Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() - 75);
            Game.stage.addActor(this.topCenterLabel);

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

            this.overlay = new Image(new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("whitePixel", Texture.class))));
            this.overlay.setColor(0.1f, 0.1f, 0.1f, 1f);
            this.overlay.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            this.overlay.setPosition(0,0);

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
            //this.startingTable.debugAll();

            this.startingTable.add(this.startingColorType).expandX().fillX();
            this.startingTable.row().padTop(40);
            this.startingTable.add(this.startingMatchType).expandX().fillX();
            this.startingTable.row().padTop(40);
            this.startingTable.add(shapeTable);
            this.startingTable.row().padTop(40);
            this.startingTable.add(this.startingGameType).expandX().fillX();
        }

        public boolean showStartingScreen(float delta){
            //First, show color type (random, same)
            if(this.state == 0){
                Game.stage.addActor(this.overlay);
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
                this.startingGameType.getColor().a = GH.lerpValue(this.startingGameType.getColor().a, 0, 1, 0.5f);
                if(this.startingGameType.getColor().a >= 1) this.state++;

            }else if(this.state == 5){
                this.waitTime = GH.lerpValue(this.waitTime, 0, 1f, 1f);
                if(this.waitTime >= 1) {
                    this.waitTime = 0;
                    this.state++;
                }
                //Remove example and such?
            }else if(this.state == 6){
                this.startingTable.getColor().a = GH.lerpValue(this.startingTable.getColor().a, 1, 0, 0.5f);
                if(this.startingTable.getColor().a <= 0) {
                    this.state++;
                }
            }else if(this.state == 7){
                this.waitTime = GH.lerpValue(this.waitTime, 0, 1f, 0.5f);
                if(this.waitTime >= 1) {
                    this.waitTime = 0;
                    this.startingTable.remove();
                    this.overlay.remove();
                    return true;
                }
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

        public void gameOverTimedGUI(GameScreen screen){
            Label.LabelStyle style = new Label.LabelStyle(Game.defaultLargeFont, Color.WHITE);
            this.roundsSurvivedLabel = new Label("Made it to round "+screen.currRound, style);
            this.roundsSurvivedLabel.setAlignment(Align.center);

            this.mainTable.add(this.lostReasonLabel).fillX().expandX();
            this.mainTable.row().padTop(50);
            this.mainTable.add(this.roundsSurvivedLabel).fillX().expandX();
            this.mainTable.row().padTop(50);
        }

        public void gameOverBestGUI(){

        }

        public void gameOverPracticeGUI(){

        }

        public void gameOverGUI(){
            this.mainTable.add(this.scoreLabel).expandX().fillX();
            this.mainTable.row().padTop(50f);
            this.mainTable.add(this.avgTimeLabel2).expandX().fillX();
            this.mainTable.row().padTop(50f);
            this.mainTable.add(this.bestTimeLabel).expandX().fillX();
            this.mainTable.row().padTop(50f);
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

        public void setAvgTimeLabelText(String text){
            if(this.avgTimeLabel != null)
                this.avgTimeLabel.setText(text);
        }

        public void setBestTimeLabelText(String text){
            if(this.bestTimeLabel != null)
                this.bestTimeLabel.setText(text);
        }

        public void setLostReasonLabelText(String text){
            if(this.lostReasonLabel != null)
                this.lostReasonLabel.setText(text);
        }

        public void setGameOverAvgTimeLabelText(String text){
            if(this.avgTimeLabel2 != null)
                this.avgTimeLabel2.setText(text);
        }

        public void setScoreLabel(String text){
            if(this.scoreLabel != null)
                this.scoreLabel.setText(text);
        }

        public void setTopCenterLabel(String text){
            if(this.topCenterLabel != null)
                this.topCenterLabel.setText(text);
        }

        public void reset(){
            this.state = 0;
            this.innerState = 0;
        }

        public static GameScreenGUI inst(){
            if(instance == null) instance = new GameScreenGUI();
            return instance;
        }

    }

    public static class MainMenuGUI{
        private static MainMenuGUI instance;

        public Table table, leaderSelectionTable, choicesTable, leaderDisplayTable, mainMenuTable;

        public TextButton leaderboards, start, quit, loginGPG;
        public TextButton colorSame, colorRandom, matchShape, matchColor, modePractice, modeBest, modeTimed, startGame;
        public TextButton threeShapes, fourShapes, fiveShapes, sixShapes;

        public TextButton bestLeaderButton, timedLeaderButton;
        public Container<Label> titleContainer;

        public Label TitleLabel;

        private TextButton.TextButtonStyle darkButtonStyle, clearGreenSelectionStyle;
        private Label.LabelStyle bigLabelStyle;

        /* Variables for the leaderboard selection table */
        private String leaderboardID;
        private int leaderboardType, leaderboardTimeSpan;

        public void makeGUI(final Game game, final MainMenu mainMenu){
            this.table = new Table();

            this.titleContainer = new Container<Label>();
            this.leaderDisplayTable = new Table();
            this.leaderSelectionTable = new Table();
            this.mainMenuTable = new Table();

            this.darkButtonStyle = new TextButton.TextButtonStyle();
            this.darkButtonStyle.font = Game.defaultFont;
            this.darkButtonStyle.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
            this.darkButtonStyle.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_down", Texture.class)));
            this.darkButtonStyle.checked = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
            this.darkButtonStyle.disabled = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
            this.darkButtonStyle.disabledFontColor = new Color(1, 1, 1, 0.5f);

            this.clearGreenSelectionStyle = new TextButton.TextButtonStyle();
            this.clearGreenSelectionStyle.font = Game.defaultFont;
            this.clearGreenSelectionStyle.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_clear", Texture.class)));
            this.clearGreenSelectionStyle.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_clear", Texture.class)));
            this.clearGreenSelectionStyle.checked = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_green", Texture.class)));
            this.clearGreenSelectionStyle.disabled = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_clear", Texture.class)));
            this.clearGreenSelectionStyle.disabledFontColor = new Color(1, 1, 1, 0.5f);

            this.bigLabelStyle = new Label.LabelStyle(Game.defaultLargeFont, Color.WHITE);

            TextButton.TextButtonStyle regularStyle = new TextButton.TextButtonStyle();
            regularStyle.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
            regularStyle.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_down", Texture.class)));
            regularStyle.checked = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
            regularStyle.font = Game.defaultFont;
            regularStyle.disabled = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
            regularStyle.disabledFontColor = new Color(1, 1, 1, 0.5f);

            TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
            style.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_clear", Texture.class)));
            style.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_green", Texture.class)));
            style.checked = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_green", Texture.class)));
            style.font = Game.defaultFont;


            start = new TextButton("Start", regularStyle);
            quit = new TextButton("Quit", regularStyle);

            leaderboards = new TextButton("Leaderboards", regularStyle);
            loginGPG = new TextButton("Login to \n Google Play", regularStyle);

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

            leaderboards.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    table.clear();
                    table.add(leaderSelectionTable);
                }
            });

            loginGPG.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Game.resolver.loginGPGS();
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

            if(Game.resolver.getSignedInGPGS())
                loginGPG.setDisabled(true);
            else
                leaderboards.setDisabled(true);

            Label.LabelStyle titleStyle = new Label.LabelStyle(Game.defaultHugeFont, Color.WHITE);
            this.TitleLabel = new Label("Connector", titleStyle);
            this.TitleLabel.setAlignment(Align.top);
            this.TitleLabel.setFontScale(1);

            titleContainer.setActor(TitleLabel);

            this.makeLeaderboardSelectionOverlay();
            this.formatMainMenu();

            this.table.setFillParent(true);
            Game.stage.addActor(this.table);
        }

        /**
         * Simply lays out already constructed components on the main menu
         *
         */
        private void formatMainMenu(){
            this.table.clear();
            this.mainMenuTable.clear();

            Table buttonTable = new Table();

            buttonTable.add(start).size(200, 75);
            buttonTable.row().padTop(40);
            buttonTable.add(loginGPG).size(200, 75);
            buttonTable.row().padTop(40);
            buttonTable.add(leaderboards).size(200, 75);
            buttonTable.row().padTop(40);
            buttonTable.add(quit).size(200, 75);

            this.mainMenuTable.top();

            this.mainMenuTable.row().padTop(50);
            this.mainMenuTable.add(titleContainer);
            this.mainMenuTable.row().padTop(75);
            this.mainMenuTable.add(buttonTable);

            this.table.add(this.mainMenuTable);
            this.table.setFillParent(true);
            this.table.top();

            this.table.debugAll();
        }

        /**
         * Creates the leaderboard selection overlay, but does not add it to the game. All
         * data is stored in leaderboardSelectionTable and can be added later to display it.
         */
        private void makeLeaderboardSelectionOverlay(){
            TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("selectionBackground", Texture.class)));

            Table leaderTable = new Table();
            Table typeTable = new Table();
            Table timeTable = new Table();
            Table buttonTable = new Table();

            Table innerTable = new Table();
            innerTable.setBackground(drawable);

            drawable = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("pixelDark", Texture.class)));
            leaderSelectionTable.setBackground(drawable);

            Label.LabelStyle labelStyle = new Label.LabelStyle(Game.defaultLargeFont, Color.WHITE);

            Label leaderboardTitleLabel = new Label("Select \nLeaderboard", labelStyle);
            leaderboardTitleLabel.setAlignment(Align.center);

            final TextButton oldExample = new TextButton("Other Way", darkButtonStyle);

            this.bestLeaderButton = new TextButton("Best", clearGreenSelectionStyle);
            this.timedLeaderButton = new TextButton("Timed", clearGreenSelectionStyle);
            final TextButton leaderPublic = new TextButton("Public", clearGreenSelectionStyle);
            final TextButton leaderSocial = new TextButton("Social", clearGreenSelectionStyle);
            final TextButton daily = new TextButton("Daily", clearGreenSelectionStyle);
            final TextButton weekly = new TextButton("Weekly", clearGreenSelectionStyle);
            final TextButton allTime = new TextButton("All Time", clearGreenSelectionStyle);

            TextButton backButton = new TextButton("Back", darkButtonStyle);
            TextButton getButton = new TextButton("Get", darkButtonStyle);

            oldExample.addListener(new ClickListener(){
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    Game.resolver.getLeaderboardGPGS("DoesntMatter");
                }
            });

            this.bestLeaderButton.addListener(new ClickListener(){
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    timedLeaderButton.setChecked(false);
                    leaderboardID = Constants.LEADERBOARD_BEST;
                }
            });

            this.timedLeaderButton.addListener(new ClickListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    bestLeaderButton.setChecked(false);
                    leaderboardID = Constants.LEADERBOARD_TIMED;
                }
            });

            leaderPublic.addListener(new ClickListener(){
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    leaderboardType = 0;
                    leaderSocial.setChecked(false);
                }
            });

            leaderSocial.addListener(new ClickListener(){
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    leaderboardType = 1;
                    leaderPublic.setChecked(false);
                }
            });

            daily.addListener(new ClickListener(){
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    leaderboardTimeSpan = 0;
                    weekly.setChecked(false);
                    allTime.setChecked(false);
                }
            });

            weekly.addListener(new ClickListener(){
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    leaderboardTimeSpan = 1;
                    daily.setChecked(false);
                    allTime.setChecked(false);
                }
            });

            allTime.addListener(new ClickListener(){
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    leaderboardTimeSpan = 2;
                    weekly.setChecked(false);
                    daily.setChecked(false);
                }
            });

            backButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    formatMainMenu();
                }
            });

            getButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    table.clear();
                    Game.resolver.getCenteredLeaderboardScore(leaderboardID, leaderboardTimeSpan, leaderboardType, 5000);
                }
            });

            leaderTable.add(bestLeaderButton).width(150).height(50); //Best
            leaderTable.add(timedLeaderButton).width(150).height(50); //Timed

            typeTable.add(leaderPublic).width(150).height(50);
            typeTable.add(leaderSocial).width(150).height(50);

            timeTable.add(daily).width(150).height(50);
            timeTable.add(weekly).width(150).height(50);
            timeTable.add(allTime).width(150).height(50);

            buttonTable.add(getButton).width(100).height(50).padRight(50);
            buttonTable.add(backButton).width(100).height(50);

            innerTable.row().pad(25,25,0,25);
            innerTable.add(leaderboardTitleLabel).width(300).height(100);
            innerTable.row().pad(10,25,0,25);
            innerTable.add(oldExample).width(150).height(50); //Best
            innerTable.row().pad(10,25,0,25);
            innerTable.add(leaderTable); //Best
            innerTable.row().pad(50,25,0,25);
            innerTable.add(typeTable);
            innerTable.row().pad(50,25,0,25);
            innerTable.add(timeTable);
            innerTable.row().pad(50,25,0,25);
            innerTable.add(buttonTable).width(150).height(50);
            innerTable.row().pad(50,25,0,25);
            innerTable.add();

            //innerTable.debugAll();

            leaderSelectionTable.add(innerTable);
        }

        /**
         * Makes the choices menu which is all contained in the choicesTable.
         */
        private void choicesMenu(){
            this.choicesTable = new Table();

            TextButton.TextButtonStyle buttonStyle = new  TextButton.TextButtonStyle();
            buttonStyle.font = Game.defaultFont;
            buttonStyle.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
            buttonStyle.over = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
            buttonStyle.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_down", Texture.class)));

            TextButton backButton = new TextButton("Back", buttonStyle);

            backButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    table.clear();
                    formatMainMenu();
                }
            });

            Label.LabelStyle titleStyle = new Label.LabelStyle(Game.defaultFont, Color.WHITE);
            titleStyle.background = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("darkStrip", Texture.class)));

            Label numShapesLabel = new Label("Number of Shapes", titleStyle);
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

            Table startTable = new Table();
            startTable.add(this.startGame).size(125, 50).padRight(50);
            startTable.add(backButton).size(125, 50);

            this.choicesTable.add(numShapesLabel).expandX().fillX().center().padBottom(10).height(40);
            this.choicesTable.row();
            this.choicesTable.add(numShapesTable).expandX();
            this.choicesTable.row().padTop(50);

            this.choicesTable.add(colorLabel).expandX().fillX().center().padBottom(10).height(40);
            this.choicesTable.row();
            this.choicesTable.add(colorTable).expandX();
            this.choicesTable.row().padTop(50);

            this.choicesTable.add(matchLabel).expandX().fillX().center().padBottom(10).height(40);
            this.choicesTable.row().padTop(10);
            this.choicesTable.add(matchTable).expandX();
            this.choicesTable.row().padTop(50);

            this.choicesTable.add(modeLabel).expandX().fillX().center().padBottom(10).height(40);
            this.choicesTable.row().padTop(10);
            this.choicesTable.add(modeTable).expandX();
            this.choicesTable.row().padTop(50);

            this.choicesTable.add(startTable);

            this.table.add(this.choicesTable).expand().fill();
        }

        /**
         * Takes in information and creates a table layout with the information.
         * @param ranks The ranks.
         * @param names The names.
         * @param scores The scores.
         */
        public void loadLeaderboardScores(Array<String> ranks, Array<String> names, Array<String> scores){
            if(ranks == null || names == null || scores == null){
                table.clear();
                table.add(leaderSelectionTable);
                return;
            }

            this.table.clear();
            this.leaderDisplayTable.clear();

            Table innerTable = new Table();
            Label.LabelStyle style = new Label.LabelStyle(Game.defaultFont, Color.WHITE);

            innerTable.add(new Label("Rank", style));
            innerTable.add().padRight(10);
            innerTable.add(new Label("Name", style));
            innerTable.add().padRight(10);
            innerTable.add(new Label("Score", style));
            innerTable.row().padTop(20);

            for(int i=0;i<names.size;i++){
                Label rank = new Label(ranks.get(i), style);
                Label name = new Label(names.get(i), style);
                Label score = new Label(scores.get(i), style);

                innerTable.add(rank);
                innerTable.add().padRight(10);
                innerTable.add(name);
                innerTable.add().padRight(10);
                innerTable.add(score);
                innerTable.row();
            }

            TextButton backButton = new TextButton("Back", darkButtonStyle);
            backButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    table.clear();
                    table.add(leaderSelectionTable); // Go back to the selection table.
                }
            });

            this.leaderDisplayTable.add(innerTable);
            this.leaderDisplayTable.row().padTop(50);
            this.leaderDisplayTable.add(backButton);

            this.table.add(this.leaderDisplayTable);
           //this.leaderSelectionTable.debugAll();
        }

        public static MainMenuGUI inst(){
            if(instance == null) instance = new MainMenuGUI();
            return instance;
        }


    }
}
