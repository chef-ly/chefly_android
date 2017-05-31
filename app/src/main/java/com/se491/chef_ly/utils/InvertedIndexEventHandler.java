package com.se491.chef_ly.utils;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static com.se491.chef_ly.utils.InvertedIndex.queryIndex;

/**
 * Created by admin on 5/30/2017.
 */

public class InvertedIndexEventHandler {

    private static InvertedIndex invertedIndex = new InvertedIndex();

    public InvertedIndexEventHandler() {
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(AddToIndexEvent event){
        for(Object phrase:event.getListToAdd()) {
            invertedIndex.indexPhrase(phrase.toString());
            Log.d("DEBUG", "Loading to Inverted Index: " + phrase.toString());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QuestionEvent event){
        List answers = invertedIndex.queryIndex(event.getQuestion());
        Log.e("DEBUG", "Answering: "+answers.get(0).toString());
        EventBus.getDefault().post(new AnswerEvent(answers.get(0).toString()));
    }

}
