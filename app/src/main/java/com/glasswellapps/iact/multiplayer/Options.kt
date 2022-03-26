package com.glasswellapps.iact.multiplayer
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import com.glasswellapps.iact.R
import com.glasswellapps.iact.character_screen.XPScreen
import com.glasswellapps.iact.character_screen.controllers.BackgroundController
import com.glasswellapps.iact.character_screen.views.BioView
import com.glasswellapps.iact.effects.Sounds
import com.glasswellapps.iact.inventory.*
import com.glasswellapps.iact.loading.CharacterHolder
import kotlinx.android.synthetic.main.dialog_multiplayer_options.*

class Options(private var screen: MultiplayerScreen, var isImperial:Boolean) {
    private val optionsMenu:Dialog = Dialog(screen)
    private val backgroundMenu = BackgroundController(screen)
    private val conditionController = MultiplayerConditions(screen)
    private val bioView= BioView(screen)
    init{
        optionsMenu.requestWindowFeature(Window.FEATURE_NO_TITLE)
        optionsMenu.setCancelable(false)
        optionsMenu.setContentView(R.layout.dialog_multiplayer_options)
        optionsMenu.setCanceledOnTouchOutside(true)
        optionsMenu.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        optionsMenu.mp_background.setOnClickListener { onBackground() }
        optionsMenu.mp_rewards_button.setOnClickListener { onReward() }
        optionsMenu.mp_accessory_button.setOnClickListener { onAccessory() }
        optionsMenu.mp_armour_button.setOnClickListener { onArmour() }
        optionsMenu.mp_melee_button.setOnClickListener { onMelee() }
        optionsMenu.mp_ranged_button.setOnClickListener{onRange()}
        optionsMenu.mp_xp_button.setOnClickListener { onXPScreen() }
        optionsMenu.mp_condition_button.setOnClickListener { onCondition() }
        optionsMenu.mp_bio_button.setOnClickListener { onBio() }
        optionsMenu.mp_remove_button.setOnClickListener { onRemove() }



        setFactionIcon()
    }
    fun onSelect(){
        Sounds.selectSound()
        optionsMenu.dismiss()
    }
    fun onNavigate(){
        onSelect()
        screen.playerToBeAdded = -1
        CharacterHolder.setIsInteractable(player.isLocal)
        CharacterHolder.setActiveCharacter(player.character)
        CharacterHolder.clearAllImages()
    }



    private fun onReward() {
        onNavigate()
        val intent = Intent(screen, RewardsScreen::class.java)
        screen.startActivity(intent)
    }
    private fun onAccessory() {
        onNavigate()
        val intent = Intent(screen, AccScreen::class.java)
        screen.startActivity(intent)
    }
    private fun onArmour() {
        val intent = Intent(screen, ArmorScreen::class.java)
        onNavigate()
        screen.startActivity(intent)
    }
    private fun onMelee() {
        onNavigate()
        val intent = Intent(screen, MeleeScreen::class.java)
        screen.startActivity(intent)
    }
    private fun onRange() {
        val intent = Intent(screen, RangedScreen::class.java)
        onNavigate()
        screen.startActivity(intent)
    }
    private fun onXPScreen() {
        val intent = Intent(screen, XPScreen::class.java)
        onNavigate()
        screen.startActivity(intent)
    }
    private fun onBackground(){
        onSelect()
        backgroundMenu.showDialog(player.character, player.view.backgroundView, player.view.camouflageView)
    }
    private fun onBio(){
        onSelect()
        bioView.showDialog(player.character)
    }
    private fun onCondition(){
        onSelect()
        conditionController.onAddCondition(player)
    }
    private fun onRemove(){
        onSelect()
        screen.onPlayerRemoved(playerNumber)
        player.hide();
    }

    private lateinit var player: Player
    private var playerNumber = -1

    fun onShowDialog(player: Player, playerNumber:Int){

        if(player.character != null){
            this.player = player
            this.playerNumber = playerNumber
            optionsMenu.player_name.text = player.character.name.toUpperCase()
            optionsMenu.player_level.text = "Level " + player.character.getLevel()
            player.character.loadPortraitImage(screen)
            optionsMenu.player_portrait.setImageDrawable(player.character.portraitImage)
            Sounds.buttonSound()

            if (player.isLocal){
                optionsMenu.mp_condition_button.isClickable = true
                optionsMenu.mp_background.isClickable = true
                optionsMenu.mp_condition_button.alpha = 1f
                optionsMenu.mp_background.alpha = 1f
                optionsMenu.read_only.visibility= View.INVISIBLE
            }
            else{
                optionsMenu.mp_condition_button.isClickable = false
                optionsMenu.mp_background.isClickable = false
                optionsMenu.mp_condition_button.alpha = 0.1f
                optionsMenu.mp_background.alpha = 0.1f
                optionsMenu.read_only.visibility= View.VISIBLE
            }

            optionsMenu.show()
        }
        else{
            Sounds.negativeSound()
        }
    }

    fun setFactionIcon(){
        var factionIcon = R.drawable.rebel_icon_lit
        if(isImperial){factionIcon = R.drawable.imperial_icon_lit}
        optionsMenu.faction_icon.setImageDrawable( screen.resources.getDrawable(factionIcon))
    }
}