package com.quickbite.connector2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.google.android.gms.ads.*;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.example.games.basegameutils.GameHelper;

public class AndroidLauncher extends AndroidApplication implements GameHelper.GameHelperListener, ActionResolver, AdInterface {
	GameHelper gameHelper;

	private AdView adView;
	private View gameView;
	private AdListener adListener;
	private InterstitialAd interstitialAd;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		Game game = new Game(this, this);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(game, config);

		if (gameHelper == null) {
			gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
			gameHelper.setMaxAutoSignInAttempts(0);
			gameHelper.enableDebugLog(true);
		}

		gameHelper.setup(this);

		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useAccelerometer = false;
		cfg.useCompass = false;

		// Do the stuff that initialize() would do for you
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		RelativeLayout layout = new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);

		AdView admobView = createAdView();
		layout.addView(admobView);

		View gameView = createGameView(cfg, game);
		layout.addView(gameView);

		setContentView(layout);
		startAdvertising(admobView);

		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(getString(R.string.inter_ad_unit_id));
		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				Toast.makeText(getApplicationContext(), "Finished Loading Interstitial", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onAdClosed() {
				Toast.makeText(getApplicationContext(), "Closed Interstitial", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private AdView createAdView() {
		adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(getString(R.string.banner_ad_unit_id));
		adView.setId(R.id.adViewId); // this is an arbitrary id, allows for relative positioning in createGameView()
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		adView.setLayoutParams(params);
		adView.setBackgroundColor(android.graphics.Color.BLACK);
		return adView;
	}

	private View createGameView(AndroidApplicationConfiguration cfg, Game game) {
		gameView = initializeForView(game, cfg);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.ABOVE, adView.getId());
		gameView.setLayoutParams(params);
		return gameView;
	}

	private void startAdvertising(AdView adView) {
		AdRequest.Builder builder = new AdRequest.Builder();
		builder.addTestDevice("BE119EDAB7342FD3CF2C0405E4AFF269");
		AdRequest adRequest = builder.build();
		adView.loadAd(adRequest);
		adView.setVisibility(View.GONE);
	}

	@Override
	public void showBannerAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adView.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public void hideBannerAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adView.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void loadInterAd() {

	}

	@Override
	public void showInterAd() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
				  if (interstitialAd.isLoaded()) {
					interstitialAd.show();
					Toast.makeText(getApplicationContext(), "Showing Interstitial", Toast.LENGTH_SHORT).show();
				  }
				  else {
					AdRequest interstitialRequest = new AdRequest.Builder().build();
					interstitialAd.loadAd(interstitialRequest);
					Toast.makeText(getApplicationContext(), "Loading Interstitial", Toast.LENGTH_SHORT).show();
				  }
				}
			});
	   	} catch (Exception e) {

		}
	}

	@Override
	public void hideInterAd() {

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
			startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(gameHelper.getApiClient()), 100);
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
	public void getCenteredLeaderboardScore(String leaderboardID, int timeSpan, int leaderboardType, float timeoutMillis) {
		final double timeStarted = TimeUtils.millis();
		final double _timeout = timeoutMillis; //In millis

		final Timer timeOutTimer = new Timer();

		final PendingResult<Leaderboards.LoadScoresResult> pendingResult = Games.Leaderboards.loadPlayerCenteredScores(gameHelper.getApiClient(),
				leaderboardID, timeSpan, leaderboardType, 10);

		//Create the callback. If valid, call the GUI to load the scores.
		pendingResult.setResultCallback(new ResultCallback<Leaderboards.LoadScoresResult>() {
            @Override
            public void onResult(@NonNull Leaderboards.LoadScoresResult loadScoresResult) {
				timeOutTimer.clear(); //Clear the timeOutTimer

				if(loadScoresResult.getStatus().getStatusCode() != GamesStatusCodes.STATUS_OK)
					return;

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

		//If we wait too long, cancel the result.
		timeOutTimer.scheduleTask(new Timer.Task() {
			@Override
			public void run() {
				if(TimeUtils.millis() >= timeStarted + _timeout) {
					Gdx.app.debug("Leaderboard", "Leaderboard Timed out successfully");
					pendingResult.cancel();
					timeOutTimer.clear();
                    GUIManager.MainMenuGUI.inst().loadLeaderboardScores(null, null, null);
				}
			}
		}, 0, 0.5f);

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
