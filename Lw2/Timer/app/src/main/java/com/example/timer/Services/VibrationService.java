package com.example.timer.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.RequiresApi;

public class VibrationService extends Service
{
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStart(Intent intent, int id)
    {
        super.onStart(intent, id);

        Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        VibrationEffect vibrationEffect = VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE);

        vibrator.vibrate(vibrationEffect);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}