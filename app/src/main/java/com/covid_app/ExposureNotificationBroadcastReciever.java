package com.covid_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ExposureNotificationBroadcastReciever extends BroadcastReceiver {
    private static final String TAG = "Broadcast";
    @Override
    public void onReceive(Context context, Intent intent) {
            {
                String action = intent.getAction();
                if (ExposureNotificationClient.ACTION_EXPOSURE_STATE_UPDATED
                        .equals(action))
                {
                    // Handle exposure found action
                    Log.d(TAG,"ACTION_EXPOSURE_STATE_UPDATED");
                }
                else if (ExposureNotificationClient.ACTION_EXPOSURE_NOT_FOUND
                        .equals(action))
                {
                    // Handle exposure not found action
                    Log.d(TAG,"ACTION_EXPOSURE_NOT_FOUND");
                }
                else if (ExposureNotificationClient.ACTION_SERVICE_STATE_UPDATED
                        .equals(action))
                {
                    // Handle service state change (for example, user manually disabled it)
                    Log.d(TAG,"ACTION_SERVICE_STATE_UPDATED");
                }
            }
        }
    }
