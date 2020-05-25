package com.creativedroids.link;

import java.util.Map;

import android.app.AlertDialog;
import android.util.Log;

import com.amazon.inapp.purchasing.GetUserIdResponse;
import com.amazon.inapp.purchasing.Item;
import com.amazon.inapp.purchasing.ItemDataResponse;
import com.amazon.inapp.purchasing.PurchaseResponse;
import com.amazon.inapp.purchasing.PurchaseUpdatesResponse;
import com.amazon.inapp.purchasing.PurchasingObserver;
import com.creativedroids.link.utils.Constant;
import com.creativedroids.link.utils.PrefClass;

public class InAppObserver extends PurchasingObserver {

	private final BuyHints baseActivity;
	private final String TAG = "InAppObserver";

	public InAppObserver(final BuyHints buyerActivity) {
		super(buyerActivity);
		this.baseActivity = buyerActivity;
	}

	@Override
	public void onGetUserIdResponse(GetUserIdResponse arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemDataResponse(ItemDataResponse itemDataResponse) {
		// TODO Auto-generated method stub
		switch (itemDataResponse.getItemDataRequestStatus()) {

		case SUCCESSFUL:
			final Map<String, Item> items = itemDataResponse.getItemData();
			Log.d(TAG, items.toString());
			break;
		case SUCCESSFUL_WITH_UNAVAILABLE_SKUS:
			// Handle Unavailable SKUs
			Log.e(TAG, itemDataResponse.toString());
			break;
		case FAILED:
			// Handle failure
			Log.e(TAG, itemDataResponse.toString());
			break;
		}
	}

	@Override
	public void onPurchaseResponse(PurchaseResponse purchaseResponse) {
		// TODO Auto-generated method stub
		switch (purchaseResponse.getPurchaseRequestStatus()) {
		case SUCCESSFUL:
			Log.d(TAG, "SUCCESSFUL" + purchaseResponse.toString());
			Log.d(TAG, "Succesfully add : " + Constant.NUM_BUY_HINTS);
			addHint(Constant.NUM_BUY_HINTS);
			// baseActivity.unlockImage();
			break;
		case FAILED:
			Log.d(TAG, "FAILED" + purchaseResponse.toString());
			// Add code to handle purchase failure if necessary
		case INVALID_SKU:
			Log.d(TAG, "INVALID_SKU" + purchaseResponse.toString());
			// Add code to handle invalid SKU if necessary
			AlertDialog.Builder builder = new AlertDialog.Builder(baseActivity);
			builder.setMessage("Invalid Product SKU").setTitle(
					"Purchase could not be completed");
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}

	@Override
	public void onPurchaseUpdatesResponse(PurchaseUpdatesResponse arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSdkAvailable(boolean arg0) {
		// TODO Auto-generated method stub

	}

	private void addHint(int value) {
		int hint = PrefClass.getHint();
		hint = hint + value;
		PrefClass.saveHint(hint);
	}
}