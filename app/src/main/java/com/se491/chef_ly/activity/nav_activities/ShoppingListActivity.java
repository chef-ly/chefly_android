package com.se491.chef_ly.activity.nav_activities;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.se491.chef_ly.Databases.DatabaseHandler;
import com.se491.chef_ly.R;
import com.se491.chef_ly.model.ShoppingListItem;

import java.util.ArrayList;

public class ShoppingListActivity extends ListActivity {
    private static ArrayList<ShoppingListItem> shoppingList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        setListAdapter(new myAdapter(this.getApplicationContext()));

        final DatabaseHandler handler = new DatabaseHandler(this.getApplicationContext());

        Button deletePurchasedBtn;

        deletePurchasedBtn = (Button) findViewById(R.id.clearPurchasedBtn);
        deletePurchasedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<ShoppingListItem> toDelete = new ArrayList<>();

                for(ShoppingListItem item : shoppingList){
                    if(item.isPurchased()){
                        toDelete.add(item);
                    }
                }
                if(toDelete.size() > 0){
                    for(ShoppingListItem item: toDelete){
                        shoppingList.remove(item);
                    }
                    DatabaseHandler h = new DatabaseHandler(getApplicationContext());
                    h.deleteItemFromShoppingList(toDelete);
                    ((BaseAdapter) getListAdapter()).notifyDataSetChanged();
                }

            }
        });

        final Button add = (Button) findViewById(R.id.addBtn);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText newItem = new EditText(getApplicationContext());
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 150);
                params.addRule(RelativeLayout.BELOW, add.getId());
                params.setMargins(20,2,20,2);
                newItem.setLayoutParams(params);
                newItem.setTextColor( ContextCompat.getColor(getApplicationContext(), R.color.black));
                newItem.setHint("new grocery list item");
                newItem.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.color.white));
                newItem.setInputType(InputType.TYPE_CLASS_TEXT);
                newItem.setImeOptions(EditorInfo.IME_ACTION_DONE);
                newItem.setMaxLines(1);
                RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_shopping_list) ;
                layout.addView(newItem);

                newItem.setOnFocusChangeListener(new View.OnFocusChangeListener(){
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        String text = newItem.getText().toString();
                        if(!hasFocus && text.length() > 0){
                            addText(text);
                            newItem.setText("");
                            newItem.setVisibility(View.GONE);
                        }
                    }
                });
                newItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                        if(actionId == EditorInfo.IME_ACTION_DONE ){

                            InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            String text = newItem.getText().toString();
                            if(!text.isEmpty()){
                                addText(text);
                                newItem.setText("");
                            }

                            newItem.setVisibility(View.GONE);

                            return true;
                        }
                        return false;
                    }
                });
            }
        });



        shoppingList = handler.getShoppingList();
    }

    private void addText(String text){

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        db.addItemToShoppingList(text, false);
        shoppingList = db.getShoppingList();

        ((BaseAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        TextView name = (TextView) v.findViewById(R.id.name);
        ShoppingListItem item = shoppingList.get(position);

        if(item.isPurchased()){
            item.setPurchased(false);
            name.setPaintFlags(name.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            item.setPurchased(true);
            name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        ((BaseAdapter) getListAdapter()).notifyDataSetChanged();

    }

    private static class myAdapter extends BaseAdapter{
        private final LayoutInflater inflater;

        myAdapter(Context context){
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return shoppingList.size();
        }

        @Override
        public Object getItem(int position) {
            return shoppingList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ShoppingListItem item = shoppingList.get(position);

            if(convertView == null){
                convertView = inflater.inflate(R.layout.activity_shopping_list_item, parent, false);
            }
            TextView name = (TextView) convertView.findViewById(R.id.name);

            name.setText(item.getName());
            if(!item.isPurchased()){
                name.setPaintFlags(name.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }else{
                name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            return convertView;

        }
    }
}
