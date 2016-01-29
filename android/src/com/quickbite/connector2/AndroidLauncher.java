package com.quickbite.connector2;

import android.content.Intent;
import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;

public class AndroidLauncher extends AndroidApplication implements GameHelper.GameHelperListener, ActionResolver {
	GameHelper gameHelper;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Game(this), config);

		if (gameHelper == null) {
			gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
			gameHelper.setMaxAutoSignInAttempts(0);
			gameHelper.enableDebugLog(true);
		}
		gameHelper.setup(this);
	}

	@Override
	public void onStart(){
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	public void onStop(){
		super.onStop();
		gameHelper.onStop();
	}

	@Override
	public void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);
		gameHelper.onActivityResult(request, response, data);
	}

	@Override
	public boolean getSignedInGPGS() {
		return gameHelper.isSignedIn();
	}

	@Override
	public void loginGPGS() {
		try {
			runOnUiThread(new Runnable(){
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (final Exception ex) {

		}
	}

	@Override
	public void submitScoreGPGS(String tableID, long score) {
        if (gameHelper.isSignedIn()) {
            Games.Leaderboards.submitScore(gameHelper.getApiClient(), tableID, score);
        }
	}

	@Override
	public void unlockAchievementGPGS(String achievementId) {
        if (gameHelper.isSignedIn()) {
            Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);
        }
	}

	@Override
	public void getLeaderboardGPGS(String leaderboardID) {
		if (gameHelper.isSignedIn()) {
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), leaderboardID), 100);
		}else if (!gameHelper.isConnecting()) {
			//loginGPGS();
		}
	}

	@Override
	public void getAchievementsGPGS() {
		//startActivityForResult(gameHelper.getGamesClient().getAchievementsIntent(), 101);
	}

	@Override
	public void onSignInFailed() {
	}

	@Override
	public void onSignInSucceeded() {
	}
}
