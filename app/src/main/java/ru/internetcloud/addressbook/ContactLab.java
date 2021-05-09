package ru.internetcloud.addressbook;

//**************************************
// синглтон
//**************************************

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ContactLab {

    private static ContactLab contactLab;
    private SQLiteDatabase sqLiteDatabase;
    private Context appContext;

    // статический метод для синглтона
    public static ContactLab getInstance(Context activityContext) {
        if (contactLab == null) {
            contactLab = new ContactLab(activityContext);
        }
        return contactLab;
    }

    // статический метод - вспомогательный для БД
    private static ContentValues getContentValues(Contact contact) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseDescription.ContactTable.Cols.NAME, contact.getName());
        contentValues.put(DatabaseDescription.ContactTable.Cols.PHONE, contact.getPhone());
        contentValues.put(DatabaseDescription.ContactTable.Cols.EMAIL, contact.getEmail());
        contentValues.put(DatabaseDescription.ContactTable.Cols.STREET, contact.getStreet());
        contentValues.put(DatabaseDescription.ContactTable.Cols.CITY, contact.getCity());
        contentValues.put(DatabaseDescription.ContactTable.Cols.STATE, contact.getState());
        contentValues.put(DatabaseDescription.ContactTable.Cols.ZIP, contact.getZip());
        contentValues.put(DatabaseDescription.ContactTable.Cols.UUID, contact.getUuid().toString());

        return contentValues;
    }

    private ContactLab(Context activityContext) {
        // закрытый конструктор, чтобы никто не смог создать экземпляр в обход getInstance()
        appContext = activityContext.getApplicationContext();

        // создать БД:
        sqLiteDatabase = new AddressBookDatabaseHelper(appContext).getWritableDatabase();
    }

    public List<Contact> getContactList() {
        List<Contact> contactList = new ArrayList<>();

        ContactCursorWrapper contactCursorWrapper = queryContacts(null, null); // получаем все записи

        try {
            contactCursorWrapper.moveToFirst();
            while (!contactCursorWrapper.isAfterLast()) {
                contactList.add(contactCursorWrapper.getContact());
                contactCursorWrapper.moveToNext();
            }
        } finally {
            contactCursorWrapper.close();
        }

        return contactList;
    }

    public long addContact(Contact contact) {
        // @return the row ID of the newly inserted row, or -1 if an error occurred
        ContentValues contentValues = getContentValues(contact);
        return sqLiteDatabase.insert(DatabaseDescription.ContactTable.TABLE_NAME, null, contentValues);
    }

    public int updateContact(Contact contact) {
        // @return the number of rows affected
        ContentValues contentValues = getContentValues(contact);
        String idString = "" + contact.getId();
        return sqLiteDatabase.update(DatabaseDescription.ContactTable.TABLE_NAME, contentValues, DatabaseDescription.ContactTable.Cols._ID + " = ?", new String[] {idString});
    }

    public int deleteContact(long contactId) {
        String idString = "" + contactId;
        return sqLiteDatabase.delete(DatabaseDescription.ContactTable.TABLE_NAME, DatabaseDescription.ContactTable.Cols._ID + " = ?", new String[] {idString});
    }

    public Contact getContact(long contactId) {

        Contact contact = null;

        String idString = "" + contactId;
        ContactCursorWrapper contactCursorWrapper = queryContacts(DatabaseDescription.ContactTable.Cols._ID + " = ?", new String[] {idString}); // получаем одну запись
        try {
            if (contactCursorWrapper.getCount() != 0) {
                contactCursorWrapper.moveToFirst();
                contact = contactCursorWrapper.getContact();
            }
        } finally {
            contactCursorWrapper.close();
        }
        return contact;
    }

    private ContactCursorWrapper queryContacts(String whereClause, String[] whereArgs) {
        Cursor cursor = sqLiteDatabase.query(
                DatabaseDescription.ContactTable.TABLE_NAME,
                null, // все столбцы
                whereClause,
                whereArgs,
                null, // group by
                null, // having
                DatabaseDescription.ContactTable.Cols.NAME // order by
                );

        // оборачиваем курсор в обертку
        return new ContactCursorWrapper(cursor);
    }

    public File getPhotoFile(Contact contact) {
        File filesDir = appContext.getFilesDir();
        return new File(filesDir, contact.getPhotoFilename());
    }
}
