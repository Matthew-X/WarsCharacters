package com.glasswellapps.iact.character_screen.views

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.glasswellapps.iact.R
import com.glasswellapps.iact.characters.Character
import com.glasswellapps.iact.effects.Sounds
import kotlinx.android.synthetic.main.dialog_bio.*

class BioView (val context:Activity){
    private var bioDialog: Dialog = Dialog(context)
    fun showDialog(character: Character) {
        bioDialog.bio_title.text = character.bio_title
        bioDialog.bio_quote.text = character.bio_quote
        bioDialog.bio_text.text = character.bio_text
        bioDialog.show()
    }


    init{
        bioDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            bioDialog.setCancelable(true)
            bioDialog.setContentView(R.layout.dialog_bio)
            bioDialog.setCanceledOnTouchOutside(true)
            bioDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            bioDialog.bio_layout.setOnClickListener {
                Sounds.selectSound()
                bioDialog.dismiss()
        }
    }
}