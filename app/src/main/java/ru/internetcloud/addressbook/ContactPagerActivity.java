package ru.internetcloud.addressbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import ru.internetcloud.addressbook.model.Contact;
import ru.internetcloud.addressbook.model.ContactLab;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class ContactPagerActivity extends AppCompatActivity
    implements ContactFragment.Callbacks {

    private static final String KEY_CONTACT_ID = "ru.internetcloud.addressbook.contact_id";
    private static final String KEY_QUERY = "ru.internetcloud.addressbook.query";
    private ViewPager contact_view_pager;
    private List<Contact> contactList;

    public static Intent newIntent(Context context, long contactId, String query) {
        Intent intent = new Intent(context, ContactPagerActivity.class);
        intent.putExtra(KEY_CONTACT_ID, contactId);
        intent.putExtra(KEY_QUERY, query);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_pager);

        boolean isTablet = getResources().getBoolean(R.bool.is_tablet);
        if (!isTablet) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        long contactId = getIntent().getLongExtra(KEY_CONTACT_ID, 0);
        String query = getIntent().getStringExtra(KEY_QUERY);

        contact_view_pager = findViewById(R.id.contact_view_pager);

        contactList = ContactLab.getInstance(this).getContactList(query);
        FragmentManager fragmentManager = getSupportFragmentManager();
        contact_view_pager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public int getCount() {
                return contactList.size();
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {
                Contact currentContact = contactList.get(position);
                return ContactFragment.newInstance(currentContact.getId());
            }
        });

        for (int i = 0; i < contactList.size(); i++) {
            if (contactList.get(i).getId() == contactId) {
                contact_view_pager.setCurrentItem(i);
                break;
            }
        }
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
