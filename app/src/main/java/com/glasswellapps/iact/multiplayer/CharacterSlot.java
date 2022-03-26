package com.glasswellapps.iact.multiplayer;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.glasswellapps.iact.R;
import com.glasswellapps.iact.character_screen.controllers.SavingController;
import com.glasswellapps.iact.characters.Character;
import com.glasswellapps.iact.effects.Sounds;

public class CharacterSlot {
    TextView playerDetected;
    View playerView;
    Player player;
    int playerNumber;
    SavingController savingController;
    MultiplayerScreen screen;

    public CharacterSlot(int playerNumber, FrameLayout container,
                         TextView playerDetected, Player[] playerList, SavingController
                                savingController, MultiplayerScreen screen) {
        this.playerNumber = playerNumber;
        this.playerDetected = playerDetected;
        this.savingController = savingController;
        this.screen = screen;

        playerView = screen.getLayoutInflater().inflate(R.layout.fragment_multiplayer_character, container);
        playerView.setAlpha(0);
        player = new Player(playerView,screen);
        playerList[playerNumber] = player;
        playerView.findViewById(R.id.mp_options2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptions();
            }
        });
        playerView.findViewById(R.id.mp_options).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptions();
            }
        });
        player.addObserver(screen);
    }

    private void onOptions(){
        if(!isPlayerAdded()){
            return;
        }
        screen.options.onShowDialog(player,playerNumber);
    }

    boolean isPlayerAdded(){
        if (player.character == null){
            screen.showPlayerAdder(playerNumber);
            return false;
        }
        return  true;
    }

    public void onNewPlayerAdded(){
        onShowPlayer();
    }
    public void remove() {
        player.remove();
        hide();
    }

    public void onShowPlayer() {
        player.show();
        Sounds.INSTANCE.buttonSound();
    }

    public void showPlayerDetected() {
        //playerDetected.setText("ACTIVATE HERO");
        playerDetected.animate().setDuration(150).alpha(1f);
    }

    public void hide(){
        //playerDetected.setText("ADD HERO");
        playerDetected.animate().setDuration(150).alpha(0f);
    }

    public void show() {
        if(player.getCharacter()!=null){
            player.show();
        }
    }

    public void updateView() {
        if(player.getCharacter()!=null){
            player.updateView();
        }
    }


    public Player getPlayer(){
        return player;
    }
    public boolean equalsID(String id){
        return player.getId().equals(id);
    }
    public boolean getIsWounded(){
        Character character = player.getCharacter();
        if(character!=null){
            return character.isWounded();
        }
        return true;
    }

    public void updateViewOnWindowChanged() {

        updateView();
    }
}
