package com.example.thetodo.dialogBoxes;

import static android.content.Context.MODE_PRIVATE;
import static com.example.thetodo.MainActivity.SHARED_PREFS_NAME;
import static com.example.thetodo.MainActivity.viewModel;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.thetodo.AlertReceiver;
import com.example.thetodo.AppObjects.Notes;
import com.example.thetodo.MainActivity;
import com.example.thetodo.R;

public class Notes_Delete_Dialog extends AppCompatDialogFragment {

    private CheckBox cb_never;
    private Notes note;
    Context cxt;

    public Notes_Delete_Dialog(Notes task) {
        this.note =task;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.main_delete_note, null);

        cb_never=v.findViewById(R.id.Main_Task_Delete_CheckBox);

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
                        viewModel.delete(note);
                        if(cb_never.isChecked()){
                            SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor SPeditor = sharedPreferences.edit();
                            SPeditor.putBoolean("NEVER_REMIND_NOTES_DELETE", true);
                            SPeditor.apply();
                        }
                    }
                });
        return builder.create();
    }
}
