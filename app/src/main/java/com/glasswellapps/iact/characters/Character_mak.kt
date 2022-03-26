package com.glasswellapps.iact.characters

import android.content.Context
import com.glasswellapps.iact.R

class Character_mak : Character {

    constructor(context: Context){
        //default values
        name = "Mak Eshka'rey"
        name_short = "mak"
        index = 12
        type = "Hero"
        defence_dice = "white"

        strength = "BG "
        insight = "BG "
        tech = "BGY"

        strengthWounded = "BR "
        insightWounded  = "BR "
        techWounded  = "BGR"

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
        xpEndurances = intArrayOf(0,0,0,0,0,0,0,0,0)
        xpHealths = intArrayOf(0,0,0,2,0,0,0,0,0)
        xpSpeeds = intArrayOf(0,0,0,0,0,0,0,0,0)
        xpItems = intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1,97)

        portraitRow = 0
        portraitCol = 2
        //Get Images
        //Update images

        //update strain, update damage, xp, cards, weapons


        bio_title = "Covert Agent"
        bio_quote = "\"The Imperial military police couldn't take me alive, and neither will you!\""
        bio_text ="Mak wields a modified Longblaster- a civilian hunting weapon he has " +
                "re-purposed in an attempt to disguise his assassinations- by averting the use of military grade weapons. " +
                "\n\nHe prefers to take out high-priority targets from long range, but isn't " +
                "opposed to a more intimate fight if he must. " +
                "Mak also commonly utilizes an array of tech from his connections in the Bothan Spynet or his past allegiance with the ISB. " +
                "Mak has a talent of “melting” in and out of combat; waylaying his foes and causing distractions to catch them unprepared. " +
                "\n\nThanks to his agility and stealth, enemies have a hard time pining this " +
                "elusive Bothan down. " +
                "In his spare time, Mak attempts to gather volunteers to aid him in removing the threat of the Imperial \"Shadow Suits\" and end any advantage the Empire could have gained from their creation."
    }

    //TODO alter for reward, duplicates, tier
    override fun loadImages(context: Context){
        super.loadImages(context)

    }

    //TODO alter for reward, duplicates, tier
    override fun updateCharacterImages(context: Context){
        super.updateCharacterImages(context)

        //card6 tier images
        if(xpCardsEquipped[8]){
            var card9Image = loadCardTierImage(context, tier, "card9")
            if (card9Image != null) {
                currentImage = card9Image
            }

        }
    }
    override fun loadPortraitImage(context:Context){
        portraitImage = context.resources.getDrawable(R.drawable.portrait_mak)
    }
}