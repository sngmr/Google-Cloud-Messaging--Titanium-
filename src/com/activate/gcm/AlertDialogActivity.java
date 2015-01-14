package com.activate.gcm;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.support.v4.app.FragmentActivity;

import org.appcelerator.kroll.common.Log;

public class AlertDialogActivity extends FragmentActivity {
	private static final String LCAT = "AlertDialogActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e(LCAT, "onCreate");

		super.onCreate(savedInstanceState);
		AlertDialogFragment fragment = new AlertDialogFragment();
		fragment.show(getSupportFragmentManager(), "alert_dialog");
		
		Log.e(LCAT, "onCreate Complete");
	}

	@Override
	public void onAttachedToWindow() {
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
	}
}
