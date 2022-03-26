package com.glasswellapps.iact.characters

import android.content.Context
import android.graphics.Bitmap
import com.glasswellapps.iact.R

class Character_jarrod : Character {

    var droid:Bitmap? = null
    var droid_card9:Bitmap? = null

    var jaxCard:Bitmap? = null
    var jaxCard_card6:Bitmap? = null
    var jaxCard_card9:Bitmap? = null
    var jaxCard_card69:Bitmap? = null

    constructor(context: Context){
        //default values
        name = "Jarrod Kelvin"
        name_short = "jarrod"
        index = 8
        type = "Hero"
        defence_dice = "black"

        strength = "GG "
        insight = "BG "
        tech = "BGY"

        strengthWounded = "RG "
        insightWounded  = "BR  "
        techWounded  = "BRG"

        background = "interior"
        health_default = 10
        endurance_default = 5
        speed_default = 4

        health = 10
        endurance = 5
        speed = 4

        totalXP = 0
        damage = 0
        strain = 0
        token = 0
        wounded = 0

        xpScores = intArrayOf(1,1,2,2,3,3,4,4,0)
        xpEndurances = intArrayOf(0,1,0,0,0,0,0,0,0)
        xpHealths = intArrayOf(0,0,0,0,0,0,0,2,0)
        xpSpeeds = intArrayOf(0,0,0,0,0,1,0,0,0)

        portraitRow = 4
        portraitCol = 1
        //Get Images
        //Update images

        loadImages(context)

        bio_title = "Robotic Overlord"
        bio_quote = "\"Jax, plug in. See what you can find for us.\""
        bio_text ="The Rebellion has ties to many smugglers, bandits, thieves, mercenaries, and people of less than stellar reputation. Mysterious figures who are not official Alliance operatives, but who are sympathetic to the Rebel cause and will occasionally do errands or favors for the right price." +
                "\n\nGrowing up on a planet where the first ones at the scene obtain the best " +
                "salvage- has conditioned Jarrod to move fast and strike when his enemies are not prepared. " +
                "Ever admiring and fearing the deadly creatures of his homeworld, Jarrod has become accustomed to using a pair of deadly Vibro-Claws he developed- " +
                "to instill fear in his foes and bring out his own savage nature. " +
                "\n\nJarrod is an expert in the field of Robotics and knows how to repair just " +
                "about anything mechanical to working condition." +
                " Alongside “Jax” he plans to seek justice for his murdered mentor Dr. Graves, and works reclaim the stolen blueprints of his loyal droid. " +
                "In thanks for his service, the Rebellion has offered to provide shelter to Jarrod’s clan if he works alongside them to defeat the Empire;" +
                " he gladly accepted the offer."
    }


    //TODO alter for reward, duplicates, tier
    override fun loadImages(context: Context){
        super.loadImages(context)
        droid = getBitmap(context, "characters/jarrod/images/droid.png")
        droid_card9 = getBitmap(context, "characters/jarrod/images/droid_card9.png")
        companionImage = droid

        jaxCard = getBitmap(context, "characters/jarrod/images/jax.png")
        jaxCard_card6 = getBitmap(context, "characters/jarrod/images/jax_card6.png")
        jaxCard_card9 = getBitmap(context, "characters/jarrod/images/jax_card9.png")
        jaxCard_card69 = getBitmap(context, "characters/jarrod/images/jax_card6_card9.png")
        companionCard  = jaxCard
    }

    //TODO alter for reward, duplicates, tier
    override fun updateCharacterImages(context: Context){
        super.updateCharacterImages(context)

        //droid images
        companionCard  = jaxCard

        if(xpCardsEquipped[8]){
            companionImage = droid_card9
            if(xpCardsEquipped[5]) {
                companionCard = jaxCard_card69
            }
            else{
                companionCard  = jaxCard_card9
            }
        }
        else{
            if(xpCardsEquipped[5]) {
                companionCard  = jaxCard_card6
            }
            companionImage = droid
        }
    }
    override fun loadPortraitImage(context:Context){
        portraitImage = context.resources.getDrawable(R.drawable.portrait_jarrod)
    }
}