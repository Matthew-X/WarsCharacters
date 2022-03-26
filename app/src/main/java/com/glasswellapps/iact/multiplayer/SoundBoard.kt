package com.glasswellapps.iact.multiplayer

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.Window
import com.glasswellapps.iact.R
import com.glasswellapps.iact.character_screen.controllers.ButtonPressedHandler
import com.glasswellapps.iact.effects.LightSaberMotionController
import com.glasswellapps.iact.effects.SoundSelector
import com.glasswellapps.iact.effects.Sounds
import kotlinx.android.synthetic.main.dialog_sounds_creature.*
import kotlinx.android.synthetic.main.dialog_sounds_humanoid.*

class SoundBoard(
    context:Activity,
    ranged:View,
    melee:View,
    alien:View,
    creature:View,
    droid:View,
    droidDeath:View,
    trooper:View,
    trooperDeath:View,
    moving:View,
    terminal:View,
    door:View,
    crate:View,
    val lightSaberAttack:View,
    val lightSaberIgnite:View,
    val isImperial:Boolean) {

    private val lightMotionController = LightSaberMotionController(context)

    private val rangedSoundIDs = intArrayOf(
        Sounds.blaster_gaster_blaster_master,
        Sounds.blaster_atat,
        Sounds.blaster_boba,
        Sounds.blaster_explosion,
        Sounds.blaster_rebel,
        Sounds.blaster_tie)

    private val rangedFrequencies = intArrayOf(2,1,2,2,1,2)
    private val rangedSounds: SoundSelector =
        SoundSelector(
            rangedSoundIDs,
            rangedFrequencies
        )

    private val meleeSoundIDs = intArrayOf(
        Sounds.melee_slice,
        Sounds.melee_impact,
        Sounds.melee_punch,
        Sounds.melee_woosh)
    private val meleeFrequencies = intArrayOf(1,1,1,1)
    private val meleeSounds: SoundSelector =
        SoundSelector(
            meleeSoundIDs,
            meleeFrequencies
        )

    private val droidDeathSoundIDs = intArrayOf(
        Sounds.droid_hit,
        Sounds.droid_shot,
        Sounds.droid_dead)
    private val droidDeathFrequencies = intArrayOf(3,3,1)
    private val droidDeathSounds: SoundSelector =
        SoundSelector(
            droidDeathSoundIDs,
            droidDeathFrequencies
        )

    private val droidSoundIDs = intArrayOf(
        Sounds.droid,
        Sounds.droid_move,
        Sounds.droid_talk)
    private val droidFrequencies = intArrayOf(1,1,1)
    private val droidSounds: SoundSelector =
        SoundSelector(
            droidSoundIDs,
            droidFrequencies
        )

    private val trooperSoundIDs = intArrayOf(
        Sounds.stormtrooper_blastem,
        Sounds.stormtrooper_copy_that,
        Sounds.stormtrooper_dont_move,
        Sounds.stormtrooper_hey,
        Sounds.stormtrooper_intruder)
    private val trooperFrequencies = intArrayOf(1,1,1,1,1)
    private val trooperSounds: SoundSelector =
        SoundSelector(
            trooperSoundIDs,
            trooperFrequencies
        )

    private val trooperDeathSoundIDs = intArrayOf(
        Sounds.stormtrooper_death,
        Sounds.stormtrooper_death1,
        Sounds.stormtrooper_death2,
        Sounds.stormtrooper_death_wilhelm)
    private val trooperDeathFrequencies = intArrayOf(2,2,2,1)
    private val trooperDeathSounds: SoundSelector =
        SoundSelector(
            trooperDeathSoundIDs,
            trooperDeathFrequencies
        )

    private val movingSoundIDs = intArrayOf(Sounds.moving);
    private val movingFrequencies = intArrayOf(1)
    private val movingSounds: SoundSelector =
        SoundSelector(
            movingSoundIDs,
            movingFrequencies
        )

    private val terminalSoundIDs = intArrayOf(
        Sounds.terminal,
        Sounds.terminal_button_low,
        Sounds.terminal_chirp,
        Sounds.terminal_loading,
        Sounds.terminal_button);
    private val terminalFrequencies = intArrayOf(1,1,1,1,1)
    private val terminalSounds: SoundSelector =
        SoundSelector(
            terminalSoundIDs,
            terminalFrequencies
        )

    private val doorSoundIDs = intArrayOf(Sounds.door, Sounds.door2);
    private val doorFrequencies = intArrayOf(2,1)
    private val doorSounds: SoundSelector =
        SoundSelector(
            doorSoundIDs,
            doorFrequencies
        )

    private val crateSoundIDs = intArrayOf(Sounds.crate, Sounds.crate_beep);
    private val crateFrequencies = intArrayOf(1,1)
    private val crateSounds: SoundSelector =
        SoundSelector(
            crateSoundIDs,
            crateFrequencies
        )

    private val lightSaberAttackSoundIDs = intArrayOf(
        Sounds.lightsaber_clash,
        Sounds.lightsaber_heavy_clash,
        Sounds.lightsaber_heavy_clash2,
        Sounds.lightsaber_quick_flurry,
        Sounds.lightsaber_heavy_flurry);
    private val lightSaberAttackFrequencies = intArrayOf(1,1,1,1,1)
    private val lightSaberAttackSounds: SoundSelector =
        SoundSelector(
            lightSaberAttackSoundIDs,
            lightSaberAttackFrequencies
        )
    val lightSaberSwingSoundIDs = intArrayOf(
        Sounds.lightsaber_stabby_stabby,
        Sounds.lightsaber_fast,
        Sounds.lightsaber_swing);
    private val lightSaberSwingFrequencies = intArrayOf(1,0,1)
    private val lightSaberSwingSounds: SoundSelector =
        SoundSelector(
            lightSaberSwingSoundIDs,
            lightSaberSwingFrequencies
        )

    val humanoidSoundDialog:Dialog = Dialog(context)
    val creatureSoundDialog:Dialog = Dialog(context)

    fun initHumanSoundDialog(){
        humanoidSoundDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        humanoidSoundDialog.setCancelable(false)
        humanoidSoundDialog.setContentView(R.layout.dialog_sounds_humanoid)
        humanoidSoundDialog.setCanceledOnTouchOutside(true)

        val jawaButton = humanoidSoundDialog.sound_jawa
        val gamoreanButton = humanoidSoundDialog.sound_gamorean
        val rhodianButton = humanoidSoundDialog.sound_rhodian
        val trandoshanButton = humanoidSoundDialog.sound_trandoshan
        val tuskenButton = humanoidSoundDialog.sound_tusken
        val wookieButton = humanoidSoundDialog.sound_wookie

        val jawaSounds = SoundSelector(
            intArrayOf(Sounds.alien_jawa, Sounds.alien_jawa1),
            intArrayOf(4, 1)
        )

        val jawaDeathButton = humanoidSoundDialog.death_jawa
        val gamoreanDeathButton = humanoidSoundDialog.death_gamorean
        val rhodianDeathButton = humanoidSoundDialog.death_rhodian
        val trandoshanDeathButton = humanoidSoundDialog.death_trandoshan
        val tuskenDeathButton = humanoidSoundDialog.death_tusken
        val wookieDeathButton = humanoidSoundDialog.death_wookie

        jawaButton.setOnClickListener { ButtonPressedHandler.onButtonPressed(jawaButton);
            jawaSounds.playRandom()}
        gamoreanButton.setOnClickListener { ButtonPressedHandler.onButtonPressed(gamoreanButton);Sounds.play(Sounds
            .alien_gamorean) }
        rhodianButton.setOnClickListener { ButtonPressedHandler.onButtonPressed(rhodianButton);
            Sounds.play(Sounds
            .alien_rhodian) }
        trandoshanButton.setOnClickListener { ButtonPressedHandler.onButtonPressed(trandoshanButton);
            Sounds.play(Sounds
            .alien_trandoshan) }
        tuskenButton.setOnClickListener { ButtonPressedHandler.onButtonPressed(tuskenButton);Sounds
            .play(Sounds
            .alien_tusken) }
        wookieButton.setOnClickListener { ButtonPressedHandler.onButtonPressed(wookieButton);Sounds
            .play(Sounds
            .alien_wookie) }

        jawaDeathButton.setOnClickListener { ButtonPressedHandler.onButtonPressed(jawaDeathButton);Sounds
            .play(Sounds
            .alien_jawa_death) }
        gamoreanDeathButton.setOnClickListener { ButtonPressedHandler.onButtonPressed(gamoreanDeathButton);Sounds.play(Sounds
            .alien_gamorean_death) }
        rhodianDeathButton.setOnClickListener { ButtonPressedHandler.onButtonPressed(rhodianDeathButton);Sounds.play(Sounds
            .alien_rhodian_death) }
        trandoshanDeathButton.setOnClickListener { ButtonPressedHandler.onButtonPressed(trandoshanDeathButton);Sounds.play(Sounds
            .alien_trandoshan_death) }
        tuskenDeathButton.setOnClickListener { ButtonPressedHandler.onButtonPressed(tuskenDeathButton);Sounds.play(Sounds
            .alien_tusken_death) }
        wookieDeathButton.setOnClickListener { ButtonPressedHandler.onButtonPressed(wookieDeathButton);Sounds.play(Sounds
            .alien_wookie_death) }

    }

    fun initCreatureSoundDialog(){
        creatureSoundDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        creatureSoundDialog.setCancelable(false)
        creatureSoundDialog.setContentView(R.layout.dialog_sounds_creature)
        creatureSoundDialog.setCanceledOnTouchOutside(true)

        val banthaButton = creatureSoundDialog.sound_bantha
        val rancorButton = creatureSoundDialog.sound_rancor
        val wampaButton = creatureSoundDialog.sound_wampa

        banthaButton.setOnClickListener { ButtonPressedHandler.onButtonPressed(banthaButton)
            Sounds.play(Sounds.creature_bantha) }
        rancorButton.setOnClickListener { ButtonPressedHandler.onButtonPressed(rancorButton)
            Sounds.play(Sounds.creature_rancor) }
        wampaButton.setOnClickListener { ButtonPressedHandler.onButtonPressed(wampaButton)
            Sounds.play(Sounds.creature_wampa) }


        val banthaDeathButton = creatureSoundDialog.death_bantha
        val rancorDeathButton = creatureSoundDialog.death_rancor
        val wampaDeathButton = creatureSoundDialog.death_wampa

        banthaDeathButton.setOnClickListener { ButtonPressedHandler.onButtonPressed(banthaDeathButton)
            Sounds.play(Sounds.creature_bantha_death) }
        rancorDeathButton.setOnClickListener { ButtonPressedHandler.onButtonPressed(rancorDeathButton)
            Sounds.play(Sounds.creature_rancor_death) }
        wampaDeathButton.setOnClickListener { ButtonPressedHandler.onButtonPressed(wampaDeathButton)
            Sounds.play(Sounds.creature_wampa_death) }
    }

    init{
        initHumanSoundDialog()
        initCreatureSoundDialog()

        ranged.setOnClickListener { onRanged()
        ButtonPressedHandler.onButtonPressed(ranged)}

        melee.setOnClickListener { onMelee()
            ButtonPressedHandler.onButtonPressed(melee)}

        door.setOnClickListener { onDoor()
            ButtonPressedHandler.onButtonPressed(door)}

        terminal.setOnClickListener { onTerminal()
            ButtonPressedHandler.onButtonPressed(terminal)}

        crate.setOnClickListener { onCrate()
            ButtonPressedHandler.onButtonPressed(crate)}

        moving.setOnClickListener { onMove()
            ButtonPressedHandler.onButtonPressed(moving)}

        droid.setOnClickListener { onDroid()
            ButtonPressedHandler.onButtonPressed(droid)}

        droidDeath.setOnClickListener { onDroidDeath()
            ButtonPressedHandler.onButtonPressed(droidDeath)}

        trooper.setOnClickListener { onTrooper()
            ButtonPressedHandler.onButtonPressed(trooper)}

        trooperDeath.setOnClickListener { onTrooperDeath()
            ButtonPressedHandler.onButtonPressed(trooperDeath)}

        alien.setOnClickListener { onHumanoid()
            ButtonPressedHandler.onButtonPressed(alien)}

        creature.setOnClickListener { onCreature()
            ButtonPressedHandler.onButtonPressed(creature)}

        lightSaberAttack.setOnClickListener { onLightSaberAttack() }

        lightSaberIgnite.setOnClickListener { onLightSaberIgnition() }
    }

    fun onRanged(){
        rangedSounds.playRandom()
    }
    fun onMelee(){
        meleeSounds.playRandom()
    }

    fun onTrooper(){
        trooperSounds.playRandom(1.5f)
    }
    fun onTrooperDeath(){
        trooperDeathSounds.playRandom(1.5f)
    }

    fun onDroid(){
        droidSounds.playRandom()
    }
    private fun onDroidDeath() {
        droidDeathSounds.playRandom()
    }

    fun onHumanoid(){
        Sounds.selectSound()
        humanoidSoundDialog.show()
    }
    fun onCreature(){
        creatureSoundDialog.show()
        Sounds.selectSound()
    }

    var isLightSaberOn = false
    fun onLightSaberIgnition(){
        if(!isLightSaberOn){
           turnLightSaberOn()
        }
        else{
           turnLightSaberOff()
        }
    }
    fun turnLightSaberOn(){
        lightSaberIgnite.animate().setDuration(200).alpha(1f)
        lightSaberAttack.animate().setDuration(200).alpha(1f)
        lightMotionController.startSound()
        Sounds.play(Sounds.lightSaber)
        Sounds.play(Sounds.lightsaber_hum)
        isLightSaberOn = true
    }
    fun turnLightSaberOff(){
        lightMotionController.stopSound()
        lightSaberIgnite.animate().setDuration(200).alpha(0.5f)
        lightSaberAttack.animate().setDuration(200).alpha(0.5f)
        Sounds.play(Sounds.lightsaber_off)
        isLightSaberOn = false
    }


    fun onLightSaberAttack(){
        if(isLightSaberOn) {
            lightSaberSwingSounds.playRandom()
            lightSaberAttackSounds.playRandom()
        }
    }
    fun onDoor(){
        doorSounds.playRandom()
    }
    fun onCrate(){
        crateSounds.playRandom()
    }
    fun onTerminal(){
        terminalSounds.playRandom()
    }
    fun onMove(){
        movingSounds.playRandom()
    }
    fun onBackPressed(){
        lightMotionController.stopSound()
    }
}