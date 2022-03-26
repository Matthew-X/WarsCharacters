package com.glasswellapps.iact.multiplayer;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.glasswellapps.iact.R;
import com.glasswellapps.iact.effects.Sounds;
import com.glasswellapps.iact.inventory.Items;

import java.util.ArrayList;
import java.util.Random;

public class RebelScreen extends MultiplayerScreen {
    ImageView[] playerNumberButtons = new ImageView[4];
    int playerCount = 4;
    View[] playerViews = new View[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rebel_screen);
        init();
    }
    @Override
    protected void init(){
        isImperial = false;
        super.init();
        initPlayerNumberButtons();

    }
    private void handlePlayerCount(){
        handPlayerButtonViews();
        handlePlayerViews();
        applyHandicap();
        showStatus();
    }
    private void showStatus(){
        switch (playerCount){
            case 0: ((TextView) findViewById(R.id.rebel_status_text)).setText(getOnePlayerText());break;
            case 1:((TextView) findViewById(R.id.rebel_status_text)).setText("LEGENDARY");break;
            case 2:((TextView) findViewById(R.id.rebel_status_text)).setText("HEROIC");break;
            case 3: ((TextView) findViewById(R.id.rebel_status_text)).setText("FULL PARTY");break;
        }
    }

    String getOnePlayerText(){
        double random = Math.random();
        String text = "HEROIC & LEGENDARY";
        if (random < 0.25) {
            text = "CHOSEN ONE";
        }
        else if(random < 0.4){
            text = "CAN SOLO";
        }

        return text;
    }

    @Override
    protected void onNewPlayerAdded() {
        super.onNewPlayerAdded();
        handlePlayerCount();
    }

    private  void handPlayerButtonViews(){
        for(int i = 0; i < characterSlots.length;i++) {
            if (playerCount == i) {
                playerNumberButtons[i].animate().alpha(1);
            } else {
                playerNumberButtons[i].animate().alpha(0.5f);
            }
        }
    }
    private void handlePlayerViews(){

        float width = playerViews[0].getWidth();
        float playerPosition =  width*1.5f - playerCount*width*1.5f/3f;
        for(int i = 0; i < characterSlots.length;i++){
            View characterSlotView =  (View)playerViews[i].getParent();
            characterSlotView.animate().alpha(i<playerCount+1?1:0);
            characterSlotView.animate().translationX(playerPosition + i*width);
        }
    }
    private  void applyHandicap(){
        for(int i = 0; i < characterSlots.length;i++) {
            if(characterSlots[i].player.character == null){
                continue;
            }
             ArrayList<Integer> rewards =  characterSlots[i].player.character.getRewards();
             if(playerCount == 0) {
                 equipReward(Items.heroic, rewards);
                 equipReward(Items.legendary, rewards);

             }
             else if (playerCount == 1){
                 equipReward(Items.legendary, rewards);
                 unequipReward(Items.heroic, rewards);

             }
             else if (playerCount == 2){
                 equipReward(Items.heroic, rewards);
                 unequipReward(Items.legendary, rewards);

             }
             else if (playerCount == 3){
                 unequipReward(Items.heroic, rewards);
                 unequipReward(Items.legendary, rewards);

             }
            characterSlots[i].player.updateView();
        }

    }

    private void equipReward(int rewardIndex, ArrayList<Integer> rewards) {
        if (!rewards.contains(rewardIndex))
            rewards.add(rewardIndex);
    }
    private void unequipReward(int rewardIndex, ArrayList<Integer> rewards){
        if (rewards.indexOf(rewardIndex) > -1)
            rewards.remove (rewards.indexOf(rewardIndex));
    }

    boolean loaded = false;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus && !loaded){
            playerCount = 0;
            for(int i = 0; i < 4; i++) {
                playerViews[i] = characterSlots[i].playerView;
            }
            for(int i = 0; i < 4; i++){
                if(characterSlots[i].getPlayer().character!=null){
                    System.out.println("sdfsdf");
                    View emptySlot = playerViews[playerCount];
                    playerViews[playerCount] = playerViews[i];
                    playerViews[i] = emptySlot;
                    playerCount++;
                }
            }
            if(playerCount!=0){
                playerCount--;
            }
            else{
                playerCount = 3;
            }
            handlePlayerCount();
            loaded = true;
        }
    }




    private void initPlayerNumberButtons(){
        playerNumberButtons[0] = findViewById(R.id.one_player);
        playerNumberButtons[1] = findViewById(R.id.two_player);
        playerNumberButtons[2] = findViewById(R.id.three_player);
        playerNumberButtons[3] = findViewById(R.id.four_player);
        playerNumberButtons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sounds.INSTANCE.selectSound();
                playerToBeAdded = 0;
                handlePlayerCount();
            }
        });
        playerNumberButtons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sounds.INSTANCE.selectSound();
                playerCount = 1;
                handlePlayerCount();
            }
        });
        playerNumberButtons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sounds.INSTANCE.selectSound();
                playerCount = 2;
                handlePlayerCount();
            }
        });
        playerNumberButtons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sounds.INSTANCE.selectSound();
                playerCount = 3;
                handlePlayerCount();
            }
        });
    }
}
