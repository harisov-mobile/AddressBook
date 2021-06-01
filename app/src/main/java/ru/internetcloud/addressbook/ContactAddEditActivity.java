package ru.internetcloud.addressbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import ru.internetcloud.addressbook.model.Contact;
import ru.internetcloud.addressbook.model.ContactLab;

public class ContactAddEditActivity extends TemplateFragmentActivity
    implements ContactAddEditFragment.Callbacks {

    private static final String KEY_CONTACT_ID = "ru.internetcloud.addressbook.contact_id";

    private String query;

    public static Intent newIntent(Context context, long contactId) {
        Intent intent = new Intent(context, ContactAddEditActivity.class);
        intent.putExtra(KEY_CONTACT_ID, contactId);
        return intent;
    }

    public static long getContactId(Intent data) {
        return data.getLongExtra(KEY_CONTACT_ID, -1);
    }

    @Override
    protected Fragment createFragment() {
        long contactId = getIntent().getLongExtra(KEY_CONTACT_ID, 0);
        return ContactAddEditFragment.newInstance(contactId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onNewContactSaved(Contact contact) {
        // кнопка SAVE
        long contactId = ContactLab.getInstance(this).addContact(contact);
        if (contactId == -1) {
            Toast.makeText(this, R.string.contact_not_added, Toast.LENGTH_SHORT).show();
        } else {
            // контакт успешно добавлен:
            Toast.makeText(this, R.string.contact_added, Toast.LENGTH_SHORT).show();
            Intent intent = ContactPagerActivity.newIntent(this, contactId, query);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onUpdateContact(Contact contact) {
        // кнопка SAVE
        int result = ContactLab.getInstance(this).updateContact(contact);
        if (result == -1) {
            Toast.makeText(this, R.string.contact_not_updated, Toast.LENGTH_SHORT).show();
        } else {
            // контакт успешно обновлен:
            Toast.makeText(this, R.string.contact_updated, Toast.LENGTH_SHORT).show();

            Intent data = new Intent();
            data.putExtra(KEY_CONTACT_ID, contact.getId()); // не понадобился, достаточно RESULT_OK и REQUEST_ADD_EDIT
            setResult(RESULT_OK, data);

            //Intent intent = ContactPagerActivity.newIntent(this, contact.getId(), query);
            //startActivity(intent);
            finish();
        }
    }


}
