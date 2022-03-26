package com.glasswellapps.iact/*

import com.glasswellapps.iact.character.ModListAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Color.TRANSPARENT
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.text.method.LinkMovementMethod
import android.util.DisplayMetrics
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.Observer
import androidx.work.*
import com.glasswellapps.iact.inventory.Items
import com.glasswellapps.iact.database.AppDatabase
import com.glasswellapps.iact.database.CharacterData
import com.glasswellapps.iact.effects.GreenHighlight
import com.glasswellapps.iact.effects.Sounds
import com.glasswellapps.iact.characters.*
import com.glasswellapps.iact.inventory.*
import kotlinx.android.synthetic.main.activity_character_screen.*
import kotlinx.android.synthetic.main.activity_load_screen.*
import kotlinx.android.synthetic.main.credits_to_us.*
import kotlinx.android.synthetic.main.dialog_assist.*
import kotlinx.android.synthetic.main.dialog_background.*
import kotlinx.android.synthetic.main.dialog_bio.*
import kotlinx.android.synthetic.main.dialog_conditions.*
import kotlinx.android.synthetic.main.dialog_options.*
import kotlinx.android.synthetic.main.dialog_quick_view.*
import kotlinx.android.synthetic.main.dialog_quick_view_button.*
import kotlinx.android.synthetic.main.dialog_rest.*
import kotlinx.android.synthetic.main.dialog_save.*
import kotlinx.android.synthetic.main.dialog_show_card.*
import kotlinx.android.synthetic.main.dialog_show_condition_card.*
import kotlinx.android.synthetic.main.grid_item.view.*
import kotlinx.android.synthetic.main.screen_settings.*
import kotlinx.android.synthetic.main.screen_stats.*
import kotlinx.android.synthetic.main.screen_xp_select.*
import kotlinx.android.synthetic.main.toast_no_actions_left.view.*
import java.io.InputStream
import kotlin.random.Random



class CharacterScreenRefactored : AppCompatActivity() {
    var character: Character = Character();
    var animateConditions = true
    var animateDamage = true
    var actionUsage = true
    var strengthGlow: GreenHighlight? = null
    var techGlow: GreenHighlight?= null
    var insightGlow: GreenHighlight?= null
    var height = 0f
    var width = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_screen)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        Sounds.reset(this)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels.toFloat()
        width = displayMetrics.widthPixels.toFloat()
        initDialogs()
        initScreen()
        initAnimations()
        initKillTracker()
    }

    var loadAnimated = false
    override fun onWindowFocusChanged(hasFocus: Boolean) {

        updateStats()
        if(character.damage>0) {
            if (character.damage < character.health) {
                add_damage.setImageDrawable(getNumber(character.damage))
                if(character.withdrawn) {
                    character.withdrawn = false
                    val slide = ObjectAnimator.ofFloat(character_images, "translationY", 0f)
                    slide.setDuration(500)
                    slide.start()
                }
                if (isWounded) {
                    character.wounded = 0
                    wounded.animate().alpha(0f)
                    isWounded = false
                    updateStats()
                }
            } else if (character.damage < character.health * 2) {
                character.wounded = character.damage - character.health
                //add_damage.setText("" + character.wounded)
                add_damage.setImageDrawable(getNumber(character.wounded))
                if (!isWounded) {
                    wounded.animate().alpha(1f)
                    character.timesWounded++
                    isWounded = true
                    updateStats()
                }
                if(character.withdrawn) {
                    character.withdrawn = false
                    val slide = ObjectAnimator.ofFloat(character_images, "translationY", 0f)
                    slide.setDuration(500)
                    slide.start()
                }
            } else {
                if(!character.withdrawn) {
                    character.withdrawn = true
                    character.timesWithdrawn++
                    val slide = ObjectAnimator.ofFloat(
                        character_images, "translationY", character_images.y, character_images.y,
                        character_image.height.toFloat()
                    )
                    slide.setDuration(1500)
                    slide.start()
                }
                //add_damage.setText("" + character.health)
                add_damage.setImageDrawable(getNumber(character.health))
            }
        }
        updateImages()

        if (hasFocus && !loadAnimated) {
            top_panel.animate().alpha(1f)
            bottom_panel.animate().alpha(1f)
            left_buttons.animate().alpha(1f)
            val animButtons = ObjectAnimator.ofFloat(
                left_buttons, "translationX", -left_buttons.width
                    .toFloat(), 0f
            )
            //animBottom.interpolator = DecelerateInterpolator()
            animButtons.duration = (500).toLong()
            animButtons.start()

            right_buttons.animate().alpha(1f)
            val animRightButtons = ObjectAnimator.ofFloat(
                right_buttons, "translationX", right_buttons
                    .width
                    .toFloat(), 0f
            )
            //animBottom.interpolator = DecelerateInterpolator()
            animRightButtons.duration = (500).toLong()
            animRightButtons.start()

            character_images.animate().alpha(1f)
            if(character.damage < character.health*2) {
                val animChar = ObjectAnimator.ofFloat(
                    character_images, "translationY", character_images.height
                        .toFloat(), character_images.height.toFloat(), 0f
                )
                animChar.interpolator = DecelerateInterpolator()
                animChar.duration = (800).toLong()
                animChar.start()
            }
            else{
                val slide = ObjectAnimator.ofFloat(
                    character_images, "translationY", character_images.y, character_images.y,
                    character_image.height.toFloat()
                )
                slide.setDuration(1500)
                slide.start()
            }
            companion_image.animate().alpha(1f)
            val animcompanion = ObjectAnimator.ofFloat(
                companion_image, "translationX",
                -character_images.width * 1.2f
                    .toFloat(), -character_images.width.toFloat() * 1.2f, 0f
            )
            animcompanion.interpolator = DecelerateInterpolator()
            animcompanion.duration = (800 * 1.2f).toLong()
            animcompanion.start()

            updateSideBarState()
            kill_tracker_bar.visibility = View.VISIBLE
            background_image.alpha=0f
            background_image.animate().alpha(1f)
            camouflage.animate().alpha(1f)
            loadAnimated = true
        }
        quickSave()
    }
    fun resetUI(){
        top_panel.animate().alpha(0f)
        bottom_panel.animate().alpha(0f)
        left_buttons.animate().alpha(0f)
        right_buttons.animate().alpha(0f)
        character_images.animate().alpha(0f)
        companion_image.animate().alpha(0f)
        background_image.alpha = 0f
        loadAnimated = false
    }

    //************************************************************************************************************
    //region Main Screen
    //************************************************************************************************************
    var isWounded = false

    private fun initScreen() {
        var load = intent.getBooleanExtra("Load", false)
        var characterName: String = intent.getStringExtra("CharacterName").toString()


        if (!load) {
            when (characterName) {
                "biv" -> {
                    character = Character_biv(this)
                }
                "davith" -> {
                    character = Character_davith(this)
                }
                "diala" -> {
                    character = Character_diala(this)
                }
                "drokdatta" -> {
                    character = Character_drokkatta(this)
                }
                "fenn" -> {
                    character = Character_fenn(this)
                }
                "gaarkhan" -> {
                    character = Character_gaarkhan(this)
                }
                "gideon" -> {
                    character = Character_gideon(this)
                }
                "jarrod" -> {
                    character = Character_jarrod(this)
                }
                "jyn" -> {
                    character = Character_jyn(this)
                }
                "loku" -> {
                    character = Character_loku(this)
                }
                "kotun" -> {
                    character = Character_kotun(this)
                }
                "mak" -> {
                    character = Character_mak(this)
                }
                "mhd19" -> {
                    character = Character_mhd19(this)
                }
                "murne" -> {
                    character = Character_murne(this)
                }
                "onar" -> {
                    character = Character_onar(this)
                }
                "saska" -> {
                    character = Character_saska(this)
                }
                "shyla" -> {
                    character = Character_shyla(this)
                }
                "verena" -> {
                    character = Character_verena(this)
                }
                "vinto" -> {
                    character = Character_vinto(this)
                }
                "drokkatta" -> {
                    character = Character_drokkatta(this)
                }
                "ct1701" -> {
                    character = Character_ct1701(this)
                }
                "tress" -> {
                    character = Character_tress(this)
                }
                "custom" -> {
                    character = CustomCharacter(this)
                    val database = AppDatabase.getInstance(this)
                    val data = database!!.getCustomDAO().getAll()
                    character.name = data[0].characterName!!
                    character.health_default = data[0].health
                    character.endurance_default = data[0].endurance
                    character.speed_default = data[0].speed
                    character.defence_dice = data[0].defence
                }
            }
            Loaded.setCharacter(character)

            if (character.startingRangedWeapon != null) {
                character.weapons.add(Items.rangedArray!![0].index)
            }
            if (character.startingMeleeWeapon != null) {
                character.weapons.add(Items.meleeArray!![0].index)
            }
        } else {
            if(Loaded.getCharacter()!=null) {
                character = Loaded.getCharacter()
            }
            else{
                finish();
            }
        }
        character.loadImages(this);
        if (character.portraitImage == null) {
            character.loadPortraitImage(this)
        }
        fileName.setText("" + character.name)

        animateConditions = character.conditionAnimSetting
        animateDamage =character.damageAnimSetting
        actionUsage = character.actionUsageSetting
        settingsScreen!!.soundEffectsVolume.progress = (character.soundEffectsSetting*100).toInt()

        when (character.defence_dice) {
            "white" -> defence.setImageDrawable(resources.getDrawable(R.drawable.dice))
            "black" -> defence.setImageDrawable(resources.getDrawable(R.drawable.dice_black))
            "" -> defence.visibility = View.INVISIBLE

        }


        updateImages()
        updateStats()
        initDamageAndStrain()
        updateStats()
        initConditions()
        updateConditionIcons()



        camouflage.setImageBitmap(getBitmap(this, "backgrounds/camo_interior.png"))

        if (character.name_short == "jarrod") {
            companion_button.visibility = View.VISIBLE

            companion_button.isClickable = true
        } else {
            companion_button.visibility = View.INVISIBLE

            companion_button.isClickable = false
        }
        background_image.setImageBitmap(character.getBackgroundImage(this))
        camouflage.setImageBitmap(character.getCamoImage(this))
    }

    private fun setDiceColor(dice: ImageView, color: Char) {
        dice.visibility = ImageView.VISIBLE
        when (color) {
            'B' -> dice.setImageDrawable(resources.getDrawable(R.drawable.dice_blue))
            'G' -> dice.setImageDrawable(resources.getDrawable(R.drawable.dice_green))
            'Y' -> dice.setImageDrawable(resources.getDrawable(R.drawable.dice_yellow))
            'R' -> dice.setImageDrawable(resources.getDrawable(R.drawable.dice_red))
            ' ' -> ImageViewCompat.setImageTintList(
                dice, ColorStateList.valueOf(
                    Color.argb(
                        0,
                        0,
                        0,
                        0
                    )
                )
            )
        }
    }

    open fun getBitmap(context: Context, path: String): Bitmap? {
        val assetManager = context.assets
        var inputStream: InputStream? = null
        var bitmap: Bitmap? = null
        val options = BitmapFactory.Options()
        for (i in 1..32) {
            try {
                inputStream = assetManager.open(path)
                options.inSampleSize = i

                bitmap = BitmapFactory.decodeStream(inputStream, null, options)
                break
            } catch (outOfMemoryError: OutOfMemoryError) {

                //println("next size" + i)
            } catch (e: Exception) {
                //e.printStackTrace()
                break
            }
        }

        try {
            inputStream?.close()
        } catch (e: Exception) {
            //e.printStackTrace()
        }
        return bitmap
    }

    private fun updateImages() {


        character.updateCharacterImages(this)
        if (animateConditions) {
            if (character.currentImage != null) {
                character.glowImage = Bitmap.createBitmap(character.currentImage!!)
                val input = Bitmap.createBitmap(character.currentImage!!)
                val output = Bitmap.createBitmap(character.currentImage!!)

                val rs = RenderScript.create(this)
                val blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
                blur.setRadius(25f)
                val tempIn = Allocation.createFromBitmap(rs, input)
                val tempOut = Allocation.createFromBitmap(rs, output)
                blur.setInput(tempIn)
                blur.forEach(tempOut)

                tempOut.copyTo(character.glowImage)

            }
            character_image.setGlowBitmap(character.glowImage)
            character.updateCharacterImages(this)
        }
        character_image.setImageBitmap(character.currentImage)
        character_image.setLayer1Bitmap(character.layer1)
        character_image.setLayer2Bitmap(character.layer2)

        if(!character.name_short.equals("jarrod")){
            if(character.astromech) {
                character.companionImage = (resources.getDrawable(R.drawable.r5_astromech1) as BitmapDrawable).bitmap
            }
            else{
                character.companionImage = null
            }
        }
        println( "ASTROMECH" +character.companionImage + " " + character.astromech )
        if (character.companionImage != null) {
            if (conditionsActive[hidden] && animateConditions) {
                companion_image.visibility = View.INVISIBLE
            } else {
                companion_image.setImageBitmap(character.companionImage)
                companion_image.visibility = View.VISIBLE
            }
        } else {
            companion_image.visibility = View.GONE
        }
    }

    fun onShowCompanionCard(view: View) {
        if (character.companionCard != null) {
            showCardDialog!!.card_image.setImageBitmap(character.companionCard)
            showCardDialog!!.show()
        }
    }

    private fun updateStats() {
        character.health = character.health_default
        character.endurance = character.endurance_default
        character.speed = character.speed_default

        for (i in 0..character.xpCardsEquipped.size - 1) {
            if (character.xpCardsEquipped[i]) {
                if (character.xpHealths[i] != 0) {
                    character.health += character.xpHealths[i]
                }
                if (character.xpEndurances[i] != 0) {
                    character.endurance += character.xpEndurances[i]
                }
                if (character.xpSpeeds[i] != 0) {
                    character.speed += character.xpSpeeds[i]
                }
            }
        }

        for (i in 0..character.accessories.size - 1) {
            character.health += Items.itemsArray!![character.accessories[i]].health
            character.endurance += Items.itemsArray!![character.accessories[i]].endurance
        }
        for (i in 0..character.armor.size - 1) {
            character.health += Items.itemsArray!![character.armor[i]].health
            //character.endurance += Items.armorArray!![character.armor[i]].endurance
        }
        for (i in 0..character.rewards.size - 1) {
            character.health += Items.rewardsArray!![character.rewards[i]].health
            //character.endurance += Items.armorArray!![character.armor[i]].endurance
        }

        if (isWounded) {
            character.endurance--
            character.speed--
        }

        setStatColor(health, character.health, character.health_default)
        setStatColor(endurance, character.endurance, character.endurance_default)
        setStatColor(speed, character.speed, character.speed_default)

        health.setText("" + character.health);
        endurance.setText("" + character.endurance);
        speed.setText("" + character.speed);

        if (!isWounded) {

            setDiceColor(strength1, character.strength[0]);
            setDiceColor(strength2, character.strength[1]);
            setDiceColor(strength3, character.strength[2]);

            setDiceColor(insight1, character.insight[0]);
            setDiceColor(insight2, character.insight[1]);
            setDiceColor(insight3, character.insight[2]);

            setDiceColor(tech1, character.tech[0]);
            setDiceColor(tech2, character.tech[1]);
            setDiceColor(tech3, character.tech[2]);
        } else {
            setDiceColor(strength1, character.strengthWounded[0]);
            setDiceColor(strength2, character.strengthWounded[1]);
            setDiceColor(strength3, character.strengthWounded[2]);

            setDiceColor(insight1, character.insightWounded[0]);
            setDiceColor(insight2, character.insightWounded[1]);
            setDiceColor(insight3, character.insightWounded[2]);

            setDiceColor(tech1, character.techWounded[0]);
            setDiceColor(tech2, character.techWounded[1]);
            setDiceColor(tech3, character.techWounded[2]);
        }


    }

    private fun setStatColor(stat: TextView, current: Int, default: Int) {
        if (current > default) {
            stat.setShadowLayer(5f, 0f, 0f, resources.getColor(R.color.dice_green))
        } else if (current < default) {
            stat.setShadowLayer(5f, 0f, 0f, resources.getColor(R.color.stat_orange))
        } else {
            stat.setShadowLayer(5f, 0f, 0f, Color.BLACK)
        }
    }

    fun onHide(view: View) {
        view.visibility = View.INVISIBLE
    }

    //endregion
    //************************************************************************************************************
    //region Damage and Strain
    //************************************************************************************************************

    private fun getNumber(number: Int): Drawable {
        var numberImage = R.drawable.number_0
        when (number) {
            1 -> numberImage = R.drawable.number_1
            2 -> numberImage = R.drawable.number_2
            3 -> numberImage = R.drawable.number_3
            4 -> numberImage = R.drawable.number_4
            5 -> numberImage = R.drawable.number_5
            6 -> numberImage = R.drawable.number_6
            7 -> numberImage = R.drawable.number_7
            8 -> numberImage = R.drawable.number_8
            9 -> numberImage = R.drawable.number_9
            10 -> numberImage = R.drawable.number_10
            11 -> numberImage = R.drawable.number_11
            12 -> numberImage = R.drawable.number_12
            13 -> numberImage = R.drawable.number_13
            14 -> numberImage = R.drawable.number_14
            15 -> numberImage = R.drawable.number_15
            16 -> numberImage = R.drawable.number_16
            17 -> numberImage = R.drawable.number_17
            18 -> numberImage = R.drawable.number_18
            19 -> numberImage = R.drawable.number_19
            20 -> numberImage = R.drawable.number_20
            21 -> numberImage = R.drawable.number_21
            22 -> numberImage = R.drawable.number_22
            23 -> numberImage = R.drawable.number_23
            24 -> numberImage = R.drawable.number_24
            25 -> numberImage = R.drawable.number_25
            26 -> numberImage = R.drawable.number_26
            27 -> numberImage = R.drawable.number_27
            28 -> numberImage = R.drawable.number_28
            29 -> numberImage = R.drawable.number_29
            30 -> numberImage = R.drawable.number_30
            31 -> numberImage = R.drawable.number_31
            32 -> numberImage = R.drawable.number_32
            33 -> numberImage = R.drawable.number_33
            34 -> numberImage = R.drawable.number_34
            35 -> numberImage = R.drawable.number_35
            36 -> numberImage = R.drawable.number_36
            37 -> numberImage = R.drawable.number_37
            38 -> numberImage = R.drawable.number_38
            39 -> numberImage = R.drawable.number_39
            40 -> numberImage = R.drawable.number_40
        }
        return resources.getDrawable(numberImage)
    }

    fun onAddStrain(view: View) {
        if (character.strain < character.endurance) {
            Sounds.strainSound()
            character.strain++
            character.strainTaken++

            if (minus_strain.alpha == 0f) {
                minus_strain.animate().alpha(1f)
            }

            //add_strain.setText("" + character.strain)
            add_strain.setImageDrawable(getNumber(character.strain))
            playRestAnim()
        } else {
            if(addDamage()) {
                Sounds.damagedSound(this, Sounds.impact)
                hitAnim()
                character.damageTaken++
                playRestAnim()
            }
            else{
                Sounds.negativeSound()
            }
        }
    }

    fun onMinusStrain(view: View) {
        if (character.strain > 0) {
            Sounds.selectSound()
            character.strain--
            //add_strain.setText("" + character.strain)
            add_strain.setImageDrawable(getNumber(character.strain))
        }
        if (character.strain == 0) {
            if (minus_strain.alpha > 0f) {
                minus_strain.animate().alpha(0f)
            }
        }
    }

    fun onAddDamage(view: View) {
        if (addDamage()) {
            playDamageAnim()
            character.damageTaken++
        }
        else{
            Sounds.negativeSound()
        }
    }

    private fun addDamage(): Boolean {
        if (character.damage < character.health * 2) {
            character.damage++


            if (minus_damage.alpha == 0f) {
                minus_damage.animate().alpha(1f)
            }

            if (character.damage < character.health) {

                //add_damage.setText("" + character.damage)
                add_damage.setImageDrawable(getNumber(character.damage))
            } else if (character.damage < character.health * 2) {

                character.wounded = character.damage - character.health
                //add_damage.setText("" + character.wounded)
                add_damage.setImageDrawable(getNumber(character.wounded))
                if (!isWounded) {
                    wounded.animate().alpha(1f)


                    character.timesWounded++
                    isWounded = true
                    quickSave()
                    updateStats()
                }

            } else {
                character.withdrawn = true
                character.timesWithdrawn++
                //add_damage.setText("" + character.health)
                add_damage.setImageDrawable(getNumber(character.health))
                val slide = ObjectAnimator.ofFloat(
                    character_images, "translationY", character_images.y, character_images.y,
                    character_image.height.toFloat()
                )
                slide.setDuration(1500)
                slide.start()
                quickSave()
            }
            return true
        }
        return false
    }

    fun onMinusDamage(view: View) {
        Sounds.selectSound()
        minusDamage()
    }

    fun minusDamage(){
        if (character.damage > 0) {

            character.damage--

            if (character.damage < character.health) {
                //add_damage.setText("" + character.damage)
                character.withdrawn = false
                add_damage.setImageDrawable(getNumber(character.damage))
                if (isWounded) {
                    character.wounded = 0
                    wounded.animate().alpha(0f)
                    isWounded = false
                    quickSave()
                    updateStats()
                }
            } else if (character.damage < character.health * 2) {
                character.withdrawn = false
                character.wounded = character.damage - character.health
                //add_damage.setText("" + character.wounded)
                add_damage.setImageDrawable(getNumber(character.wounded))
                val slide = ObjectAnimator.ofFloat(character_images, "translationY", 0f)
                slide.setDuration(500)
                slide.start()
                quickSave()
            }
            else{
                character.withdrawn = true
                character.damage = character.health*2
                character.wounded = character.damage - character.health
                add_damage.setImageDrawable(getNumber(character.wounded))

            }

        }
        if (character.damage == 0) {
            if (minus_damage.alpha > 0f) {
                minus_damage.animate().alpha(0f)
            }
        }
    }

    fun onUnwound(view: View) {
        Sounds.strainSound()
        playRestAnim()
        character.damage = 0
        character.wounded = 0
        //add_damage.setText("" + character.damage)
        add_damage.setImageDrawable(getNumber(character.damage))
        wounded.animate().alpha(0f)
        isWounded = false
        unwoundDialog!!.dismiss()
        character.withdrawn = false
        val slide = ObjectAnimator.ofFloat(character_images, "translationY", 0f)
        slide.setDuration(500)
        //slide.start()
        character_images.animate().translationY(0f)
        updateStats()
        quickSave()

    }

    private fun initDamageAndStrain() {
        if (character.damage > 0) {
            if (minus_damage.alpha == 0f) {
                minus_damage.animate().alpha(1f)
            }

            if (character.damage < character.health) {
                //add_damage.setText("" + character.damage)
                add_damage.setImageDrawable(getNumber(character.damage))

            } else {
                character.wounded = character.damage - character.health
                //add_damage.setText("" + character.wounded)
                add_damage.setImageDrawable(getNumber(character.wounded))
                wounded.animate().alpha(1f)

                isWounded = true

                //println()
                //println("withdrawn" + character.withdrawn)
                //println()
                if (character.withdrawn) {
                    //println("SLIDE DOWN")
                    character_images.animate().translationY(character_image.height.toFloat())
                }

            }
        }

        if (character.strain > 0) {
            if (minus_strain.alpha == 0f) {
                minus_strain.animate().alpha(1f)
            }

            //add_strain.setText("" + character.strain)
            add_strain.setImageDrawable(getNumber(character.strain))
        }

        add_damage.setOnLongClickListener {
            if (character.damage >= character.health) {
                unwoundDialog!!.show()
            }
            true
        }
        add_strain.setOnLongClickListener {
            restDialog!!.show()

            true
        }
    }

    //endregion
    //************************************************************************************************************
    //region Turn Actions
    //************************************************************************************************************
    var actionsLeft = 0;
    var activated = false

    fun onActivate(view: View) {

        if (!activated) {
            Sounds.selectSound()
            val flipUnactive = ObjectAnimator.ofFloat(unactive, "scaleX", 1f, 0f, 0f, 0f)
            flipUnactive.setDuration(200)
            flipUnactive.start()

            val flipActive = ObjectAnimator.ofFloat(active, "scaleX", 0f, 0f, 0f, 1f)
            flipActive.setDuration(200)
            flipActive.start()


            unactive.animate().alpha(0f).duration = 100

            if (actionUsage) {
                turnOnActionButtons()
                actionsLeft = 2;
            }
            activated = true


        } else {
            onEndActivation(view)
        }
    }

    fun turnOnActionButtons() {

        action1.visibility = View.VISIBLE
        action_button1.visibility = View.VISIBLE
        action_button1.animate().alpha(1f)
        action2.visibility = View.VISIBLE
        action_button2.visibility = View.VISIBLE
        action_button2.animate().alpha(1f)
    }

    fun onEndActivation(view: View) {
        Sounds.selectSound()
        deactivationAnim()
        endActivationDialog!!.dismiss()
        removeCondition(weakened)

        if (actionUsage) {
            turnOffActionButtons()
            if(actionsLeft <= 1){
                character.activated++
            }
        }
        else{
            character.activated++
        }
        activated = false
    }

    fun deactivationAnim(){
        val flipUnactive = ObjectAnimator.ofFloat(unactive, "scaleX", 0f, 0f, 0f, 1f)
        flipUnactive.setDuration(200)
        flipUnactive.start()

        val flipActive = ObjectAnimator.ofFloat(active, "scaleX", 1f, 0f, 0f, 0f)
        flipActive.setDuration(200)
        flipActive.start()

        unactive.animate().alpha(1f).duration = 100
    }

    fun turnOffActionButtons() {

        action_button1.animate().alpha(0f)
        //action_button1.visibility = View.GONE

        action_button2.animate().alpha(0f)
        //action_button2.visibility = View.GONE
    }

    fun onEndActivationNo(view: View) {
        Sounds.selectSound()
        endActivationDialog!!.dismiss()
    }

    fun onAction(view: View) {
        if(!activated){
            return
        }
        Sounds.selectSound()
        action_menu.visibility = View.INVISIBLE
        action_menu.alpha = 0f

        if (actionsLeft > 0) {
            //todo add focus symbol to attack
            if (conditionsActive[focused]) {
                attack_focused.visibility = View.VISIBLE
            } else {
                attack_focused.visibility = View.GONE
            }

            if (conditionsActive[hidden]) {
                attack_hidden.visibility = View.VISIBLE
            } else {
                attack_hidden.visibility = View.GONE
            }

            //todo add stun symbol and deactivate to move, special and attack
            if (conditionsActive[stunned]) {
                action_stunned_attack.visibility = View.VISIBLE
                action_stunned_move.visibility = View.VISIBLE
                action_stunned_special.visibility = View.VISIBLE
                action_remove_stun.visibility = View.VISIBLE
            } else {
                action_stunned_attack.visibility = View.INVISIBLE
                action_stunned_move.visibility = View.INVISIBLE
                action_stunned_special.visibility = View.INVISIBLE
                action_remove_stun.visibility = View.GONE
            }

            if (conditionsActive[bleeding]) {
                action_remove_bleeding.visibility = View.VISIBLE
            } else {
                action_remove_bleeding.visibility = View.GONE
            }

            action_menu.visibility = View.VISIBLE
            action_menu.animate().alpha(1f)
        }

    }

    fun actionCompleted() {
        if (actionUsage) {
            if (actionsLeft > 0) {
                actionsLeft--;
                if (actionsLeft == 1) {
                    action1.visibility = View.INVISIBLE
                } else if (actionsLeft == 0) {
                    action2.visibility = View.INVISIBLE
                }
                if (conditionsActive[bleeding]) {
                    onAddStrain(add_strain)
                }
                action_menu.visibility = View.INVISIBLE
            }
            if (actionsLeft <= 0) {

                action_menu.visibility = View.INVISIBLE
                if (activated) {
                    endActivationDialog!!.show()
                }
            }
        }
    }

    fun onAttack(view: View) {
        if (actionsLeft > 0) {


            if (!conditionsActive[stunned]) {
                removeCondition(focused)
                removeCondition(hidden)
                actionCompleted()
                Sounds.attackSound()
                character.attacksMade++

            }
            else{
                Sounds.negativeSound()
            }
        } else {
            showNoActionsLeftToast()
        }
    }

    fun onMove(view: View) {
        if (actionsLeft > 0) {
            Sounds.movingSound()
            if (!conditionsActive[stunned]) {
                actionCompleted()

                character.movesTaken++
            }
            else{
                Sounds.negativeSound()
            }
        } else {
            showNoActionsLeftToast()
        }
    }

    fun onSpecial(view: View) {

        if (actionsLeft > 0) {
            if (!conditionsActive[stunned]) {
                actionCompleted()
                Sounds.specialSound()
                character.specialActions++
            }
            else{
                Sounds.negativeSound()
            }
        } else {
            showNoActionsLeftToast()
        }
    }

    fun onInteract(view: View) {
        if (actionsLeft > 0) {
            actionCompleted()
            Sounds.interactSound()
            character.interactsUsed++
        } else {
            showNoActionsLeftToast()
        }
    }

    fun onPickUpCrate(view: View) {
        if (actionsLeft > 0) {
            actionCompleted()
            Sounds.crateSound()
            character.cratesPickedUp++
        } else {
            showNoActionsLeftToast()
        }
    }

    fun onRest(view: View) {
        rest(view)
        if (actionsLeft > 0 && actionUsage) {
            actionCompleted()
        }
    }

    fun rest(view:View){
        character.strain -= character.endurance
        if (character.strain < 0) {
            var healAmount = -character.strain;
            if(isWounded){
                healAmount = Math.min(character.wounded,healAmount)
            }
            for (i in 1..healAmount) {
                minusDamage()
            }
            character.strain = 0;
        }
        //add_strain.setText("" + character.strain)
        add_strain.setImageDrawable(getNumber(character.strain))
        Sounds.strainSound()
        playRestAnim()
        character.timesRested++
        quickSave()
        restDialog!!.dismiss()
    }

    fun onRemoveStun(view: View) {
        if (actionsLeft > 0 || !actionUsage) {
            removeCondition(stunned)
            Sounds.selectSound()
            actionCompleted()
        } else {
            showNoActionsLeftToast()
        }
    }

    fun onRemoveBleeding(view: View) {
        if (actionsLeft > 0 || !actionUsage) {
            removeCondition(bleeding)
            Sounds.selectSound()
            actionCompleted()
        } else {
            showNoActionsLeftToast()
        }
    }

    private fun showNoActionsLeftToast() {
        Sounds.negativeSound()
        val noActionsLeftToast = Toast(this)
        noActionsLeftToast!!.duration = Toast.LENGTH_SHORT
        noActionsLeftToast!!.view = layoutInflater.inflate(
            R.layout.toast_no_actions_left,
            character_view_group,
            false
        )
        noActionsLeftToast!!.setGravity(Gravity.CENTER, 0, 0)
        noActionsLeftToast!!.show()
    }

    fun onNextMission(view: View) {
        Sounds.selectSound()
        nextMissionDialog!!.dismiss()
        if (character.damage > 0) {
            if (character.withdrawn) {
                val slide = ObjectAnimator.ofFloat(character_images, "translationY", 0f)
                slide.setDuration(500)
                slide.start()
                character.withdrawn = false
            }
            if(isWounded){
                character.wounded = 0
                wounded.animate().alpha(0f)
                isWounded = false
            }

            character.damage = 0
            add_damage.setImageDrawable(getNumber(character.damage))
            minus_damage.animate().alpha(0f)
        }
        if (character.strain > 0) {
            character.strain = 0
            add_strain.setImageDrawable(getNumber(character.strain))
            minus_strain.animate().alpha(0f)
        }
        conditionsActive[focused] = false
        conditionsActive[hidden] = false
        conditionsActive[stunned] = false
        conditionsActive[bleeding] = false
        conditionsActive[weakened] = false
        if(activated) {
            turnOffActionButtons()
            deactivationAnim()
            activated = false
        }
        actionsLeft = 0
        updateConditionIcons()
        updateStats()
        quickSave()
    }

    fun onNextMissionNo(view: View) {
        Sounds.selectSound()
        nextMissionDialog!!.dismiss()
    }

    //endregion
    //************************************************************************************************************
    //region Show Condition Cards
    //************************************************************************************************************

    fun onShowFocusedCard(view: View) {
        showConditionCardDialog!!.condition_card_image.setImageDrawable(resources.getDrawable(R.drawable.card_focused))
        showConditionCardDialog!!.remove_button.visibility = View.GONE
        showConditionCardDialog!!.show()
    }

    fun onShowStunnedCard(view: View) {
        showConditionCardDialog!!.condition_card_image.setImageDrawable(resources.getDrawable(R.drawable.card_stunned))
        showConditionCardDialog!!.remove_button.visibility = View.GONE
        showConditionCardDialog!!.show()
    }

    fun onShowWeakenedCard(view: View) {
        showConditionCardDialog!!.condition_card_image.setImageDrawable(resources.getDrawable(R.drawable.card_weakened))
        showConditionCardDialog!!.remove_button.visibility = View.GONE
        showConditionCardDialog!!.show()
    }

    fun onShowBleedingCard(view: View) {
        showConditionCardDialog!!.condition_card_image.setImageDrawable(resources.getDrawable(R.drawable.card_bleeding))
        showConditionCardDialog!!.remove_button.visibility = View.GONE
        showConditionCardDialog!!.show()
    }

    fun onShowHiddenCard(view: View) {
        showConditionCardDialog!!.condition_card_image.setImageDrawable(resources.getDrawable(R.drawable.card_hidden))
        showConditionCardDialog!!.remove_button.visibility = View.GONE
        showConditionCardDialog!!.show()
    }

    fun onShowCard(view: View) {
        var currentCondition = Integer.parseInt(view.getTag().toString())
        when (currentCondition) {
            hidden -> onShowHiddenCard(view)
            focused -> onShowFocusedCard(view)
            stunned -> onShowStunnedCard(view)
            bleeding -> onShowBleedingCard(view)
            weakened -> onShowWeakenedCard(view)
        }
        showConditionCardDialog!!.remove_button.tag = currentCondition
        if(!action_menu.isVisible) {
            showConditionCardDialog!!.remove_button.visibility = View.VISIBLE
        }
        else{
            showConditionCardDialog!!.remove_button.visibility = View.GONE
        }
        if(conditionsActive[currentCondition]) {
            showConditionCardDialog!!.remove_button_text.text = "REMOVE"
        }
        else{
            showConditionCardDialog!!.remove_button_text.text = "APPLY"
        }
    }

    var sideMenuState = 0

//endregion
    //************************************************************************************************************
    //region Options Menu
    //************************************************************************************************************

    fun onShowOptions(view: View) {
        Sounds.selectSound()
        optionsDialog!!.show()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        //println("save")
    }

    fun onBiography(view: View) {
        Sounds.selectSound()
        optionsDialog!!.dismiss()
        bioDialog!!.bio_title.setText(character.bio_title)
        bioDialog!!.bio_quote.setText(character.bio_quote)
        bioDialog!!.bio_text.setText(character.bio_text)
        bioDialog!!.show()
    }

    fun onPower(view: View) {
        Sounds.selectSound()
        optionsDialog!!.dismiss()

        if (!isWounded) {
            showCardDialog!!.card_image.setImageBitmap(character.power)
        } else {
            showCardDialog!!.card_image.setImageBitmap(character.power_wounded)
        }

        showCardDialog!!.show()
    }

    fun onSave(view: View) {
        Sounds.selectSound()
        if (character.file_name.equals("autosave")) {
            saveDialog!!.show()
        } else {

            quickSave()
        }
        optionsDialog!!.dismiss()

    }

    fun onBackground(view: View) {
        Sounds.selectSound()
        optionsDialog!!.dismiss()
        backgroundDialog!!.show()
    }

    fun onSettings(view: View) {
        Sounds.selectSound()
        optionsDialog!!.dismiss()
        settingsScreen!!.imageSetting.visibility = View.INVISIBLE
        settingsScreen!!.settingsMenu.visibility = View.VISIBLE

        settingsScreen!!.toggleDamageAnim!!.isChecked = animateDamage
        settingsScreen!!.toggleConditionAnim!!.isChecked = animateConditions
        settingsScreen!!.toggleActionUsage!!.isChecked = actionUsage
        settingsScreen!!.soundEffectsVolume.progress = (character.soundEffectsSetting*100).toInt()

        when (character.imageSetting) {
            -1 -> {
                settingsScreen!!.imageSettingButton.text = "AUTO"
            }
            0 -> {
                settingsScreen!!.imageSettingButton.text = "DEFAULT"
            }
            1 -> {
                settingsScreen!!.imageSettingButton.text = "TIER 1"
            }
            2 -> {
                settingsScreen!!.imageSettingButton.text = "TIER 2"
            }
            3 -> {
                settingsScreen!!.imageSettingButton.text = "TIER 3"
            }
        }
        settingsScreen!!.show()

    }

    fun onStatistics(view: View) {
        Sounds.selectSound()
        optionsDialog!!.dismiss()
        initStatsScreen()
        statsScreen!!.show()
    }

    fun onCredits(view: View) {
        Sounds.selectSound()
        optionsDialog!!.dismiss()
        developersCreditsScreen!!.show()
    }


    //Backgrounds
    fun onBackgroundSnow(view: View) {
        Sounds.selectSound()
        character.background = "snow"
        background_image.setImageBitmap(getBitmap(this, "backgrounds/background_snow.jpg"))
        camouflage.setImageBitmap(getBitmap(this, "backgrounds/camo_snow.png"))

    }

    fun onBackgroundJungle(view: View) {
        Sounds.selectSound()
        character.background = "jungle"
        background_image.setImageBitmap(getBitmap(this, "backgrounds/background_jungle.jpg"))
        camouflage.setImageBitmap(getBitmap(this, "backgrounds/camo_jungle.png"))

    }

    fun onBackgroundDesert(view: View) {
        Sounds.selectSound()
        character.background = "desert"
        background_image.setImageBitmap(getBitmap(this, "backgrounds/background_desert.jpg"))
        camouflage.setImageBitmap(getBitmap(this, "backgrounds/camo_desert.png"))

    }

    fun onBackgroundBespin(view: View) {
        Sounds.selectSound()
        character.background = "bespin"
        background_image.setImageBitmap(getBitmap(this, "backgrounds/background_bespin.jpg"))
        camouflage.setImageBitmap(getBitmap(this, "backgrounds/camo_bespin.png"))

    }

    fun onBackgroundCity(view: View) {
        Sounds.selectSound()
        character.background = "city"
        background_image.setImageBitmap(getBitmap(this, "backgrounds/background_city.jpg"))
        camouflage.setImageBitmap(getBitmap(this, "backgrounds/camo_city.png"))

    }

    fun onBackgroundInterior(view: View) {
        Sounds.selectSound()
        character.background = "interior"
        background_image.setImageBitmap(getBitmap(this, "backgrounds/background_interior.jpg"))
        camouflage.setImageBitmap(getBitmap(this, "backgrounds/camo_interior.png"))


    }

    //endregion
    //************************************************************************************************************
    //region Side Navigation
    //************************************************************************************************************
    fun updateSideBarState(){
        when (sideMenuState) {
            -1 -> {
                extend_down_button.animate().alpha(0f);
                extend_up_button.animate().alpha(1f)
                kill_tracker_bar.animate().translationY(0f)
                menu_bar.animate().translationY(height)
            }
            0 -> {
                extend_down_button.animate().alpha(1f)
                extend_up_button.animate().alpha(1f)
                kill_tracker_bar.animate().translationY(-height)
                menu_bar.animate().translationY(0f)
            }
            1 -> {
                extend_down_button.animate().alpha(1f)
                extend_up_button.animate().alpha(0f)
                kill_tracker_bar.animate().translationY(-2* height)
                menu_bar.animate().translationY(-height)
            }
        }
    }

    fun onExtendDown(view: View) {
        if (sideMenuState > -1) {
            Sounds.selectSound()
            sideMenuState--
            updateSideBarState()
        }
    }

    fun onExtendUp(view: View) {
        if (sideMenuState < 1) {
            Sounds.selectSound()
            sideMenuState++
            updateSideBarState()
        }
    }

    fun onReward(view: View) {
        //resetUI()
        Sounds.selectSound()
        val intent = Intent(this, RewardsScreen::class.java)
        //intent.putExtra("Load",false)
        startActivity(intent);

    }

    fun onAccessory(view: View) {
        Sounds.selectSound()
        val intent = Intent(this, AccScreen::class.java)
        startActivity(intent);

    }

    fun onArmour(view: View) {
        Sounds.selectSound()
        val intent = Intent(this, ArmorScreen::class.java)
        startActivity(intent);

    }

    fun onMelee(view: View) {
        Sounds.selectSound()
        val intent = Intent(this, MeleeScreen::class.java)
        startActivity(intent);

    }

    fun onRange(view: View) {
        Sounds.selectSound()
        val intent = Intent(this, RangedScreen::class.java)
        startActivity(intent);
    }

    fun onXPScreen(view: View) {
        //resetUI()
        Sounds.selectSound()
        initXPScreen()
        xpSelectScreen!!.show()
    }

    //endregion
    //************************************************************************************************************
    //region Kill Tracker
    //************************************************************************************************************

    //TODO add to stats
    var killCounts = ArrayList<TextView>()
    val villain = 0
    val leader = 1
    val vehicle = 2
    val creature = 3
    val guard = 4
    val droid = 5
    val scum = 6
    val trooper = 7

    fun villainKillDown(view: View) {
        killCountDown(villain)
    }

    fun villainKillUp(view: View) {
        killCountUp(villain)
    }

    fun leaderKillDown(view: View) {
        killCountDown(leader)
    }

    fun leaderKillUp(view: View) {
        killCountUp(leader)
    }

    fun vehicleKillDown(view: View) {
        killCountDown(vehicle)
    }

    fun vehicleKillUp(view: View) {
        killCountUp(vehicle)
    }

    fun creatureKillDown(view: View) {
        killCountDown(creature)
    }

    fun creatureKillUp(view: View) {
        killCountUp(creature)
    }

    fun guardKillDown(view: View) {
        killCountDown(guard)
    }

    fun guardKillUp(view: View) {
        killCountUp(guard)
    }

    fun droidKillDown(view: View) {
        killCountDown(droid)
    }

    fun droidKillUp(view: View) {
        killCountUp(droid)
    }

    fun scumKillDown(view: View) {
        killCountDown(scum)
    }

    fun scumKillUp(view: View) {
        killCountUp(scum)
    }

    fun trooperKillDown(view: View) {
        killCountDown(trooper)
    }

    fun trooperKillUp(view: View) {
        killCountUp(trooper)
    }

    fun killCountUp(type: Int) {
        Sounds.selectSound()
        var killCount = Integer.parseInt(killCounts[type].text.toString())
        killCount++
        character.killCount[type] = killCount
        killCounts[type].setText("" + killCount)
    }

    fun killCountDown(type: Int) {
        var killCount = Integer.parseInt(killCounts[type].text.toString())
        if (killCount > 0) {
            Sounds.selectSound()
            killCount--
            character.killCount[type] = killCount
            killCounts[type].setText("" + killCount)
        }
    }

    fun onAssist(view: View) {
        Sounds.selectSound()
        when (view.tag) {
            villain -> {
                character.assistCount[villain]++
            }
            leader -> {
                character.assistCount[leader]++
            }
            vehicle -> {
                character.assistCount[vehicle]++
            }
            creature -> {
                character.assistCount[creature]++
            }
            guard -> {
                character.assistCount[guard]++
            }
            droid -> {
                character.assistCount[droid]++
            }
            scum -> {
                character.assistCount[scum]++
            }
            trooper -> {
                character.assistCount[trooper]++
            }
        }
        assistDialog!!.dismiss()
    }

    private fun initKillTracker() {

        killCounts.add(villain_count)
        villain_button.setOnLongClickListener {
            assistDialog!!.assist_icon.setImageDrawable(resources.getDrawable(R.drawable.icon_villian))
            assistDialog!!.remove_condition_button.setTag(villain)
            assistDialog!!.show()
            true
        }
        killCounts.add(leader_count)
        leader_button.setOnLongClickListener {
            assistDialog!!.assist_icon.setImageDrawable(resources.getDrawable(R.drawable.icon_leader))
            assistDialog!!.remove_condition_button.setTag(leader)
            assistDialog!!.show()
            true
        }
        killCounts.add(vehicle_count)
        vehicle_button.setOnLongClickListener {
            assistDialog!!.assist_icon.setImageDrawable(
                resources.getDrawable(
                    R.drawable.icon_vehicle
                )
            )
            assistDialog!!.remove_condition_button.setTag(vehicle)
            assistDialog!!.show()
            true
        }
        killCounts.add(creature_count)
        creature_button.setOnLongClickListener {
            assistDialog!!.assist_icon.setImageDrawable(resources.getDrawable(R.drawable.icon_creature))
            assistDialog!!.remove_condition_button.setTag(creature)
            assistDialog!!.show()
            true
        }
        killCounts.add(guard_count)
        guard_button.setOnLongClickListener {
            assistDialog!!.assist_icon.setImageDrawable(resources.getDrawable(R.drawable.icon_gaurd))
            assistDialog!!.remove_condition_button.setTag(guard)
            assistDialog!!.show()
            true
        }
        killCounts.add(droid_count)
        droid_button.setOnLongClickListener {
            assistDialog!!.assist_icon.setImageDrawable(resources.getDrawable(R.drawable.icon_droid))
            assistDialog!!.remove_condition_button.setTag(droid)
            assistDialog!!.show()
            true
        }
        killCounts.add(scum_count)
        scum_button.setOnLongClickListener {
            assistDialog!!.assist_icon.setImageDrawable(resources.getDrawable(R.drawable.icon_scum))
            assistDialog!!.remove_condition_button.setTag(scum)
            assistDialog!!.show()
            true
        }
        killCounts.add(trooper_count)
        trooper_button.setOnLongClickListener {
            assistDialog!!.assist_icon.setImageDrawable(
                resources.getDrawable(
                    R.drawable.icon_trooper
                )
            )
            assistDialog!!.remove_condition_button.setTag(trooper)
            assistDialog!!.show()
            true
        }

        for (i in 0..killCounts.size - 1) {
            killCounts[i].text = "" + character.killCount[i]
        }
    }

    //endregion
    //************************************************************************************************************
    //region Conditions
    //************************************************************************************************************
    var hidden = 0
    var focused = 1
    var weakened = 2
    var bleeding = 3
    var stunned = 4
    var conditionViews = ArrayList<ImageView>()
    var conditionsActive = booleanArrayOf(false, false, false, false, false)
    var conditionDrawable = intArrayOf(
        R.drawable.condition_hidden,
        R.drawable.condition_focused,
        R.drawable.condition_weakened,
        R.drawable.condition_bleeding,
        R.drawable.condition_stunned
    )

    private fun initConditions() {
        strengthGlow= GreenHighlight(strength_icon,this,this.resources)
        techGlow = GreenHighlight(tech_icon,this,this.resources)
        insightGlow = GreenHighlight(insight_icon,this,this.resources)
        conditionsActive = character.conditionsActive
        conditionViews.add(condition1)
        conditionViews.add(condition2)
        conditionViews.add(condition3)
        conditionViews.add(condition4)
        conditionViews.add(condition5)
        for (i in 0..conditionViews.size - 1) {
            conditionViews[i].setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(v: View): Boolean {
                    removeCondition(v.getTag() as Int)
                    actionCompleted()
                    return true
                }
            }
            )
        }
        hidden_all.setOnLongClickListener {
            removeCondition(hidden)
            true
        }
        focused_all.setOnLongClickListener {
            removeCondition(focused)

            true
        }
        weakened_all.setOnLongClickListener {
            removeCondition(weakened)

            true
        }
        bleeding_all.setOnLongClickListener {
            removeCondition(bleeding)
            actionCompleted()
            true
        }
        stunned_all.setOnLongClickListener {
            removeCondition(stunned)
            actionCompleted()
            true
        }
    }

    fun onAddCondition(view: View) {
        Sounds.selectSound()
        conditionsDialog!!.show()
    }

    fun onWeakened(view: View) {

        conditionsActive[weakened] = !conditionsActive[weakened]
        if (conditionsActive[weakened]) {
            character.timesWeakened++
            Sounds.conditionSound(weakened)
        } else {
            //character.timesWeakened--
            Sounds.selectSound()
        }
        updateConditionIcons()
    }

    fun onBleeding(view: View) {

        conditionsActive[bleeding] = !conditionsActive[bleeding]
        if (conditionsActive[bleeding]) {
            character.timesBleeding++
            Sounds.conditionSound(bleeding)
        } else {
            //character.timesBleeding--
            Sounds.selectSound()
        }
        updateConditionIcons()
    }

    fun onStunned(view: View) {

        conditionsActive[stunned] = !conditionsActive[stunned]
        if (conditionsActive[stunned]) {
            character.timesStunned++
            Sounds.conditionSound(stunned)
        } else {
            //character.timesStunned--
            Sounds.selectSound()
        }
        updateConditionIcons()
    }

    fun onHidden(view: View) {

        conditionsActive[hidden] = !conditionsActive[hidden]
        if (conditionsActive[hidden]) {
            character.timesHidden++
            Sounds.conditionSound(hidden)

        } else {
            //character.timesHidden--
            Sounds.selectSound()

        }
        updateConditionIcons()


    }


    fun onFocused(view: View) {
        conditionsActive[focused] = !conditionsActive[focused]
        if (conditionsActive[focused]) {
            character.timesFocused++
            Sounds.conditionSound(focused)
        } else {
            Sounds.selectSound()
        }
        updateConditionIcons()
    }
    private fun removeCondition(conditionType: Int) {

        if (actionsLeft > 0 || conditionType == hidden || conditionType == focused || conditionType == weakened || !actionUsage) {
            conditionsActive[conditionType] = false
            updateConditionIcons()
        } else {
            showNoActionsLeftToast()
        }
    }

    private fun updateConditionIcons() {
        //TODO condition save

        character.conditionsActive = conditionsActive
        character_image.animateConditions = animateConditions
        //quickSave()

        for (i in 0..conditionViews.size - 1) {
            conditionViews[i].visibility = View.GONE
        }
        show_all_conditions.visibility = View.GONE


        if (!animateConditions) {
            var active = 0;
            for (i in 0..conditionsActive.size - 1) {
                if (conditionsActive[i]) {
                    active++;
                }
            }
            conditions_row2.visibility = View.GONE

            if (active < 5) {
                show_conditions.visibility = View.VISIBLE
                show_all_conditions.visibility = View.INVISIBLE

                var conditionType = 0;
                for (i in 0..active - 1) {
                    conditionViews[i].visibility = View.VISIBLE
                    for (j in conditionType..conditionDrawable.size - 1) {
                        if (conditionsActive[j]) {
                            conditionViews[i].setImageDrawable(
                                resources.getDrawable(
                                    conditionDrawable[conditionType]
                                )
                            )
                            conditionViews[i].setTag(conditionType)
                            conditionType = j + 1;
                            break
                        }
                        conditionType = j + 1;
                    }
                }
            } else {
                show_conditions.visibility = View.GONE
                show_all_conditions.visibility = View.VISIBLE
            }
            if (active > 2) {
                conditions_row2.visibility = View.VISIBLE
            } else {
                conditions_row2.visibility = View.GONE
            }
        }



        if (!conditionsActive[hidden]) {
            conditionsDialog!!.hidden_select.alpha = 0.5f
            camouflage.visibility = View.GONE
        } else {
            conditionsDialog!!.hidden_select.alpha = 1f
            if (animateConditions) {
                camouflage.visibility = View.VISIBLE
            } else {
                camouflage.visibility = View.GONE
            }
        }


        if (!conditionsActive[focused]) {
            conditionsDialog!!.focused_select.alpha = 0.5f
            character_image.focused= false
            strengthGlow!!.disabled()
            techGlow!!.disabled()
            insightGlow!!.disabled()
        } else {
            conditionsDialog!!.focused_select.alpha = 1f
            character_image.focused = true
            strengthGlow!!.active()
            techGlow!!.active()
            insightGlow!!.active()
        }

        if (!conditionsActive[stunned]) {
            conditionsDialog!!.stunned_select.alpha = 0.5f
            character_image.stunned = false
        } else {
            conditionsDialog!!.stunned_select.alpha = 1f
            character_image.stunned = true
        }

        if (!conditionsActive[bleeding]) {
            conditionsDialog!!.bleeding_select.alpha = 0.5f
            character_image.bleeding = false
            bleeding_add_strain.visibility = View.INVISIBLE
        } else {
            conditionsDialog!!.bleeding_select.alpha = 1f
            character_image.bleeding= true
            bleeding_add_strain.visibility = View.VISIBLE
        }
        if (!conditionsActive[weakened]) {
            conditionsDialog!!.weakened_select.alpha = 0.5f
            character_image.weakened = false
        } else {
            conditionsDialog!!.weakened_select.alpha = 1f
            character_image.weakened = true
        }

        updateImages()

    }

    //endregion
    //************************************************************************************************************
    //region Animations
    //************************************************************************************************************
    var blastAnim:AnimationDrawable? = null
    var impactAnim:AnimationDrawable? = null
    var sliceAnim:AnimationDrawable? = null
    var lastAnim:AnimationDrawable? = null
    private fun initAnimations() {
        //rest_animation.setBackgroundDrawable(MainActivity.restAnim)
        //rest_animation.visibility = FrameLayout.INVISIBLE
        blastAnim = resources.getDrawable(R.drawable.blast) as AnimationDrawable
        impactAnim = resources.getDrawable(R.drawable.impact) as AnimationDrawable
        sliceAnim = resources.getDrawable(R.drawable.slice) as AnimationDrawable
    }

    private fun resetAnim() {
        lastAnim?.stop()
        lastAnim?.selectDrawable(0)
    }
    private fun playAnim(anim:AnimationDrawable){
        resetAnim()
        damage_animation.setImageDrawable(anim)
        anim!!.start()
        lastAnim = anim
    }
    private fun playDamageAnim() {
        if (animateDamage) {
            damage_animation.visibility = View.VISIBLE
            val animType = Math.random();
            if (animType < 0.3) {
                Sounds.damagedSound(this, Sounds.gaster_blaster_master)
                if(blastAnim != null) {
                    playAnim(blastAnim!!)
                }
            } else if (animType < 0.6) {
                Sounds.damagedSound(this, Sounds.slice)
                if(sliceAnim != null) {
                    playAnim(sliceAnim!!)
                }
            } else {
                Sounds.damagedSound(this, Sounds.impact)
                if(impactAnim != null) {
                    playAnim(impactAnim!!)
                }
            }
        }
        else{
            Sounds.damagedSound(this, Sounds.impact)
        }
        hitAnim();
    }

    fun hitAnim(){
        var hitY = ObjectAnimator.ofFloat(
            character_images, "translationY", 0f, 20f * Random
                .nextFloat(),
            0f, 20f * Random.nextFloat(), 0f, 20f * Random.nextFloat(), 0f
        )
        hitY.setDuration(300)
        hitY.start()

        var hitX = ObjectAnimator.ofFloat(
            character_images, "translationX", 0f, -10f * Random
                .nextFloat(),
            0f, -10f * Random.nextFloat(), 0f, -10f * Random.nextFloat(), 0f
        )
        hitX.setDuration(300)
        hitX.start()
    }

    private fun playRestAnim() {
        if (animateDamage) {
            var restAnim = ObjectAnimator.ofFloat(rest_animation, "alpha", 0f, 1f, 0.75f, 0.25f, 0f)
            restAnim.duration = 200
            restAnim.start()
        }
    }

    //endregion
    //************************************************************************************************************
    //region Dialogs and Screens
    //************************************************************************************************************

    var restDialog: Dialog? = null
    var unwoundDialog: Dialog? = null
    var conditionsDialog: Dialog? = null
    var showCardDialog: Dialog? = null
    var showConditionCardDialog: Dialog? = null
    var endActivationDialog: Dialog? = null
    var assistDialog: Dialog? = null
    var optionsDialog: Dialog? = null
    var bioDialog: Dialog? = null
    var saveDialog: Dialog? = null
    var backgroundDialog: Dialog? = null
    var settingsScreen: Dialog? = null
    var statsScreen: Dialog? = null
    var xpSelectScreen: Dialog? = null
    var developersCreditsScreen: Dialog? = null
    var quickViewDialog: Dialog? = null
    var quickViewButtonDialog: Dialog? = null
    var nextMissionDialog: Dialog? = null

    private fun initDialogs() {
        initRestDialog()
        initUnwoundDialog()
        initConditionsDialog()
        initOptionsDialog()
        initSaveDialog()
        initShowCardDialog()
        initShowConditionCardDialog()
        initEndActivationDialog()
        initAssistDialog()
        initBioDialog()
        initBackgroundDialog()
        initSettingsDialog()
        initStatsScreenDialog()
        initCreditsScreenDialog()
        initXpSelectScreenDialog()
        initQuickViewDialog()
        initNextMission()
    }

    private fun initRestDialog() {
        restDialog = Dialog(this)
        restDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        restDialog!!.setCancelable(false)
        restDialog!!.setContentView(R.layout.dialog_rest)
        restDialog!!.setCanceledOnTouchOutside(true)
        restDialog!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        restDialog!!.rest_button.setOnClickListener {
            onRest(restDialog!!.rest_button)
            //TODO
            //quickSave()
            true
        }
    }

    private fun initUnwoundDialog() {
        unwoundDialog = Dialog(this)
        unwoundDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        unwoundDialog!!.setCancelable(false)
        unwoundDialog!!.setContentView(R.layout.dialog_unwound)
        unwoundDialog!!.setCanceledOnTouchOutside(true)
        unwoundDialog!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        unwoundDialog!!.remove_condition_button.setOnClickListener {
            onUnwound(unwoundDialog!!.remove_condition_button)
            //TODO

            true
        }
    }

    private fun initConditionsDialog() {
        conditionsDialog = Dialog(this)
        conditionsDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        conditionsDialog!!.setCancelable(false)
        conditionsDialog!!.setContentView(R.layout.dialog_conditions)
        conditionsDialog!!.setCanceledOnTouchOutside(true)
        conditionsDialog!!.stunned_select.setOnLongClickListener {
            onShowCard(conditionsDialog!!.stunned_select)
            true
        }
        conditionsDialog!!.bleeding_select.setOnLongClickListener {
            onShowCard(conditionsDialog!!.bleeding_select)
            true
        }
        conditionsDialog!!.weakened_select.setOnLongClickListener {
            onShowCard(conditionsDialog!!.weakened_select)
            true
        }
        conditionsDialog!!.focused_select.setOnLongClickListener {
            onShowCard(conditionsDialog!!.focused_select)
            true
        }
        conditionsDialog!!.hidden_select.setOnLongClickListener {
            onShowCard(conditionsDialog!!.hidden_select)
            true
        }
        conditionsDialog!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
    }

    private fun initOptionsDialog() {
        optionsDialog = Dialog(this)
        optionsDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        optionsDialog!!.setCancelable(false)
        optionsDialog!!.setContentView(R.layout.dialog_options)
        optionsDialog!!.setCanceledOnTouchOutside(true)
        optionsDialog!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        optionsDialog!!.bioOption.setOnClickListener {
            onBiography(optionsDialog!!.bioOption)
            true
        }
        optionsDialog!!.powerOption.setOnClickListener {
            onPower(optionsDialog!!.powerOption)
            true
        }
        optionsDialog!!.settingsOption.setOnClickListener {
            onSettings(optionsDialog!!.settingsOption)
            true
        }
        optionsDialog!!.saveOption.setOnClickListener {
            onSave(optionsDialog!!.saveOption)
            true
        }
        optionsDialog!!.statsOption.setOnClickListener {
            onStatistics(optionsDialog!!.statsOption)
            true
        }
        optionsDialog!!.backgroundOption.setOnClickListener {
            onBackground(optionsDialog!!.backgroundOption)
            true
        }
        optionsDialog!!.creditsOption.setOnClickListener {
            onCredits(optionsDialog!!.backgroundOption)
            true
        }
    }

    private fun initSaveDialog() {
        saveDialog = Dialog(this)
        saveDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        saveDialog!!.setCancelable(false)
        saveDialog!!.setContentView(R.layout.dialog_save)
        saveDialog!!.setCanceledOnTouchOutside(true)
        saveDialog!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        saveDialog!!.save_button.setOnClickListener {
            if (character.file_name.equals("autosave")) {

                firstManualSave()
            } else {
                quickSave()
            }
            Sounds.selectSound()
            saveDialog!!.dismiss()
            true
        }
        saveDialog!!.cancel_button.setOnClickListener {
            Sounds.selectSound()
            saveDialog!!.dismiss()
            true
        }
    }

    private fun initShowCardDialog() {
        showCardDialog = Dialog(this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
        showCardDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        showCardDialog!!.setContentView(R.layout.dialog_show_card)
        showCardDialog!!.setCancelable(true)
        showCardDialog!!.setCanceledOnTouchOutside(true)
        showCardDialog!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        showCardDialog!!.show_card_dialog.setOnClickListener {
            Sounds.selectSound()
            showCardDialog!!.dismiss()
            true
        }

    }


    private fun initShowConditionCardDialog() {
        showConditionCardDialog = Dialog(this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
        showConditionCardDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        showConditionCardDialog!!.setContentView(R.layout.dialog_show_condition_card)
        showConditionCardDialog!!.setCancelable(true)
        showConditionCardDialog!!.setCanceledOnTouchOutside(true)
        showConditionCardDialog!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        showConditionCardDialog!!.show_condition_card_dialog.setOnClickListener {
            Sounds.selectSound()
            showConditionCardDialog!!.dismiss()
            true
        }

        showConditionCardDialog!!.remove_button.setOnClickListener {
            Sounds.selectSound()
            var currentCondition = Integer.parseInt(showConditionCardDialog!!.remove_button.tag.toString())
            when (currentCondition){
                hidden -> onHidden(showConditionCardDialog!!.condition_card_image)
                focused -> onFocused(showConditionCardDialog!!.condition_card_image)
                stunned -> onStunned(showConditionCardDialog!!.condition_card_image)
                bleeding -> onBleeding(showConditionCardDialog!!.condition_card_image)
                weakened -> onWeakened(showConditionCardDialog!!.condition_card_image)
            }
            showConditionCardDialog!!.dismiss()
            true
        }
    }

    private fun initEndActivationDialog() {
        endActivationDialog = Dialog(this)
        endActivationDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        endActivationDialog!!.setCancelable(false)
        endActivationDialog!!.setContentView(R.layout.dialog_end_activation)
        endActivationDialog!!.setCanceledOnTouchOutside(true)
        endActivationDialog!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
    }

    private fun initAssistDialog() {
        assistDialog = Dialog(this)
        assistDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        assistDialog!!.setCancelable(false)
        assistDialog!!.setContentView(R.layout.dialog_assist)
        assistDialog!!.setCanceledOnTouchOutside(true)
        assistDialog!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        assistDialog!!.remove_condition_button.setOnClickListener {
            onAssist(assistDialog!!.remove_condition_button)
            //TODO
            quickSave()
            true
        }
    }

    private fun initBioDialog() {
        bioDialog = Dialog(this)
        bioDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bioDialog!!.setCancelable(true)
        bioDialog!!.setContentView(R.layout.dialog_bio)
        bioDialog!!.setCanceledOnTouchOutside(true)
        bioDialog!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        bioDialog!!.bio_layout.setOnClickListener {
            Sounds.selectSound()
            bioDialog!!.dismiss()
            true
        }
    }

    private fun initCreditsScreenDialog() {
        developersCreditsScreen =
            Dialog(this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
        developersCreditsScreen!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        developersCreditsScreen!!.setCancelable(false)
        developersCreditsScreen!!.setContentView(R.layout.credits_to_us)
        developersCreditsScreen!!.setCanceledOnTouchOutside(true)
        developersCreditsScreen!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        developersCreditsScreen!!.textView41.setMovementMethod(LinkMovementMethod.getInstance())
        developersCreditsScreen!!.textView43.setMovementMethod(LinkMovementMethod.getInstance())
        developersCreditsScreen!!.textView44.setMovementMethod(LinkMovementMethod.getInstance())
        developersCreditsScreen!!.mannyPortfolio.setMovementMethod(LinkMovementMethod.getInstance())
        developersCreditsScreen!!.davidPortfolio.setMovementMethod(LinkMovementMethod.getInstance())
        developersCreditsScreen!!.mikeImageLink.setOnClickListener{
            var linkIntent = Intent()
                .setAction(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .setData(Uri.parse("http://www.mikeglasswell.com/"))
            startActivity(linkIntent)
        }
        developersCreditsScreen!!.mannyImageLink.setOnClickListener{
            var linkIntent = Intent()
                .setAction(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .setData(Uri.parse("https://mansinh-d25ff.web.app"))
            startActivity(linkIntent)
        }
        developersCreditsScreen!!.HeroXImageLink.setOnClickListener{
            var linkIntent = Intent()
                .setAction(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .setData(Uri.parse("https://twitter.com/Matthew_Hero_X"))
            startActivity(linkIntent)
        }
    }

    private fun initSettingsDialog() {
        settingsScreen = Dialog(this)
        settingsScreen!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        settingsScreen!!.setCancelable(false)
        settingsScreen!!.setContentView(R.layout.screen_settings)
        settingsScreen!!.setCanceledOnTouchOutside(true)
        settingsScreen!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))

        settingsScreen!!.toggleDamageAnim!!.isChecked = animateDamage
        settingsScreen!!.toggleDamageAnim.setOnClickListener {
            animateDamage = settingsScreen!!.toggleDamageAnim!!.isChecked
            character.damageAnimSetting = animateDamage
            Sounds.selectSound()
        }

        settingsScreen!!.toggleConditionAnim!!.isChecked = animateConditions
        settingsScreen!!.toggleConditionAnim.setOnClickListener {
            animateConditions = settingsScreen!!.toggleConditionAnim!!.isChecked
            character.conditionAnimSetting = animateConditions
            Sounds.selectSound()
            updateConditionIcons()
        }

        settingsScreen!!.toggleActionUsage.isChecked = actionUsage
        settingsScreen!!.toggleActionUsage.setOnClickListener {
            actionUsage = settingsScreen!!.toggleActionUsage.isChecked
            character.actionUsageSetting = actionUsage
            Sounds.selectSound()
            if (actionUsage) {
                if (activated) {
                    turnOnActionButtons()
                }
            } else {
                turnOffActionButtons()
            }
        }
        settingsScreen!!.imageSettingButton.setOnClickListener {
            settingsScreen!!.imageSetting.visibility = View.VISIBLE
            setPreviewImage(character.imageSetting)
            settingsScreen!!.settingsMenu.visibility = View.INVISIBLE

        }
        settingsScreen!!.imagePreview.setOnClickListener {
            settingsScreen!!.imageSetting.visibility = View.INVISIBLE
            settingsScreen!!.settingsMenu.visibility = View.VISIBLE
            Sounds.selectSound()
        }
        settingsScreen!!.imageAuto.setOnClickListener {
            setPreviewImage(-1)
        }
        settingsScreen!!.imageDefault.setOnClickListener {
            setPreviewImage(0)
        }
        settingsScreen!!.imageTier1.setOnClickListener {
            setPreviewImage(1)
        }
        settingsScreen!!.imageTier2.setOnClickListener {
            setPreviewImage(2)
        }
        settingsScreen!!.imageTier3.setOnClickListener {
            setPreviewImage(3)
        }

        settingsScreen!!.soundEffectsVolume.progress = (character.soundEffectsSetting*100).toInt()
        settingsScreen!!.soundEffectsVolume.setOnSeekBarChangeListener(object :SeekBar
        .OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Sounds.selectSound()
                character.soundEffectsSetting = p1.toFloat()/100
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {


            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })

    }

    private fun initNextMission(){
        nextMissionDialog = Dialog(this)
        nextMissionDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        nextMissionDialog!!.setCancelable(false)
        nextMissionDialog!!.setContentView(R.layout.dialog_next_mission)
        nextMissionDialog!!.setCanceledOnTouchOutside(true)
        nextMissionDialog!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))

        activationButton.setOnLongClickListener{
            nextMissionDialog!!.show()
            true
        }

    }

    private fun setPreviewImage(setting:Int) {
        Sounds.selectSound()
        character.imageSetting = setting

        character.updateCharacterImages(this)
        settingsScreen!!.imagePreview.setImageBitmap(character.currentImage)
        settingsScreen!!.imageAuto.alpha = 0.5f
        settingsScreen!!.imageDefault.alpha = 0.5f
        settingsScreen!!.imageTier1.alpha = 0.5f
        settingsScreen!!.imageTier2.alpha = 0.5f
        settingsScreen!!.imageTier3.alpha = 0.5f

        when (setting) {
            -1 -> {
                settingsScreen!!.imageAuto.alpha = 1f
                settingsScreen!!.imageSettingButton.text = "AUTO"
            }
            0 -> {
                settingsScreen!!.imageDefault.alpha = 1f
                settingsScreen!!.imageSettingButton.text = "DEFAULT"
            }
            1 -> {
                settingsScreen!!.imageTier1.alpha = 1f
                settingsScreen!!.imageSettingButton.text = "TIER 1"
            }
            2 -> {
                settingsScreen!!.imageTier2.alpha = 1f
                settingsScreen!!.imageSettingButton.text = "TIER 2"
            }
            3 -> {
                settingsScreen!!.imageTier3.alpha = 1f
                settingsScreen!!.imageSettingButton.text = "TIER 3"
            }
        }
    }
    private fun initBackgroundDialog() {
        backgroundDialog = Dialog(this)
        backgroundDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        backgroundDialog!!.setCancelable(false)
        backgroundDialog!!.setContentView(R.layout.dialog_background)
        backgroundDialog!!.setCanceledOnTouchOutside(true)
        backgroundDialog!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        backgroundDialog!!.desert_background.setOnClickListener {
            onBackgroundDesert(backgroundDialog!!.desert_background)
            //quickSave()
        }
        backgroundDialog!!.snow_background.setOnClickListener {
            onBackgroundSnow(backgroundDialog!!.snow_background)
            //quickSave()
        }
        backgroundDialog!!.jungle_background.setOnClickListener {
            onBackgroundJungle(backgroundDialog!!.jungle_background)
            // quickSave()
        }
        backgroundDialog!!.interior_background.setOnClickListener {
            onBackgroundInterior(backgroundDialog!!.interior_background)
            //  quickSave()
        }
        backgroundDialog!!.city_background.setOnClickListener {
            onBackgroundCity(backgroundDialog!!.city_background)
            //  quickSave()
        }
        backgroundDialog!!.bespin_background.setOnClickListener {
            onBackgroundBespin(backgroundDialog!!.bespin_background)
            //  quickSave()
        }
    }

    private fun initStatsScreenDialog() {
        statsScreen = Dialog(this)
        statsScreen!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        statsScreen!!.setCancelable(false)
        statsScreen!!.setContentView(R.layout.screen_stats)
        statsScreen!!.setCanceledOnTouchOutside(true)
        statsScreen!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
    }

    private fun initXpSelectScreenDialog() {
        xpSelectScreen = Dialog(this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
        xpSelectScreen!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        xpSelectScreen!!.setCancelable(false)
        xpSelectScreen!!.setContentView(R.layout.screen_xp_select)
        xpSelectScreen!!.setCanceledOnTouchOutside(true)
        xpSelectScreen!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
    }

    private fun initQuickViewDialog() {
        quickViewDialog = Dialog(this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
        quickViewDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        quickViewDialog!!.setCancelable(false)
        quickViewDialog!!.setContentView(R.layout.dialog_quick_view)
        quickViewDialog!!.setCanceledOnTouchOutside(true)
        quickViewDialog!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))

        character_images.setOnLongClickListener {
            quickViewButtonDialog!!.show()
            true
        }
        quickViewDialog!!.quick_view_back.setOnClickListener {
            quickViewDialog!!.dismiss()
        }



        quickViewButtonDialog = Dialog(this)
        quickViewButtonDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        quickViewButtonDialog!!.setCancelable(false)
        quickViewButtonDialog!!.setContentView(R.layout.dialog_quick_view_button)
        quickViewButtonDialog!!.setCanceledOnTouchOutside(true)
        quickViewDialog!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        quickViewDialog!!.quick_view_weapon.setOnClickListener { onShowItemCard(quickViewDialog!!.quick_view_weapon) }
        quickViewDialog!!.quick_view_weapon1.setOnClickListener { onShowItemCard(quickViewDialog!!.quick_view_weapon1) }
        quickViewDialog!!.quick_view_armor.setOnClickListener { onShowItemCard(quickViewDialog!!.quick_view_armor) }
        quickViewDialog!!.quick_view_acc.setOnClickListener { onShowItemCard(quickViewDialog!!.quick_view_acc) }
        quickViewDialog!!.quick_view_acc1.setOnClickListener { onShowItemCard(quickViewDialog!!.quick_view_acc1) }
        quickViewDialog!!.quick_view_acc2.setOnClickListener { onShowItemCard(quickViewDialog!!.quick_view_acc2) }


        var mods = quickViewDialog!!.quickview_mods
        var mods1 = quickViewDialog!!.quickview_mods1

        mods!!.setOnClickListener {
            Sounds.selectSound()
            quickViewDialog!!.show_mods.visibility = View.VISIBLE
        }
        mods1!!.setOnClickListener {
            Sounds.selectSound()
            quickViewDialog!!.show_mods.visibility = View.VISIBLE
        }
        quickViewDialog!!.show_mods.setOnClickListener{
            quickViewDialog!!.show_mods.visibility = View.INVISIBLE
        }

        var xp = quickViewDialog!!.quickview_xp
        xp!!.setOnClickListener {
            onXPScreen(xp)
        }

        var reward = quickViewDialog!!.quickview_reward
        reward!!.setOnClickListener {
            onReward(reward)
        }

        quickViewButtonDialog!!.quick_view_button.setOnClickListener {
            Sounds.selectSound()
            var weaponIndex = character.weapons.getOrElse(0) { -1 }
            var weaponIndex1 = character.weapons.getOrElse(1) { -1 }
            /*
            if (weaponIndex < 0) {
                mods.alpha = 0f

            }*/
            //if (weaponIndex1 < 0) {
            mods1.alpha = 0f
            //}


            var imageId = R.drawable.empty_item_slot
            quickViewDialog!!.weapon_type.visibility = View.VISIBLE

            if(weaponIndex>0) {
                if (  weaponIndex == 27) {
                    quickViewDialog!!.quick_view_weapon.setImageBitmap(character.startingMeleeWeapon)

                }
                else if(weaponIndex == 52){
                    quickViewDialog!!.quick_view_weapon.setImageBitmap(character.startingRangedWeapon)

                }
                else {
                    imageId = Items.itemsArray!![weaponIndex].resourceId
                    quickViewDialog!!.quick_view_weapon.setImageResource(imageId)
                }

                if (Items.itemsArray!![weaponIndex].type == Items.ranged) {
                    quickViewDialog!!.weapon_type.setImageResource(R.drawable.item_ranged)
                } else {
                    quickViewDialog!!.weapon_type.setImageResource(R.drawable.item_melee)
                }
            }
            else{
                quickViewDialog!!.quick_view_weapon1.setImageResource(imageId)
            }

            imageId = R.drawable.empty_item_slot
            quickViewDialog!!.weapon1_type.visibility = View.VISIBLE
            if(weaponIndex1>0){
                if (  weaponIndex1 == 27) {
                    quickViewDialog!!.quick_view_weapon1.setImageBitmap(character.startingMeleeWeapon)

                }
                else if(weaponIndex1 == 52){
                    quickViewDialog!!.quick_view_weapon1.setImageBitmap(character.startingRangedWeapon)

                }
                else{
                    imageId = Items.itemsArray!![weaponIndex1].resourceId
                    quickViewDialog!!.quick_view_weapon1.setImageResource(imageId)
                }

                if(Items.itemsArray!![weaponIndex1].type == Items.ranged){
                    quickViewDialog!!.weapon1_type.setImageResource(R.drawable.item_ranged)
                }
                else {
                    quickViewDialog!!.weapon1_type.setImageResource(R.drawable.item_melee)
                }
            }
            else{
                quickViewDialog!!.quick_view_weapon1.setImageResource(imageId)
            }


            imageId = R.drawable.empty_item_slot
            quickViewDialog!!.armor_type.visibility = View.VISIBLE
            var armorIndex = character.armor.getOrElse(0) { -1 }
            if (armorIndex >= 0) {
                imageId = Items.itemsArray!![armorIndex].resourceId
                //quickViewDialog!!.armor_type.visibility = View.GONE
            }
            quickViewDialog!!.quick_view_armor.setImageResource(imageId)


            imageId = R.drawable.empty_item_slot
            quickViewDialog!!.acc_type.visibility = View.VISIBLE
            var accIndex = character.accessories.getOrElse(0) { -1 }
            if (accIndex >= 0) {
                imageId = Items.itemsArray!![accIndex].resourceId
                //quickViewDialog!!.acc_type.visibility = View.GONE
            }
            quickViewDialog!!.quick_view_acc.setImageResource(imageId)


            imageId = R.drawable.empty_item_slot
            quickViewDialog!!.acc1_type.visibility = View.VISIBLE
            var accIndex1 = character.accessories.getOrElse(1) { -1 }
            if (accIndex1 >= 0) {
                imageId = Items.itemsArray!![accIndex1].resourceId
                quickViewDialog!!.quick_view_acc1.setImageResource(imageId)
                //quickViewDialog!!.acc1_type.visibility = View.GONE
            }
            quickViewDialog!!.quick_view_acc1.setImageResource(imageId)

            imageId = R.drawable.empty_item_slot
            quickViewDialog!!.acc2_type.visibility = View.VISIBLE
            var accIndex2 = character.accessories.getOrElse(2) { -1 }
            if (accIndex2 >= 0) {
                imageId = Items.itemsArray!![accIndex2].resourceId
                //quickViewDialog!!.acc2_type.visibility = View.GONE
            }
            quickViewDialog!!.quick_view_acc2.setImageResource(imageId)

            println()
            println("MODS SIZE " + character.mods.size)
            println()



            quickViewDialog!!.mods_grid_view.adapter = com.glasswellapps.iact.character.ModListAdapter(this,character.mods)
            quickViewDialog!!.show()
            quickViewButtonDialog!!.dismiss()
        }
    }

    fun onShowItemCard(view: ImageView) {
        var image = ((view).drawable as BitmapDrawable).bitmap
        showCardDialog!!.card_image.setImageBitmap(image)
        showCardDialog!!.show()

    }


    //endregion
    //************************************************************************************************************
    //region Stats Screen
    //************************************************************************************************************
    fun initStatsScreen() {


        statsScreen!!.edit_stat.visibility = View.INVISIBLE
        statsScreen!!.stats_name.setText("" + character.name)
        statsScreen!!.stats_portrait_image.setImageDrawable(character.portraitImage)
        var level = 5
        if (character.xpSpent <= 1) {
            level = 1
        } else if (character.xpSpent <= 4) {
            level = 2
        } else if (character.xpSpent <= 7) {
            level = 3
        } else if (character.xpSpent <= 10) {
            level = 4
        }

        statsScreen!!.stats_level.setText("" + level)
        statsScreen!!.stats_moves.setText("" + character.movesTaken)
        statsScreen!!.stats_attacks.setText("" + character.attacksMade)
        statsScreen!!.stats_interacts.setText("" + character.interactsUsed)
        statsScreen!!.stats_wounded.setText("" + character.timesWounded)
        statsScreen!!.stats_rested.setText("" + character.timesRested)
        statsScreen!!.stats_withdrawn.setText("" + character.timesWithdrawn)
        statsScreen!!.stats_activated.setText("" + character.activated)
        statsScreen!!.stats_damaged.setText("" + character.damageTaken)
        statsScreen!!.stats_strain.setText("" + character.strainTaken)
        statsScreen!!.stats_specials.setText("" + character.specialActions)
        statsScreen!!.stats_focused.setText("" + character.timesFocused)
        statsScreen!!.stats_hidden.setText("" + character.timesHidden)
        statsScreen!!.stats_stunned.setText("" + character.timesStunned)
        statsScreen!!.stats_bleeding.setText("" + character.timesBleeding)
        statsScreen!!.stats_weakened.setText("" + character.timesWeakened)
        statsScreen!!.stats_crates.setText("" + character.cratesPickedUp)

        if (character.rewardObtained) {
            statsScreen!!.stats_reward_obtained.setText("Yes")
        } else {
            statsScreen!!.stats_reward_obtained.setText("No")
        }

        setTotalKills()

        statsScreen!!.stats_kill_villain.setText("" + character.killCount[villain])
        statsScreen!!.stats_kill_vehicle.setText("" + character.killCount[vehicle])
        statsScreen!!.stats_kill_creature.setText("" + character.killCount[creature])
        statsScreen!!.stats_kill_leader.setText("" + character.killCount[leader])
        statsScreen!!.stats_kill_guardian.setText("" + character.killCount[guard])
        statsScreen!!.stats_kill_droid.setText("" + character.killCount[droid])
        statsScreen!!.stats_kill_scum.setText("" + character.killCount[scum])
        statsScreen!!.stats_kill_trooper.setText("" + character.killCount[trooper])


        statsScreen!!.stats_assist_villain.setText("" + character.assistCount[villain])
        statsScreen!!.stats_assist_vehicle.setText("" + character.assistCount[vehicle])
        statsScreen!!.stats_assist_creature.setText("" + character.assistCount[creature])
        statsScreen!!.stats_assist_leader.setText("" + character.assistCount[leader])
        statsScreen!!.stats_assist_guardian.setText("" + character.assistCount[guard])
        statsScreen!!.stats_assist_droid.setText("" + character.assistCount[droid])
        statsScreen!!.stats_assist_scum.setText("" + character.assistCount[scum])
        statsScreen!!.stats_assist_trooper.setText("" + character.assistCount[trooper])


        statsScreen!!.stats_moves.setOnClickListener { onTapStat(statsScreen!!.stats_moves) }
        statsScreen!!.stats_attacks.setOnClickListener { onTapStat(statsScreen!!.stats_attacks) }
        statsScreen!!.stats_interacts.setOnClickListener { onTapStat(statsScreen!!.stats_interacts) }
        statsScreen!!.stats_wounded.setOnClickListener { onTapStat(statsScreen!!.stats_wounded) }
        statsScreen!!.stats_rested.setOnClickListener { onTapStat(statsScreen!!.stats_rested) }
        statsScreen!!.stats_withdrawn.setOnClickListener { onTapStat(statsScreen!!.stats_withdrawn) }
        statsScreen!!.stats_activated.setOnClickListener { onTapStat(statsScreen!!.stats_activated) }
        statsScreen!!.stats_damaged.setOnClickListener { onTapStat(statsScreen!!.stats_damaged) }
        statsScreen!!.stats_strain.setOnClickListener { onTapStat(statsScreen!!.stats_strain) }
        statsScreen!!.stats_specials.setOnClickListener { onTapStat(statsScreen!!.stats_specials) }
        statsScreen!!.stats_focused.setOnClickListener { onTapStat(statsScreen!!.stats_focused) }
        statsScreen!!.stats_hidden.setOnClickListener { onTapStat(statsScreen!!.stats_hidden) }
        statsScreen!!.stats_stunned.setOnClickListener { onTapStat(statsScreen!!.stats_stunned) }
        statsScreen!!.stats_bleeding.setOnClickListener { onTapStat(statsScreen!!.stats_bleeding) }
        statsScreen!!.stats_weakened.setOnClickListener { onTapStat(statsScreen!!.stats_weakened) }
        statsScreen!!.stats_crates.setOnClickListener { onTapStat(statsScreen!!.stats_crates) }
        statsScreen!!.stats_kill_villain.setOnClickListener {
            onTapStat(
                statsScreen!!
                    .stats_kill_villain
            )
        }
        statsScreen!!.stats_kill_vehicle.setOnClickListener {
            onTapStat(
                statsScreen!!
                    .stats_kill_vehicle
            )
        }
        statsScreen!!.stats_kill_creature.setOnClickListener {
            onTapStat(
                statsScreen!!
                    .stats_kill_creature
            )
        }
        statsScreen!!.stats_kill_leader.setOnClickListener { onTapStat(statsScreen!!.stats_kill_leader) }
        statsScreen!!.stats_kill_guardian.setOnClickListener {
            onTapStat(
                statsScreen!!
                    .stats_kill_guardian
            )
        }
        statsScreen!!.stats_kill_droid.setOnClickListener {
            onTapStat(
                statsScreen!!
                    .stats_kill_droid
            )
        }
        statsScreen!!.stats_kill_scum.setOnClickListener { onTapStat(statsScreen!!.stats_kill_scum) }
        statsScreen!!.stats_kill_trooper.setOnClickListener {
            onTapStat(
                statsScreen!!
                    .stats_kill_trooper
            )
        }


        statsScreen!!.stats_assist_villain.setOnClickListener {
            onTapStat(
                statsScreen!!
                    .stats_assist_villain
            )
        }
        statsScreen!!.stats_assist_vehicle.setOnClickListener {
            onTapStat(
                statsScreen!!
                    .stats_assist_vehicle
            )
        }
        statsScreen!!.stats_assist_creature.setOnClickListener {
            onTapStat(
                statsScreen!!
                    .stats_assist_creature
            )
        }
        statsScreen!!.stats_assist_leader.setOnClickListener {
            onTapStat(
                statsScreen!!
                    .stats_assist_leader
            )
        }
        statsScreen!!.stats_assist_guardian.setOnClickListener {
            onTapStat(
                statsScreen!!
                    .stats_assist_guardian
            )
        }
        statsScreen!!.stats_assist_droid.setOnClickListener {
            onTapStat(
                statsScreen!!
                    .stats_assist_droid
            )
        }
        statsScreen!!.stats_assist_scum.setOnClickListener {
            onTapStat(
                statsScreen!!
                    .stats_assist_scum
            )
        }
        statsScreen!!.stats_assist_trooper.setOnClickListener {
            onTapStat(
                statsScreen!!
                    .stats_assist_trooper
            )
        }


        statsScreen!!.setOnCancelListener {
            character.movesTaken = Integer.parseInt(statsScreen!!.stats_moves.getText().toString())
            character.attacksMade =
                Integer.parseInt(statsScreen!!.stats_attacks.getText().toString())
            character.interactsUsed =
                Integer.parseInt(statsScreen!!.stats_interacts.getText().toString())
            character.timesWounded =
                Integer.parseInt(statsScreen!!.stats_wounded.getText().toString())
            character.timesRested =
                Integer.parseInt(statsScreen!!.stats_rested.getText().toString())
            character.timesWithdrawn =
                Integer.parseInt(statsScreen!!.stats_withdrawn.getText().toString())
            character.activated =
                Integer.parseInt(statsScreen!!.stats_activated.getText().toString())
            character.damageTaken =
                Integer.parseInt(statsScreen!!.stats_damaged.getText().toString())
            character.strainTaken =
                Integer.parseInt(statsScreen!!.stats_strain.getText().toString())
            character.specialActions =
                Integer.parseInt(statsScreen!!.stats_specials.getText().toString())
            character.timesFocused =
                Integer.parseInt(statsScreen!!.stats_focused.getText().toString())
            character.timesHidden =
                Integer.parseInt(statsScreen!!.stats_hidden.getText().toString())
            character.timesStunned =
                Integer.parseInt(statsScreen!!.stats_stunned.getText().toString())
            character.timesBleeding =
                Integer.parseInt(statsScreen!!.stats_bleeding.getText().toString())
            character.timesWeakened =
                Integer.parseInt(statsScreen!!.stats_weakened.getText().toString())
            character.cratesPickedUp =
                Integer.parseInt(statsScreen!!.stats_crates.getText().toString())

            character.rewardObtained =
                statsScreen!!.stats_reward_obtained.getText().toString().equals("Yes")

            updateKillTracker()
            quickSave()
        }
        statsScreen!!.addStat.setOnClickListener {
            if (currentStatEditing != null) {
                currentStatEditing!!.text = "" + (Integer.parseInt(
                    (currentStatEditing!!.getText
                        ().toString())
                ) + 1)
                statsScreen!!.stat_text.setText(currentStatEditing!!.text.toString())
                updateKillTracker()
            }
        }
        statsScreen!!.minusStat.setOnClickListener {
            if (currentStatEditing != null ) {
                currentStatEditing!!.text = "" + Math.max(Integer.parseInt(
                    (currentStatEditing!!.getText()
                        .toString())
                ) - 1,0)
                statsScreen!!.stat_text.setText(currentStatEditing!!.text.toString())
                updateKillTracker()
            }
        }
        statsScreen!!.edit_stat.setOnClickListener {
            statsScreen!!.edit_stat.visibility = View.INVISIBLE
            updateKillTracker()
        }


    }

    var currentStatEditing: TextView? = null
    fun updateKillTracker() {
        character.killCount[villain] =
            Integer.parseInt(statsScreen!!.stats_kill_villain.getText().toString())
        character.killCount[vehicle] =
            Integer.parseInt(statsScreen!!.stats_kill_vehicle.getText().toString())
        character.killCount[creature] =
            Integer.parseInt(statsScreen!!.stats_kill_creature.getText().toString())
        character.killCount[leader] =
            Integer.parseInt(statsScreen!!.stats_kill_leader.getText().toString())
        character.killCount[guard] =
            Integer.parseInt(statsScreen!!.stats_kill_guardian.getText().toString())
        character.killCount[droid] =
            Integer.parseInt(statsScreen!!.stats_kill_droid.getText().toString())
        character.killCount[scum] =
            Integer.parseInt(statsScreen!!.stats_kill_scum.getText().toString())
        character.killCount[trooper] =
            Integer.parseInt(statsScreen!!.stats_kill_trooper.getText().toString())



        character.assistCount[villain] =
            Integer.parseInt(statsScreen!!.stats_assist_villain.getText().toString())
        character.assistCount[vehicle] =
            Integer.parseInt(statsScreen!!.stats_assist_vehicle.getText().toString())
        character.assistCount[creature] =
            Integer.parseInt(statsScreen!!.stats_assist_creature.getText().toString())
        character.assistCount[leader] =
            Integer.parseInt(statsScreen!!.stats_assist_leader.getText().toString())
        character.assistCount[guard] =
            Integer.parseInt(statsScreen!!.stats_assist_guardian.getText().toString())
        character.assistCount[droid] =
            Integer.parseInt(statsScreen!!.stats_assist_droid.getText().toString())
        character.assistCount[scum] =
            Integer.parseInt(statsScreen!!.stats_assist_scum.getText().toString())
        character.assistCount[trooper] =
            Integer.parseInt(statsScreen!!.stats_assist_trooper.getText().toString())

        character.timesWounded =
            Integer.parseInt(statsScreen!!.stats_wounded.getText().toString())
        setTotalKills()
    }

    fun onTapStat(v: TextView) {
        statsScreen!!.edit_stat.visibility = View.VISIBLE
        currentStatEditing = v
        statsScreen!!.stat_text.setText(v.text.toString())

    }

    fun setTotalKills() {
        var totalKills = 0
        var totalAssists = 0
        for (i in 0..character.killCount.size - 1) {
            totalKills += character.killCount[i]
            totalAssists += character.assistCount[i]
        }
        statsScreen!!.stats_kill_total.setText("" + totalKills)
        statsScreen!!.stats_assist_total.setText("" + totalAssists)
        if (character.timesWounded > 0) {
            statsScreen!!.stats_kill_death_ratio.setText("" + Math.round(10*totalKills.toFloat()
                    / character.timesWounded).toFloat()/10f)
        } else {
            statsScreen!!.stats_kill_death_ratio.setText("-")
        }
    }

    //endregion
    //************************************************************************************************************
    //region XP Screen
    //************************************************************************************************************
    var xpCardImages = arrayListOf<ImageView>()

    fun initXPScreen() {
        xpCardImages.add(xpSelectScreen!!.XPCard1)
        xpCardImages.add(xpSelectScreen!!.XPCard2)
        xpCardImages.add(xpSelectScreen!!.XPCard3)
        xpCardImages.add(xpSelectScreen!!.XPCard4)
        xpCardImages.add(xpSelectScreen!!.XPCard5)
        xpCardImages.add(xpSelectScreen!!.XPCard6)
        xpCardImages.add(xpSelectScreen!!.XPCard7)
        xpCardImages.add(xpSelectScreen!!.XPCard8)
        xpCardImages.add(xpSelectScreen!!.XPCard9)

        for (i in 0..8) {
            if (character.xpCardsEquipped[i]) {
                xpCardImages[i].alpha = 1f
            } else {
                xpCardImages[i].alpha = 0.5f
            }
            if(character.xpCardImages.size > i) {
                xpCardImages[i].setImageBitmap(character.xpCardImages[i])
            }
            xpCardImages[i].setOnLongClickListener {
                onShowXPCard(xpCardImages[i])
                true
            }


        }
        var xpLeft = character.totalXP - character.xpSpent
        xpSelectScreen!!.xp_text.setText("XP: " + xpLeft)
    }

    fun onShowXPCard(view: ImageView) {
        var image = ((view).drawable as BitmapDrawable).bitmap
        showCardDialog!!.card_image.setImageBitmap(image)
        showCardDialog!!.show()
    }

    fun onXPCard(view: View) {
        var xpLeft = character.totalXP - character.xpSpent;
        println(view.toString() + " XP")
        var cardNo = Integer.parseInt(view.tag as String)
        if (character.xpCardsEquipped[cardNo]) {
            Sounds.selectSound()
            character.unequipXP(cardNo)

            xpCardImages[cardNo].animate().alpha(0.5f).duration = 50

        } else if (character.xpScores[cardNo] <= xpLeft) {
            character.equipXP(cardNo,this)
            if(character.xpCardsEquipped[cardNo]) {
                xpCardImages[cardNo].animate().alpha(1f).duration = 50
                Sounds.selectSound()
            }
        }
        else{
            showNotEnoughXP()
        }
        xpLeft = character.totalXP - character.xpSpent
        xpSelectScreen!!.xp_text.setText("XP: " + xpLeft)
        character.rewardObtained = character.xpCardsEquipped[8]

        if (character.currentImage != null) {
            //character.currentImage!!.recycle()
        }

        updateImages()
        updateStats()

    }

    private fun showNotEnoughXP() {
        Sounds.negativeSound()
        val toast = Toast(this)
        toast!!.duration = Toast.LENGTH_SHORT
        val view = this.layoutInflater.inflate(
            R.layout.toast_no_actions_left,
            null,
            false
        )
        view.toast_text.setText("NOT ENOUGH XP")


        toast!!.view = view
        toast!!.setGravity(Gravity.CENTER, 0, 0)
        toast!!.show()
    }

    fun addXP(view: View) {
        Sounds.selectSound()
        character.totalXP++
        var xpLeft = character.totalXP - character.xpSpent;
        xpSelectScreen!!.xp_text.setText("XP: " + xpLeft)
    }

    fun minusXP(view: View) {
        var xpLeft = character.totalXP - character.xpSpent;
        if (xpLeft > 0) {
            Sounds.selectSound()
            character.totalXP--
        }
        else{
            Sounds.negativeSound()
        }
        xpLeft = character.totalXP - character.xpSpent;
        xpSelectScreen!!.xp_text.setText("XP: " + xpLeft)
    }

    //endregion
    //************************************************************************************************************
    //region Saving
    //************************************************************************************************************

    fun quickSave() {
        //if(secondsSinceLastSave > 3) {
        //val character = MainActivity.selectedCharacter
        startSavingAnimation()
        val saveWorkRequestBuilder = OneTimeWorkRequest.Builder(saveWorker::class.java)
        val data = Data.Builder()

        saveWorkRequestBuilder.setInputData(data.build())
        saveWorkRequestBuilder.addTag("save")

        val saveWorkRequest =saveWorkRequestBuilder.build()

        WorkManager.getInstance(this).enqueue(saveWorkRequest)
        WorkManager.getInstance(this)
            .getWorkInfosByTagLiveData("save")
            .observe(this, Observer<List<WorkInfo>> {
                    workStatusList ->
                val currentWorkStatus = workStatusList ?.getOrNull(0)
                if (currentWorkStatus ?.state ?.isFinished == true)
                {
                    WorkManager.getInstance(this)
                        .getWorkInfosByTagLiveData("save").removeObservers(this)
                    stopSavingAnimation()
                }
            })

    }
    lateinit var savingAnimation:ObjectAnimator
    fun startSavingAnimation(){
        saving_icon.alpha=0f

        saving_icon.animate().alpha(1f)

        savingAnimation = ObjectAnimator.ofFloat(saving_icon, "scaleX", 0f,0.8f, 1f,1f,1f,0.8f,
            0f)

        savingAnimation.repeatCount = -1
        savingAnimation.duration = 500
        savingAnimation.start()
    }

    fun stopSavingAnimation(){
        val handler = Handler()
        handler.postDelayed(Runnable {
            saving_icon.animate().alpha(0f)
            handler.postDelayed(Runnable {
                savingAnimation.cancel()
            }, 1000)

        }, 1000)


    }


    fun firstManualSave() {

        println("FIRST MANUAL SAVE character " + character)
        character.file_name = "" + saveDialog!!.save_name.text.toString()
        quickSave()
    }

    override fun onBackPressed() {
        quickSave()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    override fun onStop() {
        quickSave()
        println("on stop save")
        super.onStop()
    }

    fun convertItemIDToString(itemIds: ArrayList<Int>): String {
        var itemString = ""
        for (i in 0..itemIds.size - 1) {
            itemString += "," + itemIds[i]
        }
        return itemString
    }
//endregion
}
*/