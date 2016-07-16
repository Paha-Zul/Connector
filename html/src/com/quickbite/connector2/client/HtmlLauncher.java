package com.quickbite.connector2.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.quickbite.connector2.ActionResolver;
import com.quickbite.connector2.AdInterface;
import com.quickbite.connector2.Game;

public class HtmlLauncher extends GwtApplication implements ActionResolver, AdInterface{

    @Override
    public GwtApplicationConfiguration getConfig () {
            return new GwtApplicationConfiguration(480, 320);
    }

    @Override
    public ApplicationListener getApplicationListener () {
            return new Game(this, this);
    }

    @Override
    public boolean getSignedInGPGS() {
            return false;
    }

    @Override
    public void loginGPGS() {

    }

    @Override
    public void logoutGPGS() {

    }

    @Override
    public void submitScoreGPGS(String tableID, long score) {

    }

    @Override
    public void unlockAchievementGPGS(String achievementId) {

    }

    @Override
    public void getLeaderboardGPGS(String leaderboardID) {

    }

    @Override
    public void getAchievementsGPGS() {

    }

    @Override
    public void getLeaderboardScore(String leaderboardID, int timeSpan) {

    }

    @Override
    public void getCenteredLeaderboardScore(String leaderboardID, int timeSpan, int leaderboardType, float timeoutSeconds) {

    }

    @Override
    public void getTopLeaderboardScores(String leaderboardID, int timeSpan, int numScores) {
    
    }

    @Override
    public void submitEvent(String eventID) {

    }

    @Override
    public void purchaseNoAds() {

    }

    @Override
    public void showAdmobBannerAd() {

    }

    @Override
    public void hideAdmobBannerAd() {

    }

    @Override
    public void loadAdmobInterAd() {

    }

    @Override
    public void showAdmobInterAd() {

    }

    @Override
    public void hideAdmobInterAd() {

    }

    @Override
    public boolean showAds() {
        return false;
    }
}