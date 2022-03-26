package com.glasswellapps.iact.character_screen.views

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.glasswellapps.iact.R
import com.glasswellapps.iact.character_screen.types.EffectTypes
import com.glasswellapps.iact.characters.Character
import com.glasswellapps.iact.effects.Sounds
import kotlin.random.Random

class DamageAnimationEffects (val context: Context,
                              val damageAnimation:ImageView,
                              val character_images:FrameLayout,
                              val rest_animation: FrameLayout){

    //private var damageAnimation: ImageView = characterScreen.damage_animation
    //var character = characterScreen.character
    //var character_images: FrameLayout = characterScreen.character_images
    var blastAnim: AnimationDrawable? = null
    var impactAnim: AnimationDrawable? = null
    var sliceAnim: AnimationDrawable? = null
    var lastAnim: AnimationDrawable? = null

    init{
        //rest_animation.setBackgroundDrawable(MainActivity.restAnim)
        //rest_animation.visibility = FrameLayout.INVISIBLE
        blastAnim = context.resources.getDrawable(R.drawable.blast) as AnimationDrawable
        impactAnim = context.resources.getDrawable(R.drawable.impact) as AnimationDrawable
        sliceAnim = context.resources.getDrawable(R.drawable.slice) as AnimationDrawable
    }

    private fun resetAnim() {
        lastAnim?.stop()
        lastAnim?.selectDrawable(0)
    }
    private fun playAnim(anim: AnimationDrawable, damageAnimation:ImageView){
        resetAnim()
        damageAnimation.setImageDrawable(anim)
        anim.start()
        lastAnim = anim
    }
    fun playDamageAnim(isStrainDamage:Boolean,character:Character) {
        if (character.damageAnimSetting) {
            damageAnimation.visibility = View.VISIBLE
            if(!isStrainDamage) {
                val animType = (Math.random()*3+1).toInt()
                playAnim(animType,character)
            }
            else{
                playAnim(EffectTypes.STRAIN_DAMAGE,character)
            }
        }
        else{
            playAnim(EffectTypes.HIT,character)
        }
    }

    fun playAnim(index:Int, character:Character){
        when(index){
            EffectTypes.BLAST ->playBlastAnim()
            EffectTypes.SLICE->playSliceAnim()
            EffectTypes.IMPACT->playImpactAnim()
            EffectTypes.REST->playRestAnim(character)
            EffectTypes.STRAIN->playStrainAnim(character)
            EffectTypes.STRAIN_DAMAGE->playStrainDamageAnim(character)
            EffectTypes.HIT->playHit()
        }
        character.lastEffect = index
    }
    fun playBlastAnim(){
        Sounds.damagedSound(context, Sounds.blaster_gaster_blaster_master)
        if (blastAnim != null) {
            playAnim(blastAnim!!,damageAnimation)
        }
        hitAnim()
    }
    fun playSliceAnim(){
        Sounds.damagedSound(context, Sounds.melee_slice)
        if (sliceAnim != null) {
            playAnim(sliceAnim!!,damageAnimation)
        }
        hitAnim()
    }
    fun playImpactAnim(){
        Sounds.damagedSound(context, Sounds.melee_impact)
        if (impactAnim != null) {
            playAnim(impactAnim!!,damageAnimation)
        }
        hitAnim()
    }
    private fun playStrainAnim(character: Character) {
        rest_animation.setBackgroundColor(Color.WHITE)
        Sounds.strainSound()
        if (character.damageAnimSetting) {
            var anim = ObjectAnimator.ofFloat(rest_animation, "alpha", 0f,
                .5f, 0.3f, 0.1f,
                0f)
            anim.duration = 300
            anim.start()
        }
    }

    private fun playStrainDamageAnim(character: Character) {
        playStrainAnim(character)
        hitAnim()
        Sounds.damagedSound(context, Sounds.melee_impact)
    }
    private fun playHit() {
        hitAnim()
        Sounds.damagedSound(context, Sounds.melee_impact)
    }

    fun hitAnim(){
        var hitY = ObjectAnimator.ofFloat(
            character_images, "translationY", 0f, 20f * Random
                .nextFloat(),
            0f, 20f * Random.nextFloat(), 0f, 20f * Random.nextFloat(), 0f
        )
        hitY.duration = 300
        hitY.start()

        var hitX = ObjectAnimator.ofFloat(
            character_images, "translationX", 0f, -10f * Random
                .nextFloat(),
            0f, -10f * Random.nextFloat(), 0f, -10f * Random.nextFloat(), 0f
        )
        hitX.duration = 300
        hitX.start()
    }

    fun playRestAnim(character:Character) {
        rest_animation.setBackgroundColor(Color.GREEN)
        Sounds.strainSound()
        if (character.damageAnimSetting) {
            var restAnim = ObjectAnimator.ofFloat(rest_animation, "alpha", 0f,
                .2f, 0.1f, 0.05f,
                0f)
            restAnim.duration = 500
            restAnim.start()
        }
    }
}