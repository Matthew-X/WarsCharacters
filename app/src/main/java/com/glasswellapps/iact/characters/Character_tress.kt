package com.glasswellapps.iact.characters

import android.content.Context
import com.glasswellapps.iact.R

class Character_tress : Character {

    constructor(context: Context){
        //default values
        name = "Tress Hacnua"
        name_short = "tress"
        index = 18
        type = "Hero"
        defence_dice = "white"

        strength = "GY "
        insight = "GG "
        tech = "BB "

        strengthWounded = "GR "
        insightWounded  = "GR "
        techWounded  = "RB "

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
        xpEndurances = intArrayOf(0,0,0,0,0,0,0,0,0)
        xpHealths = intArrayOf(0,0,0,0,2,0,0,0,0)
        xpSpeeds = intArrayOf(0,0,0,0,0,0,0,0,0)

        portraitRow = 2
        portraitCol = 3
        //Get Images
        //Update images

        //update strain, update damage, xp, cards, weapons

        bio_title = "Cybernetic Brawler"
        bio_quote = "\"Safety in numbers is a bedtime story your commander told you so you can sleep at night.\""
        bio_text ="The Rebellion has ties to many smugglers, bandits, thieves, mercenaries, and people of less than stellar reputation. Mysterious figures who are not official Alliance operatives, but who are sympathetic to the Rebel cause and will occasionally do errands or favors for the right price. " +
                "\n\nAfter years of practice, Tress has now well adapted to her mechanized appendages. " +
                "She has mastered the art of dexterous movement, and is freely able to operate much of her body without severely impacting her own stamina reserves. " +
                "Tress found that she could Strike Harder, and move much faster than the average opponent, receiving a distinct advantage in combat. " +
                "\n\nOver time Tress has developed into a determined, witty, and cocky woman with" +
                " a passion for her martial arts craft; empowered by the flow of combat and the achievement she gains from conquering a worthy challenge. " +
                "Her studies have honed her mind, body, and spirit to endure. Never forgetting her many losses in life, either physically or of her loved ones, " +
                "Tress vowed to never be helpless again. \n\nShe is a warrior, reforged in " +
                "hardship," +
                " and has a monster of her own to slay before she's done. " +
                "She hasn't forgotten the beast that ruined her life; one day Tress plans to return to her homeworld and face the creature that cost her so dearly..."    }

    //TODO alter for reward, duplicates, tier
    override fun loadPortraitImage(context:Context){
        portraitImage = context.resources.getDrawable(R.drawable.portrait_tress)
    }
}