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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.se491.chef_ly.R;
import com.se491.chef_ly.model.Ingredient;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ThirdFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThirdFragment extends Fragment {
    private String title;
    //private int pageNum;

    private ArrayList<View[]> ingredients;
    private ArrayList<LinearLayout> ingredientRows;
    private LinearLayout buttons;
    private ArrayAdapter<String> arrayAdapter;

    private OnFragmentInteractionListener mListener;

    public ThirdFragment() {
        // Required empty public constructor
    }

    public static ThirdFragment newInstance(String param1, int param2) {
        ThirdFragment fragment = new ThirdFragment();
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
        arrayAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.unitsOfMeasure));

        ingredients = new ArrayList<>();
        ingredientRows = new ArrayList<>();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v =  inflater.inflate(R.layout.fragment_third, container, false);
        final LinearLayout root = (LinearLayout) v.findViewById(R.id.root);

        EditText ingredientQty;
        AutoCompleteTextView ingredientUom;
        EditText ingredientName;
        Button newLineBtn;
        Button removeLineBtn;

        ingredientQty = (EditText) v.findViewById(R.id.ingredientQty);
        ingredientUom = (AutoCompleteTextView) v.findViewById(R.id.ingredientUom);
        ingredientUom.setAdapter(arrayAdapter);

        ingredientName = (EditText) v.findViewById(R.id.ingredientName);
        View[] temparray = new View[]{ingredientQty, ingredientUom, ingredientName};
        ingredients.add(temparray);

        TextView fragTitle = (TextView) v.findViewById(R.id.fragTitle);
        fragTitle.setText(title);
        //  the layout holding the add and remove buttons
        buttons = (LinearLayout) v.findViewById(R.id.buttons);
        //  Button to remove the last ingredient line
        removeLineBtn = (Button) v.findViewById(R.id.removeLineBtn);
        removeLineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = ingredientRows.size();
                if(size > 0){
                    root.removeView(ingredientRows.get(size-1));
                    ingredientRows.remove(size-1);
                }else{
                    Toast.makeText(v.getContext(), "Recipe must have at least 1 ingredient", Toast.LENGTH_SHORT).show();
                }

            }
        });
        //  Button to add line for new ingredients
        newLineBtn = (Button) v.findViewById(R.id.newLineBtn);
        newLineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText[] temp = new EditText[3];
                LinearLayout ll = new LinearLayout(getActivity());
                ll.setOrientation(LinearLayout.HORIZONTAL);
                EditText newIngredientQty = new EditText(v.getContext());
                AutoCompleteTextView newIngredientUom = new AutoCompleteTextView(v.getContext());
                newIngredientUom.setAdapter(arrayAdapter);
                EditText newIngredientName = new EditText(v.getContext());

                temp[0] = newIngredientQty;
                temp[1] = newIngredientUom;
                temp[2] = newIngredientName;

                ingredients.add(temp);
                ingredientRows.add(ll);

                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f);
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);

                newIngredientQty.setLayoutParams(params1);
                newIngredientUom.setLayoutParams(params1);
                newIngredientName.setLayoutParams(params2);

                newIngredientQty.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                newIngredientUom.setInputType(InputType.TYPE_CLASS_TEXT);
                newIngredientName.setInputType(InputType.TYPE_CLASS_TEXT);

                newIngredientQty.setTextColor(getColor(v.getContext(), R.color.white));
                newIngredientUom.setTextColor(getColor(v.getContext(), R.color.white));
                newIngredientName.setTextColor(getColor(v.getContext(), R.color.white));

                newIngredientQty.setTextSize(22);
                newIngredientUom.setTextSize(22);
                newIngredientName.setTextSize(22);

                newIngredientQty.setHint("Quantity");
                newIngredientUom.setHint("Unit");
                newIngredientName.setHint("Ingredient");

                ll.addView(newIngredientQty);
                ll.addView(newIngredientUom);
                ll.addView(newIngredientName);

                root.removeView(buttons);
                root.addView(ll);
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


    public ArrayList<Ingredient> getIngredients() throws Exception{
    //TODO handle fractions ie 1/4 from user input
        ArrayList<Ingredient> result = new ArrayList<>();
        for(View[] array : ingredients){
            String qtyString = ((EditText)array[0]).getText().toString();
            double qty;
            if(qtyString.contains("/")){
                String[] split = qtyString.split("/");
                if(split.length != 2) throw new Exception("Invalid Quantity format [" + qtyString + "]" );
                qty = Double.valueOf(split[0]) / Double.valueOf(split[1]);
            }else{
                qty = Double.valueOf(qtyString);
            }
            Ingredient i = new Ingredient(((EditText)array[2]).getText().toString(), ((AutoCompleteTextView)array[1]).getText().toString(), qty);
            result.add(i);
        }

        return result;
    }

    @SuppressWarnings("deprecation")
    private static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }
}
