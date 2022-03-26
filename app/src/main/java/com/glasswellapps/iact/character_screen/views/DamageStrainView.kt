package com.glasswellapps.iact.character_screen.views

import android.animation.ObjectAnimator
import com.glasswellapps.iact.character_screen.CharacterScreen
import com.glasswellapps.iact.character_screen.controllers.ButtonPressedHandler
import com.glasswellapps.iact.character_screen.types.EffectTypes
import com.glasswellapps.iact.effects.Sounds
import kotlinx.android.synthetic.main.activity_character_screen.*

class DamageStrainView (val characterScreen: CharacterScreen){
    var character = characterScreen.character
    var character_images = characterScreen.character_images
    var character_image = characterScreen.character_image
    var add_damage = characterScreen.add_damage
    var minus_damage = characterScreen.minus_damage
    var add_strain = characterScreen.add_strain
    var minus_strain = characterScreen.minus_strain
    var wounded = characterScreen.wounded
    var resources = characterScreen.resources
    var companion_image = characterScreen.companion_image
    val damageAnimation = DamageAnimationEffects(characterScreen, characterScreen.damage_animation, character_images, characterScreen.rest_animation);



    init{
        if (character.damage > 0) {
            if (minus_damage.alpha == 0f) {
                minus_damage.animate().alpha(1f)
            }
            if (character.damage < character.health) {
                add_damage.setImageDrawable(IANumbers.getNumber(resources, character.damage))
            }
            else {
                character.wounded = character.damage - character.health
                add_damage.setImageDrawable(IANumbers.getNumber(resources, character.wounded))
                wounded.animate().alpha(1f)
                character.isWounded = true
                if (character.withdrawn) {
                    character_images.animate().translationY(character_image.height.toFloat())
                }
            }
        }

        if (character.strain > 0) {
            if (minus_strain.alpha == 0f) {
                minus_strain.animate().alpha(1f)
            }
            add_strain.setImageDrawable(IANumbers.getNumber(resources, character.strain))
        }
    }


    fun onUnwithdrawn(){
        val slide = ObjectAnimator.ofFloat(character_images, "translationY", 0f)
        slide.duration = 500
        slide.start()
    }

    fun onWithdrawn(){
        val slide = ObjectAnimator.ofFloat(
            character_images, "translationY", character_images.y, character_images.y,
            character_image.height.toFloat()
        )
        slide.duration = 1000
        slide.start()
    }

    fun onWounded(){ wounded.animate().alpha(1f) }
    fun onUnwounded(){ wounded.animate().alpha(0f) }
    fun onUnwound(){
        damageAnimation.playAnim(EffectTypes.REST, character)
        updateDamageNumber(0)
        minus_strain.animate().alpha(0f)
        minus_damage.animate().alpha(0f)
        onUnwithdrawn()
        onUnwounded()
    }
    fun onAddDamage(damage:Int, isStrainDamage:Boolean) {
        damageAnimation.playDamageAnim(isStrainDamage, character)
        updateDamageNumber(damage)
        if (minus_damage.alpha == 0f) {
            minus_damage.animate().alpha(1f)
        }
    }
    fun onMinusDamage(damage:Int) {

        if (damage == 0) {
            if (minus_damage.alpha > 0f) {
                minus_damage.animate().alpha(0f)
            }
        }
        else{
            //ButtonPressedHandler.onButtonPressed(minus_damage)
        }
    }
    fun onAddStrain(strain:Int){
        damageAnimation.playAnim(EffectTypes.STRAIN, character)
        add_strain.setImageDrawable(IANumbers.getNumber(resources, strain))
        if (minus_strain.alpha == 0f) {
            minus_strain.animate().alpha(1f)
        }
    }

    fun onMinusStrain(strain:Int) {
        add_strain.setImageDrawable(IANumbers.getNumber(resources, strain))

        if (strain == 0) {
            if (minus_strain.alpha > 0f) {
                minus_strain.animate().alpha(0f)
            }
        }
        else{
            //ButtonPressedHandler.onButtonPressed(minus_strain)
        }
    }
    fun onRest(){
        add_strain.setImageDrawable(IANumbers.getNumber(resources, character.strain))
        //Sounds.strainSound()
        damageAnimation.playAnim(EffectTypes.REST, character)

    }
    fun updateDamageNumber(number:Int){
        add_damage.setImageDrawable(IANumbers.getNumber(resources, number))


    }
}