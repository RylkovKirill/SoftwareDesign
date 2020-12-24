package com.example.battleship.Helpers;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;

public class AnimationHelper
{
    public static void StartAnimation(View view)
    {
        AnimationDrawable animationDrawable = (AnimationDrawable) view.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }
}
