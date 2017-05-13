package com.dlac.charades;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dlac.charades.models.Category;
import com.dlac.charades.models.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dlac on 2017. 05. 06..
 */

public class DBHandler {

    public final static String DB_NAME = "CHARADES_DB";
    public final static int DB_VERSION = 1;

    public final static String TABLE_CATEGORIES = "categories";
    public final static String TABLE_QUESTIONS = "questions";

    public final static String COLUMN_TEXT = "text";
    public final static String COLUMN_CATEGORY = "category";
    public final static String COLUMN_NAME = "name";
    public final static String COLUMN_DESCRIPTION = "description";
    public final static String COLUMN_ID = "_id";

    protected DBHelper dbHelper;

    public DBHandler(Context context) {
        dbHelper = new DBHelper(context);
    }

    public List<Category> getCategories()
    {
        ArrayList<Category> categories = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_CATEGORIES,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            Category c = new Category(id,name,description);
            categories.add(c);
            cursor.moveToNext();
        }
        db.close();
        return  categories;
    }

    public List<Question> getQuestions(Category c){
        ArrayList<Question> questions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = COLUMN_CATEGORY + " = ?";
        String[] selectionArgs = { Integer.toString(c.getId()) };
        Cursor cursor = db.query(TABLE_QUESTIONS,null,selection,selectionArgs,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            String text = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT));
            Question q = new Question(id,text);
            questions.add(q);
            cursor.moveToNext();
        }
        db.close();
        return questions;
    }

    public void addQuestion(String text, Category c) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEXT, text);
        values.put(COLUMN_CATEGORY, c.getId());
        db.insert(TABLE_QUESTIONS, null, values);
        db.close();
    }

    public Category addCategory(String name, String description){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        long id = db.insert(TABLE_QUESTIONS, null, values);
        db.close();
        if (id != -1)
            return new Category((int)id,name);
        return null;
    }


    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            //super(context, DB_NAME, null, DB_VERSION);
            ///TODO: Move to persistent db
            super(context, null, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "+ TABLE_CATEGORIES +"(" +
                    "_id    INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name   VARCHAR(250)," +
                    "description   VARCHAR(500)" +
                    ")");

            db.execSQL("CREATE TABLE "+ TABLE_QUESTIONS +"(" +
                    "_id    INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "text   VARCHAR(250)," +
                    "category   INTEGER," +
                    "FOREIGN KEY(category) REFERENCES " + TABLE_CATEGORIES + "(_id)" +
                    ")");

            createInitialDataSet(db);
        }

        private void createInitialDataSet(SQLiteDatabase db)
        {
            for (int i = 0; i < 15; i++)
            {
                ContentValues c_values = new ContentValues();
                c_values.put(COLUMN_NAME, i + ". Kategória");
                c_values.put(COLUMN_DESCRIPTION, "Leírás");
                db.insert(TABLE_CATEGORIES,null,c_values);
                for (int j = 0; j < 10; j++)
                {
                    ContentValues q_values = new ContentValues();
                    q_values.put(COLUMN_CATEGORY, i);
                    q_values.put(COLUMN_TEXT, j + ". Kérdés");
                    db.insert(TABLE_QUESTIONS,null,q_values);
                }
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
            onCreate(db);
        }
    }
}

