package ru.internetcloud.addressbook;

//**************************************
// синглтон
//**************************************

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ContactLab {

    private static ContactLab contactLab;

    public static ContactLab getInstance(Context activityContext) {
        if (contactLab == null) {
            contactLab = new ContactLab(activityContext);
        }
        return contactLab;
    }

    private ContactLab(Context activityContext) {
        // закрытый конструктор, чтобы никто не смог создать экземпляр в обход getInstance()
        Context appContext = activityContext.getApplicationContext();


        // создать БД:

    }

    public List<Contact> getContactList() {
        List<Contact> contactList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Contact contact = new Contact();
            contact.setName("#" + i);
            contactList.add(contact);
        }

        return contactList;
    }
}
