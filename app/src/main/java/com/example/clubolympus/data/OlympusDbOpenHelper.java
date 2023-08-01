package com.example.clubolympus.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class OlympusDbOpenHelper extends SQLiteOpenHelper {
    public OlympusDbOpenHelper(@Nullable Context context) {
        super(context, ClubOlympusContract.DATABASE_NAME, null, ClubOlympusContract.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_MEMBERS_TABLE = "CREATE TABLE " + ClubOlympusContract.MemberEntry.TABLE_NAME + "("
                + ClubOlympusContract.MemberEntry._ID + " INTEGER PRIMARY KEY,"
                + ClubOlympusContract.MemberEntry.KEY_FIRST_NAME + " TEXT,"
                + ClubOlympusContract.MemberEntry.KEY_LAST_NAME + " TEXT,"
                + ClubOlympusContract.MemberEntry.KEY_GENDER + " INTEGER NOT NULL,"
                + ClubOlympusContract.MemberEntry.KEY_SPORT + " TEXT" + ")";

        sqLiteDatabase.execSQL(CREATE_MEMBERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ClubOlympusContract.DATABASE_NAME);
        onCreate(sqLiteDatabase);
    }
}
