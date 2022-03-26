package com.glasswellapps.iact;

import java.util.ArrayList;
import java.util.Random;

public class RandomSelector {
    public static int selectRandomIndex(int[] frequencies){
        int totalFrequency = 0;
        for (int frequency:frequencies) {
            totalFrequency+=frequency;
        }

        float sum = 0;
        float random = new Random().nextFloat()*totalFrequency;

        for(int i = 0; i < frequencies.length; i++){
            sum+=frequencies[i];
            if(random < sum){
                System.out.println("Random " + totalFrequency + " " + sum +  " " + random + " " + i);
                return i;
            }
        }
        return frequencies.length-1;
    }
}
