package com.glasswellapps.iact.effects;

import com.glasswellapps.iact.RandomSelector;
import com.glasswellapps.iact.effects.Sounds;

public class SoundSelector {
    int[] soundIDs;
    int[] frequencies;
    public SoundSelector(int[] soundIDs, int[] frequencies) {
        this.soundIDs = soundIDs;
        this.frequencies = frequencies;
    }
    public int playRandom(){
        int soundID = soundIDs[RandomSelector.selectRandomIndex(frequencies)];
        Sounds.INSTANCE.play(soundID);
        return soundID;
    }
    public int playRandom(float loudness){
        int soundID = soundIDs[RandomSelector.selectRandomIndex(frequencies)];
        Sounds.INSTANCE.play(soundID,loudness);
        return soundID;
    }
}
