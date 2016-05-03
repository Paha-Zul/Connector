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
import com.quickbite.connector2.gui.GameScreenGUI;

/**
 * Created by Paha on 1/8/2016.
 */
public class GameScreen implements Screen{
    public enum GameState {Beginning, Starting, Running, Ending, Over, Limbo}
    private GameState currGameState;

    private TextureRegion topTexture;
    private Game game;

    public Integer[] colorIDs;
    public Vector2[] positions;
    public TextureRegion[] shapes;
    public TextureRegion[] shapesGlow;
    public Color[] shapeColors;
    public Array<GameShape> shapeList;
    public Array<Vector2>[] lineLists;
    public GameShape currShape = null;
    public int lineCounter = 0;

    private volatile boolean gameOver = false;
    private float currScale = 0, currRotation = 0;

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

        lineLists = new Array[GameSettings.numShapes];

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

        this.positions = new Vector2[num];
        //We should probably go from 0-480 and 0-800
        for(int i=0;i<num;i++){
            Vector2 vec = new Vector2(sizeOfSpots/2 + ((i*sizeOfSpots)%(sizeOfSpots*(xSpots))), sizeOfSpots/2 + ((i/xSpots)*sizeOfSpots));
            this.positions[i] = vec;
        }

        this.colorIDs = new Integer[GameSettings.numShapes *2];
        for(int i=0;i<this.colorIDs.length;i++)
            this.colorIDs[i] = i/2;

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


        this.reset();

        this.shapeList = new Array<GameShape>();
        this.initLineLists();

        this.currGameState = GameState.Beginning;

        GameScreenGUI.initGameScreenGUI(this.game, this);
    }

    public void reset(){
        GameStats.currRound = GameStats.successfulRounds = 0;
        GameStats.avgTime = 0;
        GameStats.bestTime = 0;
        GameStats.currScore = 0;
        GameStats.successfulRounds = 0;
        GameStats.roundTimeLeft = GameStats.roundTimeStart;
    }

    /**
     * Restarts the current game mode.
     */
    public void restartGame(){
        this.reset();
        this.initRound();
    }

    /**
     * Initializes the new round.
     */
    private void initRound(){
        if(GameSettings.gameType == GameSettings.GameType.Fastest)
            if(GameStats.currRound >= GameStats.maxRounds)
                return;

        GameStats.roundTimeLeft = GameStats.roundTimeStart - GameStats.roundTimeDecreaseAmount*GameStats.currRound;
        GameStats.winCounter = 0;
        GameStats.failedLastRound = false;
        this.currScale = 0;

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
        GameStats.endTime = System.currentTimeMillis();

        double time = GameStats.endTime - GameStats.startTime;
        //Show the avgTime time including the new one.
        if(GameStats.avgTime == 0) GameStats.avgTime = time;
        else GameStats.avgTime = (GameStats.avgTime + time)/2;

        if(GameStats.bestTime == 0 || GameStats.bestTime >= time)
            GameStats.bestTime = time;
    }

    @Override
    public void render(float delta) {
        update(delta);

        Game.renderer.begin(ShapeRenderer.ShapeType.Filled);
        this.drawLines(Game.renderer);
        this.debugDrawGrid(Game.renderer);
        this.debugDrawShapes(Game.renderer);
        Game.renderer.end();

        Game.batch.begin();
        this.draw(Game.batch);
        Game.batch.end();
    }

    private void draw(SpriteBatch batch){
        batch.setColor(Color.WHITE);
        batch.draw(this.topTexture, Game.camera.position.x - Game.viewport.getScreenWidth()/2,
                Game.camera.position.y + Game.viewport.getScreenHeight()/2 - Game.viewport.getScreenHeight()*this.topArea,
                Gdx.graphics.getWidth(), Game.viewport.getScreenHeight()*this.topArea);


        this.drawShapes(batch);
    }

    private void drawShapes(SpriteBatch batch){
        float radius = sizeOfShapes/2;
        TextureRegion region;

        for(GameShape shape : shapeList) {
            batch.setColor(this.shapeColors[shape.getColorID()]);
            if(shape.locked) region = shapesGlow[shape.getShapeType()];
            else region = shapes[shape.getShapeType()];
            batch.draw(region, -Game.viewport.getScreenWidth()/2f + shape.position.x - radius, -Game.viewport.getScreenHeight()/2f + shape.position.y - radius,
                    radius, radius, sizeOfShapes, sizeOfShapes, this.currScale, this.currScale, this.currRotation);
        }
    }

    private void drawLines(ShapeRenderer shapeRenderer){
        //Draws all the lines
        for (Array<Vector2> list : this.lineLists) {
            int length = list.size - 1;
            for (int j = 0; j < length; j++) {
                Vector2 curr = list.get(j);
                Vector2 next = list.get(j + 1);
                shapeRenderer.rectLine(curr.x, curr.y, next.x, next.y, 4);
            }
        }

        //this.debugDrawGrid(shapeRenderer);

//        for(GameShape shape : this.shapeList)
//            shapeRenderer.rect(shape.bounds.x, shape.bounds.y, shape.bounds.width, shape.bounds.height);
    }

    private void debugDrawGrid(ShapeRenderer shapeRenderer){
        shapeRenderer.end();
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for(int i=0;i<this.positions.length;i++){
            Vector2 pos = this.positions[i];
            shapeRenderer.rect(pos.x - this.sizeOfSpots/2, pos.y - this.sizeOfSpots/2, this.sizeOfSpots, this.sizeOfSpots);
        }
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    }

    private void debugDrawShapes(ShapeRenderer shapeRenderer){
        shapeRenderer.end();
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for(int i=0;i<this.shapeList.size;i++){
            GameShape shape = this.shapeList.get(i);
            shapeRenderer.rect(shape.position.x - this.sizeOfSpots/2, shape.position.y - this.sizeOfSpots/2, this.sizeOfSpots, this.sizeOfSpots);
        }
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    }

    private void update(float delta){

        if(currGameState == GameState.Beginning) {
            if(GameScreenGUI.showStartingScreen(delta)) {
                currGameState = GameState.Starting;
                initRound();
            }

        }else if(currGameState == GameState.Running){
            if(GameSettings.gameType == GameSettings.GameType.Timed)
                this.updateTimedGame(delta);

            if(GameStats.winCounter >= GameSettings.numShapes){
                GameStats.winCounter = 0;
                this.setRoundOver(false, GameStats.RoundOver.Won);
            }

            GameScreenGUI.update(delta);
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
        GameStats.roundTimeLeft -= delta;

        if(GameStats.roundTimeLeft <= 0){
            GameStats.roundTimeLeft = 0;
            this.setRoundOver(true, GameStats.RoundOver.OutOfTime);
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
        GameStats.startTime = System.currentTimeMillis();
    }

    private void roundEnding(){
        this.initLineLists();
    }

    /**
     * Called when the round should be ended.
     */
    private void roundEnded(){
        this.shapeList = new Array<GameShape>();
        GameScreenGUI.roundEndedGUI();
        this.currGameState = GameState.Starting; //By default, set it to starting the round again.

        //If it was the fastest game type, check if we finished all of our rounds.
        if(GameSettings.gameType == GameSettings.GameType.Fastest){
            this.roundEndedBest();

        //If it was the time game type, game over if we failed once.
        }else if(GameSettings.gameType == GameSettings.GameType.Timed && GameStats.failedLastRound){
            this.roundEndedTimed();
        }

        //If we are still starting, init the round.
        if(this.currGameState == GameState.Starting)
            this.initRound();

        GameStats.currRound++;
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
        if(GameStats.currRound >= GameStats.maxRounds){
            this.currGameState = GameState.Over;
        }
    }

    /**
     * General game over.
     */
    private void gameOver(){
        String gameOverReason = "You Won!";
        if(GameStats.roundOverReason == GameStats.RoundOver.HitShape) gameOverReason = "Hit Wrong Shape!";
        else if(GameStats.roundOverReason == GameStats.RoundOver.HitLine) gameOverReason = "Hit Another Line!";
        else if(GameStats.roundOverReason == GameStats.RoundOver.OutOfTime) gameOverReason = "Out Of Time!";

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

        GameScreenGUI.gameOverGUI();
    }

    /**
     * Timed game over.
     */
    private void gameOverTimed(){
        GameScreenGUI.gameOverTimedGUI(this);
        if(GameStats.avgTime == 0) GameStats.currScore = 0;
        else GameStats.currScore = (int)((4*GameStats.currRound) *(1f/(GameStats.avgTime/1000))*(GameSettings.numShapes*4));
        Game.resolver.submitScoreGPGS(Constants.LEADERBOARD_TIMED, GameStats.currScore);
    }

    /**
     * Best game over.
     */
    private void gameOverBest(){
        GameScreenGUI.gameOverBestGUI();
        if(GameStats.avgTime == 0) GameStats.currScore = 0;
        else GameStats.currScore = (int)((4*GameStats.successfulRounds) *(1f/(GameStats.avgTime/1000))*(GameSettings.numShapes*4));
        Game.resolver.submitScoreGPGS(Constants.LEADERBOARD_BEST, GameStats.currScore);
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
    public void setRoundOver(boolean failed, GameStats.RoundOver roundOverReason){
        GameStats.roundOverReason = roundOverReason;

        //Set the state to roundEnding and stop dragging.
        this.currGameState = GameState.Ending;
        this.clickListener.dragging = false;
        GameStats.failedLastRound = failed;

        if(!failed) {
            this.recordTime();
            GameStats.successfulRounds++;
        }

        GameScreenGUI.roundOverGUI();

        if(GameSettings.gameType == GameSettings.GameType.Fastest)
            this.roundOverFastest(failed);
        else if(GameSettings.gameType == GameSettings.GameType.Timed)
            this.roundOverTimed(failed);
        else if(GameSettings.gameType == GameSettings.GameType.Practice)
            this.roundOverPractice(failed);

        this.roundEnding();
    }

    private void roundOverFastest(boolean failed){

    }

    private void roundOverTimed(boolean failed){

    }

    private void roundOverPractice(boolean failed){

    }

    /**
     * Creates new shape lineLists.
     */
    private void initLineLists(){
        for(int i=0;i<GameSettings.numShapes;i++){
            this.lineLists[i] = new Array<Vector2>(100);
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
        GameScreenGUI.reset();
    }


}
