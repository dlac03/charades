package com.dlac.charades.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.dlac.charades.R;
import com.dlac.charades.models.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dlac on 2017. 04. 30..
 */

public class CategoryAdapter  extends BaseAdapter{

    private Context mContext;
    List<Category> categories;

    public CategoryAdapter(Context c, List<Category> categories) {
        mContext = c;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        if (categories != null)
            return categories.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (categories != null){
            if (categories.size() - 1 >= position)
            return categories.get(position);
        }
            return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Button btn;
        if (convertView == null) {
            btn = new Button(mContext);
            btn.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT));
        } else {
            btn = (Button) convertView;
        }
        btn.setText(categories.get(position).getName());
        btn.setTextColor(Color.RED);
        btn.setBackgroundColor(Color.GREEN);
        btn.setId(position);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                openCategory(categories.get(position));
            }
        });
        return btn;
    }

    private void openCategory(Category category)
    {

    }
}
