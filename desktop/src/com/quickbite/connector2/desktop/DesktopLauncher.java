package com.quickbite.connector2.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.quickbite.connector2.ActionResolver;
import com.quickbite.connector2.Game;

public class DesktopLauncher implements ActionResolver {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 480;
		config.height = 800;

		DesktopLauncher instance = new DesktopLauncher();
		new LwjglApplication(new Game(instance), config);
	}

	@Override
	public boolean getSignedInGPGS() {
		return false;
	}

	@Override
	public void loginGPGS() {
		System.out.println("Logging in to GPG");
	}

	@Override
	public void submitScoreGPGS(String tableID, long score) {
		System.out.println("Submitting score to table "+tableID+" with score "+score);
	}

	@Override
	public void unlockAchievementGPGS(String achievementId) {
		System.out.println("Unlocking achievement "+achievementId);
	}

	@Override
	public void getLeaderboardGPGS(String leaderboardID) {
		System.out.println("Getting leaderboard "+leaderboardID);
	}

	@Override
	public void getAchievementsGPGS() {
		System.out.println("Getting achievement");
	}
}
