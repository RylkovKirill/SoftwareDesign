package com.example.timer.Views;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.example.timer.App;
import com.example.timer.Entities.Action;
import com.example.timer.Entities.Timer;
import com.example.timer.Enums.TimerStatus;
import com.example.timer.Helpers.SettingsHelper;
import com.example.timer.R;
import com.example.timer.Services.NotificationService;
import com.example.timer.ViewModels.TimerViewModel;
import com.example.timer.Views.Adapters.TimerPageAdapter;

import java.util.List;
import java.util.Objects;

public class TimerActivity extends AppCompatActivity {

    Timer timer;
    List<Action> actionList;
    ListView actListView;

    View next;
    View pause;
    View reset;
    View previous;
    TextView actionName;
    TextView secondNumber;
    TimerViewModel timerViewModel;

    TimerPageAdapter timerPageAdapter;

    NotificationService timerService;
    ServiceConnection serviceConnection;
    Intent intent;

    private final String timer_action = "TIMER_ACTION";
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onStop()
    {
        super.onStop();
        unbindService(serviceConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(intent,serviceConnection,0);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsHelper.SetSettings(PreferenceManager.getDefaultSharedPreferences(this));
        setTheme(SettingsHelper.Theme);
        getBaseContext().getResources().updateConfiguration(SettingsHelper.configuration, null);

        setContentView(R.layout.activity_timer);

        timerViewModel = new ViewModelProvider(this).get(TimerViewModel.class);

        actListView = findViewById(R.id.actionList);

        next  = findViewById(R.id.nextAction);
        pause = findViewById(R.id.pauseTimer);
        reset = findViewById(R.id.resetTimer);
        previous = findViewById(R.id.previousAction);

        actionName = findViewById(R.id.ActionName);
        secondNumber = findViewById(R.id.secondNumber);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                timerService = ((NotificationService.MyBinder) iBinder).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                timerViewModel.setTime(Objects.requireNonNull(intent.getExtras()).getInt("currentTime"));
                actionName.setText(intent.getExtras().getString("actionName"));
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(timer_action));
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            timer = App.getInstance().getTimerDao().Get(extras.getInt("id"));
            actionList = App.getInstance().getActionDao().GetAllByTimerId(timer.id);
        }

        View timerView = findViewById(R.id.timer);
        timerView.setBackgroundColor(timer.color);

        timerPageAdapter = new TimerPageAdapter(this, R.layout.timer_action_item, actionList);
        actListView.setAdapter(timerPageAdapter);

        intent = new Intent(this, NotificationService.class);
        intent.putExtra("timerId", timer.id);

        if(!timerViewModel.IsActionStart()){
            StartTimerService();
        }

        timerViewModel.getPosition().observe(this, integer -> {
            if (timerService != null){
                timerService.setCurrentActionNumber(integer);
            }
        });

        timerViewModel.getTime().observe(this, integer -> secondNumber.setText(integer.toString()));

        actListView.setOnItemClickListener((adapterView, view, i, l) -> timerViewModel.setPosition(i));


        next.setOnClickListener(view -> timerService.NextAction());
        pause.setOnClickListener(view -> timerService.PauseTimer());
        reset.setOnClickListener(view -> timerService.ResetTimer());
        previous.setOnClickListener(view -> timerService.PreviousAction());
    }

    public void StartTimerService()
    {
        timerViewModel.setTimerStatus(TimerStatus.STARTED);
        startService(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (timerService != null){
            timerService.PauseTimer();
        }
        stopService(intent);
    }
}