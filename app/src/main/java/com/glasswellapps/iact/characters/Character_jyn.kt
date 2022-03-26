package com.glasswellapps.iact.characters

import android.content.Context
import com.glasswellapps.iact.R

class Character_jyn : Character {


    constructor(context: Context){
        //default values
        name = "Jyn Odan"
        name_short = "jyn"
        index = 9
        type = "Hero"
        defence_dice = "white"

        strength = "B  "
        insight = "BG "
        tech = "BGY"

        strengthWounded = "R  "
        insightWounded  = "BR "
        techWounded  = "BGR"

        background = "interior"
        health_default = 10
        endurance_default = 4
        speed_default = 5

        health = 10
        endurance = 4
        speed = 5

        totalXP = 0
        damage = 0
        strain = 0
        token = 0
        wounded = 0

        xpScores = intArrayOf(1,1,2,2,3,3,4,4,0)
        xpEndurances = intArrayOf(0,0,0,0,0,0,0,0,0)
        xpHealths = intArrayOf(0,0,0,0,0,0,0,0,0)
        xpSpeeds = intArrayOf(0,0,0,0,0,0,0,0,0)

        portraitRow = 2
        portraitCol = 1
        //Get Images
        //Update images

        //update strain, update damage, xp, cards, weapons


        bio_title = "Alluring Smuggler"
        bio_quote = "\"I don't mean to brag; I just happen to be amazing.\""
        bio_text ="The Rebellion has ties to many smugglers, bandits, thieves, mercenaries, and people of less than stellar reputation. " +
                "Mysterious figures who are not official Alliance operatives, but who are sympathetic to the Rebel cause and will occasionally do errands or favors for the right price." +
                "\n\nJyn prefers Blaster_Pistols to rifles, and is most comfortable with an older" +
                " Vintage model given to her by her father when she first left her homeworld to travel the stars. " +
                "Jyn is both lithe and agile, making her way across the battlefield with ease and elegance, and finds that most problems can be solved by getting to an objective first and firing a few blaster shots along the way. " +
                "Jyn is just itching to return to her trade as a smuggler; she spends much of her free time trying to track down that deceitful snake Szark, or further details on which Imperial detaining facility contains her abducted spacecraft. " +
                "\n\nThe Valkyrie Corona is Jyn's pride and joy and she will never rest until she" +
                "regains custody of her \"baby.\" Helping the galaxy is all well and good, but " +
                "Jyn doesn't like being tied down for long."
    }

    //TODO alter for reward, duplicates, tier
    override fun loadImages(context: Context){
        super.loadImages(context)
    }

    //TODO alter for reward, duplicates, tier
    override fun updateCharacterImages(context: Context){
        super.updateCharacterImages(context)

        //card6 tier images
        if(xpCardsEquipped[5]){
            var card6Image = loadCardTierImage(context, tier, "card6")
            if (card6Image != null) {
                currentImage = card6Image
            }
        }

        //card9 tier images
        if(xpCardsEquipped[8]){
            var card9Image = loadCardTierImage(context, tier, "card9")
            if (card9Image != null) {
                currentImage = card9Image
            }
        }
    }
    override fun loadPortraitImage(context:Context){
        portraitImage = context.resources.getDrawable(R.drawable.portrait_jyn)
    }
}