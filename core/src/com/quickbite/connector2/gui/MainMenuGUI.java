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
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
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
import com.badlogic.gdx.utils.Timer;
import com.quickbite.connector2.Constants;
import com.quickbite.connector2.GH;
import com.quickbite.connector2.Game;
import com.quickbite.connector2.GameData;
import com.quickbite.connector2.GameSettings;
import com.quickbite.connector2.SoundManager;
import com.quickbite.connector2.screens.GameScreen;
import com.quickbite.connector2.screens.MainMenu;

/**
 * Created by Paha on 5/2/2016.
 */
public class MainMenuGUI {
    public static Table mainTable, leaderSelectionTable, choicesTable, leaderDisplayTable, mainMenuTable;
    public static Table infoTable;

    private static ImageTextButton leaderboards, loginGPG, start, quit;
    private static TextButton colorSame, colorRandom, matchShape, matchColor, modePractice, modeBest, modeTimed, modeFrenzy, startGameButton, infoButton;
    private static TextButton threeShapes, fourShapes, fiveShapes, sixShapes;
    private static Stack noAdsButtonStack;

    private static ButtonGroup<TextButton> modeGroup, matchGroup, colorGroup, numShapesGroup;

    public static Image titleImage;

    private static Game game;
    private static MainMenu mainMenu;

    private static NinePatch patchUp;
    private static NinePatch patchDown;

    public static void makeGUI(final Game game, final MainMenu mainMenu){
        MainMenuGUI.game = game;
        MainMenuGUI.mainMenu = mainMenu;

        patchUp = new NinePatch(Game.UIAtlas.findRegion("buttonDark_up9"), 8, 8, 11, 7);
        patchDown = new NinePatch(Game.UIAtlas.findRegion("buttonDark_down9"), 8, 8, 11, 7);

        mainTable = new Table();

        leaderDisplayTable = new Table();
        leaderSelectionTable = new Table();
        mainMenuTable = new Table();

        titleImage = new Image(Game.easyAssetManager.get("title", Texture.class));

        buildMainMenu();
        buildChoicesMenu();

        showMainMenu();

        mainTable.setFillParent(true);
        Game.stage.addActor(mainTable);

        makeInfoPage();
    }

    private static void buildMainMenu(){
        ImageButton.ImageButtonStyle soundButtonStyle = new ImageButton.ImageButtonStyle();
        soundButtonStyle.up = new NinePatchDrawable(patchUp);
        soundButtonStyle.down = new NinePatchDrawable(patchDown);
        soundButtonStyle.imageUp = new TextureRegionDrawable(Game.UIAtlas.findRegion("soundIcon"));

        ImageButton.ImageButtonStyle musicButtonStyle = new ImageButton.ImageButtonStyle();
        musicButtonStyle.up = new NinePatchDrawable(patchUp);
        musicButtonStyle.down = new NinePatchDrawable(patchDown);
        musicButtonStyle.imageUp = new TextureRegionDrawable(Game.UIAtlas.findRegion("musicIcon"));

        TextButton.TextButtonStyle removeButtonStyle = new TextButton.TextButtonStyle();
        removeButtonStyle.up = new NinePatchDrawable(patchUp);
        removeButtonStyle.down = new NinePatchDrawable(patchDown);
        removeButtonStyle.font = Game.defaultHugeFont;
        removeButtonStyle.fontColor = Color.WHITE;

        TextButton.TextButtonStyle regularStyle = new TextButton.TextButtonStyle();
        regularStyle.up = new NinePatchDrawable(patchUp);
        regularStyle.down = new NinePatchDrawable(patchDown);
        regularStyle.font = Game.defaultHugeFont;
        regularStyle.fontColor = Color.WHITE;
        regularStyle.disabledFontColor = new Color(1, 1, 1, 0.5f);

        ImageTextButton.ImageTextButtonStyle startStyle = new ImageTextButton.ImageTextButtonStyle();
        startStyle.up = new TextureRegionDrawable(Game.UIAtlas.findRegion("buttonDark_up"));
        startStyle.down = new TextureRegionDrawable(Game.UIAtlas.findRegion("buttonDark_down"));
        startStyle.disabled = new TextureRegionDrawable(Game.UIAtlas.findRegion("buttonDark_up"));
        startStyle.font = Game.defaultHugeFont;
        startStyle.fontColor = Color.WHITE;
        startStyle.disabledFontColor = new Color(1, 1, 1, 0.5f);
        startStyle.imageUp = new TextureRegionDrawable(Game.UIAtlas.findRegion("startIcon"));

        ImageTextButton.ImageTextButtonStyle leaderboardButtonStyle = new ImageTextButton.ImageTextButtonStyle(startStyle);
        leaderboardButtonStyle.imageUp = new TextureRegionDrawable(Game.UIAtlas.findRegion("leaderboardIcon"));

        ImageTextButton.ImageTextButtonStyle quitButtonStyle = new ImageTextButton.ImageTextButtonStyle(startStyle);
        quitButtonStyle.imageUp = new TextureRegionDrawable(Game.UIAtlas.findRegion("X"));

        ImageTextButton.ImageTextButtonStyle loginStyle = new ImageTextButton.ImageTextButtonStyle(startStyle);
        loginStyle.imageUp = new TextureRegionDrawable(Game.UIAtlas.findRegion("googlePlayGamesIcon"));

        infoButton = new TextButton("?", regularStyle);
        infoButton.getLabel().setFontScale(0.5f);

        final ImageButton toggleSound = new ImageButton(soundButtonStyle);
        toggleSound.getImage().setColor(GameData.colorMap.get("Blue"));

        final ImageButton toggleMusic = new ImageButton(musicButtonStyle);
        toggleMusic.getImage().setColor(GameData.colorMap.get("Orange"));

        final TextButton removeAdsButton = new TextButton("Ads", removeButtonStyle);
        removeAdsButton.getLabel().setFontScale(0.2f);
        final Image image = new Image(new TextureRegionDrawable(Game.UIAtlas.findRegion("no")));
        image.setSize(40f, 40f);
        image.setTouchable(Touchable.disabled);

        noAdsButtonStack = new Stack(removeAdsButton, image);

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

        quit = new ImageTextButton("Quit", quitButtonStyle);
        quit.getLabel().setFontScale(0.4f);
        quit.getLabelCell().fillX().expandX();
        quit.getImageCell().size(64f, 64f);
        quit.getImageCell().left();
        quit.getImage().setColor(Color.RED);

        changeLoginButton(Game.resolver.getSignedInGPGS());

        Table iconTable = new Table();
        Table buttonTable = new Table();

        //The info button, remove ads, toggle sound/music buttons.
        iconTable.add(infoButton).left().padLeft(5f).size(45f);
        iconTable.add().expandX().fillX();
        if(Game.adInterface.showAds()) iconTable.add(noAdsButtonStack).padRight(10f).size(45f);
        iconTable.add(toggleSound).padRight(10f).size(45f);
        iconTable.add(toggleMusic).padRight(5f).size(45f);

        //The main set of buttons.
        buttonTable.add(start).size(200, 75);
        buttonTable.row().padTop(20);
        buttonTable.add(loginGPG).size(200, 75);
        buttonTable.row().padTop(20);
        buttonTable.add(leaderboards).size(200, 75);
        buttonTable.row().padTop(20);
        buttonTable.add(quit).size(200, 75);

        mainMenuTable.top();

        //Laying out the icon table, title, and main buttons.
        mainMenuTable.add(iconTable).fillX();
        mainMenuTable.row().padTop(50);
        mainMenuTable.add(titleImage);
        mainMenuTable.row().padTop(50);
        mainMenuTable.add(buttonTable);

        mainTable.add(mainMenuTable);
        mainTable.setFillParent(true);
        mainTable.top();

        mainMenuTable.setFillParent(true);
        Game.stage.addActor(mainMenuTable);

        infoButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                infoTable.addAction(Actions.moveTo(0f, 0f, 0.4f, Interpolation.circleOut));
                SoundManager.playSound("click");
                Game.resolver.submitEvent(Constants.EVENT_CHECKEDINFO, "guiClick:info:click");
                Game.adInterface.hideAdmobBannerAd();
            }
        });

        toggleMusic.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(toggleMusic.isChecked()){
                    toggleMusic.getImage().getColor().a = 0.3f;
                    SoundManager.setMusicOn(false);
                }else{
                    toggleMusic.getImage().getColor().a = 1f;
                    SoundManager.setMusicOn(true);
                }

                Game.resolver.submitEvent(Constants.EVENT_TOGGLEDMUSIC, "guiClick:toggleMusic:click");
            }
        });

        toggleSound.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(toggleSound.isChecked()){
                    toggleSound.getImage().getColor().a = 0.3f;
                    SoundManager.setSoundsOn(false);
                }else{
                    toggleSound.getImage().getColor().a = 1f;
                    SoundManager.setSoundsOn(true);
                }

                Game.resolver.submitEvent(Constants.EVENT_TOGGLEDSOUND, "guiClick:toggleSound:click");
            }
        });

        removeAdsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.playSound("click");
                Game.resolver.submitEvent("", "guiClick:buyNoAds:click");
                setupBuyNoAds();
            }
        });

        toggleSound.setChecked(!SoundManager.isSoundsOn());
        toggleMusic.setChecked(!SoundManager.isMusicOn());
    }

    private static void setupBuyNoAds(){
        final Table table = new Table();
        final Table innerTable = new Table();

        final Label.LabelStyle labelStyle = new Label.LabelStyle(Game.defaultHugeFont, Color.WHITE);

        final TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = new NinePatchDrawable(patchUp);
        buttonStyle.down = new NinePatchDrawable(patchDown);
        buttonStyle.font = Game.defaultHugeFont;
        buttonStyle.fontColor = Color.WHITE;

        final Label descLabel = new Label("Do you want to pay $0.99 to support the developer and remove ads from the game?", labelStyle);
        descLabel.setWrap(true);
        descLabel.setFontScale(0.4f);
        descLabel.setAlignment(Align.center);

        final TextButton yes = new TextButton("Yes!", buttonStyle);
        yes.getLabel().setAlignment(Align.center);
        yes.getLabel().setFontScale(0.4f);

        final TextButton no = new TextButton("No!", buttonStyle);
        no.getLabel().setFontScale(0.4f);

        innerTable.row();
        innerTable.add(descLabel).expandX().fillX().colspan(2).pad(0f, 10f, 0f, 10f);
        innerTable.row().spaceTop(50f);
        innerTable.add(yes).size(100f, 50f).expandX();
        innerTable.add(no).size(100f, 50f).expandX();
        innerTable.row();

        table.add(innerTable).expandX().fillX();
        table.setFillParent(true);
        table.setTouchable(Touchable.enabled); //This makes it so things behind the table can't be clicked.

        table.background(new TextureRegionDrawable(new TextureRegion(GH.createPixel(new Color(0.1f, 0.1f, 0.1f, 0.9f)))));

        yes.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //Do stuff
                SoundManager.playSound("click");
                Game.resolver.purchaseNoAds();
                table.remove();
            }
        });

        no.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //?
                SoundManager.playSound("click");
                table.remove();
            }
        });

//        table.debugAll();
        Game.stage.addActor(table);
    }

    public static void removeNoAdsButton(){
        noAdsButtonStack.remove();
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
        regularStyle.up = new TextureRegionDrawable(Game.UIAtlas.findRegion("buttonDark_up"));
        regularStyle.down = new TextureRegionDrawable(Game.UIAtlas.findRegion("buttonDark_down"));
        regularStyle.font = Game.defaultHugeFont;
        regularStyle.disabledFontColor = new Color(1, 1, 1, 0.5f);
        regularStyle.disabledFontColor = new Color(0.5f, 0.5f, 0.5f, 0.5f);

        Color color = GameData.colorMap.get("Green");
        TextButton.TextButtonStyle numShapesButtonStyle = new TextButton.TextButtonStyle();
        numShapesButtonStyle.checked = new TextureRegionDrawable(new TextureRegion(GH.createPixel(new Color(color.r, color.g, color.b, 0.7f))));
        numShapesButtonStyle.font = Game.defaultHugeFont;
        numShapesButtonStyle.disabledFontColor = new Color(0.5f, 0.5f, 0.5f, 0.5f);
        numShapesButtonStyle.fontColor = Color.WHITE;

        color = GameData.colorMap.get("Gold");
        TextButton.TextButtonStyle colorButtonStyle = new TextButton.TextButtonStyle(numShapesButtonStyle);
        colorButtonStyle.checked = new TextureRegionDrawable(new TextureRegion(GH.createPixel(new Color(color.r, color.g, color.b, 0.7f))));

        color = GameData.colorMap.get("Red");
        TextButton.TextButtonStyle matchButtonStyle = new TextButton.TextButtonStyle(numShapesButtonStyle);
        matchButtonStyle.checked = new TextureRegionDrawable(new TextureRegion(GH.createPixel(new Color(color.r, color.g,color.b, 0.7f))));

        color = new Color(GameData.colorMap.get("Blue"));
        color.a = 0.7f;
        TextButton.TextButtonStyle modeButtonStyle = new TextButton.TextButtonStyle(numShapesButtonStyle);
        modeButtonStyle.checked = new TextureRegionDrawable(new TextureRegion(GH.createPixel(color)));

        TextButton.TextButtonStyle buttonStyle = new  TextButton.TextButtonStyle();
        buttonStyle.font = Game.defaultHugeFont;
        buttonStyle.up = new TextureRegionDrawable(Game.UIAtlas.findRegion("buttonDark_up"));
        buttonStyle.over = new TextureRegionDrawable(Game.UIAtlas.findRegion("buttonDark_up"));
        buttonStyle.down = new TextureRegionDrawable(Game.UIAtlas.findRegion("buttonDark_down"));

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

        startGameButton = new TextButton("Start", regularStyle);
        startGameButton.getLabel().setFontScale(0.4f);
        startGameButton.setDisabled(true);
        startGameButton.getColor().a = 0.5f;

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
                Game.resolver.submitEvent(Constants.EVENT_QUIT, "guiClick:quit:click");
                SoundManager.dispose();
                GameData.dispose();
                Game.stage.clear();
                Game.easyAssetManager.clear();
                Game.easyAssetManager.dispose();

                Gdx.app.exit();
            }
        });

        leaderboards.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Game.resolver.getLeaderboardGPGS("DoesntMatter");
                SoundManager.playSound("click");
                Game.resolver.submitEvent(Constants.EVENT_CHECKEDLEADERBOARDS, "guiClick:leaderboards:click");
            }
        });

        loginGPG.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(((Boolean)loginGPG.getUserObject())) {
                    Game.resolver.loginGPGS();
                    Game.resolver.submitEvent("", "guiClick:loginGPG:login");
                }else {
                    Game.resolver.logoutGPGS();
                    Game.resolver.submitEvent("", "guiClick:loginGPG:logout");
                }

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
                GameSettings.gameType = GameSettings.GameType.Practice;
                changeChoices(GameSettings.GameType.Practice);
                SoundManager.playSound("click");
            }
        });

        modeBest.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                changeChoices(GameSettings.GameType.Fastest);
                GameSettings.gameType = GameSettings.GameType.Fastest;
                checkAllOptionsSelected();
                SoundManager.playSound("click");
            }
        });

        modeTimed.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                changeChoices(GameSettings.GameType.Timed);
                GameSettings.gameType = GameSettings.GameType.Timed;
                checkAllOptionsSelected();
                SoundManager.playSound("click");
            }
        });

        modeFrenzy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                changeChoices(GameSettings.GameType.Frenzy);
                GameSettings.gameType = GameSettings.GameType.Frenzy;
                checkAllOptionsSelected();
                SoundManager.playSound("click");
            }
        });

        startGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                startGame();
                Game.resolver.submitGameStructure();
                SoundManager.playSound("click");
            }
        });

        modeGroup = new ButtonGroup<TextButton>(modePractice, modeBest, modeTimed, modeFrenzy);
        modeGroup.setMaxCheckCount(1);
        modeGroup.setMinCheckCount(1);

        matchGroup = new ButtonGroup<TextButton>(matchShape, matchColor);
        matchGroup.setMaxCheckCount(1);
        matchGroup.setMinCheckCount(1);

        colorGroup = new ButtonGroup<TextButton>(colorSame, colorRandom);
        colorGroup.setMaxCheckCount(1);
        colorGroup.setMinCheckCount(1);

        numShapesGroup = new ButtonGroup<TextButton>(threeShapes, fourShapes, fiveShapes, sixShapes);
        numShapesGroup.setMaxCheckCount(1);
        numShapesGroup.setMinCheckCount(1);

        //Initially check the defaults.
        initializeSettingsMenu();

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

        /* The backgrounds for each section label*/

        color = new Color(GameData.colorMap.get("Green"));
        Image greenBackground = new Image(GH.createPixel(color));
        greenBackground.setColor(color);
        greenBackground.getColor().a = 0.75f;

        color = new Color(GameData.colorMap.get("Red"));
        Image redBackground = new Image(GH.createPixel(color));
        redBackground.setColor(color);
        redBackground.getColor().a = 0.75f;

        color = new Color(GameData.colorMap.get("Gold"));
        Image goldBackground = new Image(GH.createPixel(color));
        goldBackground.setColor(color);
        goldBackground.getColor().a = 0.75f;

        color = new Color(GameData.colorMap.get("Blue"));
        Image blueBackground = new Image(GH.createPixel(color));
        blueBackground.setColor(color);
        blueBackground.getColor().a = 0.75f;

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
        startTable.add(startGameButton).size(125, 50);

        choicesTable.add(modeTable).expandX().fillX().padTop(30f);
        choicesTable.row();
        choicesTable.add(matchTable).expandX().fillX().spaceTop(50f);
        choicesTable.row();
        choicesTable.add(colorTable).expandX().fillX().spaceTop(50f);
        choicesTable.row();
        choicesTable.add(numShapesTable).expandX().fillX().spaceTop(50f);
        choicesTable.row();
        choicesTable.add(startTable).fillX().spaceTop(20f);

        choicesTable.top();
        choicesTable.setFillParent(true);
        choicesTable.setPosition(Game.viewport.getWorldWidth(), 0f);

        Game.stage.addActor(choicesTable);
    }

    private static void initializeSettingsMenu(){
        GameSettings.gameType = GameSettings.GameType.Practice;
        GameSettings.matchType = GameSettings.MatchType.Shapes;
        GameSettings.colorType = GameSettings.ColorType.Normal;
        GameSettings.numShapes = 3;

        modePractice.setChecked(true);
        matchShape.setChecked(true);
        colorSame.setChecked(true);
        threeShapes.setChecked(true);
        checkAllOptionsSelected();
    }

    private static void startGame(){
        choicesTable.addAction(Actions.sequence(Actions.moveTo(-Game.viewport.getWorldWidth(), 0f, 0.3f, Interpolation.circle), new Action() {
            @Override
            public boolean act(float delta) {
                Game.stage.clear();
                mainMenu.dispose();
                game.setScreen(new GameScreen(game));
                Timer.instance().clear();
                GameData.reset(); //Reset the data from the main menu fanciness
                GH.submitGameSettingsEvent();
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
    }


    /**
     * Simply lays out already constructed components on the main menu
     *
     */
    private static void toMainMenu(){
        mainMenuTable.addAction(Actions.moveTo(0f, 0f, 0.3f, Interpolation.circle));
        choicesTable.addAction(Actions.moveTo(Game.viewport.getWorldWidth(), 0f, 0.3f, Interpolation.circle));
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
            startGameButton.setDisabled(false);
            startGameButton.getColor().a = 1f;
            startGameButton.getStyle().fontColor = Color.WHITE;
        }else{
            startGameButton.setDisabled(true);
            startGameButton.getColor().a = 0.5f;
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

            colorGroup.uncheckAll();
            numShapesGroup.uncheckAll();

            GameSettings.numShapes = 3;
            GameSettings.colorType = GameSettings.ColorType.Random;
        }else{
            colorSame.setDisabled(false);
            colorRandom.setDisabled(false);
            threeShapes.setDisabled(false);
            fourShapes.setDisabled(false);
            fiveShapes.setDisabled(false);
            sixShapes.setDisabled(false);

            //If it was frenzy before, reset it.
            if(GameSettings.gameType == GameSettings.GameType.Frenzy) {
                colorSame.setChecked(true);
                threeShapes.setChecked(true);

                GameSettings.colorType = GameSettings.ColorType.Normal;
                GameSettings.numShapes = 3;
            }
        }

        checkAllOptionsSelected();
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

        String blue = "[#"+GameData.colorMap.get("Blue").toString()+"]";
        String red = "[#"+GameData.colorMap.get("Red").toString()+"]";

        float fontScale = 0.2f;

        Table innerTable = new Table();
        com.badlogic.gdx.scenes.scene2d.ui.ScrollPane scrollPane = new com.badlogic.gdx.scenes.scene2d.ui.ScrollPane(innerTable);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = Game.defaultHugeFont;
        buttonStyle.up = new NinePatchDrawable(new NinePatch(Game.UIAtlas.findRegion("buttonDark_up9"), 8, 8, 11, 7));

        TextButton backButton = new TextButton("Back", buttonStyle);
        backButton.getLabel().setFontScale(0.3f);

        Label.LabelStyle style = new Label.LabelStyle(Game.defaultHugeFont, Color.WHITE);

        Label howToPlayTitle = new Label(blue+"How To Play:[]", style);
        howToPlayTitle.setFontScale(0.4f);
        howToPlayTitle.setAlignment(Align.center);
        howToPlayTitle.setWrap(true);

        Label howToPlay = new Label("Tap and drag to connect a shape to another shape. Don't cross lines or connect to the " +
                "wrong shape.", style);
        howToPlay.setFontScale(fontScale);
        howToPlay.setAlignment(Align.center);
        howToPlay.setWrap(true);

        Label gameModesTitle = new Label(blue+"Game Modes:[]", style);
        gameModesTitle.setFontScale(0.4f);
        gameModesTitle.setAlignment(Align.center);
        gameModesTitle.setWrap(true);

        Label gameModes = new Label(red+"Practice[]\nFree-play without time limits." +
                red+"\nBest[]\nBest out of 10. Complete 10 rounds as quickly as possible." +
                red+"\nTimed[]\nRace against the clock starting at 10 seconds per round. Each round decreases time." +
                red+"\nFrenzy[]\nRandom shapes spawn and you must connect as many as you can in 30 seconds. Each connection gives you 1 bonus second", style);
        gameModes.setFontScale(fontScale);
        gameModes.setWrap(true);
        gameModes.setAlignment(Align.center);

        Label madeByTitle = new Label(blue+"Created By[]", style);
        madeByTitle.setFontScale(0.4f);
        madeByTitle.setAlignment(Align.center);

        Label madeByCompany = new Label("Quickbite Games", style);
        madeByCompany.setFontScale(0.3f);
        madeByCompany.setAlignment(Align.center);

        Label musicBy = new Label(blue+"Music by:[]", style);
        musicBy.setFontScale(0.4f);
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

        Label soundsBy = new Label(blue+"Sounds by:[]", style);
        soundsBy.setFontScale(0.4f);
        soundsBy.setWrap(true);
        soundsBy.setAlignment(Align.center);

        Label clickSoundBy = new Label("'Interface1' by Eternitys\n (on freesound.org) / CC BY 1.0", style);
        clickSoundBy.setFontScale(fontScale);
        clickSoundBy.setWrap(true);
        clickSoundBy.setAlignment(Align.center);

        Label popSoundBy = new Label("'3 Popping Pops' by wubitog\n (on freesound.org) / CC BY 1.0", style);
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
        innerTable.add(howToPlayTitle).expandX().fillX();
        innerTable.row();
        innerTable.add(howToPlay).expandX().fillX().padBottom(10f);
        innerTable.row();
        innerTable.add(gameModesTitle).expandX().fillX();
        innerTable.row();
        innerTable.add(gameModes).expandX().fillX().padBottom(10f).padLeft(10f);
        innerTable.row();
        innerTable.add(musicBy).expandX().fillX();
        innerTable.row();
        innerTable.add(musicByName).expandX().fillX();
        innerTable.row();
        innerTable.add(musicByLink).expandX().fillX();
        innerTable.row();
        innerTable.add(soundsBy).expandX().fillX();
        innerTable.row();
        innerTable.add(clickSoundBy).expandX().fillX().padBottom(10f);
        innerTable.row();
        innerTable.add(popSoundBy).expandX().fillX().padBottom(10f);
        innerTable.row();
        innerTable.add(dropletSoundBy).expandX().fillX().padBottom(10f);
        innerTable.row();
        innerTable.add(successSoundBy).expandX().fillX().padBottom(10f);
        innerTable.row();
        innerTable.add(erroSoundBy).expandX().fillX().padBottom(10f);
        innerTable.row();
        innerTable.add(backButton).size(150f, 50f);

        infoTable.setBackground(new TextureRegionDrawable(new TextureRegion(GH.createPixel(Color.BLACK))));
        infoTable.add(scrollPane).expand().fill();
        infoTable.setPosition(Game.viewport.getWorldWidth(), 0f);

        Game.stage.addActor(infoTable);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                infoTable.addAction(Actions.moveTo(Game.viewport.getWorldWidth(), 0f, 0.4f, Interpolation.circleIn));
                SoundManager.playSound("click");
                Game.adInterface.showAdmobBannerAd();
            }
        });
    }
}
