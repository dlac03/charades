package com.dlac.charades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent resultIntent = getIntent();
        ArrayList<String> roundList = resultIntent.getStringArrayListExtra("points");
        if (roundList != null)
            createTable(roundList);
    }

    private void createTable(ArrayList<String> rl)
    {
        lv = (ListView) findViewById(R.id.listView_results);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                rl );

        lv.setAdapter(arrayAdapter);
    }
}
