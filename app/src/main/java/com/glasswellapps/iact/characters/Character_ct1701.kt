package com.glasswellapps.iact.characters

import android.content.Context
import android.graphics.Bitmap
import com.glasswellapps.iact.R

class Character_ct1701 : Character {
    var tier1duplicate:Bitmap? = null
    var tier2duplicate:Bitmap? = null
    var tier3duplicate:Bitmap? = null

    var tier1helmet:Bitmap? = null
    var tier2helmet:Bitmap? = null
    var tier3helmet:Bitmap? = null

    constructor(context: Context){
        //default values
        name = "CT-1701"
        name_short = "ct1701"
        index = 1
        type = "Hero"
        defence_dice = "black"

        strength = "BY "
        insight = "G  "
        tech = "BGG"

        strengthWounded = "BR "
        insightWounded  = "R  "
        techWounded  = "BRG"

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
        xpHealths = intArrayOf(0,0,0,0,0,0,0,0,0)
        xpSpeeds = intArrayOf(0,0,0,0,0,0,0,0,0)

        portraitRow = 2
        portraitCol = 3
        //Get Images
        //Update images

        //update strain, update damage, xp, cards, weapons


        bio_title = "Wildfire"
        bio_quote = "\"Lock and load.\""
        bio_text ="Having seen service since the earliest days of the Clone Wars, \"Wildfire\" " +
                "has never truly adapted to the current methods of warfare; he still prefers to " +
                "use a gun as old as he is: the DC-15S Blaster- \"Why fix what isn't broken.\"" +
                "\n\n \"Wildfire's\" many failures and regrets over the course of his life have ever" +
                "haunted his thoughts, catalyzing the valiant soldier into a very embittered and " +
                "troubled man; his role to play in a fellow soldiers death ever haunting his dreams. " +
                "Forever after, \"Wildfire\" would come to feel closed-off, ashamed, and dejected" +
                " upon meeting a Survivor of the Jedi Order amongst the Rebellion. " +
                "\n\nHis first stop post desertion, took him to the planet Rodia to pay respects " +
                "at his departed Commander's mausoleum; tears fell from his eyes during the event for the first time in many long years. " +
                "After inevitably hitting his breaking point with the Empire, \"Wildfire\" would " +
        "come to develop intolerance for subordination to a cause he couldn't believe in. " +
                "He would forge his own destiny, without the malicious direction of enslaving " +
                "\"masters\", who viewed the remaining Clone troopers as expendable, outdated, " +
                "relics- to be discarded at a whim. " +
                "\n\nDour, Steadfast, and pugnacious as always, \"Wildfire\" has really only ever" +
                " felt at home on a battlefield; a wolf ever searching for the next hunt. " +
                "\"Wildfire\" has decided his waning years wouldn't be spent in leisure; this old" +
                " soldier is determined to die in combat, for glory, for his friends, and for a noble purpose of his own choosing." }


    //TODO alter for reward, duplicates, tier
    override fun updateCharacterImages(context: Context){
        super.updateCharacterImages(context)

        if(tier == 1){
            if(storeRandom<0.5){
                currentImage = getBitmap(context, "characters/ct1701/images/tier1image_duplicate.png")
            }
        }
        else if(tier == 2){
            if(storeRandom<0.5){
                currentImage = getBitmap(context, "characters/ct1701/images/tier2image_duplicate.png")
            }
        }
        else if(tier == 3){
            if(storeRandom<0.5){
                currentImage = getBitmap(context, "characters/ct1701/images/tier3image_duplicate.png")
            }
        }

        //todo helmet
        if(mandoHelmet || reinforcedHelmet || combatVisor){
            if(tier == 3) {
                layer2 = getBitmap(context, "characters/" + name_short + "/images/tier3_helmet.png")
            }
            else if(tier == 2){
                layer2 = getBitmap(context, "characters/" + name_short + "/images/tier2_helmet.png")
            }
            else {
                layer2 = getBitmap(context, "characters/" + name_short + "/images/tier1_helmet.png")
            }
        }
        else{
            layer2 = null
        }
    }
    override fun loadPortraitImage(context:Context){
        portraitImage = context.resources.getDrawable(R.drawable.portrait_ct)
    }
}