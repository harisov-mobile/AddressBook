package ru.internetcloud.addressbook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class IncreasedImageFragment extends DialogFragment {



    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_image, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        //alertDialogBuilder.setTitle(R.string.confirm_title);
        //alertDialogBuilder.setMessage(R.string.confirm_message);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setPositiveButton(android.R.string.ok, null);

        return alertDialogBuilder.create();
    }


}
