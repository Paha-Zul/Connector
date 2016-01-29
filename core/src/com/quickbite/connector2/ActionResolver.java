package com.quickbite.connector2;

/**
 * Created by Paha on 1/28/2016.
 */
public interface ActionResolver {
    public boolean getSignedInGPGS();
    public void loginGPGS();
    public void submitScoreGPGS(String tableID, long score);
    public void unlockAchievementGPGS(String achievementId);
    public void getLeaderboardGPGS(String leaderboardID);
    public void getAchievementsGPGS();
}
