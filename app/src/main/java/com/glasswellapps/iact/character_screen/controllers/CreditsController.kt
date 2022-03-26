package com.glasswellapps.iact.character_screen.controllers
import android.R
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.method.LinkMovementMethod
import android.view.Window
import com.glasswellapps.iact.character_screen.CharacterScreen
import kotlinx.android.synthetic.main.credits_to_us.*

class CreditsController (val characterScreen: CharacterScreen){
    private var developersCreditsScreen: Dialog =
        Dialog(characterScreen, R.style.Theme_Material_Light_NoActionBar_Fullscreen)

    init{
        developersCreditsScreen.requestWindowFeature(Window.FEATURE_NO_TITLE)
        developersCreditsScreen.setCancelable(false)
        developersCreditsScreen.setContentView(com.glasswellapps.iact.R.layout.credits_to_us)
        developersCreditsScreen.setCanceledOnTouchOutside(true)
        developersCreditsScreen.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        developersCreditsScreen.textView41.movementMethod = LinkMovementMethod.getInstance()
        developersCreditsScreen.textView43.movementMethod = LinkMovementMethod.getInstance()
        developersCreditsScreen.matthew_twitter.movementMethod = LinkMovementMethod.getInstance()
        developersCreditsScreen.mannyPortfolio.movementMethod = LinkMovementMethod.getInstance()
        developersCreditsScreen.davidPortfolio.movementMethod = LinkMovementMethod.getInstance()
        developersCreditsScreen.mikeImageLink.setOnClickListener{
            val linkIntent: Intent = Intent()
                .setAction(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .setData(Uri.parse("http://www.mikeglasswell.com/"))
            characterScreen.startActivity(linkIntent)
        }
        developersCreditsScreen.mannyPortfolio.setOnClickListener{
            val linkIntent = Intent()
                .setAction(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .setData(Uri.parse("https://mansinh-d25ff.web.app"))
            characterScreen.startActivity(linkIntent)
        }
        developersCreditsScreen.matthew_twitter.setOnClickListener{
            val linkIntent = Intent()
                .setAction(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .setData(Uri.parse("https://twitter.com/Matthew_Hero_X"))
            characterScreen.startActivity(linkIntent)
        }
    }
    fun showDialog(){
        developersCreditsScreen.show()
    }
}