package com.example.comfortable_apartment_100;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class deliveryFragment extends ListFragment {

    ArrayList<HashMap<String, String>> boxStateList;
    SimpleAdapter adapter;
    private static final String TAG_NAME = "name";
    private static final String TAG_reportDay ="reportDay";

    public deliveryFragment(ArrayList<HashMap<String, String>> AL){
        boxStateList  = AL;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new SimpleAdapter(getActivity(),
                boxStateList,android.R.layout.simple_list_item_2,
                new String[]{TAG_NAME,TAG_reportDay},
                new int[]{android.R.id.text1, android.R.id.text2});
        setListAdapter(adapter);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }
}
