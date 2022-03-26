package com.glasswellapps.iact.characters

import android.content.Context
import com.glasswellapps.iact.R

class Character_saska : Character {

    constructor(context: Context){
        //default values
        name = "Saska Teft"
        name_short = "saska"
        index = 16
        type = "Hero"
        defence_dice = "white"

        strength = "B  "
        insight = "BG "
        tech = "BGY"

        strengthWounded = "R  "
        insightWounded  = "BR "
        techWounded  = "BGR"

        background = "interior"
        health_default = 11
        endurance_default = 4
        speed_default = 5

        health = 11
        endurance = 4
        speed = 5

        totalXP = 0
        damage = 0
        strain = 0
        token = 0
        wounded = 0

        xpScores = intArrayOf(1,1,2,2,3,3,4,4,0)
        xpEndurances = intArrayOf(0,0,0,0,0,0,0,1,0)
        xpHealths = intArrayOf(0,0,0,0,0,0,0,2,0)
        xpSpeeds = intArrayOf(0,0,0,0,0,0,0,0,0)
        xpItems = intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1,101)

        portraitRow = 0
        portraitCol = 3
        //Get Images
        //Update images

        //update strain, update damage, xp, cards, weapons


        bio_title = "Enigmatic Engineer"
        bio_quote = "\"As much as I like putting things together, I also like blowing them apart.\""
        bio_text ="The Rebellion has ties to many smugglers, bandits, thieves, mercenaries, and people of less than stellar reputation. " +
                "Mysterious figures who are not official Alliance operatives, but who are sympathetic to the Rebel cause and will occasionally do errands or favors for the right price." +
                "\n\nSaska prefers to use a heavily Modified Blaster of her own creation while in" +
                " combat, as well as, a Comprehensive Tool Kit, and the many varied and useful gadgets she has built over the years. " +
                "These items range from Personal Energy Shield Devices, to Adrenal injectors, and even Explosives. " +
                "Saska’s inventions, while many times unconventional, can mean the difference between success and failure in the hands of the resourceful; " +
                "though none is more resourceful in a fight than Saska herself, who uses her own devices and illegal weapon modifications to wreak havoc on the Imperial forces. " +
                "\n\nThe Imperials brought her wrath upon themselves; they will soon realize how " +
                "poorly equipped they are by comparison. Few minds can stand up to hers in the galaxy and the Tech Fields are Saska's domain."}

    //TODO alter for reward, duplicates, tier
    override fun loadImages(context: Context){
        super.loadImages(context)
    }

    //TODO alter for reward, duplicates, tier
    override fun updateCharacterImages(context: Context){
        super.updateCharacterImages(context)

        //card9 tier images
        if(xpCardsEquipped[8] || combatVisor){
            var card9Image = loadCardTierImage(context, tier, "card9")
            if (card9Image != null) {
                currentImage = card9Image
            }
        }
    }
    override fun loadPortraitImage(context:Context){
        portraitImage = context.resources.getDrawable(R.drawable.portrait_saska)
    }
}