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
import kotlinx.android.synthetic.main.activity_acc_screen.*
import kotlinx.android.synthetic.main.dialog_show_card.*
import kotlinx.android.synthetic.main.toast.view.*

class AccScreen : AppCompatActivity() {
    val character = CharacterHolder.getActiveCharacter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acc_screen)
        var accViews = ArrayList<ImageView>()

        if(character == null){
            finish()
        }

        accViews.add(this.acc_image0)
        accViews.add(this.acc_image1)
        accViews.add(this.acc_image2)
        accViews.add(this.acc_image3)
        accViews.add(this.acc_image4)
        accViews.add(this.acc_image5)
        accViews.add(this.acc_image6)
        accViews.add(this.acc_image7)
        accViews.add(this.acc_image8)
        accViews.add(this.acc_image9)
        accViews.add(this.acc_image10)
        accViews.add(this.acc_image11)
        accViews.add(this.acc_image12)
        accViews.add(this.acc_image13)
        accViews.add(this.acc_image14)
        accViews.add(this.acc_image15)
        accViews.add(this.acc_image16)
        accViews.add(this.acc_image17)
        accViews.add(this.acc_image18)
        accViews.add(this.acc_image19)
        accViews.add(this.acc_image20)

        for(i in 0..Items.accArray!!.size-1){
            var currentItem = Items.accArray!!.get(i)
            val gridItem = accViews[i]
            if(currentItem.type>=0) {
            gridItem.alpha = 0.5f
                gridItem.setImageResource(currentItem.resourceId)
                setClickables(gridItem, currentItem)
                if (character.accessories.contains(currentItem.index)) {
                    gridItem.alpha = 1f
                }

            }
            else{
                (gridItem.parent as View).visibility = View.INVISIBLE
            }
        }


        to_melee.setBackgroundColor(resources.getColor(R.color.shadow))
        to_armor.setBackgroundColor(resources.getColor(R.color.shadow))
        to_ranged.setBackgroundColor(resources.getColor(R.color.shadow))


        showCardDialog = Dialog(this)
        showCardDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        showCardDialog!!.setContentView(R.layout.dialog_show_card)
        showCardDialog!!.setCancelable(false)
        showCardDialog!!.setCanceledOnTouchOutside(true)
        showCardDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        showCardDialog.show_card_dialog.setOnClickListener {
            Sounds.selectSound()
            showCardDialog.dismiss()
        }
    }

    fun setClickables(gridItem:ImageView, currentItem: Item) {
        gridItem.setOnLongClickListener {
            onShowCard(gridItem)
            true
        }

        gridItem.setOnClickListener {
            gridItem.alpha = equipAcc(currentItem)
            if(gridItem.alpha==1f) {
                Sounds.equipSound(this, currentItem.soundType)
            }
            //TODO onDROID
        }
    }

    fun equipAcc(item: Item): Float {
        if(!CharacterHolder.getIsInteractable()){
            Sounds.negativeSound()
            return 0.5f;
        }
        if (character.accessories.remove(item.index)) {
            if (item.index == Items.mandoHelmetIndex || item.index == Items.reinforcedHelmetIndex){
                character.helmet = false
            }
            Sounds.selectSound()


            return 0.5f
        }
        //equip if slot available
        if (character.accessories.size < 3) {

            if (item.index == Items.mandoHelmetIndex || item.index == Items.reinforcedHelmetIndex) {
                if (!character.helmet) {
                    character.helmet = true
                    character.accessories.add(item.index)
                    if(character.name_short.equals("biv")) {
                        character.changeRandom = true
                    }
                    return 1f
                }
                else{
                    showItemLimitReached(-1)
                    return 0.5f
                }
            }
            else {
                character.accessories.add(item.index)
                return 1f
            }
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
            Items.acc -> view.toast_text.setText("3 accessory limit reached")
            -1 -> view.toast_text.setText("1 helmet limit reached")
        }

        toast!!.view = view
        toast!!.setGravity(Gravity.CENTER, 0, 0)
        toast!!.show()
    }

    lateinit var showCardDialog: Dialog
    fun onShowCard(view: ImageView) {
        var image = ((view).drawable as BitmapDrawable).bitmap
        showCardDialog.card_image.setImageBitmap(image)
        showCardDialog.show()
    }

    fun onToAcc(view:View){

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