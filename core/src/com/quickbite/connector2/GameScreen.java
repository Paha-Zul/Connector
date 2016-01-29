package com.quickbite.connector2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.text.DecimalFormat;

/**
 * Created by Paha on 1/8/2016.
 */
public class GameScreen implements Screen{
    public enum GameState {Beginning, Starting, Running, Ending, Over, Limbo}
    public enum RoundOver {HitShape, HitLine, OutOfTime, Won}

    private GameState currGameState;
    private RoundOver roundOverReason;

    private TextureRegion topTexture;

    private Game game;

    public Integer[] colorIDs;
    public Vector2[] positions;
    public TextureRegion[] shapes;
    public TextureRegion[] shapesGlow;
    public TextureRegion[] gameOverShapes;
    public Color[] shapeColors;
    public Array<GameShape> shapeList;
    public Array<Vector2>[] lists;
    public GameShape currShape = null;
    public int lineCounter = 0;
    public int winCounter = 0;

    public int currRound, maxRounds=10, successfulRounds, currScore;
    private volatile boolean gameOver = false;
    private float currScale = 0, currRotation = 0;
    private double startTime, endTime, avgTime, bestTime;
    private boolean failedLastRound = false;
    private DecimalFormat formatter = new DecimalFormat("0.00");

    private float roundTime, roundTimeDecreaseAmount = 1, roundTimeStart = 10;

    private final float topArea = 0.1f;
    private float sizeOfSpots = 480/5, sizeOfShapes = 480/6;

    private GameScreenClickListener clickListener;

    public GameScreen(Game game){
        this.game = game;

        //Handle input stuff.
        InputMultiplexer multi = new InputMultiplexer();
        multi.addProcessor(clickListener = new GameScreenClickListener(this));
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
        this.currRound = this.successfulRounds = 0;
        this.avgTime = 0;
        this.currScore = 0;

        if(GameSettings.gameType == GameSettings.GameType.Timed)
            this.roundTime = this.roundTimeStart;

        this.shapeList = new Array<GameShape>();
        this.initLists();

        GUIManager.GameScreenGUI.inst().makeGUI(this.game, this);

        this.currGameState = GameState.Beginning;
    }

    /**
     * Restarts the current game mode.
     */
    public void restart(){
        GUIManager.GameScreenGUI.inst().mainTable.clear();
        this.currRound = 0;
        this.avgTime = 0;
        this.bestTime = 0;
        this.currScore = 0;

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
        for(int i=0;i<GameSettings.numShapes;i++) {
            int index = i * 2;
            this.shapeList.add(new GameShape(this.positions[index], i, this.colorIDs[index]));
            this.shapeList.add(new GameShape(this.positions[index + 1], i, this.colorIDs[index + 1]));
        }

        //Reset some stuff.
        this.clickListener.dragging = false;
        this.currShape = null;

        this.currGameState = GameState.Starting;
    }

    /**
     * Records the average and best time of the round. Sets the label if available.
     */
    public void recordTime(){
        this.endTime = System.currentTimeMillis();

        double time = this.endTime - this.startTime;
        //Show the avgTime time including the new one.
        if(this.avgTime == 0) this.avgTime = time;
        else this.avgTime = (this.avgTime + time)/2;

        if(this.bestTime == 0 || this.bestTime >= time)
            this.bestTime = time;

        GUIManager.GameScreenGUI.inst().setAvgTimeLabelText("avgTime-time: "+this.formatter.format(this.avgTime /1000));
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

        if(currGameState == GameState.Beginning) {
            if(GUIManager.GameScreenGUI.inst().showStartingScreen(delta)) {
                currGameState = GameState.Starting;
                initRound();
            }

        }else if(currGameState == GameState.Running){
            if(GameSettings.gameType == GameSettings.GameType.Timed)
                this.updateTimedGame(delta);

            if(winCounter >= GameSettings.numShapes){
                this.winCounter = 0;
                this.setRoundOver(false, RoundOver.Won);
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
        //If roundEnding, spin the shapes out!
        }else if(currGameState == GameState.Ending){
            this.currScale = lerpScale(1, 0, this.currScale, 1500);
            this.currRotation += 20;
            if(this.currScale <= 0) {
                this.currScale = 0;
                this.currRotation = 0;
                this.roundEnded();
            }
        }else if(this.currGameState == GameState.Over){
            this.gameOver();
            this.currGameState = GameState.Limbo;
        }
    }

    private void updateTimedGame(float delta){
        this.roundTime -= delta;
        GUIManager.GameScreenGUI.inst().timerLabel.setText(this.formatter.format(this.roundTime)+"");
        if(this.roundTime <= 0){
            this.roundTime = 0;
            this.setRoundOver(true, RoundOver.OutOfTime);
        }
    }

    private void updateBestGame(float delta){

    }

    private void updatePractice(float delta){

    }

    private void starting(){

    }

    private void started(){
        this.currGameState = GameState.Running;
        this.startTime = System.currentTimeMillis();
    }

    private void roundEnding(){
        this.initLists();
    }

    /**
     * Called when the round should be ended.
     */
    private void roundEnded(){
        this.shapeList = new Array<GameShape>();
        GUIManager.GameScreenGUI.inst().roundEndedGUI();
        this.currGameState = GameState.Starting; //By default, set it to starting the round again.

        //If it was the fastest game type, check if we finished all of our rounds.
        if(GameSettings.gameType == GameSettings.GameType.Fastest){
            this.roundEndedBest();

        //If it was the time game type, game over if we failed once.
        }else if(GameSettings.gameType == GameSettings.GameType.Timed && this.failedLastRound){
            this.roundEndedTimed();
        }

        //If we are still starting, init the round.
        if(this.currGameState == GameState.Starting)
            this.initRound();

        this.currRound++;
    }

    /**
     * specific Timed round ending.
     */
    private void roundEndedTimed(){
        this.currGameState = GameState.Over;
    }

    /**
     * Specific Best round ending.
     */
    private void roundEndedBest(){
        if(this.currRound >= this.maxRounds){
            this.currGameState = GameState.Over;
        }
    }

    /**
     * General game over.
     */
    private void gameOver(){
        GUIManager.GameScreenGUI.inst().setBestTimeLabelText("Best Time: "+this.formatter.format(this.bestTime/1000));
        GUIManager.GameScreenGUI.inst().setGameOverAvgTimeLabelText("Average Time: "+this.formatter.format(this.avgTime /1000));

        String gameOverReason = "You Won!";
        if(this.roundOverReason == RoundOver.HitShape) gameOverReason = "Hit Wrong Shape!";
        else if(this.roundOverReason == RoundOver.HitLine) gameOverReason = "Hit Another Line!";
        else if(this.roundOverReason == RoundOver.OutOfTime) gameOverReason = "Out Of Time!";

        GUIManager.GameScreenGUI.inst().setLostReasonLabelText(gameOverReason);

        switch(GameSettings.gameType){
            case Timed:
                gameOverTimed();
                break;
            case Fastest:
                gameOverBest();
                break;
            case Practice:
                gameOverPractice();
                break;
        }

        GUIManager.GameScreenGUI.inst().gameOverGUI();
    }

    /**
     * Timed game over.
     */
    private void gameOverTimed(){
        GUIManager.GameScreenGUI.inst().gameOverTimedGUI(this);
        this.currScore = (int)(successfulRounds *(1/avgTime)*GameSettings.numShapes);
        Game.resolver.submitScoreGPGS(Constants.LEADERBOARD_TIMED, this.currScore);
    }

    /**
     * Best game over.
     */
    private void gameOverBest(){
        GUIManager.GameScreenGUI.inst().gameOverBestGUI();
        this.currScore = (int)(successfulRounds *(1/avgTime)*GameSettings.numShapes);
        Game.resolver.submitScoreGPGS(Constants.LEADERBOARD_BEST, this.currScore);
    }

    /**
     * Practice game over.
     */
    private void gameOverPractice(){

    }

    /**
     * Called when the round should be ended.
     * @param failed True if we failed the round, false otherwise...
     */
    public void setRoundOver(boolean failed, RoundOver roundOverReason){
        this.roundOverReason = roundOverReason;

        //Set the state to roundEnding and stop dragging.
        this.currGameState = GameState.Ending;
        this.clickListener.dragging = false;
        this.failedLastRound = failed;

        TextureRegion gameOverImage;

        //If we passed, do stuff!
        if(!failed) {
            gameOverImage = this.gameOverShapes[0];
        }else {
            gameOverImage = this.gameOverShapes[1];
        }

        if(!failed) this.recordTime();
        GUIManager.GameScreenGUI.inst().roundOverGUI(gameOverImage);

        if(GameSettings.gameType == GameSettings.GameType.Fastest)
            this.roundOverFastest(failed);
        else if(GameSettings.gameType == GameSettings.GameType.Timed)
            this.roundOverBest(failed);
        else if(GameSettings.gameType == GameSettings.GameType.Practice)
            this.roundOverPractice(failed);

        this.roundEnding();
    }

    private void roundOverFastest(boolean failed){
        if(!failed){
            this.successfulRounds++;
        }

        GUIManager.GameScreenGUI.inst().roundLabel.setText(this.successfulRounds +" / "+this.currRound+" / "+this.maxRounds);
    }

    private void roundOverBest(boolean failed){

    }

    private void roundOverPractice(boolean failed){

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
        this.shapeColors = null;
        this.shapeList = null;
        this.shapes = null;
        this.colorIDs = null;
        this.gameOverShapes = null;
        GUIManager.GameScreenGUI.inst().reset();
    }


}
