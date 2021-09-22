package com.example.thetodo.dialogBoxes;

import static com.example.thetodo.MainActivity.viewModel;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatCallback;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.thetodo.AppObjects.Groups;
import com.example.thetodo.R;

public class NewGroup_Dialog extends AppCompatDialogFragment {

    private EditText ev_group_name;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.main_new_group_dialog, null);
        builder.setView(v)
                .setTitle("New Group")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!ev_group_name.getText().toString().isEmpty()) {
                            Log.i("GROUP::::", "Created....");
                            viewModel.insert(new Groups(ev_group_name.getText().toString()));
                        }
                    }
                });
        ev_group_name = v.findViewById(R.id.Main_Group_Dialog_EditText_NewGroup);
        return builder.create();
    }
}
