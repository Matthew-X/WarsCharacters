package com.glasswellapps.iact.characters

import android.content.Context
import com.glasswellapps.iact.R

class Character_mhd19 : Character {

    constructor(context: Context){
        //default values
        name = "MHD-19"
        name_short = "mhd19"
        index = 13
        type = "Hero"
        defence_dice = "black"

        strength = "B  "
        insight = "BG "
        tech = "BGY"

        strengthWounded = "R  "
        insightWounded  = "BR "
        techWounded  = "BGR"

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
        xpHealths = intArrayOf(0,0,0,0,0,0,0,0,3)
        xpSpeeds = intArrayOf(0,0,0,1,0,0,0,0,0)
        xpItems = intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1,98)

        portraitRow = 1
        portraitCol = 2
        //Get Images
        //Update images

        //update strain, update damage, xp, cards, weapons


        bio_title = "Mechanical Medic"
        bio_quote = "Maker: REDACTED\n\nModel: 2-1B_Surgical_Droid\n\nProgramming: Masculine  " +
                    "\n\n\"I will do what I must to protect my fellow soldiers.\""
        bio_text ="\"Doc's\" past is checkered at best, having been responsible for cracking the " +
                "psyche and skulls of many fools who attempted to resist the Black Suns aims; " +
                "these Files are rarely utilized, but MHD-19 has been known to \"Turn The Safety " +
                "Switch- Off\" when necessary. \n\nMHD-19 prefers to use a hidden Sidearm Blaster" +
                " when encountered in combat; " +
                "the weapon easily merges with MHD's chassis to help remain incognito and prevent the weapons discovery. " +
                "\n\n\"Doc\" also benefits from the increased Strength, Durability, and " +
                "unfettered " +
                "Agility that only a droid may posses- compared to a normal organic being, with a " +
                "\"Mind\" so brilliant, \"Doc\" is directly responsible for discovering cures for" +
                " many of the most dangerous diseases found across the galaxy. \n\nThough " +
                "primarily a" +
                " medical unit, \"Doc\" commonly Upgrades itself to remain adaptable in any situation, " +
                "regularly uploading vast amounts of information to its Reference Database to expand its abilities; " +
                "only being restrained by the \"Shell\" it wears and the parts that make up its \"innards.\" " +
                "\n\nLoyal, intelligent, cocky, and robust, \"Doc\" is the stitching that holds " +
                "its team together."
    }

    //TODO alter for reward, duplicates, tier
    override fun loadImages(context: Context){
        super.loadImages(context)

    }

    //TODO alter for reward, duplicates, tier
    override fun updateCharacterImages(context: Context){
        super.updateCharacterImages(context)
        if(tier == 3){
            if(reinforcedHelmet || combatVisor || xpCardsEquipped[8]){
                var helmetImage = loadCardTierImage(context, tier, "helmet")
                if (helmetImage != null) {
                    currentImage = helmetImage
                }
            }
        }
        else if(xpCardsEquipped[8]){
            var card9Image = loadCardTierImage(context, tier, "card9")
            if (card9Image != null) {
                currentImage = card9Image
            }
        }


    }
    override fun loadPortraitImage(context:Context){
        portraitImage = context.resources.getDrawable(R.drawable.portrait_mhd)
    }
}