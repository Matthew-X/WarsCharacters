package com.glasswellapps.iact.characters

import android.content.Context
import android.graphics.Bitmap
import com.glasswellapps.iact.R

class Character_fenn : Character {

    constructor(context: Context) {
        //default values
        name = "Fenn Signis"
        name_short = "fenn"
        index = 5
        type = "Hero"
        defence_dice = "black"

        strength = "BG "
        insight = "BG "
        tech = "BG "

        strengthWounded = "BR "
        insightWounded = "BR "
        techWounded = "BR "

        background = "interior"
        health_default = 12
        endurance_default = 4
        speed_default = 4

        health = 12
        endurance = 4
        speed = 4

        totalXP = 0
        damage = 0
        strain = 0
        token = 0
        wounded = 0

        xpScores = intArrayOf(1, 1, 2, 2, 3, 3, 4, 4, 0)
        xpEndurances = intArrayOf(0, 0, 0, 0, 0, 0, 1, 0, 0)
        xpHealths = intArrayOf(0, 0, 0, 0, 0, 0, 3, 0, 0)
        xpSpeeds = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)

        portraitRow = 3
        portraitCol = 0
        //Get Images
        //Update images

        //update strain, update damage, xp, cards, weapons


        bio_title = "Grizzled Veteran"
        bio_quote = "\"Stick together and don't get in my way.\""
        bio_text =
            "Like every professional soldier, Fenn Signis relies on his comrades, yet, he also has a powerful independent streak. " +
                    "Fenn is a military lifer and plans to fight until he can no longer carry on for his ideals. " +
                    "\n\nDue to the losses of so many close friends, Fenn suffers from attachment" +
                    " issues and a mild case of un-diagnosed PTSD (usually in the form of gore-filled nightmares) from the many battles he has been through. " +
                    "He intentionally remains distant and professional with his squad-mates; despite this, others still respect him for his efficiency and reliability. " +
                    "A lone wolf in every meaning of the word; Fenn is tactically aware, cunning, and practically feral towards his enemies. " +
                    "\n\nFenn’s favored weapon, his Infantry Rifle was the standard equipment for his" +
                    " squad while fighting on ArdaI; Reliable, consistent, and comfortable in his hands, Fenn has carried the weapon through some of the toughest of fire-fights in the galaxy."
    }

    //TODO alter for reward, duplicates, tier
    override fun loadImages(context: Context){
        super.loadImages(context)
    }

    //TODO alter for reward, duplicates, tier
    override fun updateCharacterImages(context: Context){
        super.updateCharacterImages(context)

        if(tier == 1) {
            if (Math.random()<0.5) {
                currentImage?.recycle()
                currentImage = getBitmap(context,"characters/fenn/images/tier1image_duplicate.png")
            }
        }
        if(mandoHelmet){
            layer2 = getBitmap(context, "characters/" + name_short + "/images/helmet_mando.png")
        }
        else{
            layer2 = null
        }
    }
    override fun loadPortraitImage(context:Context){
        portraitImage = context.resources.getDrawable(R.drawable.portrait_fenn)
    }
}