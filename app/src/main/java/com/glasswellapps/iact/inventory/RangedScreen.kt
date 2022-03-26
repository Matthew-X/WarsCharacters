package com.glasswellapps.iact.inventory

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import com.glasswellapps.iact.*
import com.glasswellapps.iact.effects.Sounds
import com.glasswellapps.iact.loading.CharacterHolder

import kotlinx.android.synthetic.main.activity_ranged_screen.*
import kotlinx.android.synthetic.main.dialog_show_card.*
import kotlinx.android.synthetic.main.toast.view.*

class RangedScreen : AppCompatActivity() {
    val character = CharacterHolder.getActiveCharacter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranged_screen)

        if(character == null) {
            finish();
        }
        to_melee.setBackgroundColor(resources.getColor(R.color.shadow))
        to_armor.setBackgroundColor(resources.getColor(R.color.shadow))
        to_acc.setBackgroundColor(resources.getColor(R.color.shadow))

        var rangedViews = ArrayList<ImageView>()
        rangedViews.add(this.ranged_image0)
        rangedViews.add(this.ranged_image1)
        rangedViews.add(this.ranged_image2)
        rangedViews.add(this.ranged_image3)
        rangedViews.add(this.ranged_image4)
        rangedViews.add(this.ranged_image5)
        rangedViews.add(this.ranged_image6)
        rangedViews.add(this.ranged_image7)
        rangedViews.add(this.ranged_image8)
        rangedViews.add(this.ranged_image9)
        rangedViews.add(this.ranged_image10)
        rangedViews.add(this.ranged_image11)
        rangedViews.add(this.ranged_image12)
        rangedViews.add(this.ranged_image13)
        rangedViews.add(this.ranged_image14)
        rangedViews.add(this.ranged_image15)
        rangedViews.add(this.ranged_image16)
        rangedViews.add(this.ranged_image17)
        rangedViews.add(this.ranged_image18)
        rangedViews.add(this.ranged_image19)
        rangedViews.add(this.ranged_image20)
        rangedViews.add(this.ranged_image21)
        rangedViews.add(this.ranged_image22)
        rangedViews.add(this.ranged_image23)
        rangedViews.add(this.ranged_image24)
        rangedViews.add(this.ranged_image25)
        rangedViews.add(this.ranged_image26)
        rangedViews.add(this.ranged_image27)
        rangedViews.add(this.ranged_image28)
        rangedViews.add(this.ranged_image29)
        rangedViews.add(this.ranged_image30)
        rangedViews.add(this.ranged_image31)
        rangedViews.add(this.ranged_image32)
        rangedViews.add(this.ranged_image33)
        rangedViews.add(this.ranged_image34)
        rangedViews.add(this.ranged_image35)
        rangedViews.add(this.ranged_image36)
        rangedViews.add(this.ranged_image37)
        rangedViews.add(this.ranged_image38)
        rangedViews.add(this.ranged_image39)
        rangedViews.add(this.ranged_image40)
        rangedViews.add(this.ranged_image41)

        for(i in 0..Items.rangedArray!!.size-1){
            var currentItem = Items.rangedArray!!.get(i)
            val gridItem = rangedViews[i]
            if(currentItem.type>=0) {
                gridItem.alpha = 0.5f
                if(i == 0) {
                    if (character.startingRangedWeapon != null) {
                        gridItem.setImageBitmap(character.startingRangedWeapon)
                        setClickables(gridItem, currentItem)
                    } else {
                        gridItem.setImageResource(currentItem.resourceId)
                        gridItem.alpha = 1f
                    }
                }
                else{
                    gridItem.setImageResource(currentItem.resourceId)
                    setClickables(gridItem, currentItem)
                }
                if (character.weapons.contains(currentItem.index) || character.mods.contains(currentItem.index)) {
                    gridItem.alpha = 1f
                }
            }
            else{
                (gridItem.parent as View).visibility = View.INVISIBLE
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
                Items.ranged -> {
                    gridItem.alpha = equipWeapon(currentItem)
                }
                Items.mod -> {
                    gridItem.alpha = equipMod(currentItem)
                }
            }
            if(gridItem.alpha==1f) {
                Sounds.equipSound(this, currentItem.soundType)
            }
        }
    }

    fun equipWeapon(item: Item): Float {
        if(!CharacterHolder.getIsInteractable()){
            Sounds.negativeSound()
            return 0.5f;
        }
        //remove if already equipped
        if (character.weapons.remove(item.index)) {
            Sounds.selectSound()
            return 0.5f
        }
        //equip if slot available
        if (character.weapons.size < 2) {
            character.weapons.add(item.index)
            return 1f
        }
        showItemLimitReached()
        return 0.5f
    }
    fun equipMod(item: Item): Float {
        if(!CharacterHolder.getIsInteractable()){
            Sounds.negativeSound()
            return 0.5f;
        }
        //remove if already equipped
        if (character.mods.remove(item.index)) {
            Sounds.selectSound()
            return 0.5f
        }
        //equip if not equipped
        character.mods.add(item.index)
        return 1f
    }

    private fun showItemLimitReached() {
        Sounds.negativeSound()
        val toast = Toast(this)
        toast!!.duration = Toast.LENGTH_SHORT
        val view = this.layoutInflater.inflate(
            R.layout.toast,
            null,
            false
        )
        view.toast_text.setText("2 weapon limit reached")
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


    fun onToAcc(view:View){
        Sounds.selectSound()
        val intent = Intent(this, AccScreen::class.java)
        startActivity(intent)
        finish()
    }

    fun onToArmor(view:View){
        Sounds.selectSound()
        val intent = Intent(this, ArmorScreen::class.java)
        startActivity(intent)
        finish()
    }

    fun onToMelee(view:View){
        Sounds.selectSound()
        val intent = Intent(this, MeleeScreen::class.java)
        startActivity(intent)
        finish()
    }

    fun onToRanged(view:View){
        //val intent = Intent(this, RangedScreen::class.java)
        //startActivity(intent)
    }


    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }


}