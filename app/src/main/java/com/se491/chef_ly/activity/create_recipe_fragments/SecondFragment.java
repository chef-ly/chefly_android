package com.se491.chef_ly.activity.create_recipe_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.se491.chef_ly.R;
import com.se491.chef_ly.model.Level;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SecondFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment {

    private String title;
    private Level level = Level.EASY;

    private EditText recipeTime;
    private EditText recipeServings;
    private EditText recipeCategories;

    private OnFragmentInteractionListener mListener;

    public SecondFragment() {
        // Required empty public constructor
    }

    public static SecondFragment newInstance(String param1, int param2) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putString("title", param1);
        args.putInt("pageNum", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
            //pageNum = getArguments().getInt("pageNum",0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_second, container, false);
        recipeTime = (EditText) v.findViewById(R.id.recipeTime);
        recipeServings = (EditText) v.findViewById(R.id.recipeServings);
        Spinner difficultySpinner = (Spinner) v.findViewById(R.id.difficultySpinner);
        recipeCategories = (EditText) v.findViewById(R.id.recipeCategories);

        final Level[] diff = Level.values();
        ArrayAdapter<Level> spinnerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, diff);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(spinnerArrayAdapter);
        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                level = diff[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        TextView fragTitle = (TextView) v.findViewById(R.id.fragTitle);
        fragTitle.setText(title);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FirstFragment.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public String getRecipeTime(){
        Editable temp = recipeTime.getText();
        if(temp != null){
            return temp.toString();
        }else{
            return "";
        }

    }
    public Level getRecipeLevel(){  return level;}
    public String getRecipeServings(){
        Editable temp = recipeServings.getText();
        if(temp != null){
            return temp.toString();
        }else{
            return  "";
        }

    }
    public String[] getRecipeCategories(){
        //TODO  use better regex, users may not always use ','
        String temp = recipeCategories.getText().toString();
        return temp.split(",");
    }
}
