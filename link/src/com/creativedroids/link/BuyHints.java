package com.creativedroids.link;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.inapp.purchasing.PurchasingManager;
import com.creativedroids.link.utils.Constant;
import com.creativedroids.link.utils.DataHandler;
import com.creativedroids.link.utils.PrefClass;
import com.jirbo.adcolony.AdColony;
import com.jirbo.adcolony.AdColonyAd;
import com.jirbo.adcolony.AdColonyAdAvailabilityListener;
import com.jirbo.adcolony.AdColonyAdListener;
import com.jirbo.adcolony.AdColonyVideoAd;
import com.tapjoy.TapjoyConnect;
import com.tapjoy.TapjoyConnectNotifier;
import com.tapjoy.TapjoyEarnedPointsNotifier;
import com.tapjoy.TapjoyNotifier;
import com.tapjoy.TapjoySpendPointsNotifier;

public class BuyHints extends Activity implements OnClickListener,
		AdColonyAdListener, AdColonyAdAvailabilityListener {

	private final static String TAG = "DOTS";

	private Context context = this;
	private Button btnGet20hints, btnGet5hints, btnShareApp, btnWatchVideo;
	private Handler button_text_handler;
	private Runnable button_text_runnable;

	private String tap_appId = "";
	private String tap_appKey = "";
	private String tap_hintId = "";

	private Hashtable<String, String> flags;

	private String appId = "";

	private int totalTapHints;
	private boolean earnedHints = false;
	private int sharedAppHints = 2;
	private boolean isShared = false;
	private int earnedTapHints = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.buy_hints);

		setGuiListner();

		// // Tapjoys ads
		// if (DataHandler.tapjoy_ads) {
		// tap_appId = getResources().getString(R.string.tap_appId);
		// tap_appKey = getResources().getString(R.string.tap_appKey);
		// tap_hintId = getResources().getString(R.string.tap_hintId);
		//
		// flags = new Hashtable<String, String>();
		// flags.put(TapjoyConnectFlag.ENABLE_LOGGING, "true");
		//
		// initialiseTapjoy();
		// }

		if (DataHandler.video_ads) {
			// AdColony
			final String addColonyZoneId = initAdColony();

			// Handler and Runnable for updating button text based on ad
			// availability listener
			button_text_handler = new Handler();
			button_text_runnable = new Runnable() {
				public void run() {
					btnWatchVideo.setText("Watch video for 4 hints");
					btnWatchVideo
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									Log.d(TAG, "Click play video");
									AdColonyVideoAd ad = new AdColonyVideoAd(
											addColonyZoneId)
											.withListener(BuyHints.this);
									ad.show();
								}
							});
				}
			};
		}

		if (!DataHandler.video_ads) {
			btnWatchVideo.setVisibility(View.GONE);
			// ((TextView)
			// findViewById(R.id.tvvideohints)).setVisibility(View.GONE);
		}

		// if (!DataHandler.tapjoy_ads) {
		// bfree.setVisibility(View.GONE);
		// ((TextView) findViewById(R.id.tvfreehints)).setVisibility(View.GONE);
		// }

	}

	public void makePurchase(String skuString) {

		PurchasingManager.initiatePurchaseRequest(skuString);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String skuString = "";
		switch (v.getId()) {
		case R.id.btnShareApp:
			shareIt();
			break;
		case R.id.btnGet20hints:
			skuString = getResources().getString(R.string.consumable_sku_20);
			makePurchase(skuString);
			Constant.NUM_BUY_HINTS = Constant.HINTS_20;
			break;
		case R.id.btnGet5hints:
			skuString = getResources().getString(R.string.consumable_sku_5);
			makePurchase(skuString);
			Constant.NUM_BUY_HINTS = Constant.HINTS_5;
			break;
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		initAmazonIap();
	}

	@Override
	public void onAdColonyAdAvailabilityChange(boolean available, String zone_id) {
		// TODO Auto-generated method stub
		if (available)
			button_text_handler.post(button_text_runnable);
	}

	@Override
	public void onAdColonyAdAttemptFinished(AdColonyAd arg0) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onAdColonyAdAttemptFinished");
		addHint(getResources().getInteger(R.integer.videoHints));
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context, "4 Hints added", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	@Override
	public void onAdColonyAdStarted(AdColonyAd arg0) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onAdColonyAdStarted");
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context, "Watch Complete video to add hints",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		AdColony.pause();
		// TapjoyConnect.getTapjoyConnectInstance().appPause();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		AdColony.resume(this);
		PurchasingManager.initiateGetUserIdRequest();
		// TapjoyConnect.getTapjoyConnectInstance().appResume();
		// TapjoyConnect.getTapjoyConnectInstance().getTapPoints(
		// new TapjoyNotifier() {
		// @Override
		// public void getUpdatePointsFailed(String error) {
		// Log.i(TAG, error);
		// }
		//
		// @Override
		// public void getUpdatePoints(String currencyName,
		// int pointTotal) {
		// Log.i(TAG, "currencyName: " + currencyName);
		// Log.i(TAG, "pointTotal: " + pointTotal);
		//
		// if (earnedHints) {
		// earnedTapHints = pointTotal;
		// spendTapPoints();
		// earnedHints = false;
		// } else {
		//
		// }
		//
		// }
		// });
		if (isShared && hasDayPassed(PrefClass.getLastSharedTime())) {
			PrefClass.setLastSharedTime(System.currentTimeMillis());
			sharedAppHints = getResources().getInteger(R.integer.shareHints);
			Toast.makeText(context,
					"Thanks for sharing." + sharedAppHints + " hints added",
					Toast.LENGTH_SHORT).show();
			addHint(sharedAppHints);
			isShared = false;
			this.finish();
		} else if (isShared == true
				&& hasDayPassed(PrefClass.getLastSharedTime()) == false) {
			Toast.makeText(
					context,
					"Thanks for sharing.But only 1 share is allowed per day to get hints. Try sharing tommorow",
					Toast.LENGTH_LONG).show();
			isShared = false;
			this.finish();
		}
	}

	private void initialiseTapjoy() {
		TapjoyConnect.requestTapjoyConnect(this, tap_appId, tap_appKey, flags,
				new TapjoyConnectNotifier() {
					@Override
					public void connectSuccess() {
						onConnectSuccess();
					}

					@Override
					public void connectFail() {
						onConnectFail();
					}
				});

		TapjoyConnect.getTapjoyConnectInstance().getTapPoints(
				new TapjoyNotifier() {

					@Override
					public void getUpdatePointsFailed(String error) {
						Log.i(TAG, error);
					}

					@Override
					public void getUpdatePoints(String currencyName,
							int pointTotal) {
						Log.i(TAG, "currencyName: " + currencyName);
						Log.i(TAG, "pointTotal: " + pointTotal);
						if (earnedHints) {
							earnedTapHints = pointTotal;
							spendTapPoints();
							earnedHints = false;
						} else {
						}
					}
				});

	}

	private void spendTapPoints() {
		TapjoyConnect.getTapjoyConnectInstance().spendTapPoints(earnedTapHints,
				new TapjoySpendPointsNotifier() {
					@Override
					public void getSpendPointsResponseFailed(String error) {
						Log.i(TAG, error);
					}

					@Override
					public void getSpendPointsResponse(String currencyName,
							int pointTotal) {
						Log.i(TAG, "spent tap points : " + earnedTapHints);
						addHint(earnedTapHints);
					}
				});

	}

	private void addHint(int value) {
		int hint = PrefClass.getHint();
		hint = hint + value;
		PrefClass p1 = new PrefClass(context);
		PrefClass.saveHint(hint);
	}

	private boolean hasDayPassed(long lastSharedTime) {
		boolean passed = false;
		if (lastSharedTime == 0)
			passed = true;
		else if (lastSharedTime + 24 * 60 * 60 * 100 < System
				.currentTimeMillis()) {
			passed = true;
		}
		return passed;
	}

	private void shareIt() {
		String appPackageName = getPackageName();
		isShared = true;
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_TEXT,
				"http://play.google.com/store/apps/details?id="
						+ appPackageName);
		i.putExtra(Intent.EXTRA_SUBJECT,
				getResources().getString(R.string.shareSubject));
		startActivity(Intent.createChooser(i, "Share"));
	}

	private void onConnectSuccess() {
		TapjoyConnect.getTapjoyConnectInstance().setEarnedPointsNotifier(
				new TapjoyEarnedPointsNotifier() {
					@Override
					public void earnedTapPoints(int amount) {
						earnedHints = true;
						totalTapHints = amount;
						Log.i(TAG, "you have earned " + totalTapHints);
					}
				});
	}

	private void onConnectFail() {
		Log.e(TAG, "Tapjoy connect call failed.");

	}

	private void initAmazonIap() {
		InAppObserver observer = new InAppObserver(this);
		PurchasingManager.registerObserver(observer);

		Set<String> skuList = new HashSet<String>(1);
		skuList.add(getResources().getString(R.string.consumable_sku_20));
		skuList.add(getResources().getString(R.string.consumable_sku_5));

		PurchasingManager.initiateItemDataRequest(skuList);
	}

	private String initAdColony() {
		String addColonyAppId = getResources().getString(
				R.string.addColonyAppId);
		String addColonyZoneId = getResources().getString(
				R.string.addColonyZoneId);
		AdColony.configure(this, "version:1.0,store:google", addColonyAppId,
				addColonyZoneId);
		// version - arbitrary application version
		// store - google or amazon

		// Add ad availability listener
		AdColony.addAdAvailabilityListener(this);

		// Disable rotation if not on a tablet-sized device (note: not
		// necessary to use AdColony).
		if (!AdColony.isTablet()) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		return addColonyZoneId;
	}

	private void setGuiListner() {
		btnGet20hints = (Button) findViewById(R.id.btnGet20hints);
		btnGet5hints = (Button) findViewById(R.id.btnGet5hints);
		btnShareApp = (Button) findViewById(R.id.btnShareApp);
		btnWatchVideo = (Button) findViewById(R.id.btnWatchVideo);

		btnGet20hints.setTypeface(DataHandler.getTypeface(context));
		btnGet5hints.setTypeface(DataHandler.getTypeface(context));
		btnShareApp.setTypeface(DataHandler.getTypeface(context));
		btnWatchVideo.setTypeface(DataHandler.getTypeface(context));

		btnGet20hints.setOnClickListener(this);
		btnGet5hints.setOnClickListener(this);
		btnShareApp.setOnClickListener(this);
		// btnWatchVideo.setOnClickListener(this);

		((TextView) findViewById(R.id.tvgethints)).setTypeface(DataHandler
				.getTypeface(context));
		((TextView) findViewById(R.id.tvfreehints)).setTypeface(DataHandler
				.getTypeface(context));
	}

}
