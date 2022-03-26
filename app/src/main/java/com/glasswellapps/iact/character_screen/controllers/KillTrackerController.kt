package com.glasswellapps.iact.character_screen.controllers
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.glasswellapps.iact.R
import com.glasswellapps.iact.character_screen.CharacterScreen
import com.glasswellapps.iact.character_screen.types.EnemyTypes
import com.glasswellapps.iact.effects.Sounds
import kotlinx.android.synthetic.main.activity_character_screen.*
import kotlinx.android.synthetic.main.dialog_assist.*

class KillTrackerController(val characterScreen: CharacterScreen) {
    private var character = characterScreen.character
    private lateinit var assistDialog: Dialog
    private var killCounts = ArrayList<TextView>()

    init {
        initAssistDialog()
        initKillCount(characterScreen.villain_button, characterScreen.villain_count,
            EnemyTypes.VILLAIN
        )
        initKillCount(characterScreen.leader_button, characterScreen.leader_count,
            EnemyTypes.LEADER
        )
        initKillCount(characterScreen.vehicle_button, characterScreen.vehicle_count,
            EnemyTypes.VEHICLE
        )
        initKillCount(characterScreen.creature_button, characterScreen.creature_count,
            EnemyTypes.CREATURE
        )
        initKillCount(characterScreen.guard_button, characterScreen.guard_count, EnemyTypes.GUARD)
        initKillCount(characterScreen.droid_button, characterScreen.droid_count, EnemyTypes.DROID)
        initKillCount(characterScreen.scum_button, characterScreen.scum_count, EnemyTypes.SCUM)
        initKillCount(characterScreen.trooper_button, characterScreen.trooper_count,
            EnemyTypes.TROOPER
        )

        for (i in 0 until killCounts.size) {
            killCounts[i].text = "" + character.killCount[i]
        }
    }

    private fun initKillCount(icon: ImageView, counter: TextView, type:Int){
        killCounts.add(counter)
        icon.tag = type
        icon.setOnClickListener{
            killCountUp(icon)
        }
        counter.tag = type
        counter.setOnClickListener{
            killCountDown(counter)
        }
        counter.setOnLongClickListener {
            assistDialog.assist_icon.setImageDrawable(icon.drawable)
            assistDialog.remove_condition_button.tag = counter.tag
            assistDialog.show()
            true
        }
    }

    private fun initAssistDialog() {
        assistDialog = Dialog(characterScreen)
        assistDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        assistDialog.setCancelable(false)
        assistDialog.setContentView(R.layout.dialog_assist)
        assistDialog.setCanceledOnTouchOutside(true)
        assistDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        assistDialog.remove_condition_button.setOnClickListener {
            onAssist(assistDialog.remove_condition_button)
            //TODO
            characterScreen.quickSave()
        }
    }

    private fun killCountUp(view: View) {
        val type = view.tag as Int
        Sounds.selectSound()
        var killCount = Integer.parseInt(killCounts[type].text.toString())
        killCount++
        character.killCount[type] = killCount
        killCounts[type].text = "" + killCount
    }

    private fun killCountDown(view: View) {
        val type = view.tag as Int
        var killCount = Integer.parseInt(killCounts[type].text.toString())
        if (killCount > 0) {
            Sounds.selectSound()
            killCount--
            character.killCount[type] = killCount
            killCounts[type].text = "" + killCount
        }
    }

    private fun onAssist(view: View) {
        Sounds.selectSound()
        val type = view.tag as Int
        character.assistCount[type]++
        assistDialog.dismiss()
    }
}