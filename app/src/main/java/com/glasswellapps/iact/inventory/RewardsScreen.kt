package com.glasswellapps.iact.inventory

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import com.glasswellapps.iact.*
import com.glasswellapps.iact.effects.Sounds
import com.glasswellapps.iact.loading.CharacterHolder
import kotlinx.android.synthetic.main.activity_rewards_screen.*
import kotlinx.android.synthetic.main.dialog_show_card.*
import kotlinx.android.synthetic.main.toast.view.*

class RewardsScreen : AppCompatActivity() {
    val character = CharacterHolder.getActiveCharacter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rewards_screen)

        if(character == null){
            finish()
        }
        var rewardsViews = ArrayList<ImageView>()
        rewardsViews.add(this.reward_image0)
        rewardsViews.add(this.reward_image1)
        rewardsViews.add(this.reward_image2)
        rewardsViews.add(this.reward_image3)
        rewardsViews.add(this.reward_image4)
        rewardsViews.add(this.reward_image5)
        rewardsViews.add(this.reward_image6)
        rewardsViews.add(this.reward_image7)
        rewardsViews.add(this.reward_image8)
        rewardsViews.add(this.reward_image9)
        rewardsViews.add(this.reward_image10)
        rewardsViews.add(this.reward_image11)
        rewardsViews.add(this.reward_image12)
        rewardsViews.add(this.reward_image13)
        rewardsViews.add(this.reward_image14)
        rewardsViews.add(this.reward_image15)
        rewardsViews.add(this.reward_image16)
        rewardsViews.add(this.reward_image17)
        rewardsViews.add(this.reward_image18)
        rewardsViews.add(this.reward_image19)
        rewardsViews.add(this.reward_image20)
        rewardsViews.add(this.reward_image21)
        rewardsViews.add(this.reward_image22)
        rewardsViews.add(this.reward_image23)
        rewardsViews.add(this.reward_image24)
        rewardsViews.add(this.reward_image25)
        rewardsViews.add(this.reward_image26)


        for(i in 0..Items.rewardsArray!!.size-1){
            var currentItem = Items.rewardsArray!!.get(i)
            val gridItem = rewardsViews[i]
            gridItem.alpha = 0.5f
            gridItem.setImageResource(currentItem.resourceId)
            setClickables(gridItem, currentItem)
            when (currentItem.type) {
                Items.reward -> {
                    if (character.rewards.contains(currentItem.index)) {
                        gridItem.alpha = 1f
                    }
                }
                Items.acc -> {
                    if (character.accessories.contains(currentItem.index)) {
                        gridItem.alpha = 1f
                    }
                }
            }
        }
        showCardDialog = Dialog(this)
        showCardDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        showCardDialog!!.setContentView(R.layout.dialog_show_card)
        showCardDialog!!.setCancelable(false)
        showCardDialog!!.setCanceledOnTouchOutside(true)
        showCardDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        showCardDialog!!.show_card_dialog.setOnClickListener {
            showCardDialog!!.cancel()
            true
        }
    }

    fun setClickables(gridItem:ImageView, currentItem: Item) {
        gridItem.setOnLongClickListener {
            onShowCard(gridItem)
            true
        }

        gridItem.setOnClickListener {

            when (currentItem.type) {
                Items.reward -> {
                    gridItem.alpha = equipReward(currentItem)
                }
                Items.acc -> {
                    gridItem.alpha = equipAcc(currentItem)
                }
            }

        }
    }
    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    fun equipReward(item: Item): Float {
        if(!CharacterHolder.getIsInteractable()){
            Sounds.negativeSound()
            return 0.5f;
        }
         //remove if already equipped
        if (character.rewards.remove(item.index)) {
            Sounds.selectSound()
            return 0.5f
        }

        character.rewards.add(item.index)
        Sounds.equipSound(this, item.soundType)
        return 1f
    }

    fun equipAcc(item: Item): Float {
        if(!CharacterHolder.getIsInteractable()){
            Sounds.negativeSound()
            return 0.5f;
        }
        if (character.accessories.remove(item.index)) {
            Sounds.selectSound()
            return 0.5f
        }
        //equip if slot available
        if (character.accessories.size < 3) {
            character.accessories.add(item.index)
            Sounds.equipSound(this, item.soundType)
            return 1f
        }
        //TODO toast 3 accessory limit reached
        showItemLimitReached(item.type)

        //not equipped
        return 0.5f
    }

    private fun showItemLimitReached(itemType : Int) {
        Sounds.negativeSound()
        val toast = Toast(this)
        toast!!.duration = Toast.LENGTH_SHORT
        val view = this.layoutInflater.inflate(
            R.layout.toast,
            null,
            false
        )
        when(itemType){
            Items.melee -> view.toast_text.setText("2 weapon limit reached")
            Items.ranged -> view.toast_text.setText("2 weapon limit reached")
            Items.armor -> view.toast_text.setText("1 armor limit reached")
            Items.acc -> view.toast_text.setText("3 accessory limit reached")
            -1 -> view.toast_text.setText("1 helmet limit reached")
        }

        toast!!.view = view
        toast!!.setGravity(Gravity.CENTER, 0, 0)
        toast!!.show()
    }

    var showCardDialog: Dialog? = null
    fun onShowCard(view: ImageView) {
        var image = ((view).drawable as BitmapDrawable).bitmap
        println(image)
        showCardDialog!!.card_image.setImageBitmap(image)
        showCardDialog!!.show()
    }
}


