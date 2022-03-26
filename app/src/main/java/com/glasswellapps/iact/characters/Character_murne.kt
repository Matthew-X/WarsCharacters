package com.glasswellapps.iact.characters

import android.content.Context
import android.graphics.Bitmap
import com.glasswellapps.iact.R

class Character_murne : Character {
    var card9Image:Bitmap? = null
    constructor(context: Context){
        //default values
        name = "Murne Rin"
        name_short = "murne"
        index = 14
        type = "Hero"
        defence_dice = "black"

        strength = "B  "
        insight = "BGY"
        tech = "BG "

        strengthWounded = "R  "
        insightWounded  = "BGR"
        techWounded  = "BR "

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

        xpScores = intArrayOf(1,1,2,2,3,3,4,4,0)
        xpEndurances = intArrayOf(0,0,0,0,0,0,0,0,0)
        xpHealths = intArrayOf(0,0,2,0,0,0,0,0,0)
        xpSpeeds = intArrayOf(0,0,0,0,0,0,0,0,0)
        xpItems = intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1,99)

        portraitRow = 2
        portraitCol = 2
        //Get Images
        //Update images

        //update strain, update damage, xp, cards, weapons

        bio_title = "Undercover Senator"
        bio_quote = "\"They don't need to be loyal all of the time. Only at the right time.\""
        bio_text ="Murne is not apt in combat, she prefers anonymity in the field and rarely enters a fight unless she must. " +
                "Though she actively avoids martial combat, Murne does carry a Diplomat Blaster Pistol, she purchased the day she became a senator. " +
                "She carries it within her robes at all times due to the clandestine nature of " +
                "her operations. \n\nHer true talents are in supporting her allies and bodyguards" +
                " in the field. " +
                "Whether offering data regarding troop movement, intel from her spies and allies, or by releasing false orders to Imperial officers; which are fed directly into Imperial comms. " +
                "\n\nMurne is working hard to see the Galactic Republic Emerge Anew and is " +
                "willing to die for her efforts; they just have to catch her first."   }

    //TODO alter for reward, duplicates, tier
    override fun loadImages(context: Context){
        super.loadImages(context)
        card9Image = getBitmap(context, "characters/murne/images/card9.png")
    }

    //TODO alter for reward, duplicates, tier
    override fun updateCharacterImages(context: Context){
        super.updateCharacterImages(context)

        //card6 tier images
        if(xpCardsEquipped[8]){
            layer1 = card9Image
        }
        else{
            layer1 = null
        }

        if(mandoHelmet){
            layer2 = getBitmap(context, "characters/" + name_short + "/images/helmet_mando.png")
        }
        else{
            layer2 = null
        }
    }
    override fun loadPortraitImage(context:Context){
        portraitImage = context.resources.getDrawable(R.drawable.portrait_murne)
    }
}