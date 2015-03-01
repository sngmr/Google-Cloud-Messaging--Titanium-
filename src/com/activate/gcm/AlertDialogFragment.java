package com.activate.gcm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiProperties;
import org.appcelerator.kroll.common.Log;

import org.json.JSONObject;

public class AlertDialogFragment extends DialogFragment {
	private static final String LCAT = "AlertDialogFragment";

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Log.d(LCAT, "onCreateDialog");

		// Titaniumアプリから最後のメッセージを取得
		TiProperties systProp = TiApplication.getInstance().getAppProperties();
		String lastDataStr = systProp.getString("com.activate.gcm.last_data_for_dialog", null);
		if (lastDataStr == null) {
			return null;
		} else {
			// データを消しておく
			systProp.setString("com.activate.gcm.last_data_for_dialog", null);
		}

		// その他情報を取得
		String alertTitle = systProp.getString("com.activate.gcm.dialog_title", "");
		final String[] packageNames = systProp.getString("com.activate.gcm.component", "").split("/");

		try {
			JSONObject json = new JSONObject(lastDataStr);

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			// タイトルと内容
			builder.setTitle(alertTitle);
			builder.setMessage((CharSequence) json.get("title") + "\n" + (CharSequence) json.get("message"));

			// イベントハンドラ
			builder.setPositiveButton("開く", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.setClassName(packageNames[0], packageNames[1]);
					startActivity(intent);
				}
			});
			builder.setNegativeButton("閉じる", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});

			Dialog dialog = builder.create();
			dialog.setCanceledOnTouchOutside(true);

			Log.d(LCAT, "onCreateDialog Complete");
			return dialog;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(LCAT, "onCreateDialog Exception");
			return null;
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		getActivity().finish();
	}
}
