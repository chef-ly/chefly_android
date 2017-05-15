package com.se491.chef_ly.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.se491.chef_ly.R;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.getColor;

public class DialogPopUp extends DialogFragment{
    private boolean showTitle;
    public DialogPopUp(){}

    public static DialogPopUp newInstance(String title, ArrayList<String> data){
        DialogPopUp popUp = new DialogPopUp();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putStringArrayList("data", data);

        popUp.setArguments(args);
        return popUp;
    }

    public void showTitle(boolean show){
        showTitle = show;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dialog_popup, container);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Remove title bar from dialog
        Window w = getDialog().getWindow();
        if(w != null){
            w.requestFeature(Window.FEATURE_NO_TITLE);
        }

        if(showTitle){
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(getArguments().getString("title"));
            title.setVisibility(View.VISIBLE);
        }

        Button exit = (Button) view.findViewById(R.id.exit);
        exit.bringToFront();
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        TableLayout table = (TableLayout) view.findViewById(R.id.table);
        ArrayList<String> data = getArguments().getStringArrayList("data");

        Context c = getContext();
        TableLayout.LayoutParams tableRowParams=
                new TableLayout.LayoutParams
                        (TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);

        int leftMargin=30;
        int topMargin=1;
        int rightMargin=55;
        int bottomMargin=1;

        tableRowParams.setMargins(leftMargin,topMargin,rightMargin,bottomMargin);

        int color1 = getColor(c, R.color.table_color1);
        int color2 = getColor(c, R.color.table_color2);
        int count = 0;

        if(data != null){
            for(String s : data){
                TableRow row = new TableRow(getContext());
                row.setLayoutParams(tableRowParams);
                row.setBackgroundColor(count%2 == 0 ? color1 : color2);
                row.setPadding(10,5,10,5);
                TextView text = new TextView(c);
                text.setText(s);
                text.setTextColor(getColor(c, R.color.black));
                text.setTextSize((getResources().getDimension(R.dimen.text_small) / getResources().getDisplayMetrics().density));
                text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                text.setPadding(10,5,10,5);

                row.addView(text);
                table.addView(row);
                count++;
            }
        }else{
            TableRow row = new TableRow(getContext());
            row.setLayoutParams(tableRowParams);
            row.setBackgroundColor(count%2 == 0 ? color1 : color2);
            row.setPadding(10,5,10,5);
            TextView text = new TextView(c);
            text.setText("No data available");
            text.setTextColor(getColor(c, R.color.black));
            text.setTextSize((getResources().getDimension(R.dimen.text_medium) / getResources().getDisplayMetrics().density));
            text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            text.setPadding(10,5,10,5);

            row.addView(text);
            table.addView(row);
        }

    }
}
