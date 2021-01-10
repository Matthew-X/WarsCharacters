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
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ImageViewCompat
import kotlinx.android.synthetic.main.action_menu.*
import kotlinx.android.synthetic.main.activity_character__view.*
import java.io.InputStream


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

    var actionsLeft = 0;
    var actionDialog:Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character__view)

        var load = false //intent.getBooleanExtra("Load",false)
        var characterName: String = intent.getStringExtra("CharacterName").toString()

        if (!load) {
            when (characterName) {
                "mak" -> {
                    character = Character_mak(this)
                }

            }


        } else {

        }
        name.setText("" + character.name);
        health.setText("" + character.health);
        endurance.setText("" + character.endurance);
        speed.setText("" + character.speed);

        when (character.defence_dice) {
            "white" -> ImageViewCompat.setImageTintList(defence, ColorStateList.valueOf(Color.WHITE))
            "black" -> ImageViewCompat.setImageTintList(defence, ColorStateList.valueOf(Color.BLACK))
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
                unwound.visibility = View.VISIBLE
            }
            true
        }
        add_strain.setOnLongClickListener {
            if(actionsLeft>0) {
                rest.visibility = View.VISIBLE;
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
                    v.visibility = View.INVISIBLE

                    removeCondition(v.getTag() as Int)
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
            true
        }
        stunned_all.setOnLongClickListener {
            removeCondition(stunned)
            true
        }







    }


    fun setDiceColor(dice: ImageView, color: Char) {
        when(color){
            'B' -> ImageViewCompat.setImageTintList(dice, ColorStateList.valueOf(getResources().getColor(R.color.dice_blue, null)))
            'G' -> ImageViewCompat.setImageTintList(dice, ColorStateList.valueOf(getResources().getColor(R.color.dice_green, null)))
            'Y' -> ImageViewCompat.setImageTintList(dice, ColorStateList.valueOf(getResources().getColor(R.color.dice_yellow, null)))
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
                animation.addFrame(frame, 60)
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

    }
    fun onAddDamage(view: View) {
        if(addDamage()) {
            playDamageAnim()
        }
    }

    fun addDamage():Boolean{
        if(character.damage < character.health*2) {
            character.damage++
            if(character.damage < character.health) {

                add_damage.setText("" + character.damage)
            }
            else if(character.damage < character.health*2){

                character.wounded = character.damage-character.health
                add_damage.setText("" + character.wounded)
                wounded.visibility = ImageView.VISIBLE
            }
            else{
                add_damage.setText("" + character.health)
                val slide = ObjectAnimator.ofFloat(character_image, "translationY",0f,0f,character_image.height.toFloat())
                slide.setDuration(1500)
                slide.start()
            }
            return true
        }
        return false
    }

    fun onMinusDamage(view: View) {
        if(character.damage >0) {
            character.damage--
            if(character.damage < character.health) {
                add_damage.setText("" + character.damage)
                wounded.visibility = ImageView.INVISIBLE
                character.wounded = 0;
            }
            else if(character.damage < character.health*2){
                character.wounded = character.damage-character.health
                add_damage.setText("" + character.wounded)
                val slide = ObjectAnimator.ofFloat(character_image, "translationY", 0f)
                slide.setDuration(500)
                slide.start()
            }

        }



    }

    fun onUnwound(view: View) {
            playRestAnim()
            character.damage = 0
            character.wounded = 0
            add_damage.setText("" + character.damage)
            wounded.visibility = ImageView.INVISIBLE
            unwound.visibility = View.INVISIBLE
    }


    fun onAddCondition(view: View) {
        condition_select.visibility = View.VISIBLE
    }

    fun onHide(view: View) {
        view.visibility = View.INVISIBLE
    }



    fun onWeakened(view: View) {
        if(!conditionsActive[weakened]) {
            //condition_select.visibility = View.INVISIBLE
            view.alpha = 0.5f
            conditionsActive[weakened] = true
            addCondition()
        }
        else{
            removeCondition(weakened)
        }

    }
    fun onBleeding(view: View) {
        if(!conditionsActive[bleeding]) {
            //condition_select.visibility = View.INVISIBLE
            view.alpha = 0.5f
            conditionsActive[bleeding] = true
            addCondition()
        }
        else{
            removeCondition(bleeding)
        }
    }
    fun onStunned(view: View) {
        if (!conditionsActive[stunned]) {
            //condition_select.visibility = View.INVISIBLE
            view.alpha = 0.5f
            conditionsActive[stunned] = true
            addCondition()
        }
        else{
            removeCondition(stunned)
        }
    }
    fun onHidden(view: View) {
        if(!conditionsActive[hidden]) {
            //condition_select.visibility = View.INVISIBLE
            view.alpha = 0.5f
            conditionsActive[hidden] = true
            addCondition()
        }
        else{
            removeCondition(hidden)
        }
    }
    fun onFocused(view: View) {
        if(!conditionsActive[focused]) {
            //condition_select.visibility = View.INVISIBLE
            view.alpha = 0.5f
            conditionsActive[focused] = true
            addCondition()
        }
        else{
            removeCondition(focused)
        }
    }



    fun addCondition(){
            setConditionIcons()
    }

    fun removeCondition(conditionType : Int){
        conditionsActive[conditionType] = false
        when(conditionType){
            hidden->hidden_select.alpha=1f
            focused->focused_select.alpha=1f
            stunned->stunned_select.alpha=1f
            bleeding->bleeding_select.alpha=1f
            weakened->weakened_select.alpha=1f
        }
        setConditionIcons()
    }

    fun setConditionIcons(){
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
            show_conditions.visibility = View.INVISIBLE
            show_all_conditions.visibility = View.VISIBLE
        }

        if(active>2){
            conditions_row2.visibility = View.VISIBLE
        }
        else{
            conditions_row2.visibility = View.GONE
        }
    }

    fun onActivate(view: View) {
        if(unactive.visibility == View.VISIBLE){
            unactive.visibility = View.INVISIBLE

            action1.visibility = View.VISIBLE
            action_button1.visibility = View.VISIBLE
            action2.visibility = View.VISIBLE
            action_button2.visibility = View.VISIBLE
            actionsLeft = 2;
        }
        else{
            unactive.visibility = View.VISIBLE
            action_button1.visibility = View.INVISIBLE
            action_button2.visibility = View.INVISIBLE

        }
    }

    fun onAction(view: View) {
        actionDialog = Dialog(this)
        actionDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        actionDialog!!.setCancelable(false)
        actionDialog!!.setContentView(R.layout.action_menu)
        actionDialog!!.setCanceledOnTouchOutside(true)

        actionDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //todo add focus symbol to attack
        if(conditionsActive[focused]){
            actionDialog!!.attack_focused.visibility = View.VISIBLE
        }
        else{
            actionDialog!!.attack_focused.visibility = View.GONE
        }

        //todo add stun symbol and deactivate to move, special and attack


        actionDialog!!.show()
    }
    fun actionCompleted(){
        actionsLeft--;
        if(actionsLeft == 1){
            action1.visibility = View.INVISIBLE
        }
        else if(actionsLeft == 0){
            action2.visibility = View.INVISIBLE
        }
        if(conditionsActive[bleeding]){
            onAddStrain(add_strain)
        }
    }

    fun onAttack(view: View) {
        if(actionsLeft>0 && !conditionsActive[stunned]) {
            actionCompleted()
            removeCondition(focused)
            actionDialog!!.attack_focused.visibility = View.GONE
        }
        onAction(action1)
    }
    fun onMove(view: View) {
        if(actionsLeft>0 && !conditionsActive[stunned]) {
            actionCompleted()
        }
    }
    fun onSpecial(view: View) {
        if(actionsLeft>0) {
            actionCompleted()
        }
    }
    fun onInteract(view: View) {
        if(actionsLeft>0) {
            actionCompleted()
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

            rest.visibility = View.INVISIBLE

            actionCompleted()
        }
    }
}

