package com.glasswellapps.iact.multiplayer

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.glasswellapps.iact.Characters_list
import com.glasswellapps.iact.LoadScreen
import com.glasswellapps.iact.R
import com.glasswellapps.iact.effects.Sounds
import com.glasswellapps.iact.loading.CharacterHolder
import kotlinx.android.synthetic.main.dialog_multiplayer_add_player.*


class PlayerAdder(val screen: MultiplayerScreen) {
    private var playerNumber: Int = 0
    private val addPlayerDialog: Dialog = Dialog(screen)
    init {
        addPlayerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        addPlayerDialog.setCancelable(false)
        addPlayerDialog.setContentView(R.layout.dialog_multiplayer_add_player)
        addPlayerDialog.setCanceledOnTouchOutside(true)
        addPlayerDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        addPlayerDialog.imperial_new.setOnClickListener { onNewPlayer() }
        addPlayerDialog.imperial_load.setOnClickListener { onLoadPlayer() }
    }

    fun showDialog(playerNumber : Int){
        Sounds.buttonSound()
        this.playerNumber = playerNumber
        addPlayerDialog.show()
    }
    private fun onNewPlayer(){
        Sounds.selectSound()
        val intent = Intent(screen, Characters_list::class.java)
        intent.putExtra("from","imperial")
        addPlayerDialog.dismiss()
        screen.playerToBeAdded = playerNumber
        CharacterHolder.setActiveCharacter(null)
        screen.startActivity(intent)
    }
    private fun onLoadPlayer(){
        Sounds.selectSound()
        val intent = Intent(screen, LoadScreen::class.java)
        intent.putExtra("from","imperial")
        addPlayerDialog.dismiss()
        screen.playerToBeAdded = playerNumber
        CharacterHolder.setActiveCharacter(null)
        screen.startActivity(intent)
    }
}