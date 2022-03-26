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
import kotlinx.android.synthetic.main.activity_melee_screen.*
import kotlinx.android.synthetic.main.dialog_show_card.*
import kotlinx.android.synthetic.main.toast.view.*

class MeleeScreen : AppCompatActivity() {
    val character = CharacterHolder.getActiveCharacter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_melee_screen)

        if(character == null){
            finish()
        }

        to_ranged.setBackgroundColor(resources.getColor(R.color.shadow))
        to_armor.setBackgroundColor(resources.getColor(R.color.shadow))
        to_acc.setBackgroundColor(resources.getColor(R.color.shadow))


  var meleeViews = ArrayList<ImageView>()
        meleeViews.add(this.melee_image0)
        meleeViews.add(this.melee_image1)
        meleeViews.add(this.melee_image2)
        meleeViews.add(this.melee_image3)
        meleeViews.add(this.melee_image4)
        meleeViews.add(this.melee_image5)
        meleeViews.add(this.melee_image6)
        meleeViews.add(this.melee_image7)
        meleeViews.add(this.melee_image8)
        meleeViews.add(this.melee_image9)
        meleeViews.add(this.melee_image10)
        meleeViews.add(this.melee_image11)
        meleeViews.add(this.melee_image12)
        meleeViews.add(this.melee_image13)
        meleeViews.add(this.melee_image14)
        meleeViews.add(this.melee_image15)
        meleeViews.add(this.melee_image16)
        meleeViews.add(this.melee_image17)
        meleeViews.add(this.melee_image18)
        meleeViews.add(this.melee_image19)
        meleeViews.add(this.melee_image20)
        meleeViews.add(this.melee_image21)
        meleeViews.add(this.melee_image22)
        meleeViews.add(this.melee_image23)
        meleeViews.add(this.melee_image24)
        meleeViews.add(this.melee_image25)
        meleeViews.add(this.melee_image26)
        meleeViews.add(this.melee_image27)
        meleeViews.add(this.melee_image28)
        meleeViews.add(this.melee_image29)
        meleeViews.add(this.melee_image30)
        meleeViews.add(this.melee_image31)
        meleeViews.add(this.melee_image32)

        for(i in 0..Items.meleeArray!!.size-1){
            var currentItem = Items.meleeArray!!.get(i)
            val gridItem = meleeViews[i]
            if(currentItem.type>=0) {
                gridItem.alpha = 0.5f


                if(i == 0) {
                    if (character.startingMeleeWeapon != null) {
                        gridItem.setImageBitmap(character.startingMeleeWeapon)
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

                Items.melee -> {
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


    open fun onToAcc(view:View){
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
        //val intent = Intent(this, MeleeScreen::class.java)
        //startActivity(intent)
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