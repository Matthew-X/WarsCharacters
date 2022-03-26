package com.glasswellapps.iact.character_screen.controllers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.CountDownTimer
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.View
import android.widget.ImageView
import com.glasswellapps.iact.R
import com.glasswellapps.iact.character_screen.CharacterScreen
import com.glasswellapps.iact.character_screen.types.ConditionTypes
import com.glasswellapps.iact.character_screen.views.CharacterImageView
import com.glasswellapps.iact.characters.Character
import com.glasswellapps.iact.effects.Sounds
import kotlinx.android.synthetic.main.activity_character_screen.*

class CharacterImageController (){//val characterScreen: CharacterScreen){
    //private var character = characterScreen.character
    //private var characterImage = characterScreen.character_image
    //private var resources = characterScreen.resources
    //private var companionImage = characterScreen.companion_image

    companion object {
        fun turnOffLightSaber(character_image:CharacterImageView){
            lightSaberCountdown?.cancel()
            character_image.turnOffLightSaber()
        }

        var lightSaberCountdown: CountDownTimer? = null
        fun turnOnLightSaber(character_image:CharacterImageView,delay:Long){
            if(character_image.layerLightSaber == null){ return }
            lightSaberCountdown = object : CountDownTimer(delay, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                }

                override fun onFinish() {
                    if (character_image.turnOnLightSaber()) {
                        Sounds.play(Sounds.lightSaber)
                        Sounds.play(Sounds.lightsaber_hum)

                    }
                }
            }.start()
        }

        fun update(
            character: Character, characterImage: CharacterImageView,
            companionImage: ImageView, context:
            Context
        ) {
            character.updateCharacterImages(context)
            if (character.conditionAnimSetting) {
                if (character.currentImage != null) {
                    character.glowImage = Bitmap.createBitmap(character.currentImage!!)
                    val input = Bitmap.createBitmap(character.currentImage!!)
                    val output = Bitmap.createBitmap(character.currentImage!!)

                    val rs = RenderScript.create(context)
                    val blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
                    blur.setRadius(25f)
                    val tempIn = Allocation.createFromBitmap(rs, input)
                    val tempOut = Allocation.createFromBitmap(rs, output)
                    blur.setInput(tempIn)
                    blur.forEach(tempOut)
                    tempOut.copyTo(character.glowImage)
                }
                characterImage.setGlowBitmap(character.glowImage)
                character.updateCharacterImages(context)
            }
            characterImage.setImageBitmap(character.currentImage)
            characterImage.setLayer1Bitmap(character.layer1)
            characterImage.setLayer2Bitmap(character.layer2)
            characterImage.setLightSaberBitmap(character.layerLightSaber)

            if (character.name_short != "jarrod") {
                if (character.astromech) {
                    character.companionImage =
                        (context.resources.getDrawable(
                            R.drawable.r5_astromech1, context
                                .theme
                        ) as
                                BitmapDrawable).bitmap
                } else {
                    character.companionImage = null
                }
            }
            println("ASTROMECH" + character.companionImage + " " + character.astromech)
            if (character.companionImage != null) {
                if (character.conditionsActive[ConditionTypes.HIDDEN] && character
                        .conditionAnimSetting
                ) {
                    companionImage.visibility = View.INVISIBLE
                } else {
                    companionImage.setImageBitmap(character.companionImage)
                    companionImage.visibility = View.VISIBLE
                }
            } else {
                companionImage.visibility = View.GONE
            }


        }

    }

}