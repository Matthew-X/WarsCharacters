package com.glasswellapps.iact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.glasswellapps.iact.effects.Sounds;
import com.glasswellapps.iact.multiplayer.ImperialScreen;
import com.glasswellapps.iact.multiplayer.RebelScreen;

public class NewHeroScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_hero_screen);
        findViewById(R.id.single_hero_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toCharacterListScreen();
            }
        });
        findViewById(R.id.multiple_hero_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toMultiplayerScreen();
            }
        });
    }
    void toCharacterListScreen(){
        Sounds.INSTANCE.selectSound();
        Intent intent = new Intent(this, Characters_list.class);
        intent.putExtra("from","main");
        startActivity(intent);
    }
    void toMultiplayerScreen(){
        Sounds.INSTANCE.selectSound();
        Intent intent = new Intent(this, RebelScreen.class);
        intent.putExtra("from","new");
        startActivity(intent);
    }
}