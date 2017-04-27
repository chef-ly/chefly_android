package com.se491.chef_ly.utils;

/**
 * Created by admin on 4/19/2017.
 */

public class VoiceInstructionEvent {
    private final boolean nextInstruction;

    public VoiceInstructionEvent(boolean nextInstruction){
        this.nextInstruction = nextInstruction;
    }

    public boolean getInstruction(){
        return nextInstruction;
    }

}
