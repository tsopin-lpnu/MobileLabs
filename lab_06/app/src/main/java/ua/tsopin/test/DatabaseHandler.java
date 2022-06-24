package ua.tsopin.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ua.tsopin.test.models.Note;
import ua.tsopin.test.utils.Utils;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notes_db";
    private static final String TABLE_CATEGORIES = "Categories";
    private static final String TABLE_NOTES = "Notes";

    public static final String KEY_CATEGORY_ID = "_id";
    public static final String KEY_CATEGORY_NAME = "name";
    public static final String KEY_CATEGORY_COLOR = "color";

    public static final String KEY_NOTE_ID = "_id";
    public static final String KEY_NOTE_TITLE = "title";
    public static final String KEY_NOTE_NOTE_TEXT = "note_text";
    public static final String KEY_NOTE_IS_DELETED = "is_deleted";
    public static final String KEY_NOTE_CATEGORY_ID = "category_id";
    public static final String KEY_NOTE_CREATED_AT = "created_at";
    public static final String KEY_NOTE_UPDATED_AT = "updated_at";

    private static final String SELECT_ALL_NOTES = "SELECT " +
            "n." + KEY_NOTE_ID + ", " +
            "n." + KEY_NOTE_TITLE + ", " +
            "n." + KEY_NOTE_NOTE_TEXT + ", " +
            "n." + KEY_NOTE_IS_DELETED + ", " +
            "n." + KEY_NOTE_CATEGORY_ID + ", " +
            "c." + KEY_CATEGORY_COLOR + ", " +
            "n." + KEY_NOTE_CREATED_AT + ", " +
            "n." + KEY_NOTE_UPDATED_AT + " " +
            "FROM " + TABLE_NOTES + " n " +
            "JOIN " + TABLE_CATEGORIES + " c on c." + KEY_CATEGORY_ID + " = n." + KEY_NOTE_CATEGORY_ID + " " +
            "WHERE n." + KEY_NOTE_IS_DELETED + " = 0 " +
            "ORDER BY n." + KEY_NOTE_UPDATED_AT + " DESC;";

    private static final String SELECT_NOTES_IN_CATEGORY = "SELECT " +
            "n." + KEY_NOTE_ID + ", " +
            "n." + KEY_NOTE_TITLE + ", " +
            "n." + KEY_NOTE_NOTE_TEXT + ", " +
            "n." + KEY_NOTE_IS_DELETED + ", " +
            "n." + KEY_NOTE_CATEGORY_ID + ", " +
            "c." + KEY_CATEGORY_COLOR + ", " +
            "n." + KEY_NOTE_CREATED_AT + ", " +
            "n." + KEY_NOTE_UPDATED_AT + " " +
            "FROM " + TABLE_NOTES + " n " +
            "JOIN " + TABLE_CATEGORIES + " c on c." + KEY_CATEGORY_ID + " = n." + KEY_NOTE_CATEGORY_ID + " " +
            "WHERE n." + KEY_NOTE_IS_DELETED + " = 0 AND n." + KEY_NOTE_CATEGORY_ID + " = ? "+
            "ORDER BY n." + KEY_NOTE_UPDATED_AT + " DESC;";

    private static final String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + " ( " +
            KEY_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_CATEGORY_NAME + " TEXT, " +
            KEY_CATEGORY_COLOR + " TEXT DEFAULT ('#000000'));";

    private static final String INSERT_DEFAULT_VALUES_INTO_CATEGORIES = "INSERT INTO " + TABLE_CATEGORIES +
                                                                        " (" + KEY_CATEGORY_ID + ", " + KEY_CATEGORY_NAME + ", " + KEY_CATEGORY_COLOR + ") " +
                                                                        "VALUES  (0, 'Home', '#3498DC'), " +
                                                                                "(1, 'Work', '#FAE033'), " +
                                                                                "(2, 'Hobby', '#50C878'), " +
                                                                                "(3, 'Education', '#D32730'), " +
                                                                                "(4, 'Food', '#ED9121'), " +
                                                                                "(5, 'Fun', '#C9A0DC');";

    private static final String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + " ( " +
            KEY_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_NOTE_TITLE + " TEXT, " +
            KEY_NOTE_NOTE_TEXT + " TEXT NOT NULL, " +
            KEY_NOTE_IS_DELETED + " INTEGER (1) DEFAULT (0), " +
            KEY_NOTE_CATEGORY_ID + " INTEGER REFERENCES " + TABLE_CATEGORIES + " (" + KEY_CATEGORY_ID + ") ON DELETE CASCADE ON UPDATE CASCADE, " +
            KEY_NOTE_CREATED_AT + " INTEGER, " +
            KEY_NOTE_UPDATED_AT + " INTEGER  );";

    private final Context mContext;

    private static DatabaseHandler instance = null;
    private SQLiteDatabase db;

    public static synchronized DatabaseHandler getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHandler(context);
        }
        return instance;
    }

    private DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");
        db.execSQL(CREATE_CATEGORIES_TABLE);

        db.execSQL(INSERT_DEFAULT_VALUES_INTO_CATEGORIES);

        db.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }

    public Note addNote(@NonNull Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long idx = 0;


        values.put(KEY_NOTE_TITLE, note.getTitle());
        values.put(KEY_NOTE_NOTE_TEXT, note.getDesc());
        values.put(KEY_NOTE_IS_DELETED, 0);
        values.put(KEY_NOTE_CATEGORY_ID, note.getCategory());
        values.put(KEY_NOTE_CREATED_AT, note.getCreated_at().getTime());
        values.put(KEY_NOTE_UPDATED_AT, note.getUpdated_at().getTime());

        idx = db.insert(TABLE_NOTES, null, values);

        db.close();

        if (idx != -1) {
            note.setId(idx);
        }

        return note;
    }

    public Note updateNote(@NonNull Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int is_del = note.getIs_deleted() ? 1 : 0;
        note.setUpdated_at(new Date());

        values.put(KEY_NOTE_TITLE, note.getTitle());
        values.put(KEY_NOTE_NOTE_TEXT, note.getDesc());
        values.put(KEY_NOTE_IS_DELETED, is_del);
        values.put(KEY_NOTE_CATEGORY_ID, note.getCategory());
        values.put(KEY_NOTE_UPDATED_AT, note.getUpdated_at().getTime());

        db.update(TABLE_NOTES, values, KEY_NOTE_ID + " = ?", new String[]{String.valueOf(note.getId())});

        db.close();

        return note;
    }

    public void deleteNote(@NonNull Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NOTE_IS_DELETED, 1);
        values.put(KEY_NOTE_UPDATED_AT, Utils.getNow());

        db.update(TABLE_NOTES, values, KEY_NOTE_ID + " = ?", new String[]{String.valueOf(note.getId())});
        db.close();
    }

    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> notesList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL_NOTES, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = Utils.loadNoteFromCursor(cursor);
                notesList.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return notesList;
    }

    public ArrayList<Note> getNotesInCategory(int category) {
        ArrayList<Note> notesList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_NOTES_IN_CATEGORY, new String[]{String.valueOf(category)});

        if (cursor.moveToFirst()) {
            do {
                Note note = Utils.loadNoteFromCursor(cursor);
                notesList.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return notesList;
    }

}
