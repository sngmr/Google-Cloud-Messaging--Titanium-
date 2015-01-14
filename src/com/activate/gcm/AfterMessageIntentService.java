package com.activate.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;

import org.appcelerator.kroll.common.Log;

/**
 * GCMIntentServiceの後処理として呼び出されるIntentService
 */
public class AfterMessageIntentService extends IntentService {
    private static final String LCAT = "AfterMessageIntentService";

    public AfterMessageIntentService(String name) {
        super(name);
    }

    public AfterMessageIntentService() {
        super("AfterMessageIntentService");
    }

    @Override
    protected void onHandleIntent(Intent data) {
        Log.d(LCAT, "AfterMessageIntentService.onHandleIntent");

        SystemClock.sleep(1500);

        Intent intent = new Intent();
        intent.setAction("MESSAGE_ARRIVE");
        sendBroadcast(intent);
        Log.d(LCAT, "AfterMessageIntentService.sendBroadcast");
    }
}
