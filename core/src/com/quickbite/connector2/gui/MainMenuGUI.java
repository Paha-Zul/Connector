package com.quickbite.connector2.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.quickbite.connector2.Game;
import com.quickbite.connector2.GameScreen;
import com.quickbite.connector2.GameSettings;
import com.quickbite.connector2.MainMenu;
import com.quickbite.connector2.SoundManager;

/**
 * Created by Paha on 5/2/2016.
 */
public class MainMenuGUI {
    private static MainMenuGUI instance;

    public static Table mainTable, leaderSelectionTable, choicesTable, leaderDisplayTable, mainMenuTable;
    public static Table infoTable;

    public static TextButton quit;
    public static ImageTextButton leaderboards, loginGPG, start;
    public static TextButton colorSame, colorRandom, matchShape, matchColor, modePractice, modeBest, modeTimed, modeFrenzy, startGame;
    public static TextButton threeShapes, fourShapes, fiveShapes, sixShapes;

    public static TextButton bestLeaderButton, timedLeaderButton;

    public static Image titleImage;

    private static TextButton.TextButtonStyle darkButtonStyle, clearGreenSelectionStyle;
    private static Label.LabelStyle bigLabelStyle;

    private static Game game;
    private static MainMenu mainMenu;

    public static void makeGUI(final Game game, final MainMenu mainMenu){
        MainMenuGUI.game = game;
        MainMenuGUI.mainMenu = mainMenu;

        mainTable = new Table();

        leaderDisplayTable = new Table();
        leaderSelectionTable = new Table();
        mainMenuTable = new Table();

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

        titleImage = new Image(Game.easyAssetManager.get("title", Texture.class));

        buildMainMenu();
        buildChoicesMenu();

        showMainMenu();

        mainTable.setFillParent(true);
        Game.stage.addActor(mainTable);

        makeInfoPage();
    }

    private static void buildMainMenu(){
        NinePatch patch1 = new NinePatch(Game.easyAssetManager.get("buttonDark_up9", Texture.class));
        NinePatch patch2 = new NinePatch(Game.easyAssetManager.get("buttonDark_down9", Texture.class));

        TextButton.TextButtonStyle regularStyle = new TextButton.TextButtonStyle();
        regularStyle.up = new NinePatchDrawable(patch1);
        regularStyle.down = new NinePatchDrawable(patch2);
        regularStyle.font = Game.defaultHugeFont;
        regularStyle.fontColor = Color.WHITE;
        regularStyle.disabledFontColor = new Color(1, 1, 1, 0.5f);

        ImageTextButton.ImageTextButtonStyle startStyle = new ImageTextButton.ImageTextButtonStyle();
        startStyle.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
        startStyle.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_down", Texture.class)));
        startStyle.disabled = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
        startStyle.font = Game.defaultHugeFont;
        startStyle.fontColor = Color.WHITE;
        startStyle.disabledFontColor = new Color(1, 1, 1, 0.5f);
        startStyle.imageUp = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("startIcon", Texture.class)));

        ImageTextButton.ImageTextButtonStyle leaderboardButtonStyle = new ImageTextButton.ImageTextButtonStyle(startStyle);
        leaderboardButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("leaderboardIcon", Texture.class)));

        ImageTextButton.ImageTextButtonStyle loginStyle = new ImageTextButton.ImageTextButtonStyle(startStyle);
        loginStyle.imageUp = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("googlePlayGamesIcon", Texture.class)));

        TextButton infoButton = new TextButton("?", regularStyle);
        infoButton.setPosition(0f, Game.viewport.getWorldHeight() - 32);
        infoButton.getLabel().setFontScale(0.5f);
        infoButton.setSize(32f, 32f);

        start = new ImageTextButton("Start", startStyle);
        start.getLabel().setFontScale(0.4f);
        start.getLabelCell().fillX().expandX();
        start.getImageCell().size(64f, 64f);
        start.getImageCell().left();
        start.getImage().setColor(Color.GOLD);

        leaderboards = new ImageTextButton("Boards", leaderboardButtonStyle);
        leaderboards.getLabel().setFontScale(0.4f);
        leaderboards.getLabelCell().fillX().expandX();
        leaderboards.getImageCell().size(64f, 64f);
        leaderboards.getImageCell().left();
        leaderboards.getImage().setColor(Color.RED);

        loginGPG = new ImageTextButton("Log-in", loginStyle);
        loginGPG.getLabel().setFontScale(0.4f);
        loginGPG.getLabelCell().fillX().expandX();
        loginGPG.getImageCell().size(64f, 64f);
        loginGPG.getImageCell().left();
        loginGPG.getImage().setColor(Color.GREEN);

        changeLoginButton(Game.resolver.getSignedInGPGS());

        quit = new TextButton("Quit", regularStyle);
        quit.getLabel().setFontScale(0.3f);

        Table buttonTable = new Table();

        buttonTable.add(start).size(200, 75);
        buttonTable.row().padTop(40);
        buttonTable.add(loginGPG).size(200, 75);
        buttonTable.row().padTop(40);
        buttonTable.add(leaderboards).size(200, 75);
//        buttonTable.row().padTop(40);
//        buttonTable.add(quit).size(150, 40).colspan(2);
//        buttonTable.debugAll();

        mainMenuTable.top();

        mainMenuTable.row().padTop(50);
        mainMenuTable.add(titleImage);
        mainMenuTable.row().padTop(150);
        mainMenuTable.add(buttonTable);

        mainTable.add(mainMenuTable);
        mainTable.setFillParent(true);
        mainTable.top();

        mainMenuTable.setFillParent(true);
        Game.stage.addActor(mainMenuTable);
        Game.stage.addActor(infoButton);

        infoButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                infoTable.addAction(Actions.moveTo(0f, 0f, 0.4f, Interpolation.circleOut));
            }
        });
    }

    public static void changeLoginButton(boolean loggedIn){
        if(loggedIn){
            MainMenuGUI.leaderboards.setDisabled(false);
            MainMenuGUI.loginGPG.setText("Log-out");
            MainMenuGUI.loginGPG.setUserObject(false); //Set the value to false for 'not logging in' or 'log out'.
        } else{
            MainMenuGUI.leaderboards.setDisabled(true);
            MainMenuGUI.loginGPG.setText("Log-in");
            MainMenuGUI.loginGPG.setUserObject(true); //Set the value to true for 'logging in'
        }
    }

    private static void buildChoicesMenu(){
        choicesTable = new Table();

        TextButton.TextButtonStyle regularStyle = new TextButton.TextButtonStyle();
        regularStyle.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
        regularStyle.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_down", Texture.class)));
        regularStyle.font = Game.defaultHugeFont;
        regularStyle.disabledFontColor = new Color(1, 1, 1, 0.5f);
        regularStyle.disabledFontColor = new Color(0.5f, 0.5f, 0.5f, 0.5f);

        TextButton.TextButtonStyle numShapesButtonStyle = new TextButton.TextButtonStyle();
        numShapesButtonStyle.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_clear", Texture.class)));
        numShapesButtonStyle.checked = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("pixelGreen", Texture.class)));
        numShapesButtonStyle.font = Game.defaultHugeFont;
        numShapesButtonStyle.disabledFontColor = new Color(0.5f, 0.5f, 0.5f, 0.5f);
        numShapesButtonStyle.fontColor = Color.WHITE;

        TextButton.TextButtonStyle colorButtonStyle = new TextButton.TextButtonStyle(numShapesButtonStyle);
        colorButtonStyle.checked = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("pixelGold", Texture.class)));

        TextButton.TextButtonStyle matchButtonStyle = new TextButton.TextButtonStyle(numShapesButtonStyle);
        matchButtonStyle.checked = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("pixelRed", Texture.class)));

        TextButton.TextButtonStyle modeButtonStyle = new TextButton.TextButtonStyle(numShapesButtonStyle);
        modeButtonStyle.checked = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("pixelBlue", Texture.class)));


        TextButton.TextButtonStyle buttonStyle = new  TextButton.TextButtonStyle();
        buttonStyle.font = Game.defaultHugeFont;
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
        buttonStyle.over = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_up", Texture.class)));
        buttonStyle.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("buttonDark_down", Texture.class)));

        TextButton backButton = new TextButton("Back", buttonStyle);
        backButton.getLabel().setFontScale(0.4f);

        threeShapes = new TextButton("3", numShapesButtonStyle);
        threeShapes.getLabel().setFontScale(0.3f);

        fourShapes = new TextButton("4", numShapesButtonStyle);
        fourShapes.getLabel().setFontScale(0.3f);

        fiveShapes = new TextButton("5", numShapesButtonStyle);
        fiveShapes.getLabel().setFontScale(0.3f);

        sixShapes = new TextButton("6", numShapesButtonStyle);
        sixShapes.getLabel().setFontScale(0.3f);

        colorSame = new TextButton("Same", colorButtonStyle);
        colorSame.getLabel().setFontScale(0.3f);

        colorRandom = new TextButton("Random", colorButtonStyle);
        colorRandom.getLabel().setFontScale(0.3f);

        matchShape = new TextButton("Shapes", matchButtonStyle);
        matchShape.getLabel().setFontScale(0.3f);

        matchColor = new TextButton("Colors", matchButtonStyle);
        matchColor.getLabel().setFontScale(0.3f);

        modePractice = new TextButton("Practice", modeButtonStyle);
        modePractice.getLabel().setFontScale(0.3f);

        modeBest = new TextButton("Best", modeButtonStyle);
        modeBest.getLabel().setFontScale(0.3f);

        modeTimed = new TextButton("Timed", modeButtonStyle);
        modeTimed.getLabel().setFontScale(0.3f);

        modeFrenzy = new TextButton("Frenzy", modeButtonStyle);
        modeFrenzy.getLabel().setFontScale(0.3f);

        startGame = new TextButton("Start", regularStyle);
        startGame.getLabel().setFontScale(0.4f);
        startGame.setDisabled(true);
        startGame.getColor().a = 0.5f;

        start.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainTable.clear();
                toChoicesMenu();
                SoundManager.playSound("click");
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainTable.clear();
                toMainMenu();
                SoundManager.playSound("click");
            }
        });

        quit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.playSound("click");
                Gdx.app.exit();
            }
        });

        leaderboards.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                mainTable.clear();
                Game.resolver.getLeaderboardGPGS("DoesntMatter");
                SoundManager.playSound("click");
//                mainTable.add(leaderSelectionTable);
            }
        });

        loginGPG.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(((Boolean)loginGPG.getUserObject()))
                    Game.resolver.loginGPGS();
                else
                    Game.resolver.logoutGPGS();

                SoundManager.playSound("click");
            }
        });

        threeShapes.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(threeShapes.isDisabled()) return;

                GameSettings.numShapes = 3;
                checkAllOptionsSelected();
                SoundManager.playSound("click");
            }
        });

        fourShapes.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(fourShapes.isDisabled()) return;

                GameSettings.numShapes = 4;
                checkAllOptionsSelected();
                SoundManager.playSound("click");
            }
        });

        fiveShapes.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(fiveShapes.isDisabled()) return;

                GameSettings.numShapes = 5;
                checkAllOptionsSelected();
                SoundManager.playSound("click");
            }
        });

        sixShapes.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(sixShapes.isDisabled()) return;

                GameSettings.numShapes = 6;
                checkAllOptionsSelected();
                SoundManager.playSound("click");
            }
        });

        colorSame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(colorSame.isDisabled()) return;

                GameSettings.colorType = GameSettings.ColorType.Normal;
                checkAllOptionsSelected();
                SoundManager.playSound("click");
            }
        });

        colorRandom.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(colorRandom.isDisabled()) return;

                GameSettings.colorType = GameSettings.ColorType.Random;
                checkAllOptionsSelected();
                SoundManager.playSound("click");
            }
        });

        matchShape.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameSettings.matchType = GameSettings.MatchType.Shapes;
                checkAllOptionsSelected();
                SoundManager.playSound("click");
            }
        });

        matchColor.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameSettings.matchType = GameSettings.MatchType.Color;
                checkAllOptionsSelected();
                SoundManager.playSound("click");
            }
        });

        modePractice.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                checkAllOptionsSelected();
                changeChoices(GameSettings.GameType.Practice);
                GameSettings.gameType = GameSettings.GameType.Practice;
                SoundManager.playSound("click");
            }
        });

        modeBest.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                checkAllOptionsSelected();
                changeChoices(GameSettings.GameType.Fastest);
                GameSettings.gameType = GameSettings.GameType.Fastest;
                SoundManager.playSound("click");
            }
        });

        modeTimed.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                checkAllOptionsSelected();
                changeChoices(GameSettings.GameType.Timed);
                GameSettings.gameType = GameSettings.GameType.Timed;
                SoundManager.playSound("click");
            }
        });

        modeFrenzy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                checkAllOptionsSelected();
                changeChoices(GameSettings.GameType.Frenzy);
                GameSettings.gameType = GameSettings.GameType.Frenzy;
                SoundManager.playSound("click");
            }
        });

        startGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                startGame();
                SoundManager.playSound("click");
            }
        });

        ButtonGroup<TextButton> modeGroup = new ButtonGroup<TextButton>(modePractice, modeBest, modeTimed, modeFrenzy);
        modeGroup.setMaxCheckCount(1);
        modeGroup.setMinCheckCount(0);

        ButtonGroup<TextButton> matchGroup = new ButtonGroup<TextButton>(matchShape, matchColor);
        matchGroup.setMaxCheckCount(1);
        matchGroup.setMinCheckCount(0);

        ButtonGroup<TextButton> colorGroup = new ButtonGroup<TextButton>(colorSame, colorRandom);
        colorGroup.setMaxCheckCount(1);
        colorGroup.setMinCheckCount(0);

        ButtonGroup<TextButton> numShapesGroup = new ButtonGroup<TextButton>(threeShapes, fourShapes, fiveShapes, sixShapes);
        numShapesGroup.setMaxCheckCount(1);
        numShapesGroup.setMinCheckCount(0);

        Label.LabelStyle labelStyle = new Label.LabelStyle(Game.defaultHugeFont, Color.WHITE);

        Label numShapesLabel = new Label("Number of Shapes", labelStyle);
        numShapesLabel.setAlignment(Align.center);
        numShapesLabel.setFontScale(0.35f);

        Label colorLabel = new Label("Color per Shape Pair", labelStyle);
        colorLabel.setAlignment(Align.center);
        colorLabel.setFontScale(0.35f);

        Label matchLabel = new Label("Matching", labelStyle);
        matchLabel.setAlignment(Align.center);
        matchLabel.setFontScale(0.35f);

        Label modeLabel = new Label("Mode", labelStyle);
        modeLabel.setAlignment(Align.center);
        modeLabel.setFontScale(0.35f);

        Image greenBackground = new Image(Game.easyAssetManager.get("pixelWhite", Texture.class));
        greenBackground.setColor(Color.GREEN);
        greenBackground.getColor().a = 0.75f;

        Image redBackground = new Image(Game.easyAssetManager.get("pixelWhite", Texture.class));
        redBackground.setColor(Color.RED);
        redBackground.getColor().a = 0.75f;

        Image goldBackground = new Image(Game.easyAssetManager.get("pixelWhite", Texture.class));
        goldBackground.setColor(Color.GOLD);
        goldBackground.getColor().a = 0.75f;

        Image blueBackground = new Image(Game.easyAssetManager.get("pixelWhite", Texture.class));
        blueBackground.setColor(56f/255f, 122f/255f, 244f/255f, 0.75f);

        Stack numShapesStack = new Stack(greenBackground, numShapesLabel);
        Stack colorStack = new Stack(goldBackground, colorLabel);
        Stack matchStack = new Stack(redBackground, matchLabel);
        Stack modeStack = new Stack(blueBackground, modeLabel);

        Table numShapesTable = new Table();
        numShapesTable.add(numShapesStack).colspan(6).expandX().fillX().height(35f).spaceBottom(10f);
        numShapesTable.row();
        numShapesTable.add().expandX().fillX();
        numShapesTable.add(threeShapes).size(50 ,50);
        numShapesTable.add(fourShapes).size(50 ,50);
        numShapesTable.add(fiveShapes).size(50 ,50);
        numShapesTable.add(sixShapes).size(50 ,50);
        numShapesTable.add().expandX().fillX();

        Table colorTable = new Table();
        colorTable.add(colorStack).colspan(4).expandX().fillX().height(35f).spaceBottom(10f);
        colorTable.row();
        colorTable.add().expandX().fillX();
        colorTable.add(colorSame).size(125, 50);
        colorTable.add(colorRandom).size(125, 50);
        colorTable.add().expandX().fillX();

        Table matchTable = new Table();
        matchTable.add(matchStack).colspan(4).expandX().fillX().height(35f).spaceBottom(10f);
        matchTable.row();
        matchTable.add().expandX().fillX();
        matchTable.add(matchShape).size(125, 50);
        matchTable.add(matchColor).size(125, 50);
        matchTable.add().expandX().fillX();

        Table modeTable = new Table();
        modeTable.add(modeStack).colspan(6).expandX().fillX().height(35f).spaceBottom(10f);
        modeTable.row();
        modeTable.add().expandX().fillX();
        modeTable.add(modePractice).size(120, 50);
        modeTable.add(modeBest).size(120, 50);
        modeTable.add(modeTimed).size(120, 50);
        modeTable.add(modeFrenzy).size(120, 50);
        modeTable.add().expandX().fillX();

        Table startTable = new Table();
        startTable.add(backButton).size(125, 50).padRight(50);
        startTable.add(startGame).size(125, 50);

        choicesTable.add(modeTable).expandX().fillX().padTop(50f);
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
        modeFrenzy.setChecked(false);
        modeTimed.setChecked(false);
        modePractice.setChecked(false);
        modeBest.setChecked(false);
        threeShapes.setChecked(false);
        fourShapes.setChecked(false);
        fiveShapes.setChecked(false);
        sixShapes.setChecked(false);
    }

    private static void changeChoices(GameSettings.GameType gameType){
        if(gameType == GameSettings.GameType.Frenzy){
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
            if(GameSettings.gameType == GameSettings.GameType.Frenzy) {
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
        Label.LabelStyle style = new Label.LabelStyle(Game.defaultHugeFont, Color.WHITE);

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

    public static void makeInfoPage(){
        infoTable = new Table();
        infoTable.setFillParent(true);

        /*
        Made by
        Company name
        My name

        Music by
        Artist name
        Website link

        Sound 1 by
        Artist name
        Link I got it from..

        ...
         */

        float fontScale = 0.2f;

        Table innerTable = new Table();
        com.badlogic.gdx.scenes.scene2d.ui.ScrollPane scrollPane = new com.badlogic.gdx.scenes.scene2d.ui.ScrollPane(innerTable);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = Game.defaultHugeFont;
        buttonStyle.up = new NinePatchDrawable(new NinePatch(Game.easyAssetManager.get("buttonDark_up9", Texture.class)));

        TextButton backButton = new TextButton("Back", buttonStyle);
        backButton.getLabel().setFontScale(0.2f);

        Label.LabelStyle style = new Label.LabelStyle(Game.defaultHugeFont, Color.WHITE);

        Label madeByTitle = new Label("Made By:", style);
        madeByTitle.setFontScale(0.4f);
        madeByTitle.setAlignment(Align.center);

        Label madeByCompany = new Label("Quickbite Games", style);
        madeByCompany.setFontScale(0.4f);
        madeByCompany.setAlignment(Align.center);

        Label musicBy = new Label("Music by:", style);
        musicBy.setFontScale(fontScale);
        musicBy.setWrap(true);
        musicBy.setAlignment(Align.center);

        Label musicByName = new Label("'D SMILEZ - Let You're Body Move' \n (on http://freemusicarchive.org) / CC BY 4.0 \n This is a 15 second looped snippet of the song.", style);
        musicByName.setFontScale(fontScale);
        musicByName.setWrap(true);
        musicByName.setAlignment(Align.center);

        Label musicByLink = new Label("", style);
        musicByLink.setFontScale(fontScale);
        musicByLink.setWrap(true);
        musicByLink.setAlignment(Align.center);

        Label clickSoundBy = new Label("'Interface1' by Eternitys\n (on freesound.org) / CC BY 1.0", style);
        clickSoundBy.setFontScale(fontScale);
        clickSoundBy.setWrap(true);
        clickSoundBy.setAlignment(Align.center);

        Label popSoundBy = new Label("'3 Popping Pops' by Eternitys\n (on freesound.org) / CC BY 1.0", style);
        popSoundBy.setFontScale(fontScale);
        popSoundBy.setWrap(true);
        popSoundBy.setAlignment(Align.center);

        Label dropletSoundBy = new Label("'Droplet' by Porphyr\n (on freesound.org) / CC BY 3.0", style);
        dropletSoundBy.setFontScale(fontScale);
        dropletSoundBy.setWrap(true);
        dropletSoundBy.setAlignment(Align.center);

        Label successSoundBy = new Label("'success 1' by fins\n (on freesound.org) / CC BY 1.0", style);
        successSoundBy.setFontScale(fontScale);
        successSoundBy.setWrap(true);
        successSoundBy.setAlignment(Align.center);

        Label erroSoundBy = new Label("'Error' by Autistic Lucario\n (on freesound.org) / CC BY 1.0", style);
        erroSoundBy.setFontScale(fontScale);
        erroSoundBy.setWrap(true);
        erroSoundBy.setAlignment(Align.center);

        innerTable.add(madeByTitle).expandX().fillX();
        innerTable.row();
        innerTable.add(madeByCompany).expandX().fillX().padBottom(20f);
        innerTable.row();
        innerTable.add(musicBy).expandX().fillX();
        innerTable.row();
        innerTable.add(musicByName).expandX().fillX();
        innerTable.row();
        innerTable.add(musicByLink).expandX().fillX().padBottom(20f);
        innerTable.row();
        innerTable.add(clickSoundBy).expandX().fillX().padBottom(20f);
        innerTable.row();
        innerTable.add(popSoundBy).expandX().fillX().padBottom(20f);
        innerTable.row();
        innerTable.add(dropletSoundBy).expandX().fillX().padBottom(20f);
        innerTable.row();
        innerTable.add(successSoundBy).expandX().fillX().padBottom(20f);
        innerTable.row();
        innerTable.add(erroSoundBy).expandX().fillX().padBottom(20f);
        innerTable.row();
        innerTable.add(backButton).size(125f, 50f);

        infoTable.setBackground(new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("pixelDark", Texture.class))));
        infoTable.add(scrollPane).expand().fill();
        infoTable.setPosition(Game.viewport.getWorldWidth(), 0f);

        Game.stage.addActor(infoTable);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                infoTable.addAction(Actions.moveTo(Game.viewport.getWorldWidth(), 0f, 0.4f, Interpolation.circleIn));
            }
        });
    }
}
