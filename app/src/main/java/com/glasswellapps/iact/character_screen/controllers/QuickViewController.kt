package com.glasswellapps.iact.character_screen.controllers

import android.R
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import com.glasswellapps.iact.character_screen.CharacterScreen
import com.glasswellapps.iact.character_screen.ModListAdapter
import com.glasswellapps.iact.effects.Sounds
import com.glasswellapps.iact.inventory.Items
import kotlinx.android.synthetic.main.activity_character_screen.*
import kotlinx.android.synthetic.main.dialog_quick_view.*
import kotlinx.android.synthetic.main.dialog_quick_view_button.*

class QuickViewController (val characterScreen: CharacterScreen){
    private var quickViewDialog: Dialog = Dialog(characterScreen, R.style.Theme_Material_Light_NoActionBar_Fullscreen)
    private var quickViewButtonDialog: Dialog = Dialog(characterScreen)
    private var character = characterScreen.character
    private var weapon:ImageView
    private var weapon1:ImageView
    private var armor:ImageView
    private var acc:ImageView
    private var acc1:ImageView
    private var acc2:ImageView
    private var weaponType:ImageView
    private var weapon1Type:ImageView
    private var armorType:ImageView
    private var accType:ImageView
    private var acc1Type:ImageView
    private var acc2Type:ImageView
    private var mods:TextView
    private var mods1:TextView
    private var showModsButton:FrameLayout
    private var modsGrid:GridView

    init{
        quickViewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        quickViewDialog.setCancelable(false)
        quickViewDialog.setContentView(com.glasswellapps.iact.R.layout.dialog_quick_view)
        quickViewDialog.setCanceledOnTouchOutside(true)
        quickViewDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        character = characterScreen.character
        weapon = quickViewDialog.quick_view_weapon
        weapon1 = quickViewDialog.quick_view_weapon1
        armor = quickViewDialog.quick_view_armor
        acc = quickViewDialog.quick_view_acc
        acc1 = quickViewDialog.quick_view_acc1
        acc2 = quickViewDialog.quick_view_acc2
        weaponType = quickViewDialog.weapon_type
        weapon1Type = quickViewDialog.weapon1_type
        armorType = quickViewDialog.armor_type
        accType = quickViewDialog.acc_type
        acc1Type = quickViewDialog.acc1_type
        acc2Type = quickViewDialog.acc2_type
        mods = quickViewDialog.quickview_mods
        mods1 = quickViewDialog.quickview_mods1
        showModsButton = quickViewDialog.show_mods
        modsGrid = quickViewDialog.mods_grid_view

        characterScreen.character_images.setOnLongClickListener {
            quickViewButtonDialog.show()
            true
        }
        quickViewDialog.quick_view_back.setOnClickListener {
            quickViewDialog.dismiss()
        }

        quickViewButtonDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        quickViewButtonDialog.setCancelable(false)
        quickViewButtonDialog.setContentView(com.glasswellapps.iact.R.layout.dialog_quick_view_button)
        quickViewButtonDialog.setCanceledOnTouchOutside(true)
        quickViewDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        print("WEAPON"+weapon)

        weapon.setOnClickListener { characterScreen.onShowCard((weapon.drawable as BitmapDrawable).bitmap) }
        weapon1.setOnClickListener { characterScreen.onShowCard((weapon1.drawable as BitmapDrawable).bitmap) }
        armor.setOnClickListener { characterScreen.onShowCard((armor.drawable as BitmapDrawable).bitmap) }
        acc.setOnClickListener { characterScreen.onShowCard((acc.drawable as BitmapDrawable).bitmap) }
        acc1.setOnClickListener { characterScreen.onShowCard((acc1.drawable as BitmapDrawable).bitmap) }
        acc2.setOnClickListener { characterScreen.onShowCard((acc2.drawable as BitmapDrawable).bitmap) }

        mods.setOnClickListener {
            Sounds.selectSound()
            showModsButton.visibility = View.VISIBLE
        }
        mods1.setOnClickListener {
            Sounds.selectSound()
            showModsButton.visibility = View.VISIBLE
        }
        showModsButton.setOnClickListener{
            showModsButton.visibility = View.INVISIBLE
        }

        val xp = quickViewDialog.quickview_xp
        xp.setOnClickListener {
            characterScreen.onXPScreen()
        }

        val reward = quickViewDialog.quickview_reward
        reward.setOnClickListener {
            characterScreen.onReward()
        }

        quickViewButtonDialog.quick_view_button.setOnClickListener {
            Sounds.selectSound()
            val weaponIndex = character.weapons.getOrElse(0) { -1 }
            val weaponIndex1 = character.weapons.getOrElse(1) { -1 }
            mods1.alpha = 0f
            var imageId = com.glasswellapps.iact.R.drawable.empty_item_slot
            weaponType.visibility = View.VISIBLE

            if(weaponIndex>0) {
                quickViewDialog.weapon_type.visibility = View.GONE
                if (  weaponIndex == 27) {
                    weapon.setImageBitmap(character.startingMeleeWeapon)
                }
                else if(weaponIndex == 52){
                    weapon.setImageBitmap(character.startingRangedWeapon)

                }
                else {
                    imageId = Items.itemsArray!![weaponIndex].resourceId
                    weapon.setImageResource(imageId)
                }

                if (Items.itemsArray!![weaponIndex].type == Items.ranged) {
                    weaponType.setImageResource(com.glasswellapps.iact.R.drawable.item_ranged)
                } else {
                    weaponType.setImageResource(com.glasswellapps.iact.R.drawable.item_melee)
                }
            }
            else{
                weapon1.setImageResource(imageId)
            }

            imageId = com.glasswellapps.iact.R.drawable.empty_item_slot
            weapon1Type.visibility = View.VISIBLE
            if(weaponIndex1>0){
                weapon1Type.visibility = View.GONE
                if (  weaponIndex1 == 27) {
                    weapon1.setImageBitmap(character.startingMeleeWeapon)
                }
                else if(weaponIndex1 == 52){
                    weapon1.setImageBitmap(character.startingRangedWeapon)

                }
                else{
                    imageId = Items.itemsArray!![weaponIndex1].resourceId
                    weapon1.setImageResource(imageId)
                }

                if(Items.itemsArray!![weaponIndex1].type == Items.ranged){
                    weapon1Type.setImageResource(com.glasswellapps.iact.R.drawable.item_ranged)
                }
                else {
                    weapon1Type.setImageResource(com.glasswellapps.iact.R.drawable.item_melee)
                }
            }
            else{
                weapon1.setImageResource(imageId)
            }
            imageId = com.glasswellapps.iact.R.drawable.empty_item_slot
            armorType.visibility = View.VISIBLE
            val armorIndex = character.armor.getOrElse(0) { -1 }
            if (armorIndex >= 0) {
                imageId = Items.itemsArray!![armorIndex].resourceId
                armorType.visibility = View.GONE
            }
            armor.setImageResource(imageId)


            imageId = com.glasswellapps.iact.R.drawable.empty_item_slot
            accType.visibility = View.VISIBLE
            val accIndex = character.accessories.getOrElse(0) { -1 }
            if (accIndex >= 0) {
                imageId = Items.itemsArray!![accIndex].resourceId
                accType.visibility = View.GONE
            }
            acc.setImageResource(imageId)


            imageId = com.glasswellapps.iact.R.drawable.empty_item_slot
            acc1Type.visibility = View.VISIBLE
            val accIndex1 = character.accessories.getOrElse(1) { -1 }
            if (accIndex1 >= 0) {
                imageId = Items.itemsArray!![accIndex1].resourceId
                acc1.setImageResource(imageId)
                acc1Type.visibility = View.GONE
            }
            acc1.setImageResource(imageId)

            imageId = com.glasswellapps.iact.R.drawable.empty_item_slot
            acc2Type.visibility = View.VISIBLE
            val accIndex2 = character.accessories.getOrElse(2) { -1 }
            if (accIndex2 >= 0) {
                imageId = Items.itemsArray!![accIndex2].resourceId
                acc2Type.visibility = View.GONE
            }
            acc2.setImageResource(imageId)
            modsGrid.adapter = ModListAdapter(characterScreen,character.mods)
            quickViewDialog.show()
            quickViewButtonDialog.dismiss()
        }
    }
}