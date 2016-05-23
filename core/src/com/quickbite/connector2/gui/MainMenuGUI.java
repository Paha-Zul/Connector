package com.quickbite.connector2.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.quickbite.connector2.Constants;
import com.quickbite.connector2.Game;
import com.quickbite.connector2.GameScreen;
import com.quickbite.connector2.GameSettings;
import com.quickbite.connector2.MainMenu;

/**
 * Created by Paha on 5/2/2016.
 */
public class MainMenuGUI {
    private static MainMenuGUI instance;

    public static Table table, leaderSelectionTable, choicesTable, leaderDisplayTable, mainMenuTable;

    public static TextButton leaderboards, start, quit, loginGPG;
    public static TextButton colorSame, colorRandom, matchShape, matchColor, modePractice, modeBest, modeTimed, startGame;
    public static TextButton threeShapes, fourShapes, fiveShapes, sixShapes;

    public static TextButton bestLeaderButton, timedLeaderButton;
    public static Container<Label> titleContainer;

    public static Label TitleLabel;

    private static TextButton.TextButtonStyle darkButtonStyle, clearGreenSelectionStyle;
    private static Label.LabelStyle bigLabelStyle;

    /* Variables for the leaderboard selection table */
    private static String leaderboardID;
    private static int leaderboardType, leaderboardTimeSpan;

    public static void makeGUI(final Game game, final MainMenu mainMenu){
        table = new Table();

        titleContainer = new Container<Label>();
        leaderDisplayTable = new Table();
        leaderSelectionTable = new Table();
        mainMenuTable = new Table();

        darkButtonStyle = new TextButton.TextButtonStyle();
        darkButtonStyle.font = Game.defaultFont;
        darkButtonStyle.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
        darkButtonStyle.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_down", Texture.class)));
        darkButtonStyle.checked = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
        darkButtonStyle.disabled = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
        darkButtonStyle.disabledFontColor = new Color(1, 1, 1, 0.5f);

        clearGreenSelectionStyle = new TextButton.TextButtonStyle();
        clearGreenSelectionStyle.font = Game.defaultFont;
        clearGreenSelectionStyle.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_clear", Texture.class)));
        clearGreenSelectionStyle.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_clear", Texture.class)));
        clearGreenSelectionStyle.checked = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_green", Texture.class)));
        clearGreenSelectionStyle.disabled = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_clear", Texture.class)));
        clearGreenSelectionStyle.disabledFontColor = new Color(1, 1, 1, 0.5f);

        bigLabelStyle = new Label.LabelStyle(Game.defaultLargeFont, Color.WHITE);

        TextButton.TextButtonStyle regularStyle = new TextButton.TextButtonStyle();
        regularStyle.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
        regularStyle.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_down", Texture.class)));
        regularStyle.checked = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
        regularStyle.font = Game.defaultHugeFont;
        regularStyle.disabled = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
        regularStyle.disabledFontColor = new Color(1, 1, 1, 0.5f);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_clear", Texture.class)));
        style.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_green", Texture.class)));
        style.checked = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_green", Texture.class)));
        style.font = Game.defaultHugeFont;


        start = new TextButton("Start", regularStyle);
        start.getLabel().setFontScale(0.3f);

        leaderboards = new TextButton("Leaderboards", regularStyle);
        leaderboards.getLabel().setFontScale(0.3f);

        loginGPG = new TextButton("Login to \n Google Play", regularStyle);
        loginGPG.getLabel().setFontScale(0.3f);

        quit = new TextButton("Quit", regularStyle);
        quit.getLabel().setFontScale(0.3f);

        threeShapes = new TextButton("3", style);
        threeShapes.getLabel().setFontScale(0.3f);

        fourShapes = new TextButton("4", style);
        fourShapes.getLabel().setFontScale(0.3f);

        fiveShapes = new TextButton("5", style);
        fiveShapes.getLabel().setFontScale(0.3f);

        sixShapes = new TextButton("6", style);
        sixShapes.getLabel().setFontScale(0.3f);

        colorSame = new TextButton("Same", style);
        colorSame.getLabel().setFontScale(0.3f);

        colorRandom = new TextButton("Random", style);
        colorRandom.getLabel().setFontScale(0.3f);

        matchShape = new TextButton("Shapes", style);
        matchShape.getLabel().setFontScale(0.3f);

        matchColor = new TextButton("Colors", style);
        matchColor.getLabel().setFontScale(0.3f);

        modePractice = new TextButton("Practice", style);
        modePractice.getLabel().setFontScale(0.3f);

        modeBest = new TextButton("Best", style);
        modeBest.getLabel().setFontScale(0.3f);

        modeTimed = new TextButton("Timed", style);
        modeTimed.getLabel().setFontScale(0.3f);

        startGame = new TextButton("Start", regularStyle);
        startGame.getLabel().setFontScale(0.3f);
        startGame.setDisabled(true);
        startGame.getColor().a = 0.5f;

        start.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                table.clear();
                showChoicesMenu();
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
//                table.clear();
                Game.resolver.getLeaderboardGPGS("DoesntMatter");
//                table.add(leaderSelectionTable);
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
                threeShapes.setChecked(true);
                checkAllOptionsSelected();
            }
        });

        fourShapes.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameSettings.numShapes = 4;
                threeShapes.setChecked(false);
                fourShapes.setChecked(true);
                fiveShapes.setChecked(false);
                sixShapes.setChecked(false);
                checkAllOptionsSelected();
            }
        });

        fiveShapes.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameSettings.numShapes = 5;
                threeShapes.setChecked(false);
                fourShapes.setChecked(false);
                fiveShapes.setChecked(true);
                sixShapes.setChecked(false);
                checkAllOptionsSelected();
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
                sixShapes.setChecked(true);
                checkAllOptionsSelected();
            }
        });

        colorSame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameSettings.colorType = GameSettings.ColorType.Normal;
                colorRandom.setChecked(false);
                colorSame.setChecked(true);
                checkAllOptionsSelected();
            }
        });

        colorRandom.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameSettings.colorType = GameSettings.ColorType.Random;
                colorSame.setChecked(false);
                colorRandom.setChecked(true);
                checkAllOptionsSelected();
            }
        });

        matchShape.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameSettings.matchType = GameSettings.MatchType.Shapes;
                matchColor.setChecked(false);
                matchShape.setChecked(true);
                checkAllOptionsSelected();
            }
        });

        matchColor.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameSettings.matchType = GameSettings.MatchType.Color;
                matchShape.setChecked(false);
                matchColor.setChecked(true);
                checkAllOptionsSelected();
            }
        });

        modePractice.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameSettings.gameType = GameSettings.GameType.Practice;
                modeTimed.setChecked(false);
                modeBest.setChecked(false);
                modePractice.setChecked(true);
                checkAllOptionsSelected();
            }
        });

        modeBest.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameSettings.gameType = GameSettings.GameType.Fastest;
                modeTimed.setChecked(false);
                modeBest.setChecked(true);
                modePractice.setChecked(false);
                checkAllOptionsSelected();
            }
        });

        modeTimed.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameSettings.gameType = GameSettings.GameType.Timed;
                modeTimed.setChecked(true);
                modeBest.setChecked(false);
                modePractice.setChecked(false);
                checkAllOptionsSelected();
            }
        });

        startGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Game.stage.clear();
                mainMenu.dispose();
                game.setScreen(new GameScreen(game));
            }
        });

        if(Game.resolver.getSignedInGPGS())
            loginGPG.setDisabled(true);
        else
            leaderboards.setDisabled(true);

        Label.LabelStyle titleStyle = new Label.LabelStyle(Game.defaultHugeFont, Color.WHITE);
        TitleLabel = new Label("Connector", titleStyle);
        TitleLabel.setAlignment(Align.top);
        TitleLabel.setFontScale(1);

        titleContainer.setActor(TitleLabel);

        makeLeaderboardSelectionOverlay();
        showManinMenu();

        table.setFillParent(true);
        Game.stage.addActor(table);
    }

    private static boolean checkAllOptionsSelected(){
        boolean selected = GameSettings.checkAllSelected();
        if(selected) {
            startGame.setDisabled(false);
            startGame.getColor().a = 1f;
            startGame.getStyle().fontColor = Color.WHITE;
        }else{
            startGame.setDisabled(true);
            startGame.getColor().a = 0.5f;
        }
        return selected;
    }

    /**
     * Simply lays out already constructed components on the main menu
     *
     */
    private static void showManinMenu(){
        table.clear();
        mainMenuTable.clear();

        Table buttonTable = new Table();

        buttonTable.add(start).size(200, 75);
        buttonTable.row().padTop(40);
        buttonTable.add(loginGPG).size(200, 75);
        buttonTable.row().padTop(40);
        buttonTable.add(leaderboards).size(200, 75);
        buttonTable.row().padTop(40);
        buttonTable.add(quit).size(200, 75);

        mainMenuTable.top();

        mainMenuTable.row().padTop(50);
        mainMenuTable.add(titleContainer);
        mainMenuTable.row().padTop(75);
        mainMenuTable.add(buttonTable);

        table.add(mainMenuTable);
        table.setFillParent(true);
        table.top();

        GameSettings.reset();
    }

    /**
     * Creates the leaderboard selection overlay, but does not add it to the game. All
     * data is stored in leaderboardSelectionTable and can be added later to display it.
     */
    private static void makeLeaderboardSelectionOverlay(){
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

        bestLeaderButton = new TextButton("Best", clearGreenSelectionStyle);
        timedLeaderButton = new TextButton("Timed", clearGreenSelectionStyle);
        final TextButton leaderpublic = new TextButton("public static", clearGreenSelectionStyle);
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

        bestLeaderButton.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                timedLeaderButton.setChecked(false);
                leaderboardID = Constants.LEADERBOARD_BEST;
            }
        });

        timedLeaderButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                bestLeaderButton.setChecked(false);
                leaderboardID = Constants.LEADERBOARD_TIMED;
            }
        });

        leaderpublic.addListener(new ClickListener(){
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
                leaderpublic.setChecked(false);
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
                showManinMenu();
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

        typeTable.add(leaderpublic).width(150).height(50);
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
    private static void showChoicesMenu(){
        choicesTable = new Table();

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
                showManinMenu();
            }
        });

        Label.LabelStyle titleStyle = new Label.LabelStyle(Game.defaultHugeFont, Color.WHITE);
        titleStyle.background = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("darkStrip", Texture.class)));

        Label numShapesLabel = new Label("Number of Shapes", titleStyle);
        numShapesLabel.setAlignment(Align.center);
        numShapesLabel.setFontScale(0.35f);

        Label colorLabel = new Label("Color per Shape Pair", titleStyle);
        colorLabel.setAlignment(Align.center);
        colorLabel.setFontScale(0.35f);

        Label matchLabel = new Label("Matching", titleStyle);
        matchLabel.setAlignment(Align.center);
        matchLabel.setFontScale(0.35f);

        Label modeLabel = new Label("Mode", titleStyle);
        modeLabel.setAlignment(Align.center);
        modeLabel.setFontScale(0.35f);

        Table numShapesTable = new Table();
        numShapesTable.add(threeShapes).size(50 ,50);
        numShapesTable.add(fourShapes).size(50 ,50);
        numShapesTable.add(fiveShapes).size(50 ,50);
        numShapesTable.add(sixShapes).size(50 ,50);

        Table colorTable = new Table();
        colorTable.add(colorSame).size(125, 50);
        colorTable.add(colorRandom).size(125, 50);

        Table matchTable = new Table();
        matchTable.add(matchShape).size(125, 50);
        matchTable.add(matchColor).size(125, 50);

        Table modeTable = new Table();
        modeTable.add(modePractice).size(125, 50).padRight(1);
        modeTable.add(modeBest).size(125, 50).padRight(1);
        modeTable.add(modeTimed).size(125, 50).padRight(1);

        Table startTable = new Table();
        startTable.add(startGame).size(125, 50).padRight(50);
        startTable.add(backButton).size(125, 50);

        choicesTable.add(numShapesLabel).expandX().fillX().center().padBottom(10).height(40);
        choicesTable.row();
        choicesTable.add(numShapesTable).expandX();
        choicesTable.row().padTop(50);

        choicesTable.add(colorLabel).expandX().fillX().center().padBottom(10).height(40);
        choicesTable.row();
        choicesTable.add(colorTable).expandX();
        choicesTable.row().padTop(50);

        choicesTable.add(matchLabel).expandX().fillX().center().padBottom(10).height(40);
        choicesTable.row().padTop(10);
        choicesTable.add(matchTable).expandX();
        choicesTable.row().padTop(50);

        choicesTable.add(modeLabel).expandX().fillX().center().padBottom(10).height(40);
        choicesTable.row().padTop(10);
        choicesTable.add(modeTable).expandX();
        choicesTable.row().padTop(50);

        choicesTable.add(startTable);

        choicesTable.top();

        table.add(choicesTable).expand().fill();
    }

    /**
     * Takes in information and creates a table layout with the information.
     * @param ranks The ranks.
     * @param names The names.
     * @param scores The scores.
     */
    public static void loadLeaderboardScores(Array<String> ranks, Array<String> names, Array<String> scores){
        if(ranks == null || names == null || scores == null){
            table.clear();
            table.add(leaderSelectionTable);
            return;
        }

        table.clear();
        leaderDisplayTable.clear();

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

        leaderDisplayTable.add(innerTable);
        leaderDisplayTable.row().padTop(50);
        leaderDisplayTable.add(backButton);

        table.add(leaderDisplayTable);
        //leaderSelectionTable.debugAll();
    }
}
