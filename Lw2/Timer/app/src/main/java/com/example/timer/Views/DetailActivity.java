package com.example.timer.Views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.example.timer.App;
import com.example.timer.Entities.Action;
import com.example.timer.Entities.Timer;
import com.example.timer.Helpers.SettingsHelper;
import com.example.timer.R;
import com.example.timer.Services.VibrationService;
import com.example.timer.ViewModels.ActionListViewModel;
import com.example.timer.Views.Adapters.ActionAdapter;
import com.example.timer.Views.Adapters.AlertDialogAdapter;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity
{
    Timer timer;
    ActionListViewModel detailViewModel;

    ListView actionListView;
    ActionAdapter actionAdapter;

    List<String> dialogActions;
    AlertDialogAdapter actionsOnActions;

    View addAction;
    View startTimer;
    View saveChanges;

    View colorPicker;
    View deleteAllAction;

    EditText timerName;
    EditText timerDescription;
    Context context = this;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setTheme(SettingsHelper.Theme);
        getBaseContext().getResources().updateConfiguration(SettingsHelper.configuration, null);
        dialogActions = Arrays.asList(getResources().getString(R.string.createCopy),getResources().getString(R.string.reset),getResources().getString(R.string.delete));
        actionsOnActions = new AlertDialogAdapter(context, android.R.layout.select_dialog_item,dialogActions);
        setContentView(R.layout.activity_detail);

        actionListView = findViewById(R.id.actListView);

        actionListView.setOnItemLongClickListener((parent, view, position, id) -> {

            Intent intentVibrate = new Intent(getApplicationContext(), VibrationService.class);
            startService(intentVibrate);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setNegativeButton("Cancel", null);
            builder.setAdapter(actionsOnActions, (dialog, which) ->
            {
                switch (which)
                {
                    case 0:
                        Action action =new Action( (Action) parent.getItemAtPosition(position));
                        AddAction(action);
                        break;
                    case 1:
                        ResetAction(parent, position);
                        break;
                    case 2:
                        DeleteAction(parent, position);
                        break;
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return  true;
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            timer= App.getInstance().getTimerDao().Get(extras.getInt("id"));
        }
        addAction = findViewById(R.id.addActoin);
        addAction.setOnClickListener(view -> AddAction());

        startTimer = findViewById(R.id.startTimer);
        startTimer.setOnClickListener(view -> StartTimer(timer.id));

        saveChanges = findViewById(R.id.saveChanges);
        saveChanges.setOnClickListener(view -> UpdateTimer(timer));

        timerName = findViewById(R.id.timerName);
        timerName.setText(timer.name);

        timerDescription = findViewById(R.id.timerDescription);
        timerDescription.setText(timer.description);

        colorPicker = findViewById(R.id.colorPicker);
        colorPicker.setBackgroundTintList(ColorStateList.valueOf(timer.color));

        deleteAllAction = findViewById(R.id.deleteAllActions);
        deleteAllAction.setOnClickListener(view -> App.getInstance().getActionDao().DeleteAllByTimerId(timer.id));
        actionAdapter = new ActionAdapter(context, R.layout.action_list_item, App.getInstance().getActionDao().GetAllByTimerId(timer.id));
        actionListView.setAdapter(actionAdapter);
        detailViewModel = new ViewModelProvider(this).get(ActionListViewModel.class);

        detailViewModel.getActions().observe(this, actions -> {
            actionAdapter = new ActionAdapter(context, R.layout.action_list_item, App.getInstance().getActionDao().GetAllByTimerId(timer.id));
            actionListView.setAdapter(actionAdapter);
        });
    }

    public void DeleteAction(AdapterView<?> parent, int position)
    {
        App.getInstance().getActionDao().Delete((Action) parent.getItemAtPosition(position));
    }

    public void ResetAction(AdapterView<?> parent, int position)
    {
        Action action = (Action) parent.getItemAtPosition(position);
        action.name="Timer action";
        action.secondsNumber = 30;
        App.getInstance().getActionDao().Insert(action);
    }

    public void AddAction(Action action)
    {
        App.getInstance().getActionDao().Insert(action);
    }

    public void AddAction()
    {
        Action action = new Action("Timer action", 30 , timer.id);
        App.getInstance().getActionDao().Insert(action);
    }

    public  void StartTimer(int id)
    {
        Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("color", timer.color);
        startActivity(intent);
    }

    public void UpdateTimer(Timer timer)
    {
        timer.name = timerName.getText().toString();
        timer.description = timerDescription.getText().toString();
        App.getInstance().getTimerDao().Update(timer);
    }

    public void SetColor(View view)
    {
        ColorPickerDialogBuilder
                .with(context)
                .setTitle(getResources().getString(R.string.сhooseСolor))
                .initialColor(timer.color)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(selectedColor -> {
                })
                .setPositiveButton("ok", (dialog, selectedColor, allColors) -> {
                    timer.color = selectedColor;
                    //UpdateTimer(timer);
                    colorPicker.setBackgroundTintList(ColorStateList.valueOf(timer.color));
                })
                .setNegativeButton("cancel", (dialog, which) -> {
                })
                .build()
                .show();
    }
}