package ru.internetcloud.addressbook.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import ru.internetcloud.addressbook.util.PictureUtils;
import ru.internetcloud.addressbook.R;

public class IncreasedImageFragment extends DialogFragment {

    private static final String ARG_FILE = "file";

    private File contactPhotoFile;


    public static IncreasedImageFragment newInstance(File file) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_FILE, file);

        IncreasedImageFragment increasedImageFragment = new IncreasedImageFragment();
        increasedImageFragment.setArguments(bundle);
        return increasedImageFragment;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        contactPhotoFile = (File) getArguments().getSerializable(ARG_FILE);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_image, null);
        ImageView increased_image_view = view.findViewById(R.id.increased_image_view);

        if (contactPhotoFile == null || !contactPhotoFile.exists()) {
            Drawable ic_photo_camera = getResources().getDrawable(R.drawable.ic_person_white_24dp);
            increased_image_view.setImageDrawable(ic_photo_camera);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(contactPhotoFile.getPath(), getActivity());
            increased_image_view.setImageBitmap(bitmap);
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        //alertDialogBuilder.setTitle(R.string.confirm_title);
        //alertDialogBuilder.setMessage(R.string.confirm_message);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setPositiveButton(android.R.string.ok, null);

        return alertDialogBuilder.create();
    }


}
