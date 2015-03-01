package com.activate.gcm;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiProperties;
import org.appcelerator.kroll.common.Log;

public class AlertDialogReceiver extends BroadcastReceiver {
	private static final String LCAT = "AlertDialogReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// HOME画面が表示された場合のみ
		String action = intent.getAction();
		if (TextUtils.equals(action, Intent.ACTION_USER_PRESENT)) {
			// Dialog表示用のデータがある場合のみ表示
			TiProperties systProp = TiApplication.getInstance().getAppProperties();
			String lastDataStr = systProp.getString("com.activate.gcm.last_data_for_dialog", null);
			if (lastDataStr != null) {
				Intent alertIntent = new Intent(context, AlertDialogActivity.class);
				PendingIntent alertPendingIntent = PendingIntent.getActivity(context, 0, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				try {
					alertPendingIntent.send();
					Log.d(LCAT, "pendingIntent.send");
				} catch (PendingIntent.CanceledException e) {
					e.printStackTrace();
					Log.d(LCAT, "CanceledException");
				}			
			}
		}
	}
}
