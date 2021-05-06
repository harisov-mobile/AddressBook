package ru.internetcloud.addressbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class ContactListActivity extends TemplateFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return ContactListFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
