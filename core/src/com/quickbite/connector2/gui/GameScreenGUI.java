package com.quickbite.connector2.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
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
        if(GameSettings.gameType == GameSettings.GameType.Timed || GameSettings.gameType == GameSettings.GameType.Frenzy)
            topCenterLabel.setText(formatter.format(GameStats.roundTimeLeft)+"");
        else if(GameSettings.gameType == GameSettings.GameType.Fastest)
            topCenterLabel.setText("Round: "+GameStats.currRound+"/"+GameStats.maxRounds);
        else if(GameSettings.gameType == GameSettings.GameType.Practice)
            topCenterLabel.setText("Practice");
    }

    public static void initGameScreenGUI(final Game game, final GameScreen gameScreen) {
        centerTable = new Table();
        centerTable.setFillParent(true);

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
        style.font = Game.defaultHugeFont;

    /* The restartGame and main menu button for when the game ends */

        restartButton = new TextButton("Restart", style);
        restartButton.getLabel().setFontScale(0.4f);

        mainMenuButton = new TextButton("Main Menu", style);
        mainMenuButton.getLabel().setFontScale(0.4f);

        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameScreen.restartGame();
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

        Label.LabelStyle titleLabelStyle = new Label.LabelStyle(Game.defaultHugeFont, Color.WHITE);

        scoreLabel = new Label("", titleLabelStyle);
        scoreLabel.setAlignment(Align.center);
        scoreLabel.setFontScale(0.4f);

        roundsLabel = new Label("", titleLabelStyle);
        roundsLabel.setAlignment(Align.center);
        roundsLabel.setFontScale(0.4f);

        bestTimeLabel = new Label("", titleLabelStyle);
        bestTimeLabel.setAlignment(Align.center);
        bestTimeLabel.setFontScale(0.4f);

        lostReasonLabel = new Label("", titleLabelStyle);
        lostReasonLabel.setAlignment(Align.center);
        lostReasonLabel.setFontScale(0.4f);

        avgTimeLabel = new Label("", titleLabelStyle);
        avgTimeLabel.setAlignment(Align.center);
        avgTimeLabel.setFontScale(0.4f);

        topCenterLabel = new Label("LOL", titleLabelStyle);
        topCenterLabel.setAlignment(Align.center);
        topCenterLabel.setSize(100, 50);
        topCenterLabel.setPosition(Game.viewport.getWorldWidth() / 2f - 50, Game.viewport.getScreenHeight() - 75);
        topCenterLabel.setFontScale(0.4f);

        centerTable.add(backButton);

        leftTable.add(topCenterLabel);
        leftTable.left().top();
        leftTable.setFillParent(true);

        centerTable.top();

        createStartingGUI(game, gameScreen);
    }

    /**
     * Creates and lays out the starting GUI.
     * @param game The Game instance.
     * @param screen The GameScreen instance.
     */
    public static void createStartingGUI(final Game game, final GameScreen screen){
        Game.stage.clear();

        String numShapes, colorType="Colors: Normal", matchType="Matching: Shapes", gameType="Practice!";

        if(GameSettings.colorType == GameSettings.ColorType.Random)
            colorType = "Colors: Random";

        if(GameSettings.matchType == GameSettings.MatchType.Color)
            matchType = "Matching: Colors";

        if(GameSettings.gameType == GameSettings.GameType.Fastest)
            gameType = "Best out of 10!";
        else if(GameSettings.gameType == GameSettings.GameType.Timed)
            gameType = "Time Attack!";

        Label.LabelStyle style = new Label.LabelStyle(Game.defaultHugeFont, Color.WHITE);

        startingColorType = new Label(colorType, style);
        startingColorType.setAlignment(Align.center);
        startingColorType.setColor(1, 1, 1, 0);
        startingColorType.setFontScale(0.5f);

        startingMatchType = new Label(matchType, style);
        startingMatchType.setAlignment(Align.center);
        startingMatchType.setColor(1, 1, 1, 0);
        startingMatchType.setFontScale(0.5f);

        startingGameType = new Label(gameType, style);
        startingGameType.setAlignment(Align.center);
        startingGameType.setColor(1, 1, 1, 0);
        startingGameType.setFontScale(0.5f);

        overlay = new Image(new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("whitePixel", Texture.class))));
        overlay.setColor(0.1f, 0.1f, 0.1f, 1f);
        overlay.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        overlay.setPosition(0,0);

        Table shapeTable = new Table();

        if(GameSettings.matchType == GameSettings.MatchType.Shapes && GameSettings.colorType == GameSettings.ColorType.Random){
            firstShape = new Image(new TextureRegionDrawable(Game.shapeAtlas.findRegion("Square")));
            secondShape = new Image(new TextureRegionDrawable(Game.shapeAtlas.findRegion("Square")));
            firstShape.setColor(Color.YELLOW);
            secondShape.setColor(Color.RED);
        }else if(GameSettings.matchType == GameSettings.MatchType.Shapes){
            firstShape = new Image(new TextureRegionDrawable(Game.shapeAtlas.findRegion("Square")));
            secondShape = new Image(new TextureRegionDrawable(Game.shapeAtlas.findRegion("Square")));
            firstShape.setColor(Color.RED);
            secondShape.setColor(Color.RED);
        }else if(GameSettings.matchType == GameSettings.MatchType.Color && GameSettings.colorType == GameSettings.ColorType.Random){
            firstShape = new Image(new TextureRegionDrawable(Game.shapeAtlas.findRegion("Star")));
            secondShape = new Image(new TextureRegionDrawable(Game.shapeAtlas.findRegion("Square")));
            firstShape.setColor(Color.RED);
            secondShape.setColor(Color.RED);
        }else if(GameSettings.matchType == GameSettings.MatchType.Color){
            firstShape = new Image(new TextureRegionDrawable(Game.shapeAtlas.findRegion("Star")));
            secondShape = new Image(new TextureRegionDrawable(Game.shapeAtlas.findRegion("Square")));
            firstShape.setColor(Color.RED);
            secondShape.setColor(Color.RED);
        }

        firstShape.getColor().a = 0;
        secondShape.getColor().a = 0;

        shapeTable.add(firstShape).padRight(60);
        shapeTable.add(secondShape);

        startingTable = new Table();
        startingTable.setFillParent(true);

        startingTable.add(startingColorType).expandX().fillX();
        startingTable.row().padTop(40);
        startingTable.add(startingMatchType).expandX().fillX();
        startingTable.row().padTop(40);
        startingTable.add(shapeTable);
        startingTable.row().padTop(40);
        startingTable.add(startingGameType).expandX().fillX();

//        fadeInStartScreen(screen);
        slideInStartScreen(screen);


//        Game.stage.addActor(startingTable);

        Game.stage.addActor(startingColorType);
        Game.stage.addActor(startingMatchType);
        Game.stage.addActor(firstShape);
        Game.stage.addActor(secondShape);
        Game.stage.addActor(startingGameType);
    }

    private static void fadeInStartScreen(final GameScreen screen){
        startingColorType.getColor().a = 0f;
        startingMatchType.getColor().a = 0f;
        firstShape.getColor().a = 0f;
        secondShape.getColor().a = 0f;
        startingGameType.getColor().a = 0f;

        startingColorType.addAction(Actions.sequence(Actions.fadeIn(0.5f)));
        startingMatchType.addAction(Actions.sequence(Actions.delay(0.4f), Actions.fadeIn(0.5f)));
        firstShape.addAction(Actions.sequence(Actions.delay(0.8f), Actions.fadeIn(0.5f)));
        secondShape.addAction(Actions.sequence(Actions.delay(0.8f), Actions.fadeIn(0.5f)));
        startingGameType.addAction(Actions.sequence(Actions.delay(1.2f), Actions.fadeIn(0.5f)));
        startingTable.addAction(Actions.sequence(Actions.delay(3.5f), Actions.fadeOut(0.5f), Actions.delay(0.5f), new Action() {
            @Override
            public boolean act(float delta) {
                Game.stage.clear();
                screen.beginGame();
                return true;
            }
        }));
    }

    private static void slideInStartScreen(final GameScreen screen){
        startingColorType.getColor().a = 1f;
        startingMatchType.getColor().a = 1f;
        firstShape.getColor().a = 1f;
        secondShape.getColor().a = 1f;
        startingGameType.getColor().a = 1f;

        startingColorType.setPosition(Game.viewport.getWorldWidth(), 550);
        startingMatchType.setPosition(-Game.viewport.getWorldWidth(), 500);
        firstShape.setPosition(-Game.viewport.getWorldWidth(), 425);
        secondShape.setPosition(Game.viewport.getWorldWidth(), 425);
        startingGameType.setPosition(Game.viewport.getWorldWidth(), 350);

        startingColorType.addAction(Actions.sequence(Actions.moveTo(Game.viewport.getWorldWidth()/2f - startingColorType.getWidth()/2f, startingColorType.getY(), 0.5f, Interpolation.circleOut)));
        startingMatchType.addAction(Actions.sequence(Actions.delay(0.1f), Actions.moveTo(Game.viewport.getWorldWidth()/2f - startingMatchType.getWidth()/2f, startingMatchType.getY(), 0.5f, Interpolation.circleOut)));
        firstShape.addAction(Actions.sequence(Actions.delay(0.2f), Actions.moveTo(Game.viewport.getWorldWidth()/2f - firstShape.getWidth()/2f - 50, firstShape.getY(), 0.5f, Interpolation.circleOut)));
        secondShape.addAction(Actions.sequence(Actions.delay(0.2f), Actions.moveTo(Game.viewport.getWorldWidth()/2f - secondShape.getWidth()/2f + 50, secondShape.getY(), 0.5f, Interpolation.circleOut)));
        startingGameType.addAction(Actions.sequence(Actions.delay(0.3f), Actions.moveTo(Game.viewport.getWorldWidth()/2f - startingGameType.getWidth()/2f, startingGameType.getY(), 0.5f, Interpolation.circleOut)));

        startingColorType.addAction(Actions.sequence(Actions.delay(2f), Actions.fadeOut(0.5f)));
        startingMatchType.addAction(Actions.sequence(Actions.delay(2f), Actions.fadeOut(0.5f)));
        firstShape.addAction(Actions.sequence(Actions.delay(2f), Actions.fadeOut(0.5f)));
        secondShape.addAction(Actions.sequence(Actions.delay(2f), Actions.fadeOut(0.5f)));
        startingGameType.addAction(Actions.sequence(Actions.sequence(Actions.delay(2f), Actions.fadeOut(0.5f)), new Action() {
            @Override
            public boolean act(float delta) {
                Game.stage.clear();
                screen.beginGame();
                return true;
            }
        }));
    }

    public static void showGameGUI(){
        Game.stage.clear();
        Game.stage.addActor(centerTable);
        Game.stage.addActor(leftTable);
        Game.stage.addActor(rightTable);
        gameOverTable.remove();
    }

    public static void hideGameGUI(){
        centerTable.remove();
        leftTable.remove();
        rightTable.remove();

        Game.stage.addActor(gameOverTable);
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
        hideGameGUI();
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

        //Add the game over image. We use width for both so it stays square
        gameOverImage.setSize(Game.camera.viewportWidth, Game.camera.viewportWidth);
        gameOverImage.setPosition(0f, Game.camera.viewportHeight/2f - gameOverImage.getHeight()/2f);
        Game.stage.addActor(gameOverImage);
    }

    public static void reset(){
        state = 0;
        innerState = 0;
    }

    private static void toMainMenu(GameScreen gameScreen, Game game){
        gameScreen.dispose();
        if(GameStats.numberTimesWentBackToMainMenu%GameStats.numberTimesMainMenuBetweenInterAd == 0)
            Game.adInterface.showAdmobInterAd();

        GameStats.numberTimesWentBackToMainMenu++;
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
