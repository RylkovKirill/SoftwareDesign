package com.example.timer.Views;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.timer.Helpers.SettingsHelper;

public class StartActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);
        SettingsHelper.SetSettings(PreferenceManager.getDefaultSharedPreferences(this));
        startActivity(intent);
        finish();
    }
}