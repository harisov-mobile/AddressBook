package ru.internetcloud.addressbook;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ContactAddEditActivity extends TemplateFragmentActivity
    implements ContactAddEditFragment.Callbacks {

    private static final String KEY_CONTACT_ID = "ru.internetcloud.addressbook.contact_id";

    public static Intent newIntent(Context context, long contactId) {
        Intent intent = new Intent(context, ContactAddEditActivity.class);
        intent.putExtra(KEY_CONTACT_ID, contactId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        long contactId = getIntent().getLongExtra(KEY_CONTACT_ID, 0);
        return ContactAddEditFragment.newInstance(contactId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isTablet = getResources().getBoolean(R.bool.is_tablet);
        if (!isTablet) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
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
            Intent intent = ContactPagerActivity.newIntent(this, contactId);
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
            Intent intent = ContactPagerActivity.newIntent(this, contact.getId());
            startActivity(intent);
            finish();
        }
    }
}
