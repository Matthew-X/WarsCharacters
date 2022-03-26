package com.glasswellapps.iact.characters

import android.content.Context
import com.glasswellapps.iact.R

class Character_gideon : Character {

    constructor(context: Context){
        //default values
        name = "Gideon Argus"
        name_short = "gideon"
        index = 7
        type = "Hero"
        defence_dice = "black"

        strength = "BG "
        insight = "BGY"
        tech = "BG "

        strengthWounded = "BR "
        insightWounded  = "BGR"
        techWounded  = "BR "

        background = "interior"
        health_default = 10
        endurance_default = 5
        speed_default = 4

        health = 10
        endurance = 5
        speed = 4

        totalXP = 0
        damage = 0
        strain = 0
        token = 0
        wounded = 0

        xpScores = intArrayOf(1,1,2,2,3,3,4,4,0)
        xpEndurances = intArrayOf(0,0,0,0,0,0,0,0,0)
        xpHealths = intArrayOf(0,0,2,0,0,0,0,0,0)
        xpSpeeds = intArrayOf(0,0,0,0,0,0,0,0,0)

        portraitRow = 1
        portraitCol = 1
        //Get Images
        //Update images

        //update strain, update damage, xp, cards, weapons


        bio_title = "Commanding Officer"
        bio_quote = "\"Expect nothing. Anticipate everything.\""
        bio_text ="Gideon’s weapon of choice is a concealable Holdout Blaster, a memento from his wife to keep him safe until he can return to her. " +
                "\n\nDue to having spent his entire life as a soldier, Gideon has gained an " +
                "almost second sense for avoiding traps and ambushes. " +
                "Gideon himself is a formidable soldier in his own right, and his older age does not prevent him from fighting beside his men on the front lines. " +
                "\n\nYet war does take it's toll; the many losses of friends and allies over the " +
                "years have served to strengthen Gideon's resolve, but has also left him world-weary and fatalistic in mentality. " +
                "Despite his jaded mentality, Gideon always presents himself to his men as dignified, resolute, brave, and unwavering. " +
                "\n\nGideon's greatest strength is his ability to command and inspire others, " +
                "using his tactical knowledge to focus their efforts and embolden them to victory. " +
                "Soldiers are easily drawn to his charisma and know that with Gideon leading the way, they would always win the day."   }

    //TODO alter for reward, duplicates, tier
    override fun loadPortraitImage(context:Context){
        portraitImage = context.resources.getDrawable(R.drawable.portrait_gideon)
    }
}