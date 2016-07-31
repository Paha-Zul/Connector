package com.quickbite.connector2;

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
    void unlockAchievementGPGS(String achievementId);
    void getCurrentRankInLeaderboards(String tableID);
    void getLeaderboardGPGS(String leaderboardID);
    void getAchievementsGPGS();
    void getLeaderboardScore(String leaderboardID, int timeSpan);
    void getCenteredLeaderboardScore(String leaderboardID, int timeSpan, int leaderboardType, float timeoutSeconds);
    void getTopLeaderboardScores(String leaderboardID, int timeSpan, int numScores);
    void submitEvent(String eventID);

    void purchaseNoAds();
}
