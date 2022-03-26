package com.glasswellapps.iact.character_screen
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.Toast
import com.glasswellapps.iact.*
import com.glasswellapps.iact.effects.Sounds
import com.glasswellapps.iact.loading.CharacterHolder
import kotlinx.android.synthetic.main.screen_xp_select.*
import kotlinx.android.synthetic.main.toast.view.*

class XPScreen : AppCompatActivity() {
    private val character = CharacterHolder.getActiveCharacter()
    private var xpCardImages = arrayListOf<ImageView>()
    private  lateinit var cardDisplay:CardDisplay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_xp_select)
        if(character == null){
            finish()
        }

        XPCard1.setOnClickListener { onXPCard(0); }
        XPCard2.setOnClickListener { onXPCard(1); }
        XPCard3.setOnClickListener { onXPCard(2); }
        XPCard4.setOnClickListener { onXPCard(3); }
        XPCard5.setOnClickListener { onXPCard(4); }
        XPCard6.setOnClickListener { onXPCard(5); }
        XPCard7.setOnClickListener { onXPCard(6); }
        XPCard8.setOnClickListener { onXPCard(7); }
        XPCard9.setOnClickListener { onXPCard(8); }

        xpCardImages.add(XPCard1)
        xpCardImages.add(XPCard2)
        xpCardImages.add(XPCard3)
        xpCardImages.add(XPCard4)
        xpCardImages.add(XPCard5)
        xpCardImages.add(XPCard6)
        xpCardImages.add(XPCard7)
        xpCardImages.add(XPCard8)
        xpCardImages.add(XPCard9)

        addXP.setOnClickListener { addXP(); }
        minusXP.setOnClickListener { minusXP(); }

        cardDisplay = CardDisplay(this)

        for (i in 0..8) {
            if (character.xpCardsEquipped[i]) {
                xpCardImages[i].alpha = 1f
            } else {
                xpCardImages[i].alpha = 0.5f
            }
            if (character.xpCardImages.size > i) {
                xpCardImages[i].setImageBitmap(character.xpCardImages[i])
            }
            xpCardImages[i].setOnLongClickListener {
                cardDisplay.onShowCard((xpCardImages[i].drawable as BitmapDrawable).bitmap)
                true
            }
        }
        val xpLeft = character.totalXP - character.xpSpent
        xp_text.text = "XP: $xpLeft"
    }

    private fun onXPCard(cardNo: Int) {
        if(!CharacterHolder.getIsInteractable()){
            Sounds.negativeSound()
            return;
        }
        var xpLeft = character.totalXP - character.xpSpent

        if (character.xpCardsEquipped[cardNo]) {
            Sounds.selectSound()
            character.unequipXP(cardNo)

            xpCardImages[cardNo].animate().alpha(0.5f).duration = 50

        } else if (character.xpScores[cardNo] <= xpLeft) {
            character.equipXP(cardNo, this)
            if (character.xpCardsEquipped[cardNo]) {
                xpCardImages[cardNo].animate().alpha(1f).duration = 50
                if(cardNo < 8)
                Sounds.selectSound()
            }
        } else {
            showNotEnoughXP()
        }
        xpLeft = character.totalXP - character.xpSpent
        xp_text.text = "XP: $xpLeft"
        character.rewardObtained = character.xpCardsEquipped[8]
    }

    private fun showNotEnoughXP() {
        Sounds.negativeSound()
        val toast = Toast(this)
        toast.duration = Toast.LENGTH_SHORT
        val view = this.layoutInflater.inflate(
            R.layout.toast,
            null,
            false
        )
        view.toast_text.text = "NOT ENOUGH XP"
        toast.view = view
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    private fun addXP() {
        if(!CharacterHolder.getIsInteractable()){
            Sounds.negativeSound()
            return;
        }
        Sounds.selectSound()
        character.totalXP++
        val xpLeft = character.totalXP - character.xpSpent
        xp_text.text = "XP: $xpLeft"
    }

    private fun minusXP() {
        if(!CharacterHolder.getIsInteractable()){
            Sounds.negativeSound()
            return;
        }
        var xpLeft = character.totalXP - character.xpSpent
        if (xpLeft > 0) {
            Sounds.selectSound()
            character.totalXP--
        } else {
            Sounds.negativeSound()
        }
        xpLeft = character.totalXP - character.xpSpent
        xp_text.text = "XP: $xpLeft"
    }
    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}
