package com.glasswellapps.iact.character_screen.controllers
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.glasswellapps.iact.R
import com.glasswellapps.iact.character_screen.CharacterScreen
import com.glasswellapps.iact.character_screen.views.DamageStrainView
import com.glasswellapps.iact.effects.Sounds
import kotlinx.android.synthetic.main.activity_character_screen.*
import kotlinx.android.synthetic.main.dialog_assist.*
import kotlinx.android.synthetic.main.dialog_rest.*
import kotlin.math.min

class DamageStrainController (val characterScreen: CharacterScreen){
    private var character = characterScreen.character
    private var view = DamageStrainView(characterScreen)
    private var addDamage = characterScreen.add_damage
    private var addStrain = characterScreen.add_strain
    private var minusDamage = characterScreen.minus_damage
    private var minusStrain = characterScreen.minus_strain


    init{
        addDamage.setOnClickListener { onAddDamage() }
        minusDamage.setOnClickListener { onMinusDamage() }
        addStrain.setOnClickListener { onAddStrain() }
        minusStrain.setOnClickListener { onMinusStrain() }

        addDamage.setOnLongClickListener {
            if (character.damage >= character.health) {
                characterScreen.onShowUnwoundDialog()
            }
            true
        }
        addStrain.setOnLongClickListener {
            characterScreen.onShowRestDialog()
            true
        }
    }
    private fun onAddDamage() {

        if (!addDamage(false)) {
            Sounds.negativeSound()
        }
        characterScreen.updateNetwork(false);
    }
    private fun onMinusDamage() {
        if(character.damage>0) {
            Sounds.selectSound()
            minusDamage()
            characterScreen.updateNetwork(false);
        }
    }
    private fun onAddStrain() {
        addStrain()
        characterScreen.updateNetwork(false);
    }
    private fun onMinusStrain() {
        if(character.strain>0) {
            Sounds.selectSound()
            minusStrain()
            characterScreen.updateNetwork(false);
        }
    }
    fun onRest(){
        character.strain -= character.endurance
        if (character.strain < 0) {
            var healAmount = -character.strain
            if(character.isWounded){
                healAmount = min(character.wounded,healAmount)
            }
            for (i in 1..healAmount) {
                minusDamage()
            }
            character.strain = 0
        }
        view.onRest()
        characterScreen.updateNetwork(false)
        character.timesRested++
    }

    // logic
    private fun addDamage(isStrainDamage:Boolean):Boolean{
        val damageChangeable = character.damage < character.health * 2
        if (damageChangeable) {
            view.onAddDamage(character.damage, isStrainDamage)
            character.damage++
            character.damageTaken++
            if (character.damage < character.health) {
                view.updateDamageNumber(character.damage)
            }
            else if (character.damage < character.health * 2) {
                character.wounded = character.damage - character.health
                view.updateDamageNumber(character.wounded)
                if (!character.isWounded) {
                    wounded()
                }
            }
            else {
                if(!character.withdrawn) {
                    withdrawn()
                }
                view.updateDamageNumber(character.health)
            }
        }
        return damageChangeable
    }
    private fun minusDamage() {
        if (character.damage > 0) {
            character.damage--
            view.onMinusDamage(character.damage)
            if (character.damage < character.health) {
                view.updateDamageNumber(character.damage)
                if (character.isWounded) {
                    unwounded()
                }
                if(character.withdrawn) {
                    unWithdrawn()
                }
            } else if (character.damage < character.health * 2) {
                character.wounded = character.damage - character.health
                view.updateDamageNumber(character.wounded)
                if(character.withdrawn) {
                    unWithdrawn()
                }
            }
            else{
                character.withdrawn = true
                character.damage = character.health*2
                character.wounded = character.damage - character.health
            }

        }

    }
    fun addStrain(){
        if (character.strain < character.endurance) {

            character.strain++
            character.strainTaken++
            view.onAddStrain(character.strain)
        } else {
            if(addDamage(true)) {
                character.damageTaken++
            }
            else{
                Sounds.negativeSound()
            }
        }
    }
    private fun minusStrain(){
        if (character.strain > 0) {
            character.strain--
            view.onMinusStrain(character.strain)
        }
    }

    fun unwound(){
        character.damage = 0
        character.wounded = 0
        character.isWounded = false
        character.withdrawn = false
        view.onUnwound()
    }
    fun wounded(){
        character.timesWounded++
        character.isWounded = true
        view.onWounded()
        characterScreen.updateStats()
        characterScreen.quickSave()
    }
    private fun unwounded(){
        character.wounded = 0
        character.isWounded = false
        view.onUnwounded()
        characterScreen.updateStats()
        characterScreen.quickSave()
    }
    fun withdrawn(){
        character.withdrawn = true
        character.timesWithdrawn++
        view.onWithdrawn()
        characterScreen.quickSave()
    }
    private fun unWithdrawn(){
        character.withdrawn = false
        view.onUnwithdrawn()
        characterScreen.quickSave()
    }
}