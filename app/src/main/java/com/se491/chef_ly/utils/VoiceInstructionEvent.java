package com.se491.chef_ly.utils;

/**
 * Created by admin on 4/19/2017.
 */

public class VoiceInstructionEvent {
    private final String nextInstruction;

    public VoiceInstructionEvent(String nextInstruction){
        this.nextInstruction = nextInstruction;
    }

    public String getInstruction(){
        return nextInstruction;
    }



}
