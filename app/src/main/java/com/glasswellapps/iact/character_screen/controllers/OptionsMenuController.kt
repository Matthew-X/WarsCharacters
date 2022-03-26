package com.glasswellapps.iact.character_screen.controllers
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.glasswellapps.iact.R
import com.glasswellapps.iact.character_screen.CharacterScreen
import com.glasswellapps.iact.character_screen.views.BioView
import com.glasswellapps.iact.effects.Sounds
import kotlinx.android.synthetic.main.activity_character_screen.*
import kotlinx.android.synthetic.main.dialog_options.*


class OptionsMenuController (val characterScreen: CharacterScreen){
    private var optionsMenu: Dialog = Dialog(characterScreen)
    private var backgroundMenu = BackgroundController(characterScreen)
    private var settingsMenu = SettingsMenuController(characterScreen)
    private var creditsScreen = CreditsController(characterScreen)
    private var statsScreen = StatsScreenController(characterScreen)
    private var character = characterScreen.character
    private var bioView = BioView(characterScreen)

    init{

        backgroundMenu.setBackground(character,characterScreen.background_image,characterScreen.camouflage)
        optionsMenu.requestWindowFeature(Window.FEATURE_NO_TITLE)
        optionsMenu.setCancelable(false)
        optionsMenu.setContentView(R.layout.dialog_options)
        optionsMenu.setCanceledOnTouchOutside(true)
        optionsMenu.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        optionsMenu.bioOption.setOnClickListener { onBiography() }
        optionsMenu.powerOption.setOnClickListener { onPower() }
        optionsMenu.settingsOption.setOnClickListener { onSettings() }
        optionsMenu.saveOption.setOnClickListener { onSave() }
        optionsMenu.statsOption.setOnClickListener { onStatistics() }
        optionsMenu.backgroundOption.setOnClickListener { onBackground() }
        optionsMenu.creditsOption.setOnClickListener {onCredits() }
    }

    private fun onBiography() {
        Sounds.selectSound()
        optionsMenu.dismiss()
        bioView.showDialog(character)
    }
    private fun onPower() {
        Sounds.selectSound()
        optionsMenu.dismiss()
        if (!character.isWounded) {
            if(character.power!= null) {
                characterScreen.onShowCard(character.power!!)
            }

        } else {
            if(character.power_wounded!= null) {
                characterScreen.onShowCard(character.power_wounded!!)
            }
        }
    }
    private fun onSave() {
        Sounds.selectSound()
        characterScreen.showSaveDialog()
        optionsMenu.dismiss()
    }
    private fun onBackground() {
        Sounds.selectSound()
        optionsMenu.dismiss()
        backgroundMenu.showDialog(character,characterScreen.background_image,characterScreen.camouflage)
    }
    private fun onSettings() {
        Sounds.selectSound()
        optionsMenu.dismiss()
        settingsMenu.showDialog()
    }
    private fun onStatistics() {
        Sounds.selectSound()
        optionsMenu.dismiss()
        statsScreen.showDialog()
    }
    private fun onCredits() {
        Sounds.selectSound()
        optionsMenu.dismiss()
        creditsScreen.showDialog()
    }


    fun showDialog(){
        optionsMenu.show()
    }


    fun onStop(){
        backgroundMenu.onStop()
    }

}