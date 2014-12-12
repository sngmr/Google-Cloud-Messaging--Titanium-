package com.activate.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;

import org.appcelerator.kroll.common.Log;

public class TimerIntentService extends IntentService {
    private static final String LCAT = "TimerIntentService";

    public TimerIntentService(String name) {
        super(name);
    }

    public TimerIntentService() {
        super("TimerIntentService");
    }

    @Override
    protected void onHandleIntent(Intent data) {
        Log.d(LCAT, "TimerIntentService.onHandleIntent");

        SystemClock.sleep(5000);

        Intent intent = new Intent();
        intent.setAction("TIMER_FINISHED");
        sendBroadcast(intent);
        Log.d(LCAT, "TimerIntentService.sendBroadcast");
    }
}
