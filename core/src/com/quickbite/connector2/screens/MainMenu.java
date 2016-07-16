package com.quickbite.connector2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.quickbite.connector2.GH;
import com.quickbite.connector2.Game;
import com.quickbite.connector2.GameData;
import com.quickbite.connector2.GameSettings;
import com.quickbite.connector2.GameShape;
import com.quickbite.connector2.ICallback;
import com.quickbite.connector2.gui.MainMenuGUI;

/**
 * Created by Paha on 1/8/2016.
 */
public class MainMenu implements Screen {
    private Game game;

    private double nextTick, interval = 500; //in millis

    public MainMenu(Game game){
        this.game = game;
    }

    @Override
    public void show() {
        MainMenuGUI.makeGUI(game, this);
        GameData.reset();

        setupPurchases();

//        testScores();

        //We put this here to reset the input processor from the GameScreen
        // when we come back to the main menu.
        Gdx.input.setInputProcessor(Game.stage);

        Game.adInterface.showAdmobBannerAd();

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                final GameShape shape1 = new GameShape(new Vector2(MathUtils.random(50f, 430f), MathUtils.random(50f, 750f)), MathUtils.random(5), 100, GameData.getRandomColor(), 1f);
                shape1.setScaleSpeed(0.75f);
                shape1.setOpacity(0.5f);

                shape1.onEndingCallback = new ICallback() {
                    @Override
                    public void run() {
                        ParticleEffect effect = GameData.explosionEffectPool.obtain();
                        effect.setPosition(shape1.position.x - Game.viewport.getWorldWidth()/2f, shape1.position.y - Game.viewport.getWorldHeight()/2f);
                        effect.getEmitters().get(0).getTint().setColors(new float[]{shape1.getColor().r, shape1.getColor().g, shape1.getColor().b});
                        effect.getEmitters().get(0).getTransparency().setHigh(0f, 0.5f);
                        effect.getEmitters().get(0).setSprite(new Sprite(new TextureRegion(GameData.shapeTextures[shape1.getShapeType()])));
                        effect.start();

                        GameData.particleEffects.add(effect);
                    }
                };

                GameData.gameShapeList.add(shape1);
            }
        }, 0f, 1f);
    }

    private void setupPurchases(){
//        PurchaseSystem.onAppRestarted();
//
//        if (PurchaseSystem.hasManager()) {
//
//            // purchase system is ready to start. Let's initialize our product list etc...
//            PurchaseManagerConfig config = new PurchaseManagerConfig();
//            config.addOffer(new Offer());
////            config.addOffer()
////            config.addOffer()
//            config.addStoreParam(PurchaseManagerConfig.STORE_NAME_ANDROID_GOOGLE, "<Google key>");
////            config.addStoreParam(PurchaseManagerConfig.STORE_NAME_ANDROID_OUYA, new Object[] {
////                    OUYA_DEVELOPER_ID,
////                    KEYPATH
////            });
//
//
//
//            // let's start the purchase system...
//            PurchaseSystem.install(new PurchaseObserver() {
//                @Override
//                public void handleInstall() {
//
//                }
//
//                @Override
//                public void handleInstallError(Throwable e) {
//
//                }
//
//                @Override
//                public void handleRestore(Transaction[] transactions) {
//
//                }
//
//                @Override
//                public void handleRestoreError(Throwable e) {
//
//                }
//
//                @Override
//                public void handlePurchase(Transaction transaction) {
//
//                }
//
//                @Override
//                public void handlePurchaseError(Throwable e) {
//
//                }
//
//                @Override
//                public void handlePurchaseCanceled() {
//
//                }
//            }, config);
//
//            // to make a purchase (results are reported to the observer)
//            PurchaseSystem.purchase("product_identifier");
//
//            // (*) to restore existing purchases (results are reported to the observer)
////            PurchaseSystem.purchaseRestore();
//
//            // obtain localized product information (not supported by all platforms)
////            Information information = PurchaseSystem.getInformation("product_identifier");
//        }
    }

    private void testScores(){
        System.out.println(""+GH.calcScore(GameSettings.GameType.Fastest, 1000, 1000, 3, 8, 8));
        System.out.println(""+GH.calcScore(GameSettings.GameType.Fastest, 1500, 1000, 3, 10, 10));

        System.out.println(""+GH.calcScore(GameSettings.GameType.Fastest, 1000, 1000, 4, 8, 8));
        System.out.println(""+GH.calcScore(GameSettings.GameType.Fastest, 2000, 1000, 4, 10, 10));

        System.out.println(""+GH.calcScore(GameSettings.GameType.Fastest, 2000, 1000, 5, 8, 8));
        System.out.println(""+GH.calcScore(GameSettings.GameType.Fastest, 2500, 1000, 5, 10, 10));

        System.out.println(""+GH.calcScore(GameSettings.GameType.Fastest, 2500, 1000, 6, 8, 8));
        System.out.println(""+GH.calcScore(GameSettings.GameType.Fastest, 3000, 1000, 6, 10, 10));
    }

    @Override
    public void render(float delta) {
        double time = TimeUtils.millis();

        //Check if we need to enable/disable some buttons
        if(time >= this.nextTick){
            this.nextTick =  time + interval;
            MainMenuGUI.changeLoginButton(Game.resolver.getSignedInGPGS());
        }

        Game.batch.begin();

        for(int i = GameData.particleEffects.size-1; i >= 0; i--){
            ParticleEffect effect = GameData.particleEffects.get(i);
            if(effect.isComplete())
                GameData.particleEffects.removeIndex(i);
            else
                effect.draw(Game.batch, delta/4f);
        }

        for(int i = GameData.gameShapeList.size-1; i >= 0; i--){
            GameShape shape = GameData.gameShapeList.get(i);
            if(shape.isDead())
                GameData.gameShapeList.removeIndex(i);
            else
                shape.render(Game.batch, delta);
        }

        Game.batch.end();

//        showShapePlay(delta);
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
        Game.adInterface.hideAdmobBannerAd();
    }
}
