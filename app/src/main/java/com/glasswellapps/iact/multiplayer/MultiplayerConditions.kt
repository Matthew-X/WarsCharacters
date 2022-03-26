package com.glasswellapps.iact.multiplayer
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import androidx.core.content.res.ResourcesCompat
import com.glasswellapps.iact.R
import com.glasswellapps.iact.character_screen.types.ConditionTypes
import com.glasswellapps.iact.effects.Sounds
import kotlinx.android.synthetic.main.dialog_conditions.*
import kotlinx.android.synthetic.main.dialog_show_condition_card.*

class MultiplayerConditions (val context: Activity) {
    private var resources = context.resources
    private lateinit var conditionsDialog: Dialog
    private lateinit var showConditionCardDialog: Dialog

    init {
        initConditionsDialog()
        initShowConditionCardDialog()
    }

    private fun initConditionsDialog(){
        conditionsDialog = Dialog(context)
        conditionsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        conditionsDialog.setCancelable(false)
        conditionsDialog.setContentView(R.layout.dialog_conditions)
        conditionsDialog.setCanceledOnTouchOutside(true)

        conditionsDialog.stunned_select.setOnClickListener{
            onStunned()
        }
        conditionsDialog.stunned_select.setOnLongClickListener {
            onShowCard(conditionsDialog.stunned_select)
            true
        }

        conditionsDialog.bleeding_select.setOnClickListener {
            onBleeding()
        }
        conditionsDialog.bleeding_select.setOnLongClickListener {
            onShowCard(conditionsDialog.bleeding_select)
            true
        }

        conditionsDialog.weakened_select.setOnClickListener {
            onWeakened()
        }
        conditionsDialog.weakened_select.setOnLongClickListener {
            onShowCard(conditionsDialog.weakened_select)
            true
        }

        conditionsDialog.focused_select.setOnClickListener {
            onFocused()
        }
        conditionsDialog.focused_select.setOnLongClickListener {
            onShowCard(conditionsDialog.focused_select)
            true
        }

        conditionsDialog.hidden_select.setOnClickListener {
            onHidden()
        }
        conditionsDialog.hidden_select.setOnLongClickListener {
            onShowCard(conditionsDialog.hidden_select)
            true
        }
        conditionsDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun initShowConditionCardDialog() {
        showConditionCardDialog = Dialog(context, android.R.style
            .Theme_Material_Light_NoActionBar_Fullscreen)
        showConditionCardDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        showConditionCardDialog.setContentView(R.layout.dialog_show_condition_card)
        showConditionCardDialog.setCancelable(true)
        showConditionCardDialog.setCanceledOnTouchOutside(true)
        showConditionCardDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        showConditionCardDialog.show_condition_card_dialog.setOnClickListener {
            Sounds.selectSound()
            showConditionCardDialog.dismiss()
        }

        showConditionCardDialog.remove_button.visibility = View.GONE
    }

    fun onShowCard(view: View) {
        val character = player.character
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

        if(character.conditionsActive[currentCondition]) {
            showConditionCardDialog.remove_button_text.text = "REMOVE"
        }
        else{
            showConditionCardDialog.remove_button_text.text = "APPLY"
        }
        showConditionCardDialog.remove_button.visibility = View.GONE
        showConditionCardDialog.show()
    }

    lateinit var player: Player
    fun onAddCondition(player: Player) {
        this.player = player
        update()
        conditionsDialog.show()
    }

    private fun onWeakened() {
        val character = player.character
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
        val character = player.character
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
        val character = player.character
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
        val character = player.character
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
        val character = player.character
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
        val character = player.character

            character.conditionsActive[conditionType] = false
            update()

    }

    fun removeStun(){ removeCondition(ConditionTypes.STUNNED) }
    fun removeBleeding(){ removeCondition(ConditionTypes.BLEEDING) }
    fun removeFocused(){ removeCondition(ConditionTypes.FOCUSED) }
    fun removeHidden(){ removeCondition(ConditionTypes.HIDDEN) }
    fun removeWeakened(){ removeCondition(ConditionTypes.WEAKENED) }

    fun update() {
        if(player!=null) {
            player.updateView()
            val character = player.character

            var hidden = character.conditionsActive[ConditionTypes.HIDDEN]
            conditionsDialog.hidden_select.animate().setDuration(100).alpha(getAlpha(hidden))

            var focused = character.conditionsActive[ConditionTypes.FOCUSED]
            conditionsDialog.focused_select.animate().setDuration(100).alpha(getAlpha(focused))

            var weakened = character.conditionsActive[ConditionTypes.WEAKENED]
            conditionsDialog.weakened_select.animate().setDuration(100).alpha(getAlpha(weakened))

            var bleeding = character.conditionsActive[ConditionTypes.BLEEDING]
            conditionsDialog.bleeding_select.animate().setDuration(100).alpha(getAlpha(bleeding))

            var stunned = character.conditionsActive[ConditionTypes.STUNNED]
            conditionsDialog.stunned_select.animate().setDuration(100).alpha(getAlpha(stunned))

            conditionsDialog.dismiss()
        }
    }
    fun getAlpha(active: Boolean):Float{
        if(active){
            return 1f
        }
        return 0.5f
    }
}