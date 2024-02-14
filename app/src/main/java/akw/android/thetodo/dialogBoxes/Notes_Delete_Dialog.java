package akw.android.thetodo.dialogBoxes;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;


import akw.android.thetodo.AppObjects.Notes;
import akw.android.thetodo.Keys;
import akw.android.thetodo.MainActivity;
import akw.android.thetodo.R;

public class Notes_Delete_Dialog extends AppCompatDialogFragment {

    private CheckBox cb_never;
    private Notes note;

    public Notes_Delete_Dialog(Notes task) {
        this.note = task;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.main_delete_note, null);

        cb_never = v.findViewById(R.id.Main_Task_Delete_CheckBox);

        builder.setView(v)
                .setTitle("Delete Note")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.viewModel.delete(note);
                        if (cb_never.isChecked()) {
                            SharedPreferences sharedPreferences = getContext().getSharedPreferences(Keys.SHARED_PREFS_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor SPeditor = sharedPreferences.edit();
                            SPeditor.putBoolean("NEVER_REMIND_NOTES_DELETE", true);
                            SPeditor.apply();
                        }
                    }
                });
        return builder.create();
    }
}
