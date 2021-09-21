package com.example.thetodo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.thetodo.AppObjects.Notes;
import com.example.thetodo.DataBase.TheViewModel;

import java.util.Date;

public class EditNote extends AppCompatActivity {

    private TheViewModel viewModel;
    private EditText ev_title, ev_desc;
    private int n_id;
    private int g_id;
    private Notes theNote;
    private boolean isNew = true;
    final int CAMERA_INTENT= 51;
    final int IMAGE_EDITOR_INTENT=50;
    private Bitmap bmpImg;
    private ImageView iv_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        viewModel = ViewModelProviders.of(this).get(TheViewModel.class);

        ev_title = findViewById(R.id.NotesEditor_EditTextView_Title);
        ev_desc = findViewById(R.id.NotesEditor_EditTextView_all);
        iv_image=findViewById(R.id.NotesEditor_ImageView_image);

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
            if(title!= theNote.getTitle()||desc !=theNote.getBody()){
                theNote.setTitle(title);
                theNote.setBody(desc);
                Date getDate=java.util.Calendar.getInstance().getTime();
                theNote.setDate(String.valueOf(getDate).substring(0, 10));
                viewModel.update(theNote);
            }
        }

    }

    public void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null){
            Log.i("TAKePIC", "Triggered");
            startActivityForResult(intent, CAMERA_INTENT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CAMERA_INTENT:
                if(resultCode== Activity.RESULT_OK){
                    bmpImg=(Bitmap) data.getExtras().get("data");
                    if(bmpImg!=null){
                        Intent intent= new Intent(this,ImageEditor.class);
                        intent.putExtra("img", bmpImg);
                        startActivityForResult(intent,IMAGE_EDITOR_INTENT);
                    }
                }
                break;
            case IMAGE_EDITOR_INTENT:
                if(resultCode==Activity.RESULT_OK){
                    bmpImg=(Bitmap) data.getExtras().get("data");
                    if(bmpImg!=null){
                        iv_image.setImageBitmap(bmpImg);
                    }
                    }else{}
                break;
        }
    }

    public void pic(View view) {
        takePicture();
    }

    //    private TextWatcher checkText = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//
//        }
//    };


}