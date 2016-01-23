package com.quickbite.connector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import java.text.DecimalFormat;

/**
 * Created by Paha on 1/8/2016.
 */
public class GameScreen implements Screen, InputProcessor{
    public enum GameState {Starting, Running, Ending, Over}
    private GameState currGameState;

    private TextureRegion topTexture;

    private Game game;
    private Integer[] colorIDs;
    private Vector2[] positions;
    private TextureRegion[] shapes;
    private TextureRegion[] shapesGlow;
    private TextureRegion[] gameOverShapes;
    private Color[] shapeColors;

    private Array<GameShape> shapeList;
    private Array<Vector2>[] lists;
    private GameShape currShape = null;
    private int winCounter = 0, lineCounter = 0;
    private volatile boolean gameOver = false;
    private float currScale = 0, currRotation = 0;
    private double startTime, endTime, avg;
    public int currRound, maxRounds=10, currScore;
    private boolean dragging = false, failedLastRound = false;
    private DecimalFormat formatter = new DecimalFormat("#.00");

    private float roundTime, roundTimeDecreaseAmount = 1, roundTimeStart = 10;

    private final float disBetweenPositions = 10, topArea = 0.1f;
    private float sizeOfSpots = 480/5, sizeOfShapes = 480/6;

    /* GUI STUFF */
    private Table table = new Table();
    private Image gameOverImage;
    private Label roundLabel, avgLabel, colorTypeLabel, matchTypeLabel, gameTypeLabel, timerLabel;
    private TextButton restartButton, mainMenuButton;
    private ImageButton backButton;

    public GameScreen(Game game){
        this.game = game;

        //Handle input stuff.
        InputMultiplexer multi = new InputMultiplexer();
        multi.addProcessor(this);
        multi.addProcessor(Game.stage);
        Gdx.input.setInputProcessor(multi);

        lists = new Array[GameSettings.numShapes];

        this.sizeOfSpots = Gdx.graphics.getWidth()/6;
        this.sizeOfShapes = Gdx.graphics.getWidth()/6;
    }

    @Override
    public void show() {
        float startingY = Game.camera.viewportHeight/2 - Game.camera.viewportHeight*this.topArea - sizeOfSpots/2;
        float startingX = Game.camera.position.x + Game.camera.viewportWidth/2 - sizeOfSpots/2;

        int xSpots = (int)(Game.camera.viewportWidth/sizeOfSpots);
        int ySpots = (int)((Gdx.graphics.getHeight()- sizeOfSpots - (Gdx.graphics.getHeight()*this.topArea))/sizeOfSpots)+1;
        int num = xSpots*ySpots;

        this.topTexture = new TextureRegion(Game.easyAssetManager.get("Top", Texture.class));

        positions = new Vector2[num];
        for(int i=0;i<num;i++){
            Vector2 vec = new Vector2(startingX - ((i*sizeOfSpots)%(sizeOfSpots*(xSpots))), startingY - ((i/xSpots)*sizeOfSpots));
            positions[i] = vec;
        }

        this.colorIDs = new Integer[GameSettings.numShapes *2];
        for(int i=0;i<this.colorIDs.length;i++)
            colorIDs[i] = i/2;

        shapes = new TextureRegion[6];
        shapes[0] = new TextureRegion(Game.easyAssetManager.get("Star", Texture.class));
        shapes[1] = new TextureRegion(Game.easyAssetManager.get("Square", Texture.class));
        shapes[2] = new TextureRegion(Game.easyAssetManager.get("Circle", Texture.class));
        shapes[3] = new TextureRegion(Game.easyAssetManager.get("Diamond", Texture.class));
        shapes[4] = new TextureRegion(Game.easyAssetManager.get("Triangle", Texture.class));
        shapes[5] = new TextureRegion(Game.easyAssetManager.get("Hexagon", Texture.class));

        shapesGlow = new TextureRegion[6];
        shapesGlow[0] = new TextureRegion(Game.easyAssetManager.get("Star_glow", Texture.class));
        shapesGlow[1] = new TextureRegion(Game.easyAssetManager.get("Square_glow", Texture.class));
        shapesGlow[2] = new TextureRegion(Game.easyAssetManager.get("Circle_glow", Texture.class));
        shapesGlow[3] = new TextureRegion(Game.easyAssetManager.get("Diamond_glow", Texture.class));
        shapesGlow[4] = new TextureRegion(Game.easyAssetManager.get("Triangle_glow", Texture.class));
        shapesGlow[5] = new TextureRegion(Game.easyAssetManager.get("Hexagon_glow", Texture.class));

        shapeColors = new Color[6];
        shapeColors[0] = Color.YELLOW;
        shapeColors[1] = Color.RED;
        shapeColors[2] = Color.GREEN;
        shapeColors[3] = Color.CYAN;
        shapeColors[4] = Color.ORANGE;
        shapeColors[5] = Color.BLUE;

        this.gameOverShapes = new TextureRegion[2];

        Texture texture = Game.easyAssetManager.get("checkmark", Texture.class);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.gameOverShapes[0] = new TextureRegion(texture);

        texture = Game.easyAssetManager.get("X", Texture.class);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.gameOverShapes[1] = new TextureRegion(texture);

        //Reset our stuff.
        this.currRound = this.currScore = 0;
        this.avg = 0;

        if(GameSettings.gameType == GameSettings.GameType.Timed)
            this.roundTime = this.roundTimeStart;

        initRound();
        this.initLists();
        this.makeGUI();
    }

    private void makeGUI(){
        this.table = new Table();
        this.table.setFillParent(true);
        Game.stage.addActor(this.table);

        TextureRegion arrow = new TextureRegion(Game.easyAssetManager.get("leftArrow", Texture.class));

        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
        imageButtonStyle.up = new TextureRegionDrawable(Game.defaultButtonUp);
        imageButtonStyle.down = new TextureRegionDrawable(Game.defaultButtonDown);
        imageButtonStyle.imageUp = new TextureRegionDrawable(arrow);
        imageButtonStyle.imageDown = new TextureRegionDrawable(arrow);

        this.backButton = new ImageButton(imageButtonStyle);
        this.backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Game.stage.clear();
                game.setScreen(new MainMenu(game));
            }
        });
        this.backButton.setSize(64, 32);
        this.backButton.setPosition(Gdx.graphics.getWidth()/2 - 32, Gdx.graphics.getHeight() - 32);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_normal", Texture.class)));
        style.down = new TextureRegionDrawable(new TextureRegion(Game.easyAssetManager.get("defaultButton_down", Texture.class)));
        style.font = Game.defaultFont;

        /* The restart and main menu button for when the game ends */

        this.restartButton = new TextButton("Restart", style);
        this.mainMenuButton = new TextButton("Main Menu", style);

        this.restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                restart();
            }
        });
        this.mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Game.stage.clear();
                game.setScreen(new MainMenu(game));
            }
        });

        /* The labels for information about the game*/

        String colorType="Colors: Normal", matchType="Matching: Shapes", gameType="Practice";

        if(GameSettings.colorType == GameSettings.ColorType.Random)
            colorType = "Colors: Random";

        if(GameSettings.matchType == GameSettings.MatchType.Color)
            matchType = "Matching: Colors";

        if(GameSettings.gameType == GameSettings.GameType.Fastest)
            gameType = "Mode: Fastest";
        else if(GameSettings.gameType == GameSettings.GameType.Timed)
            gameType = "Mode; TimeAttack";

        Label.LabelStyle labelStyle = new Label.LabelStyle(Game.defaultFont, Color.WHITE);
        Label.LabelStyle titleLabelStyle = new Label.LabelStyle(Game.defaultLargeFont, Color.WHITE);

        this.colorTypeLabel = new Label(colorType, labelStyle);
        this.matchTypeLabel = new Label(matchType, labelStyle);
        this.gameTypeLabel = new Label(gameType, labelStyle);

        Table labelTable = new Table();
        labelTable.left().top();
        labelTable.setFillParent(true);
        Game.stage.addActor(labelTable);

        labelTable.add(colorTypeLabel).left();
        labelTable.row();
        labelTable.add(matchTypeLabel).left();
        labelTable.row();
        labelTable.add(gameTypeLabel).left();

        if(GameSettings.gameType == GameSettings.GameType.Fastest){
            Table otherTable = new Table();
            otherTable.setFillParent(true);
            otherTable.right().top();
            labelStyle = new Label.LabelStyle(Game.defaultFont, Color.WHITE);

            this.avgLabel = new Label("avg-time: 0", labelStyle);
            this.avgLabel.setAlignment(Align.center);
            this.avgLabel.setSize(100, 50);
            otherTable.add(avgLabel);
            otherTable.row();

            this.roundLabel = new Label("0 / "+this.currRound+" / "+this.maxRounds, labelStyle);
            this.roundLabel.setAlignment(Align.center);
            this.roundLabel.setSize(100, 50);
            otherTable.add(roundLabel);

            Game.stage.addActor(otherTable);
        }

        if(GameSettings.gameType == GameSettings.GameType.Timed){
            this.timerLabel = new Label(this.roundTime+"", titleLabelStyle);
            this.timerLabel.setAlignment(Align.center);
            this.timerLabel.setSize(100, 50);
            this.timerLabel.setPosition(Gdx.graphics.getWidth()/2 - 50, Gdx.graphics.getHeight() - 75);
            Game.stage.addActor(this.timerLabel);
        }

        Game.stage.addActor(this.backButton);
    }

    public void restart(){
        this.currRound = 0;
        this.table.clear();
        this.initRound();
    }

    /**
     * Initializes the new round.
     */
    private void initRound(){
        if(GameSettings.gameType == GameSettings.GameType.Fastest){
            if(this.currRound >= this.maxRounds){
                return;
            }
        }

        this.roundTime = this.roundTimeStart - this.roundTimeDecreaseAmount*this.currRound;
        this.winCounter = 0;
        this.currScale = 0;
        this.failedLastRound = false;

        //Shuffle some arrays
        shuffleArray(this.positions);
        if(GameSettings.colorType == GameSettings.ColorType.Random) shuffleArray(this.colorIDs);

        //Make a new list of shapes.
        this.shapeList = new Array<GameShape>();
        for(int i=0;i<GameSettings.numShapes;i++) {
            int index = i * 2;
            this.shapeList.add(new GameShape(this.positions[index], i, this.colorIDs[index]));
            this.shapeList.add(new GameShape(this.positions[index + 1], i, this.colorIDs[index + 1]));
        }

        //Reset some stuff.
        this.dragging = false;
        this.currShape = null;

        this.currGameState = GameState.Starting;
    }

    public void setAvgTime(){
        //Show the avg time including the new one.
        if(this.avg == 0) this.avg = this.endTime - this.startTime;
        else this.avg = (this.avg + (this.endTime - this.startTime))/2;
        this.avgLabel.setText("avg-time: "+this.formatter.format(this.avg/1000));
    }

    @Override
    public void render(float delta) {
        update(delta);

        Game.renderer.begin(ShapeRenderer.ShapeType.Filled);
        this.drawUsingShapeRenderer(Game.renderer);
        Game.renderer.end();

        Game.batch.begin();
        this.draw(Game.batch);
        Game.batch.end();
    }

    private void draw(SpriteBatch batch){
        batch.setColor(Color.WHITE);
        batch.draw(this.topTexture, Game.camera.position.x - Game.camera.viewportWidth/2,
                Game.camera.position.y + Game.camera.viewportHeight/2 - Game.camera.viewportHeight*this.topArea,
                Gdx.graphics.getWidth(), Game.camera.viewportHeight*this.topArea);


        this.drawShapes(batch);
    }

    private void drawShapes(SpriteBatch batch){
        float radius = sizeOfShapes/2;
        TextureRegion region;

        for(GameShape shape : shapeList) {
            batch.setColor(this.shapeColors[shape.getColorID()]);
            if(shape.locked) region = shapesGlow[shape.getShapeType()];
            else region = shapes[shape.getShapeType()];
            batch.draw(region, shape.position.x - radius, shape.position.y - radius, radius, radius, sizeOfShapes, sizeOfShapes, this.currScale, this.currScale, this.currRotation);
        }
    }

    private void drawUsingShapeRenderer(ShapeRenderer shapeRenderer){
        //Draws all the lines
        for (Array<Vector2> list : this.lists) {
            int length = list.size - 1;
            for (int j = 0; j < length; j++) {
                Vector2 curr = list.get(j);
                Vector2 next = list.get(j + 1);
                shapeRenderer.rectLine(curr.x, curr.y, next.x, next.y, 4);
            }
        }

        //this.debugDrawShapeAreas(shapeRenderer);

//        for(GameShape shape : this.shapeList)
//            shapeRenderer.rect(shape.bounds.x, shape.bounds.y, shape.bounds.width, shape.bounds.height);
    }

    private void debugDrawShapeAreas(ShapeRenderer shapeRenderer){
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for(int i=0;i<this.positions.length;i++){
            Vector2 pos = this.positions[i];
            shapeRenderer.rect(pos.x - this.sizeOfSpots/2, pos.y - this.sizeOfSpots/2, this.sizeOfSpots, this.sizeOfSpots);
        }
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    }

    private void update(float delta){
        if(currGameState == GameState.Running){
            if(GameSettings.gameType == GameSettings.GameType.Timed)
                this.updateTimedGame(delta);

            if(winCounter >= GameSettings.numShapes){
                this.winCounter = 0;
                this.setGameOver(false);
            }

        //If starting, spin the shapes in!
        }else if(currGameState == GameState.Starting){
            this.currScale = lerpScale(0, 1, this.currScale, 1500);
            this.currRotation += 20;
            if(this.currScale >= 1) {
                this.currScale = 1;
                this.currRotation = 0;
                this.started();
            }
        //If ending, spin the shapes out!
        }else if(currGameState == GameState.Ending){
            this.currScale = lerpScale(1, 0, this.currScale, 1500);
            this.currRotation += 20;
            if(this.currScale <= 0) {
                this.currScale = 0;
                this.currRotation = 0;
                this.ended();
            }
        }
    }

    private void updateTimedGame(float delta){
        this.roundTime -= delta;
        this.timerLabel.setText(this.formatter.format(this.roundTime)+"");
        if(this.roundTime <= 0){
            this.roundTime = 0;
            this.setGameOver(true);
        }
    }

    private void starting(){

    }

    private void started(){
        this.currGameState = GameState.Running;
        this.startTime = System.currentTimeMillis();
    }

    private void ending(){
        this.initLists();
    }

    private void ended(){
        this.gameOverImage.remove();
        this.currGameState = GameState.Starting;

        //If it was the fastest game type, check if we finished all of our rounds.
        if(GameSettings.gameType == GameSettings.GameType.Fastest){
            if(this.currRound >= this.maxRounds){
                this.currGameState = GameState.Over;

                this.table.add(this.restartButton).size(200, 75);
                this.table.row().padTop(50);
                this.table.add(this.mainMenuButton).size(200, 75);
            }

        //If it was the time game type, game over if we failed once.
        }else if(GameSettings.gameType == GameSettings.GameType.Timed && this.failedLastRound){
            this.currGameState = GameState.Over;

            this.table.add(this.restartButton).size(200, 75);
            this.table.row().padTop(50);
            this.table.add(this.mainMenuButton).size(200, 75);
        }

        if(this.currGameState == GameState.Starting)
            this.initRound();
    }

    public void setGameOver(boolean failed){
        //Set the state to ending and stop dragging.
        this.currGameState = GameState.Ending;
        this.dragging = false;
        this.failedLastRound = failed;
        this.endTime = System.currentTimeMillis();
        this.currRound++;

        //If we passed, do stuff!
        if(!failed) {
            this.gameOverImage = new Image(this.gameOverShapes[0]);
        }else {
            this.gameOverImage = new Image(this.gameOverShapes[1]);
        }

        if(GameSettings.gameType == GameSettings.GameType.Fastest)
            this.gameOverFastest(failed);
        else if(GameSettings.gameType == GameSettings.GameType.Timed)
            this.gameOverTimed(failed);
        else if(GameSettings.gameType == GameSettings.GameType.Practice)
            this.gameOverPractice(failed);

        //Add the game over image.
        this.gameOverImage.setPosition(0, Gdx.graphics.getWidth()/2);
        this.gameOverImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
        Game.stage.addActor(this.gameOverImage);

        this.ending();

    }

    private void gameOverFastest(boolean failed){
        if(!failed){
            this.currScore++;
            this.setAvgTime();
        }

        this.roundLabel.setText(this.currScore+" / "+this.currRound+" / "+this.maxRounds);
    }

    private void gameOverTimed(boolean failed){

    }

    private void gameOverPractice(boolean failed){

    }

    private void initLists(){
        for(int i=0;i<GameSettings.numShapes;i++){
            this.lists[i] = new Array<Vector2>(100);
        }
    }

    private float lerpScale(float startScale, float targetScale, float currScale, float time){
        float incr = Math.abs(startScale - targetScale)/(time/60);
        if(startScale > targetScale) currScale -= incr;
        else currScale += incr;

        return currScale;
    }

    static void shuffleArray(Object[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = MathUtils.random(i);
            // Simple swap
            Object a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.table.remove();
        this.mainMenuButton = null;
        this.restartButton = null;
        this.shapeColors = null;
        this.shapeList = null;
        this.shapes = null;
        this.colorIDs = null;
        this.gameOverShapes = null;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldPos = Game.camera.unproject(new Vector3(screenX, screenY, 0));
        this.currShape = null;

        //Check if we clicked/touched on a shape. If so, record it and start dragging.
        for(GameShape shape : this.shapeList){
            if(!shape.locked && shape.isOver(worldPos.x, worldPos.y)){
                this.currShape = shape;
                this.lists[this.lineCounter].add(new Vector2(shape.position.x, shape.position.y));
                this.dragging = true;
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(this.currShape == null || !this.dragging) return false;

        Vector3 worldPos = Game.camera.unproject(new Vector3(screenX, screenY, 0));
        boolean onShape = false;
        this.dragging = false;

        for(GameShape shape : this.shapeList){
            //If a shape is clicked.
            if(shape.isOver(worldPos.x, worldPos.y)){
                if(shape == this.currShape) break;

                boolean condition = false;
                if(GameSettings.matchType == GameSettings.MatchType.Shapes) condition = shape.getShapeType() == this.currShape.getShapeType();
                else if(GameSettings.matchType == GameSettings.MatchType.Color) condition = shape.getColorID() == this.currShape.getColorID();

                //Correct one? one more victory point
                if(condition) {
                    this.winCounter++;

                    //Lock the shapes.
                    this.currShape.locked = true;
                    shape.locked = true;
                    onShape = true;
                    this.lists[this.lineCounter].add(new Vector2(shape.position.x, shape.position.y));
                    this.lineCounter = (this.lineCounter+1)%GameSettings.numShapes;
                }
                break;
            }
        }

        //If we didn't let go on a shape, restart the list.
        if(!onShape) this.lists[this.lineCounter] = new Array<Vector2>(200);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(this.currShape == null || !this.dragging) return false;
        Array<Vector2> list = this.lists[this.lineCounter];
        if(list.size == 0) return false;

        Vector3 worldPos = Game.camera.unproject(new Vector3(screenX, screenY, 0));

        if(list.get(list.size-1).dst(worldPos.x, worldPos.y) > this.disBetweenPositions){
            list.add(new Vector2(worldPos.x, worldPos.y));
            Game.executor.submit(new CheckCollision(this, this.shapeList, this.lineCounter, this.lists, this.currShape, worldPos.x, worldPos.y, list.get(list.size-2), list.get(list.size-1)));
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
