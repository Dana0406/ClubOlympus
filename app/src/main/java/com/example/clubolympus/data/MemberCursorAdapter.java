package com.example.clubolympus.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;
import com.example.clubolympus.data.ClubOlympusContract.MemberEntry;
import com.example.clubolympus.R;

public class MemberCursorAdapter extends CursorAdapter {


    public MemberCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.member_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView firstNameTextView = view.findViewById(R.id.firstNameTextView);
        TextView sportTextView = view.findViewById(R.id.sportTextView);

        String firstName = cursor.getString(cursor.getColumnIndexOrThrow(MemberEntry.KEY_FIRST_NAME));
        String lastName = cursor.getString(cursor.getColumnIndexOrThrow(MemberEntry.KEY_LAST_NAME));
        String sport = cursor.getString(cursor.getColumnIndexOrThrow(MemberEntry.KEY_SPORT));

        firstNameTextView.setText(firstName+" "+lastName);
        sportTextView.setText(sport);
    }
}
