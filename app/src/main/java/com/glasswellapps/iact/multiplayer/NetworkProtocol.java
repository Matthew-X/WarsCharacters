package com.glasswellapps.iact.multiplayer;
import android.app.Activity;

import com.glasswellapps.iact.character_screen.controllers.BackgroundController;
import com.glasswellapps.iact.characters.Character;

import java.util.Arrays;

public class NetworkProtocol {


    public static String createID(){
        String time = String.valueOf(System.currentTimeMillis());
        return time.substring(time.length()-1- MessageLength.ID,time.length()-1);
    }

    public static int onReceiveMessage(byte[] bytes, Player[] playerList,
                                       Activity context) {
        System.out.println("PACKET SIZE: "+bytes.length);
        String id = getIDFromMessage(bytes);
        Integer characterIndex = getCharacterIndexFromMessage(bytes);

        System.out.println("DISCONNECT?" + bytes[MessageLength.ID] + " " + ((int)bytes[MessageLength.ID] == Codes.DISCONNECT));
        if((int)bytes[MessageLength.ID] == Codes.DISCONNECT){
            return Codes.DISCONNECT;
        }

        Player sender = null;
        int emptyIndex = Codes.PARTY_FULL;

        for (int i = playerList.length - 1; i >=0; i--) {
            if (playerList[i].id.equals(id) && playerList[i].getCharacterIndex() == characterIndex) {
                sender = playerList[i];
                sender.updateCharacter(bytes);
                return Codes.UPDATE;
            }
            else if (playerList[i].character == null){
                emptyIndex = i;
            }
        }
        if(sender == null && emptyIndex!=Codes.PARTY_FULL){
            playerList[emptyIndex].newCharacter(bytes,context);
            return emptyIndex;
        }
        return Codes.PARTY_FULL;
    }

    public static String getIDFromMessage(byte[] bytes){
        return new String(Arrays.copyOfRange(bytes, 0, MessageLength.ID));
    }
    public static int getCharacterIndexFromMessage(byte[] bytes){
        return (int)bytes[Codes.CHARACTER];
    }


    public static byte[] sendCharacter(String networkID, Character character, boolean updateImages) {
        byte[] message = new byte[MessageLength.getAll()];
        putNetworkID(message,networkID);
        putCharacter(message,character);
        putActivated(message,character);
        putBackground(message,character);
        putConditions(message,character);
        putXP(message,character);
        putWeapon(message,character);
        putArmour(message,character);
        putAccessories(message,character);
        putRewards(message,character);
        putDamageStrain(message,character);
        message[Codes.UPDATE_IMAGES] = boolToByte(updateImages);
        return message;
    }
    static void putNetworkID(byte[] message, String networkID){
        byte[] byteID = networkID.getBytes();
        System.arraycopy(byteID, 0, message, 0, MessageLength.ID);
    }
    static void putCharacter(byte[] message, Character character){
        message[Codes.CHARACTER] = (byte)character.getIndex();
    }
    static void putBackground(byte[] message, Character character){
        message[Codes.BACKGROUND] = (byte)BackgroundController.Companion.getBackgroundIndex(character.getBackground());
    }
    static void putActivated(byte[] message, Character character){
        message[Codes.ACTIVATED] = (byte)(character.isActivated()?1:0);
    }
    static void putDamageStrain(byte[] message, Character character){
        message[Codes.DAMAGE_STRAIN] = (byte)character.getDamage();
        message[Codes.DAMAGE_STRAIN+1] = (byte)character.getStrain();
        message[Codes.DAMAGE_STRAIN+2] = (byte)character.getLastEffect();
        character.setLastEffect(0);
    }
    static void putConditions(byte[] message, Character character){
        boolArrayIntoBytes(character.getConditionsActive(), message, Codes.CONDITIONS);
    }
    static void putXP(byte[] message, Character character){
        boolArrayIntoBytes(character.getXpCardsEquipped(), message, Codes.XP);
    }
    static void putWeapon(byte[] message, Character character){
        for(int i = 0; i < MessageLength.WEAPONS; i++) {
            if(i<character.getWeapons().size()){
                message[Codes.WEAPONS + i] = character.getWeapons().get(i).byteValue();
            }
            else{
                message[Codes.WEAPONS + i] = (byte) Codes.EMPTY;
            }
        }
    }
    static void putArmour(byte[] message, Character character){
        if(character.getArmor().isEmpty()){
            message[Codes.ARMOUR] = (byte) Codes.EMPTY;
        }
        else{
            message[Codes.ARMOUR] = character.getArmor().get(0).byteValue();
        }
    }
    static void putAccessories(byte[] message, Character character){
        for(int i = 0; i < MessageLength.ACC; i++) {
            if(i<character.getAccessories().size()){
                message[Codes.ACC + i] = character.getAccessories().get(i).byteValue();
            }
            else{
                message[Codes.ACC + i] = (byte) Codes.EMPTY;
            }
        }
    }
    static void putRewards (byte[] message, Character character){
        boolean heroicEquipped = character.getRewards().contains(5);
        message[Codes.REWARDS] = boolToByte(heroicEquipped);

        boolean legendaryEquipped = character.getRewards().contains(11);
        message[Codes.REWARDS+1] = boolToByte(legendaryEquipped);
    }
    static void boolArrayIntoBytes(boolean[] bools, byte[] bytes, int start){
        for(int i = 0; i < bools.length;i++){
            bytes[start+i] = boolToByte(bools[i]);
        }
    }
    static byte boolToByte(boolean b){
        return (byte)(b?1:0);
    }


    public static void disconnect(BluetoothManager bluetoothManager) {
        byte[] message = new byte[MessageLength.ID+1];
        putNetworkID(message,bluetoothManager.id);
        message[MessageLength.ID] = Codes.DISCONNECT;
        bluetoothManager.sendMessageToAll(message);//, id);
    }

    public static int onClientReceiveMessage(byte[] message, BluetoothManager bluetoothManager) {
        // messages to all
        if((int)message[0] == Codes.GAMEOVER){
            return Codes.GAMEOVER;
        }

        if((int)message[MessageLength.ID] == Codes.DISCONNECT){
            return Codes.DISCONNECT;
        }

        // messages to specific client
        if (!NetworkProtocol.getIDFromMessage(message).equals(bluetoothManager.id)) {
            //return NONE;
        }

        return Codes.NONE;
    }
}
