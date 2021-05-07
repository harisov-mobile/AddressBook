package ru.internetcloud.addressbook;

public final class DatabaseDescription {

    public static final String DATABASE_NAME = "address_book.db";

    public static final class ContactTable {
        public static final String TABLE_NAME = "contacts";

        public static final class Cols {
            public static final String _ID = "_id";
            public static final String NAME = "name";
            public static final String PHONE = "phone";
        }
    }

}
