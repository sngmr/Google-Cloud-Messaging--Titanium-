package com.activate.gcm;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.appcelerator.kroll.common.Log;

public class AlertDialogActivity extends FragmentActivity {
	private static final String LCAT = "AlertDialogActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(LCAT, "AlertDialogActivity.onCreate");

		super.onCreate(savedInstanceState);
		AlertDialogFragment fragment = new AlertDialogFragment();
		fragment.show(getSupportFragmentManager(), "alert_dialog");
		
		Log.d(LCAT, "AlertDialogActivity.onCreate Complete");
	}
}
