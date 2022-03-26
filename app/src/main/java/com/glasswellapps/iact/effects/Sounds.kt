package com.glasswellapps.iact.effects

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.glasswellapps.iact.loading.CharacterHolder
import com.glasswellapps.iact.inventory.Items
import com.glasswellapps.iact.R
import kotlin.random.Random

object Sounds {
    val select = 0
    val lightSaber = 1
    val equip_electric = 2
    val equip_blade = 3
    val equip_gun = 4
    val equip_impact = 5
    val equip_armor = 6
    val equip_clothing = 7
    val droid = 8
    val melee_slice = 9
    val lightsaber_stabby_stabby = 10
    val blaster_gaster_blaster_master = 11
    val melee_impact = 12
    val strain = 13
    val moving = 14
    val special = 15
    val crate = 16
    val door = 17
    val door2 = 88
    val terminal_button = 18
    val terminal = 19
    val stormtrooper_death_wilhelm = 20
    val blaster_atat = 21
    val alien_gamorean = 22
    val alien_gamorean_death = 23
    val alien_jawa = 24
    val alien_jawa1 = 35
    val alien_jawa_death = 36
    val alien_rhodian = 37
    val alien_rhodian_death = 38
    val alien_trandoshan = 39
    val alien_trandoshan_death = 40
    val alien_tusken = 41
    val alien_tusken_death = 42
    val alien_wookie = 43
    val alien_wookie_death = 44
    val blaster_boba = 45
    val blaster_explosion = 46
    val blaster_rebel = 47
    val blaster_tie = 48
    val crate_beep= 49
    val droid_hit = 50
    val droid_dead = 51
    val lightsaber_fast = 52
    val lightsaber_off = 53
    val lightsaber_on = 54
    val lightsaber_swing = 55
    val lightsaber_clash = 82
    val lightsaber_heavy_clash = 83
    val lightsaber_heavy_clash2 = 84
    val lightsaber_heavy_flurry = 85
    val lightsaber_quick_flurry = 86
    val lightsaber_hum = 87
    val melee_punch = 56
    val melee_woosh = 57
    val stormtrooper_blastem = 58
    val stormtrooper_copy_that = 59
    val stormtrooper_death = 60
    val stormtrooper_death1 = 61
    val stormtrooper_death2 = 62
    val stormtrooper_dont_move = 63
    val stormtrooper_hey = 64
    val stormtrooper_intruder = 65
    val terminal_button_low = 66
    val terminal_chirp = 67
    val terminal_confirm = 68
    val terminal_loading = 69
    val droid_shot = 70
    val negative = 71
    val droid_talk = 72
    val droid_move = 73
    val creature_wampa = 74
    val creature_wampa_death = 75
    val creature_bantha = 76
    val creature_bantha_death = 77
    val creature_rancor = 78
    val creature_rancor_death = 79
    val emperor_laugh = 80
    val emperor_laugh1 = 81

    var sPBuilder = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_GAME)

    var soundPool = SoundPool.Builder()
        .setMaxStreams(10)
        .setAudioAttributes(sPBuilder.build())
        .build()

    var sEPool: IntArray? = null


    fun sounEPool(context: Context) {
        sEPool = IntArray(89)

        sEPool!![select] = soundPool.load(context, R.raw.select, 1)

        sEPool!![alien_gamorean] = soundPool.load(context, R.raw.alien_garmorean, 1)
        sEPool!![alien_gamorean_death] = soundPool.load(context, R.raw.alien_gamorean_death, 1)
        sEPool!![alien_jawa] = soundPool.load(context, R.raw.alien_jawa, 1)
        sEPool!![alien_jawa1] = soundPool.load(context, R.raw.alien_jawa1, 1)
        sEPool!![alien_jawa_death] = soundPool.load(context, R.raw.alien_jawa_death, 1)
        sEPool!![alien_rhodian] = soundPool.load(context,R.raw.alien_rhodian,1)
        sEPool!![alien_rhodian_death] = soundPool.load(context, R.raw.alien_rhodian_death,1)
        sEPool!![alien_trandoshan] = soundPool.load(context, R.raw.alien_trandoshan, 1)
        sEPool!![alien_trandoshan_death] = soundPool.load(context, R.raw.alien_trandoshan_death, 1)
        sEPool!![alien_tusken] = soundPool.load(context, R.raw.alien_tusken, 1)
        sEPool!![alien_tusken_death] = soundPool.load(context, R.raw.alien_tusken_death, 1)
        sEPool!![alien_wookie] = soundPool.load(context, R.raw.alien_wookie, 1)
        sEPool!![alien_wookie_death] = soundPool.load(context, R.raw.alien_wookie_death, 1)

        sEPool!![creature_bantha]  = soundPool.load(context, R.raw.creature_bantha, 1)
        sEPool!![creature_bantha_death]  = soundPool.load(context, R.raw.creature_bantha_death, 1)
        sEPool!![creature_rancor]  = soundPool.load(context, R.raw.creature_rancor, 1)
        sEPool!![creature_rancor_death]  = soundPool.load(context, R.raw.creature_rancor_death, 1)
        sEPool!![creature_wampa]  = soundPool.load(context, R.raw.creature_wampa, 1)
        sEPool!![creature_wampa_death]  = soundPool.load(context, R.raw.creature_wampa_death, 1)

        sEPool!![lightSaber] = soundPool.load(context, R.raw.lightsaber_ignite, 1)
        sEPool!![lightsaber_on] = soundPool.load(context,R.raw.lightsaber_on,1)
        sEPool!![lightsaber_off] = soundPool.load(context, R.raw.lightsaber_off,1)

        sEPool!![lightsaber_fast] = soundPool.load(context, R.raw.lightsaber_fast,1)
        sEPool!![lightsaber_swing] = soundPool.load(context, R.raw.lightsaber_swing,1)
        sEPool!![lightsaber_clash] = soundPool.load(context, R.raw.lightsaber_clash, 1)
        sEPool!![lightsaber_heavy_clash] = soundPool.load(context, R.raw
            .lightsaber_heavy_clash, 1)
        sEPool!![lightsaber_heavy_clash2] = soundPool.load(context, R.raw
            .lightsaber_heavy_clash2, 1)
        sEPool!![lightsaber_heavy_flurry] = soundPool.load(context, R.raw
            .lightsaber_heavy_flurry, 1)
        sEPool!![lightsaber_quick_flurry] = soundPool.load(context, R.raw
            .lightsaber_quick_flurry, 1)
        sEPool!![lightsaber_stabby_stabby] = soundPool.load(context, R.raw.lightsaber_stabby_stabby, 1)
        sEPool!![lightsaber_hum] = soundPool.load(context, R.raw.lightsaber_hum, 1)

        sEPool!![equip_electric] = soundPool.load(context, R.raw.electric, 1)
        sEPool!![equip_blade] = soundPool.load(context, R.raw.equip_blade, 1)
        sEPool!![equip_gun] = soundPool.load(context, R.raw.equip_gun, 1)
        sEPool!![equip_impact] = soundPool.load(context, R.raw.equip_impact, 1)
        sEPool!![equip_armor] =    soundPool.load(context, R.raw.equip_armor, 1)
        sEPool!![equip_clothing] =   soundPool.load(context, R.raw.equip_clothing, 1)

        sEPool!![droid] =   soundPool.load(context, R.raw.droid, 1)
        sEPool!![droid_move] = soundPool.load(context, R.raw.droid_move, 1)
        sEPool!![droid_talk] = soundPool.load(context, R.raw.droid_talk, 1)

        sEPool!![droid_hit] = soundPool.load(context,R.raw.droid_hit,1)
        sEPool!![droid_dead] = soundPool.load(context,R.raw.droid_death,1)
        sEPool!![droid_shot] = soundPool.load(context,R.raw.droid_shot,1)

        sEPool!![melee_slice] = soundPool.load(context, R.raw.melee_slice, 1)
        sEPool!![melee_impact] = soundPool.load(context, R.raw.melee_impact, 1)
        sEPool!![melee_punch] = soundPool.load(context, R.raw.melee_punch, 1)
        sEPool!![melee_woosh] = soundPool.load(context, R.raw.melee_woosh, 1)

        sEPool!![blaster_gaster_blaster_master] = soundPool.load(context, R.raw.gaster_blaster_master, 1)
        sEPool!![blaster_atat] = soundPool.load(context, R.raw.blaster_atat, 1)
        sEPool!![blaster_boba] = soundPool.load(context, R.raw.blaster_boba,1)
        sEPool!![blaster_explosion] = soundPool.load(context, R.raw.blaster_explosion,1)
        sEPool!![blaster_rebel] = soundPool.load(context, R.raw.blaster_rebel,1)
        sEPool!![blaster_tie] = soundPool.load(context, R.raw.blaster_tie,1)

        sEPool!![strain] =soundPool.load(context, R.raw.strain, 1)
        sEPool!![moving] =soundPool.load(context, R.raw.moving, 1)
        sEPool!![special] =soundPool.load(context, R.raw.special, 1)
        sEPool!![crate] =soundPool.load(context, R.raw.crate, 1)
        sEPool!![crate_beep] =soundPool.load(context, R.raw.crate_beep, 1)
        sEPool!![door] = soundPool.load(context, R.raw.door, 1)
        sEPool!![door2] = soundPool.load(context, R.raw.door2, 1)

        sEPool!![terminal_button] = soundPool.load(context, R.raw.terminal_button, 1)
        sEPool!![terminal] = soundPool.load(context, R.raw.terminal, 1)
        sEPool!![terminal_button_low] = soundPool.load(context,R.raw.terminal_button_low,1)
        sEPool!![terminal_chirp] = soundPool.load(context,R.raw.terminal_chirp,1)
        sEPool!![terminal_confirm] = soundPool.load(context,R.raw.terminal_confirm,1)
        sEPool!![terminal_loading] = soundPool.load(context,R.raw.terminal_loading,1)

        sEPool!![stormtrooper_death_wilhelm] = soundPool.load(context, R.raw.stormtrooper_death_wilhelm, 1)
        sEPool!![stormtrooper_death]  = soundPool.load(context, R.raw.stormtrooper_death, 1)
        sEPool!![stormtrooper_death1]  = soundPool.load(context, R.raw.stormtrooper_death1, 1)
        sEPool!![stormtrooper_death2]  = soundPool.load(context, R.raw.stormtrooper_death2, 1)

        sEPool!![stormtrooper_blastem]  = soundPool.load(context, R.raw.stormtrooper_blastem, 1)
        sEPool!![stormtrooper_copy_that]  = soundPool.load(context, R.raw.stormtrooper_copy_that, 1)
        sEPool!![stormtrooper_dont_move]  = soundPool.load(context, R.raw.stormtrooper_dont_move, 1)
        sEPool!![stormtrooper_hey]  = soundPool.load(context, R.raw.stormtrooper_hey, 1)
        sEPool!![stormtrooper_intruder]  = soundPool.load(context, R.raw.stormtrooper_intruder, 1)

        sEPool!![negative] =soundPool.load(context, R.raw.negative, 1)

        sEPool!![emperor_laugh] =soundPool.load(context, R.raw.emperor_laugh, 1)
        sEPool!![emperor_laugh1] =soundPool.load(context, R.raw.emperor_laugh1, 1)
    }

    fun reset(context: Context){
        if(sEPool == null) {
            sounEPool(context);
        }
    }

    private var volume = 1f;
    fun setSoundVolume(volume:Float){
        this.volume = volume;
    }

    fun play(soundId : Int){
        play(soundId, 0.8f)
    }
    fun play(soundId : Int, loudness:Float){
        if(sEPool!=null) {
            if(sEPool!![soundId] != null) {
                soundPool.play(sEPool!![soundId], volume*loudness, volume*loudness, 1, 0, 1f)
            }
        }
    }

    fun selectSound(){
        try {
            play(select)
        }
        catch (e:Exception){
            System.out.println(e)

        }
    }
    fun negativeSound(){
        try {
            play(negative)
        }
        catch (e:Exception){
            System.out.println(e)

        }
    }
    fun strainSound(){
        try{
            play(strain)
        }
        catch (e:Exception){
            System.out.println(e)

        }
    }

    fun equipSound(context: Context, equipSoundType: Int) {
        try {
            when (equipSoundType) {
                select -> {
                    play(select)
                }
                lightSaber -> {
                    play(lightSaber)
                }
                equip_electric -> {
                    play(equip_electric)
                }
                equip_blade -> {
                    play(equip_blade)
                }
                equip_gun -> {
                    play(equip_gun)
                }
                equip_impact -> {
                    play(equip_impact)
                }
                equip_armor -> {
                    play(equip_armor)
                }
                equip_clothing -> {
                    play(equip_clothing)
                }
                droid -> {
                    play(droid)
                }
            }
        }
        catch (e:Exception){
            System.out.println(e)

        }
    }

    fun attackSound() {
        try{
            val character = CharacterHolder.getActiveCharacter()
            var whichSound = 0
            if(character.weapons.size>0) {
                var whichWeapon =(Math.random()*character.weapons.size).toInt()
                var index = character.weapons[whichWeapon]
                whichSound = Items.itemsArray!![index].soundType

            }

            when (whichSound) {
                0->{
                    play(melee_impact)
                }
                equip_blade -> {
                    play(melee_slice)
                }
                lightSaber -> {
                    play(lightsaber_stabby_stabby)
                }
                equip_electric -> {
                    play(equip_electric)
                }
                equip_gun -> { play(blaster_gaster_blaster_master) }
                equip_impact -> {
                    play(melee_impact)
                }
            }
        }
        catch (e:Exception){
            System.out.println(e)

        }
    }

    fun damagedSound(context: Context, damageType: Int) {
        try {
            when (damageType) {
                melee_slice -> {
                    play(melee_slice)
                }
                melee_impact -> {
                    play(melee_impact)
                }
                blaster_gaster_blaster_master -> {
                    play(blaster_gaster_blaster_master)
                }
            }
        }
        catch (e:Exception){
            System.out.println(e)

        }
    }

    fun conditionSound(conditionType:Int){
        selectSound()
    }

    fun movingSound(){
        try{
            play(moving)
        }
        catch (e:Exception){
            System.out.println(e)
        }
    }

    fun buttonSound(){
        try {
            play(terminal_button)
        }catch (e:Exception){
            System.out.println(e)
        }
    }
    fun terminalSound(){
        try {
            if (Random.nextBoolean()) {
                play(terminal_button)
            } else {
                play(terminal)
            }
        }catch (e:Exception){
            System.out.println(e)
        }
    }
    fun doorSound(){
        try{
            if(Random.nextBoolean()){
                play(door)
            }
            else{
                play(door2)
            }
        }
        catch (e:Exception){
            System.out.println(e)
        }
    }

    fun specialSound(){
        try{
            play(special)
        }
        catch (e:Exception){
            System.out.println(e)
        }
    }

    fun crateSound(){
        try{
            play(crate)
        }
        catch (e:Exception){
            System.out.println(e)
        }
    }
}