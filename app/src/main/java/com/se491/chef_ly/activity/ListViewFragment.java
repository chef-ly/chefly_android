package com.se491.chef_ly.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.auth0.android.result.Credentials;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.se491.chef_ly.R;
import com.se491.chef_ly.http.RequestMethod;
import com.se491.chef_ly.model.RecipeInformation;
import com.se491.chef_ly.model.RecipeList;
import com.se491.chef_ly.utils.CredentialsManager;
import com.se491.chef_ly.utils.GetRecipesFromServer;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class ListViewFragment extends Fragment implements LoaderManager.LoaderCallbacks<RecipeList>{

    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "pageNum";
    private static final String TAG = "LISTVIEW_FRAG";

    private static final String urlString ="https://chefly-prod.herokuapp.com/list/random/5";

    private ListView listView;
    private RecipeList list;
    private View emptyView;

    private static  String title;
    private String pageNum;
    private AtomicBoolean isLoading;

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

        isLoading = new AtomicBoolean(false);

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

        listView.setAdapter(new ListViewFragment.RecipeAdapter(getContext(), list, mListener));
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

        final LoaderManager.LoaderCallbacks callbacks = this;
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //TODO remove limit and or wait for user to scroll past end of list to load more
                if(firstVisibleItem+visibleItemCount + 2 == totalItemCount && totalItemCount!=0  && totalItemCount < 25) {

                    if(!isLoading.get()){
                        Log.d(TAG, "Getting more recipes from server");
                        isLoading.set(true);

                        RequestMethod requestPackage = new RequestMethod();
                        requestPackage.setEndPoint(urlString);
                        requestPackage.setMethod("GET"); //  or requestPackage.setMethod("POST");

                        Bundle bundle = new Bundle();
                        bundle.putParcelable("requestPackage", requestPackage);

                        getLoaderManager().initLoader((new Date()).hashCode() , bundle, callbacks).forceLoad();
                    }
                }
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

    public void onRecipeLiked(RecipeInformation recipe, boolean b) {
        if (mListener != null) {
            mListener.onFragmentInteraction(recipe , b);
        }
    }

    public int  getListSize(){
        return list.size();
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


    protected interface OnFragmentInteractionListener {

        void onFragmentInteraction(RecipeInformation recipe, boolean add);
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

    public void addRecipe(RecipeInformation r){
        list.add(r);
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }
    public void removeRecipe(RecipeInformation r){
        list.remove(r);
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }
    public void removeFavorite(RecipeInformation r){
        int index = list.indexOf(r);
        list.get(index).setFavorite(false);
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    //  LoaderManager callback method
    @Override
    public Loader<RecipeList> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "Loader Created");
        RequestMethod rm = args.getParcelable("requestPackage");
        return  new GetRecipesFromServer(getContext(), rm);
    }
    //  LoaderManager callback method
    @Override
    public void onLoadFinished(Loader<RecipeList> loader, RecipeList data) {
        Log.d(TAG, "Loader - OnLoadFinish");
        for(RecipeInformation recipe : data){
            list.add(recipe);
        }

        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
        isLoading.set(false);
        Log.d(TAG, "Getting more recipes from server --- Done");
    }
    //  LoaderManager callback method
    @Override
    public void onLoaderReset(Loader<RecipeList> loader) {

    }

    static private class RecipeAdapter extends BaseAdapter {
        private final Context context;
        private final LayoutInflater inflater;
        private RecipeList recipes;
        private OnFragmentInteractionListener passer;
        static class ViewHolder{
            final ImageView icon;
            final TextView name;
            final ImageView rating;
            final ImageButton favBtn;

            ViewHolder(ImageView ic, TextView na, ImageView ra, ImageButton ib){
                this.icon = ic;
                this.name = na;
                this.rating = ra;
                this.favBtn = ib;

                favBtn.setFocusable(false);
                favBtn.setFocusableInTouchMode(false);
            }
        }

        RecipeAdapter(Context context, RecipeList recipes, OnFragmentInteractionListener passer){
            this.context = context;
            this.recipes = recipes;
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.passer = passer;

        }
        RecipeList getRecipes(){
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
        public View getView(int position, View convertView, final ViewGroup parent) {
            View row = convertView;
            final ViewHolder holder;
            if(convertView == null){
                row = inflater.inflate(R.layout.recipe_list_item, parent, false);
                holder = new ViewHolder(
                        (ImageView) row.findViewById(R.id.image),
                        (TextView) row.findViewById(R.id.recipeName),
                        (ImageView) row.findViewById(R.id.recipeRating),
                        (ImageButton) row.findViewById(R.id.favBtn)
                );

                row.setTag(holder);
            }else{
                holder = (ViewHolder) row.getTag();
            }

            final RecipeInformation r = recipes.get(position);
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

            holder.favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG + title, "Recipe ID -> " + r.getId());
                    if(holder.favBtn.isActivated()){
                        r.setFavorite(false);
                        holder.favBtn.setActivated(false);
                        passer.onFragmentInteraction(r,false);

                        Log.d(TAG + title , "passer.passRecipe(r,false); " + r.getId());
                        //todo notify server not favorite, remove recipe from favorite list
                        //Credentials c = CredentialsManager.getCredentials(context);
                        //String token = c.getAccessToken();


                    }else{
                        r.setFavorite(true);
                        holder.favBtn.setActivated(true);
                        passer.onFragmentInteraction(r, true);
                        Log.d(TAG + title, "passer.passRecipe(r,true); " + r.getId());
                        //todo notify server favorite, send recipe to favorite list
                    }
                }
            });
            holder.favBtn.setActivated(r.isFavorite());

            try{
                String image =r.getImage(); //take the url
                Log.d(TAG, "Image URL --> " + image);
                if(!image.isEmpty()){
                    Glide.with(context)
                            .load(image)
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
