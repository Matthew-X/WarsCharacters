package com.glasswellapps.iact.multiplayer;

public class Codes {
    public final static int DISCONNECT = 127;
    public final static int EMPTY = 126;
    // identifier and position in message array
    public final static int CHARACTER = 4;//1 byte
    public final static int BACKGROUND = 5;//1 byte
    public final static int ACTIVATED = 6;//1 byte
    //Update stats
    public final static int DAMAGE_STRAIN = 7;//3 byte values and effect
    public final static int CONDITIONS = 10;//5 bytes
    //Update stats and images
    public final static int XP = 15;//9bytes
    public final static int WEAPONS = 24;//2bytes
    public final static int ARMOUR = 26;//1 byte
    public final static int ACC = 27; //3 bytes
    public final static int REWARDS = 30;//2 bytes
    public final static int UPDATE_IMAGES = 32;
    public final static int GAMEOVER = 33;

    public final static int PARTY_FULL = -1;
    public final static int UPDATE = -2;
    public final static int NONE = -3;
}
