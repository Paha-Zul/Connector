package com.quickbite.connector2.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.quickbite.connector2.ActionResolver;
import com.quickbite.connector2.AdInterface;
import com.quickbite.connector2.Game;
import com.quickbite.connector2.gui.GameOverGUI;

public class DesktopLauncher implements ActionResolver, AdInterface {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 480;
		config.height = 800;
		config.resizable = false;

		DesktopLauncher instance = new DesktopLauncher();
		new LwjglApplication(new Game(instance, instance), config);
	}

	@Override
	public boolean getSignedInGPGS() {
		return true;
	}

	@Override
	public void loginGPGS() {
		System.out.println("Logging in to GPG");
	}

	@Override
	public void logoutGPGS() {
		System.out.println("Logging out of GPG");
	}

	@Override
	public void submitScoreGPGS(String tableID, long score) {
		System.out.println("Submitting score to table ' "+tableID+"' with score "+score);
	}

	@Override
	public void unlockAchievementGPGS(String achievementId) {
		System.out.println("Unlocking achievement "+achievementId);
	}

	@Override
	public void getCurrentRankInLeaderboards(String tableID, GameOverGUI gameOverGUI) {

	}

	@Override
	public void getLeaderboardGPGS(String leaderboardID) {
		System.out.println("Getting leaderboard "+leaderboardID);
	}

	@Override
	public void getAchievementsGPGS() {
		System.out.println("Getting achievement");
	}

	@Override
	public void getLeaderboardScore(String leaderboardID, int timeSpan) {
		System.out.println("Getting Leaderboard Score");
	}

	@Override
	public void getCenteredLeaderboardScore(String leaderboardID, int timeSpan, int leaderboardType, float timeoutSeconds) {
		System.out.println("Getting Leaderboard Centered Score with timespane of "+timeSpan);

	}

	@Override
	public void getTopLeaderboardScores(String leaderboardID, int timeSpan, int numScores) {
		System.out.println("Getting top leaderboard Score");
	}

	@Override
	public void submitEvent(String eventID) {
		System.out.println("Submitting for eventID "+eventID);
	}

	@Override
	public void purchaseNoAds() {
		System.out.println("Purchasing no ads");
	}

	@Override
	public void showAdmobBannerAd() {
		System.out.println("Showing banner");
	}

	@Override
	public void hideAdmobBannerAd() {
		System.out.println("Hiding banner");
	}

	@Override
	public void loadAdmobInterAd() {
		System.out.println("Loading inter");
	}

	@Override
	public void showAdmobInterAd() {
		System.out.println("Showing inter");
	}

	@Override
	public void hideAdmobInterAd() {
		System.out.println("Hiding inter");
	}

	@Override
	public boolean showAds() {
		return true;
	}
}
