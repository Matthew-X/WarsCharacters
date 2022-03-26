package com.glasswellapps.iact.characters

import android.content.Context
import com.glasswellapps.iact.R

class Character_loku : Character {

    constructor(context: Context){
        //default values
        name = "Loku Kanoloa"
        name_short = "loku"
        index = 11
        type = "Hero"
        defence_dice = "white"
        strength = "B  "
        insight = "BGY"
        tech = "B  "
        background = "interior"

        health_default = 10
        endurance_default = 5
        speed_default = 5

        strengthWounded = "R  "
        insightWounded  = "BGR"
        techWounded  = "R  "

        health = 10
        endurance = 5
        speed = 5

        totalXP = 0
        damage = 0
        strain = 0
        token = 0
        wounded = 0

        xpScores = intArrayOf(1,1,2,2,3,3,4,4,0)
        xpEndurances = intArrayOf(0,0,0,0,0,0,0,0,0)
        xpHealths = intArrayOf(0,0,0,0,0,2,0,0,0)
        xpSpeeds = intArrayOf(0,0,0,0,0,0,0,0,0)
        xpItems = intArrayOf(-1,-1,-1,96,-1,-1,-1,-1,-1)

        portraitRow = 3
        portraitCol = 1
        //Get Images
        //Update images

        //update strain, update damage, xp, cards, weapons


        bio_title = "Vigilant Marksman"
        bio_quote = "\"His armor is weak at the neck. Aim higher.\""
        bio_text ="Through the rigorous training undergone by all Mon Cal Special Forces, and an innate talent for observation, Loku is able to gather every piece of relevant information about the battlefield. " +
                "\n\nThis Information about the terrain and his enemies provides a decisive " +
                "advantage to his team, and he quickly becomes a focal point of clarity in the chaos of battle. " +
                "This information, combined with his remarkable aim, enables him to spot and eliminate enemies before they even become a threat to him or his allies. " +
                "\n\nLoku prefers to wield a durable and reliable All-Weather Rifle (resistant to" +
                " the harsh climates found across the galaxy and able to fire while submerged), and prefers to find locations on the field where he can attack from range, to provide direction, or distraction, for his allies. " +
                "\n\nThough the empire has never witnessed him in direct combat, Loku has made a " +
                "name for himself throughout Imperial channels as an elite marksman and a highly dangerous threat. " +
                "Loku takes pride in the fact his enemies fear him, and that they will never see him coming."}

    //TODO alter for reward, duplicates, tier
    override fun loadImages(context: Context){
        super.loadImages(context)

    }

    //TODO alter for reward, duplicates, tier
    override fun updateCharacterImages(context: Context){
        super.updateCharacterImages(context)

        //card6 tier images
        if(xpCardsEquipped[3]){
            var card4Image = loadCardTierImage(context, tier, "card4")
            if (card4Image != null) {
                currentImage = card4Image
            }
        }
    }
    override fun loadPortraitImage(context:Context){
        portraitImage = context.resources.getDrawable(R.drawable.portrait_loku)
    }
}