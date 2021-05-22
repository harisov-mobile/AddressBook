package ru.internetcloud.addressbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ContactFragment extends Fragment {

    public interface Callbacks {
        public void onEditContact(long contactId);
        public void onDeleteContact(long contactId);
    }

    Callbacks hostActivity;

    public static final String ARG_CONTACT_ID = "ru.internetcloud.addressbook.contact_id";
    private static final String TAG_CONFIRM_DELETE = "Confirm_delete";
    private static final int REQUEST_CONFIRM_DELETE = 0;
    private static final String DIALOG_INCREASED_IMAGE = "IncreasedImage";

    private Contact contact;

    private TextView name_text_view;
    private TextView phone_text_view;
    private TextView email_text_view;
    private TextView street_text_view;
    private TextView city_text_view;
    private TextView state_text_view;
    private TextView zip_text_view;
    private ImageView contact_image_view;

    private File contactPhotoFile;

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
        contactPhotoFile = ContactLab.getInstance(getActivity()).getPhotoFile(contact);
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
        contact_image_view = view.findViewById(R.id.contact_image_view);

        name_text_view.setText(contact.getName());
        phone_text_view.setText(contact.getPhone());
        email_text_view.setText(contact.getEmail());
        street_text_view.setText(contact.getStreet());
        city_text_view.setText(contact.getCity());
        state_text_view.setText(contact.getState());
        zip_text_view.setText(contact.getZip());

        contact_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // показать увеличенную фотографию:
                if (contactPhotoFile != null && contactPhotoFile.exists()) {
                    FragmentManager fragmentManager = getFragmentManager();
                    IncreasedImageFragment increasedImageFragment = IncreasedImageFragment.newInstance(contactPhotoFile);
                    increasedImageFragment.show(fragmentManager, DIALOG_INCREASED_IMAGE);
                }
            }
        });

        updatePhotoView();

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
        // задаем вопрос пользователю: действительно он хочет удалить контакт?
        FragmentManager fragmentManager = getFragmentManager();
        ConfirmDeleteFragment confirmDeleteFragment = new ConfirmDeleteFragment();
        confirmDeleteFragment.setTargetFragment(ContactFragment.this, REQUEST_CONFIRM_DELETE); // Назначение целевого фрагмента
        confirmDeleteFragment.show(fragmentManager, TAG_CONFIRM_DELETE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // этот метод будет вызван из диалогового фрагмента, который передаст интент с информацией, что выбрал пользователь: удаление или нет.
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CONFIRM_DELETE) {
            boolean isDeleted = data.getBooleanExtra(ConfirmDeleteFragment.KEY_CONFIRM_DELETE, false);
            if (isDeleted) {
                hostActivity.onDeleteContact(contact.getId());
            }
        }
    }

    private void updatePhotoView() {

        if (contactPhotoFile == null || !contactPhotoFile.exists()) {
            Drawable ic_photo_camera = getResources().getDrawable(R.drawable.ic_person_outline_white_24dp);
            contact_image_view.setImageDrawable(ic_photo_camera);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(contactPhotoFile.getPath(), getActivity());
            contact_image_view.setImageBitmap(bitmap);
        }
    }
}
