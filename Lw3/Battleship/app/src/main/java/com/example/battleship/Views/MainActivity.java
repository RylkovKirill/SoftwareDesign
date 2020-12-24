package com.example.battleship.Views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.battleship.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import com.example.battleship.Helpers.AnimationHelper;
import in.goodiebag.carouselpicker.CarouselPicker;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnimationHelper.StartAnimation(findViewById(R.id.container));

        CarouselPicker carouselPicker = findViewById(R.id.carousel);

        Button currentAction = findViewById(R.id.currentAction);
        currentAction.setOnClickListener(view ->
        {
            switch (carouselPicker.getCurrentItem())
            {
                case 0:
                    CreateGame();
                    break;
                case 1:
                    ConnectToGame();
                    break;
                case 2:
                    PlayWithAI();
                    break;
                case 3:
                    ShowMap();
                    break;
                case 4:
                    ShowProfile();
                    break;
                case 5:
                    ShowStatistics();
                    break;
                case 6:
                    Logout();
                    break;

            }
        });

        List<CarouselPicker.PickerItem> imageItems = new ArrayList<>();

        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.add));
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.connect));
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.ai));
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.map));
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.profile));
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.statistics));
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.logout));

        CarouselPicker.CarouselViewAdapter imageAdapter = new CarouselPicker.CarouselViewAdapter(this, imageItems, 0);
        carouselPicker.setAdapter(imageAdapter);
        carouselPicker.setCurrentItem(3);
        currentAction.setText(R.string.map);

        carouselPicker.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position)
            {
                switch (carouselPicker.getCurrentItem())
                {
                    case 0:
                        currentAction.setText(R.string.add);
                        break;
                    case 1:
                        currentAction.setText(R.string.connect);
                        break;
                    case 2:
                        currentAction.setText(R.string.playWithAI);
                        break;
                    case 3:
                        currentAction.setText(R.string.map);
                        break;
                    case 4:
                        currentAction.setText(R.string.profile);
                        break;
                    case 5:
                        currentAction.setText(R.string.statistics);
                        break;
                    case 6:
                        currentAction.setText(R.string.logout);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) { }
        });
    }

    private void PlayWithAI()
    {
        Intent intent = new Intent(this, StartGameActivity.class);
        intent.putExtra("isHost", true);
        intent.putExtra("withAI", true);
        startActivity(intent);
    }

    private void CreateGame()
    {
        Intent intent = new Intent(this, StartGameActivity.class);
        intent.putExtra("isHost", true);
        intent.putExtra("withAI", false);
        startActivity(intent);
    }

    private void ConnectToGame()
    {
        Intent intent = new Intent(this, StartGameActivity.class);
        intent.putExtra("isHost", false);
        intent.putExtra("withAI", false);
        startActivity(intent);
    }

    private void ShowMap()
    {
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        startActivity(intent);
    }

    private void ShowProfile()
    {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void ShowStatistics()
    {
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
    }

    private void Logout()
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
