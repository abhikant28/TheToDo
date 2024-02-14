package akw.android.thetodo;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import akw.android.thetodo.AppObjects.Notes;
import akw.android.thetodo.DataBase.TheViewModel;

public class EditNote extends AppCompatActivity {

    final int IMAGE_TO_TEXT_INTENT = 52, CAMERA_INTENT = 51, IMAGE_EDITOR_INTENT = 50;
    private TheViewModel viewModel;
    private EditText ev_title, ev_desc;
    private int n_id, g_id;
    private Notes theNote;
    private boolean isNew = true;
    private Bitmap bmpImg;
    private ImageView iv_image;
    private Toolbar toolbar;
    private TextToSpeech textToSpeech;
    private TextRecognizer textRecognizer;
    private ImageView iv_image_text_dialog_cover;
    private EditText et_image_text_dialog_output;
    private ProgressBar pb_image_text_dialog_progress;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_INTENT:
                if (resultCode == Activity.RESULT_OK) {
                    bmpImg = (Bitmap) data.getExtras().get("data");
                    if (bmpImg != null) {
                        Intent intent = new Intent(this, ImageEditor.class);
                        intent.putExtra("img", bmpImg);
                        startActivityForResult(intent, IMAGE_EDITOR_INTENT);
                    }
                }
                break;
            case IMAGE_EDITOR_INTENT:
                if (resultCode == Activity.RESULT_OK) {
                    bmpImg = (Bitmap) data.getExtras().get("data");
                    if (bmpImg != null) {
                        iv_image.setImageBitmap(bmpImg);
                    }
                } else {
                }
                break;
            case IMAGE_TO_TEXT_INTENT:
                if (data != null) {
                    showImageToTextDialog();
                    imageToText(data.getData());
                }
                break;
        }
    }


    @Override
    public void finish() {
        if (!ev_title.getText().toString().trim().isEmpty() || !ev_desc.getText().toString().trim().isEmpty()) {
            save();
        }
        overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editnote_typeselect, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.NoteEdit_MenuOption_ReadAloud:
                readAloud();
                break;
            case R.id.NoteEdit_MenuOption_ScanText:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMAGE_TO_TEXT_INTENT);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        toolbar = findViewById(R.id.EditNote_Toolbar1);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProviders.of(this).get(TheViewModel.class);

        ev_title = findViewById(R.id.NotesEditor_EditTextView_Title);
        ev_desc = findViewById(R.id.NotesEditor_EditTextView_all);
        iv_image = findViewById(R.id.NotesEditor_ImageView_image);

        Intent extras = getIntent();
        if (extras.getBooleanExtra("isNew", true)) {
            g_id = extras.getIntExtra("g_id", 0);
        } else {
            n_id = extras.getIntExtra("note_id", 0);
            theNote = viewModel.getNote(n_id);
            ev_desc.setText(theNote.getBody());
            ev_title.setText(theNote.getTitle());
            isNew = false;
        }
    }
    private void showImageToTextDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.image_to_text_text_dialog);

        iv_image_text_dialog_cover = dialog.findViewById(R.id.OCR_imageView_cover);
        et_image_text_dialog_output = dialog.findViewById(R.id.OCR_editText_output);
        pb_image_text_dialog_progress = dialog.findViewById(R.id.OCR_progressBar);
        Button bt_image_image_text_dialog_copy = dialog.findViewById(R.id.OCR_button_copy);
        Button bt_image_image_text_dialog_dismiss = dialog.findViewById(R.id.OCR_button_dismiss);
        pb_image_text_dialog_progress.setVisibility(View.VISIBLE);
        iv_image_text_dialog_cover.setVisibility(View.VISIBLE);
        et_image_text_dialog_output.setVisibility(View.INVISIBLE);
        bt_image_image_text_dialog_copy.setOnClickListener(view -> {

            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", et_image_text_dialog_output.getText().toString());
            clipboard.setPrimaryClip(clip);
            dialog.dismiss();
            Toast.makeText(this,"Text Copied to clipboard",Toast.LENGTH_SHORT ).show();
        });

        bt_image_image_text_dialog_dismiss.setOnClickListener(view ->{
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT );
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.OcrOutputPopUpDialog;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    public void imageToText(Uri uri) {
        TextRecognizer textRecognizer =
                TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        TextRecognizer textRecognizerDevnagri =
                TextRecognition.getClient(new DevanagariTextRecognizerOptions.Builder().build());

//        Bitmap bitmap = null;
//        try {
//            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (bitmap != null) {
        try {
            InputImage inputImage = InputImage.fromFilePath(this, uri);

            Task<Text> result = textRecognizer.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                            et_image_text_dialog_output.setText(text.getText());
                            et_image_text_dialog_output.setVisibility(View.VISIBLE);
                            pb_image_text_dialog_progress.setVisibility(View.GONE);
                            iv_image_text_dialog_cover.setVisibility(View.GONE);
                        }
                    })
                    .addOnFailureListener(e -> Log.e("OCR::::::",e.toString() ));

        } catch (IOException e) {
            e.printStackTrace();
        }


//        }
    }


    public void readAloud() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language Missing");
                    } else {
                        textToSpeech.setSpeechRate(1);
                        textToSpeech.speak(ev_desc.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }
        });
    }

    public void save() {
        String title = ev_title.getText().toString().trim();
        String desc = ev_desc.getText().toString().trim();
        if (isNew) {
            Notes note = new Notes(title.trim(), desc);
            note.setG_id(g_id);
            viewModel.insert(note);
            isNew = false;
        } else {
            if (!title.equals(theNote.getTitle()) || !desc.equals(theNote.getBody())) {
                theNote.setTitle(title);
                theNote.setBody(desc);
                Date getDate = java.util.Calendar.getInstance().getTime();
                theNote.setDate(String.valueOf(getDate).substring(0, 10));
                viewModel.update(theNote);
            }
        }

    }

    public void takePicture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 8);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                //Log.i("TAKePIC", "Triggered");
                startActivityForResult(intent, CAMERA_INTENT);
            }
        }
    }

    public void pic(View view) {
        takePicture();
    }

    public void copyImageText(View view) {


    }
}