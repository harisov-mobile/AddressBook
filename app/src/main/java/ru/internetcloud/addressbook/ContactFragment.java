package ru.internetcloud.addressbook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class ContactFragment extends Fragment {

    public interface Callbacks {
        public void onEditContact(long contactId);
        public void onDeleteContact(long contactId);
    }

    Callbacks hostActivity;

    public static final String ARG_CONTACT_ID = "ru.internetcloud.addressbook.contact_id";

    private Contact contact;

    private TextView name_text_view;
    private TextView phone_text_view;
    private TextView email_text_view;
    private TextView street_text_view;
    private TextView city_text_view;
    private TextView state_text_view;
    private TextView zip_text_view;

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

        setHasOptionsMenu(true);

        long contactId = getArguments().getLong(ARG_CONTACT_ID, 0);
        contact = ContactLab.getInstance(getActivity()).getContact(contactId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        name_text_view = view.findViewById(R.id.name_text_view);
        phone_text_view = view.findViewById(R.id.phone_text_view);
        email_text_view = view.findViewById(R.id.email_text_view);
        street_text_view = view.findViewById(R.id.street_text_view);
        city_text_view = view.findViewById(R.id.city_text_view);
        state_text_view = view.findViewById(R.id.state_text_view);
        zip_text_view = view.findViewById(R.id.zip_text_view);

        name_text_view.setText(contact.getName());
        phone_text_view.setText(contact.getPhone());
        email_text_view.setText(contact.getEmail());
        street_text_view.setText(contact.getStreet());
        city_text_view.setText(contact.getCity());
        state_text_view.setText(contact.getState());
        zip_text_view.setText(contact.getZip());

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_contact_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case R.id.action_edit:
                hostActivity.onEditContact(contact.getId());
                return true; // обязательно в конце true возвращать!

            case R.id.action_delete:
                deleteContact();
                return true; // обязательно в конце true возвращать!

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteContact() {

        hostActivity.onDeleteContact(contact.getId());

//        int result = ContactLab.getInstance(getActivity()).deleteContact(contact);
//
//        if (result == 0) {
//            Toast.makeText(getActivity(), R.string.contact_not_deleted, Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(getActivity(), R.string.contact_deleted, Toast.LENGTH_SHORT).show();
//        }

        //confirmDelete.show(getFragmentManager(), "rustam");
    }

    public final DialogFragment confirmDelete = new DialogFragment() {
        // create an AlertDialog and return it
        @Override
        public Dialog onCreateDialog(Bundle bundle) {
            // create a new AlertDialog Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle(R.string.confirm_title);
            builder.setMessage(R.string.confirm_message);

            // provide an OK button that simply dismisses the dialog
            builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int button) {

                            int result = ContactLab.getInstance(getActivity()).deleteContact(contact.getId());

                            if (result == 0) {
                                Toast.makeText(getActivity(), R.string.contact_not_deleted, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), R.string.contact_deleted, Toast.LENGTH_SHORT).show();
                            }

                            //hostActivity.onDeleteContact
                            //listener.onContactDeleted(); // notify listener
                        }
                    }
            );

            builder.setNegativeButton(R.string.button_cancel, null);
            return builder.create(); // return the AlertDialog
        }
    };

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
