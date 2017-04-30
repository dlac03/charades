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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        List<Category> mockData = createMockData();

        GridView gridview = (GridView) findViewById(R.id.grid_category);
        gridview.setAdapter(new CategoryAdapter(this,mockData));
    }

    private List<Category> createMockData() {
        List<Category> categories = new ArrayList<>();

        for (int i = 0; i < 10; i ++)
        {
            Category category = new Category(i, (i + 1) + ". Kategória");
            categories.add(category);
        }

        Random random = new Random();
        List<Question> questions = new ArrayList<>();

        for (int i = 0; i < 150; i++)
        {
            Category category = categories.get(random.nextInt(10));
            Question question = new Question(category, "Valami kérdés" + i);
            questions.add(question);
        }

        return categories;
    }
}
