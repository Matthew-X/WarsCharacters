package com.glasswellapps.iact

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.glasswellapps.iact.effects.Sounds
import com.glasswellapps.iact.loading.CharacterHolder
import com.glasswellapps.iact.multiplayer.ImperialScreen
import com.glasswellapps.iact.multiplayer.RebelScreen
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        Sounds.reset(this)

        new_button.setOnClickListener {
            Sounds.selectSound()
            val intent = Intent(this, Characters_list::class.java)
            intent.putExtra("from", "main")
            startActivity(intent)
        }
        loadButton.setOnClickListener {
            Sounds.selectSound()
            val intent = Intent(this, LoadScreen::class.java)
            intent.putExtra("from","main")
            startActivity(intent)
        }
        partyButton.setOnClickListener {
            Sounds.selectSound()
            val intent = Intent(this, RebelScreen::class.java)
            intent.putExtra("from","main")
            startActivity(intent)
        }
        imperialButton.setOnClickListener {
            Sounds.selectSound()
            val intent = Intent(this, ImperialScreen::class.java)
            intent.putExtra("from","main")
            startActivity(intent)
        }
        createButton.setOnClickListener {
            Sounds.selectSound()
            val intent = Intent(this, CreateScreen::class.java)
            intent.putExtra("from","main")
            startActivity(intent)
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);


    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        //clear memory of all character data and character images
        CharacterHolder.clearParty()
        CharacterHolder.clearActiveCharacter()
    }
}