package com.quickbite.connector2.gui;

import com.badlogic.gdx.graphics.Color;
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
import com.quickbite.connector2.Game;
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
    private Table centerTable = new Table();
    private Table leftTable = new Table();
    private Table rightTable = new Table();
    private Image gameOverImage;
    private ImageButton backButton;

    public Label centerLabel; //This label is special. We will draw this one manually to the screen.
    private Label startingColorType, startingMatchType, startingGameType;
    private Table startingTable;
    private Image firstShape, secondShape;
    private TextureRegion[] gameOverShapes;
    private DecimalFormat formatter = new DecimalFormat("0.00");

    private GameScreen gameScreen;

    public GameScreenGUI(GameScreen gameScreen){
        this.gameScreen = gameScreen;
    }

    public void update(float delta){
        if(GameSettings.gameType == GameSettings.GameType.Timed)
            centerLabel.setText(formatter.format(GameStats.roundTimeLeft)+"");
        else if(GameSettings.gameType == GameSettings.GameType.Fastest)
            centerLabel.setText("Round\n"+(GameStats.currRound+1)+"/"+GameStats.maxRounds);
        else if(GameSettings.gameType == GameSettings.GameType.Practice)
            centerLabel.setText("Practice");
        else if(GameSettings.gameType == GameSettings.GameType.Frenzy)
            centerLabel.setText(formatter.format(GameStats.roundTimeLeft)+"\n"+GameStats.successfulRounds);
    }

    public void initGameScreenGUI(final Game game, final GameScreen gameScreen) {
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


        Label.LabelStyle titleLabelStyle = new Label.LabelStyle(Game.defaultHugeFont, Color.WHITE);

        centerLabel = new Label("", titleLabelStyle);
        centerLabel.setAlignment(Align.center);
        centerLabel.setPosition(0, 0);
        centerLabel.setFontScale(1.5f);
        centerLabel.getColor().a = 0.5f;

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
    public void createStartingGUI(final Game game, final GameScreen screen){
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

    private void slideInStartScreen(final GameScreen screen){
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

    public void hide(){
        centerTable.remove();
        leftTable.remove();
        rightTable.remove();

        centerLabel.setText("");
    }

    public void show(){
        Game.stage.clear();
        Game.stage.addActor(centerTable);
        Game.stage.addActor(leftTable);
        Game.stage.addActor(rightTable);
    }

    private void gameOverTableReset(){
        centerTable.remove();
        leftTable.remove();
        rightTable.remove();
    }

    public void roundEndedGUI(){
        if(gameOverImage != null) {
            gameOverImage.remove();
            gameOverImage = null;
        }
    }

    /**
     * Displays stuff at the end of a round.
     */
    public void roundOverGUI(){
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

    private void toMainMenu(GameScreen gameScreen, Game game){
        gameScreen.dispose();
        if(GameStats.numberTimesWentBackToMainMenu%GameStats.numberTimesMainMenuBetweenInterAd == 0)
            Game.adInterface.showAdmobInterAd();

        GameStats.numberTimesWentBackToMainMenu++;
        reset();
        Game.stage.clear();
        game.setScreen(new MainMenu(game));
    }

    public void reset(){
        centerTable.clear();
        leftTable.clear();
        rightTable.clear();
    }
}
