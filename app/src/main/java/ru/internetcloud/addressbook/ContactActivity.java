package ru.internetcloud.addressbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class ContactActivity extends TemplateFragmentActivity
    implements ContactFragment.Callbacks {

    private static final String KEY_CONTACT_ID = "ru.internetcloud.addressbook.contact_id";

    public static Intent newIntent(Context context, long contactId) {
        Intent intent = new Intent(context, ContactActivity.class);
        intent.putExtra(KEY_CONTACT_ID, contactId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        long contactId = getIntent().getLongExtra(KEY_CONTACT_ID, 0);
        return ContactFragment.newInstance(contactId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onEditContact(long contactId) {
        // открыть на редактирование существующий контакт, но закрыть саму себя:
        Intent intent = ContactAddEditActivity.newIntent(this, contactId);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDeleteContact(long contactId) {
        int result = ContactLab.getInstance(this).deleteContact(contactId);
        if (result == 0) {
            Toast.makeText(this, R.string.contact_not_deleted, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.contact_deleted, Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
