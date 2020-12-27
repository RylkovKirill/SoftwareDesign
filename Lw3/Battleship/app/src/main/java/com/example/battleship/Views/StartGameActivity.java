package com.example.battleship.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import com.example.battleship.GameLogic.Map;
import com.example.battleship.GameLogic.MapHelpers.MapGenerator;
import com.example.battleship.GameLogic.MapHelpers.MapValidator;
import com.example.battleship.GameLogic.Ship;
import com.example.battleship.Enums.MapType;
import com.example.battleship.Models.Game;
import com.example.battleship.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

import com.example.battleship.Helpers.AnimationHelper;
import com.example.battleship.Helpers.FirebaseHelper;
import com.example.battleship.Helpers.IdGenerator;
import es.dmoral.toasty.Toasty;

public class StartGameActivity extends AppCompatActivity
{
    EditText gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
        AnimationHelper.StartAnimation(findViewById(R.id.container));

        boolean isHost = getIntent().getBooleanExtra("isHost", false);
        boolean withAI = getIntent().getBooleanExtra("withAI", false);

        gameId = findViewById(R.id.gameId);
        gameId.setText(IdGenerator.GenerateId());

        Button loadUserMap = findViewById(R.id.loadUserMap);
        loadUserMap.setOnClickListener(this::loadMap);

        Button generateRandomMap = findViewById(R.id.generateRandomMap);
        generateRandomMap.setOnClickListener(this::generateMap);

        Button newId = findViewById(R.id.newId);
        Button share = findViewById(R.id.share);
        if(isHost)
        {
            newId.setOnClickListener(v -> gameId.setText(IdGenerator.GenerateId()));
            share.setOnClickListener(v->
                    ShareCompat.IntentBuilder.from(this)
                            .setType("text/plain")
                            .setChooserTitle("Chooser title")
                            .setText(gameId.getText().toString())
                            .startChooser());
            if(withAI)
            {
                newId.setVisibility(View.GONE);
                share.setVisibility(View.GONE);
                gameId.setVisibility(View.GONE);
            }
        }
        else
        {
            newId.setVisibility(View.GONE);
            share.setVisibility(View.GONE);
            gameId.setText("");
            gameId.setHint(R.string.enterId);
        }

        Button newGame = findViewById(R.id.startGame);
        newGame.setOnClickListener(v-> {
            MapValidator mapValidator = new MapValidator();
            MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            assert mapFragment != null;
            Map map = mapFragment.map;
            if (mapValidator.isValid(map))
            {
                String id = gameId.getText().toString();
                String email =  Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
                ArrayList<Ship> ships = mapValidator.getCurrentShips();

                DatabaseReference game = new FirebaseHelper().GetDatabaseReference("games").child(id);

                ValueEventListener checkListener = new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        game.removeEventListener(this);
                        if (isHost && !withAI && !dataSnapshot.exists())
                        {
                            game.setValue(new Game(email,new Gson().toJson(map), new Gson().toJson(ships), null, null, null));
                        }
                        else if(!isHost && dataSnapshot.exists())
                        {
                            game.child("secondUser").setValue(email);
                            game.child("secondMap").setValue(new Gson().toJson(map));
                            game.child("secondUserShips").setValue(new Gson().toJson(ships));
                        }
                        else if(isHost && withAI && !dataSnapshot.exists())
                        {
                            game.setValue(new Game(email,new Gson().toJson(map), new Gson().toJson(ships), "AI", new Gson().toJson( new MapGenerator().GenerateRandomMap()), null));
                        }
                        StartGame(id, isHost, withAI);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                };
                game.addValueEventListener(checkListener);
            }
            else
            {
                Toasty.error(this, R.string.invalidMap, Toast.LENGTH_SHORT, true).show();
            }
        });


        getSupportFragmentManager().beginTransaction().replace(R.id.map, MapFragment.newInstance(new Gson().toJson( new Map()),MapType.CREATURE)).commit();
    }

    private void generateMap(View view)
    {
        MapGenerator mapGenerator = new MapGenerator();
        getSupportFragmentManager().beginTransaction().replace(R.id.map, MapFragment.newInstance(new Gson().toJson( mapGenerator.GenerateRandomMap()),MapType.CREATURE)).commit();
    }

    public void loadMap(View view)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String stringMap = sharedPreferences.getString("map", "");
        getSupportFragmentManager().beginTransaction().replace(R.id.map, MapFragment.newInstance(stringMap, MapType.CREATURE)).commit();
    }

    private void StartGame(String gameId, boolean isHost, Boolean withAI)
    {
        Intent intent = new Intent(this, BattleActivity.class);
        intent.putExtra("isHost", isHost);
        intent.putExtra("withAI", withAI);
        intent.putExtra("gameId", gameId);
        startActivity(intent);
    }
}