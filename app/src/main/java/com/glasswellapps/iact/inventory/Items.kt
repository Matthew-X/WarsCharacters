package com.glasswellapps.iact.inventory
import com.glasswellapps.iact.R
import com.glasswellapps.iact.effects.Sounds

object Items {
    var rewardsArray: Array<Item>? = null
    var accArray: Array<Item>? = null
    var armorArray: Array<Item>? = null
   var meleeArray: Array<Item>? = null
       var rangedArray: Array<Item>? = null

    var itemsArray: Array<Item>? = null

    //types
    const val reward = 0
    const val acc = 1
    const val armor = 2
    const val melee = 3
    const val ranged = 4
    const val mod = 5
    const val xp = 6


    const val tier1 = -1
    const val tier2 = -2
    const val tier3 = -3
    const val mods = -4
    const val empty = -5
    const val space = -6



    //image change items
    const val mandoHelmetIndex = 7
    const val astromechIndex = 8
    const val combatVisorIndex = 11
    const val reinforcedHelmetIndex = 16
    const val ancientLightSaberIndex = 45

    const val heroic = 5
    const val legendary = 11

    init {
        itemsArray = arrayOf(
            //0
            Item(acc, 1, acc, R.drawable.acc_t1_bactapump,""),
            Item(acc, 1, acc, R.drawable.acc_t1_combatvembrace,""),
            Item(acc, 1, acc, R.drawable.acc_t1_emergancyinjector,""),
            Item(acc, 1, acc, R.drawable.acc_t1_portablemedkit,""),
            //4
            Item(acc, 1, acc, R.drawable.acc_t1_survivalgear,""),
            Item(acc, 2, acc, R.drawable.acc_t2_cyberneticarm,""),
            Item(acc, 2, acc, R.drawable.acc_t2_extraammunition,"blaster"),
            Item(acc, 2, acc, R.drawable.acc_t2_mandalorianhelmet,""),
            //8
            Item(acc, 2, acc, R.drawable.acc_t2_r5_astromech, "droid"),
            Item(acc, 2, acc, R.drawable.acc_t2_slicingtools, "slice"),
            Item(acc, 3, acc, R.drawable.acc_t3_combatknife, "slice"),
            Item(acc, 3, acc, R.drawable.acc_t3_combatvisor,""),
            //12
            Item(acc, 3, acc, R.drawable.acc_t3_concussiongrenades,""),
            Item(acc, 3, acc, R.drawable.acc_t3_hiddenblade, "slice"),
            Item(acc, 3, acc, R.drawable.acc_t3_personalshield,""),
            Item(acc, 3, acc, R.drawable.acc_t3_powercharger,""),
            //16
            Item(acc, 3, acc, R.drawable.acc_t3_reinforcedhelmet,""),
            Item(acc, 3, acc, R.drawable.acc_t3_supplypack,""),
            Item(armor, 1,armor, R.drawable.arm_t1_combatcoat,"clothing"),
            Item(armor, 1,armor, R.drawable.arm_t1_responsivearmor,"armor"),
            //20
            Item(armor, 1,armor, R.drawable.arm_t1_shadowsilkcloak,"clothing"),
            Item(armor, 2,armor, R.drawable.arm_t2_combatcoat,"clothing"),
            Item(armor, 2,armor, R.drawable.arm_t2_environmenthazardsuit,"clothing"),
            Item(armor, 2,armor, R.drawable.arm_t2_laminatearmor,"armor"),
            //24
            Item(armor, 3,armor, R.drawable.arm_t3_admiralsuniform,"clothing"),
            Item(armor, 3,armor, R.drawable.arm_t3_laminatearmor,"armor"),
            Item(armor, 3,armor, R.drawable.arm_t3_plastoidarmor,"armor"),
            Item(melee, 1,melee, R.drawable.empty_item_slot,"impact"),
            //28
            Item(melee, 1,melee, R.drawable.mel_t1_armoredgauntlets,"impact"),
            Item(melee, 1,melee, R.drawable.mel_t1_gaffistick,"impact"),
            Item(melee, 1,melee, R.drawable.mel_t1_punchdagger,"slice"),
            Item(melee, 1,melee, R.drawable.mel_t1_vibroblade,"slice"),
            //32
            Item(melee, 1,melee, R.drawable.mel_t1_vibroknife,"slice"),
            Item(melee, 1,melee, R.drawable.mel_t1_vibrosword,"slice"),
            Item(mod, 1,melee, R.drawable.mod_t1_balancedhilt, ""),
            Item(mod, 1,melee, R.drawable.mod_t1_extendedhaft, ""),
            //36
            Item(melee, 2,melee, R.drawable.mel_t2_bd_1vibroax,"impact"),
            Item(melee, 2,melee, R.drawable.mel_t2_doublevibrosword,"slice"),
            Item(melee, 2,melee, R.drawable.mel_t2_polearm,"impact"),
            Item(melee, 2,melee, R.drawable.mel_t2_stunbaton,"electric"),
            //40
            Item(melee, 2,melee, R.drawable.mel_t2_vibroknucklers,"impact"),
            Item(mod, 2,melee, R.drawable.mod_t2_energizedhilt, ""),
            Item(mod, 2,melee, R.drawable.mod_t2_highimpactguard, ""),
            Item(mod, 2,melee, R.drawable.mod_t2_weightedhead, ""),
            //44
            Item(mod, 2,melee, R.drawable.mod_t2_focusingbeam, ""),
            Item(melee, 3,melee, R.drawable.mel_t3_ancientlightsaber,"lightSaber"),
            Item(melee, 3,melee, R.drawable.mel_t3_borifle,"impact"),
            Item(melee, 3,melee, R.drawable.mel_t3_electrostaff,"electric"),
            //48
            Item(melee, 3,melee, R.drawable.mel_t3_forcepike,"impact"),
            Item(melee, 3,melee, R.drawable.mel_t3_ryykblades,"slice"),
            Item(mod, 3,melee, R.drawable.mod_t3_shockemitter, ""),
            Item(mod, 3,melee, R.drawable.mod_t3_vibrogenerator, ""),
            //52
            Item(ranged, 1,ranged, R.drawable.empty_item_slot,"blaster"),
            Item(ranged, 1,ranged, R.drawable.gun_t1_chargedpistol,"blaster"),
            Item(ranged, 1,ranged, R.drawable.gun_t1_ddcdefender,"blaster"),
            Item(ranged, 1,ranged, R.drawable.gun_t1_dh17,"blaster"),
            //56
            Item(ranged, 1,ranged, R.drawable.gun_t1_dl44,"blaster"),
            Item(ranged, 1,ranged, R.drawable.gun_t1_e11,"blaster"),
            Item(ranged, 1,ranged, R.drawable.gun_t1_handcannon,"blaster"),
            Item(ranged, 1,ranged, R.drawable.gun_t1_tatooinehuntingrifle,"blaster"),
            //60
            Item(mod, 1,ranged, R.drawable.mod_t1_marksmanbarrel, ""),
            Item(mod, 1,ranged, R.drawable.mod_t1_sniperscope, ""),
            Item(mod, 1,ranged, R.drawable.mod_t1_tacticaldisplay, ""),
            Item(mod, 1,ranged, R.drawable.mod_t1_underbarrelhh4, ""),
            //64
            Item(mod, 1,ranged, R.drawable.mod_t1_chargedammopack, ""),
            Item(ranged, 2,ranged, R.drawable.gun_t2_434deathhammer,"blaster"),
            Item(ranged, 2,ranged, R.drawable.gun_t2_a280,"blaster"),
            Item(ranged, 2,ranged, R.drawable.gun_t2_dt12heavyblasterpistol,"blaster"),
            //68
            Item(ranged, 2,ranged, R.drawable.gun_t2_e11d,"blaster"),
            Item(ranged, 2,ranged, R.drawable.gun_t2_ee3carbine,"blaster"),
            Item(ranged, 2,ranged, R.drawable.gun_t2_huntersrifle,"blaster"),
            Item(ranged, 2,ranged, R.drawable.gun_t2_t21,"blaster"),
            //72
            Item(mod, 2,ranged, R.drawable.mod_t2_boltupgrade, ""),
            Item(mod, 2,ranged, R.drawable.mod_t2_overcharger, ""),
            Item(mod, 2,ranged, R.drawable.mod_t2_plasmacell, ""),
            Item(mod, 2,ranged, R.drawable.mod_t2_spreadbarrel, ""),
            //76
            Item(ranged, 3,ranged, R.drawable.gun_t3_a12sniperrifle,"blaster"),
            Item(ranged, 3,ranged, R.drawable.gun_t3_disruptorpistol,"blaster"),
            Item(ranged, 3,ranged, R.drawable.gun_t3_dlt19,"blaster"),
            Item(ranged, 3,ranged, R.drawable.gun_t3_dxr6,"blaster"),
            //80
            Item(ranged, 3,ranged, R.drawable.gun_t3_modifiedenergycannon,"blaster"),
            Item(ranged, 3,ranged, R.drawable.gun_t3_pulsecannon,"blaster"),
            Item(ranged, 3,ranged, R.drawable.gun_t3_sportingblaster,"blaster"),
            Item(ranged, 3,ranged, R.drawable.gun_t3_valken38carbine,"blaster"),
            //84
            Item(mod, 3,ranged, R.drawable.mod_t3_disruptioncell, ""),
            Item(mod, 3,ranged, R.drawable.mod_t3_telescopingsights, ""),
            Item(acc, 0,reward, R.drawable.reward1_adrenalimplant,""),
            Item(acc, 0,reward, R.drawable.reward4_bardottanshard,""),
            //88
            Item(acc, 0,reward, R.drawable.reward19_quickdrawholster,""),

            //biv 89,90
            Item(armor,1,xp, R.drawable.xp_biv_trophyarmor,"armor"),
            Item(mod,1,xp,R.drawable.xp_biv_vibrobayonet,""),
            //davith 91,92
            Item(acc,0,xp,R.drawable.xp_davith_radiantholocron,""),
            Item(melee,1,xp,R.drawable.xp_davith_shoroudedlightsaber,"lightSaber"),
            //diala 93
            Item(melee,0,xp,R.drawable.xp_diala_shuenslightsaber,"lightSaber"),
            //drokatta 94,95
            Item(ranged,0,xp,R.drawable.xp_drokatta_repeatercannon,"ranged"),
            Item(mod,0,xp,R.drawable.xp_drokatta_shrapnelrounds,""),
            //loku 96
            Item(acc,0,xp,R.drawable.xp_loku_spectrumscanner,""),
            //mak 97
            Item(armor,0,xp,R.drawable.xp_mak_shadowsuit,"clothing"),
            //murne 98
            Item(acc,0,xp,R.drawable.xp_mhd19_systemsupgrade,"droid"),
            //murne 99
            Item(acc,1,xp,R.drawable.xp_murne_camdroid,"droid"),
            //onar 100
            Item(armor,1,xp,R.drawable.xp_onar_blacksunarmor,"armor"),
            //saska 101
            Item(acc,0,xp,R.drawable.xp_saska_techgoggles,""),
            //shyla 102,103
            Item(acc,0,xp,R.drawable.xp_shyla_remotedetonator,""),
            Item(acc,0,xp,R.drawable.xp_shyla_smokebombs,""),
            //vinto 104
            Item(ranged,1,xp,R.drawable.xp_vinto_offhandblaster,"blaster")
        )

        //indexing of all items at the array

        for (i in 0..itemsArray!!.size - 1) {

            itemsArray!!.get(i).index = i
        }

        //Combat Vambrace
        itemsArray!!.get(1).health = 1
        //Cybernetic Arm
        itemsArray!!.get(5).health = -2
        itemsArray!!.get(5).endurance = 1
        //Mando Helmet
        itemsArray!!.get(7).health = 1

        //Tier1 Combat Coat
        itemsArray!!.get(21).health = 2
        //Responsive Armor
        itemsArray!!.get(19).health = 2
        //Tier2 Combat Coat
        itemsArray!!.get(21).health = 2
        //Tier2 Laminate Armor
        itemsArray!!.get(23).health = 3
        //Admirals Uniform
        itemsArray!!.get(24).health = 1
        //Tier3 Laminate Armor
        itemsArray!!.get(25).health = 3


        //******************************************************************************************



        val emptyItem = Item(empty, -1, R.layout.empty_item)



        //******************************************************************************************
        //REWARDS SCREEN

        rewardsArray = arrayOf(
            itemsArray!![86],
            Item(reward, 0, R.drawable.allianceefficiency),
            Item(reward, 0, R.drawable.reward3_alliedoperations),

            itemsArray!![87],
            Item(reward, 0, R.drawable.reward5_counterparts),
            Item(reward, 0, R.drawable.reward6_heroic),

            Item(reward, 0, R.drawable.reward7_hondostreasure),
            Item(reward, 0, R.drawable.reward8_insidesource),
            Item(reward, 0, R.drawable.reward9_interceptedplans),

            Item(reward, 0, R.drawable.reward10_intimidation),
            Item(reward, 0, R.drawable.reward11_ladyluck),
            Item(reward, 0, R.drawable.reward12_legendary),

            Item(reward, 0, R.drawable.reward13_lessonsofhistory),
            Item(reward, 0, R.drawable.reward14_lobotsfavor),
            Item(reward, 0, R.drawable.reward15_lockedchest),

            Item(reward, 0, R.drawable.reward16_missingkey),
            Item(reward, 0, R.drawable.reward17_novacellleader),
            Item(reward, 0, R.drawable.reward18_populistsupport),

            itemsArray!![88],
            Item(reward, 0, R.drawable.reward_rebelrecon),
            Item(reward, 0, R.drawable.reward21_reliefeffort),

            Item(reward, 0, R.drawable.reward22_roughrider),
            Item(reward, 0, R.drawable.reward23_techspecialist),
            Item(reward, 0, R.drawable.reward24_thewaysofthefoce),

            Item(reward, 0, R.drawable.reward25_undertheradar)
        )
        for (i in 0..rewardsArray!!.size - 1) {
            if(i != 0 && i != 3 && i != 18) {
                rewardsArray!!.get(i).index = i
            }
        }
        rewardsArray!![5].health = 3
        rewardsArray!![11].health = 10
//
//        //******************************************************************************************
//        //ACCESSORIES SCREEN

        accArray = arrayOf(


            itemsArray!![0],
            itemsArray!![1],
            itemsArray!![2],

            itemsArray!![3],
            itemsArray!![4],
            emptyItem,



            itemsArray!![5],
            itemsArray!![6],
            itemsArray!![7],

            itemsArray!![8],
            itemsArray!![9],
            emptyItem,



            itemsArray!![10],
            itemsArray!![11],
            itemsArray!![12],

            itemsArray!![13],
            itemsArray!![14],
            itemsArray!![15],

            itemsArray!![16],
            itemsArray!![17],
            emptyItem
        )



        //******************************************************************************************
        //ARMORs SCREEN

        armorArray = arrayOf(

            itemsArray!![18],
            itemsArray!![19],
            itemsArray!![20],

            itemsArray!![21],
            itemsArray!![22],
            itemsArray!![23],

            itemsArray!![24],
            itemsArray!![25],
            itemsArray!![26]
        )


        //******************************************************************************************
        //MELEE SCREEN

        meleeArray = arrayOf(

            itemsArray!![27],
            itemsArray!![28],
            itemsArray!![29],

            itemsArray!![30],
            itemsArray!![31],
            itemsArray!![32],

            itemsArray!![33],
            emptyItem,
            emptyItem,


            itemsArray!![34],
            itemsArray!![35],
            emptyItem,


            itemsArray!![36],
            itemsArray!![37],
            itemsArray!![38],

            itemsArray!![39],
            itemsArray!![40],
            emptyItem,


            itemsArray!![41],
            itemsArray!![42],
            itemsArray!![43],

            itemsArray!![44],
            emptyItem,
            emptyItem,


            itemsArray!![45],
            itemsArray!![46],
            itemsArray!![47],

            itemsArray!![48],
            itemsArray!![49],
            emptyItem,


            itemsArray!![50],
            itemsArray!![51],
            emptyItem
        )

        //******************************************************************************************
        //RANGED SCREEN

        rangedArray = arrayOf(

            itemsArray!![52],
            itemsArray!![53],
            itemsArray!![54],

            itemsArray!![55],
            itemsArray!![56],
            itemsArray!![57],

            itemsArray!![58],
            itemsArray!![59],
            emptyItem,


            itemsArray!![60],
            itemsArray!![61],
            itemsArray!![62],

            itemsArray!![63],
            itemsArray!![64],
            emptyItem,


            itemsArray!![65],
            itemsArray!![66],
            itemsArray!![67],

            itemsArray!![68],
            itemsArray!![69],
            itemsArray!![70],

            itemsArray!![71],
            emptyItem,
            emptyItem,


            itemsArray!![72],
            itemsArray!![73],
            itemsArray!![74],

            itemsArray!![75],
            emptyItem,
            emptyItem,


            itemsArray!![76],
            itemsArray!![77],
            itemsArray!![78],

            itemsArray!![79],
            itemsArray!![80],
            itemsArray!![81],

            itemsArray!![82],
            itemsArray!![83],
            emptyItem,


            itemsArray!![84],
            itemsArray!![85],
            emptyItem
        )

    }

}

//Main items Class that stores all the values for each individual item

class Item() {
    var resourceId = -1
    var index = -1
    var where = 0

    var type = 0
    var health = 0
    var endurance = 0
    var tier = 0
    var soundType = 0 //default sound type

    constructor(type: Int, tier: Int, resourceId: Int) : this() {
        this.resourceId = resourceId
        this.type = type
        this.tier = tier
    }


    constructor( type: Int, tier: Int, where: Int, imageID: Int, soundType:
    String) : this
        () {
        this.resourceId = imageID
        this.type = type
        this.tier = tier
        this.where = where

        if (soundType == "slice") this.soundType = Sounds.equip_blade
        if (soundType == "lightSaber") this.soundType = Sounds.equip_gun
        if (soundType == "electric") this.soundType =  Sounds.equip_electric
        if (soundType == "blaster") this.soundType =  Sounds.equip_gun
        if (soundType == "impact") this.soundType =  Sounds.equip_impact
        if (soundType == "armor") this.soundType =  Sounds.equip_armor
        if (soundType == "clothing") this.soundType =  Sounds.equip_clothing
        if (soundType == "droid") this.soundType =  Sounds.droid
    }
    constructor( type: Int, tier: Int, where: Int, imageID: Int,subType: Int, soundType:
    String) : this
        () {
        this.resourceId = imageID
        this.type = type
        this.tier = tier
        this.where = where

        if (soundType == "slice") this.soundType = Sounds.equip_blade
        if (soundType == "lightSaber") this.soundType = Sounds.equip_gun
        if (soundType == "electric") this.soundType =  Sounds.equip_electric
        if (soundType == "blaster") this.soundType =  Sounds.equip_gun
        if (soundType == "impact") this.soundType =  Sounds.equip_impact
        if (soundType == "armor") this.soundType =  Sounds.equip_armor
        if (soundType == "clothing") this.soundType =  Sounds.equip_clothing
        if (soundType == "droid") this.soundType =  Sounds.droid
    }
}