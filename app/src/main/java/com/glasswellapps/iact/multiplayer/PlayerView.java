package com.glasswellapps.iact.multiplayer;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.glasswellapps.iact.BitmapLoader;
import com.glasswellapps.iact.R;
import com.glasswellapps.iact.character_screen.controllers.ActionController;
import com.glasswellapps.iact.character_screen.controllers.CharacterImageController;
import com.glasswellapps.iact.character_screen.controllers.StatController;
import com.glasswellapps.iact.character_screen.types.ConditionTypes;
import com.glasswellapps.iact.character_screen.views.CharacterImageView;
import com.glasswellapps.iact.character_screen.views.DamageAnimationEffects;
import com.glasswellapps.iact.character_screen.views.IANumbers;
import com.glasswellapps.iact.character_screen.views.PulsatingImageView;
import com.glasswellapps.iact.character_screen.views.StatView;
import com.glasswellapps.iact.characters.Character;

public class PlayerView {
    View view;
    View activationButton;
    ImageView damageView, strainView, minusDamageView, minusStrainView;

    ImageView  activeView, inactiveView;
    ImageView backgroundView;
    ImageView damageAnimView;
    ImageView companionImageView;
    PulsatingImageView camouflageView;
    CharacterImageView characterImageView;

    FrameLayout characterImageViews, woundedView;
    FrameLayout restAnimView;

    DamageAnimationEffects damageAnimation;
    View[] conditionViews = new View[5];// weakenedView, bleedingView, stunnedView, hiddenView,


    TextView nameView;
    TextView healthView, speedView, enduranceView;
    ImageView[] strengthView, insightView, techView;

    Activity context;
    public boolean isVisible = false;
    public PlayerView(View view, Activity context){
        this.view = view;
        this.context = context;
        damageView = view.findViewById(R.id.damage);
        strainView = view.findViewById(R.id.strain);
        minusDamageView = view.findViewById(R.id.imperial_minus_damage);
        minusStrainView = view.findViewById(R.id.imperial_minus_strain);

        activationButton =  view.findViewById(R.id.activationButton);
        inactiveView = view.findViewById(R.id.inactive);
        activeView = view.findViewById(R.id.active);

        nameView = view.findViewById(R.id.fileName);
        characterImageView = view.findViewById(R.id.character_image);
        characterImageViews = view.findViewById(R.id.character_images);

        backgroundView = view.findViewById(R.id.background_image);
        camouflageView = view.findViewById(R.id.bt_camouflage);
        woundedView = view.findViewById(R.id.wounded);
        damageAnimView = view.findViewById(R.id.damage_animation);
        restAnimView = view.findViewById(R.id.rest_animation);
        conditionViews[ConditionTypes.FOCUSED]= view.findViewById(R.id.focused);
        conditionViews[ConditionTypes.HIDDEN]= view.findViewById(R.id.hidden);
        conditionViews[ConditionTypes.WEAKENED]= view.findViewById(R.id.weakened);
        conditionViews[ConditionTypes.BLEEDING]= view.findViewById(R.id.bleeding);
        conditionViews[ConditionTypes.STUNNED]= view.findViewById(R.id.stunned);


        healthView = view.findViewById(R.id.health);
        speedView = view.findViewById(R.id.speed);
        enduranceView = view.findViewById(R.id.endurance);
        strengthView = new ImageView[]{view.findViewById(R.id.strength1),
                view.findViewById(R.id.strength2),view.findViewById(R.id.strength3)};
        insightView = new ImageView[]{view.findViewById(R.id.insight1),
                view.findViewById(R.id.insight2),view.findViewById(R.id.insight3)};
        techView = new ImageView[]{view.findViewById(R.id.tech1),
                view.findViewById(R.id.tech2),view.findViewById(R.id.tech3)};
        companionImageView = view.findViewById(R.id.companion_image);

        damageAnimation = new DamageAnimationEffects(context, damageAnimView, characterImageViews
                ,restAnimView);
    }
    public View getActivationButton (){return activationButton;}
    public ImageView getDamageView(){return damageView;}
    public ImageView getStrainView(){return strainView;}
    public ImageView getMinusDamageView(){return minusDamageView;}
    public ImageView getMinusStrainView(){return minusStrainView;}
    public ImageView getBackgroundView(){
        return backgroundView;
    }
    public PulsatingImageView getCamouflageView(){
        return camouflageView;
    }
    public void show() {
        view.setVisibility(View.VISIBLE);
        view.animate().setDuration(150).alpha(1);
        isVisible = true;
    }
    public void hide(){
        //view.animate().alpha(0);
        view.animate().setDuration(300).alpha(0);
        isVisible = false;
    }

    boolean lightSaberTurnOn = true;
    public void updateImages(Character character){
        if(character==null){
            return;
        }
        CharacterImageController.Companion.update(character,characterImageView,companionImageView, context);
    }
    public void turnOnLightSaber(int delay){
        if(lightSaberTurnOn) {
            CharacterImageController.Companion.turnOnLightSaber(characterImageView, delay);
            lightSaberTurnOn = false;
        }
    }
    public void turnOffLightSaber(){
        lightSaberTurnOn = true;
        CharacterImageController.Companion.turnOffLightSaber(characterImageView);
    }

    public void updateStats(Character character){
        StatController.Companion.update(character,
                healthView,
                enduranceView,
                speedView,
                strengthView,
                insightView,
                techView,
                context.getResources());
    }
    public void updateDamage(Character character, Boolean isLocal){
        int number = character.getDamage();
        if(character.getDamage() < character.getHealth()){
            unWounded(character);
            unWithdrawn();
        }
        else if(character.getDamage()<character.getHealth()*2){
            number = character.getDamage() - character.getHealth();
            wounded(character);
            unWithdrawn();
        }
        else if(character.getDamage()== character.getHealth()*2){
            wounded(character);
            withdrawn();
            number = character.getHealth();
        }
        damageView.setImageDrawable(IANumbers.getNumber(context.getResources(),number));
        if(isLocal){
            minusDamageView.animate().alpha(character.getDamage()>0?1:0);
        }
    }
    public void updateStrain(int strain, boolean isLocal){
        strainView.setImageDrawable(IANumbers.getNumber(context.getResources(),strain));
        if(isLocal){
            minusStrainView.animate().alpha(strain>0?1:0);
        }
    }
    public void playEffect(int effect, Character character){
        if(effect>0 && isVisible){
            damageAnimation.playAnim(effect, character);
        }
    }
    public void wounded(Character character){
        woundedView.animate().alpha(1f);
        updateStats(character);
    }
    public void unWounded(Character character){
        woundedView.animate().alpha(0f);
        updateStats(character);
    }
    ObjectAnimator slideDown ;
    public void withdrawn(){
        if(slideDown!= null) {
            if (slideDown.isStarted()) {
                return;
            }
        }
        slideDown = ObjectAnimator.ofFloat(
                characterImageViews, "translationY", characterImageViews.getY(),
                characterImageViews.getY(),
                characterImageViews.getHeight());
        slideDown.setDuration(1000);
        slideDown.start();
    }

    ObjectAnimator slideUp;
    public void unWithdrawn(){
        if(slideUp!= null) {
            if (slideUp.isStarted()) {
                return;
            }
        }
        slideUp = ObjectAnimator.ofFloat(characterImageViews,
                "translationY", 0f);
        slideUp.setDuration(500);
        slideUp.start();
    }
    public void activated(boolean isActivated) {
        ActionController.Companion.activationAnim(activeView,inactiveView,isActivated);
    }
    public void setBackground(String background) {
        backgroundView.setImageBitmap(BitmapLoader.Companion.getBitmap(context, "backgrounds" +
                "/background_"+background+
                ".jpg"));
    }
    public void setConditions(boolean[] conditionsActive) {
        for(int i = 0; i < conditionsActive.length; i++){
            boolean isActive = conditionsActive[i];
            if(isActive) {
                conditionViews[i].setVisibility(View.VISIBLE);
            }
            else {
                conditionViews[i].setVisibility(View.GONE);
            }
        }
    }
    public void setDefenceDice(Character character) {
        StatView.Companion.setDice(character,view.findViewById(R.id.defence),context.getResources());
    }

    public void updateAll(Character character, boolean isLocal) {
        if(character==null){
            return;
        }
        updateImages(character);
        setBackground(character.getBackground());
        setDefenceDice(character);
        updateDamage(character,isLocal);
        updateStrain(character.getStrain(),isLocal);
        updateStats(character);
        setConditions(character.getConditionsActive());
    }

    public void onStop(){
        try {
            characterImageView.onStop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
