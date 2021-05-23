package ru.internetcloud.addressbook.database;

public final class DatabaseDescription {

    public static final String DATABASE_NAME = "address_book.db";

    public static final class ContactTable {
        public static final String TABLE_NAME = "contacts";

        public static final class Cols {
            public static final String _ID = "_id";
            public static final String NAME = "name";
            public static final String PHONE = "phone";
            public static final String EMAIL = "email";
            public static final String STREET = "street";
            public static final String CITY = "city";
            public static final String STATE = "state";
            public static final String ZIP = "zip";
            public static final String UUID = "uuid";
        }
    }

}
