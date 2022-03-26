package com.glasswellapps.iact.characters

import android.content.Context
import com.glasswellapps.iact.R

class Character_diala : Character {

    constructor(context: Context){
        //default values
        name = "Diala Passil"
        name_short = "diala"
        index = 3
        type = "Hero"
        defence_dice = "white"

        strength = "BG "
        insight = "BGY"
        tech = "B  "

        strengthWounded = "BR "
        insightWounded  = "BGR"
        techWounded  = "R  "

        background = "interior"
        health_default = 12
        endurance_default = 5
        speed_default = 4

        health = 12
        endurance = 5
        speed = 4

        totalXP = 0
        damage = 0
        strain = 0
        token = 0
        wounded = 0

        xpScores = intArrayOf(1,1,2,2,3,3,4,4,0)
        xpEndurances = intArrayOf(0,0,0,0,0,0,0,0,0)
        xpHealths = intArrayOf(0,0,0,0,0,0,0,0,0)
        xpSpeeds = intArrayOf(0,0,0,0,1,0,0,0,0)
        xpItems = intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1,93)

        portraitRow = 2
        portraitCol = 0
        //Get Images
        //Update images

        //update strain, update damage, xp, cards, weapons


        bio_title = "Haunted Padawan"
        bio_quote = "\"A master of the Resilience Form is invincible. One who aspires to it, such as myself, is simply very hard to kill.\""
        bio_text ="Even before her training on Jedha, Diala was a capable fighter. She now only grows stronger as she learns new ways to weave the force into her actions on the battlefield. " +
                "\n\nDiala laments that she was not able to finish her Jedi training, create a " +
                "lightsaber before her master fell, or find all of the the pieces necessary to craft one herself. " +
                "Thus, she wields a durable, yet unremarkable, Plasteel Staff- with a skill and ease only a force sensitive could conjure. " +
                "\n\nDiala is able to remain calm and focused under fire in combat, using the " +
                "defensive saber forms taught to her by Shu Yen, Form I (Shii-Cho) and Form_III (Soresu), to protect herself- and turn her enemy’s attacks against them. " +
                "In battle Diala weathers the storm; then rains down counter blows upon her foolish attackers."
    }

    //TODO alter for reward, duplicates, tier
    override fun loadImages(context: Context){
        super.loadImages(context)

    }


    //TODO alter for reward, duplicates, tier
    override fun updateCharacterImages(context: Context){
        super.updateCharacterImages(context)
        layerLightSaber = null
        //card9 tier images
        if(xpCardsEquipped[8]){
            var card9Image = loadCardTierImage(context, tier, "card9")
            if (card9Image != null) {
                currentImage = card9Image
                layerLightSaber = getBitmap(context,
                    "characters/diala/images/lightsaber_card9.png");
            }
        }
        if(tier == 3) {
            if (ancientLightSaber) {
                if(xpCardsEquipped[8]){
                    currentImage = getBitmap(context,
                        "characters/diala/images/tier3image_ancient_light_saber_card9.png");
                    layerLightSaber = getBitmap(context,
                        "characters/diala/images/lightsaber_ancient_card9.png");
                }
                else{
                    currentImage = getBitmap(context,"characters/diala/images/tier3image_ancient_light_saber.png");
                    layerLightSaber = getBitmap(context,
                        "characters/diala/images/lightsaber_ancient.png");
                }
            }
        }
        println(""+ancientLightSaber + " " + tier)
    }
    override fun loadPortraitImage(context:Context){
        portraitImage = context.resources.getDrawable(R.drawable.portrait_diala)
    }
}