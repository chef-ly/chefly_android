package com.se491.chef_ly.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.se491.chef_ly.R;
import com.se491.chef_ly.model.RecipeInformation;
import com.se491.chef_ly.model.RecipeList;

public class ListViewFragment extends Fragment {

    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "pageNum";
    private static final String TAG = "LISTVIEW_FRAG";

    private ListView listView;
    private RecipeList list;
    private View emptyView;

    private String title;
    private String pageNum;

    private OnFragmentInteractionListener mListener;

    public ListViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListViewFragment newInstance(String param1, String param2) {
        ListViewFragment fragment = new ListViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            title = savedInstanceState.getString("title");
            pageNum = savedInstanceState.getString("pageNum");
            list = savedInstanceState.getParcelable(title);
        }else{
            if (getArguments() != null) {
                title = getArguments().getString("title");
                pageNum = getArguments().getString("pageNum");
                //list = getArguments().getParcelableArrayList("list");
            }
            if(list == null){
                list = new RecipeList();
            }
        }



        Log.d(TAG, "results -> " + title + " " + pageNum );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_list_view, container, false);

        listView = (ListView)  v.findViewById(R.id.list);
        emptyView = v.findViewById(R.id.emptyList);

        if(title.equals("Favorites")){
            emptyView.setBackground(getResources().getDrawable(R.drawable.emptylist, null));
        }else{
            emptyView.setBackground(getResources().getDrawable(R.drawable.emptylistwelcome, null));
        }

        listView.setAdapter(new ListViewFragment.RecipeAdapter(getContext(), list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView l, View v, int position, long id) {
                Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
                RecipeInformation recipe = ((RecipeInformation) l.getAdapter().getItem(position));
                //intent.putExtra("recipe", String.valueOf(recipeID));
                intent.putExtra("recipeDetail", recipe);
                Log.d(TAG, "Recipe Clicked: id -> " + recipe.getId());
                startActivity(intent);
            }
        });
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                listView.setEmptyView(emptyView);
            }
        }, 500);

        // Inflate the layout for this fragment
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
    protected interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        RecipeList r = ((RecipeAdapter)listView.getAdapter()).getRecipes();
        outState.putString("title", title);
        outState.putString("pageNum", pageNum);
        outState.putParcelable(title,r);
    }

    public String getTitle(){
        return title;
    }

    public void updateListAdapter(RecipeList newList){
        if(list == null){
            list = new RecipeList();
        }
        for(RecipeInformation r : newList){
            list.add(r);
        }

        if(listView != null) {
            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            Log.d(TAG, "ListView updated " + title + " " + listView.getAdapter().getCount() + " list " + list.size());
            //listView.setVisibility(View.VISIBLE);


        }


    }

    static protected class RecipeAdapter extends BaseAdapter {
        private final Context context;
        private final LayoutInflater inflater;
        private RecipeList recipes;
        static class ViewHolder{
            final ImageView icon;
            final TextView name;
            final ImageView rating;

            ViewHolder(ImageView ic, TextView na, ImageView ra){
                this.icon = ic;
                this.name = na;
                this.rating = ra;
            }
        }

        RecipeAdapter(Context context, RecipeList recipes){
            this.context = context;
            this.recipes = recipes;
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        protected RecipeList getRecipes(){
            return recipes;
        }
        @Override
        public int getCount() {
            return recipes.size();
        }

        @Override
        public Object getItem(int position) {
            return recipes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            final ViewHolder holder;
            if(convertView == null){
                row = inflater.inflate(R.layout.recipe_list_item, parent, false);
                holder = new ViewHolder(
                        (ImageView) row.findViewById(R.id.image),
                        (TextView) row.findViewById(R.id.recipeName),
                        (ImageView) row.findViewById(R.id.recipeRating)
                );

                row.setTag(holder);
            }else{
                holder = (ViewHolder) row.getTag();
            }

            RecipeInformation r = recipes.get(position);
            holder.name.setText(r.getTitle());


            double rating = r.getSpoonacularScore();
            rating = Math.round(rating);
            if(rating < 1){
                holder.rating.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.nostars));
            }else if(rating < 2){
                holder.rating.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.onestars));
            }else if(rating < 3){
                holder.rating.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.twostars));
            }else if(rating < 4){
                holder.rating.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.threestars));
            }else if(rating < 5){
                holder.rating.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fourstars));
            }else{
                holder.rating.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fivestars));
            }



            try{
                String image =r.getImage(); //take the url
                Log.d(TAG, "Image URL --> " + image);
                if(!image.isEmpty()){
                    Glide.with(context)
                            .load(image)
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    Log.d(TAG, "Error loading image -> " + e.getMessage());
                                    return false;
                                }
                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    Log.d(TAG, "Image loaded!");
                                    return false;
                                }
                            })
                            .error(R.drawable.noimageavailable)
                            .placeholder(R.drawable.circular_gradient)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .into(holder.icon);
                }else{
                    holder.icon.setImageResource(R.drawable.noimageavailable);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return row;
        }

    }

}
