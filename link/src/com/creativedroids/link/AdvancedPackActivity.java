package com.creativedroids.link;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.creativedroids.link.R;
import com.creativedroids.link.utils.DataHandler;

public class AdvancedPackActivity extends Activity implements OnClickListener {

	private Button bpack5, bpack6, bpack7, bpack8, bpack9, bpack10;
	private Intent intent;
	private TextView tvselectPack;
	private Context context = this;
	private String packName = "a5x5";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.packselectscreen);

		tvselectPack = (TextView) findViewById(R.id.tvSelectPack);
		tvselectPack.setTypeface(DataHandler.getTypeface(context));

		bpack5 = (Button) findViewById(R.id.bpack5);
		bpack6 = (Button) findViewById(R.id.bpack6);
		bpack7 = (Button) findViewById(R.id.bpack7);
		bpack8 = (Button) findViewById(R.id.bpack8);
		bpack9 = (Button) findViewById(R.id.bpack9);
		bpack10 = (Button) findViewById(R.id.bpack10);

		bpack5.setTypeface(DataHandler.getTypeface(context));
		bpack6.setTypeface(DataHandler.getTypeface(context));
		bpack7.setTypeface(DataHandler.getTypeface(context));
		bpack8.setTypeface(DataHandler.getTypeface(context));
		bpack9.setTypeface(DataHandler.getTypeface(context));
		bpack10.setTypeface(DataHandler.getTypeface(context));

		bpack5.setOnClickListener(this);
		bpack6.setOnClickListener(this);
		bpack7.setOnClickListener(this);
		bpack8.setOnClickListener(this);
		bpack9.setOnClickListener(this);
		bpack10.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.bpack5:
			packName = "a5x5";
			break;
		case R.id.bpack6:
			packName = "a6x6";
			break;
		case R.id.bpack7:
			packName = "a7x7";
			break;
		case R.id.bpack8:
			packName = "a8x8";
			break;
		case R.id.bpack9:
			packName = "a9x9";
			break;
		case R.id.bpack10:
			packName = "a10x10";
			break;
		}
		intent = new Intent(this, LevelsActivity.class);
		intent.putExtra("packname", packName);
		intent.putExtra("packDisplayName", packName);
		startActivity(intent);
	}

}
