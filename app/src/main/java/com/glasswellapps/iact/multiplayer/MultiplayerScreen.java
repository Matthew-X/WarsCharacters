package com.glasswellapps.iact.multiplayer;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;
import com.glasswellapps.iact.CardDisplay;
import com.glasswellapps.iact.R;
import com.glasswellapps.iact.ShortToast;
import com.glasswellapps.iact.character_screen.controllers.ButtonPressedHandler;
import com.glasswellapps.iact.effects.Sounds;
import com.glasswellapps.iact.loading.CharacterHolder;

import java.util.Observable;
import java.util.Observer;

public abstract class MultiplayerScreen extends AppCompatActivity implements Observer {
    SeekBar volumeSlider;
    CharacterSlot[] characterSlots = new CharacterSlot[4];
    Player[] playerList = new Player[4];
    CardDisplay cardDisplay;
    Options options;
    PlayerAdder playerAdder;
    MultiplayerSavingController savingController;
    SoundBoard soundBoard;
    boolean isImperial = false;

    public int playerToBeAdded = -1;
    protected void init(){
        initDialogs();
        initSaving();
        initCharacterSlots();
        initSoundboard();
        initNextMission();
        savingController.loadPlayers();
        for(int i = 0; i < 4; i++) {
            characterSlots[i].show();
        }
    }

    private void initDialogs() {
        cardDisplay = new CardDisplay(this);
        options = new Options(this, isImperial);
        playerAdder = new PlayerAdder(this);
    }
    private void initSoundboard() {
        soundBoard = new SoundBoard(
                this,
                findViewById(R.id.sound_blaster),
                findViewById(R.id.sound_melee),
                findViewById(R.id.sound_alien),
                findViewById(R.id.sound_creature),
                findViewById(R.id.sound_droid),
                findViewById(R.id.sound_droid_death),
                findViewById(R.id.sound_trooper),
                findViewById(R.id.sound_trooper_death),
                findViewById(R.id.sound_move),
                findViewById(R.id.sound_terminal),
                findViewById(R.id.sound_door),
                findViewById(R.id.sound_crate),
                findViewById(R.id.sound_lightsaber_attack),
                findViewById(R.id.sound_lightsaber),
                true);

        volumeSlider = findViewById(R.id.imperial_volume_slider);
        volumeSlider.setProgress(50);
        Sounds.INSTANCE.setSoundVolume(0.5f);
        volumeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Sounds.INSTANCE.setSoundVolume((float)i/100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    void addPlayer(){


    }
    void initCharacterSlots(){

        characterSlots[0] = new CharacterSlot(
                0,
                findViewById(R.id.player0),
                findViewById(R.id.rebel_detected_text0),
                playerList, savingController,this);
        characterSlots[1] = new CharacterSlot(
                1,
                findViewById(R.id.player1),
                findViewById(R.id.rebel_detected_text1),
                playerList, savingController,this);
        characterSlots[2] = new CharacterSlot(
                2,
                findViewById(R.id.player2),
                findViewById(R.id.rebel_detected_text2)
                ,playerList, savingController,this);
        characterSlots[3] = new CharacterSlot(
                3,
                findViewById(R.id.player3),
                findViewById(R.id.rebel_detected_text3)
                ,playerList, savingController,this);

    }

    void initSaving(){
        ImageView savingIcon  = findViewById(R.id.mp_saving_icon);
        savingController = new MultiplayerSavingController(this, savingIcon, playerList
                ,isImperial);
    }

    public void savePlayers() {
        savingController.savePlayers();
    }

    public void showOptions(int playerNumber) {
        options.onShowDialog(playerList[playerNumber],playerNumber);
    }

    public void showPlayerAdder(int playerNumber) {
        playerAdder.showDialog(playerNumber);
    }

    public void onPlayerRemoved(int playerNumber){
        characterSlots[playerNumber].remove();
        savingController.savePlayers();
    }
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(playerToBeAdded != -1){
           onNewPlayerAdded();
        }
        updateViewOnWindowChanged();
    }

    protected void onNewPlayerAdded(){
        if(CharacterHolder.getActiveCharacter()!=null) {
            characterSlots[playerToBeAdded].player.loadLocalCharacter(CharacterHolder.getActiveCharacter(),this);
            characterSlots[playerToBeAdded].onNewPlayerAdded();
            CharacterHolder.setActiveCharacter(null);
            playerToBeAdded = -1;
        }
    }
    void updateViewOnWindowChanged(){
        if(characterSlots == null){
            return;
        }
        for(int i = 0;i<characterSlots.length;i++){
            characterSlots[i].updateViewOnWindowChanged();
        }
    }
    public void update(Observable observable, Object o) {
        if(observable instanceof Player){
            checkIsGameOver();
            savingController.quickSave();
        }
    }

    public void updateParty(){

    }

    protected void onGameOver(){
        System.out.println("GAMEOVER");
        isGameOver = true;
        //if(new Random().nextFloat() < 0.5f) {
        Sounds.INSTANCE.play(Sounds.INSTANCE.getEmperor_laugh(),2);
        /*}
        else{
            Sounds.INSTANCE.play(Sounds.INSTANCE.getEmperor_laugh1());
        }*/
        Dialog gameOverDialog = new Dialog(this);
        gameOverDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gameOverDialog.setCancelable(false);
        gameOverDialog.setContentView(R.layout.dialog_gameover);
        gameOverDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        gameOverDialog.findViewById(R.id.continue_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sounds.INSTANCE.selectSound();
                gameOverDialog.dismiss();
                nextMissionDialog.show();
            }
        });
        View gameOver = gameOverDialog.findViewById(R.id.gameover_view);
        gameOver.setAlpha(0);
        gameOverDialog.show();
        gameOver.animate().setDuration(4000).alpha(1);
        savingController.quickSave();
    }

    @Override
    public void onBackPressed() {
        savingController.savePlayers();
        soundBoard.onBackPressed();
        for (Player player:playerList) {
            player.onStop();
        }
        super.onBackPressed();
        finish();
    }
    @Override
    protected void onStop() {
        savingController.savePlayers();
        super.onStop();
        for( Player player: playerList){
            if(player!=null) {
                player.onNavigate();
            }
        }
        //stopService();
    }

    boolean isGameOver = false;
    public void checkIsGameOver(){
        boolean allWounded = true;
        for(CharacterSlot slot : characterSlots){
            System.out.println(slot.getIsWounded());
            if(!slot.getIsWounded()){
                allWounded = false;
            }
        }
        if(isGameOver){
            if(!allWounded) isGameOver = false;
        }
        else {
            if(allWounded) onGameOver();
        }
    }

    Dialog nextMissionDialog;
    private void initNextMission(){
        nextMissionDialog = new Dialog(this);
        findViewById(R.id.end_mission_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextMissionButton();
            }
        });

        nextMissionDialog.requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        nextMissionDialog.setCancelable(false);
        nextMissionDialog.setContentView(R.layout.dialog_next_mission);
        nextMissionDialog.setCanceledOnTouchOutside(true);
        nextMissionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        nextMissionDialog.findViewById(R.id.next_mission_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextMission();
            }
        });
        nextMissionDialog.findViewById(R.id.next_mission_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextMissionNo();
            }
        });
    }
    private void onNextMissionButton(){
        Sounds.INSTANCE.selectSound();
        ButtonPressedHandler.onButtonPressed(findViewById(R.id.end_mission_button));
        nextMissionDialog.show();
    }

    private void onNextMission() {
        Sounds.INSTANCE.selectSound();
        for (Player player: playerList) {
            player.onNextMission();
        }
        nextMissionDialog.dismiss();
        savingController.quickSave();
    }
    private void onNextMissionNo() {
        Sounds.INSTANCE.selectSound();
        nextMissionDialog.dismiss();
    }
}