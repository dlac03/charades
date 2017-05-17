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
import java.util.StringTokenizer;

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
        dbHelper.insertQuestion(db,c.getId(),text);
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

            //createMockDataSet(db);
            createInitialDataSet(db);
        }

        private void createMockDataSet(SQLiteDatabase db)
        {
            for (int i = 0; i < 15; i++)
            {
                insertCategory(db, i + ". Kategória", "Leírás");

                for (int j = 0; j < 10; j++)
                {
                    insertQuestion(db, i, j + ". Kérdés" );
                }
            }
        }

        private void createInitialDataSet (SQLiteDatabase db)
        {
            long actorCategoryId = insertCategory(db,"Színészek", "Filmszínészek a nagyvilágból");
            insertQuestion(db,actorCategoryId,"Hugh Jackman");
            insertQuestion(db,actorCategoryId,"Patrick Stewart");
            insertQuestion(db,actorCategoryId,"Matt LeBlanc");
            insertQuestion(db,actorCategoryId,"William Dafoe");
            insertQuestion(db,actorCategoryId,"Michael Fassbender");
            insertQuestion(db,actorCategoryId,"Jennifer Lawrence");
            insertQuestion(db,actorCategoryId,"Megan Fox");
            insertQuestion(db,actorCategoryId,"Zoe Saldana");
            insertQuestion(db,actorCategoryId,"Michelle Rodriguez");

            long rockstarCategoryId = insertCategory(db,"Rocksztárok", "Rocksztárok a rock minden műfajából");
            insertQuestion(db,rockstarCategoryId,"Eddie Veder");
            insertQuestion(db,rockstarCategoryId,"Dave Grohl");
            insertQuestion(db,rockstarCategoryId,"Anthony Kiedis");
            insertQuestion(db,rockstarCategoryId,"Lemmy Kilmister");
            insertQuestion(db,rockstarCategoryId,"Jimmy Page");
            insertQuestion(db,rockstarCategoryId,"James Hetfield");
            insertQuestion(db,rockstarCategoryId,"Gary Clark Jr.");
            insertQuestion(db,rockstarCategoryId,"Jack White");
            insertQuestion(db,rockstarCategoryId,"Kurt Cobain");

            long animalID = insertCategory(db,"Állatok", "Derítsd ki, milyen állat vagy!");
            insertQuestion(db,animalID,"Gólya");
            insertQuestion(db,animalID,"Zsiráf");
            insertQuestion(db,animalID,"Lamantin");
            insertQuestion(db,animalID,"Delfin");
            insertQuestion(db,animalID,"Kukac");
            insertQuestion(db,animalID,"Ebihal");
            insertQuestion(db,animalID,"Kockásfülű nyúl");
            insertQuestion(db,animalID,"Mókus");
            insertQuestion(db,animalID,"Denevér");

            long drugId = insertCategory(db,"Drogok", "Mesélj csak, milyen drog is vagy?");
            insertQuestion(db,drugId,"MDMA");
            insertQuestion(db,drugId,"Varázsgomba");
            insertQuestion(db,drugId,"Fű");
            insertQuestion(db,drugId,"Kokain");
            insertQuestion(db,drugId,"Heroin");
            insertQuestion(db,drugId,"Alkohol");
            insertQuestion(db,drugId,"Szipuá");
            insertQuestion(db,drugId,"Meszkalin");

            long movieId = insertCategory(db,"Filmek", "Magyar és külföldi filmek");
            insertQuestion(db,movieId,"Vissza a jövőbe");
            insertQuestion(db,movieId,"Valami amerika");
            insertQuestion(db,movieId,"Nagy utazás");
            insertQuestion(db,movieId,"Gyűrűk Ura");
            insertQuestion(db,movieId,"Mátrix");
            insertQuestion(db,movieId,"Ponyvaregény");
            insertQuestion(db,movieId,"Shop Stop");
            insertQuestion(db,movieId,"Donnie Darko");
            insertQuestion(db,movieId,"Részeges karate mester");
            insertQuestion(db,movieId,"La La Land");
        }

        private long insertCategory(SQLiteDatabase db, String name, String description)
        {
            ContentValues c_values = new ContentValues();
            c_values.put(COLUMN_NAME, name);
            c_values.put(COLUMN_DESCRIPTION, description);
            long id = db.insert(TABLE_CATEGORIES,null,c_values);
            return  id;
        }

        private void insertQuestion(SQLiteDatabase db, long categoryId, String text)
        {
            ContentValues q_values = new ContentValues();
            q_values.put(COLUMN_CATEGORY, categoryId);
            q_values.put(COLUMN_TEXT, text);
            db.insert(TABLE_QUESTIONS,null,q_values);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
            onCreate(db);
        }
    }
}

