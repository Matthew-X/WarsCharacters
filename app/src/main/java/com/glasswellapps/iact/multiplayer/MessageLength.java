package com.glasswellapps.iact.multiplayer;

public class MessageLength {
    //Message lengths
    public final static int ID = 4;
    public final static int CHARACTER = 1;//byte
    public final static int BACKGROUND = 1;
    public final static int ACTIVATED = 1;//byte
    //Update stats
    public final static int DAMAGE_STRAIN = 3;//byte value and effect
    public final static int CONDITIONS = 5;//byte
    public final static int REWARDS = 2;
    //Update stats and images
    public final static int XP = 9;//2bytes
    public final static int WEAPONS = 2;//byte
    public final static int ARMOUR = 1;//byte
    public final static int ACC = 3;
    public final static int UPDATE_IMAGES = 1;

    public static int get(int code){
        switch (code){
            case Codes.CHARACTER: return CHARACTER;
            case Codes.BACKGROUND: return BACKGROUND;
            case Codes.ACTIVATED:return ACTIVATED;
            case Codes.DAMAGE_STRAIN: return DAMAGE_STRAIN;
            case Codes.CONDITIONS: return CONDITIONS;
            case Codes.REWARDS: return REWARDS;
            case Codes.XP: return XP;
            case Codes.WEAPONS: return WEAPONS;
            case Codes.ARMOUR: return ARMOUR;
            case Codes.ACC: return ACC;
        }
        return 0;
    }

    public static int getAll(){
        return ID+CHARACTER+BACKGROUND+ACTIVATED+DAMAGE_STRAIN+CONDITIONS+XP+WEAPONS+ARMOUR+ACC+REWARDS+UPDATE_IMAGES;
    }
}
