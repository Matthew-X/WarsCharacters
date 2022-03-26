package com.glasswellapps.iact.character_screen.controllers
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.glasswellapps.iact.R
import com.glasswellapps.iact.ShortToast
import com.glasswellapps.iact.character_screen.CharacterScreen
import com.glasswellapps.iact.character_screen.types.ConditionTypes
import com.glasswellapps.iact.effects.GreenHighlight
import com.glasswellapps.iact.effects.Sounds
import kotlinx.android.synthetic.main.activity_character_screen.*
import kotlinx.android.synthetic.main.dialog_conditions.*
import kotlinx.android.synthetic.main.dialog_show_condition_card.*

class ConditionsController (val characterScreen: CharacterScreen){
    private var character = characterScreen.character
    private var resources = characterScreen.resources
    private val showAlpha = 0.7f
    private var conditionViews = ArrayList<ImageView>()
    private var conditionDrawable = intArrayOf(
        R.drawable.condition_hidden,
        R.drawable.condition_focused,
        R.drawable.condition_weakened,
        R.drawable.condition_bleeding,
        R.drawable.condition_stunned
    )
    private var strengthGlow: GreenHighlight = GreenHighlight(characterScreen.strength_icon, characterScreen, resources)
    private var techGlow: GreenHighlight = GreenHighlight(characterScreen.tech_icon, characterScreen, resources)
    private var insightGlow: GreenHighlight = GreenHighlight(characterScreen.insight_icon, characterScreen, resources)
    private lateinit var conditionsDialog: Dialog
    private lateinit var showConditionCardDialog: Dialog

    init {
        conditionViews.add(characterScreen.bleeding)
        conditionViews.add(characterScreen.condition2)
        conditionViews.add(characterScreen.condition3)
        conditionViews.add(characterScreen.condition4)
        conditionViews.add(characterScreen.condition5)
        for (i in 0 until conditionViews.size) {
            conditionViews[i].setOnLongClickListener { v ->
                removeCondition(v.tag as Int)
                true
            }
        }
        characterScreen.hidden_all.setOnLongClickListener {
            removeCondition(ConditionTypes.HIDDEN)
            true
        }
        characterScreen.focused_all.setOnLongClickListener {
            removeCondition(ConditionTypes.FOCUSED)

            true
        }
        characterScreen.weakened_all.setOnLongClickListener {
            removeCondition(ConditionTypes.WEAKENED)

            true
        }
        characterScreen.bleeding_all.setOnLongClickListener {
            removeCondition(ConditionTypes.BLEEDING)
            characterScreen.actionCompleted()
            true
        }
        characterScreen.stunned_all.setOnLongClickListener {
            removeCondition(ConditionTypes.STUNNED)
            characterScreen.actionCompleted()
            true
        }
        initConditionsDialog()
        initShowConditionCardDialog()

        characterScreen.add_condition.setOnClickListener { onAddCondition() }
        update()
    }

    private fun initConditionsDialog(){
        conditionsDialog = Dialog(characterScreen)
        conditionsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        conditionsDialog.setCancelable(false)
        conditionsDialog.setContentView(R.layout.dialog_conditions)
        conditionsDialog.setCanceledOnTouchOutside(true)

        conditionsDialog.stunned_select.setOnClickListener{
            onStunned()
            conditionsDialog.dismiss()
        }
        conditionsDialog.stunned_select.setOnLongClickListener {
            onShowCard(conditionsDialog.stunned_select)
            true
        }

        conditionsDialog.bleeding_select.setOnClickListener {
            onBleeding()
            conditionsDialog.dismiss()
        }
        conditionsDialog.bleeding_select.setOnLongClickListener {
            onShowCard(conditionsDialog.bleeding_select)
            true
        }

        conditionsDialog.weakened_select.setOnClickListener {
            onWeakened()
            conditionsDialog.dismiss()
        }
        conditionsDialog.weakened_select.setOnLongClickListener {
            onShowCard(conditionsDialog.weakened_select)
            true
        }

        conditionsDialog.focused_select.setOnClickListener {
            onFocused()
            conditionsDialog.dismiss()
        }
        conditionsDialog.focused_select.setOnLongClickListener {
            onShowCard(conditionsDialog.focused_select)
            true
        }

        conditionsDialog.hidden_select.setOnClickListener {
            onHidden()
            conditionsDialog.dismiss()
        }
        conditionsDialog.hidden_select.setOnLongClickListener {
            onShowCard(conditionsDialog.hidden_select)
            true
        }
        conditionsDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun initShowConditionCardDialog() {
        showConditionCardDialog = Dialog(characterScreen, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
        showConditionCardDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        showConditionCardDialog.setContentView(R.layout.dialog_show_condition_card)
        showConditionCardDialog.setCancelable(true)
        showConditionCardDialog.setCanceledOnTouchOutside(true)
        showConditionCardDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        showConditionCardDialog.show_condition_card_dialog.setOnClickListener {
            Sounds.selectSound()
            showConditionCardDialog.dismiss()
        }

        showConditionCardDialog.remove_button.setOnClickListener {
            val currentCondition = Integer.parseInt(showConditionCardDialog.remove_button.tag
                .toString())
            when (currentCondition){
                ConditionTypes.HIDDEN -> onHidden()
                ConditionTypes.FOCUSED -> onFocused()
                ConditionTypes.STUNNED -> onStunned()
                ConditionTypes.BLEEDING -> onBleeding()
                ConditionTypes.WEAKENED -> onWeakened()
            }
            showConditionCardDialog.dismiss()
        }
    }

    fun onShowCard(view: View) {
        val currentCondition = Integer.parseInt(view.tag.toString())
        var drawableId = R.drawable.empty_item_slot
        when (currentCondition) {
            ConditionTypes.HIDDEN -> drawableId = R.drawable.card_hidden
            ConditionTypes.FOCUSED -> drawableId = R.drawable.card_focused
            ConditionTypes.STUNNED -> drawableId = R.drawable.card_stunned
            ConditionTypes.BLEEDING -> drawableId = R.drawable.card_bleeding
            ConditionTypes.WEAKENED -> drawableId = R.drawable.card_weakened
        }
        val drawable = ResourcesCompat.getDrawable(resources,drawableId,null)
        showConditionCardDialog.condition_card_image.setImageDrawable(drawable)
        showConditionCardDialog.remove_button.tag = currentCondition
        /*if(!characterScreen.action_menu.isVisible) {
            showConditionCardDialog.remove_button.visibility = View.VISIBLE
        }
        else{
            showConditionCardDialog.remove_button.visibility = View.GONE
        }*/
        if(character.conditionsActive[currentCondition]) {
            showConditionCardDialog.remove_button_text.text = "REMOVE"
        }
        else{
            showConditionCardDialog.remove_button_text.text = "APPLY"
        }
        //showConditionCardDialog.remove_button.visibility = View.GONE
        showConditionCardDialog.show()
    }

    private fun onAddCondition() {
        Sounds.selectSound()
        conditionsDialog.show()
    }

    private fun onWeakened() {
        character.conditionsActive[ConditionTypes.WEAKENED] = !character
            .conditionsActive[ConditionTypes.WEAKENED]
        if (character.conditionsActive[ConditionTypes.WEAKENED]) {
            character.timesWeakened++
            Sounds.conditionSound(ConditionTypes.WEAKENED)
        } else {
            //character.timesWeakened--
            Sounds.selectSound()
        }
        update()
    }

    private fun onBleeding() {
        character.conditionsActive[ConditionTypes.BLEEDING] = !character.conditionsActive[ConditionTypes.BLEEDING]
        if (character.conditionsActive[ConditionTypes.BLEEDING]) {
            character.timesBleeding++
            Sounds.conditionSound(ConditionTypes.BLEEDING)
        } else {
            Sounds.selectSound()
        }
        update()
    }

    private fun onStunned() {
        character.conditionsActive[ConditionTypes.STUNNED] = !character.conditionsActive[ConditionTypes.STUNNED]
        if (character.conditionsActive[ConditionTypes.STUNNED]) {
            character.timesStunned++
            Sounds.conditionSound(ConditionTypes.STUNNED)
        } else {
            Sounds.selectSound()
        }
        update()
    }

    private fun onHidden() {
        character.conditionsActive[ConditionTypes.HIDDEN] = !character.conditionsActive[ConditionTypes.HIDDEN]
        if (character.conditionsActive[ConditionTypes.HIDDEN]) {
            character.timesHidden++
            Sounds.conditionSound(ConditionTypes.HIDDEN)
        } else {
            Sounds.selectSound()
        }
        update()
    }

    private fun onFocused() {
        character.conditionsActive[ConditionTypes.FOCUSED] = !character.conditionsActive[ConditionTypes.FOCUSED]
        if (character.conditionsActive[ConditionTypes.FOCUSED]) {
            character.timesFocused++
            Sounds.conditionSound(ConditionTypes.FOCUSED)
        } else {
            Sounds.selectSound()
        }
        update()
    }

    private fun removeCondition(conditionType: Int) {
        if(character.actionsLeft>0 || conditionType == ConditionTypes.HIDDEN || conditionType ==
            ConditionTypes.FOCUSED ||
            conditionType == ConditionTypes.WEAKENED ||
            !character.actionUsageSetting) {
            character.conditionsActive[conditionType] = false
            update()
        }
        else{
            ShortToast.show(characterScreen,"NO ACTIONS LEFT")
        }
    }

    fun removeStun(){ removeCondition(ConditionTypes.STUNNED) }
    fun removeBleeding(){ removeCondition(ConditionTypes.BLEEDING) }
    fun removeFocused(){ removeCondition(ConditionTypes.FOCUSED) }
    fun removeHidden(){ removeCondition(ConditionTypes.HIDDEN) }
    fun removeWeakened(){ removeCondition(ConditionTypes.WEAKENED) }

    fun update() {
        //TODO condition save
        characterScreen.character_image.animateConditions = character.conditionAnimSetting
        //quickSave()

        for (i in 0 until conditionViews.size) {
            conditionViews[i].visibility = View.GONE
        }
        characterScreen.show_all_conditions.visibility = View.GONE


        if (!character.conditionAnimSetting) {
            var active = 0
            for (element in character.conditionsActive) {
                if (element) {
                    active++
                }
            }
            characterScreen.conditions_row2.visibility = View.GONE

            if (active < 5) {
                characterScreen.show_conditions.visibility = View.VISIBLE
                characterScreen.show_all_conditions.visibility = View.INVISIBLE

                var conditionType = 0
                for (i in 0 until active) {
                    conditionViews[i].visibility = View.VISIBLE
                    for (j in conditionType until conditionDrawable.size) {
                        if (character.conditionsActive[j]) {
                            conditionViews[i].setImageDrawable(ResourcesCompat.getDrawable(resources, conditionDrawable[conditionType],null))
                            conditionViews[i].tag = conditionType
                            conditionType = j + 1
                            break
                        }
                        conditionType = j + 1
                    }
                }
            } else {
                characterScreen.show_conditions.visibility = View.GONE
                characterScreen.show_all_conditions.visibility = View.VISIBLE
            }
            if (active > 2) {
                characterScreen.conditions_row2.visibility = View.VISIBLE
            } else {
                characterScreen.conditions_row2.visibility = View.GONE
            }
        }
        if (!character.conditionsActive[ConditionTypes.HIDDEN]) {
            conditionsDialog.hidden_select.alpha = showAlpha
            characterScreen.camouflage.visibility = View.GONE
        } else {
            conditionsDialog.hidden_select.alpha = 1f
            if (character.conditionAnimSetting) {
                characterScreen.camouflage.visibility = View.VISIBLE
            } else {
                characterScreen.camouflage.visibility = View.GONE
            }
        }
        if (!character.conditionsActive[ConditionTypes.FOCUSED]) {
            conditionsDialog.focused_select.alpha = showAlpha
            characterScreen.character_image.focused= false
            strengthGlow.disabled()
            techGlow.disabled()
            insightGlow.disabled()
        } else {
            conditionsDialog.focused_select.alpha = 1f
            characterScreen.character_image.focused = true
            strengthGlow.active()
            techGlow.active()
            insightGlow.active()
        }
        if (!character.conditionsActive[ConditionTypes.STUNNED]) {
            conditionsDialog.stunned_select.alpha = showAlpha
            characterScreen.character_image.stunned = false
        } else {
            conditionsDialog.stunned_select.alpha = 1f
            characterScreen.character_image.stunned = true
        }
        if (!character.conditionsActive[ConditionTypes.BLEEDING]) {
            conditionsDialog.bleeding_select.alpha = showAlpha
            characterScreen.character_image.bleeding = false
            characterScreen.bleeding_add_strain.visibility = View.INVISIBLE
        } else {
            conditionsDialog.bleeding_select.alpha = 1f
            characterScreen.character_image.bleeding= true
            characterScreen.bleeding_add_strain.visibility = View.VISIBLE
        }
        if (!character.conditionsActive[ConditionTypes.WEAKENED]) {
            conditionsDialog.weakened_select.alpha = showAlpha
            characterScreen.character_image.weakened = false
        } else {
            conditionsDialog.weakened_select.alpha = 1f
            characterScreen.character_image.weakened = true
        }
    }
}

