package com.glasswellapps.iact.character_screen.controllers

import android.animation.ObjectAnimator
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Toast
import com.glasswellapps.iact.R
import com.glasswellapps.iact.character_screen.CharacterScreen
import com.glasswellapps.iact.character_screen.types.ConditionTypes
import com.glasswellapps.iact.effects.Sounds
import com.glasswellapps.iact.inventory.Items
import kotlinx.android.synthetic.main.activity_character_screen.*
import kotlinx.android.synthetic.main.dialog_end_activation.*
import kotlinx.android.synthetic.main.dialog_next_mission.*

class ActionController (val characterScreen: CharacterScreen){
    private var character = characterScreen.character
    private var characterViewGroup = characterScreen.character_view_group
    private var layoutInflater = characterScreen.layoutInflater

    private var nextMissionDialog: Dialog = Dialog(characterScreen)
    private var endActivationDialog: Dialog = Dialog(characterScreen)

    private var activationButton = characterScreen.activationButton
    private var inactive = characterScreen.inactive
    private var active = characterScreen.active

    private var action1 = characterScreen.action1
    private var action2 = characterScreen.action2
    private var actionPanel = characterScreen.action_panel;

    private var actionMenu = characterScreen.action_menu
    private var attackFocused = characterScreen.attack_focused
    private var attackHidden = characterScreen.attack_hidden
    private var actionStunnedAttack = characterScreen.action_stunned_attack
    private var actionStunnedMove = characterScreen.action_stunned_move
    private var actionStunnedSpecial = characterScreen.action_stunned_special
    private var actionRemoveStun = characterScreen.action_remove_stun
    private var actionRemoveBleeding = characterScreen.action_remove_bleeding

    private var actionPanelPosition = 0f

    init{
        activationButton.setOnClickListener { onActivationButton() }
        activationButton.setOnLongClickListener{
            showNextMission()
            true
        }

        action1.setOnClickListener { onAction() }
        action2.setOnClickListener { onAction() }



        nextMissionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        nextMissionDialog.setCancelable(false)
        nextMissionDialog.setContentView(R.layout.dialog_next_mission)
        nextMissionDialog.setCanceledOnTouchOutside(true)
        nextMissionDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        nextMissionDialog.next_mission_yes.setOnClickListener { onNextMission() }
        nextMissionDialog.next_mission_no.setOnClickListener { onNextMissionNo() }

        endActivationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        endActivationDialog.setCancelable(false)
        endActivationDialog.setContentView(R.layout.dialog_end_activation)
        endActivationDialog.setCanceledOnTouchOutside(true)
        endActivationDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        endActivationDialog.end_activation_yes.setOnClickListener{onEndActivation()}
        endActivationDialog.end_activation_no.setOnClickListener{onEndActivationNo()}

        characterScreen.action_attack.setOnClickListener { onAttack() }
        characterScreen.action_move.setOnClickListener { onMove() }
        characterScreen.action_special.setOnClickListener { onSpecial() }
        characterScreen.action_interactDoor.setOnClickListener { onInteractDoor() }
        characterScreen.action_interactTerminal.setOnClickListener { onInteractTerminal() }
        characterScreen.action_interactCrate.setOnClickListener { onPickUpCrate() }
        characterScreen.action_rest.setOnClickListener { characterScreen.onRest() }
        characterScreen.action_remove_stun.setOnClickListener { onRemoveStun() }
        characterScreen.action_remove_bleeding.setOnClickListener { onRemoveBleeding() }


        actionPanelPosition = -characterScreen.width/2.5f
    }

    private fun onAttack() {
        if (character.actionsLeft > 0) {
            if (!character.conditionsActive[ConditionTypes.STUNNED]) {
                characterScreen.onRemoveFocused()
                characterScreen.onRemoveHidden()
                actionCompleted()
                playAttackSound()
                character.attacksMade++
            }
            else{
                Sounds.negativeSound()
            }
        } else {
            showNoActionsLeftToast()
        }
    }

    private fun playAttackSound(){
        if(character.layerLightSaber != null){
            Sounds.play(Sounds.lightsaber_heavy_flurry)
            return
        }
        if(character.weapons.size > 0){
            val weaponType = Items.itemsArray?.get(character.weapons[0])?.type
            when(weaponType) {
                Items.ranged -> Sounds.play(Sounds.blaster_gaster_blaster_master)
                Items.melee -> Sounds.play(Sounds.melee_impact)
            }
        }
    }



    private fun onMove() {
        if (character.actionsLeft > 0) {
            Sounds.movingSound()
            if (!character.conditionsActive[ConditionTypes.STUNNED]) {
                actionCompleted()
                character.movesTaken++
            }
            else{
                Sounds.negativeSound()
            }
        } else {
            showNoActionsLeftToast()
        }
    }
    private fun onSpecial() {
        if (character.actionsLeft > 0) {
            if (!character.conditionsActive[ConditionTypes.STUNNED]) {
                actionCompleted()
                Sounds.specialSound()
                character.specialActions++
            }
            else{
                Sounds.negativeSound()
            }
        } else {
            showNoActionsLeftToast()
        }
    }
    private fun onInteractDoor() {
        if (character.actionsLeft > 0) {
            actionCompleted()
            Sounds.doorSound()
            character.interactsUsed++
        } else {
            showNoActionsLeftToast()
        }
    }
    private fun onInteractTerminal(){
        if (character.actionsLeft > 0) {
            actionCompleted()
            Sounds.terminalSound()
            character.interactsUsed++
        } else {
            showNoActionsLeftToast()
        }
    }
    private fun onPickUpCrate() {
        if (character.actionsLeft > 0) {
            actionCompleted()
            Sounds.crateSound()
            character.cratesPickedUp++
        } else {
            showNoActionsLeftToast()
        }
    }

    private fun onRemoveStun() {
        if (character.actionsLeft > 0 || !character.actionUsageSetting) {
            characterScreen.onRemoveStun()
            Sounds.selectSound()
            actionCompleted()
        } else {
            showNoActionsLeftToast()
        }
    }
    private fun onRemoveBleeding() {
        if (character.actionsLeft > 0 || !character.actionUsageSetting) {
            characterScreen.onRemoveBleeding()
            Sounds.selectSound()
            actionCompleted()
        } else {
            showNoActionsLeftToast()
        }
    }

    private fun showNoActionsLeftToast() {
        Sounds.negativeSound()
        val noActionsLeftToast = Toast(characterScreen)
        noActionsLeftToast.duration = Toast.LENGTH_SHORT
        noActionsLeftToast.view =  layoutInflater.inflate(
            R.layout.toast,
            characterViewGroup,
            false
        )
        noActionsLeftToast.setGravity(Gravity.CENTER, 0, 0)
        noActionsLeftToast.show()
    }
    private fun onActivationButton() {
        if (!character.isActivated) {
            onActivate()
        } else {
            onEndActivation()
        }
        characterScreen.updateNetwork(false);
    }
    private fun onActivate(){

        if (character.actionUsageSetting) {
            character.actionsLeft = 2
            turnOnActionButtons()
        }
        character.isActivated = true
        activationAnim(active,inactive,character.isActivated)
    }
    private fun onEndActivation() {
        endActivationDialog.dismiss()
        characterScreen.onRemoveWeakened()
        turnOffActionButtons()
        if (character.actionUsageSetting) {
            if(character.actionsLeft <= 1){
                character.activated++
            }
        }
        else{
            character.activated++
        }
        //activationAnim(active,inactive,false)
        character.isActivated = false
        activationAnim(active,inactive,character.isActivated)
    }

    companion object {
        fun activationAnim(active: View, inactive: View, isActive: Boolean) {
            Sounds.selectSound()
            if (isActive) {
                flipIn(active)
                flipOut(inactive)
            } else {
                flipIn(inactive)
                flipOut(active)
            }
        }

        private fun flipIn(view: View) {
            ObjectAnimator.ofFloat(view, "scaleX", 0f, 0f, 0f, 1f).setDuration(200).start()
        }

        private fun flipOut(view: View) {
            ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f, 0f, 0f).setDuration(200).start()
        }
    }

    fun turnOnActionButtons() {

        action1.animate().alpha(1f)
        action2.animate().alpha(1f)
        actionPanel.animate().setDuration(200).translationX(0f)
    }
    fun turnOffActionButtons() {
        actionPanel.animate().setDuration(200).translationX(actionPanelPosition)
    }
    private fun onEndActivationNo() {
        Sounds.selectSound()
        endActivationDialog.dismiss()
    }
    private fun onAction() {
        if(!character.isActivated || character.actionsLeft <= 0){
            return
        }
        Sounds.selectSound()
        actionMenu.visibility = View.INVISIBLE
        actionMenu.alpha = 0f
        //if (character.actionsLeft > 0) {
            //todo add focus symbol to attack
            if (character.conditionsActive[ConditionTypes.FOCUSED]) {
                attackFocused.visibility = View.VISIBLE
            } else {
                attackFocused.visibility = View.GONE
            }
            if (character.conditionsActive[ConditionTypes.HIDDEN]) {
                attackHidden.visibility = View.VISIBLE
            } else {
                attackHidden.visibility = View.GONE
            }
            //todo add stun symbol and deactivate to move, special and attack
            if (character.conditionsActive[ConditionTypes.STUNNED]) {
                actionStunnedAttack.visibility = View.VISIBLE
                actionStunnedMove.visibility = View.VISIBLE
                actionStunnedSpecial.visibility = View.VISIBLE
                actionRemoveStun.visibility = View.VISIBLE
            } else {
                actionStunnedAttack.visibility = View.INVISIBLE
                actionStunnedMove.visibility = View.INVISIBLE
                actionStunnedSpecial.visibility = View.INVISIBLE
                actionRemoveStun.visibility = View.GONE
            }
            if (character.conditionsActive[ConditionTypes.BLEEDING]) {
                actionRemoveBleeding.visibility = View.VISIBLE
            } else {
                actionRemoveBleeding.visibility = View.GONE
            }
            actionMenu.visibility = View.VISIBLE
            actionMenu.animate().alpha(1f)
        //}
    }
    fun actionCompleted() {
        if (character.actionUsageSetting) {
            if (character.actionsLeft > 0) {
                character.actionsLeft--
                if (character.actionsLeft == 1) {
                    action1.alpha = 0f
                } else if (character.actionsLeft == 0) {
                    action2.alpha = 0f
                }
                if (character.conditionsActive[ConditionTypes.BLEEDING]) {
                    characterScreen.onAddStrain()
                }
                actionMenu.visibility = View.INVISIBLE
            }
            if (character.actionsLeft <= 0) {
                actionMenu.visibility = View.INVISIBLE
                if (character.isActivated) {
                    endActivationDialog.show()
                }
            }
        }
    }

    fun showNextMission(){
        nextMissionDialog.show()
    }

    private fun onNextMission() {
        Sounds.selectSound()
        nextMissionDialog.dismiss()
        characterScreen.onUnwound()
        character.conditionsActive = BooleanArray(5)
        if(character.isActivated) {
            turnOffActionButtons()
            activationAnim(active,inactive,false)
            character.isActivated = false
        }
        character.actionsLeft = 0
        characterScreen.updateConditions()
        characterScreen.updateStats()
        characterScreen.quickSave()
    }
    private fun onNextMissionNo() {
        Sounds.selectSound()
        nextMissionDialog.dismiss()
    }
}