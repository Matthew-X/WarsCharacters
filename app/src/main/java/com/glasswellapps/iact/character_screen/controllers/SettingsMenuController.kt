package com.glasswellapps.iact.character_screen.controllers
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.SeekBar
import com.glasswellapps.iact.R
import com.glasswellapps.iact.character_screen.CharacterScreen
import com.glasswellapps.iact.effects.Sounds
import kotlinx.android.synthetic.main.screen_settings.*

class SettingsMenuController (val characterScreen: CharacterScreen){
    private var settingsMenu: Dialog = Dialog(characterScreen)
    private var character = characterScreen.character

    init{
        settingsMenu.requestWindowFeature(Window.FEATURE_NO_TITLE)
        settingsMenu.setCancelable(false)
        settingsMenu.setContentView(R.layout.screen_settings)
        settingsMenu.setCanceledOnTouchOutside(true)
        settingsMenu.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        settingsMenu.toggleDamageAnim.isChecked =  character.damageAnimSetting
        settingsMenu.toggleDamageAnim.setOnClickListener {
            character.damageAnimSetting = settingsMenu.toggleDamageAnim.isChecked
            Sounds.selectSound()
        }

        settingsMenu.toggleConditionAnim.isChecked = character.conditionAnimSetting
        settingsMenu.toggleConditionAnim.setOnClickListener {
            character.conditionAnimSetting = settingsMenu.toggleConditionAnim.isChecked
            Sounds.selectSound()
            characterScreen.updateConditions()
        }

        settingsMenu.toggleActionUsage.isChecked = character.actionUsageSetting
        settingsMenu.toggleActionUsage.setOnClickListener {
            character.actionUsageSetting = settingsMenu.toggleActionUsage.isChecked
            Sounds.selectSound()
        }
        settingsMenu.imageSettingButton.setOnClickListener {
            settingsMenu.imageSetting.visibility = View.VISIBLE
            setPreviewImage(character.imageSetting)
            settingsMenu.settingsMenu.visibility = View.INVISIBLE

        }
        settingsMenu.imagePreview.setOnClickListener {
            settingsMenu.imageSetting.visibility = View.INVISIBLE
            settingsMenu.settingsMenu.visibility = View.VISIBLE
            Sounds.selectSound()
        }
        settingsMenu.imageAuto.setOnClickListener {
            setPreviewImage(-1)
        }
        settingsMenu.imageDefault.setOnClickListener {
            setPreviewImage(0)
        }
        settingsMenu.imageTier1.setOnClickListener {
            setPreviewImage(1)
        }
        settingsMenu.imageTier2.setOnClickListener {
            setPreviewImage(2)
        }
        settingsMenu.imageTier3.setOnClickListener {
            setPreviewImage(3)
        }

        settingsMenu.soundEffectsVolume.progress = (character.soundEffectsSetting*100).toInt()
        Sounds.setSoundVolume(character.soundEffectsSetting)
        settingsMenu.soundEffectsVolume.setOnSeekBarChangeListener(object : SeekBar
        .OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                character.soundEffectsSetting = p1.toFloat()/100
                Sounds.setSoundVolume(character.soundEffectsSetting)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {


            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
        settingsMenu.soundEffectsVolume.progress = (character.soundEffectsSetting*100).toInt()
    }
    private fun setPreviewImage(setting:Int) {
        Sounds.selectSound()
        character.imageSetting = setting

        character.updateCharacterImages(characterScreen)
        settingsMenu.imagePreview.setImageBitmap(character.currentImage)
        settingsMenu.imageAuto.alpha = 0.5f
        settingsMenu.imageDefault.alpha = 0.5f
        settingsMenu.imageTier1.alpha = 0.5f
        settingsMenu.imageTier2.alpha = 0.5f
        settingsMenu.imageTier3.alpha = 0.5f

        when (setting) {
            -1 -> {
                settingsMenu.imageAuto.alpha = 1f
                settingsMenu.imageSettingButton.text = "AUTO"
            }
            0 -> {
                settingsMenu.imageDefault.alpha = 1f
                settingsMenu.imageSettingButton.text = "DEFAULT"
            }
            1 -> {
                settingsMenu.imageTier1.alpha = 1f
                settingsMenu.imageSettingButton.text = "TIER 1"
            }
            2 -> {
                settingsMenu.imageTier2.alpha = 1f
                settingsMenu.imageSettingButton.text = "TIER 2"
            }
            3 -> {
                settingsMenu.imageTier3.alpha = 1f
                settingsMenu.imageSettingButton.text = "TIER 3"
            }
        }
    }

    fun showDialog() {
        settingsMenu.imageSetting.visibility = View.INVISIBLE
        settingsMenu.settingsMenu.visibility = View.VISIBLE

        settingsMenu.toggleDamageAnim!!.isChecked = character.damageAnimSetting
        settingsMenu.toggleConditionAnim!!.isChecked = character.conditionAnimSetting
        settingsMenu.toggleActionUsage!!.isChecked = character.actionUsageSetting
        settingsMenu.soundEffectsVolume.progress = (character.soundEffectsSetting*100).toInt()

        when (character.imageSetting) {
            -1 -> {
                settingsMenu.imageSettingButton.text = "AUTO"
            }
            0 -> {
                settingsMenu.imageSettingButton.text = "DEFAULT"
            }
            1 -> {
                settingsMenu.imageSettingButton.text = "TIER 1"
            }
            2 -> {
                settingsMenu.imageSettingButton.text = "TIER 2"
            }
            3 -> {
                settingsMenu.imageSettingButton.text = "TIER 3"
            }
        }
        settingsMenu.show()
    }
}