package com.glasswellapps.iact.characters

import android.content.Context
import com.glasswellapps.iact.R

class Character_davith : Character {
    constructor(context: Context) {
        //default values
        name = "Davith Elso"
        name_short = "davith"
        index = 2
        type = "Hero"
        defence_dice = "white"

        strength = "BG "
        insight = "BGY"
        tech = "B  "

        strengthWounded = "BR "
        insightWounded = "BGR"
        techWounded = "R  "

        background = "interior"
        health_default = 11
        endurance_default = 4
        speed_default = 4

        health = 11
        endurance = 4
        speed = 4


        damage = 0
        strain = 0
        token = 0
        wounded = 0

        totalXP = 0
        xpEndurances = intArrayOf(0, 0, 0, 0, 0, 0, 0, 1, 0)
        xpHealths = intArrayOf(0, 0, 0, 0, 0, 0, 0, 3, 0)
        xpSpeeds = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
        xpItems = intArrayOf(-1,-1,-1,-1,-1,92,-1,-1,91)

        portraitRow = 1
        portraitCol = 0
        //Get Images
        //Update images

        //update strain, update damage, xp, cards, weapons

        bio_title = "Exiled Jedi"
        bio_quote = "\"Once discovered, the best defense is an unrelenting assault.\""
        bio_text = "Due to the roving Jedi-Hunters still searching for him and his ilk; " +
                "Davith decided to set aside his Lightsaber for now, keeping it hidden somewhere secret and safe until necessary. " +
                "He instead brandishes a deadly Heirloom Dagger when facing his foes in combat; the blade being the only memento he has left of his parents before he joined the Jedi order. " +
                "\n\nHe is quite skilled in the ways of the force; able to alter the movement of " +
                "his enemies, and increase his own inherent talents to an advanced level. When enhanced by the force, Elso’s Speed allows him to dart amongst his foes without them even comprehending his movements. " +
                "Davith has also been trained to use the force to Mask His Presence from his enemies Eyesight and Cloud The Minds of the unwitting. " +
                "\n\nHaving successfully taken down a huge number of the Imperial leadership, " +
                "Davith is a true threat to the Empire at large. \"Hawkbat\" will find vengeance for his friends, and one day the Emperor will fall- for all of his crimes. "
    }

    //TODO alter for reward, duplicates, tier
    override fun loadImages(context: Context) {
        super.loadImages(context)
    }

    //TODO alter for reward, duplicates, tier
    override fun updateCharacterImages(context: Context) {
        super.updateCharacterImages(context)
        //card6 and card9 combined tier images
        layerLightSaber = null

        val card6 = xpCardsEquipped[5] && !ancientLightSaber && !xpCardsEquipped[8]
        val card9 = !xpCardsEquipped[5] && !ancientLightSaber && xpCardsEquipped[8]
        val ancient = !xpCardsEquipped[5] && ancientLightSaber && !xpCardsEquipped[8]
        val card6Card9 = xpCardsEquipped[5] && !ancientLightSaber && xpCardsEquipped[8]
        val card6Ancient = xpCardsEquipped[5] && ancientLightSaber && !xpCardsEquipped[8]
        val card9Ancient = !xpCardsEquipped[5] && ancientLightSaber && xpCardsEquipped[8]
        val card6Card9Ancient = xpCardsEquipped[5] && ancientLightSaber && xpCardsEquipped[8]
        if (card6Card9) {
            var card69Image = loadCardTierImage(context, tier, "card6_card9")
            if (card69Image != null) {
                currentImage = card69Image
                layerLightSaber = getBitmap(context,
                    "characters/davith/images/lightsaber_card6.png");
            }
        }
        if (card6Card9Ancient) {
            var card69Image = loadCardTierImage(context, tier, "card6_card9")
            if (card69Image != null) {
                currentImage = card69Image
                layerLightSaber = getBitmap(context,
                    "characters/davith/images/lightsaber_card6.png");
            }
        }
        //card6 tier images
        if (card6Ancient) {

            var card6Image = loadCardTierImage(context, tier, "card6_ancient_light_saber")
            if (card6Image != null) {
                currentImage = card6Image
                if (!xpCardsEquipped[8])
                    layerLightSaber = getBitmap(
                        context,
                        "characters/davith/images/lightsaber_ancient_card6.png"
                    );
            }
        }
        if (card6) {
            var card6Image = loadCardTierImage(context, tier, "card6")
            if (card6Image != null) {
                currentImage = card6Image
                layerLightSaber = getBitmap(context, "characters/davith/images/lightsaber_card6.png");
            }
        }

        //card9 tier images
        if (card9Ancient) {
                var card9Image = loadCardTierImage(context, tier, "card9_ancient_light_saber")
                if (card9Image != null) {
                    currentImage = card9Image
                    layerLightSaber = getBitmap(context,
                        "characters/davith/images/lightsaber_ancient.png");
                }
            }
        if(card9) {
            var card9Image = loadCardTierImage(context, tier, "card9")
            if (card9Image != null) {
                currentImage = card9Image
            }
        }

        if(ancient){
            var ancientLightSaberImage = loadCardTierImage(context, tier, "ancient_light_saber")
            if (ancientLightSaberImage != null) {
                currentImage = ancientLightSaberImage
                layerLightSaber = getBitmap(context,
                    "characters/davith/images/lightsaber_ancient.png");
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
        portraitImage = context.resources.getDrawable(R.drawable.portrait_davith)
    }


}