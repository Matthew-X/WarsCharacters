package com.glasswellapps.iact.characters

import android.content.Context
import android.graphics.Bitmap
import com.glasswellapps.iact.R

class Character_vinto : Character {
    var card6Images = ArrayList<Bitmap?>()
    constructor(context: Context){
        //default values
        name = "Vinto Hreeda"
        name_short = "vinto"
        index = 20
        type = "Hero"
        defence_dice = "white"


        strength = "G  "
        insight = "BGG"
        tech = "RY "

        strengthWounded = "R  "
        insightWounded  = "BGR  "
        techWounded  = "RR "

        background = "interior"
        health_default = 11
        endurance_default = 4
        speed_default = 4

        health = 11
        endurance = 4
        speed = 4

        totalXP = 0
        damage = 0
        strain = 0
        token = 0
        wounded = 0

        xpScores = intArrayOf(1,1,2,2,3,3,4,4,0)
        xpEndurances = intArrayOf(0,0,1,0,0,0,0,0,0)
        xpHealths = intArrayOf(0,0,0,0,0,0,0,0,0)
        xpSpeeds = intArrayOf(0,1,0,0,0,0,0,0,0)
        xpItems = intArrayOf(-1,-1,-1,-1,-1,104,-1,-1,-1)

        portraitRow = 3
        portraitCol = 3
        //Get Images
        //Update images

        //update strain, update damage, xp, cards, weapons



        bio_title = "Unstoppable Avenger"
        bio_quote = "\"I show the same mercy that has been shown to me.\""
        bio_text ="The Rebellion has ties to many smugglers, bandits, thieves, mercenaries, and people of less than stellar reputation. " +
                "Mysterious figures who are not official Alliance operatives, but who are sympathetic to the Rebel cause and will occasionally do errands or favors for the right price. " +
                "\n\nDetermined, grim, and solemn, Vinto has led a long and tired life of near constant combat. " +
                "He is a natural with a blaster, his favorite weapon being a modified Hair-Trigger Pistol, which has been greatly upgraded to increase its firing rate;" +
                " he often dual wields a Secondary Blaster as well to lay covering fire, and assist in keeping his enemies at bay. " +
                "\n\nMany claim to have killed this elusive Rodian over the years; yet Vinto always seems to get back up ready for more. " +
                "His many \"deaths\" have earned him the nickname: \"Revenant\"- leading many to believe the Rodian warrior is a cursed spirit who travels the galaxy seeking revenge. " +
                "Vinto has completed hundred of contracts across the galaxy on many varied planets; his resolve and tenacity push him ever further towards his next mission. " +
                "He plans to fight until there is nothing left of himself to offer the galaxy; luckily Vinto is just getting started."  }

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
        if(mandoHelmet){
            layer2 =  getBitmap(context, "characters/" + name_short + "/images/helmet_mando.png")
        }
        else{
            layer2 = null
        }
    }
    override fun loadPortraitImage(context:Context){
        portraitImage = context.resources.getDrawable(R.drawable.portrait_vinto)
    }
}