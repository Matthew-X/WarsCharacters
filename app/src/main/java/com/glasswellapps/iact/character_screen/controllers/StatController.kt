package com.glasswellapps.iact.character_screen.controllers

import android.content.res.Resources
import android.widget.ImageView
import android.widget.TextView
import com.glasswellapps.iact.character_screen.CharacterScreen
import com.glasswellapps.iact.character_screen.views.StatView
import com.glasswellapps.iact.characters.Character
import com.glasswellapps.iact.inventory.Items
import kotlinx.android.synthetic.main.activity_character_screen.*

class StatController (){
    companion object {
        fun update(
            character: Character,
            health: TextView,
            endurance: TextView,
            speed: TextView,
            strength: Array<ImageView>,
            insight: Array<ImageView>,
            tech: Array<ImageView>,
            resources: Resources
        ) {
            character.health = character.health_default
            character.endurance = character.endurance_default
            character.speed = character.speed_default

            applyEquippedStats(character)

            if (character.isWounded) {
                character.endurance--
                character.speed--
            }
            StatView.update(
                character, health, endurance, speed, strength, insight, tech,
                resources
            )
        }

        private fun applyEquippedStats(character: Character) {
            for (i in character.xpCardsEquipped.indices) {
                if (character.xpCardsEquipped[i]) {
                    if (character.xpHealths[i] != 0) {
                        character.health += character.xpHealths[i]
                    }
                    if (character.xpEndurances[i] != 0) {
                        character.endurance += character.xpEndurances[i]
                    }
                    if (character.xpSpeeds[i] != 0) {
                        character.speed += character.xpSpeeds[i]
                    }
                }
            }

            for (i in 0 until character.accessories.size) {
                character.health += Items.itemsArray!![character.accessories[i]].health
                character.endurance += Items.itemsArray!![character.accessories[i]].endurance
            }
            for (i in 0 until character.armor.size) {
                character.health += Items.itemsArray!![character.armor[i]].health
            }
            for (i in 0 until character.rewards.size) {
                character.health += Items.rewardsArray!![character.rewards[i]].health
            }
        }
    }
}