package ru.internetcloud.addressbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ContactAddEditFragment extends Fragment {

    public interface Callbacks {
        public void onNewContactSaved(Contact contact);
        public void onUpdateContact(Contact contact);
    }

    private Callbacks hostActivity;

    private static final String ARG_CONTACT_ID = "ru.internetcloud.addressbook.contact_id";

    private static final String TAG_CONFIRM_SAVE = "Confirm_save";
    private static final int REQUEST_CONFIRM_SAVE = 1;
    private static final int REQUEST_PHOTO = 2;

    private Contact contact;

    private TextInputEditText name_text_input_edit_text;
    private TextInputEditText phone_text_input_edit_text;
    private TextInputEditText email_text_input_edit_text;
    private TextInputEditText street_text_input_edit_text;
    private TextInputEditText city_text_input_edit_text;
    private TextInputEditText state_text_input_edit_text;
    private TextInputEditText zip_text_input_edit_text;

    private Button save_button;
    private FloatingActionButton save_fab;

    private ImageView contact_image_view;
    private File contactPhotoFile;

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
        } else {
            contact = new Contact(0);
        }

        contactPhotoFile = ContactLab.getInstance(getActivity()).getPhotoFile(contact);
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

        save_fab = view.findViewById(R.id.save_fab);
        save_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        if (contact.getId() != 0) {
            name_text_input_edit_text.setText(contact.getName());
            phone_text_input_edit_text.setText(contact.getPhone());
            email_text_input_edit_text.setText(contact.getEmail());
            street_text_input_edit_text.setText(contact.getStreet());
            city_text_input_edit_text.setText(contact.getCity());
            state_text_input_edit_text.setText(contact.getState());
            zip_text_input_edit_text.setText(contact.getZip());
        }

        // обращение к фотокамере, чтобы сделать фото контакта:
        contact_image_view = view.findViewById(R.id.contact_image_view);
        updatePhotoView();

        PackageManager packageManager = getActivity().getPackageManager();

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = contactPhotoFile != null && captureImage.resolveActivity(packageManager) != null;

        contact_image_view.setEnabled(canTakePhoto); // заблокировать фото-кнопку, если на телефоне нет камеры или нет места куда сохранять фото-файл.

        contact_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhotoFromCamera();
                Uri uri = FileProvider.getUriForFile(getActivity(), "ru.internetcloud.addressbook.fileprovider", contactPhotoFile);

                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                // список приложений(активити), которые могут снимок сделать:
                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager()
                        .queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        return view;
    }

    public void getPhotoFromCamera() {

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

    @Override
    public void onPause() {
        super.onPause();
//        // если пользователь отредактировал контакт, но выходит без сохранения - задать вопрос:
//        boolean isNameEmpty = "".equals(name_text_input_edit_text.getText().toString());
//        boolean isNameEquals = name_text_input_edit_text.getText().toString().equals(contact.getName());
//        boolean isPhoneEquals = phone_text_input_edit_text.getText().toString().equals(contact.getPhone());
//
//        if (!isNameEmpty || !isNameEquals || !isPhoneEquals) {
//            FragmentManager fragmentManager = getFragmentManager();
//            ConfirmSaveFragment confirmSaveFragment = new ConfirmSaveFragment();
//            confirmSaveFragment.setTargetFragment(this, REQUEST_CONFIRM_SAVE); // Назначение целевого фрагмента
//            confirmSaveFragment.show(fragmentManager, TAG_CONFIRM_SAVE);
//        } else {
//
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // этот метод будет вызван из диалогового фрагмента, который передаст интент с информацией, что выбрал пользователь: удаление или нет.
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CONFIRM_SAVE) {
            boolean isSaved = data.getBooleanExtra(ConfirmSaveFragment.KEY_CONFIRM_SAVE, false);
            if (isSaved) {
                saveChanges();
            }
        } else if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(), "ru.internetcloud.addressbook.fileprovider", contactPhotoFile);

            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();
        }
    }

    private void saveChanges() {
        String name = name_text_input_edit_text.getText().toString();
        if ("".equals(name)) {
            Toast.makeText(getActivity(), "Error! Fill Name, please", Toast.LENGTH_SHORT).show();
        } else {
            fillContact();
            if (contact.getId() == 0) {
                hostActivity.onNewContactSaved(contact);
            } else {
                hostActivity.onUpdateContact(contact);
            }
        }
    }

    private void updatePhotoView() {
        if (contactPhotoFile == null || !contactPhotoFile.exists()) {
            Drawable ic_photo_camera = getResources().getDrawable(R.drawable.ic_photo_camera_white_24dp);
            contact_image_view.setImageDrawable(ic_photo_camera);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(contactPhotoFile.getPath(), getActivity());
            contact_image_view.setImageBitmap(bitmap);
        }
    }
}
