package com.se491.chef_ly.activity.nav_activities;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.se491.chef_ly.Databases.DatabaseHandler;
import com.se491.chef_ly.R;
import com.se491.chef_ly.model.ShoppingListItem;

import java.util.ArrayList;

public class ShoppingListActivity extends ListActivity {
    private static ArrayList<ShoppingListItem> shoppingList = new ArrayList<>();
    private Button deletePurchasedBtn;
    private Button finished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        setListAdapter(new myAdapter(this.getApplicationContext()));

        final DatabaseHandler handler = new DatabaseHandler(this.getApplicationContext());

        finished = (Button) findViewById(R.id.finishedBtn);
        finished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
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

        shoppingList = handler.getShoppingList();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        TextView qty = (TextView) v.findViewById(R.id.qty);
        TextView uom = (TextView) v.findViewById(R.id.uom);
        TextView name = (TextView) v.findViewById(R.id.name);
        ShoppingListItem item = shoppingList.get(position);

        if(item.isPurchased()){
            item.setPurchased(false);
            qty.setPaintFlags(qty.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            uom.setPaintFlags(uom.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            name.setPaintFlags(name.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            item.setPurchased(true);
            qty.setPaintFlags(qty.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            uom.setPaintFlags(uom.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        ((BaseAdapter) getListAdapter()).notifyDataSetChanged();

    }

    static class myAdapter extends BaseAdapter{
        private final LayoutInflater inflater;
        private final Context context;

        myAdapter(Context context){
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.context = context;
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

            TextView qty = (TextView) convertView.findViewById(R.id.qty);
            TextView uom = (TextView) convertView.findViewById(R.id.uom);
            TextView name = (TextView) convertView.findViewById(R.id.name);

            qty.setText(String.valueOf(item.getQty()));
            uom.setText(item.getUnitOfMeasure());
            name.setText(item.getName());
            if(!item.isPurchased()){
                qty.setPaintFlags(qty.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                uom.setPaintFlags(uom.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                name.setPaintFlags(name.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }else{
                qty.setPaintFlags(qty.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                uom.setPaintFlags(uom.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            return convertView;

        }
    }
}
