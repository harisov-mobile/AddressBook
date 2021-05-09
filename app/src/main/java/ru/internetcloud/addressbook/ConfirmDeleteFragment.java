package ru.internetcloud.addressbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ConfirmDeleteFragment extends DialogFragment {

    public static final String KEY_CONFIRM_DELETE = "ru.internetcloud.addressbook.confirm_delete";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.confirm_title);
        alertDialogBuilder.setMessage(R.string.confirm_message);
        alertDialogBuilder.setNegativeButton(R.string.button_cancel, null); // для негативного ответа ничего не делаем
        alertDialogBuilder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean isDeleted = true;
                sendResult(Activity.RESULT_OK, isDeleted);
            }
        });

        return alertDialogBuilder.create();
    }

    private void sendResult(int resultCode, boolean isDeleted) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(KEY_CONFIRM_DELETE, isDeleted);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent); // у целевого фрагмента вызываем метод onActivityResult
    }
}
