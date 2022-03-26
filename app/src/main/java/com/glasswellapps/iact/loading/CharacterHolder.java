package com.glasswellapps.iact.loading;
import com.glasswellapps.iact.characters.Character;

import java.util.ArrayList;

public class CharacterHolder {
    private static Character activeCharacter;
    public static Character getActiveCharacter(){
        return activeCharacter;
    }
    public static void setActiveCharacter(Character c) {
        activeCharacter = c;
    }
    public static void clearActiveCharacter(){
        if(activeCharacter == null){
            return;
        }
        activeCharacter.clearImages();
        activeCharacter = null;
    }


    private static ArrayList<Character> party;
    public static  ArrayList<Character> getParty(){
        return party;
    }

    public static boolean isInParty(String characterName){
        if(CharacterHolder.party != null){
            for(int i = 0; i < party.size(); i++) {
                if(characterName.equals(party.get(i).getName_short())){
                    return true;
                }
            }
        }
        return false;
    }

    public static void addToParty(Character character){
        if(party == null){
            party = new ArrayList<Character>();
        }
        if(party.contains(character)){
            return;
        }
        party.add(character);
    }
    public static void removeFromParty(Character character){
        if(party == null){
            return;
        }
        character.clearImages();
        party.remove(character);
    }
    public static void clearParty(){
        if(party == null){
            return;
        }
        clearAllImages();
        party.clear();
        party = null;

    }
    public static void clearAllImages(){
        if(activeCharacter!=null) {
            activeCharacter.clearImages();
        }
        if(party ==null){
            return;
        }
        for (Character character: party) {
            if(character!=null) {
                character.clearImages();
            }
        }
    }
    static boolean isInteractable = true;
    public static boolean getIsInteractable(){return isInteractable;}
    public static void setIsInteractable(boolean interactable) {
        isInteractable = interactable;
    }
}
