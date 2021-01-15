package com.example.imperialassault

import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat

import kotlinx.android.synthetic.main.activity_character__view.*
import kotlinx.android.synthetic.main.dialog_action_menu.*
import kotlinx.android.synthetic.main.dialog_assist.*
import kotlinx.android.synthetic.main.dialog_bio.*
import kotlinx.android.synthetic.main.dialog_conditions.*
import kotlinx.android.synthetic.main.dialog_show_card.*
import kotlinx.android.synthetic.main.screen_item_select.*
import java.io.InputStream
import kotlin.random.Random


class Character_view : AppCompatActivity() {
    var character:Character = Character();
    var blastAnim:AnimationDrawable = AnimationDrawable()
    var impactAnim :AnimationDrawable= AnimationDrawable()
    var restAnim:AnimationDrawable= AnimationDrawable()
    var sliceAnim:AnimationDrawable= AnimationDrawable()

    var hidden = 0
    var focused = 1
    var weakened = 2
    var bleeding = 3
    var stunned = 4


    var conditionViews = ArrayList<ImageView>()
    var conditionsActive = booleanArrayOf(false,false,false,false,false)
    var conditionDrawable = intArrayOf(R.drawable.condition_hidden, R.drawable.condition_focused, R.drawable.condition_weakened, R.drawable.condition_bleeding, R.drawable.condition_stunned)

    var restDialog:Dialog? = null
    var unwoundDialog:Dialog? = null
    var conditionsDialog:Dialog? = null
    var actionDialog:Dialog? = null
    var showCardDialog:Dialog? = null
    var endActivationDialog:Dialog? = null
    var assistDialog:Dialog? = null
    var optionsDialog:Dialog? = null
    var bioDialog:Dialog? = null
    var saveDialog:Dialog? = null
    var backgroundDialog:Dialog? = null
    var creditsScreen:Dialog? = null
    var settingsScreen:Dialog? = null
    var statsScreen:Dialog? = null
    var itemSelectScreen:Dialog?=null
    var xpSelectScreen:Dialog?=null

    var actionsLeft = 0;
    var killCounts = ArrayList<TextView>()
    val villain =0
    val leader =1
    val vehicle =2
    val creature =3
    val guard =4
    val droid =5
    val scum =6
    val trooper =7

    var activated = false
    var isWounded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character__view)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        var load = false //intent.getBooleanExtra("Load",false)
        var characterName: String = intent.getStringExtra("CharacterName").toString()

        if (!load) {
            when (characterName) {
                "biv" -> { character = Character_biv(this) }
                "davith" -> { character = Character_davith(this) }
                "diala" -> { character = Character_diala(this) }
                "drokdatta" -> { character = Character_drokkatta(this) }
                "fenn" -> { character = Character_fenn(this) }
                "gaarkhan" -> { character = Character_gaarkhan(this) }
                "gideon" -> { character = Character_gideon(this) }
                "jarrod" -> { character = Character_jarrod(this) }
                "jyn" -> { character = Character_jyn(this) }
                "loku" -> { character = Character_loku(this) }
                "kotun" -> { character = Character_kotun(this) }
                "mak" -> { character = Character_mak(this) }
                "mhd19" -> { character = Character_mhd19(this) }
                "murne" -> { character = Character_murne(this) }
                "onar" -> { character = Character_onar(this) }
                "saska" -> { character = Character_saska(this) }
                "shyla" -> { character = Character_shyla(this) }
                "verena" -> { character = Character_verena(this) }
                "vinto" -> { character = Character_vinto(this) }
            }

        } else {

        }
        name.setText("" + character.name);
        health.setText("" + character.health);
        endurance.setText("" + character.endurance);
        speed.setText("" + character.speed);

        when (character.defence_dice) {
            "white" -> ImageViewCompat.setImageTintList(
                defence,
                ColorStateList.valueOf(Color.WHITE)
            )
            "black" -> ImageViewCompat.setImageTintList(
                defence,
                ColorStateList.valueOf(Color.BLACK)
            )
            "" -> defence.visibility=View.INVISIBLE

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

        character_type.setText(character.type)
        character_image.setImageBitmap(character.getCharacterImage())
        background_image.setImageBitmap(character.getBackgroundImage(this))

        blastAnim = createAnimation("blast")
        impactAnim = createAnimation("impact")
        sliceAnim = createAnimation("slice")
        restAnim = createAnimation("rest")

        blast_animation.setBackgroundDrawable(blastAnim)
        blast_animation.visibility = FrameLayout.INVISIBLE
        slice_animation.setBackgroundDrawable(sliceAnim)
        slice_animation.visibility = FrameLayout.INVISIBLE
        impact_animation.setBackgroundDrawable(impactAnim)
        impact_animation.visibility = FrameLayout.INVISIBLE
        rest_animation.setBackgroundDrawable(restAnim)
        rest_animation.visibility = FrameLayout.INVISIBLE



        add_damage.setOnLongClickListener {
            if (character.damage >= character.health) {
                unwoundDialog!!.show()
            }
            true
        }
        add_strain.setOnLongClickListener {
            if (actionsLeft > 0) {
                restDialog!!.show()
            } else {
                showNoActionsLeftToast()
            }
            true
        }




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


        restDialog = Dialog(this)
        restDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        restDialog!!.setCancelable(false)
        restDialog!!.setContentView(R.layout.dialog_rest)
        restDialog!!.setCanceledOnTouchOutside(true)
        restDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        unwoundDialog = Dialog(this)
        unwoundDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        unwoundDialog!!.setCancelable(false)
        unwoundDialog!!.setContentView(R.layout.dialog_unwound)
        unwoundDialog!!.setCanceledOnTouchOutside(true)
        unwoundDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

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
        conditionsDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        optionsDialog = Dialog(this)
        optionsDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        optionsDialog!!.setCancelable(false)
        optionsDialog!!.setContentView(R.layout.dialog_options)
        optionsDialog!!.setCanceledOnTouchOutside(true)
        optionsDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        actionDialog = Dialog(this)
        actionDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        actionDialog!!.setCancelable(false)
        actionDialog!!.setContentView(R.layout.dialog_action_menu)
        actionDialog!!.setCanceledOnTouchOutside(true)
        actionDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        showCardDialog = Dialog(this)
        showCardDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        showCardDialog!!.setContentView(R.layout.dialog_show_card)
        showCardDialog!!.setCancelable(false)
        showCardDialog!!.setCanceledOnTouchOutside(true)
        showCardDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        showCardDialog!!.card_image.setOnClickListener {
            showCardDialog!!.cancel()
            true
        }

        endActivationDialog = Dialog(this)
        endActivationDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        endActivationDialog!!.setCancelable(false)
        endActivationDialog!!.setContentView(R.layout.dialog_end_activation)
        endActivationDialog!!.setCanceledOnTouchOutside(true)
        endActivationDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        assistDialog = Dialog(this)
        assistDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        assistDialog!!.setCancelable(false)
        assistDialog!!.setContentView(R.layout.dialog_assist)
        assistDialog!!.setCanceledOnTouchOutside(true)
        assistDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        bioDialog = Dialog(this)
        bioDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bioDialog!!.setCancelable(false)
        bioDialog!!.setContentView(R.layout.dialog_bio)
        bioDialog!!.setCanceledOnTouchOutside(true)
        bioDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        saveDialog = Dialog(this)
        saveDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        saveDialog!!.setCancelable(false)
        saveDialog!!.setContentView(R.layout.dialog_save)
        saveDialog!!.setCanceledOnTouchOutside(true)
        saveDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))



        backgroundDialog = Dialog(this)
        backgroundDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        backgroundDialog!!.setCancelable(false)
        backgroundDialog!!.setContentView(R.layout.dialog_background)
        backgroundDialog!!.setCanceledOnTouchOutside(true)
        backgroundDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        settingsScreen= Dialog(this,android.R.style.Theme_Material_NoActionBar )
        settingsScreen!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        settingsScreen!!.setCancelable(false)
        settingsScreen!!.setContentView(R.layout.screen_settings)
        settingsScreen!!.setCanceledOnTouchOutside(true)
        settingsScreen!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        statsScreen = Dialog(this,android.R.style.Theme_Material_NoActionBar )
        statsScreen!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        statsScreen!!.setCancelable(false)
        statsScreen!!.setContentView(R.layout.screen_stats)
        statsScreen!!.setCanceledOnTouchOutside(true)
        statsScreen!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        creditsScreen = Dialog(this,android.R.style.Theme_Material_NoActionBar )
        creditsScreen!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        creditsScreen!!.setCancelable(false)
        creditsScreen!!.setContentView(R.layout.screen_stats)
        creditsScreen!!.setCanceledOnTouchOutside(true)
        creditsScreen!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        itemSelectScreen = Dialog(this,android.R.style.Theme_Material_NoActionBar )
        itemSelectScreen!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        itemSelectScreen!!.setCancelable(false)
        itemSelectScreen!!.setContentView(R.layout.screen_item_select)
        itemSelectScreen!!.setCanceledOnTouchOutside(true)

        xpSelectScreen = Dialog(this,android.R.style.Theme_Material_NoActionBar)
        xpSelectScreen!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        xpSelectScreen!!.setCancelable(false)
        xpSelectScreen!!.setContentView(R.layout.screen_xp_select)
        xpSelectScreen!!.setCanceledOnTouchOutside(true)


        killCounts.add(villain_count)
       villain_button.setOnLongClickListener {
           assistDialog!!.assist_icon.setImageDrawable(resources.getDrawable(R.drawable.icon_villian))
           assistDialog!!.assist_button.setTag(villain)
            assistDialog!!.show()
            true
        }
        killCounts.add(leader_count)
        leader_button.setOnLongClickListener {
            assistDialog!!.assist_icon.setImageDrawable(resources.getDrawable(R.drawable.icon_leader))
            assistDialog!!.assist_button.setTag(leader)
            assistDialog!!.show()
            true
        }
        killCounts.add(vehicle_count)
        vehicle_button.setOnLongClickListener {
            assistDialog!!.assist_icon.setImageDrawable(resources.getDrawable(R.drawable
                .icon_vehicle))
            assistDialog!!.assist_button.setTag(vehicle)
            assistDialog!!.show()
            true
        }
        killCounts.add(creature_count)
        creature_button.setOnLongClickListener {
            assistDialog!!.assist_icon.setImageDrawable(resources.getDrawable(R.drawable.icon_creature))
            assistDialog!!.assist_button.setTag(creature)
            assistDialog!!.show()
            true
        }
        killCounts.add(guard_count)
        guard_button.setOnLongClickListener {
            assistDialog!!.assist_icon.setImageDrawable(resources.getDrawable(R.drawable.icon_gaurd))
            assistDialog!!.assist_button.setTag(guard)
            assistDialog!!.show()
            true
        }
        killCounts.add(droid_count)
       droid_button.setOnLongClickListener {
            assistDialog!!.assist_icon.setImageDrawable(resources.getDrawable(R.drawable.icon_droid))
           assistDialog!!.assist_button.setTag(droid)
            assistDialog!!.show()
            true
        }
        killCounts.add(scum_count)
        scum_button.setOnLongClickListener {
            assistDialog!!.assist_icon.setImageDrawable(resources.getDrawable(R.drawable.icon_scum))
            assistDialog!!.assist_button.setTag(scum)
            assistDialog!!.show()
            true
        }
        killCounts.add(trooper_count)
        trooper_button.setOnLongClickListener {
            assistDialog!!.assist_icon.setImageDrawable(resources.getDrawable(R.drawable
                .icon_trooper))
            assistDialog!!.assist_button.setTag(trooper)
            assistDialog!!.show()
            true
        }


    }


    fun setDiceColor(dice: ImageView, color: Char) {
        when(color){
            'B' -> ImageViewCompat.setImageTintList(dice, ColorStateList.valueOf(ContextCompat.getColor(applicationContext,R.color.dice_blue)))
            'G' -> ImageViewCompat.setImageTintList(dice, ColorStateList.valueOf(ContextCompat.getColor(applicationContext,R.color.dice_green)))
            'Y' -> ImageViewCompat.setImageTintList(dice, ColorStateList.valueOf(ContextCompat.getColor(applicationContext,R.color.dice_yellow)))
            'R' -> ImageViewCompat.setImageTintList(dice, ColorStateList.valueOf(ContextCompat.getColor(applicationContext,R.color.dice_red)))
            ' ' -> dice.visibility = ImageView.GONE
        }
    }

    fun playDamageAnim(){
        val animType = Math.random();
        if(animType<0.3){
            blast_animation.visibility = FrameLayout.VISIBLE
            blastAnim.setVisible(true, true)
            blastAnim.start()
        }
        else if(animType<0.6){
            slice_animation.visibility = FrameLayout.VISIBLE
            sliceAnim.setVisible(true, true)
            sliceAnim.start()
        }
        else{
            impact_animation.visibility = FrameLayout.VISIBLE
            impactAnim.setVisible(true, true)
            impactAnim.start()
        }


    }
    fun playRestAnim(){
        rest_animation.visibility = FrameLayout.VISIBLE
        restAnim.setVisible(true, true)
        restAnim.start()
    }


    fun createAnimation(type: String): AnimationDrawable{

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels.toFloat()
        val width = displayMetrics.widthPixels.toFloat()

        val animation = AnimationDrawable()


        for(i in 0..9){
            var bitmap = getBitmap(this, "animations/" + type + "/" + type + "_" + +i + ".png")


            if (bitmap != null) {
                val bitmapWidth = width/(height-128)*bitmap.width
                val bitmapOffset =((bitmap.width-bitmapWidth)/2).toInt()
                println(bitmapWidth)
                bitmap = Bitmap.createBitmap(bitmap, bitmapOffset, 0, bitmapWidth.toInt(), bitmap.height)
                val frame = BitmapDrawable(resources, bitmap);
                if(type .equals("rest")) {
                    animation.addFrame(frame, 100)
                }

            else {
                animation.addFrame(frame, 70)
            }
            }
        }
        animation.setOneShot(true);


        return animation;
    }

    open fun getBitmap(context: Context, path: String): Bitmap? {
        val assetManager = context.assets
        var inputStream: InputStream? = null
        try {
            inputStream = assetManager.open(path)
            return BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun onAddStrain(view: View) {
        if(character.strain < character.endurance) {
            character.strain++
            character.strainTaken++

            if(minus_strain.alpha==0f){
                minus_strain.animate().alpha(1f)
            }

            add_strain.setText("" + character.strain)

        }
        else{
            addDamage()

        }
        playRestAnim()

    }
    fun onMinusStrain(view: View) {
        if(character.strain >0) {
            character.strain--
            add_strain.setText("" + character.strain)
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

    fun addDamage():Boolean{


        if(character.damage < character.health*2) {
            character.damage++
            character.damageTaken++

            if(minus_damage.alpha==0f){
                minus_damage.animate().alpha(1f)
            }

            if(character.damage < character.health) {

                add_damage.setText("" + character.damage)
            }
            else if(character.damage < character.health*2){

                character.wounded = character.damage-character.health
                add_damage.setText("" + character.wounded)
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
                }

            }
            else{
                add_damage.setText("" + character.health)
                val slide = ObjectAnimator.ofFloat(character_image, "translationY",0f,0f,character_image.height.toFloat())
                slide.setDuration(1500)
                slide.start()
            }
            var hitY = ObjectAnimator.ofFloat(character_image,"translationY",0f,20f*Random
                .nextFloat(),
                0f,20f*Random.nextFloat(),0f,20f*Random.nextFloat(),0f)
            hitY .setDuration(300)
            hitY .start()

            var hitX = ObjectAnimator.ofFloat(character_image,"translationX",0f,-10f*Random
                .nextFloat(),
                0f,-10f*Random.nextFloat(),0f,-10f*Random.nextFloat(),0f)
            hitX .setDuration(300)
            hitX .start()

            return true
        }
        return false
    }

    fun onMinusDamage(view: View) {
        if(character.damage >0) {
            character.damage--
            if(character.damage < character.health) {
                add_damage.setText("" + character.damage)
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
                }
            }
            else if(character.damage < character.health*2){
                character.wounded = character.damage-character.health
                add_damage.setText("" + character.wounded)
                val slide = ObjectAnimator.ofFloat(character_image, "translationY", 0f)
                slide.setDuration(500)
                slide.start()
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
        add_damage.setText("" + character.damage)
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

        unwoundDialog!!.cancel()
    }


    fun onAddCondition(view: View) {
        conditionsDialog!!.show()

    }

    fun onHide(view: View) {
        view.visibility = View.INVISIBLE
    }



    fun onWeakened(view: View) {
        conditionsActive[weakened] =!conditionsActive[weakened]
        if(conditionsActive[weakened]) {
            character.timesWeakend++
        }
        else{
            character.timesWeakend--
        }
        updateConditionIcons()
    }
    fun onBleeding(view: View) {
        conditionsActive[bleeding] =!conditionsActive[bleeding]
        if(conditionsActive[bleeding]) {
            character.timesBleeding++
        }else{
            character.timesBleeding--
        }
        updateConditionIcons()
    }
    fun onStunned(view: View) {
        conditionsActive[stunned] =!conditionsActive[stunned]
        if(conditionsActive[stunned]) {
            character.timesStunned++
        }
        else{
            character.timesStunned--
        }
        updateConditionIcons()
    }
    fun onHidden(view: View) {
        conditionsActive[hidden] =!conditionsActive[hidden]
        if(conditionsActive[hidden]) {
            character.timesHidden++
        }
        else{
            character.timesHidden--
        }
        updateConditionIcons()
    }
    fun onFocused(view: View) {
        conditionsActive[focused] =!conditionsActive[focused]
        if(conditionsActive[focused]) {
            character.timesFocused++
        }
        else{
            character.timesFocused--
        }
        updateConditionIcons()
    }


    fun removeCondition(conditionType : Int){

        if(actionsLeft>0 || conditionType==hidden||conditionType==focused||conditionType==weakened) {
            conditionsActive[conditionType] = false
            updateConditionIcons()
        }
        else{
            showNoActionsLeftToast()
        }
    }

    fun updateConditionIcons(){
        for(i in 0..conditionViews.size-1){
            conditionViews[i].visibility = View.GONE
        }

        var active = 0;
        for(i in 0..conditionsActive.size-1){
            if(conditionsActive[i]){
                active++;
            }
        }

        if(active<5) {
            show_conditions.visibility = View.VISIBLE
            show_all_conditions.visibility = View.INVISIBLE

            var conditionType = 0;
            for (i in 0..active - 1) {
                conditionViews[i].visibility = View.VISIBLE
                for (j in conditionType..conditionDrawable.size - 1) {
                    if (conditionsActive[j]) {
                        conditionViews[i].setImageDrawable(resources.getDrawable(conditionDrawable[conditionType]))
                        conditionViews[i].setTag(conditionType)
                        conditionType = j + 1;
                        break
                    }
                    conditionType = j + 1;
                }
            }
        }
        else{
            show_conditions.visibility = View.GONE
            show_all_conditions.visibility = View.VISIBLE
        }

        if(active>2){
            conditions_row2.visibility = View.VISIBLE
        }
        else{
            conditions_row2.visibility = View.GONE
        }


        if(!conditionsActive[hidden]) {
            conditionsDialog!!.hidden_select.alpha = 1f
        }
        else{
            conditionsDialog!!.hidden_select.alpha = 0.5f
        }

        if(!conditionsActive[hidden]) {
            conditionsDialog!!.hidden_select.alpha = 1f
        }
        else{
            conditionsDialog!!.hidden_select.alpha = 0.5f
        }

        if(!conditionsActive[focused]) {
            conditionsDialog!!.focused_select.alpha=1f
        }
        else{
            conditionsDialog!!.focused_select.alpha = 0.5f
        }

        if(!conditionsActive[stunned]) {
            conditionsDialog!!.stunned_select.alpha = 1f
        }
        else{
            conditionsDialog!!.stunned_select.alpha = 0.5f
        }

        if(!conditionsActive[bleeding]) {
            conditionsDialog!!.bleeding_select.alpha = 1f
        }
        else{
            conditionsDialog!!.bleeding_select.alpha = 0.5f
        }

        if(!conditionsActive[weakened]) {
            conditionsDialog!!.weakened_select.alpha = 1f
        }
        else{
            conditionsDialog!!.weakened_select.alpha = 0.5f
        }
    }

    fun onActivate(view: View) {

        if(!activated){
            val flipUnactive = ObjectAnimator.ofFloat(unactive,"scaleX",1f,0f,0f,0f)
            flipUnactive.setDuration(300)
            flipUnactive.start()

            val flipActive = ObjectAnimator.ofFloat(active,"scaleX",0f,0f,0f,1f)
            flipActive.setDuration(300)
            flipActive.start()


            action1.visibility = View.VISIBLE
            action_button1.visibility = View.VISIBLE
            action_button1.animate().alpha(1f)
            action2.visibility = View.VISIBLE
            action_button2.visibility = View.VISIBLE
            action_button2.animate().alpha(1f)
            actionsLeft = 2;
            activated = true

            character.activated++
        }
        else{
           onEndActivation(view)
        }
    }

    fun onEndActivation(view:View){

        endActivationDialog!!.cancel()
        removeCondition(weakened)
        val flipUnactive = ObjectAnimator.ofFloat(unactive,"scaleX",0f,0f,0f,1f)
        flipUnactive.setDuration(300)
        flipUnactive.start()

        val flipActive = ObjectAnimator.ofFloat(active,"scaleX",1f,0f,0f,0f)
        flipActive.setDuration(300)
        flipActive.start()

        action_button1.animate().alpha(0f)
        action_button1.visibility = View.GONE

        action_button2.animate().alpha(0f)
        action_button2.visibility = View.GONE
        activated = false

    }

    fun onEndActivationNo(view:View){
        endActivationDialog!!.cancel()
    }


    fun onAction(view: View) {

        actionDialog!!.cancel()

        if(actionsLeft>0) {
            //todo add focus symbol to attack
            if (conditionsActive[focused]) {
                actionDialog!!.attack_focused.visibility = View.VISIBLE
            } else {
                actionDialog!!.attack_focused.visibility = View.GONE
            }

            if (conditionsActive[hidden]) {
                actionDialog!!.attack_hidden.visibility = View.VISIBLE
            } else {
                actionDialog!!.attack_hidden.visibility = View.GONE
            }

            //todo add stun symbol and deactivate to move, special and attack
            if (conditionsActive[stunned]) {
                actionDialog!!.action_stunned_attack.visibility = View.VISIBLE
                actionDialog!!.action_stunned_move.visibility = View.VISIBLE
                actionDialog!!.action_stunned_special.visibility = View.VISIBLE
                actionDialog!!.action_remove_stun.visibility = View.VISIBLE
            } else {
                actionDialog!!.action_stunned_attack.visibility = View.INVISIBLE
                actionDialog!!.action_stunned_move.visibility = View.INVISIBLE
                actionDialog!!.action_stunned_special.visibility = View.INVISIBLE
                actionDialog!!.action_remove_stun.visibility = View.GONE
            }

            if (conditionsActive[bleeding]) {
                actionDialog!!.action_remove_bleeding.visibility = View.VISIBLE
            } else {
                actionDialog!!.action_remove_bleeding.visibility = View.GONE
            }

            actionDialog!!.show()
        }

    }

    fun actionCompleted(){
        if(actionsLeft >0) {
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
        if(actionsLeft <=0) {

            actionDialog!!.cancel()
            if(activated) {
                endActivationDialog!!.show()
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
        if(actionsLeft>0) {
            character.strain -= character.endurance

            if (character.strain < 0) {
                for (i in 1..-character.strain) {
                    onMinusDamage(view)
                }
                character.strain = 0;
            }
            add_strain.setText("" + character.strain)
            playRestAnim()
            character.timesRested++
            restDialog!!.cancel()

            actionCompleted()
        }
        else{
            showNoActionsLeftToast()
        }
    }

    fun onRemoveStun(view: View) {
        if(actionsLeft>0) {
            removeCondition(stunned)
            onAction(action_complete)
            actionCompleted()
        }
        else{
            showNoActionsLeftToast()
        }
    }

    fun onRemoveBleeding(view: View) {
        if(actionsLeft>0) {
            removeCondition(bleeding)
            onAction(action_complete)
            actionCompleted()
        }
        else{
            showNoActionsLeftToast()
        }
    }




    fun onShowFocusedCard(view:View){
        showCardDialog!!.card_image.setImageDrawable(resources.getDrawable(R.drawable.card_focused))
        showCardDialog!!.show()
    }
    fun onShowStunnedCard(view:View){
        showCardDialog!!.card_image.setImageDrawable(resources.getDrawable(R.drawable.card_stunned))
        showCardDialog!!.show()
    }
    fun onShowWeakenedCard(view:View){
        showCardDialog!!.card_image.setImageDrawable(resources.getDrawable(R.drawable.card_weakened))
        showCardDialog!!.show()
    }
    fun onShowBleedingCard(view:View){
        showCardDialog!!.card_image.setImageDrawable(resources.getDrawable(R.drawable.card_bleeding))
        showCardDialog!!.show()
    }
    fun onShowHiddenCard(view:View){
        showCardDialog!!.card_image.setImageDrawable(resources.getDrawable(R.drawable.card_hidden))
        showCardDialog!!.show()
    }

    fun onShowCard(view:View){
        when(view.getTag()){
            hidden->onShowHiddenCard(view)
            focused->onShowFocusedCard(view)
            stunned->onShowStunnedCard(view)
            bleeding->onShowBleedingCard(view)
            weakened->onShowWeakenedCard(view)
        }
    }

    fun showNoActionsLeftToast(){
        val noActionsLeftToast=Toast(this)
        noActionsLeftToast!!.duration = Toast.LENGTH_SHORT
        noActionsLeftToast!!.view = layoutInflater.inflate(R.layout.toast_no_actions_left,character_view_group, false)
        noActionsLeftToast!!.setGravity(Gravity.CENTER,0,0)
        noActionsLeftToast!!. show()
    }

    var sideMenuState = 0
    fun onExtendDown(view: View) {

        if(sideMenuState>-1){
            sideMenuState--
            kill_tracker_bar.animate().translationYBy(menu_bar.height.toFloat())
            menu_bar.animate().translationYBy(menu_bar.height.toFloat())
        }
        when(sideMenuState){
            -1->{extend_down_button.animate().alpha(0f);
                extend_up_button.animate().alpha(1f)
                kill_tracker_bar.animate().translationY(menu_bar.height.toFloat())
                menu_bar.animate().translationY(menu_bar.height.toFloat())}
            0->{extend_down_button.animate().alpha(1f)
                extend_up_button.animate().alpha(1f)
                kill_tracker_bar.animate().translationY(0f)
                menu_bar.animate().translationY(0f)}
            1->{extend_down_button.animate().alpha(1f)
                extend_up_button.animate().alpha(0f)
                kill_tracker_bar.animate().translationY(-menu_bar.height.toFloat())
                menu_bar.animate().translationY(-menu_bar.height.toFloat())}
        }


    }
    fun onExtendUp(view: View) {

        if(sideMenuState<1){
            sideMenuState++
            kill_tracker_bar.animate().translationYBy(-menu_bar.height.toFloat())
            menu_bar.animate().translationYBy(-menu_bar.height.toFloat())
        }
        when(sideMenuState){
            -1->{extend_down_button.animate().alpha(0f);
                extend_up_button.animate().alpha(1f)
                kill_tracker_bar.animate().translationY(menu_bar.height.toFloat())
                menu_bar.animate().translationY(menu_bar.height.toFloat())}
            0->{extend_down_button.animate().alpha(1f)
                extend_up_button.animate().alpha(1f)
                kill_tracker_bar.animate().translationY(0f)
                menu_bar.animate().translationY(0f)}
            1->{extend_down_button.animate().alpha(1f)
                extend_up_button.animate().alpha(0f)
                kill_tracker_bar.animate().translationY(-menu_bar.height.toFloat())
                menu_bar.animate().translationY(-menu_bar.height.toFloat())}
        }

    }

    fun onReward(view: View) {
        itemSelectScreen!!.show()
        val currentTab= itemSelectScreen!!.item_selection_tabs.getTabAt(0)
        currentTab!!.select()
    }
    fun onAccessory(view: View) {
        itemSelectScreen!!.show()
        val currentTab= itemSelectScreen!!.item_selection_tabs.getTabAt(1)
        currentTab!!.select()
    }
    fun onArmour(view: View) {
        itemSelectScreen!!.show()
        val currentTab= itemSelectScreen!!.item_selection_tabs.getTabAt(2)
        currentTab!!.select()
    }
    fun onMelee(view: View) {
        itemSelectScreen!!.show()
        val currentTab= itemSelectScreen!!.item_selection_tabs.getTabAt(3)
        currentTab!!.select()
    }
    fun onRange(view: View) {
        itemSelectScreen!!.show()
        val currentTab= itemSelectScreen!!.item_selection_tabs.getTabAt(4)
        currentTab!!.select()
    }
    fun onXPScreen(view: View) {
        xpSelectScreen!!.show()

    }

    fun killCountUp(type:Int){
        var killCount = Integer.parseInt(killCounts[type].text.toString())
        killCount++
        killCounts[type].setText(""+killCount)
    }
    fun killCountDown(type:Int){
        var killCount = Integer.parseInt(killCounts[type].text.toString())
        if(killCount>0) {
            killCount--
            killCounts[type].setText("" + killCount)
        }
    }

    //TODO add to stats
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

    //TODO add to stats
    fun onAssist(view: View) {
        when(view.tag){
            villain->{character.assistCount[villain]++}
            leader->{character.assistCount[leader]++}
            vehicle->{character.assistCount[vehicle]++}
            creature->{character.assistCount[creature]++}
            guard->{character.assistCount[guard]++}
            droid->{character.assistCount[droid]++}
            scum->{character.assistCount[scum]++}
            trooper->{character.assistCount[trooper]++}
        }
        assistDialog!!.cancel()
    }

    fun onShowOptions(view: View) {
        optionsDialog!!.show()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        println("save")
    }

    fun onBiography(view: View) {
        optionsDialog!!.cancel()
        bioDialog!!.bio_title.setText(character.bio_title)
        bioDialog!!.bio_quote.setText(character.bio_quote)
        bioDialog!!.bio_text.setText(character.bio_text)
        bioDialog!!.show()
    }
    fun onPower(view: View) {
        optionsDialog!!.cancel()
        if(!isWounded) {
            showCardDialog!!.card_image.setImageBitmap(character.power)
        }
        else{
            showCardDialog!!.card_image.setImageBitmap(character.power_wounded)
        }

        showCardDialog!!.show()
    }
    fun onSave(view: View) {
        optionsDialog!!.cancel()
        saveDialog!!.show()
    }
    fun onBackground(view: View) {
        optionsDialog!!.cancel()
        backgroundDialog!!.show()
    }
    fun onSettings(view: View) {
        optionsDialog!!.cancel()
        settingsScreen!!.show()
    }
    fun onStatistics(view: View) {
        optionsDialog!!.cancel()
        statsScreen!!.show()
    }

    fun onCredits(view: View) {
        optionsDialog!!.cancel()
        creditsScreen!!.show()
    }

    fun onBackgroundSnow(view: View) {
        background_image.setImageBitmap(getBitmap(this,"backgrounds/background_snow.png"))

    }
    fun onBackgroundJungle(view: View) {
        background_image.setImageBitmap(getBitmap(this,"backgrounds/background_jungle.png"))
    }
    fun onBackgroundDesert(view: View) {
        background_image.setImageBitmap(getBitmap(this,"backgrounds/background_desert.png"))
    }
    fun onBackgroundInterior(view: View) {
        background_image.setImageBitmap(getBitmap(this,"backgrounds/background_interior.png"))
    }

    fun onSaveCharacter(view: View) {
        saveDialog!!.cancel()
    }
}

