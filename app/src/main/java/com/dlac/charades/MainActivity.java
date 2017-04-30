package com.dlac.charades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dlac.charades.models.Category;
import com.dlac.charades.models.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void StartApplication(View v)
    {
        Intent intent = new Intent(this, CategoryActivity.class);
        startActivity(intent);
    }
}
