package ru.internetcloud.addressbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ContactActivity extends TemplateFragmentActivity {

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
}
