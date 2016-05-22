package com.quickbite.connector2.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.quickbite.connector2.GH;
import com.quickbite.connector2.Game;
import com.quickbite.connector2.GameScreen;
import com.quickbite.connector2.GameSettings;
import com.quickbite.connector2.GameStats;
import com.quickbite.connector2.MainMenu;

import java.text.DecimalFormat;

/**
 * Created by Paha on 5/2/2016.
 */
public class GameScreenGUI {
    private static Table centerTable = new Table();
    private static Table leftTable = new Table();
    private static Table rightTable = new Table();
    private static Table gameOverTable = new Table();
    private static Image gameOverImage;
    private static TextButton restartButton, mainMenuButton;
    private static ImageButton backButton;

    /* Game over screen */
    private static Label roundsSurvivedLabel, bestTimeLabel, lostReasonLabel, avgTimeLabel, scoreLabel, roundsLabel;

    /* Starting screen stuff */
    private static int state = 0, innerState = 0;
    private static Label startingColorType, startingMatchType, startingGameType, topCenterLabel;
    private static Table startingTable;
    private static Image firstShape, secondShape, overlay;
    private static float waitTime = 0;
    private static TextureRegion[] gameOverShapes;
    private static DecimalFormat formatter = new DecimalFormat("0.00");

    public static void update(float delta){
        if(GameSettings.gameType == GameSettings.GameType.Timed)
            topCenterLabel.setText(formatter.format(GameStats.roundTimeLeft)+"");
        else if(GameSettings.gameType == GameSettings.GameType.Fastest)
            topCenterLabel.setText("Round: "+GameStats.currRound+"/"+GameStats.maxRounds);
    }

    public static void initGameScreenGUI(final Game game, final GameScreen gameScreen) {
        centerTable = new Table();
        centerTable.setFillParent(true);
        Game.stage.addActor(centerTable);

        gameOverShapes = new TextureRegion[2];

        Texture texture = Game.easyAssetManager.get("checkmark", Texture.class);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        gameOverShapes[0] = new TextureRegion(texture);

        texture = Game.easyAssetManager.get("X", Texture.class);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        gameOverShapes[1] = new TextureRegion(texture);

        TextureRegion arrow = new TextureRegion(Game.easyAssetManager.get("leftArrow", Texture.class));

        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
        imageButtonStyle.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
        imageButtonStyle.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_down", Texture.class)));
        imageButtonStyle.imageUp = new TextureRegionDrawable(arrow);
        imageButtonStyle.imageDown = new TextureRegionDrawable(arrow);

        backButton = new ImageButton(imageButtonStyle);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Game.stage.clear();
                toMainMenu(gameScreen, game);
            }
        });

        backButton.setSize(64, 32);
        backButton.setPosition(Gdx.graphics.getWidth() / 2 - 32, Gdx.graphics.getHeight() - 32);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
        style.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_down", Texture.class)));
        style.font = Game.defaultFont;

    /* The restartGame and main menu button for when the game ends */

        restartButton = new TextButton("Restart", style);
        mainMenuButton = new TextButton("Main Menu", style);

        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameScreen.restartGame();
                gameOverTable.remove();
                Game.stage.addActor(centerTable);
                Game.stage.addActor(leftTable);
                Game.stage.addActor(rightTable);
            }
        });
        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Game.stage.clear();
                toMainMenu(gameScreen, game);
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

        scoreLabel = new Label("", titleLabelStyle);
        scoreLabel.setAlignment(Align.center);

        roundsLabel = new Label("", titleLabelStyle);
        roundsLabel.setAlignment(Align.center);

        bestTimeLabel = new Label("", titleLabelStyle);
        bestTimeLabel.setAlignment(Align.center);

        lostReasonLabel = new Label("", titleLabelStyle);
        lostReasonLabel.setAlignment(Align.center);

        avgTimeLabel = new Label("", titleLabelStyle);
        avgTimeLabel.setAlignment(Align.center);

        topCenterLabel = new Label("LOL", titleLabelStyle);
        topCenterLabel.setAlignment(Align.center);
        topCenterLabel.setSize(100, 50);
        topCenterLabel.setPosition(Game.viewport.getWorldWidth() / 2f - 50, Game.viewport.getScreenHeight() - 75);
        topCenterLabel.setFontScale(0.8f);

        centerTable.add(backButton);

        leftTable.add(topCenterLabel);
        leftTable.left().top();
        leftTable.setFillParent(true);

        centerTable.top();

        Game.stage.addActor(centerTable);
        Game.stage.addActor(leftTable);

        makeStartingGUI(game, gameScreen);
    }

    public static void makeStartingGUI(final Game game, final GameScreen scren){
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

        startingColorType = new Label(colorType, style);
        startingColorType.setAlignment(Align.center);
        startingColorType.setColor(1, 1, 1, 0);

        startingMatchType = new Label(matchType, style);
        startingMatchType.setAlignment(Align.center);
        startingMatchType.setColor(1, 1, 1, 0);

        startingGameType = new Label(gameType, style);
        startingGameType.setAlignment(Align.center);
        startingGameType.setColor(1, 1, 1, 0);

        overlay = new Image(new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("whitePixel", Texture.class))));
        overlay.setColor(0.1f, 0.1f, 0.1f, 1f);
        overlay.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        overlay.setPosition(0,0);

        Table shapeTable = new Table();

        if(GameSettings.matchType == GameSettings.MatchType.Shapes){
            firstShape = new Image(new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("Square", Texture.class))));
            secondShape = new Image(new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("Square", Texture.class))));
            firstShape.setColor(Color.YELLOW);
            secondShape.setColor(Color.RED);
        }else{
            firstShape = new Image(new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("Star", Texture.class))));
            secondShape = new Image(new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("Square", Texture.class))));
            firstShape.setColor(Color.RED);
            secondShape.setColor(Color.RED);
        }

        firstShape.getColor().a = 0;
        secondShape.getColor().a = 0;

        shapeTable.add(firstShape).padRight(60);
        shapeTable.add(secondShape);

        startingTable = new Table();
        startingTable.setFillParent(true);
        //startingTable.debugAll();

        startingTable.add(startingColorType).expandX().fillX();
        startingTable.row().padTop(40);
        startingTable.add(startingMatchType).expandX().fillX();
        startingTable.row().padTop(40);
        startingTable.add(shapeTable);
        startingTable.row().padTop(40);
        startingTable.add(startingGameType).expandX().fillX();
    }

    public static boolean showStartingScreen(float delta){
        //First, show color type (random, same)
        if(state == 0){
            Game.stage.addActor(overlay);
            Game.stage.addActor(startingTable);
            state++;
        }else if(state == 1){
            //Show colors
            startingColorType.getColor().a = GH.lerpValue(startingColorType.getColor().a, 0, 1, 0.5f);
            if(startingColorType.getColor().a >= 1) state++;

        }else if(state == 2){
            //Show matching
            startingMatchType.getColor().a = GH.lerpValue(startingMatchType.getColor().a, 0, 1, 0.5f);
            if(startingMatchType.getColor().a >= 1) state++;

        }else if(state == 3){
            Color color;
            //Show example
            if(innerState == 0) {
                color = firstShape.getColor();
                firstShape.getColor().a = GH.lerpValue(color.a, 0, 1, 0.5f);

                color = secondShape.getColor();
                secondShape.getColor().a = GH.lerpValue(color.a, 0, 1, 0.5f);
                if (secondShape.getColor().a >= 1) state++;
            }else{
                innerState = 0;
                state++;
            }

        }else if(state == 4){
            //Show game type
            startingGameType.getColor().a = GH.lerpValue(startingGameType.getColor().a, 0, 1, 0.5f);
            if(startingGameType.getColor().a >= 1) state++;

        }else if(state == 5){
            waitTime = GH.lerpValue(waitTime, 0, 1f, 1f);
            if(waitTime >= 1) {
                waitTime = 0;
                state++;
            }
            //Remove example and such?
        }else if(state == 6){
            startingTable.getColor().a = GH.lerpValue(startingTable.getColor().a, 1, 0, 0.5f);
            if(startingTable.getColor().a <= 0) {
                state++;
            }
        }else if(state == 7){
            waitTime = GH.lerpValue(waitTime, 0, 1f, 0.5f);
            if(waitTime >= 1) {
                waitTime = 0;
                startingTable.remove();
                overlay.remove();
                return true;
            }
        }

        //Second, show what we are matching (shapes/colors)

        //Third, show two shapes, if matching random color shapes, 2 diff shapes with different colors.
        //Otherwise, 2 same shapes with same/random colors, either is fine...

        //Lastly, display game type (Practice, Best out of 10, TimeAttack)

        return false;
    }

    public static void gameOverTimedGUI(GameScreen screen){
        gameOverTableReset();

        Label.LabelStyle style = new Label.LabelStyle(Game.defaultLargeFont, Color.WHITE);
        roundsSurvivedLabel = new Label("Made it to round "+GameStats.currRound, style);
        roundsSurvivedLabel.setAlignment(Align.center);

        gameOverTable.add(lostReasonLabel).fillX().expandX();
        gameOverTable.row().padTop(50);
        gameOverTable.add(roundsSurvivedLabel).fillX().expandX();
        gameOverTable.row().padTop(50);

        gameOverTable.setFillParent(true);
        Game.stage.addActor(gameOverTable);
    }

    public static void gameOverBestGUI(){

    }

    public static void gameOverPracticeGUI(){

    }

    /**
     * Displays the GUI at the end of the game (whether it's win or lose)
     */
    public static void gameOverGUI(){
        gameOverTableReset();

        lostReasonLabel.setText(GH.getLostReason());
        scoreLabel.setText("Score: "+GameStats.currScore);
        roundsLabel.setText(GameStats.successfulRounds+" Rounds Completed!");
        avgTimeLabel.setText("Average Time: "+formatter.format(GameStats.avgTime/1000)+"s.");
        bestTimeLabel.setText("Best Time: "+formatter.format(GameStats.bestTime/1000)+"s.");

        if(GameSettings.gameType == GameSettings.GameType.Timed){
            gameOverTable.add(lostReasonLabel).expandX().fillX();
            gameOverTable.row().padTop(50f);
        }

        gameOverTable.add(scoreLabel).expandX().fillX();
        gameOverTable.row().padTop(50f);
        gameOverTable.add(roundsLabel).expandX().fillX();
        gameOverTable.row().padTop(50f);
        gameOverTable.add(avgTimeLabel).expandX().fillX();
        gameOverTable.row().padTop(50f);
        gameOverTable.add(bestTimeLabel).expandX().fillX();
        gameOverTable.row().padTop(50f);
        gameOverTable.add(restartButton).size(200f, 75f);
        gameOverTable.row().padTop(50f);
        gameOverTable.add(mainMenuButton).size(200f, 75f);

        scoreLabel.getColor().a = 0f;
        avgTimeLabel.getColor().a = 0f;
        bestTimeLabel.getColor().a = 0f;
        restartButton.getColor().a = 0f;
        mainMenuButton.getColor().a = 0f;

        scoreLabel.addAction(Actions.fadeIn(0.5f));
        avgTimeLabel.addAction(Actions.sequence(Actions.delay(0.1f), Actions.fadeIn(0.5f)));
        bestTimeLabel.addAction(Actions.sequence(Actions.delay(0.2f), Actions.fadeIn(0.5f)));
        restartButton.addAction(Actions.sequence(Actions.delay(0.3f), Actions.fadeIn(0.5f)));
        mainMenuButton.addAction(Actions.sequence(Actions.delay(0.4f), Actions.fadeIn(0.5f)));

        gameOverTable.setFillParent(true);
        Game.stage.addActor(gameOverTable);
    }

    private static void gameOverTableReset(){
        centerTable.remove();
        leftTable.remove();
        rightTable.remove();

        gameOverTable.clear();
    }


    public static void roundEndedGUI(){
        if(gameOverImage != null) {
            gameOverImage.remove();
            gameOverImage = null;
        }
    }

    /**
     * Displays stuff at the end of a round.
     */
    public static void roundOverGUI(){
    if(gameOverImage != null) return;

    if(GameStats.failedLastRound)
    gameOverImage = new Image(gameOverShapes[1]);
    else
    gameOverImage = new Image(gameOverShapes[0]);

    //Add the game over image.
    gameOverImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
    gameOverImage.setPosition(0f, Gdx.graphics.getHeight()/2f - gameOverImage.getHeight()/2f);
    Game.stage.addActor(gameOverImage);
}

    public static void reset(){
        state = 0;
        innerState = 0;
    }

    private static void toMainMenu(GameScreen gameScreen, Game game){
        gameScreen.dispose();
        Game.adInterface.showAdmobInterAd();
        resetTables();
        Game.stage.clear();
        game.setScreen(new MainMenu(game));
    }

    private static void resetTables(){
        centerTable.clear();
        leftTable.clear();
        rightTable.clear();

        gameOverTable.clear();
    }
}
