package com.glasswellapps.iact.character_screen.controllers
import com.glasswellapps.iact.characters.Character
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.TextView
import com.glasswellapps.iact.R
import com.glasswellapps.iact.character_screen.CharacterScreen
import com.glasswellapps.iact.character_screen.types.EnemyTypes
import kotlinx.android.synthetic.main.screen_stats.*
import kotlin.math.max

class StatsScreenController (private var characterScreen: CharacterScreen) {
    private var character:Character = characterScreen.character
    private var statsScreen: Dialog = Dialog(characterScreen)
    private val villain = EnemyTypes.VILLAIN
    private val vehicle = EnemyTypes.VEHICLE
    private val creature = EnemyTypes.CREATURE
    private val leader = EnemyTypes.LEADER
    private val scum = EnemyTypes.SCUM
    private val droid = EnemyTypes.DROID
    private val guard = EnemyTypes.GUARD
    private val trooper = EnemyTypes.TROOPER

    init{
        statsScreen.requestWindowFeature(Window.FEATURE_NO_TITLE)
        statsScreen.setCancelable(false)
        statsScreen.setContentView(R.layout.screen_stats)
        statsScreen.setCanceledOnTouchOutside(true)
        statsScreen.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        statsScreen.edit_stat.visibility = View.INVISIBLE
        statsScreen.stats_name.text =  character.name
        statsScreen.stats_portrait_image.setImageDrawable(character.portraitImage)

        statsScreen.stats_moves.setOnClickListener { onTapStat(statsScreen.stats_moves) }
        statsScreen.stats_attacks.setOnClickListener { onTapStat(statsScreen.stats_attacks) }
        statsScreen.stats_interacts.setOnClickListener { onTapStat(statsScreen.stats_interacts) }
        statsScreen.stats_wounded.setOnClickListener { onTapStat(statsScreen.stats_wounded) }
        statsScreen.stats_rested.setOnClickListener { onTapStat(statsScreen.stats_rested) }
        statsScreen.stats_withdrawn.setOnClickListener { onTapStat(statsScreen.stats_withdrawn) }
        statsScreen.stats_activated.setOnClickListener { onTapStat(statsScreen.stats_activated) }
        statsScreen.stats_damaged.setOnClickListener { onTapStat(statsScreen.stats_damaged) }
        statsScreen.stats_strain.setOnClickListener { onTapStat(statsScreen.stats_strain) }
        statsScreen.stats_specials.setOnClickListener { onTapStat(statsScreen.stats_specials) }
        statsScreen.stats_focused.setOnClickListener { onTapStat(statsScreen.stats_focused) }
        statsScreen.stats_hidden.setOnClickListener { onTapStat(statsScreen.stats_hidden) }
        statsScreen.stats_stunned.setOnClickListener { onTapStat(statsScreen.stats_stunned) }
        statsScreen.stats_bleeding.setOnClickListener { onTapStat(statsScreen.stats_bleeding) }
        statsScreen.stats_weakened.setOnClickListener { onTapStat(statsScreen.stats_weakened) }
        statsScreen.stats_crates.setOnClickListener { onTapStat(statsScreen.stats_crates) }
        statsScreen.stats_kill_villain.setOnClickListener {
            onTapStat(
                statsScreen
                    .stats_kill_villain
            )
        }
        statsScreen.stats_kill_vehicle.setOnClickListener {
            onTapStat(
                statsScreen
                    .stats_kill_vehicle
            )
        }
        statsScreen.stats_kill_creature.setOnClickListener {
            onTapStat(
                statsScreen
                    .stats_kill_creature
            )
        }
        statsScreen.stats_kill_leader.setOnClickListener { onTapStat(statsScreen.stats_kill_leader) }
        statsScreen.stats_kill_guardian.setOnClickListener {
            onTapStat(
                statsScreen
                    .stats_kill_guardian
            )
        }
        statsScreen.stats_kill_droid.setOnClickListener {
            onTapStat(
                statsScreen
                    .stats_kill_droid
            )
        }
        statsScreen.stats_kill_scum.setOnClickListener { onTapStat(statsScreen.stats_kill_scum) }
        statsScreen.stats_kill_trooper.setOnClickListener {
            onTapStat(
                statsScreen
                    .stats_kill_trooper
            )
        }


        statsScreen.stats_assist_villain.setOnClickListener {
            onTapStat(
                statsScreen
                    .stats_assist_villain
            )
        }
        statsScreen.stats_assist_vehicle.setOnClickListener {
            onTapStat(
                statsScreen
                    .stats_assist_vehicle
            )
        }
        statsScreen.stats_assist_creature.setOnClickListener {
            onTapStat(
                statsScreen
                    .stats_assist_creature
            )
        }
        statsScreen.stats_assist_leader.setOnClickListener {
            onTapStat(
                statsScreen
                    .stats_assist_leader
            )
        }
        statsScreen.stats_assist_guardian.setOnClickListener {
            onTapStat(
                statsScreen
                    .stats_assist_guardian
            )
        }
        statsScreen.stats_assist_droid.setOnClickListener {
            onTapStat(
                statsScreen
                    .stats_assist_droid
            )
        }
        statsScreen.stats_assist_scum.setOnClickListener {
            onTapStat(
                statsScreen
                    .stats_assist_scum
            )
        }
        statsScreen.stats_assist_trooper.setOnClickListener {
            onTapStat(
                statsScreen
                    .stats_assist_trooper
            )
        }


        statsScreen.setOnCancelListener {
            character.movesTaken = Integer.parseInt(statsScreen.stats_moves.text.toString())
            character.attacksMade =
                Integer.parseInt(statsScreen.stats_attacks.text.toString())
            character.interactsUsed =
                Integer.parseInt(statsScreen.stats_interacts.text.toString())
            character.timesWounded =
                Integer.parseInt(statsScreen.stats_wounded.text.toString())
            character.timesRested =
                Integer.parseInt(statsScreen.stats_rested.text.toString())
            character.timesWithdrawn =
                Integer.parseInt(statsScreen.stats_withdrawn.text.toString())
            character.activated =
                Integer.parseInt(statsScreen.stats_activated.text.toString())
            character.damageTaken =
                Integer.parseInt(statsScreen.stats_damaged.text.toString())
            character.strainTaken =
                Integer.parseInt(statsScreen.stats_strain.text.toString())
            character.specialActions =
                Integer.parseInt(statsScreen.stats_specials.text.toString())
            character.timesFocused =
                Integer.parseInt(statsScreen.stats_focused.text.toString())
            character.timesHidden =
                Integer.parseInt(statsScreen.stats_hidden.text.toString())
            character.timesStunned =
                Integer.parseInt(statsScreen.stats_stunned.text.toString())
            character.timesBleeding =
                Integer.parseInt(statsScreen.stats_bleeding.text.toString())
            character.timesWeakened =
                Integer.parseInt(statsScreen.stats_weakened.text.toString())
            character.cratesPickedUp =
                Integer.parseInt(statsScreen.stats_crates.text.toString())

            character.rewardObtained =
                statsScreen.stats_reward_obtained.text.toString() == "Yes"

            updateKillTracker()
            characterScreen.quickSave()
        }
        statsScreen.addStat.setOnClickListener {
            currentStatEditing.text =  (Integer.parseInt(
                (currentStatEditing.text.toString())) + 1).toString()
            statsScreen.stat_text.text = currentStatEditing.text.toString()
            updateKillTracker()
        }
        statsScreen.minusStat.setOnClickListener {
            currentStatEditing.text =  max(Integer.parseInt(
                (currentStatEditing.text.toString())) - 1,0).toString()
            statsScreen.stat_text.text = currentStatEditing.text.toString()
            updateKillTracker()
        }
        statsScreen.edit_stat.setOnClickListener {
            statsScreen.edit_stat.visibility = View.INVISIBLE
            updateKillTracker()
        }


    }

    fun showDialog()
    {
        var level = character.getLevel()


        statsScreen.stats_level.text = level.toString()
        statsScreen.stats_moves.text = character.movesTaken.toString()
        statsScreen.stats_attacks.text =  character.attacksMade.toString()
        statsScreen.stats_interacts.text =  character.interactsUsed.toString()
        statsScreen.stats_wounded.text =  character.timesWounded.toString()
        statsScreen.stats_rested.text =  character.timesRested.toString()
        statsScreen.stats_withdrawn.text =  character.timesWithdrawn.toString()
        statsScreen.stats_activated.text =  character.activated.toString()
        statsScreen.stats_damaged.text =  character.damageTaken.toString()
        statsScreen.stats_strain.text =  character.strainTaken.toString()
        statsScreen.stats_specials.text =  character.specialActions.toString()
        statsScreen.stats_focused.text =  character.timesFocused.toString()
        statsScreen.stats_hidden.text =  character.timesHidden.toString()
        statsScreen.stats_stunned.text =  character.timesStunned.toString()
        statsScreen.stats_bleeding.text =  character.timesBleeding.toString()
        statsScreen.stats_weakened.text =  character.timesWeakened.toString()
        statsScreen.stats_crates.text =  character.cratesPickedUp.toString()

        if (character.rewardObtained) {
            statsScreen.stats_reward_obtained.text = "Yes"
        } else {
            statsScreen.stats_reward_obtained.text = "No"
        }

        setTotalKills()

        statsScreen.stats_kill_villain.text =  character.killCount[villain].toString()
        statsScreen.stats_kill_vehicle.text =  character.killCount[vehicle].toString()
        statsScreen.stats_kill_creature.text =  character.killCount[creature].toString()
        statsScreen.stats_kill_leader.text =  character.killCount[leader].toString()
        statsScreen.stats_kill_guardian.text =  character.killCount[guard].toString()
        statsScreen.stats_kill_droid.text =  character.killCount[droid].toString()
        statsScreen.stats_kill_scum.text =  character.killCount[scum].toString()
        statsScreen.stats_kill_trooper.text =  character.killCount[trooper].toString()


        statsScreen.stats_assist_villain.text =  character.assistCount[villain].toString()
        statsScreen.stats_assist_vehicle.text =  character.assistCount[vehicle].toString()
        statsScreen.stats_assist_creature.text =  character.assistCount[creature].toString()
        statsScreen.stats_assist_leader.text =  character.assistCount[leader].toString()
        statsScreen.stats_assist_guardian.text =  character.assistCount[guard].toString()
        statsScreen.stats_assist_droid.text =  character.assistCount[droid].toString()
        statsScreen.stats_assist_scum.text =  character.assistCount[scum].toString()
        statsScreen.stats_assist_trooper.text =  character.assistCount[trooper].toString()

        statsScreen.show()
    }


    private lateinit var currentStatEditing: TextView
    private fun updateKillTracker() {
        character.killCount[villain] =
            Integer.parseInt(statsScreen.stats_kill_villain.text.toString())
        character.killCount[vehicle] =
            Integer.parseInt(statsScreen.stats_kill_vehicle.text.toString())
        character.killCount[creature] =
            Integer.parseInt(statsScreen.stats_kill_creature.text.toString())
        character.killCount[leader] =
            Integer.parseInt(statsScreen.stats_kill_leader.text.toString())
        character.killCount[guard] =
            Integer.parseInt(statsScreen.stats_kill_guardian.text.toString())
        character.killCount[droid] =
            Integer.parseInt(statsScreen.stats_kill_droid.text.toString())
        character.killCount[scum] =
            Integer.parseInt(statsScreen.stats_kill_scum.text.toString())
        character.killCount[trooper] =
            Integer.parseInt(statsScreen.stats_kill_trooper.text.toString())
        character.assistCount[villain] =
            Integer.parseInt(statsScreen.stats_assist_villain.text.toString())
        character.assistCount[vehicle] =
            Integer.parseInt(statsScreen.stats_assist_vehicle.text.toString())
        character.assistCount[creature] =
            Integer.parseInt(statsScreen.stats_assist_creature.text.toString())
        character.assistCount[leader] =
            Integer.parseInt(statsScreen.stats_assist_leader.text.toString())
        character.assistCount[guard] =
            Integer.parseInt(statsScreen.stats_assist_guardian.text.toString())
        character.assistCount[droid] =
            Integer.parseInt(statsScreen.stats_assist_droid.text.toString())
        character.assistCount[scum] =
            Integer.parseInt(statsScreen.stats_assist_scum.text.toString())
        character.assistCount[trooper] =
            Integer.parseInt(statsScreen.stats_assist_trooper.text.toString())

        character.timesWounded =
            Integer.parseInt(statsScreen.stats_wounded.text.toString())
        setTotalKills()
    }

    private fun onTapStat(v: TextView) {
        statsScreen.edit_stat.visibility = View.VISIBLE
        currentStatEditing = v
        statsScreen.stat_text.text = v.text.toString()

    }

    private fun setTotalKills() {
        var totalKills = 0
        var totalAssists = 0
        for (i in character.killCount.indices) {
            totalKills += character.killCount[i]
            totalAssists += character.assistCount[i]
        }
        statsScreen.stats_kill_total.text =  totalKills.toString()
        statsScreen.stats_assist_total.text =  totalAssists.toString()
        if (character.timesWounded > 0) {
            statsScreen.stats_kill_death_ratio.text =
                (Math.round(10*totalKills.toFloat() / character.timesWounded).toFloat()/10f).toString()
        } else {
            statsScreen.stats_kill_death_ratio.text = "-"
        }
    }
}