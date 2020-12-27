package com.example.battleship.Views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.battleship.GameLogic.Map;
import com.example.battleship.GameLogic.MapHelpers.MapValidator;
import com.example.battleship.Enums.MapType;
import com.example.battleship.R;
import com.google.gson.Gson;

import com.example.battleship.Helpers.AnimationHelper;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class MapActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        AnimationHelper.StartAnimation(findViewById(R.id.container));

        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String stringMap = sharedPreferences.getString("map", "");

        getSupportFragmentManager().beginTransaction().replace(R.id.map, MapFragment.newInstance(stringMap, MapType.CREATURE)).commit();
        //getSupportFragmentManager().beginTransaction().add(R.id.map, MapFragment.newInstance(stringMap, MapType.Creation)).commit();

        Button saveMap = findViewById(R.id.saveMap);
        saveMap.setOnClickListener(v->
        {
            Map map = ((MapFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.map))).map;
            if(new MapValidator().isValid(map))
            {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("map", new Gson().toJson(map));
                editor.apply();
            }
            else
            {
                Toasty.error(this, R.string.invalidMap, Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}
