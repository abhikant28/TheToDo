package com.example.thetodo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.thetodo.AppObjects.Notes;
import com.example.thetodo.DataBase.TheViewModel;

public class EditNote extends AppCompatActivity {

    private TheViewModel viewModel;
    private EditText ev_title, ev_desc;
    private int n_id;
    private int g_id=0;
    private Notes theNote;
    private boolean isNew = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        viewModel = ViewModelProviders.of(this).get(TheViewModel.class);

        ev_title = findViewById(R.id.NotesEditor_EditTextView_Title);
        ev_desc = findViewById(R.id.NotesEditor_EditTextView_all);

        Intent extras = getIntent();
        if (extras.getBooleanExtra("isNew", true)) {
            g_id=extras.getIntExtra("g_id", 0);
        } else {
            n_id = extras.getIntExtra("note_id", 0);
            theNote = viewModel.getNote(n_id);
            ev_desc.setText(theNote.getBody());
            ev_title.setText(theNote.getTitle());
            isNew = false;
        }
    }

    @Override
    public void onBackPressed() {
        if (ev_title.getText().toString().isEmpty() && ev_desc.getText().toString().isEmpty()) {
        } else {
            save();
        }
        super.onBackPressed();
    }

    public void save() {
        String title = ev_title.getText().toString();
        String desc = ev_desc.getText().toString();
        if (isNew) {
            Notes note = new Notes(title.trim(), desc, 0);
            note.setG_id(g_id);
            viewModel.insert(note);
            isNew = false;
        } else {
            theNote.setTitle(title);
            theNote.setBody(desc);
            viewModel.update(theNote);
        }

    }

    private TextWatcher checkText = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


}