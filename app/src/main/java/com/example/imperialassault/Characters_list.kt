package com.example.imperialassault

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Characters_list : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_characters_list)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        var CharactersImage= arrayListOf<ImageView>(
            findViewById(R.id.imageViewMak),
            findViewById(R.id.imageViewBiv),
            findViewById(R.id.imageViewCT),
            findViewById(R.id.imageViewDavith),
            findViewById(R.id.imageViewDiala),
            findViewById(R.id.imageViewFenn),
            findViewById(R.id.imageViewVinto),
            findViewById(R.id.imageViewGaar),
            findViewById(R.id.imageViewGideon),
            findViewById(R.id.imageViewJarrod),
            findViewById(R.id.imageViewJyn),
            findViewById(R.id.imageViewKo),
            findViewById(R.id.imageViewLoku),
            findViewById(R.id.imageViewMHD),
            findViewById(R.id.imageViewMurne),
            findViewById(R.id.imageViewOnar),
            findViewById(R.id.imageViewOrok),
            findViewById(R.id.imageViewSaska),
            findViewById(R.id.imageViewShyla),
            findViewById(R.id.imageViewTress),
            findViewById(R.id.imageViewVerena),
        )
        var allChSel = BitmapFactory.decodeResource(resources,R.drawable.allcharacterselect_21)
        allChSel = Bitmap.createScaledBitmap(allChSel,2547,850,false)

        var row = 6
        var col = 4
        var width = 2547/col
        var height = 850/row
        var buffer1 = 0

        for (r in 0..row-1){
            for (c in 0..col-1){
                if(buffer1 > 20) break
                CharactersImage[buffer1++].setImageBitmap(Bitmap.createBitmap(allChSel,0+
                        (width*c),0+(height*r),width-1,height-1))
            }
        }
    }

    fun onSelectMak(view: View) {
        val intent = Intent(this,Character_view::class.java)
        intent.putExtra("CharacterName","mak")
        //intent.putExtra("Load",false)
        startActivity(intent);
    }
}