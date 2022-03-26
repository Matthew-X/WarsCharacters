package com.glasswellapps.iact.characters

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.widget.Toast
import com.glasswellapps.iact.inventory.Item
import com.glasswellapps.iact.inventory.Items
import com.glasswellapps.iact.R
import com.glasswellapps.iact.effects.Sounds
import kotlinx.android.synthetic.main.toast.view.*
import java.io.InputStream

open class Character {
    var name = ""
    var name_short = ""
    var index = 0
    var file_name = "autosave"
    var id = -1

    var type = ""
    var defence_dice = ""

    var strength = "RGB"
    var insight = "RGB"
    var tech = "RGB"

    var strengthWounded = "RGB"
    var insightWounded = "RGB"
    var techWounded = "RGB"


    var health_default = 10
    var endurance_default = 5
    var speed_default = 5

    var health = 10
    var endurance = 5
    var speed = 4

    var xpScores = intArrayOf(1,1,2,2,3,3,4,4,0)
    var xpEndurances: IntArray = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    var xpHealths: IntArray = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    var xpSpeeds: IntArray = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    var xpItems = intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1,-1)

    var bio_title = ""
    var bio_quote = ""
    var bio_text = ""

    var power:Bitmap?=null
    var power_wounded:Bitmap?=null

    //Images
    var currentImage:Bitmap? = null
    var layer2:Bitmap? = null
    var layer1:Bitmap? = null
    var layerLightSaber: Bitmap? = null
    var companionImage:Bitmap? = null
    var glowImage:Bitmap?=null

    var startingMeleeWeapon:Bitmap?=null
    var startingRangedWeapon:Bitmap?=null

    var ancientLightSaber = false
    var combatVisor= false
    var mandoHelmet = false
    var reinforcedHelmet = false
    var astromech = false


    var tier = 0
    var imageSetting = -1 //-1 = auto, 0 = default, 1 = tier1, 2 = tier2, 3 = tier3

    var xpCardImages  = ArrayList<Bitmap>()
    var companionCard:Bitmap? = null
    var portraitImage:Drawable? = null
    var portraitRow = 0
    var portraitCol = 0

    //immediate state
    var actionsLeft = 0
    var isActivated = false
    var isWounded = false;
    var lastEffect = 0;

    //****************************************************************************************************
    //region To Save
    //****************************************************************************************************
    var damage = 0
    var strain = 0
    var token = 0
    var wounded = 0

    var conditionsActive = booleanArrayOf(false,false,false,false,false)

    var totalXP = 0
    var xpSpent = 0
    var xpCardsEquipped: BooleanArray= booleanArrayOf(false,false,false,false,false,false,false,false,false)

    var weapons = arrayListOf<Int>()
    var accessories = arrayListOf<Int>()
    var helmet = false
    var armor = arrayListOf<Int>()
    var rewards  = arrayListOf<Int>()
    var mods = arrayListOf<Int>()

    var background = "interior"

    //stats
    // villain, leader, vehicle,creature,  guard, droid, scum, trooper
    var killCount = arrayOf(0,0,0,0,0,0,0,0)
    var assistCount = arrayOf(0,0,0,0,0,0,0,0)

    var movesTaken = 0
    var attacksMade = 0
    var interactsUsed = 0
    var timesWounded = 0
    var timesRested = 0
    var timesWithdrawn = 0
    var activated = 0
    var damageTaken = 0
    var strainTaken = 0
    var specialActions = 0
    var timesFocused = 0
    var timesHidden = 0
    var timesStunned = 0
    var timesBleeding = 0
    var timesWeakened = 0
    var cratesPickedUp = 0

    var withdrawn = false
    var rewardObtained = false


    var changeRandom = false
    var storeRandom = 0.0

    //endregion
    //****************************************************************************************************

    open fun loadImages(context: Context){
        //loadTierImages(context)

        loadXPCardImages(context)
        loadPowerImages(context)
        startingRangedWeapon = loadStartingWeaponRanged(context)
        startingMeleeWeapon = loadStartingWeaponMelee(context)


    }

    open fun loadPowerImages(context: Context) {
        power = getBitmap(context,"characters/" + name_short + "/power.png")
        power_wounded = getBitmap(context,"characters/" + name_short + "/power_wounded.png")
    }

    open fun loadTierImage(context: Context, tier:Int){
        val image = getBitmap(context, "characters/" + name_short + "/images/tier" + tier + "image.png")
        currentImage = image
    }

    open fun loadXPCardImages(context: Context){
        val images = java.util.ArrayList<Bitmap>()
        for (i in 1..9) {
            val image = getBitmap(context, "characters/" + name_short + "/xpcards/card" + i + ".jpg")
            if (image != null) {
                images.add(image)
            }
            else{
                images.add(BitmapFactory.decodeResource(context.resources, R.drawable.empty_item_slot))
            }
        }
        xpCardImages = images
    }

    fun loadStartingWeaponRanged(context: Context):Bitmap?{
        return getBitmap(context, "characters/" + name_short + "/xpcards/starting_weapon_ranged.jpg")
    }

    fun loadStartingWeaponMelee(context: Context):Bitmap?{
        return getBitmap(context, "characters/" + name_short + "/xpcards/starting_weapon_melee.jpg")
    }

    open fun getBackgroundImage(context: Context): Bitmap? {
        val image = getBitmap(context, "backgrounds/background_"+ background + ".jpg")
        return image;
    }
    open fun getCamoImage(context: Context): Bitmap? {
        val image = getBitmap(context, "backgrounds/camo_"+ background + ".png")
        return image;
    }

    open fun loadPortraitImage(context:Context){
        portraitImage = context.resources.getDrawable(R.drawable.portrait_fenn)
    }


    open fun getBitmap(context: Context, path: String): Bitmap? {
        val assetManager = context.assets
        var inputStream: InputStream? = null
        var bitmap:Bitmap? = null
        val options = BitmapFactory.Options()
        for(i in 1..32) {
            try {
                inputStream = assetManager.open(path)
                options.inSampleSize = i
                bitmap = BitmapFactory.decodeStream(inputStream,null,options)
                break
            } catch (outOfMemoryError:OutOfMemoryError) {

                //println("next size"+i)
            } catch (e: Exception) {
                //e.printStackTrace()
            }
        }

        try {
            inputStream?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

    open fun loadCardTierImage(context: Context, tier:Int, cards:String):Bitmap?{
        val image = getBitmap(context, "characters/" + name_short + "/images/tier" + tier +
                "image_"+cards+".png")
        return image
    }

    open fun clearImages(){
        power?.recycle()
        power_wounded?.recycle()
        currentImage?.recycle()
        layer2?.recycle()
        layer1?.recycle()
        layerLightSaber?.recycle()
        companionImage?.recycle()
        glowImage?.recycle()
        startingMeleeWeapon?.recycle()
        startingRangedWeapon?.recycle()

        power=null
        power_wounded=null
        currentImage=null
        layer2=null
        layer1=null
        layerLightSaber=null
        companionImage=null
        glowImage=null
        startingMeleeWeapon=null
        startingRangedWeapon=null
    }

    open fun updateCharacterImages(context:Context){
        ancientLightSaber = false
        for(i in 0..weapons.size-1){
            var index = weapons[i]
            ancientLightSaber = (index == Items.ancientLightSaberIndex)||ancientLightSaber
        }
        combatVisor= false
        mandoHelmet = false
        reinforcedHelmet = false
        astromech = false
        for(i in 0..accessories.size-1){
            var index = accessories[i]
            reinforcedHelmet = (index == Items.reinforcedHelmetIndex)||reinforcedHelmet
            mandoHelmet = (index == Items.mandoHelmetIndex)||mandoHelmet
            combatVisor = (index == Items.combatVisorIndex)||combatVisor
            astromech = (index == Items.astromechIndex)||astromech
        }


        var oldTier = tier

        if(imageSetting == -1){
            calculateTier()
        }
        else{
            tier = imageSetting
        }

        if(name_short.equals("verena") || name_short.equals("ct1701")){
            if(tier != oldTier){
                updateRandom()
            }
        }

        loadTierImage(context,tier)

        updateRandom()
    }

    fun calculateTier(){
        var tier1Equipped = 0
        var tier2Equipped = 0
        var tier3Equipped = 0

        for(i in 0..weapons.size-1){
            var index = weapons[i]
            var item: Item
            item = Items.itemsArray!![index]

            when(item.tier){
                1->tier1Equipped++
                2->tier2Equipped++
                3->tier3Equipped++
            }
        }
        for(i in 0..armor.size-1){
            var index = armor[i]
            var item: Item
            item = Items.itemsArray!![index]
            when(item.tier){
                1->tier1Equipped++
                2->tier2Equipped++
                3->tier3Equipped++
            }
        }
        for(i in 0..accessories.size-1){
            var index = accessories[i]


            var item: Item
            item = Items.itemsArray!![index]

            when(item.tier){
                1->tier1Equipped++
                2->tier2Equipped++
                3->tier3Equipped++
            }
        }
        tier = 0

        if((xpSpent>=9 && (tier2Equipped >=1|| tier3Equipped >=1))||xpSpent>=11){
            tier = 3
        }
        else if((xpSpent>=6 && (tier2Equipped >=1 || tier3Equipped >=1))||xpSpent >= 8){
            tier = 2
        }
        else if((xpSpent>=3 && (tier1Equipped >=1 || tier2Equipped >=1 || tier3Equipped >=1)) ||xpSpent >= 5) {
            tier = 1
        }
    }

    fun updateRandom(){
        if(changeRandom) {
            changeRandom = false
            storeRandom = Math.random()
        }
    }

    fun equipXP(cardNo:Int, context: Activity){

        if(xpItems[cardNo]>0){
            var xpItem = Items.itemsArray!![xpItems[cardNo]]
            when(xpItem.type){
                Items.melee ->{
                    if(weapons.size<2){
                        weapons.add(xpItem.index);
                        xpCardsEquipped[cardNo] = true
                        xpSpent += xpScores[cardNo]
                        Sounds.equipSound(context, xpItem.soundType)
                    }
                    else{
                        showItemLimitReached(xpItem.type,context)
                    }
                }
                Items.ranged ->{
                    if(weapons.size<2){
                        weapons.add(xpItem.index)
                        xpCardsEquipped[cardNo] = true
                        xpSpent += xpScores[cardNo]
                        Sounds.equipSound(context, xpItem.soundType)
                    }
                    else{
                        showItemLimitReached(xpItem.type,context)
                    }
                }
                Items.armor ->{
                    if(armor.size<1){
                        armor.add(xpItem.index)
                        xpCardsEquipped[cardNo] = true
                        xpSpent += xpScores[cardNo]
                        Sounds.equipSound(context, xpItem.soundType)
                    }
                    else{
                        showItemLimitReached(xpItem.type,context)
                    }
                }
                Items.acc ->{
                    if(accessories.size<3){
                        accessories.add(xpItem.index)
                        xpCardsEquipped[cardNo] = true
                        xpSpent += xpScores[cardNo]
                        Sounds.equipSound(context, xpItem.soundType)
                    }
                    else{
                        showItemLimitReached(xpItem.type,context)
                    }
                }
                Items.mod ->{
                    mods.add(xpItem.index)
                    xpCardsEquipped[cardNo] = true
                    xpSpent += xpScores[cardNo]
                }
            }
        }
        else{
            xpCardsEquipped[cardNo] = true
            xpSpent += xpScores[cardNo]
        }
    }
    fun unequipXP(cardNo:Int){
        xpCardsEquipped[cardNo] = false
        xpSpent -= xpScores[cardNo]
        if(xpItems[cardNo]>0){
            var xpItem = Items.itemsArray!![xpItems[cardNo]]
            when(xpItem.type){
                Items.melee ->{if(weapons.contains(xpItem.index)){weapons.remove(xpItem.index)}}
                Items.ranged ->{if(weapons.contains(xpItem.index)){weapons.remove(xpItem.index)}}
                Items.armor ->{if(armor.contains(xpItem.index)){armor.remove(xpItem.index)}}
                Items.acc ->{if(accessories.contains(xpItem.index)){accessories.remove(xpItem.index)}}
                Items.mod ->{if(mods.contains(xpItem.index)){mods.remove(xpItem.index)}}
            }
        }
    }


    private fun showItemLimitReached(itemType : Int,context: Activity) {
        Sounds.negativeSound()
        val toast = Toast(context)
        toast!!.duration = Toast.LENGTH_SHORT
        val view = context.layoutInflater.inflate(
            R.layout.toast,
            null,
            false
        )
        when(itemType){
            Items.melee -> view.toast_text.setText("2 weapon limit reached")
            Items.ranged -> view.toast_text.setText("2 weapon limit reached")
            Items.armor -> view.toast_text.setText("1 armor limit reached")
            Items.acc -> view.toast_text.setText("3 accessory limit reached")
            Items.reward -> view.toast_text.setText("3 accessory limit reached")

        }

        toast!!.view = view
        toast!!.setGravity(Gravity.CENTER, 0, 0)
        toast!!.show()
    }

    fun getLevel(): Int {
        var level = 5
        when {
            xpSpent <= 1 -> {
                level = 1
            }
            xpSpent <= 4 -> {
                level = 2
            }
            xpSpent <= 7 -> {
                level = 3
            }
            xpSpent <= 9 -> {
                level = 4
            }
        }
        return level
    }

    var damageAnimSetting = true
    var conditionAnimSetting = true
    var actionUsageSetting = true
   var soundEffectsSetting = 1f

}