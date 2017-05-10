package com.se491.chef_ly.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.se491.chef_ly.R;


public class IntroFragment extends Fragment{

    private static final String ARG_PARAM1 = "pageNum";
    private static final String ARG_PARAM2 = "layout";

    private int pageNum;
    private int imageID;

    private OnFragmentInteractionListener mListener;

    public IntroFragment() {
        // Required empty public constructor
    }

    public static IntroFragment newInstance(int param1, int param2) {
        IntroFragment fragment = new IntroFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageNum = getArguments().getInt(ARG_PARAM1);
            imageID = getArguments().getInt(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the imageID for this fragment
        View v =  inflater.inflate(R.layout.fragment_intro, container, false);

        ImageView image = (ImageView) v.findViewById(R.id.imageView);
        Glide.with(getContext())
                .load(imageID)
                .asGif()
                .placeholder(R.drawable.circular_gradient)
                .into(image);


        return v;
    }

    public void getPageNum(int page) {
        if (mListener != null) {
            mListener.onFragmentInteraction(pageNum);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
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
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int page);
    }
}
