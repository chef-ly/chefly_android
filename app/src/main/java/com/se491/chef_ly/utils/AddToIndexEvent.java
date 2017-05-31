package com.se491.chef_ly.utils;

import java.util.List;

/**
 * Created by admin on 5/30/2017.
 */

public class AddToIndexEvent {
    private final List<String> listToAdd;

    public AddToIndexEvent(List listToAdd){
        this.listToAdd = listToAdd;
    }

    public List getListToAdd(){
        return listToAdd;
    }
}
