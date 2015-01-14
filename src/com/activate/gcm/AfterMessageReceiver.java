package com.activate.gcm;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.appcelerator.kroll.common.Log;

/**
 * AfterMessageIntentServiceから呼び出されるReceiver
 */
public class AfterMessageReceiver extends BroadcastReceiver {
	private static final String LCAT = "AfterMessageReceiver";

	@Override
	public void onReceive(Context context, Intent data) {
		Log.d(LCAT, "AfterMessageReceiver.onReceive");
		
		Intent intent = new Intent(context, AlertDialogActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		try {
			pendingIntent.send();
			Log.d(LCAT, "AfterMessageReceiver.pendingIntent.send");
		} catch (PendingIntent.CanceledException e) {
			e.printStackTrace();
			Log.e(LCAT, "PendingIntent.CanceledException");
		}
	}
}
