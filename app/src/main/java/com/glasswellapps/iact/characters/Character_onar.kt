package com.glasswellapps.iact.characters

import android.content.Context
import com.glasswellapps.iact.R

class Character_onar : Character {

    constructor(context: Context) {
        //default values
        name = "Onar Koma"
        name_short = "onar"
        index = 15
        type = "Hero"
        defence_dice = ""

        strength = "BYY"
        insight = "GY "
        tech = "BR "

        strengthWounded = "BRY"
        insightWounded = "GR "
        techWounded = "RR "

        background = "interior"
        health_default = 20
        endurance_default = 4
        speed_default = 4

        health = 20
        endurance = 4
        speed = 4

        totalXP = 0
        damage = 0
        strain = 0
        token = 0
        wounded = 0

        xpScores = intArrayOf(1, 1, 2, 2, 3, 3, 4, 4, 0)
        xpEndurances = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
        xpHealths = intArrayOf(0, 0, 0, 2, 0, 0, 3, 0, 0)
        xpSpeeds = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
        xpItems = intArrayOf(-1,-1,-1,-1,-1,-1,100,-1,-1)

        portraitRow = 3
        portraitCol = 2
        //Get Images
        //Update images

        //update strain, update damage, xp, cards, weapons


        bio_title = "Marauding Mutant"
        bio_quote = "\"I do my best work when I stay in harm's way.\""
        bio_text = "The Rebellion has ties to many smugglers, bandits, thieves, mercenaries, and people of less than stellar reputation. " +
                "Mysterious figures who are not official Alliance operatives, but who are sympathetic to the Rebel cause and will occasionally do errands or favors for the right price." +
                "\n\nOnar Koma is not good creature, in fact he is a very bad one; like most of the Aqualish race -he is gruff, brash, cruel, and impatient. " +
                "Onar has a bad reputation for being quick to act and quicker to anger; one must choose their words carefully around this aggressive titan or face his wrath." +
                "\n\nOnar revels in combat, being amongst the strongest Brawlers in the galaxy, " +
                "easily weathering the many blows his enemies land on his Durasteel-hard hide. " +
                "His favorite weapons are his fists, though he is also partial to the " +
                "Bodyguard " +
                "Rifle issued to him in the early days of \"Service\" to the Black Sun. The " +
                "\n\nRebels don’t trust him, yet, need any help they can get; regrettably beggars" +
                " can’t be choosers when it comes to galactic freedom. " +
                "High Command has determined it is better to keep tabs on Onar and attempt to " +
                "minimize the chaos. The enemy of my enemy is my friend; at least for now…"
    }
    //TODO alter for reward, duplicates, tier
    override fun loadImages(context: Context){
        super.loadImages(context)

    }

    //TODO alter for reward, duplicates, tier
    override fun updateCharacterImages(context: Context){
        super.updateCharacterImages(context)

        //card6 tier images
        if(xpCardsEquipped[6]){
            var card7Image = loadCardTierImage(context, tier, "card7")
            if (card7Image != null) {
                currentImage = card7Image
            }
        }

        if(mandoHelmet){
            layer2 = getBitmap(context, "characters/" + name_short + "/images/helmet_mando.png")
        }
        else{
            layer2 = null
        }
    }
    override fun loadPortraitImage(context:Context){
        portraitImage = context.resources.getDrawable(R.drawable.portrait_onar)
    }
}