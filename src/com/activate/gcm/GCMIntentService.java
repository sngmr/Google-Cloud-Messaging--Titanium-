package com.activate.gcm;

import java.io.IOException;
import java.io.File;
import java.util.HashMap;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiProperties;
import org.appcelerator.kroll.common.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMBroadcastReceiver;
import com.activate.gcm.C2dmModule;
import com.activate.gcm.AlertDialogActivity;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONObject;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String LCAT = "GCMIntentService";

	private static final String REGISTER_EVENT = "registerC2dm";
	private static final String UNREGISTER_EVENT = "unregister";
	private static final String MESSAGE_EVENT = "message";
	private static final String ERROR_EVENT = "error";

	public GCMIntentService(){
		super(TiApplication.getInstance().getAppProperties().getString("com.activate.gcm.sender_id", ""));
	}

	@Override
	public void onRegistered(Context context, String registrationId){
		Log.d(LCAT, "Registered: " + registrationId);

		C2dmModule.getInstance().sendSuccess(registrationId);
	}

	@Override
	public void onUnregistered(Context context, String registrationId) {
		Log.d(LCAT, "Unregistered");

		C2dmModule.getInstance().fireEvent(UNREGISTER_EVENT, new HashMap());
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.d(LCAT, "Message received");

		TiProperties systProp = TiApplication.getInstance().getAppProperties();

		HashMap data = new HashMap();
		for (String key : intent.getExtras().keySet()) {
			Log.d(LCAT, "Message key: " + key + " value: " + intent.getExtras().getString(key));

			String eventKey = key.startsWith("data.") ? key.substring(5) : key;
			data.put(eventKey, intent.getExtras().getString(key));
		}

		// Notificationメッセージから情報を取得
		// ※以前はサウンドとバイブレーションがあったけど削除した

		// アイコン
		int icon = systProp.getInt("com.activate.gcm.icon", 0);
		int iconBig = systProp.getInt("com.activate.gcm.bigicon", 0);
		Bitmap largeIcon = null;
		if (iconBig != 0) {
			largeIcon = BitmapFactory.decodeResource(getResources(), iconBig);
		}

		//another way to get icon :
		//http://developer.appcelerator.com/question/116650/native-android-java-module-for-titanium-doesnt-generate-rjava

		// Tickerテキスト
		CharSequence tickerText = (CharSequence) data.get("ticker");

		// タイトル・メッセージ
		CharSequence contentTitle = (CharSequence) data.get("title");
		CharSequence contentText = (CharSequence) data.get("message");

        // 起動Intentを作成
		Intent launcherIntent = new Intent("android.intent.action.MAIN");
		launcherIntent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		// I'm sure there is a better way ...
		launcherIntent.setComponent(ComponentName.unflattenFromString(systProp.getString("com.activate.gcm.component", "")));
		launcherIntent.addCategory("android.intent.category.LAUNCHER");

		// PendinIntentを作成
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, launcherIntent, 0);

        if (contentText == null) {
            Log.d(LCAT, "Message received , no contentText so will make this silent");
        } else {
            Log.d(LCAT, "Creating notification ...");

            // Notificationを作成
            Notification.Builder builder = new Notification.Builder(this)
            .setTicker(contentTitle)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setSmallIcon(icon)
            .setLargeIcon(largeIcon)
            .setWhen(System.currentTimeMillis())
            .setContentIntent(contentIntent)
            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
            .setAutoCancel(true);

            // NotificationManagerから通知
            NotificationManager notificationManager = 
            	(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);	// Service.NOTIFICATION_SERVICE
            notificationManager.notify(1, builder.getNotification());
        }

        // アプリ本体などで利用できるようにデータ保存
        JSONObject json = new JSONObject(data);
        systProp.setString("com.activate.gcm.last_data", json.toString());
        if (C2dmModule.getInstance() != null){
            C2dmModule.getInstance().sendMessage(data);
        }

        //
        // Notification表示後にさらにDialogで注意喚起
        //
        // Dialogタイトルがなければ追加Dialog無し（tiapp.xmlで定義）
        String alertTitle = systProp.getString("com.activate.gcm.dialog_title", null);
        if (contentText != null && alertTitle != null) {
    		// 追加Dialogが利用するデータを保存（ダイアログ表示時に消される）
    		systProp.setString("com.activate.gcm.last_data_for_dialog", json.toString());

    		// 端末ロック状態を取得
    		final KeyguardManager keyManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
    		if (keyManager.inKeyguardRestrictedInputMode()) {
    			// ロック状態：別のReceiverが毎回アンロックで動いてDialog表示用のデータがあればDialog表示する
    			Log.d(LCAT, "Screen is Locked. No dialog showed. Try next launch.");
    		} else {
    			// ロックされてない：Dialogを表示する
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

	@Override
	public void onError(Context context, String errorId) {
		Log.e(LCAT, "Error: " + errorId);

		C2dmModule.getInstance().sendError(errorId);
	}

	@Override
	public boolean onRecoverableError(Context context, String errorId) {
		Log.e(LCAT, "RecoverableError: " + errorId);

		C2dmModule.getInstance().sendError(errorId);

		return true;
	}

}

