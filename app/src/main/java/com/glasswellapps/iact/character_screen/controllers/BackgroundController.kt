package com.glasswellapps.iact.character_screen.controllers

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.Image
import android.view.Window
import android.widget.ImageView
import com.glasswellapps.iact.BitmapLoader
import com.glasswellapps.iact.BitmapLoader.Companion.getBitmap
import com.glasswellapps.iact.R
import com.glasswellapps.iact.character_screen.CharacterScreen
import com.glasswellapps.iact.character_screen.views.PulsatingImageView
import com.glasswellapps.iact.characters.Character
import com.glasswellapps.iact.effects.Sounds
import kotlinx.android.synthetic.main.activity_character_screen.*
import kotlinx.android.synthetic.main.dialog_background.*

class BackgroundController (val context: Activity){
    private lateinit var character:com.glasswellapps.iact.characters.Character
    private lateinit var backgroundImage:ImageView
    private lateinit var camouflage:PulsatingImageView
    private var backgroundDialog: Dialog = Dialog(context)
    init{
        backgroundDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        backgroundDialog.setCancelable(false)
        backgroundDialog.setContentView(R.layout.dialog_background)
        backgroundDialog.setCanceledOnTouchOutside(true)
        backgroundDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        backgroundDialog.snow_background.setOnClickListener { onBackgroundSnow() }
        backgroundDialog.jungle_background.setOnClickListener { onBackgroundJungle() }
        backgroundDialog.desert_background.setOnClickListener { onBackgroundDesert() }
        backgroundDialog.bespin_background.setOnClickListener { onBackgroundBespin() }
        backgroundDialog.city_background.setOnClickListener { onBackgroundCity() }
        backgroundDialog.interior_background.setOnClickListener { onBackgroundInterior() }

    }
    companion object {
        val backgroundNames = arrayOf("interior","desert","snow","bespin","jungle","city")
        fun getBackgroundName(index: Int): String {
            return backgroundNames[index];
        }

        fun getBackgroundIndex(name: String): Int {
            return backgroundNames.indexOf(name);
        }
    }
    fun setBackground(character:Character, backgroundImage:ImageView,
                      camouflage:PulsatingImageView){
        this.character = character
        this.backgroundImage = backgroundImage
        this.camouflage = camouflage

        backgroundImage.setImageBitmap(
                getBitmap(
                    context, "backgrounds" +
                            "/background_" + character.background +
                            ".jpg"
                )
            )
        camouflage.setImageBitmap(BitmapLoader.getBitmap(context, "backgrounds/camo_"+ character.background +".png"))
        backgroundImage.setImageBitmap(character.getBackgroundImage(context))
        camouflage.setImageBitmap(character.getCamoImage(context))

    }

    fun onStop(){
        camouflage.onStop()
    }

    fun showDialog(character:Character, backgroundImage:ImageView, camouflage:PulsatingImageView){
        setBackground(character,backgroundImage,camouflage)
        backgroundDialog.show()
    }
    private fun onBackgroundSnow() {
        Sounds.selectSound()
        character.background = "snow"
        backgroundImage.setImageBitmap(BitmapLoader.getBitmap(context, "backgrounds/background_snow.jpg"))
        camouflage.setImageBitmap(BitmapLoader.getBitmap(context, "backgrounds/camo_snow.png"))
    }
    private fun onBackgroundJungle() {
        Sounds.selectSound()
        character.background = "jungle"
        backgroundImage.setImageBitmap(BitmapLoader.getBitmap(context, "backgrounds/background_jungle.jpg"))
        camouflage.setImageBitmap(BitmapLoader.getBitmap(context, "backgrounds/camo_jungle.png"))
    }
    private fun onBackgroundDesert() {
        Sounds.selectSound()
        character.background = "desert"
        backgroundImage.setImageBitmap(
            BitmapLoader.getBitmap(context, "backgrounds/background_desert" +
                    ".jpg"))
        camouflage.setImageBitmap(BitmapLoader.getBitmap(context, "backgrounds/camo_desert.png"))
    }
    private fun onBackgroundBespin() {
        Sounds.selectSound()
        character.background = "bespin"
        backgroundImage.setImageBitmap(BitmapLoader.getBitmap(context, "backgrounds/background_bespin.jpg"))
        camouflage.setImageBitmap(BitmapLoader.getBitmap(context, "backgrounds/camo_bespin.png"))
    }
    private fun onBackgroundCity() {
        Sounds.selectSound()
        character.background = "city"
        backgroundImage.setImageBitmap(BitmapLoader.getBitmap(context, "backgrounds/background_city.jpg"))
        camouflage.setImageBitmap(BitmapLoader.getBitmap(context, "backgrounds/camo_city.png"))
    }
    private fun onBackgroundInterior() {
        Sounds.selectSound()
        character.background = "interior"
        backgroundImage.setImageBitmap(BitmapLoader.getBitmap(context, "backgrounds/background_interior.jpg"))
        camouflage.setImageBitmap(BitmapLoader.getBitmap(context, "backgrounds/camo_interior.png"))
    }
}