package com.example.imperialassault

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewConfiguration
import android.view.Window
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_armor_screen.*
import kotlinx.android.synthetic.main.dialog_show_card.*
import kotlinx.android.synthetic.main.toast_no_actions_left.view.*

class ArmorScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_armor_screen)

        to_melee.setBackgroundColor(resources.getColor(R.color.shadow))
        to_ranged.setBackgroundColor(resources.getColor(R.color.shadow))
        to_acc.setBackgroundColor(resources.getColor(R.color.shadow))


        var armorViews = ArrayList<ImageView>()
        armorViews.add(this.armor_image0)
        armorViews.add(this.armor_image1)
        armorViews.add(this.armor_image2)
        armorViews.add(this.armor_image3)
        armorViews.add(this.armor_image4)
        armorViews.add(this.armor_image5)
        armorViews.add(this.armor_image6)
        armorViews.add(this.armor_image7)
        armorViews.add(this.armor_image8)

        for(i in 0..Items.armorArray!!.size-1){
            var currentItem = Items.armorArray!!.get(i)
            val gridItem = armorViews[i]
            if(currentItem.type>=0) {
            gridItem.alpha = 0.5f
            var character = MainActivity.selectedCharacter!!

                gridItem.setImageResource(currentItem.resourceId)
                setClickables(gridItem, currentItem)


                if (character.weapons.contains(currentItem.index)) {
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

    fun setClickables(gridItem:ImageView, currentItem:Item) {
        gridItem.setOnLongClickListener {
            onShowCard(gridItem)
            true
        }

        gridItem.setOnClickListener {
            gridItem.alpha = equipArmor(currentItem)
            if(gridItem.alpha==1f) {
                Sounds.equipSound(this, currentItem.soundType)
            }
        }
    }

    fun equipArmor(item: Item): Float {
        var character = MainActivity.selectedCharacter!!

        //remove if already equipped
        if (character.armor.remove(item.index)) {
            Sounds.selectSound()
            return 0.5f
        }

        //equip if slot available
        if (character.armor.size < 1) {
            character.armor.add(item.index)
            return 1f
        }
        //TODO toast 1 armor limit reached
        showItemLimitReached()

        //not equipped
        return 0.5f
    }

    private fun showItemLimitReached() {
        Sounds.negativeSound()
        val toast = Toast(this)
        toast!!.duration = Toast.LENGTH_SHORT
        val view = this.layoutInflater.inflate(
            R.layout.toast_no_actions_left,
            null,
            false
        )
        view.toast_text.setText("1 armor limit reached")
        toast!!.view = view
        toast!!.setGravity(Gravity.CENTER, 0, 0)
        toast!!.show()
    }

    fun onToAcc(view:View){
        Sounds.selectSound()
        val intent = Intent(this, AccScreen::class.java)
        startActivity(intent)
        finish()
    }

    fun onToArmor(view:View){
        Sounds.selectSound()
        //val intent = Intent(this, ArmorScreen::class.java)
        //startActivity(intent)
    }

    fun onToMelee(view:View){
        Sounds.selectSound()
        val intent = Intent(this, MeleeScreen::class.java)
        startActivity(intent)
        finish()
    }

    fun onToRanged(view:View){
        Sounds.selectSound()
        val intent = Intent(this, RangedScreen::class.java)
        startActivity(intent)
        finish()
    }


    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}