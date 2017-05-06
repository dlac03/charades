package com.dlac.charades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.dlac.charades.adapters.CategoryAdapter;
import com.dlac.charades.models.Category;
import com.dlac.charades.models.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CategoryActivity extends AppCompatActivity {

    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHandler = new DBHandler(this);
        setContentView(R.layout.activity_category);
        List<Category> categories = dbHandler.getCategories();

        GridView gridview = (GridView) findViewById(R.id.grid_category);
        gridview.setAdapter(new CategoryAdapter(this, categories));
    }
}
