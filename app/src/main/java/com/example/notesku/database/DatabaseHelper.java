package com.example.notesku.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nama Database
    private static final String DATABASE_NAME = "notes.db";

    // Versi Database
    private static final int DATABASE_VERSION = 1;

    // Nama Tabel
    public static final String TABLE_NOTE = "notes";

    // Nama Kolom
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_AUDIO = "audio_path";
    public static final String COLUMN_DATE = "created_at";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + TABLE_NOTE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_CONTENT + " TEXT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_AUDIO + " TEXT, " +
                COLUMN_DATE + " TEXT" +
                ");";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);

        onCreate(db);

    }
    // CREATE (Tambah Catatan)
    public long insertNote(Note note){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_CONTENT, note.getContent());
        values.put(COLUMN_CATEGORY, note.getCategory());
        values.put(COLUMN_AUDIO, note.getAudioPath());
        values.put(COLUMN_DATE, note.getCreatedAt());

        long result = db.insert(TABLE_NOTE, null, values);

        db.close();

        return result;
    }
    // READ (Menampilkan Semua Catatan)
    public List<Note> getAllNotes() {

        List<Note> noteList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NOTE + " ORDER BY " + COLUMN_ID + " DESC",
                null);

        if (cursor.moveToFirst()) {
            do {

                Note note = new Note();

                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                note.setContent(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)));
                note.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
                note.setAudioPath(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUDIO)));
                note.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));

                noteList.add(note);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return noteList;
    }
    // UPDATE (Mengubah Catatan)
    public int updateNote(Note note) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_CONTENT, note.getContent());
        values.put(COLUMN_CATEGORY, note.getCategory());
        values.put(COLUMN_AUDIO, note.getAudioPath());
        values.put(COLUMN_DATE, note.getCreatedAt());

        int result = db.update(
                TABLE_NOTE,
                values,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(note.getId())}
        );

        db.close();

        return result;
    }
    // DELETE
    public void deleteNote(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(
                TABLE_NOTE,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        db.close();
    }
}