package ru.internetcloud.addressbook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class AddressBookDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;


    public AddressBookDatabaseHelper(@Nullable Context context) {
        super(context, DatabaseDescription.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createCommand = "create table " + DatabaseDescription.ContactTable.TABLE_NAME
                + " (" + DatabaseDescription.ContactTable.Cols._ID + " integer primary key autoincrement,"
                + DatabaseDescription.ContactTable.Cols.NAME + " TEXT,"
                + DatabaseDescription.ContactTable.Cols.PHONE + " TEXT"
                + ")";

        db.execSQL(createCommand);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}