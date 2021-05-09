package ru.internetcloud.addressbook;

import android.database.Cursor;
import android.database.CursorWrapper;

public class ContactCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public ContactCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Contact getContact() {

        long _id = getLong(getColumnIndex(DatabaseDescription.ContactTable.Cols._ID));

        Contact contact = new Contact(_id);
        contact.setName(getString(getColumnIndex(DatabaseDescription.ContactTable.Cols.NAME)));
        contact.setPhone(getString(getColumnIndex(DatabaseDescription.ContactTable.Cols.PHONE)));
        contact.setEmail(getString(getColumnIndex(DatabaseDescription.ContactTable.Cols.EMAIL)));
        contact.setStreet(getString(getColumnIndex(DatabaseDescription.ContactTable.Cols.STREET)));
        contact.setCity(getString(getColumnIndex(DatabaseDescription.ContactTable.Cols.CITY)));
        contact.setState(getString(getColumnIndex(DatabaseDescription.ContactTable.Cols.STATE)));
        contact.setZip(getString(getColumnIndex(DatabaseDescription.ContactTable.Cols.ZIP)));

        return contact;
    }

}
