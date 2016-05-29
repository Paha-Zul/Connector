package com.quickbite.connector2.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.quickbite.connector2.Game;
import com.quickbite.connector2.GameScreen;
import com.quickbite.connector2.GameSettings;
import com.quickbite.connector2.MainMenu;

/**
 * Created by Paha on 5/2/2016.
 */
public class MainMenuGUI {
    private static MainMenuGUI instance;

    public static Table mainTable, leaderSelectionTable, choicesTable, leaderDisplayTable, mainMenuTable;

    public static TextButton leaderboards, start, quit, loginGPG;
    public static TextButton colorSame, colorRandom, matchShape, matchColor, modePractice, modeBest, modeTimed, modeChallenge, startGame;
    public static TextButton threeShapes, fourShapes, fiveShapes, sixShapes;

    public static TextButton bestLeaderButton, timedLeaderButton;
    public static Container<Label> titleContainer;

    public static Label TitleLabel;

    private static TextButton.TextButtonStyle darkButtonStyle, clearGreenSelectionStyle;
    private static Label.LabelStyle bigLabelStyle;

    private static Game game;
    private static MainMenu mainMenu;

    public static void makeGUI(final Game game, final MainMenu mainMenu){
        MainMenuGUI.game = game;
        MainMenuGUI.mainMenu = mainMenu;

        mainTable = new Table();

        titleContainer = new Container<Label>();
        leaderDisplayTable = new Table();
        leaderSelectionTable = new Table();
        mainMenuTable = new Table();

        darkButtonStyle = new TextButton.TextButtonStyle();
        darkButtonStyle.font = Game.defaultFont;
        darkButtonStyle.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
        darkButtonStyle.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_down", Texture.class)));
        darkButtonStyle.disabledFontColor = new Color(1, 1, 1, 0.5f);

        clearGreenSelectionStyle = new TextButton.TextButtonStyle();
        clearGreenSelectionStyle.font = Game.defaultFont;
        clearGreenSelectionStyle.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_clear", Texture.class)));
        clearGreenSelectionStyle.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_clear", Texture.class)));
        clearGreenSelectionStyle.checked = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_green", Texture.class)));
        clearGreenSelectionStyle.disabledFontColor = new Color(1, 1, 1, 0.5f);

        bigLabelStyle = new Label.LabelStyle(Game.defaultLargeFont, Color.WHITE);

        TextButton.TextButtonStyle regularStyle = new TextButton.TextButtonStyle();
        regularStyle.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
        regularStyle.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_down", Texture.class)));
        regularStyle.font = Game.defaultHugeFont;
        regularStyle.disabledFontColor = new Color(1, 1, 1, 0.5f);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_clear", Texture.class)));
        style.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_green", Texture.class)));
        style.checked = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_green", Texture.class)));
        style.font = Game.defaultHugeFont;

        Label.LabelStyle titleStyle = new Label.LabelStyle(Game.defaultHugeFont, Color.WHITE);
        TitleLabel = new Label("Connector", titleStyle);
        TitleLabel.setAlignment(Align.top);
        TitleLabel.setFontScale(1);

        titleContainer.setActor(TitleLabel);

        buildMainMenu();
        buildChoicesMenu();

        showMainMenu();

        mainTable.setFillParent(true);
        Game.stage.addActor(mainTable);
    }

    private static void buildMainMenu(){
        TextButton.TextButtonStyle regularStyle = new TextButton.TextButtonStyle();
        regularStyle.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
        regularStyle.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_down", Texture.class)));
        regularStyle.font = Game.defaultHugeFont;
        regularStyle.disabledFontColor = new Color(1, 1, 1, 0.5f);

        start = new TextButton("Start", regularStyle);
        start.getLabel().setFontScale(0.3f);

        leaderboards = new TextButton("Leaderboards", regularStyle);
        leaderboards.getLabel().setFontScale(0.3f);

        loginGPG = new TextButton("Login to \n Google Play", regularStyle);
        loginGPG.getLabel().setFontScale(0.3f);

        if(Game.resolver.getSignedInGPGS())
            loginGPG.setDisabled(true);
        else
            leaderboards.setDisabled(true);

        quit = new TextButton("Quit", regularStyle);
        quit.getLabel().setFontScale(0.3f);

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

        mainTable.add(mainMenuTable);
        mainTable.setFillParent(true);
        mainTable.top();

        mainMenuTable.setFillParent(true);
        Game.stage.addActor(mainMenuTable);
    }

    private static void buildChoicesMenu(){
        choicesTable = new Table();

        TextButton.TextButtonStyle regularStyle = new TextButton.TextButtonStyle();
        regularStyle.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
        regularStyle.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_down", Texture.class)));
        regularStyle.font = Game.defaultHugeFont;
        regularStyle.disabledFontColor = new Color(1, 1, 1, 0.5f);
        regularStyle.disabledFontColor = new Color(0.5f, 0.5f, 0.5f, 0.5f);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_clear", Texture.class)));
        style.checked = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_green", Texture.class)));
        style.font = Game.defaultHugeFont;
        style.disabledFontColor = new Color(0.5f, 0.5f, 0.5f, 0.5f);
        style.fontColor = Color.WHITE;

        TextButton.TextButtonStyle buttonStyle = new  TextButton.TextButtonStyle();
        buttonStyle.font = Game.defaultFont;
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
        buttonStyle.over = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
        buttonStyle.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_down", Texture.class)));

        TextButton backButton = new TextButton("Back", buttonStyle);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainTable.clear();
                toMainMenu();
            }
        });

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

        modeChallenge = new TextButton("Challenge", style);
        modeChallenge.getLabel().setFontScale(0.3f);

        startGame = new TextButton("Start", regularStyle);
        startGame.getLabel().setFontScale(0.3f);
        startGame.setDisabled(true);
        startGame.getColor().a = 0.5f;

        start.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainTable.clear();
                toChoicesMenu();
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
//                mainTable.clear();
                Game.resolver.getLeaderboardGPGS("DoesntMatter");
//                mainTable.add(leaderSelectionTable);
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
                if(threeShapes.isDisabled()) return;

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
                if(fourShapes.isDisabled()) return;

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
                if(fiveShapes.isDisabled()) return;

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
                if(sixShapes.isDisabled()) return;

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
                if(colorSame.isDisabled()) return;

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
                if(colorRandom.isDisabled()) return;

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
                modeTimed.setChecked(false);
                modeBest.setChecked(false);
                modePractice.setChecked(true);
                modeChallenge.setChecked(false);
                checkAllOptionsSelected();
                changeChoices(GameSettings.GameType.Practice);
                GameSettings.gameType = GameSettings.GameType.Practice;
            }
        });

        modeBest.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                modeTimed.setChecked(false);
                modeBest.setChecked(true);
                modePractice.setChecked(false);
                modeChallenge.setChecked(false);
                checkAllOptionsSelected();
                changeChoices(GameSettings.GameType.Fastest);
                GameSettings.gameType = GameSettings.GameType.Fastest;
            }
        });

        modeTimed.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                modeTimed.setChecked(true);
                modeBest.setChecked(false);
                modePractice.setChecked(false);
                modeChallenge.setChecked(false);
                checkAllOptionsSelected();
                changeChoices(GameSettings.GameType.Timed);
                GameSettings.gameType = GameSettings.GameType.Timed;
            }
        });

        modeChallenge.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                modeTimed.setChecked(false);
                modeBest.setChecked(false);
                modePractice.setChecked(false);
                modeChallenge.setChecked(true);
                checkAllOptionsSelected();
                changeChoices(GameSettings.GameType.Challenge);
                GameSettings.gameType = GameSettings.GameType.Challenge;
            }
        });

        startGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                startGame();
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
        numShapesTable.add(numShapesLabel).colspan(6).expandX().fillX().height(35f).spaceBottom(10f);
        numShapesTable.row();
        numShapesTable.add().expandX().fillX();
        numShapesTable.add(threeShapes).size(50 ,50);
        numShapesTable.add(fourShapes).size(50 ,50);
        numShapesTable.add(fiveShapes).size(50 ,50);
        numShapesTable.add(sixShapes).size(50 ,50);
        numShapesTable.add().expandX().fillX();

        Table colorTable = new Table();
        colorTable.add(colorLabel).colspan(4).expandX().fillX().height(35f).spaceBottom(10f);
        colorTable.row();
        colorTable.add().expandX().fillX();
        colorTable.add(colorSame).size(125, 50);
        colorTable.add(colorRandom).size(125, 50);
        colorTable.add().expandX().fillX();

        Table matchTable = new Table();
        matchTable.add(matchLabel).colspan(4).expandX().fillX().height(35f).spaceBottom(10f);
        matchTable.row();
        matchTable.add().expandX().fillX();
        matchTable.add(matchShape).size(125, 50);
        matchTable.add(matchColor).size(125, 50);
        matchTable.add().expandX().fillX();

        Table modeTable = new Table();
        modeTable.add(modeLabel).colspan(6).expandX().fillX().height(35f).spaceBottom(10f);
        modeTable.row();
        modeTable.add().expandX().fillX();
        modeTable.add(modePractice).size(120, 50);
        modeTable.add(modeBest).size(120, 50);
        modeTable.add(modeTimed).size(120, 50);
        modeTable.add(modeChallenge).size(120, 50);
        modeTable.add().expandX().fillX();

        Table startTable = new Table();
        startTable.add(startGame).size(125, 50).padRight(50);
        startTable.add(backButton).size(125, 50);

        choicesTable.add(modeTable).expandX().fillX().spaceTop(50f);
        choicesTable.row();
        choicesTable.add(matchTable).expandX().fillX().spaceTop(50f);
        choicesTable.row();
        choicesTable.add(colorTable).expandX().fillX().spaceTop(50f);
        choicesTable.row();
        choicesTable.add(numShapesTable).expandX().fillX().spaceTop(50f);
        choicesTable.row();
        choicesTable.add(startTable).fillX().spaceTop(50f);

        choicesTable.top();
        choicesTable.setFillParent(true);
        choicesTable.setPosition(Game.viewport.getWorldWidth(), 0f);

        Game.stage.addActor(choicesTable);
    }

    private static void startGame(){
        choicesTable.addAction(Actions.sequence(Actions.moveTo(-Game.viewport.getWorldWidth(), 0f, 0.3f, Interpolation.circle), new Action() {
            @Override
            public boolean act(float delta) {
                Game.stage.clear();
                mainMenu.dispose();
                game.setScreen(new GameScreen(game));
                return true;
            }
        }));
    }

    /**
     * Simply lays out already constructed components on the main menu
     *
     */
    private static void showMainMenu(){
        mainMenuTable.getColor().a = 0f;
        mainMenuTable.addAction(Actions.fadeIn(0.4f));

        GameSettings.reset();
        clearSelectedChoices();
    }


    /**
     * Simply lays out already constructed components on the main menu
     *
     */
    private static void toMainMenu(){
        mainMenuTable.addAction(Actions.moveTo(0f, 0f, 0.3f, Interpolation.circle));
        choicesTable.addAction(Actions.moveTo(Game.viewport.getWorldWidth(), 0f, 0.3f, Interpolation.circle));
        GameSettings.reset();
        clearSelectedChoices();
    }

    /**
     * Makes the choices menu which is all contained in the choicesTable.
     */
    private static void toChoicesMenu(){
        choicesTable.addAction(Actions.moveTo(0f, 0f, 0.3f, Interpolation.circle));
        mainMenuTable.addAction(Actions.moveTo(-Game.viewport.getWorldWidth(), 0f, 0.3f, Interpolation.circle));
        checkAllOptionsSelected();
    }

    /**
     * Checks if all items are selected. If not, disables the start button. If so, enables the start button.
     * @return True if all options needed are selected, false otherwise.
     */
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

    public static void clearSelectedChoices(){
        colorRandom.setChecked(false);
        colorSame.setChecked(false);
        matchColor.setChecked(false);
        matchShape.setChecked(false);
        modeChallenge.setChecked(false);
        modeTimed.setChecked(false);
        modePractice.setChecked(false);
        modeBest.setChecked(false);
        threeShapes.setChecked(false);
        fourShapes.setChecked(false);
        fiveShapes.setChecked(false);
        sixShapes.setChecked(false);
    }

    private static void changeChoices(GameSettings.GameType gameType){
        if(gameType == GameSettings.GameType.Challenge){
            colorSame.setDisabled(true);
            colorRandom.setDisabled(true);
            threeShapes.setDisabled(true);
            fourShapes.setDisabled(true);
            fiveShapes.setDisabled(true);
            sixShapes.setDisabled(true);

            colorSame.setChecked(false);
            colorRandom.setChecked(false);
            threeShapes.setChecked(false);
            fourShapes.setChecked(false);
            fiveShapes.setChecked(false);
            sixShapes.setChecked(false);

            GameSettings.numShapes = 3;
            GameSettings.colorType = GameSettings.ColorType.Random;
        }else{
            colorSame.setDisabled(false);
            colorRandom.setDisabled(false);
            threeShapes.setDisabled(false);
            fourShapes.setDisabled(false);
            fiveShapes.setDisabled(false);
            sixShapes.setDisabled(false);

            //If it was challenge before, reset it.
            if(GameSettings.gameType == GameSettings.GameType.Challenge) {
                GameSettings.numShapes = 0;
                GameSettings.colorType = GameSettings.ColorType.Nothing;
            }
        }

        checkAllOptionsSelected();
    }

    /**
     * Takes in information and creates a mainTable layout with the information.
     * @param ranks The ranks.
     * @param names The names.
     * @param scores The scores.
     */
    public static void loadLeaderboardScores(Array<String> ranks, Array<String> names, Array<String> scores){
        if(ranks == null || names == null || scores == null){
            mainTable.clear();
            mainTable.add(leaderSelectionTable);
            return;
        }

        mainTable.clear();
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
                mainTable.clear();
                mainTable.add(leaderSelectionTable); // Go back to the selection mainTable.
            }
        });

        leaderDisplayTable.add(innerTable);
        leaderDisplayTable.row().padTop(50);
        leaderDisplayTable.add(backButton);

        mainTable.add(leaderDisplayTable);
        //leaderSelectionTable.debugAll();
    }
}
