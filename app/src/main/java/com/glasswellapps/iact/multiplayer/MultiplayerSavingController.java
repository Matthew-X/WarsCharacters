package com.glasswellapps.iact.multiplayer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.annotation.NonNull;

import com.glasswellapps.iact.character_screen.controllers.SavingController;
import com.glasswellapps.iact.characters.Character;
import com.glasswellapps.iact.effects.WorkingAnimations;
import com.glasswellapps.iact.loading.CharacterHolder;
import com.glasswellapps.iact.loading.LoadingController;

public class MultiplayerSavingController extends SavingController {
    Activity context;
    SharedPreferences sharedPreferences;
    Player[] playerList;
    boolean isImperial;
    String name = "Party";

    public MultiplayerSavingController(@NonNull Activity context, @NonNull View saving_icon,
                                       Player[] playerList,
                                       boolean isImperial) {
        super(context, saving_icon);
        this.context = context;
        if(isImperial) {
            name = "Imperial";
        }
        sharedPreferences = getContext().getSharedPreferences(name,
                Context.MODE_PRIVATE);
        this.playerList = playerList;
        this.isImperial = isImperial;
    }

    public void loadPlayers(){
        onSavingStarted();
        int[] playerIDs = getPlayerIDs();
        Character[] loadedCharacters = LoadingController.Companion.loadCharactersByID(playerIDs,
                context);
        System.out.println(getPlayerIDs().length);
        System.out.println("LOAD " + loadedCharacters.length);
        for(int j = 0; j < playerIDs.length;j++) {
            for (int i = 0; i < loadedCharacters.length; i++) {
                if (loadedCharacters[i].getId() == playerIDs[j]) {
                    playerList[j].loadLocalCharacter(loadedCharacters[i], context);
                }
            }
        }
        onSavingFinished();
    }

    int[] getPlayerIDs(){
        int[] playerIDs = new int[4];
        for(int i = 0; i < 4;i++){
            playerIDs[i] = sharedPreferences.getInt(name+i,-1);
            System.out.println("GET ID " + playerIDs[i]);
        }
        return  playerIDs;
    }
    public void savePlayers(){
        CharacterHolder.clearParty();
        for (int i = 0; i < playerList.length; i++) {
            Player player = playerList[i];
            if(player.isLocal && player.getCharacter()!=null){
                player.getCharacter().setFile_name(name +(i+1));
                CharacterHolder.addToParty(player.getCharacter());
            }
        }
        quickSave();
    }



    @Override
    protected void onSavingStarted() {
        if(isImperial) {
            WorkingAnimations.Companion.startGearAnimation(getSaving_icon());
        }
        else{
            WorkingAnimations.Companion.startSpinningAnimation(getSaving_icon());
        }
    }

    @Override
    protected void onSavingFinished(){
        super.onSavingFinished();
        commitToSharedPreferences(playerList);
    }

    void commitToSharedPreferences(Player[] playerList){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < playerList.length; i++) {
            Player player = playerList[i];
            if(player.isLocal && player.getCharacter()!=null){
                editor.putInt(name+i, player.getCharacter().getId());
                System.out.println("SAVE ID "+player.getCharacter().getId());
            }
            else{
                editor.putInt(name+i, -1);

            }
        }
        editor.commit();
    }
}
