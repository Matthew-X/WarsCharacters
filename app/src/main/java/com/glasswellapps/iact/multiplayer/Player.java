package com.glasswellapps.iact.multiplayer;
import android.app.Activity;
import android.view.View;

import com.glasswellapps.iact.character_screen.types.EffectTypes;
import com.glasswellapps.iact.effects.Sounds;
import com.glasswellapps.iact.character_screen.CharacterBuilder;
import com.glasswellapps.iact.character_screen.controllers.BackgroundController;
import com.glasswellapps.iact.characters.Character;
import com.glasswellapps.iact.loading.CharacterHolder;

import java.util.Observable;

public class Player extends Observable {
    String id = "";
    Character character;
    Activity context;
    public boolean isLocal;
    PlayerView playerView;
    public Player(View view, MultiplayerScreen context) {
        playerView = new PlayerView(view, context);
        playerView.getActivationButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIsNotUpdatable() || !playerView.isVisible){return;}
                character.setActivated(!character.isActivated());
                playerView.activated(character.isActivated());
            }
        });
        playerView.getDamageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIsNotUpdatable() || !playerView.isVisible){return;}
                onAddDamage();
            }
        });

        playerView.getMinusDamageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIsNotUpdatable() || !playerView.isVisible ){return;}
                onMinusDamage();
            }
        });
        playerView.getStrainView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIsNotUpdatable() || !playerView.isVisible){return;}
                onAddStrain();
            }
        });
        playerView.getMinusStrainView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIsNotUpdatable() || !playerView.isVisible){return;}
                onMinusStrain();
            }
        });
    }

    public String getId(){
        return id;
    }
    public void newCharacter(byte[] message, Activity context) {
        this.context = context;
        getID(message);
        int characterIndex = message[Codes.CHARACTER];
        character = CharacterBuilder.Companion.create(characterIndex,context);
        character.loadImages(context);
        isLocal = false;
        updateBackground(message,true);
        onNewCharacterAdded();
        updateCharacter(message);
    }
    public int getCharacterIndex(){
        return character.getIndex();
    }
    public PlayerView getView(){
        return playerView;
    }
    public Character getCharacter(){
        return  character;
    }
    void onNewCharacterAdded(){
        playerView.nameView.setText(character.getName());
        playerView.setDefenceDice(character);
        character.setWounded(character.getDamage()>=character.getHealth());
        character.setWithdrawn(character.getDamage() == character.getHealth()*2);

    }

    public void updateCharacter(byte[] message) {
        System.out.println("ID "+id);
        updateXP(message);
        updateWeapons(message);
        updateArmour(message);
        updateAccessories(message);
        updateRewards(message);
        updateBackground(message,false);
        updateIsActivated(message);
        updateConditions(message);
        updateDamageStrain(message);

        playerView.updateStats(character);
        if(message[Codes.UPDATE_IMAGES]==(byte)1){
            playerView.updateImages(character);
        }
    }
    void getID(byte[] message){
        byte[] byteID = new byte[MessageLength.ID];
        for(int i = 0; i < 4;i++){
            byteID[i] = message[i];
        }
        id = new String(byteID);
        System.out.println("RECEIVED ID " + id);
    }

    private void updateAccessories(byte[] message) {
        character.getAccessories().clear();
        int acc1 = message[Codes.ACC];
        int acc2 = message[Codes.ACC+1];
        int acc3 = message[Codes.ACC+2];
        if(acc1 != Codes.EMPTY){
            character.getAccessories().add(acc1);
        }
        if(acc2 != Codes.EMPTY){
            character.getAccessories().add(acc2);
        }
        if(acc3 != Codes.EMPTY){
            character.getAccessories().add(acc3);
        }
    }
    private void updateArmour(byte[] message) {
        int armour =  message[Codes.ARMOUR];
        character.getArmor().clear();
        if(armour != Codes.EMPTY){
            character.getArmor().add(armour);
        }
    }

    private void updateWeapons(byte[] message){
        character.getWeapons().clear();
        int weapon1 = message[Codes.WEAPONS];
        int weapon2 = message[Codes.WEAPONS+1];
        if(weapon1 != Codes.EMPTY){
            character.getWeapons().add(weapon1);
        }
        if(weapon2 != Codes.EMPTY){
            character.getWeapons().add(weapon2);
        }
    }

    private void updateRewards(byte[] message){
        character.getRewards().clear();
        boolean heroicEquipped = message[Codes.REWARDS]==(byte)1;
        boolean legendaryEquipped = message[Codes.REWARDS+1]==(byte)1;
        if(heroicEquipped){
            character.getRewards().add(5);
        }
        if(legendaryEquipped){
            character.getRewards().add(11);
        }
    }

    private void updateIsActivated(byte[] message) {
        boolean isActivated = message[Codes.ACTIVATED] ==1;
        if(isActivated != character.isActivated()){
            character.setActivated(isActivated);
            playerView.activated(isActivated);
        }
        System.out.println("RECEIVED ACTIVATED " + isActivated);
    }
    private void updateBackground(byte[] message, boolean firstUpdate) {
        String background = BackgroundController.Companion.getBackgroundName(message[Codes.BACKGROUND]);
        if(!background.equals(character.getBackground())||firstUpdate) {
            character.setBackground(background);
            playerView.setBackground(background);
        }
        System.out.println("RECEIVED BACKGROUND " + background);
    }
    private void updateConditions(byte[] message){
        for(int i = 0; i < MessageLength.CONDITIONS; i++){
            boolean isActive = message[Codes.CONDITIONS+i] == (byte)1;
            System.out.println("RECEIVED CONDITION " +i+" "+ isActive);
            character.getConditionsActive()[i] = isActive;
        }
        playerView.setConditions(character.getConditionsActive());
    }

    private void updateXP(byte[] message){
        boolean hasChanged = false;
        for(int i = 0; i < 9; i++){
            boolean isEquipped = message[Codes.XP+i] == (byte)1;
            System.out.println("RECEIVED XP " +i+" "+ isEquipped);
            if(isEquipped != character.getXpCardsEquipped()[i] ){
                hasChanged = true;
                if(isEquipped){
                    character.equipXP(i,context);
                }
                else{
                    character.unequipXP(i);
                }
            }
        }
        character.setRewardObtained(character.getXpCardsEquipped()[8]);
        System.out.println("HAS CHANGED "+hasChanged);
    }

    private void updateDamageStrain(byte[] message) {
        int damage = message[Codes.DAMAGE_STRAIN];
        if(damage != character.getDamage()) {
            updateDamage(damage);
        }

        System.out.println("RECEIVED DAMAGE " + damage);
        //STRAIN
        int strain = message[Codes.DAMAGE_STRAIN+1];
        if(character.getStrain() != strain){
            updateStrain(strain);
        }
        System.out.println("RECEIVED STRAIN " + strain);

        int effect = message[Codes.DAMAGE_STRAIN+2];
        playerView.playEffect(effect, character);


    }

    private void onAddStrain(){
        if (character.getStrain() < character.getEndurance()) {
            character.setStrain(character.getStrain()+1);
            character.setStrainTaken(character.getStrainTaken()+1);
            playerView.damageAnimation.playAnim(EffectTypes.STRAIN,character);
        } else {
            if(addDamage(true)) {
                character.setDamageTaken(character.getDamageTaken()+1);
            }
            else{
                Sounds.INSTANCE.negativeSound();
            }
        }
        updateStrain(character.getStrain());
    }
    private void onMinusStrain(){
        if(character.getStrain()>0) {
            Sounds.INSTANCE.selectSound();
            character.setStrain(character.getStrain()-1);
            updateStrain(character.getStrain());
        }
    }

    private void updateStrain(int strain) {
        character.setStrain(strain);
        playerView.updateStrain(strain, isLocal);
        //updateMinusButtons();
    }

    private void onAddDamage(){
        if (!addDamage(false)) {
            Sounds.INSTANCE.negativeSound();
        }
    }

    private boolean addDamage(boolean isStrainDamage){
        boolean damageChangeable = character.getDamage() < character.getHealth() * 2;
        if (damageChangeable) {
            playerView.damageAnimation.playDamageAnim(isStrainDamage, character);
            character.setDamage(character.getDamage() + 1);
            character.setDamageTaken(character.getDamageTaken() + 1);
            updateDamage(character.getDamage());
        }
        return damageChangeable;
    }

    private void onMinusDamage(){
        if(character.getDamage()>0) {
            Sounds.INSTANCE.selectSound();
            character.setDamage(character.getDamage()-1);
            updateDamage(character.getDamage());
        }
    }

    private void updateDamage(int damage) {
        character.setDamage(damage);

        if (damage < character.getHealth()) {
            if (character.isWounded()) {
                onUnwounded();
            }
            if(character.getWithdrawn()) {
                onUnwithdrawn();
            }
        }
        else if (damage < character.getHealth() * 2) {
            character.setWounded(character.getDamage() - character.getHealth());
            if (!character.isWounded()) {
                onWounded();
            }
            if(character.getWithdrawn()) {
                onUnwithdrawn();
            }
        }
        else {
            if(!character.getWithdrawn()) {
                onWithdrawn();
            }
        }
        playerView.updateDamage(character,isLocal);
        //updateMinusButtons();
    }
    void onUnwithdrawn(){
        character.setWithdrawn(false);
        playerView.unWithdrawn();
    }
    void onWithdrawn(){
        character.setWithdrawn(true);
        playerView.withdrawn();
    }
    void onWounded(){
        character.setWounded(true);
        playerView.wounded(character);
        setChanged();
        notifyObservers();
    }
    void onUnwounded(){
        character.setWounded(false);
        playerView.unWounded(character);
        setChanged();
        notifyObservers();
    }

    /*
    void onUnwound(){
        character.setDamage(0);
        character.setWounded(0);
        character.setWithdrawn(false);
        character.setWounded(false);
        playerView.unWounded(character);
    }*/

    public void loadLocalCharacter(Character character, Activity context) {
        this.context = context;
        this.character = character;
        character.loadImages(context);
        onNewCharacterAdded();
        updateView();
        playerView.turnOnLightSaber(1500);
        updateMinusButtons();

        isLocal = true;
    }

    void updateMinusButtons(){
        if(isLocal) {
            playerView.getMinusDamageView().animate().alpha(character.getDamage() > 0 ? 1f : 0f);
            playerView.getMinusStrainView().animate().alpha(character.getDamage() > 0 ? 1f : 0f);
        }
    }
    public void updateView() {
        updateDamage(character.getDamage());
        playerView.updateAll(character,isLocal);
        playerView.turnOnLightSaber(500);
    }
    boolean getIsNotUpdatable(){
        return character==null || !isLocal;
    }
    public void show() {
        playerView.show();
        //playerView.lightSaberTurnOn = true;
        //turnOnLightSaber(300);
        updateView();
    }
    public void hide(){
        playerView.hide();
    }
    public void remove(){
        CharacterHolder.removeFromParty(character);
        character = null;
        id = "";
        hide();
    }
    public void onNavigate(){
        if(getView()!=null) {
            getView().turnOffLightSaber();
            //onStop();
        }
    }

    public void turnOnLightSaber(int delay) {
        playerView.turnOnLightSaber(delay);
    }

    public void onNextMission() {
        if(character == null || !isLocal){
            return;
        }
        character.setDamage(0);
        character.setStrain(0);
        character.setWounded(false);
        character.setWithdrawn(false);

        if(character.isActivated()) {
            character.setActivated(false);
            playerView.activated(false);
        }
    }

    public void onStop(){
        playerView.onStop();
    }
}
