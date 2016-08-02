package com.quickbite.connector2.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
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
import com.quickbite.connector2.screens.GameScreen;
import com.quickbite.connector2.screens.MainMenu;

import java.text.DecimalFormat;

/**
 * Created by Paha on 7/30/2016.
 */
public class GameOverGUI {
    private Table buttonTable, dailyTable, weeklyTable, AllTimeTable, gameOverTable;
    private Label roundsSurvivedLabel, bestTimeLabel, lostReasonLabel, avgTimeLabel, scoreLabel, previousScoreLabel, roundsLabel;

    private TextButton restartButton, mainMenuButton, moreStatsButton;
    private ImageButton backButton;

    private Label dailyRank, weeklyRank, allTimeRank;
    private Image dailyLoading, weeklyLoading, allTimeLoading;

    private DecimalFormat formatter = new DecimalFormat("0.00");

    private GameScreen gameScreen;

    private boolean showing = false;

    public GameOverGUI(GameScreen gameScreen){
        this.gameScreen = gameScreen;
        final GameScreen _screen = gameScreen;

        gameOverTable = new Table();

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(Game.UIAtlas.findRegion("buttonDark_up")));
        style.down = new TextureRegionDrawable(new TextureRegion(Game.UIAtlas.findRegion("buttonDark_down")));
        style.font = Game.defaultHugeFont;

        restartButton = new TextButton("Restart", style);
        restartButton.getLabel().setFontScale(0.3f);

        mainMenuButton = new TextButton("Main Menu", style);
        mainMenuButton.getLabel().setFontScale(0.3f);

        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                _screen.restartGame();
            }
        });
        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Game.stage.clear();
                toMainMenu(_screen, _screen.game);
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
    }

    public void show(){
        Game.stage.addActor(gameOverTable);

        //Gotta clear these
        dailyLoading.clearActions();
        weeklyLoading.clearActions();
        allTimeLoading.clearActions();

        dailyRank.setText("Daily rank: -");
        weeklyRank.setText("Weekly rank: -");
        allTimeRank.setText("All Time rank: -");

        gameOverGUI();

        showing = true;
    }

    public void hide(){
        dailyLoading.clearActions();
        weeklyLoading.clearActions();
        allTimeLoading.clearActions();

        dailyRank.setText("Daily rank: -");
        weeklyRank.setText("Weekly rank: -");
        allTimeRank.setText("All Time rank: -");

        showing = false;
    }

    /**
     * Displays the GUI at the end of the game (whether it's win or lose)
     */
    private void gameOverGUI(){
        gameOverTableReset();
        int space = 30;

        //We want this at the top.
        gameOverTable.setFillParent(true);

        //We want to only show a few stats initially. Maybe the reason we lost at the top, main achievement (rounds completed?) and score

        lostReasonLabel.setText(GH.getLostReason());
        scoreLabel.setText("Score: "+ GameStats.currScore);

        //Choose either "connections made" or "rounds completed"
        if(GameSettings.gameType == GameSettings.GameType.Frenzy)
            roundsLabel.setText(GameStats.successfulRounds+" Connections Made!");
        else
            roundsLabel.setText(GameStats.successfulRounds+" Rounds Completed!");

        //Average time and best time.
        avgTimeLabel.setText("Average Time: "+formatter.format(GameStats.avgTime/1000)+"s.");
        bestTimeLabel.setText("Best Time: "+formatter.format(GameStats.bestTime/1000)+"s.");

        gameOverTable.row().padTop(space).padBottom(space); //Add an initial row

        //Only add why we lost if it's timed or frenzy
        if(GameSettings.gameType == GameSettings.GameType.Timed || GameSettings.gameType == GameSettings.GameType.Frenzy){
            gameOverTable.add(lostReasonLabel).expandX().fillX().padBottom(space);
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
        Game.resolver.getCurrentRankInLeaderboards(GH.getCurrentLeaderboardTableID(), this);

        //Apparently we need this to set the initial positions of stuff we've moved.
        gameOverTable.validate();
        gameOverTable.act(0.016f);
        Game.stage.act();
    }

    private void setUpScoreTables(){
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

    private void slideInFromTopOrBottom(Actor actor, float startingY, float time, float delay){
        Vector2 originalPos = new Vector2(actor.getX(), actor.getY());

        //This doesn't work sometimes?
        actor.setPosition(originalPos.x, startingY);
        actor.addAction(Actions.moveTo(originalPos.x, startingY));

        actor.addAction(Actions.sequence(Actions.delay(delay), Actions.moveTo(originalPos.x, originalPos.y, time, Interpolation.circleOut)));
    }

    private void showMoreStats(){
//        Display the extra stats...
        int space = 30;

        //Let's save the current positions of some stuff before we add stuff to the table.
        Vector2 lostPos = new Vector2(lostReasonLabel.getX(), lostReasonLabel.getY());
        Vector2 roundPos = new Vector2(roundsLabel.getX(), roundsLabel.getY());
        Vector2 scorePos = new Vector2(scoreLabel.getX(), scoreLabel.getY());

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

        //Do this stuff to update positions
        gameOverTable.validate();
        gameOverTable.act(0.016f);
        Game.stage.act();

        float time = 0.5f;
        float delay = 0f;
        float delayIncr = 0.1f;
        float startingY = -200f;

        slideInFromTopOrBottom(lostReasonLabel, lostPos.y, time, delay += delayIncr);
        slideInFromTopOrBottom(roundsLabel, roundPos.y, time, delay += delayIncr);
        slideInFromTopOrBottom(scoreLabel, scorePos.y, time, delay += delayIncr);

        slideInFromTopOrBottom(previousScoreLabel, startingY, time, delay += delayIncr);
        slideInFromTopOrBottom(avgTimeLabel, startingY, time, delay += delayIncr);
        slideInFromTopOrBottom(bestTimeLabel, startingY, time, delay += delayIncr);
        slideInFromTopOrBottom(dailyTable, startingY, time, delay += delayIncr);
        slideInFromTopOrBottom(weeklyTable, startingY, time, delay += delayIncr);
        slideInFromTopOrBottom(AllTimeTable, startingY, time, delay += delayIncr);
        slideInFromTopOrBottom(buttonTable, startingY, time, delay += delayIncr);
    }

    public void setDailyRank(String rank){
        dailyRank.setText("Daily rank: "+rank);
        dailyLoading.remove();
    }

    public void setWeeklyRank(String rank){
        weeklyRank.setText("Weekly rank: "+rank);
        weeklyLoading.remove();
    }

    public void setAllTimeRank(String rank){
        allTimeRank.setText("All Time rank: "+rank);
        allTimeLoading.remove();
    }

    private void gameOverTableReset(){
        dailyRank.setText("Daily rank: -");
        weeklyRank.setText("Weekly rank: -");
        allTimeRank.setText("All Time rank: -");

        gameOverTable.clear();
    }

    private void toMainMenu(GameScreen gameScreen, Game game){
        gameScreen.dispose();
        if(GameStats.numberTimesWentBackToMainMenu%GameStats.numberTimesMainMenuBetweenInterAd == 0)
            Game.adInterface.showAdmobInterAd();

        GameStats.numberTimesWentBackToMainMenu++;
        gameScreen.gameScreenGUI.reset();
        Game.stage.clear();
        game.setScreen(new MainMenu(game));
    }

    public boolean isShowing(){
        return showing;
    }
}
