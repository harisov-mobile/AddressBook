package ru.internetcloud.addressbook;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ContactAddEditFragment extends Fragment {

    public interface Callbacks {
        public void onNewContactSaved(Contact contact);
        public void onUpdateContact(Contact contact);
    }

    private Callbacks hostActivity;

    private static final String ARG_CONTACT_ID = "ru.internetcloud.addressbook.contact_id";

    private Contact contact;

    private TextInputEditText name_text_input_edit_text;
    private TextInputEditText phone_text_input_edit_text;
    private TextInputEditText email_text_input_edit_text;
    private TextInputEditText street_text_input_edit_text;
    private TextInputEditText city_text_input_edit_text;
    private TextInputEditText state_text_input_edit_text;
    private TextInputEditText zip_text_input_edit_text;

    private Button save_button;

    public static ContactAddEditFragment newInstance(long contactId) {
        ContactAddEditFragment addEditFragment = new ContactAddEditFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ARG_CONTACT_ID, contactId);
        addEditFragment.setArguments(bundle);
        return addEditFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long contactId = getArguments().getLong(ARG_CONTACT_ID, 0);
        if (contactId > 0) {
            contact = ContactLab.getInstance(getActivity()).getContact(contactId);
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_add_edit, container, false);

        name_text_input_edit_text = view.findViewById(R.id.name_text_input_edit_text);
        phone_text_input_edit_text = view.findViewById(R.id.phone_text_input_edit_text);
        email_text_input_edit_text = view.findViewById(R.id.email_text_input_edit_text);
        street_text_input_edit_text = view.findViewById(R.id.street_text_input_edit_text);
        city_text_input_edit_text = view.findViewById(R.id.city_text_input_edit_text);
        state_text_input_edit_text = view.findViewById(R.id.state_text_input_edit_text);
        zip_text_input_edit_text = view.findViewById(R.id.zip_text_input_edit_text);

        save_button = view.findViewById(R.id.save_button);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                String name = name_text_input_edit_text.getText().toString();
                if ("".equals(name)) {
                    Toast.makeText(getActivity(), "Error! Fill Name, please", Toast.LENGTH_SHORT).show();
                } else {
                    if (contact == null) {
                        contact = new Contact(0);
                        fillContact();

                        hostActivity.onNewContactSaved(contact);

                    } else {
                        fillContact();
                        hostActivity.onUpdateContact(contact);

                    }
                }
            }
        });

        if (contact != null) {
            name_text_input_edit_text.setText(contact.getName());
            phone_text_input_edit_text.setText(contact.getPhone());
            email_text_input_edit_text.setText(contact.getEmail());
            street_text_input_edit_text.setText(contact.getStreet());
            city_text_input_edit_text.setText(contact.getCity());
            state_text_input_edit_text.setText(contact.getState());
            zip_text_input_edit_text.setText(contact.getZip());
        }

        return view;
    }

    private void fillContact() {
        contact.setName(name_text_input_edit_text.getText().toString());
        contact.setPhone(phone_text_input_edit_text.getText().toString());
        contact.setEmail(email_text_input_edit_text.getText().toString());
        contact.setStreet(street_text_input_edit_text.getText().toString());
        contact.setCity(city_text_input_edit_text.getText().toString());
        contact.setState(state_text_input_edit_text.getText().toString());
        contact.setZip(zip_text_input_edit_text.getText().toString());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        hostActivity = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hostActivity = null;
    }
}
