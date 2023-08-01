package com.example.clubolympus.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.clubolympus.data.ClubOlympusContract.*;

public class OlympusContentProvider extends ContentProvider {

    public static final int MEMBERS = 111;
    public static final int MEMBER_ID = 222;

    OlympusDbOpenHelper dbOpenHelper;

    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        uriMatcher.addURI(ClubOlympusContract.AUTHORITY,ClubOlympusContract.PATH_MEMBERS,MEMBERS);
        uriMatcher.addURI(ClubOlympusContract.AUTHORITY,ClubOlympusContract.PATH_MEMBERS + "/#",MEMBER_ID);
    }

    @Override
    public boolean onCreate() {
        dbOpenHelper = new OlympusDbOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings,
                        @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor;

        int match = uriMatcher.match(uri);

        switch (match){
            case MEMBERS:
                cursor = db.query(MemberEntry.TABLE_NAME, strings, s, strings1,
                        null, null, s1);
                break;
            case MEMBER_ID:
                s = MemberEntry._ID + "=?";
                strings1 = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(MemberEntry.TABLE_NAME, strings, s, strings1,
                        null, null, s1);
                break;
            default:
                throw new IllegalArgumentException("Can't query incorrect URI" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        String firstName = contentValues.getAsString(MemberEntry.KEY_FIRST_NAME);
        if(firstName==null){
            throw new IllegalArgumentException("You have to input first name");
        }

        String lastName = contentValues.getAsString(MemberEntry.KEY_FIRST_NAME);
        if(lastName==null){
            throw new IllegalArgumentException("You have to input last name");
        }

        Integer gender = contentValues.getAsInteger(MemberEntry.KEY_GENDER);
        if(gender==null || !(gender==MemberEntry.GENDER_UNKNOWN ||
                gender==MemberEntry.GENDER_MALE || gender==MemberEntry.GENDER_FEMALE)){
            throw new IllegalArgumentException("You have to input correct gender");
        }

        String sport = contentValues.getAsString(MemberEntry.KEY_FIRST_NAME);
        if(sport==null){
            throw new IllegalArgumentException("You have to input sport");
        }

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);

        switch (match){
            case MEMBERS:
                long id = db.insert(MemberEntry.TABLE_NAME, null, contentValues);
                if(id == -1){
                    Log.e("insert method:", "Insertion of data in the table failed for " + uri);
                    return null;
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return ContentUris.withAppendedId(uri,id);
            default:
                throw new IllegalArgumentException("Can't query incorrect URI" + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int rowsDeleted;

        switch (match){
            case MEMBERS:
                rowsDeleted = db.delete(MemberEntry.TABLE_NAME, s, strings);
                break;
            case MEMBER_ID:
                s = MemberEntry._ID + "=?";
                strings = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted =  db.delete(MemberEntry.TABLE_NAME, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Can't delete this URI" + uri);
        }

        if(rowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String s, @Nullable String[] strings) {

        if(contentValues.containsKey(MemberEntry.KEY_FIRST_NAME)) {
            String firstName = contentValues.getAsString(MemberEntry.KEY_FIRST_NAME);
            if (firstName == null) {
                throw new IllegalArgumentException("You have to input first name");
            }
        }

        if(contentValues.containsKey(MemberEntry.KEY_LAST_NAME)) {
            String lastName = contentValues.getAsString(MemberEntry.KEY_FIRST_NAME);
            if (lastName == null) {
                throw new IllegalArgumentException("You have to input last name");
            }
        }

        if(contentValues.containsKey(MemberEntry.KEY_GENDER)) {
            Integer gender = contentValues.getAsInteger(MemberEntry.KEY_GENDER);
            if (gender == null || !(gender == MemberEntry.GENDER_UNKNOWN ||
                    gender == MemberEntry.GENDER_MALE || gender == MemberEntry.GENDER_FEMALE)) {
                throw new IllegalArgumentException("You have to input correct gender");
            }
        }

        if(contentValues.containsKey(MemberEntry.KEY_GENDER)) {
            String sport = contentValues.getAsString(MemberEntry.KEY_FIRST_NAME);
            if (sport == null) {
                throw new IllegalArgumentException("You have to input sport");
            }
        }

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int rowsUpdated;

        switch (match){
            case MEMBERS:
                rowsUpdated = db.update(MemberEntry.TABLE_NAME, contentValues, s, strings);
                break;
            case MEMBER_ID:
                s = MemberEntry._ID + "=?";
                strings = new String[] {String.valueOf(ContentUris.parseId(uri))};

                rowsUpdated = db.update(MemberEntry.TABLE_NAME, contentValues, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Can't update this URI" + uri);
        }
        if(rowsUpdated!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowsUpdated;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match){
            case MEMBERS:
                return MemberEntry.CONTENT_MULTIPLE_ITEMS;
            case MEMBER_ID:
                return MemberEntry.CONTENT_SINGLE_ITEMS;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}
