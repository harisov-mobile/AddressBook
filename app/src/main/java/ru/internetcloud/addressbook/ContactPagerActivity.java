package ru.internetcloud.addressbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import ru.internetcloud.addressbook.dialog.ConfirmSaveFragment;
import ru.internetcloud.addressbook.model.Contact;
import ru.internetcloud.addressbook.model.ContactLab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactPagerActivity extends AppCompatActivity
    implements ContactFragment.Callbacks {

    private static final String TAG = "rustam";

    private static final int REQUEST_ADD_EDIT = 0;

    private static final String KEY_CONTACT_ID = "ru.internetcloud.addressbook.contact_id";
    private static final String KEY_QUERY = "ru.internetcloud.addressbook.query";
    private ViewPager contact_view_pager;
    //private List<Contact> contactList;
    private List<Long> contactIdList;
    private int currentPosition;
    private FragmentStatePagerAdapter fragmentStatePagerAdapter;

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

        String subtitle = "";
        if (query != null && !query.equals("")) {
            subtitle = "" + getResources().getString(R.string.search) + ": " + query;
        }

        final AppCompatActivity currentActivity = this;
        currentActivity.getSupportActionBar().setSubtitle(subtitle);

        contact_view_pager = findViewById(R.id.contact_view_pager);

        //contactList = ContactLab.getInstance(this).getContactList(query);
        contactIdList = ContactLab.getInstance(this).getContactIdList(query);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentStatePagerAdapter = new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public int getCount() {
                //return contactList.size();
                return contactIdList.size();
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {
//                Contact currentContact = contactList.get(position);
//                return ContactFragment.newInstance(currentContact.getId());
                return ContactFragment.newInstance(contactIdList.get(position));
            }

            @Override
            public int getItemPosition(@NonNull Object object) {
                // этот метод срабатывает только тогда, когда у адаптера вызван метод notifyDataSetChanged
                if (object instanceof ContactFragment) {
                    ((ContactFragment)object).updateFragment();
                }
                return super.getItemPosition(object);
            }
        };

        contact_view_pager.setAdapter(fragmentStatePagerAdapter);

        contact_view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                Log.i(TAG, " onPageSelected : currentPosition = " + currentPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        for (int i = 0; i < contactList.size(); i++) {
//            if (contactList.get(i).getId() == contactId) {
//                currentPosition = i;
//                Log.i(TAG, " currentPosition = " + currentPosition);
//                //contact_view_pager.setCurrentItem(i);
//                break;
//            }
//        }
        for (int i = 0; i < contactIdList.size(); i++) {
            if (contactIdList.get(i) == contactId) {
                currentPosition = i;
                Log.i(TAG, " currentPosition = " + currentPosition);
                //contact_view_pager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // обновляю фрагмент, т.к. возможно, что было редактирование и поля поменялись.
        Log.i(TAG, " onResume : currentPosition = " + currentPosition);
        contact_view_pager.setCurrentItem(currentPosition);


//        ContactFragment contactFragment = ContactFragment.newInstance(currentContact.getId());
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, contactFragment)
//                .commit();
    }

    @Override
    public void onEditContact(long contactId) {
        Intent intent = ContactAddEditActivity.newIntent(this, contactId);
        //startActivity(intent);
        startActivityForResult(intent, REQUEST_ADD_EDIT);
        // finish(); // открыть на редактирование существующий контакт, но закрыть саму себя:
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        // этот метод будет вызван из ContactAddEditFragment
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_ADD_EDIT) {
            fragmentStatePagerAdapter.notifyDataSetChanged();

            // этот код оставил как пример декодирование результата
//            if (data == null) {
//                return;
//            } else {
//                // обновить из базы данных contactList.get(currentPosition)
//                long contactId = ContactAddEditActivity.getContactId(data); // декодирование результата (чтобы не знать и не хранить где-то наименование ключа
//                if (contactId > 0) {
//                    Contact contact = ContactLab.getInstance(this).getContact(contactId); // считаю свежую информацию о контакте из базы данных.
//                    if (contact != null) {
//                        for (int i = 0; i < contactList.size(); i++) {
//                            if (contactList.get(i).getId() == contactId) {
//                                currentPosition = i;
//                                contactList.set(i, contact); // обновлю контакт в списке контактов.
//                                fragmentStatePagerAdapter.notifyDataSetChanged();
//                                //Log.i(TAG, " currentPosition = " + currentPosition);
//                                //contact_view_pager.setCurrentItem(i);
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
        }

    }
}
