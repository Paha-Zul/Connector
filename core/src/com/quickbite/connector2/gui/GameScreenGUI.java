package com.quickbite.connector2.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
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
import com.quickbite.connector2.GameData;
import com.quickbite.connector2.GameSettings;
import com.quickbite.connector2.GameStats;
import com.quickbite.connector2.SoundManager;
import com.quickbite.connector2.screens.GameScreen;
import com.quickbite.connector2.screens.MainMenu;

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
    private static TextButton restartButton, mainMenuButton, moreStatsButton;
    private static ImageButton backButton;

    /* Game over screen */
    private static Table buttonTable, dailyTable, weeklyTable, AllTimeTable;
    private static Label roundsSurvivedLabel, bestTimeLabel, lostReasonLabel, avgTimeLabel, scoreLabel, previousScoreLabel, roundsLabel;

    /* Starting screen stuff */
    private static int state = 0, innerState = 0;

    public static Label centerLabel; //This label is special. We will draw this one manually to the screen.
    private static Label startingColorType, startingMatchType, startingGameType;
    private static Label dailyRank, weeklyRank, allTimeRank;
    private static Image dailyLoading, weeklyLoading, allTimeLoading;
    private static Table startingTable;
    private static Image firstShape, secondShape;
    private static float waitTime = 0;
    private static TextureRegion[] gameOverShapes;
    private static DecimalFormat formatter = new DecimalFormat("0.00");

    public static void update(float delta){
        if(GameSettings.gameType == GameSettings.GameType.Timed)
            centerLabel.setText(formatter.format(GameStats.roundTimeLeft)+"");
        else if(GameSettings.gameType == GameSettings.GameType.Fastest)
            centerLabel.setText("Round\n"+GameStats.currRound+"/"+GameStats.maxRounds);
        else if(GameSettings.gameType == GameSettings.GameType.Practice)
            centerLabel.setText("Practice");
        else if(GameSettings.gameType == GameSettings.GameType.Frenzy)
            centerLabel.setText(formatter.format(GameStats.roundTimeLeft)+"\n"+GameStats.successfulRounds);
    }

    public static void initGameScreenGUI(final Game game, final GameScreen gameScreen) {
        Game.stage.clear();
        centerTable = new Table();
        centerTable.setFillParent(true);

        gameOverShapes = new TextureRegion[2];

        TextureRegion texture = Game.UIAtlas.findRegion("checkmark");
        gameOverShapes[0] = new TextureRegion(texture);

        texture = Game.UIAtlas.findRegion("X");
        gameOverShapes[1] = new TextureRegion(texture);

        TextureRegion arrow = new TextureRegion(Game.UIAtlas.findRegion("leftArrow2"));

        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
        imageButtonStyle.up = new TextureRegionDrawable(new TextureRegion(Game.UIAtlas.findRegion("buttonDark_up")));
        imageButtonStyle.down = new TextureRegionDrawable(new TextureRegion(Game.UIAtlas.findRegion("buttonDark_down")));
        imageButtonStyle.imageUp = new TextureRegionDrawable(arrow);
        imageButtonStyle.imageDown = new TextureRegionDrawable(arrow);

        backButton = new ImageButton(imageButtonStyle);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Game.stage.clear();
                toMainMenu(gameScreen, game);
                SoundManager.playSound("click");
            }
        });

        backButton.getImageCell().pad(5f, 5f, 5f, 5f);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(Game.UIAtlas.findRegion("buttonDark_up")));
        style.down = new TextureRegionDrawable(new TextureRegion(Game.UIAtlas.findRegion("buttonDark_down")));
        style.font = Game.defaultHugeFont;

    /* The restartGame and main menu button for when the game ends */

        restartButton = new TextButton("Restart", style);
        restartButton.getLabel().setFontScale(0.3f);

        mainMenuButton = new TextButton("Main Menu", style);
        mainMenuButton.getLabel().setFontScale(0.3f);

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

        moreStatsButton = new TextButton("+", style);
        moreStatsButton.getLabel().setFontScale(0.4f);
        moreStatsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showMoreStats();
            }
        });

        Label.LabelStyle titleLabelStyle = new Label.LabelStyle(Game.defaultHugeFont, Color.WHITE);

        scoreLabel = new Label("", titleLabelStyle);
        scoreLabel.setAlignment(Align.center);
        scoreLabel.setFontScale(0.4f);

        previousScoreLabel = new Label("", titleLabelStyle);
        previousScoreLabel.setAlignment(Align.center);
        previousScoreLabel.setFontScale(0.4f);

        roundsLabel = new Label("", titleLabelStyle);
        roundsLabel.setAlignment(Align.center);
        roundsLabel.setFontScale(0.4f);

        bestTimeLabel = new Label("", titleLabelStyle);
        bestTimeLabel.setAlignment(Align.center);
        bestTimeLabel.setFontScale(0.4f);

        lostReasonLabel = new Label("", titleLabelStyle);
        lostReasonLabel.setColor(Color.RED);
        lostReasonLabel.setWrap(true);
        lostReasonLabel.setAlignment(Align.center);
        lostReasonLabel.setFontScale(0.4f);

        dailyRank = new Label("", titleLabelStyle);
        dailyRank.setAlignment(Align.center);
        dailyRank.setFontScale(0.4f);

        weeklyRank = new Label("", titleLabelStyle);
        weeklyRank.setAlignment(Align.center);
        weeklyRank.setFontScale(0.4f);

        allTimeRank = new Label("", titleLabelStyle);
        allTimeRank.setAlignment(Align.center);
        allTimeRank.setFontScale(0.4f);

        avgTimeLabel = new Label("", titleLabelStyle);
        avgTimeLabel.setAlignment(Align.center);
        avgTimeLabel.setFontScale(0.4f);

        centerLabel = new Label("", titleLabelStyle);
        centerLabel.setAlignment(Align.center);
        centerLabel.setPosition(0, 0);
        centerLabel.setFontScale(1.5f);
        centerLabel.getColor().a = 0.5f;

        dailyLoading = new Image(Game.shapeAtlas.findRegion("Square"));
        dailyLoading.setColor(Color.RED);
        dailyLoading.setSize(32, 32);
        dailyLoading.setOrigin(Align.center);

        weeklyLoading = new Image(Game.shapeAtlas.findRegion("Square"));
        weeklyLoading.setColor(Color.RED);
        weeklyLoading.setSize(32, 32);
        weeklyLoading.setOrigin(Align.center);

        allTimeLoading = new Image(Game.shapeAtlas.findRegion("Square"));
        allTimeLoading.setColor(Color.RED);
        allTimeLoading.setSize(32, 32);
        allTimeLoading.setOrigin(Align.center);

        leftTable.add(backButton).size(45f).pad(10f, 10f, 0f, 0f);
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

        slideInStartScreen(screen);

        Game.stage.addActor(startingColorType);
        Game.stage.addActor(startingMatchType);
        Game.stage.addActor(firstShape);
        Game.stage.addActor(secondShape);
        Game.stage.addActor(startingGameType);
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

    public static void hideGameOverGUI(){
        Game.stage.clear();
        Game.stage.addActor(centerTable);
        Game.stage.addActor(leftTable);
        Game.stage.addActor(rightTable);
        gameOverTable.remove();
    }

    public static void showGameOverGUI(){
        centerTable.remove();
        leftTable.remove();
        rightTable.remove();

        centerLabel.setText("");

        Game.stage.addActor(gameOverTable);

        //Gotta clear these
        dailyLoading.clearActions();
        weeklyLoading.clearActions();
        allTimeLoading.clearActions();
    }

    /**
     * Displays the GUI at the end of the game (whether it's win or lose)
     */
    public static void gameOverGUI(){
        gameOverTableReset();
        int space = 30;

        //We want this at the top.
        gameOverTable.setFillParent(true);
        showGameOverGUI();

        //We want to only show a few stats initially. Maybe the reason we lost at the top, main achievement (rounds completed?) and score

        lostReasonLabel.setText(GH.getLostReason());
        scoreLabel.setText("Score: "+GameStats.currScore);

        //Choose either "connections made" or "rounds completed"
        if(GameSettings.gameType == GameSettings.GameType.Frenzy)
            roundsLabel.setText(GameStats.successfulRounds+" Connections Made!");
        else
            roundsLabel.setText(GameStats.successfulRounds+" Rounds Completed!");

        //Average time and best time.
        avgTimeLabel.setText("Average Time: "+formatter.format(GameStats.avgTime/1000)+"s.");
        bestTimeLabel.setText("Best Time: "+formatter.format(GameStats.bestTime/1000)+"s.");

        //Only add why we lost if it's timed or frenzy
        if(GameSettings.gameType == GameSettings.GameType.Timed || GameSettings.gameType == GameSettings.GameType.Frenzy){
            gameOverTable.add(lostReasonLabel).expandX().fillX().padBottom(space).padTop(space);
            gameOverTable.row();
        }

        //Add the round we ended on.
        gameOverTable.add(roundsLabel).expandX().fillX().padBottom(space);
        gameOverTable.row();

        //Add the score.
        gameOverTable.add(scoreLabel).expandX().fillX().padBottom(space);
        gameOverTable.row();

        buttonTable = new Table();

        buttonTable.add(restartButton).size(150f, 50f);
        buttonTable.add(moreStatsButton).size(50f, 50f).pad(0f, 10f, 0f, 10f);
        buttonTable.add(mainMenuButton).size(150f, 50f);

        gameOverTable.add(buttonTable);
        gameOverTable.row();

        //Here we set up the tables, but don't add them to the game over table yet.
        setUpScoreTables();

        gameOverTable.top();

        //We have to validate and execute the game over table once. This lays out the table so we can get
        //correct positions.
        gameOverTable.validate();
        gameOverTable.act(0.016f);
//        gameOverTable.debugAll();
//        buttonTable.debugAll();

        float time = 0.5f;
        float delay = 0f;
        float delayIncr = 0.1f;
        float startingY = -200f;

        slideInFromTopOrBottom(lostReasonLabel, startingY, time, delay += delayIncr);
        slideInFromTopOrBottom(roundsLabel, startingY, time, delay += delayIncr);
        slideInFromTopOrBottom(scoreLabel, startingY, time, delay += delayIncr);
        slideInFromTopOrBottom(buttonTable, startingY, time, delay += delayIncr);

        //Do this last. Even if we aren't showing the score right away!
        Game.resolver.getCurrentRankInLeaderboards(GH.getCurrentLeaderboardTableID());

        //Apparently we need this to set the initial positions of stuff we've moved.
        gameOverTable.validate();
        gameOverTable.act(0.016f);
        Game.stage.act();
    }

    private static void setUpScoreTables(){
        dailyTable = new Table();
        weeklyTable = new Table();
        AllTimeTable = new Table();

        dailyTable.add(dailyRank);
        dailyTable.add(dailyLoading).size(32);

        weeklyTable.add(weeklyRank);
        weeklyTable.add(weeklyLoading).size(32);

        AllTimeTable.add(allTimeRank);
        AllTimeTable.add(allTimeLoading).size(32);

        dailyLoading.addAction(Actions.forever(Actions.rotateBy(5f)));
        weeklyLoading.addAction(Actions.forever(Actions.rotateBy(5f)));
        allTimeLoading.addAction(Actions.forever(Actions.rotateBy(5f)));
    }

    private static void slideInFromTopOrBottom(Actor actor, float startingY, float time, float delay){
        Vector2 originalPos = new Vector2(actor.getX(), actor.getY());

        //This doesn't work sometimes?
        actor.setPosition(originalPos.x, startingY);
        actor.addAction(Actions.moveTo(originalPos.x, startingY));

        actor.addAction(Actions.sequence(Actions.delay(delay), Actions.moveTo(originalPos.x, originalPos.y, time, Interpolation.circleOut)));
    }

    private static void showMoreStats(){
//        Display the extra stats...
        int space = 30;

        buttonTable.remove(); //We'll add this back later.
        moreStatsButton.remove(); //Gone!

        //Add the highest score.
        previousScoreLabel.setText("Highest score: "+ GameData.scoreMap.get(GameSettings.gameType, 0));
        gameOverTable.add(previousScoreLabel).expandX().fillX().padBottom(space);
        gameOverTable.row();

        //If not frenzy, add average and best time.
        if(GameSettings.gameType != GameSettings.GameType.Frenzy) {
            gameOverTable.add(avgTimeLabel).expandX().fillX().padBottom(space);
            gameOverTable.row();

            gameOverTable.add(bestTimeLabel).expandX().fillX().padBottom(space);
            gameOverTable.row();
        }

        gameOverTable.add(dailyTable).expandX().fillX();
        gameOverTable.row().padTop(space);

        gameOverTable.add(weeklyTable).expandX().fillX();
        gameOverTable.row().padTop(space);

        gameOverTable.add(AllTimeTable).expandX().fillX();
        gameOverTable.row().padTop(space);

        gameOverTable.add(buttonTable);

        gameOverTable.validate();
        gameOverTable.act(0.016f);
        Game.stage.act();

        float time = 0.5f;
        float delay = 0f;
        float delayIncr = 0.1f;
        float startingY = -200f;

        slideInFromTopOrBottom(previousScoreLabel, startingY, time, delay += delayIncr);
        slideInFromTopOrBottom(avgTimeLabel, startingY, time, delay += delayIncr);
        slideInFromTopOrBottom(bestTimeLabel, startingY, time, delay += delayIncr);
        slideInFromTopOrBottom(dailyTable, startingY, time, delay += delayIncr);
        slideInFromTopOrBottom(weeklyTable, startingY, time, delay += delayIncr);
        slideInFromTopOrBottom(AllTimeTable, startingY, time, delay += delayIncr);
        slideInFromTopOrBottom(buttonTable, startingY, time, delay += delayIncr);
    }

    public static void setDailyRank(String rank){
        dailyRank.setText("Daily rank: "+rank);
        dailyLoading.remove();
        dailyLoading.clearActions();
    }

    public static void setWeekylRank(String rank){
        weeklyRank.setText("Weekly rank: "+rank);
        weeklyLoading.remove();
        weeklyLoading.clearActions();
    }

    public static void setAllTimeRank(String rank){
        allTimeRank.setText("All Time rank: "+rank);
        allTimeLoading.remove();
        allTimeLoading.clearActions();
    }

    private static void gameOverTableReset(){
        centerTable.remove();
        leftTable.remove();
        rightTable.remove();

        dailyRank.setText("Daily rank: -");
        weeklyRank.setText("Weekly rank: -");
        allTimeRank.setText("All Time rank: -");

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
