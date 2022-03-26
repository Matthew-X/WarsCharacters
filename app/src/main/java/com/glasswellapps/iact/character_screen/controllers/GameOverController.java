package com.glasswellapps.iact.character_screen.controllers;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;

import com.glasswellapps.iact.R;
import com.glasswellapps.iact.effects.Sounds;
import com.glasswellapps.iact.multiplayer.Player;

import java.util.Observable;

public class GameOverController extends Observable {
    boolean isGameOver = false;
    Activity context;
    ActionController actionController;
    public GameOverController(Activity context, ActionController actionController) {
        this.context = context;
        this.actionController = actionController;
    }

    public void onGameOver(){
        System.out.println("GAMEOVER");
        isGameOver = true;
        Sounds.INSTANCE.play(Sounds.INSTANCE.getEmperor_laugh(),2);
        Dialog gameOverDialog = new Dialog(context);
        gameOverDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gameOverDialog.setCancelable(false);
        gameOverDialog.setContentView(R.layout.dialog_gameover);
        gameOverDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        gameOverDialog.findViewById(R.id.continue_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sounds.INSTANCE.selectSound();
                gameOverDialog.dismiss();
                actionController.showNextMission();
            }
        });
        View gameOver = gameOverDialog.findViewById(R.id.gameover_view);
        gameOver.setAlpha(0);
        gameOverDialog.show();
        gameOver.animate().setDuration(4000).alpha(1);
    }
}
