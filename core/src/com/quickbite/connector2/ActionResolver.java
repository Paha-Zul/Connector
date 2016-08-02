package com.quickbite.connector2;

import com.quickbite.connector2.gui.GameOverGUI;

/**
 * Created by Paha on 1/28/2016.
 */
public interface ActionResolver {
    String SKU_NOADS = "no_ads";

    // (arbitrary) request code for the purchase flow
    int RC_REQUEST = 10001;

    boolean getSignedInGPGS();
    void loginGPGS();
    void logoutGPGS();
    void submitScoreGPGS(String tableID, long score);

    /**
     * Submits an event to analytics
     * @param eventID The EventID for google play games. If blank, will not send anything.
     * @param GA_ID The EventID for GameAnalytics. If blank, will not send anything.
     */
    void submitEvent(String eventID, String GA_ID);

    /**
     * Submits the game structure settings to GameAnalytics. Call this only when the start game button is hit.
     */
    void submitGameStructure();

    void unlockAchievementGPGS(String achievementId);
    void getCurrentRankInLeaderboards(String tableID, GameOverGUI gameOverGUI);
    void getLeaderboardGPGS(String leaderboardID);
    void getAchievementsGPGS();
    void getLeaderboardScore(String leaderboardID, int timeSpan);
    void getCenteredLeaderboardScore(String leaderboardID, int timeSpan, int leaderboardType, float timeoutSeconds);
    void getTopLeaderboardScores(String leaderboardID, int timeSpan, int numScores);

    void purchaseNoAds();
}
