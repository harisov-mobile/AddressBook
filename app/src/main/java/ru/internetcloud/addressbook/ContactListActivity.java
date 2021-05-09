package ru.internetcloud.addressbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ContactListActivity extends TemplateFragmentActivity
    implements ContactListFragment.Callbacks, ContactFragment.Callbacks, ContactAddEditFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return ContactListFragment.newInstance();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_list_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSelectContact(long contactId) {

        View view = findViewById(R.id.fragment_detail_container);

        if (view == null) {
            // это телефон:
            // открыть на просмотр существующий контакт:
            Intent intent = ContactPagerActivity.newIntent(this, contactId);
            startActivity(intent);
        } else {
            // это планшет:
            ContactFragment contactFragment = ContactFragment.newInstance(contactId);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_detail_container, contactFragment)
                    .commit();
        }
    }

    @Override
    public void onAddContact() {
        View view = findViewById(R.id.fragment_detail_container);

        if (view == null) {
            // это телефон:
            // открыть на редактирование новый контакт:
            Intent intent = ContactAddEditActivity.newIntent(this, 0);
            startActivity(intent);
        } else {
            // это планшет:
            ContactAddEditFragment contactAddEditFragment = ContactAddEditFragment.newInstance(0);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_detail_container, contactAddEditFragment)
                    .commit();
        }
    }

    @Override
    public void onEditContact(long contactId) {
        // это планшет:
        ContactAddEditFragment contactAddEditFragment = ContactAddEditFragment.newInstance(contactId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_detail_container, contactAddEditFragment)
                .commit();
    }

    @Override
    public void onDeleteContact(long contactId) {
        int result = ContactLab.getInstance(this).deleteContact(contactId);
        if (result == 0) {
            Toast.makeText(this, R.string.contact_not_deleted, Toast.LENGTH_SHORT).show();
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            ContactFragment contactFragment = (ContactFragment) fragmentManager.findFragmentById(R.id.fragment_detail_container);
            fragmentManager.beginTransaction()
                    .remove(contactFragment)
                    .commit();

            ContactListFragment contactListFragment = (ContactListFragment) fragmentManager.findFragmentById(R.id.fragment_container);
            contactListFragment.updateUI();

            Toast.makeText(this, R.string.contact_deleted, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNewContactSaved(Contact contact) {
        // кнопка SAVE
        long contactId = ContactLab.getInstance(this).addContact(contact);
        if (contactId == -1) {
            Toast.makeText(this, R.string.contact_not_added, Toast.LENGTH_SHORT).show();
        } else {
            // контакт успешно добавлен, надо фрагмент другой показать:
            ContactFragment contactFragment = ContactFragment.newInstance(contactId);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_detail_container, contactFragment)
                    .commit();

            ContactListFragment contactListFragment = (ContactListFragment) fragmentManager.findFragmentById(R.id.fragment_container);
            contactListFragment.updateUI();

            Toast.makeText(this, R.string.contact_added, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpdateContact(Contact contact) {
        // кнопка SAVE
        int result = ContactLab.getInstance(this).updateContact(contact);
        if (result == 0) {
            Toast.makeText(this, R.string.contact_not_updated, Toast.LENGTH_SHORT).show();
        } else {
            // контакт успешно добавлен, надо фрагмент другой показать:
            ContactFragment contactFragment = ContactFragment.newInstance(contact.getId());
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_detail_container, contactFragment)
                    .commit();

            ContactListFragment contactListFragment = (ContactListFragment) fragmentManager.findFragmentById(R.id.fragment_container);
            contactListFragment.updateUI();

            Toast.makeText(this, R.string.contact_updated, Toast.LENGTH_SHORT).show();
        }
    }
}
