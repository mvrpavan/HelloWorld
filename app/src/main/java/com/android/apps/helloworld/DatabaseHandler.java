package com.android.apps.helloworld;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;

/**
 * Created by pavan on 10/28/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userActions",
    TABLE_HISTORY = "History",
    KEY_ID = "id",
    KEY_TIME = "ActionDateTime",
    KEY_ACTION = "Action";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_HISTORY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TIME + " TEXT," + KEY_ACTION + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }

    public void addUserAction(UserAction userAction) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TIME, userAction.getActionDate().toString());
        values.put(KEY_ACTION, userAction.getActionDesc());

        db.insert(TABLE_HISTORY, null, values);
        db.close();
    }

    public UserAction getUserAction(int id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_HISTORY, new String[] { KEY_ID, KEY_TIME, KEY_ACTION }, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null );

        if (cursor != null)
            cursor.moveToFirst();

        UserAction userAction = new UserAction(Integer.parseInt(cursor.getString(0)), Date.valueOf(cursor.getString(1)), cursor.getString(2));
        db.close();
        cursor.close();
        return userAction;
    }

    public void deleteUserAction(UserAction userAction) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_HISTORY, KEY_ID + "=?", new String[]{String.valueOf(userAction.getId())});
        db.close();
    }

    public int getActionCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HISTORY, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();

        return count;
    }
}
