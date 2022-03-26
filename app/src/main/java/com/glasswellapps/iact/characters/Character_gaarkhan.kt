package com.glasswellapps.iact.characters

import android.content.Context
import com.glasswellapps.iact.R

class Character_gaarkhan : Character {

    constructor(context: Context){
        //default values
        name = "Gaarkhan"
        name_short = "gaarkhan"
        index = 6
        type = "Hero"
        defence_dice = "black"

        strength = "BGY"
        insight = "B  "
        tech = "BG "

        strengthWounded = "BGR"
        insightWounded  = "R  "
        techWounded  = "BR "

        background = "interior"
        health_default = 14
        endurance_default = 4
        speed_default = 4

        health = 14
        endurance = 4
        speed = 4

        totalXP = 0
        damage = 0
        strain = 0
        token = 0
        wounded = 0

        xpScores = intArrayOf(1,1,2,2,3,3,4,4,0)
        xpEndurances = intArrayOf(0,0,0,0,0,0,0,1,0)
        xpHealths = intArrayOf(0,0,0,0,0,0,0,0,0)
        xpSpeeds = intArrayOf(0,0,0,0,0,0,0,1,0)

        portraitRow = 0
        portraitCol = 1
        //Get Images
        //Update images

        //update strain, update damage, xp, cards, weapons


        bio_title = "Resolute Warmaster"
        bio_quote = "\"Underneath that fur is a few hundred pounds of solid muscle and anger.\""
        bio_text ="In combat, Gaarkhan is the epitome of Wookiee ferocity: he charges forward to engage the enemy with his favorite weapon, a deadly Vibro-Ax." +
                " Any foes who are lucky enough to survive his savage attack will quickly find that he is most dangerous when injured. " +
                "\n\nGruff, moody, ferocious, and intimidating, Gaarkhan surveys his surroundings" +
                " with a critical eye; those foolish enough to threaten his allies will know the true definition of fear." +
                " \n\nGaarkhan has shamed himself once for revenge; he now hopes to make things " +
                "right by freeing his people from servitude. The Empire will know the wrath of a true Wookiee warrior. "  }

    //TODO alter for reward, duplicates, tier
    override fun loadPortraitImage(context:Context){
        portraitImage = context.resources.getDrawable(R.drawable.portrait_gaarkhan)
    }

    //TODO alter for reward, duplicates, tier
    override fun updateCharacterImages(context: Context){
        super.updateCharacterImages(context)

        if(reinforcedHelmet || combatVisor){
            layer2 = getBitmap(context, "characters/" + name_short + "/images/tier3image_helmet.png")
        }
        else{
            layer2 = null
        }
    }
}