package com.quickbite.connector2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.example.android.trivialdrivesample.util.IabHelper;
import com.example.android.trivialdrivesample.util.IabResult;
import com.example.android.trivialdrivesample.util.Inventory;
import com.example.android.trivialdrivesample.util.Purchase;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.example.games.basegameutils.GameHelper;
import com.quickbite.connector2.gui.MainMenuGUI;

public class AndroidLauncher extends AndroidApplication implements GameHelper.GameHelperListener, ActionResolver, AdInterface {
	GameHelper gameHelper;

	private AdView adView;
	private View gameView;
	private AdListener adListener;
	private InterstitialAd interstitialAd;
	private boolean showingBannerAd = false, showAds = false, bannerAdLoaded = false;
	private IabHelper mHelper;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		Game game = new Game(this, this);

		initBilling();

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(game, config);

		setupUpGameHelper();

		super.onCreate(savedInstanceState);

		setupAds(game);
	}

	private void setupUpGameHelper(){
		if (gameHelper == null) {
			gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
			gameHelper.setMaxAutoSignInAttempts(0);
			gameHelper.enableDebugLog(true);
		}

		gameHelper.setup(this);
	}

	private void setupAds(Game game){
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useAccelerometer = false;
		cfg.useCompass = false;

		// Do the stuff that initialize() would do for you
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		FrameLayout fLayout = new FrameLayout(this);
		FrameLayout.LayoutParams fParams = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT,
				Gravity.BOTTOM|android.view.Gravity.CENTER_HORIZONTAL);
		fLayout.setLayoutParams(fParams);

		AdView admobView = createAdView();

		View gameView = createGameView(cfg, game);

		fLayout.addView(gameView);
		fLayout.addView(admobView);

		setContentView(fLayout);
		startAdvertising(admobView);

		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(getString(R.string.inter_ad_unit_id));
		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
			}
			@Override
			public void onAdClosed() {
				AdRequest interstitialRequest = new AdRequest.Builder().build();
				interstitialAd.loadAd(interstitialRequest);
				SoundManager.playMusic();
			}
		});

		AdRequest interstitialRequest = new AdRequest.Builder().build();
		interstitialAd.loadAd(interstitialRequest);
	}

	private AdView createAdView() {
		adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(getString(R.string.banner_ad_unit_id));
		adView.setId(R.id.adViewId); // this is an arbitrary id, allows for relative positioning in createGameView()
		FrameLayout.LayoutParams fParams = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT,
				Gravity.BOTTOM|android.view.Gravity.CENTER_HORIZONTAL);
		adView.setLayoutParams(fParams);
		adView.setBackgroundColor(Color.TRANSPARENT);
		return adView;
	}

	private View createGameView(AndroidApplicationConfiguration cfg, Game game) {
		gameView = initializeForView(game, cfg);
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
	public void showAdmobBannerAd() {
		if(showAds) {
			showingBannerAd = true;
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					adView.setVisibility(View.VISIBLE);
				}
			});
		}
	}

	@Override
	public void hideAdmobBannerAd() {
		showingBannerAd = false;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adView.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void loadAdmobInterAd() {

	}

	@Override
	public void showAdmobInterAd() {
		if(showAds) {
			try {
				runOnUiThread(new Runnable() {
					public void run() {
						if (interstitialAd.isLoaded()) {
							interstitialAd.show();
						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void hideAdmobInterAd() {

	}

	@Override
	public boolean showAds() {
		return showAds;
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

//		if ((request == 100) && response == GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED) {
//			gameHelper.disconnect();
//			// update your logic here (show login btn, hide logout btn).
//		} else {
//			try {
//				gameHelper.onActivityResult(request, response, data);
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
	}

	@Override
	public void onResume() {
		super.onResume();
		SoundManager.playMusic();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
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
			ex.printStackTrace();
		}
	}

	@Override
	public void logoutGPGS() {
        Log.d("AndroidLauncher", "Trying to sign out");
		runOnUiThread(new Runnable() {
			public void run() {
				gameHelper.signOut();
			}
		});
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
		}
	}

	@Override
	public void getAchievementsGPGS() {
		//startActivityForResult(gameHelper.getGamesClient().getAchievementsIntent(), 100);
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

            }
        });

		//If we wait too long, cancel the result.
		timeOutTimer.scheduleTask(new Timer.Task() {
			@Override
			public void run() {
				if(TimeUtils.millis() >= timeStarted + _timeout) {
					pendingResult.cancel();
					timeOutTimer.clear();
				}
			}
		}, 0, 0.5f);

	}

	@Override
	public void getTopLeaderboardScores(String leaderboardID, int timeSpan, int numScores) {
		Games.Leaderboards.loadTopScores(gameHelper.getApiClient(), leaderboardID, timeSpan, LeaderboardVariant.COLLECTION_PUBLIC, 10);
	}

	@Override
	public void submitEvent(String eventID) {
		Games.Events.increment(gameHelper.getApiClient(), eventID, 1);
	}

	private void initBilling(){
		System.out.println("Starting up billing...");

		mHelper = new IabHelper(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiqYxrqMywByEg2dXFaOlcZBNP/JV3fA5iOw+a52SNMEQOW2fsPIkR0MpnnkUW66wzPGdr9gzR" +
				"4Ete5UO3lvbkDo7YFrOtETazfV5FVLNVoCZhb0Yp3Wo9ewRlEayfkJOKTfNDlCqKiRaKyMgtZRX0BG47N25VOV8Hg1qP/Q0bFXSd+WANnwZAyPuinTUtFFkSW1G9Nnzy1LshS4zJsdY" +
				"F+T0tpRQ/8lK/DySoRPZp58d6IUsNCTTQaDTY42gHs4CnhAP6DQRSmhjXZZZ4VnA67D9DcKzazFt3A97cSU2MzhR0q+VAAlzb/gMaLbeWLFckwRV3x1U4+9aa+1pa1L/SQIDAQAB");

		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			@Override
			public void onIabSetupFinished(IabResult result) {
				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
                    Log.d("AndroidLauncher", "There was a problem starting billing");
				}else {
					//Let's then query the inventory...
					try {
						mHelper.queryInventoryAsync(mGotInventoryListener);
					} catch (IabHelper.IabAsyncInProgressException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	public void purchaseNoAds() {
		try {
			//Launch the purchase flow with a test SKU for now.
			mHelper.launchPurchaseFlow(this, SKU_NOADS, RC_REQUEST, new IabHelper.OnIabPurchaseFinishedListener() {
				@Override
				public void onIabPurchaseFinished(IabResult result, Purchase info) {
					if(!result.isSuccess()){

					}else{
						showAds = false;
						hideAdmobBannerAd();
						MainMenuGUI.removeNoAdsButton();
					}
				}
			}, "hmm");
		} catch (IabHelper.IabAsyncInProgressException e) {
			e.printStackTrace();
		}
	}

	private IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		@Override
		public void onQueryInventoryFinished(IabResult result, Inventory inv) {
			if(result.isFailure() || mHelper == null || inv == null) {
//				System.out.println("InventoryCallback, Something went wrong. result failed: "+result.isFailure()+
//						", mHelper null: "+(mHelper == null) +", inventory null: "+(inv == null));
				return;
			}

			if (inv.hasPurchase("no_ads")) {
				showAds = false;
				hideAdmobBannerAd();
			} else {
				showAds = true;
			}
		}
	};

	@Override
	public void onSignInFailed() {
		SoundManager.playMusic();
	}

	@Override
	public void onSignInSucceeded() {
		SoundManager.playMusic();
	}


}
