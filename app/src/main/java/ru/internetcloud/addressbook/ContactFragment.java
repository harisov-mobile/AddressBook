package ru.internetcloud.addressbook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ContactFragment extends Fragment {

    public static final String ARG_CONTACT_ID = "ru.internetcloud.addressbook.contact_id";

    private Contact contact;

    private EditText name_edit_text;
    private EditText phone_edit_text;
    private Button save_button;

    public static ContactFragment newInstance(long contactId) {
        ContactFragment contactFragment = new ContactFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ARG_CONTACT_ID, contactId);
        contactFragment.setArguments(bundle);
        return contactFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long contactId = getArguments().getLong(ARG_CONTACT_ID, 0);
        contact = ContactLab.getInstance(getActivity()).getContact(contactId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        name_edit_text = view.findViewById(R.id.name_edit_text);
        phone_edit_text = view.findViewById(R.id.phone_edit_text);
        save_button = view.findViewById(R.id.save_button);

        if (contact != null) {
            name_edit_text.setText(contact.getName());
            phone_edit_text.setText(contact.getPhone());
        }

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name_edit_text.getText().toString();
                if ("".equals(name)) {
                    Toast.makeText(getActivity(), "Error! Fill name, please", Toast.LENGTH_SHORT).show();
                } else {
                    if (contact == null) {
                        contact = new Contact(0);
                        contact.setName(name);
                        contact.setPhone(phone_edit_text.getText().toString());
                        ContactLab.getInstance(getActivity()).addContact(contact);
                    } else {
                        contact.setName(name);
                        contact.setPhone(phone_edit_text.getText().toString());
                        ContactLab.getInstance(getActivity()).updateContact(contact);
                    }
                    // finish();
                }
            }
        });

        return view;
    }
}
