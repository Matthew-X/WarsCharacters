package com.glasswellapps.iact.character_screen.controllers;

import android.os.CountDownTimer;
import android.view.View;

public class ButtonPressedHandler {
    public static void onButtonPressed(View view) {
        new CountDownTimer(50, 50) {
            @Override
            public void onTick(long millisUntilFinished) {
                view.setAlpha(0.5f);
            }

            @Override
            public void onFinish() {
                view.animate().setDuration(100).alpha(1);
            }
        }.start();
    }
}
