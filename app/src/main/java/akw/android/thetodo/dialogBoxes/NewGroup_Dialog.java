package akw.android.thetodo.dialogBoxes;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;


import akw.android.thetodo.AppObjects.Groups;
import akw.android.thetodo.MainActivity;

import akw.android.thetodo.R;

//public class NewGroup_Dialog extends AppCompatDialogFragment {
//
//    private EditText ev_group_name;
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View v = inflater.inflate(R.layout.main_new_group_dialog, null);
//        builder.setView(v)
//                .setTitle("New Group")
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                })
//                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        if (!ev_group_name.getText().toString().isEmpty()) {
//                            MainActivity.viewModel.insert(new Groups(ev_group_name.getText().toString()));
//                        }
//                    }
//                });
//        ev_group_name = v.findViewById(R.id.Main_Group_Dialog_EditText_NewGroup);
//        return builder.create();
//    }
//}
