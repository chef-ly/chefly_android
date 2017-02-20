package com.se491.chef_ly.activity.create_recipe_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.se491.chef_ly.R;
import com.se491.chef_ly.model.Ingredient;
import com.se491.chef_ly.model.IngredientItem;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FourthFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FourthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FourthFragment extends Fragment {
    private String title;
    private int pageNum;

    private ArrayList<EditText> directions;
    private LinearLayout buttons;
    private EditText step;
    private Button newLineBtn;
    private Button removeLineBtn;

    private OnFragmentInteractionListener mListener;

    public FourthFragment() {
        // Required empty public constructor
    }

    public static FourthFragment newInstance(String param1, int param2) {
        FourthFragment fragment = new FourthFragment();
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
            pageNum = getArguments().getInt("pageNum",0);
        }

        directions = new ArrayList<>();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v =  inflater.inflate(R.layout.fragment_fourth, container, false);
        final LinearLayout root = (LinearLayout) v.findViewById(R.id.root);

        step = (EditText) v.findViewById(R.id.step);

        directions.add(step);

        TextView fragTitle = (TextView) v.findViewById(R.id.fragTitle);
        fragTitle.setText(title);
        //  the layout holding the add and remove buttons
        buttons = (LinearLayout) v.findViewById(R.id.buttons);
        //  Button to remove the last ingredient line
        removeLineBtn = (Button) v.findViewById(R.id.removeLineBtn);
        removeLineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = directions.size();
                if(size > 1){
                    root.removeView(directions.get(size-1));
                    directions.remove(size-1);
                }else{
                    Toast.makeText(v.getContext(), "Recipe must have at least 1 step", Toast.LENGTH_SHORT).show();
                }

            }
        });
        //  Button to add line for new ingredients
        newLineBtn = (Button) v.findViewById(R.id.newLineBtn);
        newLineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText newstep = new EditText(v.getContext());

                directions.add(newstep);

                newstep.setInputType(InputType.TYPE_CLASS_TEXT);

                newstep.setTextColor(getColor(v.getContext(), R.color.white));

                newstep.setTextSize(22);

                newstep.setHint("Next step...");

                root.removeView(buttons);
                root.addView(newstep);
                root.addView(buttons);
            }
        });

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


    public ArrayList<String> getDirections(){

        ArrayList<String> result = new ArrayList<>();
        for(EditText step : directions){
            result.add(step.getText().toString());
        }
        return result;
    }

    @SuppressWarnings("deprecation")
    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }
}
