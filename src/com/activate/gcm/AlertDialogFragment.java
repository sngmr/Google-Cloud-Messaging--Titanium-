package com.activate.gcm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiProperties;
import org.appcelerator.kroll.common.Log;

import org.json.JSONObject;

public class AlertDialogFragment extends DialogFragment {
	private static final String LCAT = "AlertDialogActivity";

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Log.d(LCAT, "AlertDialogFragment.onCreateDialog");

		// Titaniumアプリから最後のメッセージを取得
		TiProperties systProp = TiApplication.getInstance().getAppProperties();
		String lastDataStr = systProp.getString("com.activate.gcm.last_data", null);
		if (lastDataStr == null) {
			return null;
		}

		try {
			JSONObject json = new JSONObject(lastDataStr);

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle((CharSequence) json.get("title"));
			builder.setMessage((CharSequence) json.get("message"));

			Dialog dialog = builder.create();
			dialog.setCanceledOnTouchOutside(true);

			Log.d(LCAT, "AlertDialogFragment.onCreateDialog Complete");
			return dialog;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(LCAT, "AlertDialogFragment.onCreateDialog JSONParse Exception");
			return null;
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		getActivity().finish();
	}
}
