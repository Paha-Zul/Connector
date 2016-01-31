package com.quickbite.connector2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.Array;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;
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
        if (getSignedInGPGS()) {
            Games.Leaderboards.submitScore(gameHelper.getApiClient(), tableID, score);
        }
	}

	@Override
	public void unlockAchievementGPGS(String achievementId) {
        if (getSignedInGPGS()) {
            Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);
        }
	}

	@Override
	public void getLeaderboardGPGS(String leaderboardID) {
		if (getSignedInGPGS()) {
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
	public void getLeaderboardScore(String leaderboardID, int timeSpan) {
		Games.Leaderboards.loadCurrentPlayerLeaderboardScore(gameHelper.getApiClient(), leaderboardID,
				timeSpan, LeaderboardVariant.COLLECTION_PUBLIC);
	}

	@Override
	public void getCenteredLeaderboardScore(String leaderboardID, int timeSpan) {
		Games.Leaderboards.loadPlayerCenteredScores(gameHelper.getApiClient(), leaderboardID, timeSpan,
				LeaderboardVariant.COLLECTION_PUBLIC, 10).setResultCallback(new ResultCallback<Leaderboards.LoadScoresResult>() {
            @Override
            public void onResult(@NonNull Leaderboards.LoadScoresResult loadScoresResult) {
                //GUIManager.MainMenuGUI.inst().loadLeaderboardScores();
                Array<String> ranks = new Array<>();
                Array<String> names = new Array<>();
                Array<String> scores = new Array<>();

                for(LeaderboardScore score : loadScoresResult.getScores()){
                    ranks.add(score.getDisplayRank());
                    names.add(score.getScoreHolderDisplayName());
                    scores.add(score.getDisplayScore());
                }

                GUIManager.MainMenuGUI.inst().loadLeaderboardScores(ranks, names, scores);
            }
        });
	}

	@Override
	public void getTopLeaderboardScores(String leaderboardID, int timeSpan, int numScores) {
		Games.Leaderboards.loadTopScores(gameHelper.getApiClient(), leaderboardID, timeSpan, LeaderboardVariant.COLLECTION_PUBLIC, 10);
	}

	@Override
	public void onSignInFailed() {

	}

	@Override
	public void onSignInSucceeded() {

	}
}
