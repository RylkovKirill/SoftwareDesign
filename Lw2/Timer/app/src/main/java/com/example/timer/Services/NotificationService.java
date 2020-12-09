package com.example.timer.Services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.Toast;

import com.example.timer.App;
import com.example.timer.Entities.Action;
import com.example.timer.Entities.Timer;
import com.example.timer.Enums.TimerStatus;
import com.example.timer.R;

import java.util.List;

public class NotificationService extends Service
{
    public  Timer timer;
    public List<Action> actions;
    public int currentActionNumber = 0;
    public TimerStatus timerStatus = TimerStatus.WAITING;
    public int remainder;

    public MediaPlayer player;
    public CountDownTimer countDownTimer;
    public MyBinder binder = new MyBinder();

    public void setCurrentActionNumber(int currentActionNumber)
    {
        this.currentActionNumber = currentActionNumber;
        Action currentAction = actions.get(currentActionNumber);
        StartTimer(currentAction.secondsNumber, currentAction.name);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate()
    {
        player = MediaPlayer.create(this, R.raw.notification);
    }

    @Override
    public void onDestroy()
    {
        player.stop();
    }

    @Override
    public void onStart(Intent intent, int startid)
    {
        timer = App.getInstance().getTimerDao().Get(intent.getExtras().getInt("timerId", -1));
        actions = App.getInstance().getActionDao().GetAllByTimerId(timer.id);
        if(actions.size() == 0)
        {
            player = (MediaPlayer.create(getApplicationContext(), R.raw.final_notification));
            Toast.makeText(getApplicationContext(), timer.name, Toast.LENGTH_LONG).show();
            player.start();
        }
        else
        {
            Action currentAction = actions.get(currentActionNumber);
            timerStatus = TimerStatus.STARTED;
            StartTimer(currentAction.secondsNumber, currentAction.name);
        }
    }

    private void StartTimer(int secondsNumber, String actionName)
    {
        if(countDownTimer != null)
        {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(secondsNumber * 1000, 1000)
        {
            @Override
            public void onTick(long time)
            {
                remainder = (int)(time/1000);
                Intent intent = new Intent("TIMER_ACTION");
                intent.putExtra("currentTime", remainder);
                intent.putExtra("actionName", actionName);
                sendBroadcast(intent);
            }

            @Override
            public void onFinish()
            {
                if (currentActionNumber < actions.size() - 1)
                {
                    Toast.makeText(getApplicationContext(), actionName, Toast.LENGTH_SHORT).show();
                    setCurrentActionNumber(++currentActionNumber);
                }
                else
                 {
                    player = (MediaPlayer.create(getApplicationContext(), R.raw.final_notification));
                    Toast.makeText(getApplicationContext(), timer.name, Toast.LENGTH_LONG).show();
                }
                player.start();
            }
        };
        countDownTimer.start();
    }

    public void PreviousAction()
    {
        if (timerStatus == TimerStatus.STARTED)
        {
            if(currentActionNumber > 0)
            {
                setCurrentActionNumber(--currentActionNumber);
            }
        }
    }

    public void PauseTimer()
    {
        if (timerStatus == TimerStatus.STARTED)
        {
            if(countDownTimer != null)
            {
                countDownTimer.cancel();
                timerStatus = TimerStatus.STOPPED;
            }
        }
        else
        {
            Action currentAction = actions.get(currentActionNumber);
            StartTimer(remainder, currentAction.name);
            timerStatus = TimerStatus.STARTED;
        }
    }

    public void ResetTimer()
    {
        setCurrentActionNumber(0);
    }

    public void NextAction()
    {
        if (timerStatus == TimerStatus.STARTED)
        {
            if(currentActionNumber < actions.size() - 1)
            {
                setCurrentActionNumber(++currentActionNumber);
            }
        }
    }

    public class MyBinder extends Binder
    {
        public NotificationService getService()
        {
            return NotificationService.this;
        }
    }
}

