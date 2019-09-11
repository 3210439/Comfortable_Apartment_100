package com.example.comfortable_apartment_100;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class Howto extends AppCompatActivity {

    ArrayList<String> dataArr = new ArrayList<String>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howto);
        dataArr.add("종이류 버리는 방법");
        dataArr.add("플라스틱 분류하는 방법");
        dataArr.add("음식물 쓰레기 버리는 방법");


        listView =(ListView)findViewById(R.id.List_view);
        ArrayAdapter<String> Adapter = new ArrayAdapter<String>(this,R.layout.listview_item, dataArr);
        listView.setAdapter(Adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1)
                {
//                    Intent intent = new Intent(getApplicationContext(), RequireActivity.class);
//                    startActivity(intent);
                }
            }
        });
    }
}