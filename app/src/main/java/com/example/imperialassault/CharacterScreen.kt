package com.example.imperialassault

import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Color.TRANSPARENT
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.DisplayMetrics
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ImageViewCompat
import kotlinx.android.synthetic.main.activity_character_view.*
import kotlinx.android.synthetic.main.dialog_assist.*
import kotlinx.android.synthetic.main.dialog_background.*
import kotlinx.android.synthetic.main.dialog_bio.*
import kotlinx.android.synthetic.main.dialog_conditions.*
import kotlinx.android.synthetic.main.dialog_options.*
import kotlinx.android.synthetic.main.dialog_rest.*
import kotlinx.android.synthetic.main.dialog_save.*
import kotlinx.android.synthetic.main.dialog_show_card.*
import kotlinx.android.synthetic.main.screen_settings.*
import kotlinx.android.synthetic.main.screen_stats.*
import kotlinx.android.synthetic.main.screen_xp_select.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.InputStream
import kotlin.random.Random

var height = 0f
var width = 0f

class CharacterScreen : AppCompatActivity(){
    var character:Character = Character();
    var animateConditions = true
    var animateDamage = true
    var autoImageChange = true
    var actionUsage = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAnimation()
        setContentView(R.layout.activity_character_view)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels.toFloat()
        width = displayMetrics.widthPixels.toFloat()

        initDialogs()
        initScreen()
        initAnimations()
        initKillTracker()
        startSaveTimer()
    }
    fun setAnimation(){
        /*
        if(Build.VERSION.SDK_INT>20) {
            val fade = android.transition.Fade()
            fade.setDuration(200);
            getWindow().setExitTransition(fade);
            getWindow().setEnterTransition(fade);
        }*/
    }

    var loadAnimated = false
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        updateStats()
        updateImages()

        if(hasFocus&&!loadAnimated ){
            top_panel.animate().alpha(1f)
            val animTop = ObjectAnimator.ofFloat(top_panel,"translationY",-top_panel.height
                .toFloat(),0f)

            animTop .duration=(500).toLong()
            animTop .start()
            bottom_panel.animate().alpha(1f)
            val animBottom = ObjectAnimator.ofFloat(bottom_panel,"translationY",bottom_panel.height
                .toFloat(),0f)
            //animBottom.interpolator = DecelerateInterpolator()
            animBottom.duration=(500).toLong()
            animBottom.start()

            left_buttons.animate().alpha(1f)
            val animButtons = ObjectAnimator.ofFloat(left_buttons,"translationX",-left_buttons.width
                .toFloat(),0f)
            //animBottom.interpolator = DecelerateInterpolator()
            animButtons.duration=(500).toLong()
            animButtons.start()

            right_buttons.animate().alpha(1f)
            val animRightButtons = ObjectAnimator.ofFloat(right_buttons,"translationX",right_buttons
                .width
                .toFloat(),0f)
            //animBottom.interpolator = DecelerateInterpolator()
            animRightButtons.duration=(500).toLong()
            animRightButtons.start()

            character_images.animate().alpha(1f)
            val animChar= ObjectAnimator.ofFloat(character_images,"translationX",-character_images.width
                .toFloat(),-character_images.width.toFloat(),0f)
            animChar.interpolator = DecelerateInterpolator()
            animChar.duration=(800).toLong()
            animChar.start()

            companion_layer.animate().alpha(1f)
            val animcompanion= ObjectAnimator.ofFloat( companion_layer,"translationX",
                -character_images.width*1.2f
                .toFloat(),-character_images.width.toFloat()*1.2f,0f)
            animcompanion.interpolator = DecelerateInterpolator()
            animcompanion.duration=(800*1.2f).toLong()
            animcompanion.start()

            loadAnimated = true
        }
    }

    //************************************************************************************************************
    //region Main Screen
    //************************************************************************************************************
    var isWounded = false

    private fun initScreen(){
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
            }
            MainActivity.selectedCharacter = character


        } else {
            character = MainActivity.selectedCharacter!!
        }
        if(character.portraitImage==null) {
            character.loadPortraitImage(this)
        }
        name.setText("" + character.name);


        when (character.defence_dice) {
            "white" -> defence.setImageDrawable(resources.getDrawable(R.drawable.dice))
            "black" -> defence.setImageDrawable(resources.getDrawable(R.drawable.dice_black))
            "" -> defence.visibility = View.INVISIBLE

        }
        setDiceColor(strength1, character.strength[0]);
        setDiceColor(strength2, character.strength[1]);
        setDiceColor(strength3, character.strength[2]);

        setDiceColor(insight1, character.insight[0]);
        setDiceColor(insight2, character.insight[1]);
        setDiceColor(insight3, character.insight[2]);

        setDiceColor(tech1, character.tech[0]);
        setDiceColor(tech2, character.tech[1]);
        setDiceColor(tech3, character.tech[2]);

        updateImages()
        updateStats()
        initDamageAndStrain()
        updateStats()
        initConditions()
        updateConditionIcons()


        camouflage.setImageBitmap(getBitmap(this, "backgrounds/camo_interior.png"))

        if(character.name_short == "jarrod"){
            companion_button.visibility = View.VISIBLE
            companion_layer.visibility = View.VISIBLE
            companion_button.isClickable = true
        }
        else{
            companion_button.visibility = View.INVISIBLE
            companion_layer.visibility = View.GONE
            companion_button.isClickable = false
        }
        background_image.setImageBitmap(character.getBackgroundImage(this))
    }
    private fun setDiceColor(dice: ImageView, color: Char) {
        dice.visibility = ImageView.VISIBLE
        when(color){
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
        var bitmap:Bitmap? = null
        val options = BitmapFactory.Options()
        for(i in 1..32) {
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
    private fun updateImages(){
        if(autoImageChange) {
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

            if (character.name_short != "jarrod") {
                character_image.setLayer1Bitmap(character.layer1)
            } else {
                /*
            if (conditionsActive[hidden] && animateConditions) {
                companion_layer.visibility = View.INVISIBLE

            } else{*/
                companion_layer.visibility = View.VISIBLE
                companion_layer.setImageBitmap(character.layer1);
                println("DROIDDDDDDDDDDDDDDD")
                //}
            }
            //quickSave()
        }
    }
    fun onShowCompanionCard(view: View) {
        if(character.companionCard != null) {
            showCardDialog!!.card_image.setImageBitmap(character.companionCard)
            showCardDialog!!.show()
        }
    }
    private fun updateStats(){
        character.health= character.health_default
        character.endurance = character.endurance_default
        character.speed = character.speed_default

        for(i in 0..character.xpCardsEquipped.size-1){
            if(character.xpCardsEquipped[i]){
                if(character.xpHealths[i]!=0) {
                    character.health += character.xpHealths[i]
                }
                if(character.xpEndurances[i]!=0) {
                    character.endurance += character.xpEndurances[i]
                }
                if(character.xpSpeeds[i]!=0) {
                    character.speed += character.xpSpeeds[i]
                }
            }
        }

        for(i in 0..character.accessories.size-1){
            character.health += Items.accArray!![character.accessories[i]].health
            character.endurance += Items.accArray!![character.accessories[i]].endurance
        }
        for(i in 0..character.armor.size-1){
            character.health += Items.armorArray!![character.armor[i]].health
            //character.endurance += Items.armorArray!![character.armor[i]].endurance
        }
        for(i in 0..character.rewards.size-1){
            character.health += Items.rewardsArray!![character.rewards[i]].health
            //character.endurance += Items.armorArray!![character.armor[i]].endurance
        }

        if(isWounded){
            character.endurance--
            character.speed--
        }

        setStatColor(health, character.health, character.health_default)
        setStatColor(endurance, character.endurance, character.endurance_default)
        setStatColor(speed, character.speed, character.speed_default)

        health.setText("" + character.health);
        endurance.setText("" + character.endurance);
        speed.setText("" + character.speed);

        //TODO
        quickSave()
    }

    private fun setStatColor(stat:TextView, current:Int, default:Int){
        if(current > default){
            stat.setShadowLayer(5f, 0f, 0f, resources.getColor(R.color.dice_green))
        }
        else if(current < default){
            stat.setShadowLayer(5f, 0f, 0f, resources.getColor(R.color.stat_orange))
        }
        else{
            stat.setShadowLayer(5f, 0f, 0f, Color.BLACK)
        }
    }

    fun onHide(view: View){
        view.visibility = View.INVISIBLE
    }

    //endregion
    //************************************************************************************************************
    //region Damage and Strain
    //************************************************************************************************************

    private fun getNumber(number: Int):Drawable{
        var numberImage = R.drawable.number_0
        when(number){
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
        }
        return resources.getDrawable(numberImage)
    }
    fun onAddStrain(view: View) {
        if(character.strain < character.endurance) {
            character.strain++
            character.strainTaken++

            if(minus_strain.alpha==0f){
                minus_strain.animate().alpha(1f)
            }

            //add_strain.setText("" + character.strain)
            add_strain.setImageDrawable(getNumber(character.strain))
        }
        else{
            addDamage()

        }
        playRestAnim()

    }
    fun onMinusStrain(view: View) {
        if(character.strain >0) {
            character.strain--
            //add_strain.setText("" + character.strain)
            add_strain.setImageDrawable(getNumber(character.strain))
        }
        if(character.strain==0){
            if(minus_strain.alpha>0f){
                minus_strain.animate().alpha(0f)
            }
        }

    }
    fun onAddDamage(view: View) {
        if(addDamage()) {
            playDamageAnim()

        }
    }
    private fun addDamage():Boolean{
        if(character.damage < character.health*2) {
            character.damage++
            character.damageTaken++

            if(minus_damage.alpha==0f){
                minus_damage.animate().alpha(1f)
            }

            if(character.damage < character.health) {

                //add_damage.setText("" + character.damage)
                add_damage.setImageDrawable(getNumber(character.damage))
            }
            else if(character.damage < character.health*2){

                character.wounded = character.damage-character.health
                //add_damage.setText("" + character.wounded)
                add_damage.setImageDrawable(getNumber(character.wounded))
                if(!isWounded) {
                    wounded.animate().alpha(1f)

                    setDiceColor(strength1, character.strengthWounded[0]);
                    setDiceColor(strength2, character.strengthWounded[1]);
                    setDiceColor(strength3, character.strengthWounded[2]);

                    setDiceColor(insight1, character.insightWounded[0]);
                    setDiceColor(insight2, character.insightWounded[1]);
                    setDiceColor(insight3, character.insightWounded[2]);

                    setDiceColor(tech1, character.techWounded[0]);
                    setDiceColor(tech2, character.techWounded[1]);
                    setDiceColor(tech3, character.techWounded[2]);


                    character.timesWounded++
                    isWounded = true
                    updateStats()
                }

            }
            else{
                character.withdrawn = true
                character.timesWithdrawn++
                //add_damage.setText("" + character.health)
                add_damage.setImageDrawable(getNumber(character.health))
                val slide = ObjectAnimator.ofFloat(
                    character_images, "translationY", 0f, 0f,
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
        if(character.damage >0) {

            character.damage--
            character.withdrawn = false
            if(character.damage < character.health) {
                //add_damage.setText("" + character.damage)
                add_damage.setImageDrawable(getNumber(character.damage))
                if(isWounded) {
                    character.wounded = 0
                    wounded.animate().alpha(0f)

                    setDiceColor(strength1, character.strength[0]);
                    setDiceColor(strength2, character.strength[1]);
                    setDiceColor(strength3, character.strength[2]);

                    setDiceColor(insight1, character.insight[0]);
                    setDiceColor(insight2, character.insight[1]);
                    setDiceColor(insight3, character.insight[2]);

                    setDiceColor(tech1, character.tech[0]);
                    setDiceColor(tech2, character.tech[1]);
                    setDiceColor(tech3, character.tech[2]);

                    isWounded = false
                    updateStats()
                }
            }
            else if(character.damage < character.health*2){
                character.wounded = character.damage-character.health
                //add_damage.setText("" + character.wounded)
                add_damage.setImageDrawable(getNumber(character.wounded))
                val slide = ObjectAnimator.ofFloat(character_images, "translationY", 0f)
                slide.setDuration(500)
                slide.start()
                quickSave()
            }

        }
        if(character.damage==0){
            if(minus_damage.alpha>0f){
                minus_damage.animate().alpha(0f)
            }
        }
    }
    fun onUnwound(view: View) {
        playRestAnim()
        character.damage = 0
        character.wounded = 0
        //add_damage.setText("" + character.damage)
        add_damage.setImageDrawable(getNumber(character.damage))
        wounded.animate().alpha(0f)

        setDiceColor(strength1, character.strength[0]);
        setDiceColor(strength2, character.strength[1]);
        setDiceColor(strength3, character.strength[2]);

        setDiceColor(insight1, character.insight[0]);
        setDiceColor(insight2, character.insight[1]);
        setDiceColor(insight3, character.insight[2]);

        setDiceColor(tech1, character.tech[0]);
        setDiceColor(tech2, character.tech[1]);
        setDiceColor(tech3, character.tech[2]);

        isWounded = false
        updateStats()
        unwoundDialog!!.dismiss()
    }
    private fun initDamageAndStrain() {
        if(character.damage>0) {
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

                setDiceColor(strength1, character.strengthWounded[0]);
                setDiceColor(strength2, character.strengthWounded[1]);
                setDiceColor(strength3, character.strengthWounded[2]);

                setDiceColor(insight1, character.insightWounded[0]);
                setDiceColor(insight2, character.insightWounded[1]);
                setDiceColor(insight3, character.insightWounded[2]);

                setDiceColor(tech1, character.techWounded[0]);
                setDiceColor(tech2, character.techWounded[1]);
                setDiceColor(tech3, character.techWounded[2]);

                isWounded = true
                //println()
                //println("withdrawn" + character.withdrawn)
                //println()
                if (character.withdrawn){
                    //println("SLIDE DOWN")
                    wounded.animate().translationY(character_image.height.toFloat())
                }

            }
        }

        if(character.strain>0) {
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
            if (actionsLeft > 0 || !actionUsage) {
                restDialog!!.show()
            } else {
                showNoActionsLeftToast()
            }
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
        if(!activated){
            /*
            val flipUnactive = ObjectAnimator.ofFloat(unactive, "scaleX", 1f, 0f, 0f, 0f)
            flipUnactive.setDuration(300)
            flipUnactive.start()

            val flipActive = ObjectAnimator.ofFloat(active, "scaleX", 0f, 0f, 0f, 1f)
            flipActive.setDuration(300)
            flipActive.start()

             */
            unactive.animate().alpha(0f).duration = 100

            if(actionUsage) {
                turnOnActionButtons()
                actionsLeft = 2;
            }
            activated = true

            character.activated++
        }
        else{
            onEndActivation(view)
        }
    }

    fun turnOnActionButtons(){

        action1.visibility = View.VISIBLE
        action_button1.visibility = View.VISIBLE
        action_button1.animate().alpha(1f)
        action2.visibility = View.VISIBLE
        action_button2.visibility = View.VISIBLE
        action_button2.animate().alpha(1f)

        /*
        action_button1.visibility = View.VISIBLE
        val flipAction1 = ObjectAnimator.ofFloat(action_button1, "scaleY", 0f, 1f)
        flipAction1.setDuration(300)
        flipAction1.start()

        action_button2.visibility = View.VISIBLE
        val flipAction2 = ObjectAnimator.ofFloat(action_button2, "scaleY", 0f, 1f)
        flipAction2.setDuration(300)
        flipAction2.start()
        */

    }

    fun onEndActivation(view: View){

        endActivationDialog!!.dismiss()
        removeCondition(weakened)
        /*
        val flipUnactive = ObjectAnimator.ofFloat(unactive, "scaleX", 0f, 0f, 0f, 1f)
        flipUnactive.setDuration(300)
        flipUnactive.start()

        val flipActive = ObjectAnimator.ofFloat(active, "scaleX", 1f, 0f, 0f, 0f)
        flipActive.setDuration(300)
        flipActive.start()
        */
        unactive.animate().alpha(1f).duration = 100

        if(actionUsage) {
            turnOffActionButtons()
        }
        activated = false

    }

    fun turnOffActionButtons(){

        action_button1.animate().alpha(0f)
        action_button1.visibility = View.GONE

        action_button2.animate().alpha(0f)
        action_button2.visibility = View.GONE

/*
        val flipAction1 = ObjectAnimator.ofFloat(action_button1, "scaleY", 1f, 0f)
        flipAction1.setDuration(300)
        flipAction1.start()


        val flipAction2 = ObjectAnimator.ofFloat(action_button2, "scaleY", 1f, 0f)
        flipAction2.setDuration(300)
        flipAction2.start()
        */

    }

    fun onEndActivationNo(view: View){
        endActivationDialog!!.dismiss()
    }
    fun onAction(view: View) {

        action_menu.visibility = View.INVISIBLE
        action_menu.alpha = 0f

        if(actionsLeft>0) {
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
    fun actionCompleted(){
        if(actionUsage) {
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
        if(actionsLeft>0 ) {
            if(!conditionsActive[stunned]) {
                removeCondition(focused)
                removeCondition(hidden)
                actionCompleted()
                onAction(action_complete)
                character.attacksMade++
            }
        }
        else{
            showNoActionsLeftToast()
        }
    }
    fun onMove(view: View) {
        if(actionsLeft>0 ) {
            if(!conditionsActive[stunned]) {
                actionCompleted()
                onAction(action_complete)
                character.movesTaken++
            }
        }
        else{
            showNoActionsLeftToast()
        }
    }
    fun onSpecial(view: View) {
        if(actionsLeft>0) {
            if(!conditionsActive[stunned]) {
                actionCompleted()
                onAction(action_complete)
                character.specialActions++
            }
        }
        else{
            showNoActionsLeftToast()
        }
    }
    fun onInteract(view: View) {
        if(actionsLeft>0) {
            actionCompleted()
            onAction(action_complete)
            character.interactsUsed++
        }
        else{
            showNoActionsLeftToast()
        }
    }
    fun onRest(view: View) {
        if(actionsLeft>0 || !actionUsage) {
            character.strain -= character.endurance

            if (character.strain < 0) {
                for (i in 1..-character.strain) {
                    onMinusDamage(view)
                }
                character.strain = 0;
            }
            //add_strain.setText("" + character.strain)
            add_strain.setImageDrawable(getNumber(character.strain))
            playRestAnim()
            character.timesRested++
            restDialog!!.dismiss()

            actionCompleted()
        }
        else{
            showNoActionsLeftToast()
        }
    }
    fun onRemoveStun(view: View) {
        if(actionsLeft>0 || !actionUsage) {
            removeCondition(stunned)
            onAction(action_complete)
            actionCompleted()
        }
        else{
            showNoActionsLeftToast()
        }
    }
    fun onRemoveBleeding(view: View) {
        if(actionsLeft>0 || !actionUsage) {
            removeCondition(bleeding)
            onAction(action_complete)
            actionCompleted()
        }
        else{
            showNoActionsLeftToast()
        }
    }
    private fun showNoActionsLeftToast(){
        val noActionsLeftToast=Toast(this)
        noActionsLeftToast!!.duration = Toast.LENGTH_SHORT
        noActionsLeftToast!!.view = layoutInflater.inflate(
            R.layout.toast_no_actions_left,
            character_view_group,
            false
        )
        noActionsLeftToast!!.setGravity(Gravity.CENTER, 0, 0)
        noActionsLeftToast!!. show()
    }

    //endregion
    //************************************************************************************************************
    //region Show Cards
    //************************************************************************************************************

    fun onShowFocusedCard(view: View){
        showCardDialog!!.card_image.setImageDrawable(resources.getDrawable(R.drawable.card_focused))
        showCardDialog!!.show()
    }
    fun onShowStunnedCard(view: View){
        showCardDialog!!.card_image.setImageDrawable(resources.getDrawable(R.drawable.card_stunned))
        showCardDialog!!.show()
    }
    fun onShowWeakenedCard(view: View){
        showCardDialog!!.card_image.setImageDrawable(resources.getDrawable(R.drawable.card_weakened))
        showCardDialog!!.show()
    }
    fun onShowBleedingCard(view: View){
        showCardDialog!!.card_image.setImageDrawable(resources.getDrawable(R.drawable.card_bleeding))
        showCardDialog!!.show()
    }
    fun onShowHiddenCard(view: View){
        showCardDialog!!.card_image.setImageDrawable(resources.getDrawable(R.drawable.card_hidden))
        showCardDialog!!.show()
    }
    fun onShowCard(view: View){
        when(view.getTag()){
            hidden -> onShowHiddenCard(view)
            focused -> onShowFocusedCard(view)
            stunned -> onShowStunnedCard(view)
            bleeding -> onShowBleedingCard(view)
            weakened -> onShowWeakenedCard(view)
        }
    }

    var sideMenuState = 0

//endregion
    //************************************************************************************************************
    //region Options Menu
    //************************************************************************************************************

    fun onShowOptions(view: View) {
        optionsDialog!!.show()
    }
    override fun onUserInteraction() {
        super.onUserInteraction()
        //println("save")
    }
    fun onBiography(view: View) {
        optionsDialog!!.dismiss()
        bioDialog!!.bio_title.setText(character.bio_title)
        bioDialog!!.bio_quote.setText(character.bio_quote)
        bioDialog!!.bio_text.setText(character.bio_text)
        bioDialog!!.show()
    }
    fun onPower(view: View) {
        optionsDialog!!.dismiss()
        if(!isWounded) {
            showCardDialog!!.card_image.setImageBitmap(character.power)
        }
        else{
            showCardDialog!!.card_image.setImageBitmap(character.power_wounded)
        }

        showCardDialog!!.show()
    }
    fun onSave(view: View) {
        if(character.file_name.equals("autosave")) {
            saveDialog!!.show()
        }
        else{
            quickSave()
        }
        optionsDialog!!.dismiss()

    }
    fun onBackground(view: View) {
        optionsDialog!!.dismiss()
        backgroundDialog!!.show()
    }
    fun onSettings(view: View) {
        optionsDialog!!.dismiss()
        settingsScreen!!.show()
    }
    fun onStatistics(view: View) {
        optionsDialog!!.dismiss()
        initStatsScreen()
        statsScreen!!.show()
    }
    fun onCredits(view: View) {
        optionsDialog!!.dismiss()
        developersCreditsScreen!!.show()
    }
    fun onDevCredits(view: View) {
        optionsDialog!!.dismiss()
        developersCreditsScreen!!.show()
    }

    //Backgrounds
    fun onBackgroundSnow(view: View) {
        character.background = "snow"
        background_image.setImageBitmap(getBitmap(this, "backgrounds/background_snow.jpg"))
        camouflage.setImageBitmap(getBitmap(this, "backgrounds/camo_snow.png"))
    }
    fun onBackgroundJungle(view: View) {
        character.background = "jungle"
        background_image.setImageBitmap(getBitmap(this, "backgrounds/background_jungle.jpg"))
        camouflage.setImageBitmap(getBitmap(this, "backgrounds/camo_jungle.png"))
    }
    fun onBackgroundDesert(view: View) {
        character.background = "desert"
        background_image.setImageBitmap(getBitmap(this, "backgrounds/background_desert.jpg"))
        camouflage.setImageBitmap(getBitmap(this, "backgrounds/camo_desert.png"))
    }
    fun onBackgroundInterior(view: View) {
        character.background = "interior"
        background_image.setImageBitmap(getBitmap(this, "backgrounds/background_interior.jpg"))
        camouflage.setImageBitmap(getBitmap(this, "backgrounds/camo_interior.png"))
    }

    //endregion
    //************************************************************************************************************
    //region Side Navigation
    //************************************************************************************************************

    fun onExtendDown(view: View) {
        val lp = kill_tracker_bar.layoutParams
        lp.height = menu_bar.height+22
        kill_tracker_bar.layoutParams = lp
        var h = menu_bar.height.toFloat()+88+66
        if(sideMenuState>-1){
            sideMenuState--
            kill_tracker_bar.animate().translationYBy(h)
            menu_bar.animate().translationYBy(h)
        }
        when(sideMenuState){
            -1 -> {
                extend_down_button.animate().alpha(0f);
                extend_up_button.animate().alpha(1f)
                kill_tracker_bar.animate().translationY(h)
                menu_bar.animate().translationY(h)
            }
            0 -> {
                extend_down_button.animate().alpha(1f)
                extend_up_button.animate().alpha(1f)
                kill_tracker_bar.animate().translationY(0f)
                menu_bar.animate().translationY(0f)
            }
            1 -> {
                extend_down_button.animate().alpha(1f)
                extend_up_button.animate().alpha(0f)
                kill_tracker_bar.animate().translationY(-h)
                menu_bar.animate().translationY(-h)
            }
        }


    }
    fun onExtendUp(view: View) {
        val lp = kill_tracker_bar.layoutParams
        lp.height = menu_bar.height+22
        kill_tracker_bar.layoutParams = lp
        var h = menu_bar.height.toFloat()+88+66
        if(sideMenuState<1){
            sideMenuState++
            kill_tracker_bar.animate().translationYBy(-h)
            menu_bar.animate().translationYBy(-h)
        }
        when(sideMenuState){
            -1 -> {
                extend_down_button.animate().alpha(0f);
                extend_up_button.animate().alpha(1f)
                kill_tracker_bar.animate().translationY(h)
                menu_bar.animate().translationY(h)
            }
            0 -> {
                extend_down_button.animate().alpha(1f)
                extend_up_button.animate().alpha(1f)
                kill_tracker_bar.animate().translationY(0f)
                menu_bar.animate().translationY(0f)
            }
            1 -> {
                extend_down_button.animate().alpha(1f)
                extend_up_button.animate().alpha(0f)
                kill_tracker_bar.animate().translationY(-h)
                menu_bar.animate().translationY(-h)
            }
        }

    }
    fun onReward(view: View) {
        val intent = Intent(this, RewardsScreen::class.java)
        //intent.putExtra("Load",false)
        startActivity(intent);
    }
    fun onAccessory(view: View) {
        val intent = Intent(this, ItemSelectScreen::class.java)
        intent.putExtra("tab", "accessory")
        //intent.putExtra("Load",false)
        startActivity(intent);
    }
    fun onArmour(view: View) {
        val intent = Intent(this, ItemSelectScreen::class.java)
        intent.putExtra("tab", "armour")
        //intent.putExtra("Load",false)
        startActivity(intent);
    }
    fun onMelee(view: View) {
        val intent = Intent(this, ItemSelectScreen::class.java)
        intent.putExtra("tab", "melee")
        //intent.putExtra("Load",false)
        startActivity(intent);
    }
    fun onRange(view: View) {
        val intent = Intent(this, ItemSelectScreen::class.java)
        intent.putExtra("tab", "range")
        //intent.putExtra("Load",false)
        startActivity(intent);
    }
    fun onXPScreen(view: View) {
        initXPScreen()
        xpSelectScreen!!.show()

    }

    //endregion
    //************************************************************************************************************
    //region Kill Tracker
    //************************************************************************************************************

    //TODO add to stats
    var killCounts = ArrayList<TextView>()
    val villain =0
    val leader =1
    val vehicle =2
    val creature =3
    val guard =4
    val droid =5
    val scum =6
    val trooper =7

    fun villainKillDown(view: View) {killCountDown(villain)}
    fun villainKillUp(view: View) {killCountUp(villain)}
    fun leaderKillDown(view: View) {killCountDown(leader)}
    fun leaderKillUp(view: View) {killCountUp(leader)}
    fun vehicleKillDown(view: View) {killCountDown(vehicle)}
    fun vehicleKillUp(view: View) {killCountUp(vehicle)}
    fun creatureKillDown(view: View) {killCountDown(creature)}
    fun creatureKillUp(view: View) {killCountUp(creature)}
    fun guardKillDown(view: View) {killCountDown(guard)}
    fun guardKillUp(view: View) {killCountUp(guard)}
    fun droidKillDown(view: View) {killCountDown(droid)}
    fun droidKillUp(view: View) {killCountUp(droid)}
    fun scumKillDown(view: View) {killCountDown(scum)}
    fun scumKillUp(view: View) {killCountUp(scum)}
    fun trooperKillDown(view: View) {killCountDown(trooper)}
    fun trooperKillUp(view: View) {killCountUp(trooper)}

    fun killCountUp(type: Int){
        var killCount = Integer.parseInt(killCounts[type].text.toString())
        killCount++
        character.killCount[type]=killCount
        killCounts[type].setText("" + killCount)
    }
    fun killCountDown(type: Int){
        var killCount = Integer.parseInt(killCounts[type].text.toString())
        if(killCount>0) {
            killCount--
            character.killCount[type]=killCount
            killCounts[type].setText("" + killCount)
        }
    }
    fun onAssist(view: View) {
        when(view.tag){
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
    private fun initKillTracker(){

        killCounts.add(villain_count)
        villain_button.setOnLongClickListener {
            assistDialog!!.assist_icon.setImageDrawable(resources.getDrawable(R.drawable.icon_villian))
            assistDialog!!.unwound_button.setTag(villain)
            assistDialog!!.show()
            true
        }
        killCounts.add(leader_count)
        leader_button.setOnLongClickListener {
            assistDialog!!.assist_icon.setImageDrawable(resources.getDrawable(R.drawable.icon_leader))
            assistDialog!!.unwound_button.setTag(leader)
            assistDialog!!.show()
            true
        }
        killCounts.add(vehicle_count)
        vehicle_button.setOnLongClickListener {
            assistDialog!!.assist_icon.setImageDrawable(
                resources.getDrawable(
                    R.drawable
                        .icon_vehicle
                )
            )
            assistDialog!!.unwound_button.setTag(vehicle)
            assistDialog!!.show()
            true
        }
        killCounts.add(creature_count)
        creature_button.setOnLongClickListener {
            assistDialog!!.assist_icon.setImageDrawable(resources.getDrawable(R.drawable.icon_creature))
            assistDialog!!.unwound_button.setTag(creature)
            assistDialog!!.show()
            true
        }
        killCounts.add(guard_count)
        guard_button.setOnLongClickListener {
            assistDialog!!.assist_icon.setImageDrawable(resources.getDrawable(R.drawable.icon_gaurd))
            assistDialog!!.unwound_button.setTag(guard)
            assistDialog!!.show()
            true
        }
        killCounts.add(droid_count)
        droid_button.setOnLongClickListener {
            assistDialog!!.assist_icon.setImageDrawable(resources.getDrawable(R.drawable.icon_droid))
            assistDialog!!.unwound_button.setTag(droid)
            assistDialog!!.show()
            true
        }
        killCounts.add(scum_count)
        scum_button.setOnLongClickListener {
            assistDialog!!.assist_icon.setImageDrawable(resources.getDrawable(R.drawable.icon_scum))
            assistDialog!!.unwound_button.setTag(scum)
            assistDialog!!.show()
            true
        }
        killCounts.add(trooper_count)
        trooper_button.setOnLongClickListener {
            assistDialog!!.assist_icon.setImageDrawable(
                resources.getDrawable(
                    R.drawable
                        .icon_trooper
                )
            )
            assistDialog!!.unwound_button.setTag(trooper)
            assistDialog!!.show()
            true
        }

        for(i in 0..killCounts.size-1){
            killCounts[i].text = ""+character.killCount[i]
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

    private fun initConditions(){
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
        conditionsDialog!!.show()
    }
    fun onWeakened(view: View) {
        conditionsActive[weakened] =!conditionsActive[weakened]
        if(conditionsActive[weakened]) {
            character.timesWeakened++
        }
        else{
            //character.timesWeakened--
        }
        updateConditionIcons()
    }
    fun onBleeding(view: View) {
        conditionsActive[bleeding] =!conditionsActive[bleeding]
        if(conditionsActive[bleeding]) {
            character.timesBleeding++
        }else{
            //character.timesBleeding--
        }
        updateConditionIcons()
    }
    fun onStunned(view: View) {
        conditionsActive[stunned] =!conditionsActive[stunned]
        if(conditionsActive[stunned]) {
            character.timesStunned++
        }
        else{
            //character.timesStunned--
        }
        updateConditionIcons()
    }
    fun onHidden(view: View) {
        conditionsActive[hidden] =!conditionsActive[hidden]
        if(conditionsActive[hidden]) {
            character.timesHidden++

        }
        else{
            //character.timesHidden--

        }
        updateConditionIcons()


    }
    fun onFocused(view: View) {
        conditionsActive[focused] =!conditionsActive[focused]
        if(conditionsActive[focused]) {
            character.timesFocused++
        }
        else{
            //character.timesFocused--
        }
        updateConditionIcons()
    }

    private fun removeCondition(conditionType: Int){

        if(actionsLeft>0 || conditionType==hidden||conditionType==focused||conditionType ==weakened || !actionUsage) {
            conditionsActive[conditionType] = false
            updateConditionIcons()
        }
        else{
            showNoActionsLeftToast()
        }
    }
    private fun updateConditionIcons(){
        //TODO condition save

        character.conditionsActive = conditionsActive
        character_image.animateConditions = animateConditions
        quickSave()

        for(i in 0..conditionViews.size-1){
            conditionViews[i].visibility = View.GONE
        }
        show_all_conditions.visibility = View.GONE


        if(!animateConditions) {
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



        if(!conditionsActive[hidden]) {
            conditionsDialog!!.hidden_select.alpha = 1f
            camouflage.visibility=View.GONE
        }
        else{
            conditionsDialog!!.hidden_select.alpha = 0.5f
            if(animateConditions) {
                camouflage.visibility = View.VISIBLE
            }
            else{
                camouflage.visibility=View.GONE
            }
        }

        if(!conditionsActive[focused]) {
            conditionsDialog!!.focused_select.alpha=1f
            character_image.focused = false
        }
        else{
            conditionsDialog!!.focused_select.alpha = 0.5f
            character_image.focused = true
        }

        if(!conditionsActive[stunned]) {
            conditionsDialog!!.stunned_select.alpha = 1f
            character_image.stunned = false
        }
        else{
            conditionsDialog!!.stunned_select.alpha = 0.5f
            character_image.stunned = true
        }
        if(!conditionsActive[bleeding]) {
            conditionsDialog!!.bleeding_select.alpha = 1f
            character_image.bleeding = false
        }
        else{
            conditionsDialog!!.bleeding_select.alpha = 0.5f
            character_image.bleeding = true
        }
        if(!conditionsActive[weakened]) {
            conditionsDialog!!.weakened_select.alpha = 1f
            character_image.weakened = false
        }
        else{
            conditionsDialog!!.weakened_select.alpha = 0.5f
            character_image.weakened = true
        }

        updateImages()

    }

    //endregion
    //************************************************************************************************************
    //region Animations
    //************************************************************************************************************

    private fun initAnimations(){
        //rest_animation.setBackgroundDrawable(MainActivity.restAnim)
        //rest_animation.visibility = FrameLayout.INVISIBLE
    }
    private fun playDamageAnim(){
        if(animateDamage) {
            val animType = Math.random();
            if (animType < 0.3) {
                damage_animation.setBackgroundDrawable(MainActivity.blastAnim)
                damage_animation.visibility = FrameLayout.VISIBLE
                MainActivity.blastAnim!!.setVisible(true, true)
                MainActivity.blastAnim!!.start()
            } else if (animType < 0.6) {
                damage_animation.setBackgroundDrawable(MainActivity.sliceAnim)
                damage_animation.visibility = FrameLayout.VISIBLE
                MainActivity.sliceAnim!!.setVisible(true, true)
                MainActivity.sliceAnim!!.start()
            } else {
                damage_animation.setBackgroundDrawable(MainActivity.impactAnim)
                damage_animation.visibility = FrameLayout.VISIBLE
                MainActivity.impactAnim!!.setVisible(true, true)
                MainActivity.impactAnim!!.start()
            }
            var hitY = ObjectAnimator.ofFloat(
                character_images, "translationY", 0f, 20f * Random
                    .nextFloat(),
                0f, 20f * Random.nextFloat(), 0f, 20f * Random.nextFloat(), 0f
            )
            hitY .setDuration(300)
            hitY .start()

            var hitX = ObjectAnimator.ofFloat(
                character_images, "translationX", 0f, -10f * Random
                    .nextFloat(),
                0f, -10f * Random.nextFloat(), 0f, -10f * Random.nextFloat(), 0f
            )
            hitX .setDuration(300)
            hitX .start()
        }

    }
    private fun playRestAnim(){
        if(animateDamage) {
            var restAnim = ObjectAnimator.ofFloat(rest_animation, "alpha", 0f, 1f, 0.75f, 0.25f, 0f)
            restAnim.duration = 200
            restAnim.start()
        }
    }

    //endregion
    //************************************************************************************************************
    //region Dialogs and Screens
    //************************************************************************************************************

    var restDialog:Dialog? = null
    var unwoundDialog:Dialog? = null
    var conditionsDialog:Dialog? = null
    var showCardDialog:Dialog? = null
    var endActivationDialog:Dialog? = null
    var assistDialog:Dialog? = null
    var optionsDialog:Dialog? = null
    var bioDialog:Dialog? = null
    var saveDialog:Dialog? = null
    var backgroundDialog:Dialog? = null
    var settingsScreen:Dialog? = null
    var statsScreen:Dialog? = null
    var xpSelectScreen: Dialog?=null
    var developersCreditsScreen:Dialog? = null

    private fun initDialogs(){
        initRestDialog()
        initUnwoundDialog()
        initConditionsDialog()
        initOptionsDialog()
        initSaveDialog()
        initShowCardDialog()
        initEndActivationDialog()
        initAssistDialog()
        initBioDialog()
        initBackgroundDialog()
        initSettingsDialog()
        initStatsScreenDialog()
        initCreditsScreenDialog()
        initXpSelectScreenDialog()
    }
    private fun initRestDialog(){
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
    private fun initUnwoundDialog(){
        unwoundDialog = Dialog(this)
        unwoundDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        unwoundDialog!!.setCancelable(false)
        unwoundDialog!!.setContentView(R.layout.dialog_unwound)
        unwoundDialog!!.setCanceledOnTouchOutside(true)
        unwoundDialog!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        unwoundDialog!!.unwound_button.setOnClickListener {
            onUnwound(unwoundDialog!!.unwound_button)
            //TODO
            //quickSave()
            true
        }
    }
    private fun initConditionsDialog(){
        conditionsDialog = Dialog(this)
        conditionsDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        conditionsDialog!!.setCancelable(false)
        conditionsDialog!!.setContentView(R.layout.dialog_conditions)
        conditionsDialog!!.setCanceledOnTouchOutside(true)
        conditionsDialog!!.stunned_select.setOnLongClickListener {
            onShowStunnedCard(conditionsDialog!!.stunned_select)
            true
        }
        conditionsDialog!!.bleeding_select.setOnLongClickListener {
            onShowBleedingCard(conditionsDialog!!.bleeding_select)
            true
        }
        conditionsDialog!!.weakened_select.setOnLongClickListener {
            onShowWeakenedCard(conditionsDialog!!.weakened_select)
            true
        }
        conditionsDialog!!.focused_select.setOnLongClickListener {
            onShowFocusedCard(conditionsDialog!!.focused_select)
            true
        }
        conditionsDialog!!.hidden_select.setOnLongClickListener {
            onShowHiddenCard(conditionsDialog!!.hidden_select)
            true
        }
        conditionsDialog!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
    }
    private fun initOptionsDialog(){
        optionsDialog = Dialog(this)
        optionsDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        optionsDialog!!.setCancelable(false)
        optionsDialog!!.setContentView(R.layout.dialog_options)
        optionsDialog!!.setCanceledOnTouchOutside(true)
        optionsDialog!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        optionsDialog!!.bioOption.setOnClickListener{
            onBiography(optionsDialog!!.bioOption)
            true
        }
        optionsDialog!!.powerOption.setOnClickListener{
            onPower(optionsDialog!!.powerOption)
            true
        }
        optionsDialog!!.settingsOption.setOnClickListener{
            onSettings(optionsDialog!!.settingsOption)
            true
        }
        optionsDialog!!.saveOption.setOnClickListener{
            onSave(optionsDialog!!.saveOption)
            true
        }
        optionsDialog!!.statsOption.setOnClickListener{
            onStatistics(optionsDialog!!.statsOption)
            true
        }
        optionsDialog!!.backgroundOption.setOnClickListener{
            onBackground(optionsDialog!!.backgroundOption)
            true
        }
    }
    private fun initSaveDialog(){
        saveDialog = Dialog(this)
        saveDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        saveDialog!!.setCancelable(false)
        saveDialog!!.setContentView(R.layout.dialog_save)
        saveDialog!!.setCanceledOnTouchOutside(true)
        saveDialog!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        saveDialog!!.save_button.setOnClickListener {
            if(character.file_name.equals("autosave")) {
                firstManualSave()
            }
            else{
                createNewSave()
            }
            saveDialog!!.dismiss()
            true
        }
        saveDialog!!.cancel_button.setOnClickListener {
            saveDialog!!.dismiss()
            true
        }
    }
    private fun initShowCardDialog(){
        showCardDialog = Dialog(this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
        showCardDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        showCardDialog!!.setContentView(R.layout.dialog_show_card)
        showCardDialog!!.setCancelable(false)
        showCardDialog!!.setCanceledOnTouchOutside(true)
        showCardDialog!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        showCardDialog!!.show_card_dialog.setOnClickListener {
            showCardDialog!!.dismiss()
            true
        }

    }
    private fun initEndActivationDialog(){
        endActivationDialog = Dialog(this)
        endActivationDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        endActivationDialog!!.setCancelable(false)
        endActivationDialog!!.setContentView(R.layout.dialog_end_activation)
        endActivationDialog!!.setCanceledOnTouchOutside(true)
        endActivationDialog!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
    }
    private fun initAssistDialog(){
        assistDialog = Dialog(this)
        assistDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        assistDialog!!.setCancelable(false)
        assistDialog!!.setContentView(R.layout.dialog_assist)
        assistDialog!!.setCanceledOnTouchOutside(true)
        assistDialog!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        assistDialog!!.unwound_button.setOnClickListener {
            onAssist(assistDialog!!.unwound_button)
            //TODO
            quickSave()
            true
        }
    }
    private fun initBioDialog(){
        bioDialog = Dialog(this)
        bioDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bioDialog!!.setCancelable(false)
        bioDialog!!.setContentView(R.layout.dialog_bio)
        bioDialog!!.setCanceledOnTouchOutside(true)
        bioDialog!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        bioDialog!!.bio_layout.setOnClickListener{
            bioDialog!!.dismiss()
            true
        }
    }
    private fun initCreditsScreenDialog(){
        developersCreditsScreen = Dialog(this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
        developersCreditsScreen!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        developersCreditsScreen!!.setCancelable(false)
        developersCreditsScreen!!.setContentView(R.layout.credits_to_us)
        developersCreditsScreen!!.setCanceledOnTouchOutside(true)
        developersCreditsScreen!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
    }
    private fun initSettingsDialog(){
        settingsScreen= Dialog(this)
        settingsScreen!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        settingsScreen!!.setCancelable(false)
        settingsScreen!!.setContentView(R.layout.screen_settings)
        settingsScreen!!.setCanceledOnTouchOutside(true)
        settingsScreen!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))

        settingsScreen!!.toggleDamageAnim!!.isChecked = animateDamage
        settingsScreen!!.toggleDamageAnim.setOnClickListener{
            animateDamage = settingsScreen!!.toggleDamageAnim!!.isChecked
        }

        settingsScreen!!.toggleConditionAnim!!.isChecked = animateConditions
        settingsScreen!!.toggleConditionAnim.setOnClickListener{
            animateConditions = settingsScreen!!.toggleConditionAnim!!.isChecked
            updateConditionIcons()
        }

        settingsScreen!!.toggleAutoImageChange.isChecked = autoImageChange
        settingsScreen!!.toggleAutoImageChange.setOnClickListener{
            autoImageChange = settingsScreen!!.toggleAutoImageChange.isChecked
        }

        settingsScreen!!.toggleActionUsage.isChecked = actionUsage
        settingsScreen!!.toggleActionUsage.setOnClickListener{
            actionUsage = settingsScreen!!.toggleActionUsage.isChecked
            if(actionUsage){
                if(activated) {
                    turnOnActionButtons()
                }
            }
            else{
                turnOffActionButtons()
            }
        }

        settingsScreen!!.creditsButton.setOnClickListener{
            onCredits(settingsScreen!!.creditsButton)
        }
    }
    private fun initBackgroundDialog(){
        backgroundDialog = Dialog(this)
        backgroundDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        backgroundDialog!!.setCancelable(false)
        backgroundDialog!!.setContentView(R.layout.dialog_background)
        backgroundDialog!!.setCanceledOnTouchOutside(true)
        backgroundDialog!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        backgroundDialog!!.desert_background.setOnClickListener{
            onBackgroundDesert(backgroundDialog!!.desert_background)
            quickSave()
        }
        backgroundDialog!!.snow_background.setOnClickListener{
            onBackgroundSnow(backgroundDialog!!.snow_background)
            quickSave()
        }
        backgroundDialog!!.jungle_background.setOnClickListener{
            onBackgroundJungle(backgroundDialog!!.jungle_background)
            quickSave()
        }
        backgroundDialog!!.interior_background.setOnClickListener{
            onBackgroundInterior(backgroundDialog!!.interior_background)
            quickSave()
        }
    }
    private fun initStatsScreenDialog(){
        statsScreen = Dialog(this)
        statsScreen!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        statsScreen!!.setCancelable(false)
        statsScreen!!.setContentView(R.layout.screen_stats)
        statsScreen!!.setCanceledOnTouchOutside(true)
        statsScreen!!.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
    }
    private fun initXpSelectScreenDialog(){
        xpSelectScreen = Dialog(this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
        xpSelectScreen!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        xpSelectScreen!!.setCancelable(false)
        xpSelectScreen!!.setContentView(R.layout.screen_xp_select)
        xpSelectScreen!!.setCanceledOnTouchOutside(true)
    }

    //endregion
    //************************************************************************************************************
    //region Stats Screen
    //************************************************************************************************************
    fun initStatsScreen(){
        statsScreen!!.stats_name.setText("" + character.name)
        statsScreen!!.stats_portrait_image.setImageDrawable(character.portraitImage)
        var level = 5
        if(character.xpSpent <= 1){
            level = 1
        }
        else if(character.xpSpent <= 4){
            level = 2
        }
        else if(character.xpSpent <= 7){
            level = 3
        }
        else if(character.xpSpent <= 10){
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

        if(character.rewardObtained) {
            statsScreen!!.stats_reward_obtained.setText("Yes")
        }
        else{
            statsScreen!!.stats_reward_obtained.setText("No")
        }

        var totalKills = 0
        var totalAssists = 0
        for(i in 0..character.killCount.size-1){
            totalKills += character.killCount[i]
            totalAssists += character.assistCount[i]
        }
        statsScreen!!.stats_kill_total.setText("" + totalKills)
        statsScreen!!.stats_kill_villain.setText("" + character.killCount[villain])
        statsScreen!!.stats_kill_vehicle.setText("" + character.killCount[vehicle])
        statsScreen!!.stats_kill_creature.setText("" + character.killCount[creature])
        statsScreen!!.stats_kill_leader.setText("" + character.killCount[leader])
        statsScreen!!.stats_kill_guardian.setText("" + character.killCount[guard])
        statsScreen!!.stats_kill_droid.setText("" + character.killCount[droid])
        statsScreen!!.stats_kill_scum.setText("" + character.killCount[scum])
        statsScreen!!.stats_kill_trooper.setText("" + character.killCount[trooper])

        statsScreen!!.stats_assist_total.setText("" + totalAssists)
        statsScreen!!.stats_assist_villain.setText("" + character.assistCount[villain])
        statsScreen!!.stats_assist_vehicle.setText("" + character.assistCount[vehicle])
        statsScreen!!.stats_assist_creature.setText("" + character.assistCount[creature])
        statsScreen!!.stats_assist_leader.setText("" + character.assistCount[leader])
        statsScreen!!.stats_assist_guardian.setText("" + character.assistCount[guard])
        statsScreen!!.stats_assist_droid.setText("" + character.assistCount[droid])
        statsScreen!!.stats_assist_scum.setText("" + character.assistCount[scum])
        statsScreen!!.stats_assist_trooper.setText("" + character.assistCount[trooper])

        if(character.timesWounded>0) {
            statsScreen!!.stats_kill_death_ratio.setText("" + totalKills.toFloat() / character.timesWounded)
        }
        else{
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

        for(i in 0.. character.xpCardImages.size-1){
            xpCardImages[i].setImageBitmap(character.xpCardImages[i])
            xpCardImages[i].setOnLongClickListener {
                onShowXPCard(xpCardImages[i])
                true
            }
            if(character.xpCardsEquipped[i]){
                xpCardImages[i].alpha = 1f
            }
            else{
                xpCardImages[i].alpha = 0.5f
            }
            xpCardImages[i].setTag(i)
        }
        var xpLeft = character.totalXP-character.xpSpent
        xpSelectScreen!!.xp_text.setText("XP: " + xpLeft)
    }
    fun onShowXPCard(view: ImageView){
        var image = ((view).drawable as BitmapDrawable).bitmap
        showCardDialog!!.card_image.setImageBitmap(image)
        showCardDialog!!.show()
    }
    fun onXPCard(view: View) {
        var xpLeft = character.totalXP-character.xpSpent;
        var cardNo = view.tag as Int
        if(character.xpCardsEquipped[cardNo]){
            character.xpCardsEquipped[cardNo] = false
            xpCardImages[cardNo].animate().alpha(0.5f).duration = 50
            character.xpSpent -= character.xpScores[cardNo]
        }
        else if( character.xpScores[cardNo] <= xpLeft) {
            character.xpCardsEquipped[cardNo] = true
            xpCardImages[cardNo].animate().alpha(1f).duration = 50
            character.xpSpent+=character.xpScores[cardNo]
        }
        xpLeft = character.totalXP-character.xpSpent
        xpSelectScreen!!.xp_text.setText("XP: " + xpLeft)
        character.rewardObtained = character.xpCardsEquipped[8]

        if(character.currentImage!=null) {
            //character.currentImage!!.recycle()
        }

        updateImages()
        updateStats()

    }
    fun addXP(view: View){
        character.totalXP++
        var xpLeft = character.totalXP-character.xpSpent;
        xpSelectScreen!!.xp_text.setText("XP: " + xpLeft)
    }
    fun minusXP(view: View){
        var xpLeft = character.totalXP-character.xpSpent;
        if(xpLeft>0){
            character.totalXP--
        }
        xpLeft = character.totalXP-character.xpSpent;
        xpSelectScreen!!.xp_text.setText("XP: " + xpLeft)
    }

    //endregion
    //************************************************************************************************************
    //region Saving
    //************************************************************************************************************
    var secondsSinceLastSave = 0
    val autosaveTIme = 600

    fun startSaveTimer(){
        GlobalScope.launch{
            while(secondsSinceLastSave < autosaveTIme){
                delay(1000)
                secondsSinceLastSave++
            }
            println("TIMED AUTOSAVE")
            quickSave()
            startSaveTimer()
        }
    }
    fun quickSave(){
        //if(secondsSinceLastSave > 3) {
            //val character = MainActivity.selectedCharacter


                var saveFile = getCharacterData(character.file_name)
                val database = AppDatabase.getInstance(this)


                GlobalScope.launch {
                        if (character.id != -1) {
                            saveFile.id = character.id
                            database!!.getCharacterDAO().update(saveFile)
                            println("update save")
                        } else {
                            character.id = saveFile.id
                            database!!.getCharacterDAO().insert(saveFile)
                            println("insert save")
                        }
                        println("QUICK SAVE character " + character + " " + character.id)

            secondsSinceLastSave = 0
        }
    }
    fun firstManualSave() {

        println("FIRST MANUAL SAVE character " + character)
        if (character != null) {
            var saveFile = getCharacterData("" + saveDialog!!.save_name.text.toString())
            character.file_name = saveFile.fileName + ""

            val database = AppDatabase.getInstance(this)
            GlobalScope.launch {

                if (character.id != -1) {
                    saveFile.id = character.id
                    database!!.getCharacterDAO().update(saveFile)
                } else {
                    character.id = saveFile.id
                    database!!.getCharacterDAO().insert(saveFile)
                }

            }
        }
        secondsSinceLastSave = 0
    }
    fun createNewSave(){

        //println("character" + character)
        if(character!=null){
            var saveFile= getCharacterData("" + saveDialog!!.save_name.text.toString())
            character.file_name = saveFile.fileName+""
            character.id = saveFile.id

            val database = AppDatabase.getInstance(this)


            GlobalScope.launch {

                database!!.getCharacterDAO().insert(saveFile)

            }


        }
        secondsSinceLastSave = 0
    }

    override fun onBackPressed() {
        quickSave()
        super.onBackPressed()
    }
    override fun onStop() {
        quickSave()
        println("on stop save")
        super.onStop()
    }

    fun getCharacterData(fileName:String) : CharacterData{
        var data = CharacterData(
            fileName,
            System.currentTimeMillis(),
            character.name_short,
            character.damage,
            character.strain,
            character.token,
            character.wounded,
            character.conditionsActive[0],
            character.conditionsActive[1],
            character.conditionsActive[2],
            character.conditionsActive[3],
            character.conditionsActive[4],
            character.totalXP,
            character.xpSpent,
            character.xpCardsEquipped[0],
            character.xpCardsEquipped[1],
            character.xpCardsEquipped[2],
            character.xpCardsEquipped[3],
            character.xpCardsEquipped[4],
            character.xpCardsEquipped[5],
            character.xpCardsEquipped[6],
            character.xpCardsEquipped[7],
            character.xpCardsEquipped[8],
            character.weapons.getOrElse(0){-1},
            character.weapons.getOrElse(1){-1},
            character.accessories.getOrElse(0){-1},
            character.accessories.getOrElse(1){-1},
            character.accessories.getOrElse(2){-1},
            character.helmet,
            character.armor.getOrElse(0){-1},
            convertItemIDToString(character.mods),
            convertItemIDToString(character.rewards),
            character.background,
            character.conditionsActive[0],
            character.conditionsActive[1],
            character.conditionsActive[2],
            character.conditionsActive[3],
            character.conditionsActive[4],
            character.killCount[0],
            character.killCount[1],
            character.killCount[2],
            character.killCount[3],
            character.killCount[4],
            character.killCount[5],
            character.killCount[6],
            character.killCount[7],
            character.assistCount[0],
            character.assistCount[1],
            character.assistCount[2],
            character.assistCount[3],
            character.assistCount[4],
            character.assistCount[5],
            character.assistCount[6],
            character.assistCount[7],
            character.movesTaken,
            character.attacksMade,
            character.interactsUsed,
            character.timesWounded,
            character.timesRested,
            character.timesWithdrawn,
            character.activated,
            character.damageTaken,
            character.strainTaken,
            character.specialActions,
            character.timesFocused,
            character.timesHidden,
            character.timesStunned,
            character.timesBleeding,
            character.timesWeakened,
            character.cratesPickedUp,
            character.rewardObtained,
            character.withdrawn)


        return data
    }

    fun convertItemIDToString(itemIds: ArrayList<Int>) :String{
        var itemString = ""
        for(i in 0..itemIds.size-1){
            itemString += ","+itemIds[i]
        }
        return itemString
    }
    fun convertStringToItemID(itemString:String):ArrayList<Int> {
        var itemIds = arrayListOf<Int>()
        var itemStrings = itemString.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        for(itemId in itemStrings){
            if(itemId.isNotEmpty()){
                itemIds.add(itemId.toInt())
            }
        }
        return itemIds
    }

//endregion
}


