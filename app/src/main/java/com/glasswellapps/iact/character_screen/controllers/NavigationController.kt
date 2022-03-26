package com.glasswellapps.iact.character_screen.controllers
import android.content.Intent
import android.util.DisplayMetrics
import com.glasswellapps.iact.character_screen.CharacterScreen
import com.glasswellapps.iact.character_screen.XPScreen
import com.glasswellapps.iact.effects.Sounds
import com.glasswellapps.iact.inventory.*
import kotlinx.android.synthetic.main.activity_character_screen.*

class NavigationController(val characterScreen: CharacterScreen) {
    private var extendDownButton = characterScreen.extend_down_button
    private var extendUpButton = characterScreen.extend_up_button
    private var killTrackerBar = characterScreen.kill_tracker_bar
    private var menuBar = characterScreen.menu_bar
    private var optionsMenu: OptionsMenuController = OptionsMenuController(characterScreen)
    private var sideMenuState = 0


    init{
        extendUpButton.setOnClickListener { onExtendUp() }
        extendDownButton.setOnClickListener { onExtendDown() }
        characterScreen.rewards_button.setOnClickListener { onReward() }
        characterScreen.item_accessory_button.setOnClickListener { onAccessory() }
        characterScreen.item_armour_button.setOnClickListener { onArmour() }
        characterScreen.item_melee_button.setOnClickListener { onMelee() }
        characterScreen.item_ranged_button.setOnClickListener{onRange()}
        characterScreen.xp_button.setOnClickListener { onXPScreen() }
        characterScreen.options_button.setOnClickListener { onShowOptions() }
    }

    fun update() {
        when (sideMenuState) {
            -1 -> {
                extendDownButton.animate().alpha(0f)
                extendUpButton.animate().alpha(1f)
                killTrackerBar.animate().translationY(0f)
                menuBar.animate().translationY(characterScreen.height)
            }
            0 -> {
                extendDownButton.animate().alpha(1f)
                extendUpButton.animate().alpha(1f)
                killTrackerBar.animate().translationY(-characterScreen.height)
                menuBar.animate().translationY(0f)
            }
            1 -> {
                extendDownButton.animate().alpha(1f)
                extendUpButton.animate().alpha(0f)
                killTrackerBar.animate().translationY(-2*characterScreen.height)
                menuBar.animate().translationY(-characterScreen.height)
            }
        }
    }
    private fun onExtendDown() {
        if (sideMenuState > -1) {
            Sounds.selectSound()
            sideMenuState--
            update()
        }
    }
    private fun onExtendUp() {
        if (sideMenuState < 1) {
            Sounds.selectSound()
            sideMenuState++
            update()
        }
    }
    private fun onNavigate(intent:Intent){
        Sounds.selectSound()
        //characterScreen.turnOffLightSaber()
        characterScreen.startActivity(intent)
    }

    fun onReward() {
        val intent = Intent(characterScreen, RewardsScreen::class.java)
        onNavigate(intent)
    }
    private fun onAccessory() {
        val intent = Intent(characterScreen, AccScreen::class.java)
        onNavigate(intent)
    }
    private fun onArmour() {
        val intent = Intent(characterScreen, ArmorScreen::class.java)
        onNavigate(intent)
    }
    private fun onMelee() {
        val intent = Intent(characterScreen, MeleeScreen::class.java)
        onNavigate(intent)
    }
    private fun onRange() {
        val intent = Intent(characterScreen, RangedScreen::class.java)
        onNavigate(intent)
    }
    fun onXPScreen() {
        val intent = Intent(characterScreen, XPScreen::class.java)
        onNavigate(intent)
    }
    private fun onShowOptions() {
        Sounds.selectSound()
        optionsMenu.showDialog()
    }

    fun onStop() {
        optionsMenu.onStop()
    }
}