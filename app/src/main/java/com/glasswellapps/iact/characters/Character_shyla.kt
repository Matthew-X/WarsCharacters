package com.glasswellapps.iact.characters

import android.content.Context
import android.graphics.Bitmap
import com.glasswellapps.iact.R

class Character_shyla : Character {
    var card4Image:Bitmap? = null
    var card5Image:Bitmap? = null
    //var helmetImages = ArrayList<Bitmap?>()
    constructor(context: Context){
        //default values
        name = "Shyla Varad"
        name_short = "shyla"
        index = 17
        type = "Hero"
        defence_dice = "black"

        strength = "BBG"
        insight = "GG "
        tech = "BY "

        strengthWounded = "BBR"
        insightWounded  = "GR "
        techWounded  = "BR "

        background = "interior"
        health_default = 12
        endurance_default = 4
        speed_default = 5

        health = 12
        endurance = 4
        speed = 5

        totalXP = 0
        damage = 0
        strain = 0
        token = 0
        wounded = 0

        xpScores = intArrayOf(1,1,2,2,3,3,4,4,0)
        xpEndurances = intArrayOf(0,0,0,0,0,0,0,1,0)
        xpHealths = intArrayOf(0,0,0,0,0,0,0,0,0)
        xpSpeeds = intArrayOf(0,0,0,0,0,0,0,0,0)
        xpItems = intArrayOf(-1,-1,-1,102,103,-1,-1,-1,-1)

        portraitRow = 1
        portraitCol = 3
        //Get Images
        //Update images

        //update strain, update damage, xp, cards, weapons


        bio_title = "Professional Mercenary"
        bio_quote = "\"I come from and ancient warrior tradition. That tradition will dismantle you.\""
        bio_text ="The Rebellion has ties to many smugglers, bandits, thieves, mercenaries, and people of less than stellar reputation. " +
                "Mysterious figures who are not official Alliance operatives, but who are sympathetic to the Rebel cause and will occasionally do errands or favors for the right price." +
                "\n\nThe Mandalorians pride themselves in the art-of-combat, training their youths to be skilled with all manner of Martial Arts, Melee Weaponry, and Marksmanship. " +
                "The soldiers of Mandalore utilize many advanced gadgets to combat the more unique enemies in the galaxy: Remote Detonators, Smoke Bombs, and Whipcord launchers- just to name a few. " +
                "Shyla above it all, prefers her specially crafted Duelists Blade, meant for precise and deadly strikes. " +
                "The Ancient Mandalorians once fought Jedi with these unique blades- called Vibroswords, Crafted specifically to contend with the Lightsabers of their enemies. " +
                "\n\nIntroverted, conservative, professional, and skilled, Shyla is a real force " +
                "to be reckoned with. In her free time, Shyla researches into Deathwatch in an attempt to feel closer to her heroes; and to gleam as much knowledge as she can from the long dead organization. " +
                "Something at the back of her mind seems to entice her about the organization and she plans to get to the bottom of it."}


    //TODO alter for reward, duplicates, tier
    override fun loadImages(context: Context){
        super.loadImages(context)
        card4Image = getBitmap(context, "characters/shyla/images/card4.png")
        card5Image = getBitmap(context, "characters/shyla/images/card5.png")
        //helmetImages = loadCardTierImages(context,"helmet")
    }

    //TODO alter for reward, duplicates, tier
    override fun updateCharacterImages(context: Context){
        super.updateCharacterImages(context)

        if(xpCardsEquipped[3]){
            layer1 = card4Image
        }
        else{
            layer1 = null
        }
        if(xpCardsEquipped[4]){
            layer2 = card5Image
        }
        else{
            layer2 = null
        }

        //todo helmet
        if(mandoHelmet || combatVisor || reinforcedHelmet){
            var helmetImage = loadCardTierImage(context,tier,"helmet")
            if(helmetImage != null) {
                currentImage = helmetImage
            }
        }
    }
    override fun loadPortraitImage(context:Context){
        portraitImage = context.resources.getDrawable(R.drawable.portrait_shyla)
    }
}