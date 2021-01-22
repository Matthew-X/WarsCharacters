package com.example.imperialassault

import androidx.room.*

@Entity
data class CharacterData (
    @ColumnInfo(name = "fileName") val fileName: String?,

    @ColumnInfo(name = "damage") val damage: Int,
    @ColumnInfo(name = "strain") val strain: Int,
    @ColumnInfo(name = "token") val token: Int,
    @ColumnInfo(name = "wounded") val wounded: Int,

    @ColumnInfo(name = "totalXP") val totalXP: Int,
    @ColumnInfo(name = "xpSpent") val xpSpent: Int,

    @ColumnInfo(name = "xpCardsEquipped1") val xpCardsEquipped1: Boolean,
    @ColumnInfo(name = "xpCardsEquipped2") val xpCardsEquipped2: Boolean,
    @ColumnInfo(name = "xpCardsEquipped3") val xpCardsEquipped3: Boolean,

    @ColumnInfo(name = "xpCardsEquipped4") val xpCardsEquipped4: Boolean,
    @ColumnInfo(name = "xpCardsEquipped5") val xpCardsEquipped5: Boolean,
    @ColumnInfo(name = "xpCardsEquipped6") val xpCardsEquipped6: Boolean,

    @ColumnInfo(name = "xpCardsEquipped7") val xpCardsEquipped7: Boolean,
    @ColumnInfo(name = "xpCardsEquipped8") val xpCardsEquipped8: Boolean,
    @ColumnInfo(name = "xpCardsEquipped9") val xpCardsEquipped9: Boolean,

    @ColumnInfo(name = "weapon1") val weapon1: String?,
    @ColumnInfo(name = "weapon2") val weapon2: String?,
    @ColumnInfo(name = "accessory1") val accessory1: String?,
    @ColumnInfo(name = "accessory2") val accessory2: String?,
    @ColumnInfo(name = "accessory3") val accessory3: String?,
    @ColumnInfo(name = "helmet") val helmet: String?,
    @ColumnInfo(name = "armour") val armour: String?,

    @ColumnInfo(name = "background") val background: String?,

    @ColumnInfo(name = "focused") val focused: Boolean,
    @ColumnInfo(name = "hidden") val hidden: Boolean,
    @ColumnInfo(name = "stunned") val stunned: Boolean,
    @ColumnInfo(name = "bleeding") val bleeding: Boolean,
    @ColumnInfo(name = "weakened") val weakened: Boolean,

    @ColumnInfo(name = "killVillain") val killVillian: Int,
    @ColumnInfo(name = "killLeader") val killLeader: Int,
    @ColumnInfo(name = "killVehicle") val killVehicle: Int,
    @ColumnInfo(name = "killCreature") val killCreature: Int,
    @ColumnInfo(name = "killGuard") val killGuard: Int,
    @ColumnInfo(name = "killDroid") val killDroid: Int,
    @ColumnInfo(name = "killScum") val killScum: Int,
    @ColumnInfo(name = "killTrooper") val killTrooper: Int,

    @ColumnInfo(name = "assistVillain") val assistVillian: Int,
    @ColumnInfo(name = "assistLeader") val assistLeader: Int,
    @ColumnInfo(name = "assistVehicle") val assistVehicle: Int,
    @ColumnInfo(name = "assistCreature") val assistCreature: Int,
    @ColumnInfo(name = "assistGuard") val assistGuard: Int,
    @ColumnInfo(name = "assistDroid") val assistDroid: Int,
    @ColumnInfo(name = "assistScum") val assistScum: Int,
    @ColumnInfo(name = "assistTrooper") val assistTrooper: Int,

    @ColumnInfo(name = "moves") val moves: Int,
    @ColumnInfo(name = "attacks") val attacks: Int,
    @ColumnInfo(name = "interact") val interact: Int,
    @ColumnInfo(name = "timesWounded") val timesWounded: Int,
    @ColumnInfo(name = "rested") val rested: Int,
    @ColumnInfo(name = "withdrawn") val withdrawn: Int,
    @ColumnInfo(name = "activated") val activated: Int,
    @ColumnInfo(name = "damageTaken") val timesDamaged: Int,
    @ColumnInfo(name = "strainTaken") val strainTaken: Int,
    @ColumnInfo(name = "special") val special: Int,

    @ColumnInfo(name = "timesFocused") val timesFocused: Int,
    @ColumnInfo(name = "timesHidden") val timesHidden: Int,
    @ColumnInfo(name = "timesStunned") val timesStunned: Int,
    @ColumnInfo(name = "timesBleeding") val timesBleeding: Int,
    @ColumnInfo(name = "timesWeakened") val timesWeakened: Int,
    @ColumnInfo(name = "cratesPickedUp") val cratesPickedUp: Int,
)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}