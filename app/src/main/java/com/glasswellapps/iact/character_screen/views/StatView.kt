package com.glasswellapps.iact.character_screen.views

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import com.glasswellapps.iact.R
import com.glasswellapps.iact.character_screen.CharacterScreen
import com.glasswellapps.iact.characters.Character
import kotlinx.android.synthetic.main.activity_character_screen.*

class StatView(val characterScreen: CharacterScreen) {
    //var resources = characterScreen.resources
    //var character = characterScreen.character
    //var defence = characterScreen.defence
    //var health = characterScreen.health
    //var endurance = characterScreen.endurance
    //var speed = characterScreen.speed
    //var strength = arrayOf(characterScreen.strength1,characterScreen.strength2,characterScreen.strength3)
    //var insight = arrayOf(characterScreen.insight1,characterScreen.insight2,characterScreen.insight3)
    //var tech = arrayOf(characterScreen.tech1,characterScreen.tech2,characterScreen.tech3)
    init{
        setDice(characterScreen.character, characterScreen.defence, characterScreen.resources)
    }
    companion object {
        fun setDice(character: Character, defence: ImageView, resources: Resources) {
            when (character.defence_dice) {
                "white" -> defence.setImageDrawable(resources.getDrawable(R.drawable.dice))
                "black" -> defence.setImageDrawable(resources.getDrawable(R.drawable.dice_black))
                "" -> defence.visibility = View.INVISIBLE
            }
        }

        open fun update(
            character: Character,
            health: TextView,
            endurance: TextView,
            speed: TextView,
            strength: Array<ImageView>,
            insight: Array<ImageView>,
            tech: Array<ImageView>,
            resources: Resources
        ) {

            setStatColor(health, character.health, character.health_default, resources)
            setStatColor(endurance, character.endurance, character.endurance_default, resources)
            setStatColor(speed, character.speed, character.speed_default, resources)

            health.text = "" + character.health
            endurance.text = "" + character.endurance
            speed.text = "" + character.speed

            if (!character.isWounded) {
                setDiceColor(strength[0], character.strength[0], resources)
                setDiceColor(strength[1], character.strength[1], resources)
                setDiceColor(strength[2], character.strength[2], resources)

                setDiceColor(insight[0], character.insight[0], resources)
                setDiceColor(insight[1], character.insight[1], resources)
                setDiceColor(insight[2], character.insight[2], resources)

                setDiceColor(tech[0], character.tech[0], resources)
                setDiceColor(tech[1], character.tech[1], resources)
                setDiceColor(tech[2], character.tech[2], resources)
            } else {
                setDiceColor(strength[0], character.strengthWounded[0], resources)
                setDiceColor(strength[1], character.strengthWounded[1], resources)
                setDiceColor(strength[2], character.strengthWounded[2], resources)

                setDiceColor(insight[0], character.insightWounded[0], resources)
                setDiceColor(insight[1], character.insightWounded[1], resources)
                setDiceColor(insight[2], character.insightWounded[2], resources)

                setDiceColor(tech[0], character.techWounded[0], resources)
                setDiceColor(tech[1], character.techWounded[1], resources)
                setDiceColor(tech[2], character.techWounded[2], resources)
            }
        }

        private fun setStatColor(stat: TextView, current: Int, default: Int, resources: Resources) {
            if (current > default) {
                stat.setShadowLayer(5f, 0f, 0f, resources.getColor(R.color.dice_green))
            } else if (current < default) {
                stat.setShadowLayer(5f, 0f, 0f, resources.getColor(R.color.stat_orange))
            } else {
                stat.setShadowLayer(5f, 0f, 0f, Color.BLACK)
            }
        }

        private fun setDiceColor(dice: ImageView, color: Char, resources: Resources) {
            dice.visibility = ImageView.VISIBLE
            when (color) {
                'B' -> dice.setImageDrawable(resources.getDrawable(R.drawable.dice_blue))
                'G' -> dice.setImageDrawable(resources.getDrawable(R.drawable.dice_green))
                'Y' -> dice.setImageDrawable(resources.getDrawable(R.drawable.dice_yellow))
                'R' -> dice.setImageDrawable(resources.getDrawable(R.drawable.dice_red))
                ' ' -> ImageViewCompat.setImageTintList(
                    dice, ColorStateList.valueOf(
                        Color.argb(
                            0,
                            0,
                            0,
                            0
                        )
                    )
                )
            }
        }
    }
}