package com.glasswellapps.iact.loading

import android.app.Activity
import com.glasswellapps.iact.character_screen.CharacterBuilder
import com.glasswellapps.iact.characters.Character
import com.glasswellapps.iact.database.AppDatabase
import com.glasswellapps.iact.database.CharacterData

class LoadingController () {

    companion object {
        lateinit var loadedData: List<CharacterData>

        fun loadData(context: Activity) {
            val database = AppDatabase.getInstance(context)
            loadedData = database!!.getCharacterDAO().getAll();
        }

        fun loadDataByID(ids:IntArray, context: Activity):List<CharacterData>{
            val database = AppDatabase.getInstance(context)
            return database!!.getCharacterDAO().loadAllByIds(ids)
        }
        fun loadCharactersByID(ids:IntArray, context: Activity):Array<Character>{
            val characterData = loadDataByID(ids,context)
            System.out.println("LOAD BY ID "+characterData.size)
            var loadedCharacters = Array<Character>(characterData.size) { i -> Character() }
            for(i in 0 until characterData.size){
                loadedCharacters[i] = loadCharacter(characterData[i],context)
            }
            return loadedCharacters
        }

        private fun getCharacter(characterName: String?, context: Activity):Character {
            var character = com.glasswellapps.iact.characters.Character();

            if(characterName != null){
                character = CharacterBuilder.create(characterName,context)
            }
            return character
        }

        fun loadCharacter(saveData:CharacterData, context: Activity):Character {
            var selectedCharacter = getCharacter(saveData.characterName, context)

            selectedCharacter.file_name = "" + saveData.fileName
            selectedCharacter.id = saveData.id

            selectedCharacter.damage = saveData.damage
            selectedCharacter.strain = saveData.strain
            selectedCharacter.token = saveData.token
            selectedCharacter.wounded = saveData.wounded

            selectedCharacter.conditionsActive = booleanArrayOf(
                saveData.conditionsActive1,
                saveData.conditionsActive2,
                saveData.conditionsActive3,
                saveData.conditionsActive4,
                saveData.conditionsActive5
            )

            selectedCharacter.totalXP = saveData.totalXP
            selectedCharacter.xpSpent = saveData.xpSpent
            selectedCharacter.xpCardsEquipped = booleanArrayOf(
                saveData.xpCardsEquipped1,
                saveData.xpCardsEquipped2,
                saveData.xpCardsEquipped3,
                saveData.xpCardsEquipped4,
                saveData.xpCardsEquipped5,
                saveData.xpCardsEquipped6,
                saveData.xpCardsEquipped7,
                saveData.xpCardsEquipped8,
                saveData.xpCardsEquipped9
            )

            if (saveData.weapon1 != -1) {
                selectedCharacter.weapons.add(saveData.weapon1)
            }
            if (saveData.weapon2 != -1) {
                selectedCharacter.weapons.add(saveData.weapon2)
            }
            if (saveData.accessory1 != -1) {
                selectedCharacter.accessories.add(saveData.accessory1)
            }
            if (saveData.accessory2 != -1) {
                selectedCharacter.accessories.add(saveData.accessory2)
            }
            if (saveData.accessory3 != -1) {
                selectedCharacter.accessories.add(saveData.accessory3)
            }
            selectedCharacter.helmet = saveData.helmet
            if (saveData.armour != -1) {
                selectedCharacter.armor.add(saveData.armour)
            }


            //TODO rewards and mods
            selectedCharacter.rewards = convertStringToItemID("" + saveData.rewards)
            selectedCharacter.mods = convertStringToItemID("" + saveData.mods)


            selectedCharacter.background = saveData.background.toString()

            selectedCharacter.killCount = arrayOf(
                saveData.killVillian,
                saveData.killLeader,
                saveData.killVehicle,
                saveData.killCreature,
                saveData.killGuard,
                saveData.killDroid,
                saveData.killScum,
                saveData.killTrooper
            )

            selectedCharacter.assistCount = arrayOf(
                saveData.assistVillian,
                saveData.assistLeader,
                saveData.assistVehicle,
                saveData.assistCreature,
                saveData.assistGuard,
                saveData.assistDroid,
                saveData.assistScum,
                saveData.assistTrooper
            )

            selectedCharacter.movesTaken = saveData.moves
            selectedCharacter.attacksMade = saveData.attacks
            selectedCharacter.interactsUsed = saveData.interact
            selectedCharacter.timesWounded = saveData.timesWounded
            selectedCharacter.timesRested = saveData.rested
            selectedCharacter.timesWithdrawn = saveData.timesWithdrawn
            selectedCharacter.activated = saveData.activated
            selectedCharacter.damageTaken = saveData.damageTaken
            selectedCharacter.strainTaken = saveData.strainTaken
            selectedCharacter.specialActions = saveData.special

            selectedCharacter.timesFocused = saveData.timesFocused
            selectedCharacter.timesHidden = saveData.timesHidden
            selectedCharacter.timesStunned = saveData.timesStunned
            selectedCharacter.timesBleeding = saveData.timesBleeding
            selectedCharacter.timesWeakened = saveData.timesWeakened
            selectedCharacter.cratesPickedUp = saveData.cratesPickedUp
            selectedCharacter.rewardObtained = saveData.rewardObtained
            selectedCharacter.withdrawn = saveData.withdrawn

            selectedCharacter.damageAnimSetting = saveData.damageSetting
            selectedCharacter.soundEffectsSetting = saveData.soundEffectsSetting
            selectedCharacter.actionUsageSetting = saveData.actionUsageSetting
            selectedCharacter.imageSetting = saveData.imageSetting
            selectedCharacter.conditionAnimSetting = saveData.conditionAnimSetting

            return selectedCharacter;
        }
        fun convertStringToItemID(itemString: String): ArrayList<Int> {
            var itemIds = arrayListOf<Int>()
            var itemStrings =
                itemString.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            for (itemId in itemStrings) {
                if (itemId.isNotEmpty()) {
                    itemIds.add(itemId.toInt())
                }
            }
            return itemIds
        }
    }





}