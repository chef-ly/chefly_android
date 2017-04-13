package com.se491.chef_ly.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.se491.chef_ly.R;
import com.se491.chef_ly.model.Recipe;

import java.util.ArrayList;
import java.util.List;


public class ListViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "pageNum";
    private static final String TAG = "LISTVIEW_FRAG";

    private ListView listView;
    private ArrayList<Recipe> list;
    private View emptyView;

    // TODO: Rename and change types of parameters
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
        list = new ArrayList<>();
        if (getArguments() != null) {
            title = getArguments().getString("title");
            pageNum = getArguments().getString("pageNum");
            //list = getArguments().getParcelableArrayList("list");
        }
        Log.d(TAG, "results -> " + title + " " + pageNum );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_list_view, container, false);

        listView = (ListView)  v.findViewById(R.id.list);
        emptyView = v.findViewById(R.id.emptyList);

        listView.setAdapter(new ListViewFragment.RecipeAdapter(getContext(), list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView l, View v, int position, long id) {
                Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
                String recipeID = ((Recipe) l.getAdapter().getItem(position)).getId();
                intent.putExtra("recipe", recipeID);
                Log.d(TAG, "Recipe Clicked: id -> " + recipeID);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public String getTitle(){
        return title;
    }
    public void updateListAdapter(ArrayList<Recipe> newList){
        if(list != null){
            for(Recipe r : newList){
                list.add(r);
            }
        }else{
            Log.d(TAG, "Attempted to access arrayList before it could be initialized");
        }

        if(listView != null) {
            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            Log.d(TAG, "ListView updated " + title + " " + listView.getAdapter().getCount() + " list " + list.size());
            //listView.setVisibility(View.VISIBLE);


        }


    }

    static private class RecipeAdapter extends BaseAdapter {
        private final Context context;
        private final LayoutInflater inflater;
        //private SparseBooleanArray likes =new SparseBooleanArray();
        private List<Recipe> recipes = new ArrayList<>();
        static class ViewHolder{
            final ImageView icon;
            //final ImageButton like;
            final TextView name;
            final ImageView rating;
            // ViewHolder(ImageView ic, ImageButton ib, TextView na, ImageView ra){
            ViewHolder(ImageView ic, TextView na, ImageView ra){
                this.icon = ic;
                //this.like = ib;
                this.name = na;
                this.rating = ra;
            }
        }

        RecipeAdapter(Context context, ArrayList<Recipe> recipes){
            this.context = context;
            this.recipes = recipes;
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
                        //(ImageButton) row.findViewById(R.id.likeBtn),
                        (TextView) row.findViewById(R.id.recipeName),
                        (ImageView) row.findViewById(R.id.recipeRating)
                );

                // Listview does not operate correctly if there are focusable elements in it
                //holder.like.setFocusableInTouchMode(false);
                //holder.like.setFocusable(false);

                //holder.like.setOnClickListener(new LikeListener(position));

                row.setTag(holder);
            }else{
                holder = (ViewHolder) row.getTag();
            }

            Recipe r = recipes.get(position);
            holder.name.setText(r.getName());

            // TODO
            //if(isFavorite(r.getId())){
            //    holder.like.setActivated(true);
            //}
            /*
            if(likes.get(position)){
                holder.like.setImageResource(R.drawable.heartselected);
                //likes.put(position, true);
            }else{
                holder.like.setImageResource(R.drawable.heartunselected);
                //likes.put(position, false);
            }
            */





            double rating = r.getRating();
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
                Uri uri=r.getImage(); //take the url
                String image = uri.toString(); //make the url into a string
                if(!image.isEmpty()){
                    image = image.substring(1, image.length()-1);  //remove the quotes from uri string
                    final String tag = "ListView";
                    Glide.with(context)
                            .load(image).asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .dontAnimate()
                            .listener(new RequestListener<String, Bitmap>() {
                                @Override
                                public boolean onException(Exception e, String model, com.bumptech.glide.request.target.Target<Bitmap> target, boolean isFirstResource) {
                                    Log.d(tag, e.getMessage());
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, String model, com.bumptech.glide.request.target.Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    Log.d(tag, "Image resource ready");
                                    return false;
                                }
                            })
                            .error(R.drawable.noimageavailable)
                            .into(holder.icon);
                }else{
                    holder.icon.setImageResource(R.drawable.noimageavailable);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return row;
        }
        class LikeListener implements ImageButton.OnClickListener {
            private int position;
            LikeListener(int pos){
                position = pos;
                //likes.put(position,false);
                Log.d(TAG, "position -> " +position );
            }
            @Override
            public void onClick(View v) {
                //TODO add logic to like/add to favorites
                //boolean state = likes.get(position, false);
                //if(state){
                //    ((ImageButton) v).setImageResource(R.drawable.heartunselected);
                //    likes.put(position, false);
                //}else{
                //    ((ImageButton) v).setImageResource(R.drawable.heartselected);
                //    likes.put(position, true);
                //}
            }

        }
    }

}
