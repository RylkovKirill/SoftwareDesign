package com.example.timer.Views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.timer.App;
import com.example.timer.Helpers.SettingsHelper;
import com.example.timer.R;
import com.example.timer.Entities.Timer;
import com.example.timer.Services.VibrationService;
import com.example.timer.ViewModels.TimerListViewModel;
import com.example.timer.Views.Adapters.AlertDialogAdapter;
import com.example.timer.Views.Adapters.TimerListAdapter;
import com.example.timer.Views.DetailActivity;
import com.example.timer.Views.SettingsActivity;
import com.example.timer.Views.TimerActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    List<String> dialogActions;
    AlertDialogAdapter actionsOnTimer;

    ListView timerListView;

    Context context = this;

    TimerListAdapter timerAdapter;
    View addTimer;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        dialogActions = Arrays.asList(getResources().getString(R.string.play),getResources().getString(R.string.edit),getResources().getString(R.string.createCopy),getResources().getString(R.string.delete));
        actionsOnTimer = new AlertDialogAdapter(context, android.R.layout.select_dialog_item,dialogActions);

        setTheme(SettingsHelper.Theme);
        getBaseContext().getResources().updateConfiguration(SettingsHelper.configuration, null);

        setContentView(R.layout.activity_main);


        addTimer =  findViewById(R.id.addTimerButton);
        addTimer.setOnClickListener(view -> AddTimer(new Timer()));

        timerAdapter = new TimerListAdapter(context, R.layout.timer_list_item, App.getInstance().getTimerDao().GetAll());


        timerListView = findViewById(R.id.timerListView);
        timerListView.setSelector(android.R.color.transparent);
        timerListView.setDivider(null);
        timerListView.setDividerHeight(20);
        timerListView.setAdapter(timerAdapter);

        timerListView.setOnItemClickListener((parent, view, position, id) -> EditTimer(parent, position));

        timerListView.setOnItemLongClickListener((parent, view, position, id) -> {

            Intent intentVibrate = new Intent(getApplicationContext(), VibrationService.class);
            startService(intentVibrate);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setNegativeButton("Cancel", null);
            builder.setAdapter(actionsOnTimer, (dialog, which) ->
            {
                switch (which)
                {
                    case 0:
                        PlayTimer(parent, position);
                        break;
                    case 1:
                        EditTimer(parent, position);
                        break;
                    case 2:
                        Timer timer =new Timer( (Timer) parent.getItemAtPosition(position));
                        AddTimer(timer);
                        break;
                    case 3:
                        DeleteTimer(parent, position);
                        break;
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return  true;
        });

        new ViewModelProvider(this).get(TimerListViewModel.class).getTimers().observe(this, timers -> {
            timerAdapter = new TimerListAdapter(context, R.layout.timer_list_item, App.getInstance().getTimerDao().GetAll());
            timerListView.setAdapter(timerAdapter);
        });
    }


    public void PlayTimer(AdapterView<?> parent, int position)
    {
        Intent intent = new Intent(context, TimerActivity.class);
        intent.putExtra("id", ((Timer) parent.getItemAtPosition(position)).id);
        startActivity(intent);
    }

    public void EditTimer(AdapterView<?> parent, int position)
    {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("id", ((Timer) parent.getItemAtPosition(position)).id);
        startActivity(intent);
    }

    public void DeleteTimer(AdapterView<?> parent, int position)
    {
        App.getInstance().getTimerDao().Delete(((Timer) parent.getItemAtPosition(position)));
    }

    public void AddTimer(Timer timer)
    {
        App.getInstance().getTimerDao().Insert(timer);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuItem menuItem = menu.add(0, 1, 0, getResources().getString(R.string.settings));
        menuItem.setIntent(new Intent(this, SettingsActivity.class));
        return super.onCreateOptionsMenu(menu);
    }
}