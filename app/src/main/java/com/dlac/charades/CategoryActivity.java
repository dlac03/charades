package com.dlac.charades;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

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

        setToolbar();
    }


    private void setToolbar()
    {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.category_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        Toolbar tb = (Toolbar) findViewById(R.id.category_toolbar);
        tb.setTitle("Charades");
        tb.inflateMenu(R.menu.category);

        tb.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return onOptionsItemSelected(item);
                    }
                });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                Toast.makeText(getApplicationContext(),"Charades 1.0 - Detvay László - 2017",Toast.LENGTH_SHORT);
                break;
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
        return true;
    }
}
