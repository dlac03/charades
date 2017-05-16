package com.dlac.charades.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
;
import com.dlac.charades.QuestionActivity;
import com.dlac.charades.R;
import com.dlac.charades.models.Category;

import java.util.List;

/**
 * Created by dlac on 2017. 04. 30..
 */

public class CategoryAdapter  extends BaseAdapter{

    private Context context;
    List<Category> categories;

    public CategoryAdapter(Context c, List<Category> categories) {
        context = c;
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
            btn = new Button(context);
            btn.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT));
        } else {
            btn = (Button) convertView;
        }
        btn.setText(categories.get(position).getName());
        btn.setTextColor(Color.BLACK);
        btn.setBackgroundColor(Color.WHITE);
        btn.setId(position);
        btn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.help_circle, 0, 0);

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                openCategory(categories.get(position));
            }
        });
        return btn;
    }

    private void openCategory(final Category category)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.Theme_AppCompat_Light_Dialog);
        builder.setMessage(category.getName())
                .setTitle("Választott kategória")
                .setMessage(category.getDescription());

        builder.setPositiveButton(R.string.button_continue, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(context, QuestionActivity.class);
                intent.putExtra("category", category);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
