package com.glasswellapps.iact.characters

import android.content.Context
import com.glasswellapps.iact.R

class Character_kotun : Character {

    constructor(context: Context){
        //default values
        name = "Ko-Tun Feralo"
        name_short = "kotun"
        index = 10
        type = "Hero"
        defence_dice = "black"

        strength = "B  "
        insight = "BGY"
        tech = "BG "

        strengthWounded = "E  "
        insightWounded  = "BRG"
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
        xpHealths = intArrayOf(0,0,0,0,0,2,0,0,0)
        xpSpeeds = intArrayOf(0,0,0,0,0,1,0,0,0)

        portraitRow = 4
        portraitCol = 0
        //Get Images
        //Update images

        //update strain, update damage, xp, cards, weapons


        bio_title = "Tenacious Quartermaster"
        bio_quote = "\"Any chance to gain advantage must be seized.\""
        bio_text ="Tactical, savvy, and prepared to face the unexpected (having learned the hard way) Ko-Tun Feralo is a steadfast ally in the thick of combat. " +
                "Now dubbed-“the Merchant of Death” by her comrades, Ko-Tun has worked her way to the top of her field by sheer effort and hard work. " +
                "\n\nIn the early days as a scout sniper Ko-Tun racked up 31 confirmed Imperial " +
                "Officer kills across the galaxy without the use of a spotter, with her trusty Service Rifle- a standard issue marksman rifle that she has effectively mastered. " +
                "Dutiful, honor-bound, brave, and keen-eyed she helps bring some added perspective and firepower to her new unit. " +
                "\n\nHaving worked as a merchant for so long, Ko-Tun has learned the value of " +
                "being adaptable; commonly bringing large reserves of ammo, and weapon modifications for her squad on each outing. " +
                "In her spare time Ko-Tun focuses on new business deals for Gideon's Cell and the Rebellion at large, or more intel on her MIA father. " +
                "Sahm-Ken will not remain a ghost for long as Ko-Tun plans to remain ever-vigilant."    }

    //TODO alter for reward, duplicates, tier
    override fun loadPortraitImage(context:Context){
        portraitImage = context.resources.getDrawable(R.drawable.portrait_kotun)
    }

    override fun updateCharacterImages(context: Context){
        super.updateCharacterImages(context)

        if(reinforcedHelmet){
            layer2 = getBitmap(context, "characters/" + name_short + "/images/reinforced_helmet.png")
        }
        else{
            layer2 = null
        }

        if(combatVisor){
            layer1 = getBitmap(context, "characters/" + name_short + "/images/combat_visor.png")
        }
        else{
            layer1 = null
        }
    }
}