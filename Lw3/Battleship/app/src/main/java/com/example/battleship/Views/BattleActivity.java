package com.example.battleship.Views;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.battleship.Enums.MapType;
import com.example.battleship.Enums.ShotType;
import com.example.battleship.GameLogic.ArtificialIntelligence;
import com.example.battleship.GameLogic.GameState;
import com.example.battleship.GameLogic.Map;
import com.example.battleship.Helpers.AnimationHelper;
import com.example.battleship.Helpers.FirebaseHelper;
import com.example.battleship.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Random;

import es.dmoral.toasty.Toasty;

public class BattleActivity extends AppCompatActivity
{
    Vibrator vibrator;
    FirebaseHelper firebaseHelper;
    DatabaseReference firstMap;
    DatabaseReference secondMap;
    DatabaseReference firstUser;
    DatabaseReference secondUser;
    DatabaseReference moveCounter;
    ArtificialIntelligence AI;
    boolean isHost;
    boolean withAI;
    GameState gameState;
    int winnerCheck;
    String gameId;
    String firstName;
    String secondName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        AnimationHelper.StartAnimation(findViewById(R.id.container));
        winnerCheck = 0;
        gameId = getIntent().getStringExtra("gameId");
        isHost = getIntent().getBooleanExtra("isHost", false);
        withAI = getIntent().getBooleanExtra("withAI", false);
        gameState = GameState.WAITING;
        firebaseHelper = new FirebaseHelper();
        assert gameId != null;
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        firstMap = firebaseHelper.GetDatabaseReference("games").child(gameId).child("firstMap");
        secondMap = firebaseHelper.GetDatabaseReference("games").child(gameId).child("secondMap");
        firstUser = firebaseHelper.GetDatabaseReference("games").child(gameId).child("firstUser");
        firstUser.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                firstName = dataSnapshot.getValue(String.class);
                firstUser.removeEventListener(this);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        secondUser = firebaseHelper.GetDatabaseReference("games").child(gameId).child("secondUser");
        secondUser.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                secondName = dataSnapshot.getValue(String.class);
                secondUser.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        moveCounter = firebaseHelper.GetDatabaseReference("games").child(gameId).child("moveCounter");
        getSupportFragmentManager().beginTransaction().replace(R.id.firstMap, MapFragment.newInstance(MapType.PLAYER)).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.secondMap, MapFragment.newInstance(MapType.NOTACTIVE)).commit();
        if(withAI)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.secondMap, MapFragment.newInstance(MapType.ENEMY)).commit();
            firstMap.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    AI = new ArtificialIntelligence(new Gson().fromJson( dataSnapshot.getValue(String.class), new TypeToken<Map>(){}.getType()));
                    firstMap.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {
                }
            });
        }

        UpdateFirstMap();
       if(isHost)
       {
            WaitingEnemy();
       }
       if(!isHost)
       {
            gameState = GameState.PLAYING;
       }
        UpdateSecondMap();
        SwapSide();

    }

    public void UpdateGameState(ShotType shotType)
    {
        if (shotType == ShotType.MISS)
        {
            if (isHost)
            {
                moveCounter.setValue(false);
                if(withAI)
                {
                    AI.takeShot();
                    moveCounter.setValue(true);
                }
            }
            else
            {
                moveCounter.setValue(true);
            }
        }
        else if (shotType == ShotType.HIT)
        {
            winnerCheck++;
            if (vibrator.hasVibrator())
            {
                vibrator.vibrate(50);
            }
            if(winnerCheck==19)
            {
                moveCounter.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        boolean value = dataSnapshot.getValue(boolean.class);
                        if (isHost)
                        {
                            if (value)
                            {
                                Toasty.success(getBaseContext(), R.string.won, Toast.LENGTH_SHORT, true).show();
                                SaveGame();
                            }
                            else
                            {
                                Toasty.success(getBaseContext(),  R.string.lost, Toast.LENGTH_SHORT, true).show();
                            }
                        }
                        else
                        {
                            if (!value)
                            {
                                Toasty.success(getBaseContext(), R.string.won, Toast.LENGTH_SHORT, true).show();
                                SaveGame();
                            }
                            else
                            {
                                Toasty.success(getBaseContext(), R.string.lost, Toast.LENGTH_SHORT, true).show();
                            }
                        }
                        onBackPressed();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
            }
        }
        
        MapFragment mapFragment1 = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.firstMap);
        assert mapFragment1 != null;
        Map fmap = mapFragment1.map;

        MapFragment mapFragment2 = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.secondMap);
        assert mapFragment2 != null;
        Map smap = mapFragment2.map;

        Gson gson = new Gson();
        String jsonGrid1 = gson.toJson(fmap);
        if (isHost)
            if(withAI)
            {
                firstMap.setValue(gson.toJson(AI.getMap()));
            }
            else
            {
                firstMap.setValue(jsonGrid1);
            }
        else
        {
            secondMap.setValue(jsonGrid1);
        }
        String jsonGrid2 = gson.toJson(smap);
        if (isHost)
        {
            secondMap.setValue(jsonGrid2);
        }
        else
        {
            firstMap.setValue(jsonGrid2);
        }
    }

    private void SaveGame()
    {
        DatabaseReference stats =  firebaseHelper.GetDatabaseReference("gameStatistic").child(gameId + System.currentTimeMillis());
        stats.child("firstUser").setValue(firstName);
        stats.child("secondUser").setValue(secondName);
        stats.child("firstUserResult").setValue(1);
        stats.child("secondUserResult").setValue(0);
        stats.child("gameDate").setValue(System.currentTimeMillis());
    }

    private void WaitingEnemy()
    {
        secondMap.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String value = dataSnapshot.getValue(String.class);
                if(value == null)
                {
                   return;
                }
                gameState = GameState.PLAYING;
                MapFragment mapFragment1 = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.secondMap);
                assert mapFragment1 != null;
                mapFragment1.setMapType(MapType.ENEMY);
                moveCounter.setValue(true);
                //moveCounter.setValue(new Random().nextBoolean()); //AI превый
                Toasty.success(getBaseContext(), R.string.connect, Toast.LENGTH_SHORT, true).show();
                secondMap.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
    
    private void UpdateFirstMap()
    {
         firstMap.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String value = dataSnapshot.getValue(String.class);
                if (isHost)
                {
                    MapFragment mapFragment1 = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.firstMap);
                    getSupportFragmentManager().beginTransaction().replace(R.id.firstMap, MapFragment.newInstance(value, mapFragment1.getMapType())).commit();
                }
                else
                {
                    MapFragment mapFragment1 = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.secondMap);
                    getSupportFragmentManager().beginTransaction().replace(R.id.secondMap, MapFragment.newInstance(value, mapFragment1.getMapType())).commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void UpdateSecondMap()
    {
        secondMap.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String value = dataSnapshot.getValue(String.class);
                if (isHost)
                {
                    MapFragment mapFragment1 = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.secondMap);
                    getSupportFragmentManager().beginTransaction().replace(R.id.secondMap, MapFragment.newInstance((value), mapFragment1.getMapType())).commit();
                }
                else
                {
                    MapFragment mapFragment1 = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.firstMap);
                    getSupportFragmentManager().beginTransaction().replace(R.id.firstMap, MapFragment.newInstance((value), mapFragment1.getMapType())).commit();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){ }
        });
    }

    private void SwapSide()
    {
        moveCounter.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(gameState != GameState.WAITING)
                {
                    boolean value = dataSnapshot.getValue(boolean.class);
                    MapFragment mapFragment1 = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.secondMap);
                    assert mapFragment1 != null;
                    if (isHost)
                    {
                        MapType mapType = value ? MapType.ENEMY : MapType.NOTACTIVE;
                        mapFragment1.setMapType(mapType);
                    }
                    else
                    {
                        MapType mapType = !value ? MapType.ENEMY : MapType.NOTACTIVE;
                        mapFragment1.setMapType(mapType);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}
