package com.quickbite.connector2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.quickbite.connector2.Constants;
import com.quickbite.connector2.GH;
import com.quickbite.connector2.Game;
import com.quickbite.connector2.GameData;
import com.quickbite.connector2.GameScreenClickListener;
import com.quickbite.connector2.GameSettings;
import com.quickbite.connector2.GameShape;
import com.quickbite.connector2.GameStats;
import com.quickbite.connector2.ICallback;
import com.quickbite.connector2.SoundManager;
import com.quickbite.connector2.gui.GameScreenGUI;

/**
 * Created by Paha on 1/8/2016.
 * The main game screen. This will handle updating and drawing the game. The GUI will be handled by the GameScreenGUI class.
 */
public class GameScreen implements Screen{
    public enum GameState {Beginning, Starting, Running, Ending, Over, Limbo}
    private GameState currGameState;

    private boolean startedRoundEnd = false, roundStartingFlag = false;

    private TextureRegion topTexture;
    private Game game;

    public Integer[] colorIDs;
    public Array<Vector2> positions;
    public Array<Vector2>[] lineLists;
    public GameShape currShape = null;
    public int lineCounter = 0;

    private Array<Vector2> takenPositions;

    private volatile boolean gameOver = false;

    private final float topArea = 0.1f;

    private GameScreenClickListener clickListener;

    private float counter, challengeCounter = 1f;

    public GameScreen(Game game){
        this.game = game;

        GameShape.gameScreen = this; //Static reference.

        //Handle input stuff.
        InputMultiplexer multi = new InputMultiplexer();
        multi.addProcessor(clickListener = new GameScreenClickListener(this));
        multi.addProcessor(Game.stage);
        Gdx.input.setInputProcessor(multi);

        GameData.gameInit();
        lineLists = new Array[GameSettings.numShapes*2];
    }

    @Override
    public void show() {

        int xSpots = (int)((Game.camera.viewportWidth - GameData.playAreaPadding.getLeft() - GameData.playAreaPadding.getRight())/(GameData.sizeOfSpots));
        int ySpots = (int)((Game.camera.viewportHeight - (Game.camera.viewportHeight*this.topArea) - GameData.playAreaPadding.getBottom())/GameData.sizeOfSpots);
        int numPositions = xSpots*ySpots;

        this.topTexture = new TextureRegion(Game.easyAssetManager.get("Top", Texture.class));

        this.positions = new Array<Vector2>(numPositions);
        if(GameSettings.gameType == GameSettings.GameType.Frenzy) //Only initialize this list if we need it.
            this.takenPositions = new Array<Vector2>(numPositions);

        for(int i=0;i<xSpots;i++){
            for(int j=0;j<ySpots;j++){
                Vector2 vec = new Vector2((float)GameData.playAreaPadding.getLeft() + GameData.sizeOfSpots/2f + (i*GameData.sizeOfSpots),
                        (float)GameData.playAreaPadding.getBottom() + GameData.sizeOfSpots/2 + (j*GameData.sizeOfSpots));
                this.positions.add(vec);
            }
        }

        this.colorIDs = new Integer[GameSettings.numShapes *2];
        for(int i=0;i<this.colorIDs.length;i++)
            this.colorIDs[i] = i/2;


        this.reset();

        GameData.gameShapeList = new Array<GameShape>();
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

        currGameState = GameState.Beginning;
    }

    /**
     * Restarts the current game mode.
     */
    public void restartGame(){
        GameScreenGUI.createStartingGUI(this.game, this);
        this.reset();
    }

    /**
     * Initializes the new round.
     */
    private void initRound(){
        if(GameSettings.gameType == GameSettings.GameType.Fastest)
            if(GameStats.currRound >= GameStats.maxRounds)
                return;

        if(GameSettings.gameType == GameSettings.GameType.Timed) {
            if (GameStats.currRound <= 5)
                GameStats.roundTimeLeft = GameStats.roundTimeStart - GameStats.roundTimeDecreaseAmount * GameStats.currRound;
            else
                GameStats.roundTimeLeft = GameStats.roundTimeStart - GameStats.getRoundTimeDecreaseAmountBelow5 * GameStats.currRound;
        }else if(GameSettings.gameType == GameSettings.GameType.Frenzy){
            GameStats.roundTimeLeft = 20f;
        }

        GameStats.winCounter = 0;
        GameStats.failedLastRound = false;

        //Shuffle some arrays
        this.positions.shuffle();
        if(GameSettings.colorType == GameSettings.ColorType.Random) GH.shuffleArray(this.colorIDs);

        Color[] colors = GameData.colorMap.values().toArray(new Color[GameData.colorMap.size()]);

        if(GameSettings.gameType != GameSettings.GameType.Frenzy) {
            //Make a new list of GameData.shapeTextures.
            for (int i = 0; i < GameSettings.numShapes; i++) {
                int index = i * 2;
                GameData.gameShapeList.add(new GameShape(this.positions.get(index), i, (int) GameData.sizeOfShapes, colors[this.colorIDs[index]]));
                GameData.gameShapeList.add(new GameShape(this.positions.get(index+1), i, (int) GameData.sizeOfShapes, colors[this.colorIDs[index + 1]]));
            }
        }

        //Reset some stuff.
        this.clickListener.dragging = false;
        this.currShape = null;

        this.currGameState = GameState.Starting;
    }

    public void startRecordTime(){
        GameStats.startTime = System.currentTimeMillis();
    }

    /**
     * Records the average and best time of the round. Sets the label if available.
     */
    public void endRecordTime(){
        GameStats.endTime = System.currentTimeMillis();

        double time = GameStats.endTime - GameStats.startTime;
        //Show the avgTime time including the new one.
        if(GameStats.avgTime == 0) GameStats.avgTime = time;
        else GameStats.avgTime = (GameStats.avgTime + time)/2;

        if(GameStats.bestTime == 0 || GameStats.bestTime >= time)
            GameStats.bestTime = time;
    }

    public void shapeClicked(GameShape shape){
        this.currShape = shape;
        this.lineLists[this.lineCounter].add(new Vector2(shape.position.x, shape.position.y));
        SoundManager.playSound("pop", 0.75f);

        if(GameSettings.gameType == GameSettings.GameType.Frenzy)
            startRecordTime();
    }

    /**
     * Call to connect two GameData.shapeTextures.
     * @param shape1 The first shape.
     * @param shape2 The second shape.
     */
    public void shapesConnected(GameShape shape1, GameShape shape2){
        //Lock the GameData.shapeTextures.
        shape1.setLocked(true);
        shape2.setLocked(true);

        if(shape1.getLifetime() <= shape2.getLifetime())
            shape2.setLifeTime(shape1.getLifetime());
        else
            shape1.setLifeTime(shape2.getLifetime());

        //We only need to set this for one of them. We don't want both shapes resetting the line.
        shape1.setLineNumber(lineCounter);

        ParticleEffect effect = GameData.explosionEffectPool.obtain();
        effect.setPosition(shape1.position.x - Game.viewport.getWorldWidth()/2f, shape1.position.y - Game.viewport.getWorldHeight()/2f);
        effect.getEmitters().get(0).getTint().setColors(new float[]{shape1.getColor().r, shape1.getColor().g, shape1.getColor().b});
        effect.getEmitters().get(0).setSprite(new Sprite(new TextureRegion(GameData.shapeTextures[shape1.getShapeType()])));
        effect.start();

        GameData.particleEffects.add(effect);

        ParticleEffect effect2 = GameData.explosionEffectPool.obtain();
        effect2.setPosition(shape2.position.x - Game.viewport.getWorldWidth()/2f, shape2.position.y - Game.viewport.getWorldHeight()/2f);
        effect2.getEmitters().get(0).getTint().setColors(new float[]{shape2.getColor().r, shape2.getColor().g, shape2.getColor().b});
        effect2.getEmitters().get(0).setSprite(new Sprite(new TextureRegion(GameData.shapeTextures[shape2.getShapeType()])));
        effect2.start();

        GameData.particleEffects.add(effect2);

        this.lineCounter = (this.lineCounter+1)%this.lineLists.length;

        if(GameSettings.gameType == GameSettings.GameType.Frenzy)
            GameStats.successfulRounds++;

        SoundManager.playSound("pop", 0.75f);
        SoundManager.playSound("waterdrop");

        if(GameSettings.gameType == GameSettings.GameType.Frenzy)
            endRecordTime();
    }

    private Vector2 getRandomPositionAndShuffle(){
        Vector2 position = this.positions.pop();
        this.positions.shuffle();
        return position;
    }

    @Override
    public void render(float delta) {
        update(delta);

        Game.batch.begin();
        GameScreenGUI.centerLabel.draw(Game.batch, 1f);
        Game.batch.end();

        Game.renderer.begin(ShapeRenderer.ShapeType.Filled);
        this.drawLines(Game.renderer);
//        this.debugDrawGrid(Game.renderer);
//        this.debugDrawShapes(Game.renderer);
        Game.renderer.end();

        Game.batch.begin();
        this.draw(Game.batch);
        Game.batch.end();
    }

    private void draw(SpriteBatch batch){
        batch.setColor(Color.WHITE);
        batch.draw(this.topTexture, Game.camera.position.x - Game.viewport.getWorldWidth()/2,
                Game.camera.position.y + Game.viewport.getWorldHeight()/2 - Game.viewport.getWorldHeight()*this.topArea,
                Gdx.graphics.getWidth(), Game.viewport.getWorldHeight()*this.topArea);


        this.drawParticles(batch, Gdx.graphics.getDeltaTime());
        this.drawShapes(batch);
    }

    /**
     * Draws active particles
     * @param batch The spritebatch to use.
     * @param delta The time between frames.
     */
    private void drawParticles(SpriteBatch batch, float delta){
        for(int i=GameData.particleEffects.size-1; i >= 0; i--){
            GameData.particleEffects.get(i).draw(batch, delta);
            if(GameData.particleEffects.get(i).isComplete())
                GameData.particleEffects.removeIndex(i);
        }
    }

    /**
     * Draws the game GameData.shapeTextures.
     * @param batch The spritebatch to use.
     */
    private void drawShapes(SpriteBatch batch){
        for(int i = GameData.gameShapeList.size-1; i >= 0; i--) {
            GameShape shape = GameData.gameShapeList.get(i);
            if(shape.isDead()) GameData.gameShapeList.removeIndex(i);
            else shape.render(batch, Gdx.graphics.getDeltaTime());
        }
    }

    /**
     * Draws lines on the screen that the player created
     * @param shapeRenderer The shape renderer to use.
     */
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

//        for(GameShape shape : GameData.gameShapeList)
//            shapeRenderer.rect(shape.bounds.x, shape.bounds.y, shape.bounds.width, shape.bounds.height);
    }

    /**
     * Draws the grid for debugging.
     * @param shapeRenderer The shape renderer to use
     */
    private void debugDrawGrid(ShapeRenderer shapeRenderer){
        shapeRenderer.end();
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for(int i=0;i<this.positions.size;i++){
            Vector2 pos = this.positions.get(i);
            shapeRenderer.rect(pos.x - GameData.sizeOfSpots/2, pos.y - GameData.sizeOfSpots/2, GameData.sizeOfSpots, GameData.sizeOfSpots);
        }
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    }

    /**
     * Draws the shape click areas (not the actual GameData.shapeTextures themselves) for debugging.
     * @param shapeRenderer The shape renderer to use.
     */
    private void debugDrawShapes(ShapeRenderer shapeRenderer){
        shapeRenderer.end();
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for(int i = 0; i<GameData.gameShapeList.size; i++){
            GameShape shape = GameData.gameShapeList.get(i);
            shapeRenderer.rect(shape.position.x - GameData.sizeOfSpots/2, shape.position.y - GameData.sizeOfSpots/2, GameData.sizeOfSpots, GameData.sizeOfSpots);
        }
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    }

    /**
     * General update. Can be used for stuff that is common to all game types.
     * @param delta The time between frames.
     */
    private void update(float delta){
        if(currGameState == GameState.Beginning) {

        }else if(currGameState == GameState.Running){
            //Frenzy is special
            if(GameSettings.gameType != GameSettings.GameType.Frenzy) {
                if(GameSettings.gameType == GameSettings.GameType.Timed)
                    this.updateTimedGame(delta);

                if (GameStats.winCounter >= GameSettings.numShapes) {
                    GameStats.winCounter = 0;
                    this.setRoundOver(false, GameStats.RoundOver.Won);
                }
            }else{
                this.updateFrenzy(delta);
            }

            GameScreenGUI.update(delta);
        //If roundStarting, spin the GameData.shapeTextures in!
        }else if(currGameState == GameState.Starting){
            if(!roundStartingFlag)
                roundStarting();
            counter+=delta;
            if(counter >= 0.5) {
                counter = 0;
                this.roundStarted();
            }
        //If roundEnding, spin the GameData.shapeTextures out!
        }else if(currGameState == GameState.Ending){
            if(!startedRoundEnd)
                this.startRoundEnd();
            counter+=delta;
            if(counter >= 0.5) {
                counter = 0;
                this.roundEnded();
            }
        }else if(this.currGameState == GameState.Over){
            this.gameOver();
            this.currGameState = GameState.Limbo;
        }
    }

    /**
     * The update specifics for a timed game.
     * @param delta The time between frames.
     */
    private void updateTimedGame(float delta){
        GameStats.roundTimeLeft -= delta;

        if(GameStats.roundTimeLeft <= 0){
            GameStats.roundTimeLeft = 0;
            this.setRoundOver(true, GameStats.RoundOver.OutOfTime);
        }
    }

    /**
     * Update function for 'best' game mode specifics.
     * @param delta The time between frames.
     */
    private void updateBestGame(float delta){

    }

    /**
     * Update function for 'practice' game mode specifics.
     * @param delta The time between frames.
     */
    private void updatePractice(float delta){

    }

    private void updateFrenzy(float delta){
        challengeCounter+=delta;
        if(challengeCounter > 0.75f){
            int randShape = MathUtils.random(0, GameData.shapeTextures.length-1);
            Color randColor = (Color)GameData.colorMap.values().toArray()[MathUtils.random(0, GameData.colorMap.size()-1)];
            final Vector2 position = getRandomPositionAndShuffle();
            takenPositions.add(position);
            GameData.gameShapeList.add(new GameShape(position, randShape, (int) GameData.sizeOfShapes, randColor, 5f, new ICallback() {
                @Override
                public void run() {
//                    Sound sound = Game.easyAssetManager.get("whoosh_out", Sound.class);
//                    sound.play(0.7f, 2f, 0f);
                }
            }, new ICallback() {
                @Override
                public void run() {
                    takenPositions.removeValue(position, true);
                    positions.add(position);
                }
            }));
//            Sound sound = Game.easyAssetManager.get("whoosh_in", Sound.class);
//            sound.play(0.7f, 2f, 0f);
            this.challengeCounter = 0f;
        }

        GameStats.roundTimeLeft -= delta;

        if(GameStats.roundTimeLeft <= 0){
            GameStats.roundTimeLeft = 0;
            this.setRoundOver(true, GameStats.RoundOver.OutOfTime);
        }
    }

    public void beginGame(){
        currGameState = GameState.Starting;

        //TODO Temp fix for now to bypass starting delay. Maybe a better way to handle this?
        if(GameSettings.gameType == GameSettings.GameType.Frenzy)
            counter = 99999999;

        GameScreenGUI.showGameGUI();
    }

    /**
     * Called when a round begins to start.
     */
    private void roundStarting(){
        roundStartingFlag = true;
        this.initRound();
    }

    /**
     * Called when a round has fully started.
     */
    private void roundStarted(){
        this.currGameState = GameState.Running;

        if(GameSettings.gameType != GameSettings.GameType.Frenzy)
            this.startRecordTime();

        for (GameShape gameShape : GameData.gameShapeList) {
            gameShape.setStarted();
        }

        roundStartingFlag = false;
    }

    private void roundEnding(){
        this.initLineLists();
    }

    private void startRoundEnd(){
        if(!GameStats.failedLastRound) {
            if(GameSettings.gameType != GameSettings.GameType.Frenzy)
                this.endRecordTime();

            GameStats.successfulRounds++;
            SoundManager.playSound("success");
        }else{
            SoundManager.playSound("error");
        }

        for(GameShape shape : GameData.gameShapeList)
            shape.setEnding();

        this.startedRoundEnd = true;

        GameScreenGUI.roundOverGUI();

        if(GameSettings.gameType == GameSettings.GameType.Fastest)
            this.roundOverFastest(GameStats.failedLastRound);
        else if(GameSettings.gameType == GameSettings.GameType.Timed)
            this.roundOverTimed(GameStats.failedLastRound);
        else if(GameSettings.gameType == GameSettings.GameType.Practice)
            this.roundOverPractice(GameStats.failedLastRound);

        this.roundEnding();
    }

    /**
     * Called when the round should be ended.
     */
    private void roundEnded(){
        GameStats.currRound++; //This needs to be called before init() round below.

        GameData.gameShapeList = new Array<GameShape>();
        GameScreenGUI.roundEndedGUI();
        this.currGameState = GameState.Starting; //By default, set it to roundStarting the round again.

        //If it was the fastest game type, check if we finished all of our rounds.
        if(GameSettings.gameType == GameSettings.GameType.Fastest){
            this.roundEndedBest();

        //If it was the time game type, game over if we failed once.
        }else if(GameSettings.gameType == GameSettings.GameType.Timed && GameStats.failedLastRound){
            this.roundEndedTimed();

        }else if(GameSettings.gameType == GameSettings.GameType.Frenzy){
            roundEndedChallenge();
        }

        this.startedRoundEnd = false;
    }

    /**
     * Called when the round should be ended.
     * @param failed True if we failed the round, false otherwise...
     */
    public synchronized void setRoundOver(boolean failed, GameStats.RoundOver roundOverReason){
        GameStats.roundOverReason = roundOverReason;
        GameStats.failedLastRound = failed;

        //Set the state to roundEnding and stop dragging.
        this.currGameState = GameState.Ending;
        this.clickListener.dragging = false;
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

    private void roundEndedChallenge(){
        this.currGameState = GameState.Over;
    }

    /**
     * General game over.
     */
    private void gameOver(){
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
            case Frenzy:
                gameOverChallenge();
                break;
        }

        GameScreenGUI.gameOverGUI();
    }

    /**
     * Timed game over.
     */
    private void gameOverTimed(){
        this.calcScore();
    }

    /**
     * Best game over.
     */
    private void gameOverBest(){
        this.calcScore();
    }

    /**
     * Practice game over.
     */
    private void gameOverPractice(){

    }

    /**
     * Practice game over.
     */
    private void gameOverChallenge(){
        this.calcScore();
    }

    private void calcScore(){
        String leaderboard = "";
        //If the game type is best out of 10
        if(GameSettings.gameType == GameSettings.GameType.Fastest) {
            if (GameStats.avgTime == 0) GameStats.currScore = 0;
            else
                GameStats.currScore = (int) ((3 * GameStats.successfulRounds) * (20f / (GameStats.avgTime / 1000)) * (GameSettings.numShapes * 3));

            leaderboard = Constants.LEADERBOARD_BEST;

        //If the game type is time attack
        }else if(GameSettings.gameType == GameSettings.GameType.Timed){
            if(GameStats.avgTime == 0) GameStats.currScore = 0;
            else GameStats.currScore = (int)((2*(GameStats.currRound-1)) * (20f/(GameStats.avgTime/1000)) * (GameSettings.numShapes*3));

            leaderboard = Constants.LEADERBOARD_TIMED;

            //If the game type is challenge.
        }else if(GameSettings.gameType == GameSettings.GameType.Frenzy){
//            if(GameStats.avgTime == 0) GameStats.currScore = 0;
            GameStats.currScore = (int)(125*(GameStats.successfulRounds));

            leaderboard = Constants.LEADERBOARD_FRENZY;
        }

        saveScore(leaderboard, GameSettings.gameType, GameStats.currScore);
    }

    /**
     * Saves the score.
     * @param leaderboard The leaderboard ID to use and save to.
     * @param type The type of game being played
     * @param score The score
     */
    private void saveScore(String leaderboard, GameSettings.GameType type, int score){
        //Save it to the prefs and local
        GameData.prefs.putInteger(leaderboard, score);
        GameData.prefs.flush();
        if(GameData.scoreMap.get(type, 0) < score)
            GameData.scoreMap.put(type, score);

        //Save to the leaderboard if connected
        Game.resolver.submitScoreGPGS(leaderboard, score);
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
        for(int i=0;i<lineLists.length;i++){
            this.lineLists[i] = new Array<Vector2>(100);
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
        GameData.reset();
        this.colorIDs = null;
        GameScreenGUI.reset();
    }


}
