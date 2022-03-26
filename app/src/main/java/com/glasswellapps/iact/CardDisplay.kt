package com.glasswellapps.iact

import android.app.Activity
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.glasswellapps.iact.effects.Sounds
import kotlinx.android.synthetic.main.dialog_show_card.*

class CardDisplay (context:Activity){
    private val showCardDialog =Dialog(context)
    init{
        showCardDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        showCardDialog.setContentView(R.layout.dialog_show_card)
        showCardDialog.setCancelable(true)
        showCardDialog.setCanceledOnTouchOutside(true)
        showCardDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        showCardDialog.show_card_dialog.setOnClickListener {
            Sounds.selectSound()
            showCardDialog.dismiss()
        }
    }
    fun onShowCard(image : Bitmap){
        showCardDialog.card_image.setImageBitmap(image)
        showCardDialog.show()
    }
}