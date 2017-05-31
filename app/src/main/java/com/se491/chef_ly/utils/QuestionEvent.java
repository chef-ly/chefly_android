package com.se491.chef_ly.utils;

/**
 * Created by admin on 5/30/2017.
 */

public class QuestionEvent {
    private final String Question;

    public QuestionEvent(String question) {
        this.Question = question;
    }

    public String getQuestion(){
        return this.Question;
    }
}
