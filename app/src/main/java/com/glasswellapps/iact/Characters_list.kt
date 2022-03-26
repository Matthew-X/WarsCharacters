package com.glasswellapps.iact

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.glasswellapps.iact.character_screen.CharacterBuilder
import com.glasswellapps.iact.character_screen.CharacterScreen
import com.glasswellapps.iact.effects.Sounds
import com.glasswellapps.iact.loading.CharacterHolder
import kotlinx.android.synthetic.main.activity_characters_list.*

class Characters_list : AppCompatActivity() {
    var charactersImage = arrayListOf<ImageView>()
    var width = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAnimation()
        setContentView(R.layout.activity_characters_list)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        width = displayMetrics.widthPixels.toFloat()/displayMetrics.density
        //Characters list for List view to choose characters

        charactersImage = arrayListOf<ImageView>(
            findViewById(R.id.imageViewBiv),
            findViewById(R.id.imageViewGaar),
            findViewById(R.id.imageViewMak),
            findViewById(R.id.imageViewSaska),
            findViewById(R.id.imageViewDavith),
            findViewById(R.id.imageViewGideon),
            findViewById(R.id.imageViewMHD),
            findViewById(R.id.imageViewShyla),
            findViewById(R.id.imageViewDiala),
            findViewById(R.id.imageViewJyn),
            findViewById(R.id.imageViewMurne),
            findViewById(R.id.imageViewVerena),
            findViewById(R.id.imageViewFenn),
            findViewById(R.id.imageViewLoku),
            findViewById(R.id.imageViewOnar),
            findViewById(R.id.imageViewVinto),
            findViewById(R.id.imageViewKo),
            findViewById(R.id.imageViewJarrod),
            findViewById(R.id.imageViewDrok),
            findViewById(R.id.imageViewCT),
            findViewById(R.id.imageViewTress)

        )
        var allChSel = BitmapFactory.decodeResource(resources,R.drawable
            .allcharacterselect_21)
        allChSel = Bitmap.createScaledBitmap(allChSel,2048,683,false)

        //code bellow cuts out image  assets from one image sheet for Character list.

        var row = 6
        var col = 4
        var width = 2048/col
        var height = 683/row
        var buffer1 = 0

        for (r in 0..row-1){
            for (c in 0..col-1){
                if(buffer1 > 20) break
                var drawable = BitmapDrawable( Bitmap.createBitmap
                (allChSel,0+
                (width*c),0+(height*r),width-1,height-1))

                charactersImage[buffer1++].setImageDrawable(drawable)
            }
        }

        if(CreateScreen.customCharacter != null){
            customCharacter.visibility = View.VISIBLE;
        }
        else{
            customCharacter.visibility = View.INVISIBLE;
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        characterList.animate().alpha(1f)
        var sortedCharArray = arrayListOf<View>(
            findViewById(R.id.imageViewBiv),
            findViewById(R.id.imageViewDiala),
            findViewById(R.id.imageViewSaska),
            findViewById(R.id.imageViewGideon),
            findViewById(R.id.imageViewJarrod),
            findViewById(R.id.imageViewVinto),
            findViewById(R.id.imageViewMak),
            findViewById(R.id.imageViewLoku),
            findViewById(R.id.imageViewDrok),
            findViewById(R.id.imageViewShyla),
            findViewById(R.id.imageViewJyn),
            findViewById(R.id.imageViewVerena),
            findViewById(R.id.imageViewOnar),
            findViewById(R.id.imageViewDavith),
            findViewById(R.id.imageViewMHD),
            findViewById(R.id.imageViewGaar),
            findViewById(R.id.imageViewFenn),
            findViewById(R.id.imageViewCT),
            findViewById(R.id.imageViewKo),
            findViewById(R.id.imageViewMurne),
            findViewById(R.id.imageViewTress),
            findViewById(R.id.customCharacter)
        )
        if(hasFocus) {
            for (i in 0 until sortedCharArray.size) {
                println("ANIMATED LOAD")
                var anim = ObjectAnimator.ofFloat(
                    sortedCharArray[i],
                    "translationX", -(i.toFloat()/2 + 1) *width,
                    0f
                )
                anim.duration = ((i.toFloat()/2 + 1)  * 150).toLong()

                println(""+anim.duration + " " + anim.values[0])

                anim.start()

            }
        }

    }

    fun setAnimation(){
        /*if(Build.VERSION.SDK_INT>20) {
            val fade = android.transition.Fade()
            fade.setDuration(100);
            getWindow().setExitTransition(fade);
            getWindow().setEnterTransition(fade);
        }*/
    }

    //Load layout for selected character (pointing out it's tag to note which assets to load)

    fun onSelect(view: View) {
        val characterName = view.tag.toString()
        if(CharacterHolder.isInParty(characterName)) {
            Sounds.negativeSound();
            ShortToast.show(this, "ALREADY IN PARTY")
            return
        }
        Sounds.selectSound()
        for(i in 0..charactersImage.count()-1){
            if(charactersImage[i] != view){
                charactersImage[i].alpha = 0f
            }
        }
        CharacterHolder.setActiveCharacter(CharacterBuilder.create(characterName,this))
        var from: String = intent.getStringExtra("from").toString()

        when(from){
            "imperial"-> onBackPressed()
            "main"-> toCharacterScreen()
        }
    }

    private fun toCharacterScreen(){
        val intent = Intent(this, CharacterScreen::class.java)
        startActivity(intent);
        finish()
    }

    fun onSelectCustom(view: View) {
        Sounds.selectSound()
        for(i in 0..charactersImage.count()-1){

                charactersImage[i].alpha = 0f
        }
        if(CharacterHolder.getActiveCharacter() != null) {
            wipeSelectedCharacter()
        }
        val intent = Intent(this, CharacterScreen::class.java)
        intent.putExtra("CharacterName", "custom")
        intent.putExtra("Load", false)


        startActivity(intent);
        finish()

    }



    fun wipeSelectedCharacter(){

        CharacterHolder.getActiveCharacter().currentImage!!.recycle()
        for (i in 0..CharacterHolder.getActiveCharacter().xpCardImages.size-1) {
            if (CharacterHolder.getActiveCharacter().xpCardImages[i] != null) {
                CharacterHolder.getActiveCharacter().xpCardImages!![i].recycle()
            }
        }
        if (CharacterHolder.getActiveCharacter().power != null) {
            CharacterHolder.getActiveCharacter().power!!.recycle()
            CharacterHolder.getActiveCharacter().power_wounded!!.recycle()
        }
        if (CharacterHolder.getActiveCharacter().portraitImage != null) {
            CharacterHolder.getActiveCharacter().portraitImage = null
        }
        CharacterHolder.setActiveCharacter(null)
    }
}