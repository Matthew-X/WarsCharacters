package com.example.imperialassault

import android.content.Context
import android.graphics.Bitmap
import com.example.imperialassault.Character

class Character_drokkatta : Character {
    var card5Images = ArrayList<Bitmap?>()
    constructor(context: Context){
        //default values
        name = "Drokkatta"
        name_short = "drokkatta"
        type = "Hero"
        defence_dice = "black"

        strength = "BGG"
        insight = "BB "
        tech = "GG "

        strengthWounded = "BRG"
        insightWounded  = "BR "
        techWounded  = "RG "

        background = "interior"
        health_default = 13
        endurance_default = 4
        speed_default = 4

        health = 13
        endurance = 4
        speed = 4

        totalXP = 0
        damage = 0
        strain = 0
        token = 0
        wounded = 0

        xpScores = intArrayOf(1,1,2,2,3,3,4,4,0)
        xpEndurances = intArrayOf(0,0,0,0,0,0,0,0,0)
        xpHealths = intArrayOf(2,0,0,0,0,0,0,0,0)
        xpSpeeds = intArrayOf(0,0,0,0,0,0,0,0,0)

        portraitRow = 4
        portraitCol = 3
        //Get Images
        //Update images

        //update strain, update damage, xp, cards, weapons

        loadImages(context)

        bio_title = "Euphoric Demolitionist"
        bio_quote = "\"Bathe in Fire!\""
        bio_text ="Drokkatta was trained on Kashyyyk as a demolitions expert during the Clone Wars, talented in all things that go “boom”; the sound of an explosion being a favorite noise in the world. " +
                "\n\nThe Imperials took Drokkatta's home, took their freedom, and family, but " +
                "they would never take away the spark of rebellion garnered in Drokkatta's heart. " +
                "One day they would see the Empire go “boom” for what they had done. Drokkatta prefers to wield a MGL-9 Thermo-Grenade Launcher, titled “Boomer”, a devastating, concussive weapon provided by the rebels for use in taking down lightly armored vehicles, gun emplacements, and structures. " +
                "\n\nDrokkatta works hard for the alliance to restore the Republic, and with " +
                "their backing, The Wookie will one day return to rescue fellow friends trapped in the labor camps on Geonosis."   }

    //TODO alter for reward, duplicates, tier
    override fun loadImages(context: Context){
        super.loadImages(context)
        card5Images = loadCardTierImages(context,"card5")
    }

    //TODO alter for reward, duplicates, tier
    override fun updateCharacterImages(){
        super.updateCharacterImages()


        //card6 tier images
        if(xpCardsEquipped[4]){
            if(card5Images[tier] != null){
                currentImage = card5Images[tier]
            }
        }
    }
}