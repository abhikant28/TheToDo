package akw.android.thetodo;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

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
    private int n_id, g_id, alignment = 0;
    private Notes theNote;
    private boolean isNew = true, textBold = false, textItalic = false, textUnderline = false, textStrike = false, listing = false;
    private Bitmap bmpImg;
    private ImageView iv_image, iv_image_text_dialog_cover;
    private Toolbar toolbar;
    private TextToSpeech textToSpeech;
    private EditText et_image_text_dialog_output;
    private ProgressBar pb_image_text_dialog_progress;
    SpannableStringBuilder previousStyledText;
    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            SpannableStringBuilder spannable = new SpannableStringBuilder(text);
            int cursorPosition = ev_desc.getSelectionEnd();

            updatePrevStyles(previousStyledText, spannable);

// Apply new styling to the typed text
            if (textBold) {
                spannable.setSpan(new StyleSpan(Typeface.BOLD),
                        Math.max(0, cursorPosition - 1), cursorPosition,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (textItalic) {
                spannable.setSpan(new StyleSpan(Typeface.ITALIC),
                        Math.max(0, cursorPosition - 1), cursorPosition,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (textStrike) {
                spannable.setSpan(new StrikethroughSpan(),
                        Math.max(0, cursorPosition - 1), cursorPosition,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (textUnderline) {
                spannable.setSpan(new UnderlineSpan(),
                        Math.max(0, cursorPosition - 1), cursorPosition,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

// Update the EditText with the styled text
            ev_desc.removeTextChangedListener(this);
            ev_desc.setText(spannable);

// Set the cursor position
            if (cursorPosition >= 0 && cursorPosition <= spannable.length()) {
                ev_desc.setSelection(cursorPosition);
            } else {
                // If the previous cursor position is out of bounds, move it to the end
                ev_desc.setSelection(spannable.length());
            }
            ev_desc.addTextChangedListener(this);

// Store the styled text for future reference
            previousStyledText = spannable;

        }
    };

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
    public void onBackPressed() {
        finish();
        save();
        super.onBackPressed();
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
                findViewById(R.id.NotesEditor_cv_stop).setVisibility(View.VISIBLE);
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

        Intent extras = getIntent();
        if (extras.getBooleanExtra("isNew", true)) {
            g_id = extras.getIntExtra("g_id", 0);
        } else {
            n_id = extras.getIntExtra("note_id", 0);
            theNote = viewModel.getNote(n_id);
            Spanned spanned= Html.fromHtml(theNote.getBody());
            previousStyledText= SpannableStringBuilder.valueOf(spanned);
            updatePrevStyles(previousStyledText,SpannableStringBuilder.valueOf(spanned));
            ev_desc.setText(spanned);
            ev_title.setText(theNote.getTitle());
            isNew = false;
        }
        ev_desc.addTextChangedListener(textWatcher);
        ev_desc.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                // Check if the list boolean is true
                if (listing) {
                    // Add a new line with a bullet point
                    int pos = ev_desc.getSelectionEnd();
                    ev_desc.getText().insert(pos, "\n\u2022 ");
                    ev_desc.setSelection(pos + 3);
                    return true; // Consume the key event
                }
            }
            return false; // Let the system handle the key event
        });
    }

    private void updatePrevStyles(SpannableStringBuilder previousStyledText, Spannable spannable) {
        if (previousStyledText != null) {
            Object[] spans = previousStyledText.getSpans(0, previousStyledText.length(), Object.class);
            for (Object span : spans) {
                spannable.setSpan(span, previousStyledText.getSpanStart(span), previousStyledText.getSpanEnd(span), previousStyledText.getSpanFlags(span));
            }
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
            Toast.makeText(this, "Text Copied to clipboard", Toast.LENGTH_SHORT).show();
        });

        bt_image_image_text_dialog_dismiss.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.OcrOutputPopUpDialog;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    public void imageToText(Uri uri) {
        TextRecognizer textRecognizer =
                TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        TextRecognizer textRecognizerDevnagri =
                TextRecognition.getClient(new DevanagariTextRecognizerOptions.Builder().build());

        try {
            InputImage inputImage = InputImage.fromFilePath(this, uri);

            Task<Text> result = textRecognizer.process(inputImage)
                    .addOnSuccessListener(text -> {
                        et_image_text_dialog_output.setText(text.getText());
                        et_image_text_dialog_output.setVisibility(View.VISIBLE);
                        pb_image_text_dialog_progress.setVisibility(View.GONE);
                        iv_image_text_dialog_cover.setVisibility(View.GONE);
                    })
                    .addOnFailureListener(e -> Log.e("OCR::::::", e.toString()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readAloud() {
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.ENGLISH);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language Missing");
                } else {
                    textToSpeech.setSpeechRate(1);
                    textToSpeech.speak(ev_desc.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);
                }
            }
        });
        CardView cv_stop = findViewById(R.id.NotesEditor_cv_stop);
        ImageView iv_stop = findViewById(R.id.NotesEditor_iv_stop);
        iv_stop.setOnClickListener(view -> {
            Log.i("textToSpeech.setOnClickListener::::", "Clicked : ");
            textToSpeech.stop();
            cv_stop.setVisibility(View.GONE);
        });
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

            }

            @Override
            public void onDone(String s) {
                Log.i("textToSpeech.onDOne::::", "Finished: " + s);
                cv_stop.setVisibility(View.GONE);
            }

            @Override
            public void onError(String s) {
                Log.i("textToSpeech.onError::::", "Error : " + s);

            }
        });
    }

    public void save() {
        String title = ev_title.getText().toString().trim();
        Spannable spannable = ev_desc.getText();
        updatePrevStyles(previousStyledText, spannable);
        String desc = Html.toHtml(spannable);
        if (isNew) {
            Notes note = new Notes(title.trim(), desc);
            note.setG_id(g_id);
            viewModel.insert(note);
            isNew = false;
        } else {
            if (!previousStyledText.equals(spannable) || !title.equals(theNote.getTitle()) || !desc.equals(theNote.getBody())) {
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


//    public void listButton(View view) {
//        ImageView iv_ = findViewById(R.id.NotesEditor_iv_list);
//        CardView cv_ = findViewById(R.id.NotesEditor_cv_list);
//        int pos = ev_desc.getSelectionEnd()-1;
//        if (listing) {
//            String s = ev_desc.getText().toString();
//            Editable es = ev_desc.getEditableText();
//            if (s.charAt(pos - 1) == '\u2022' && s.charAt(pos) == ' ') {
//                es.delete(pos - 1, pos);
//                ev_desc.setText(es);
//                ev_desc.setSelection(pos - 2);
//            } else if (s.charAt(pos) == '\u2022') {
//                es.delete(pos - 1, pos);
//                ev_desc.setText(es);
//                ev_desc.setSelection(pos);
//            }
//
//            cv_.setCardBackgroundColor(Color.TRANSPARENT);
//            iv_.setImageResource(R.drawable.ic_round_view_list_24_black);
//        } else {
//            if (ev_desc.getText().charAt(pos) == '\n') {
//                ev_desc.getText().insert(pos, "\u2022 ");
//                ev_desc.setSelection(pos + 2);
//            } else {
//                ev_desc.getText().insert(pos + 1, "\n\u2022 ");
//                ev_desc.setSelection(pos + 4);
//            }
//            cv_.setCardBackgroundColor(Color.WHITE);
//            iv_.setImageResource(R.drawable.ic_round_view_list_24_white);
//        }
//        listing = !listing;
//    }

    public void boldButton(View view) {
        ImageView iv_bold = findViewById(R.id.NotesEditor_iv_bold);
        CardView cv_bold = findViewById(R.id.NotesEditor_cv_bold);
        int selectionStart = ev_desc.getSelectionStart();
        int selectionEnd = ev_desc.getSelectionEnd();
        if (selectionStart != selectionEnd) {
            Spanned text = (Spanned) ev_desc.getText();
            Object[] spans = text.getSpans(selectionStart, selectionEnd, Object.class);

            // Apply bold style to the selected text, preserving existing styles
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            builder.setSpan(new StyleSpan(Typeface.BOLD), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            for (Object span : spans) {
                builder.setSpan(span, text.getSpanStart(span), text.getSpanEnd(span), text.getSpanFlags(span));
            }
            // Set the modified text back to the EditText
            ev_desc.setText(builder);

            // Move the cursor to the end of the selection
            ev_desc.setSelection(selectionEnd);
        } else {
            if (textBold) {
                cv_bold.setCardBackgroundColor(Color.TRANSPARENT);
                iv_bold.setImageResource(R.drawable.ic_baseline_format_bold_24_white);
            } else {
                cv_bold.setCardBackgroundColor(Color.WHITE);
                iv_bold.setImageResource(R.drawable.ic_baseline_format_bold_24_black);
            }
            textBold = !textBold;
        }
    }

    public void italicButton(View view) {
        ImageView iv_ = findViewById(R.id.NotesEditor_iv_italic);
        CardView cv_ = findViewById(R.id.NotesEditor_cv_italic);
        int selectionStart = ev_desc.getSelectionStart();
        int selectionEnd = ev_desc.getSelectionEnd();
        if (selectionStart != selectionEnd) {
            Spanned text = (Spanned) ev_desc.getText();
            Object[] spans = text.getSpans(selectionStart, selectionEnd, Object.class);

            // Apply bold style to the selected text, preserving existing styles
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            builder.setSpan(new StyleSpan(Typeface.BOLD), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            for (Object span : spans) {
                builder.setSpan(span, text.getSpanStart(span), text.getSpanEnd(span), text.getSpanFlags(span));
            }
            // Set the modified text back to the EditText
            ev_desc.setText(builder);

            // Move the cursor to the end of the selection
            ev_desc.setSelection(selectionEnd);
        } else {
            if (textItalic) {
                cv_.setCardBackgroundColor(Color.TRANSPARENT);
                iv_.setImageResource(R.drawable.ic_round_format_italic_24_white);
            } else {
                cv_.setCardBackgroundColor(Color.WHITE);
                iv_.setImageResource(R.drawable.ic_round_format_italic_24_black);
            }
            textItalic = !textItalic;

        }
    }

    public void strikeButton(View view) {
        ImageView iv_bold = findViewById(R.id.NotesEditor_iv_strike);
        CardView cv_bold = findViewById(R.id.NotesEditor_cv_strike);
        int selectionStart = ev_desc.getSelectionStart();
        int selectionEnd = ev_desc.getSelectionEnd();
        if (selectionStart != selectionEnd) {
            Spanned text = (Spanned) ev_desc.getText();
            Object[] spans = text.getSpans(selectionStart, selectionEnd, Object.class);

            // Apply bold style to the selected text, preserving existing styles
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            builder.setSpan(new StyleSpan(Typeface.BOLD), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            for (Object span : spans) {
                builder.setSpan(span, text.getSpanStart(span), text.getSpanEnd(span), text.getSpanFlags(span));
            }
            // Set the modified text back to the EditText
            ev_desc.setText(builder);

            // Move the cursor to the end of the selection
            ev_desc.setSelection(selectionEnd);
        } else {
            if (textStrike) {
                cv_bold.setCardBackgroundColor(Color.TRANSPARENT);
                iv_bold.setImageResource(R.drawable.ic_baseline_format_strikethrough_24_white);
            } else {
                cv_bold.setCardBackgroundColor(Color.WHITE);
                iv_bold.setImageResource(R.drawable.ic_baseline_format_strikethrough_24_black);
            }
            textStrike = !textStrike;

        }
    }

    public void underlineButton(View view) {
        ImageView iv_bold = findViewById(R.id.NotesEditor_iv_underLine);
        CardView cv_bold = findViewById(R.id.NotesEditor_cv_underLine);
        int selectionStart = ev_desc.getSelectionStart();
        int selectionEnd = ev_desc.getSelectionEnd();
        if (selectionStart != selectionEnd) {
            Spanned text = (Spanned) ev_desc.getText();
            Object[] spans = text.getSpans(selectionStart, selectionEnd, Object.class);

            // Apply bold style to the selected text, preserving existing styles
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            builder.setSpan(new StyleSpan(Typeface.BOLD), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            for (Object span : spans) {
                builder.setSpan(span, text.getSpanStart(span), text.getSpanEnd(span), text.getSpanFlags(span));
            }
            // Set the modified text back to the EditText
            ev_desc.setText(builder);

            // Move the cursor to the end of the selection
            ev_desc.setSelection(selectionEnd);
        } else {
            if (textUnderline) {
//                cv_bold.setCardBackgroundColor(Color.TRANSPARENT);
                iv_bold.setImageResource(R.drawable.ic_baseline_format_underlined_24_white);
            } else {
//                cv_bold.setBackgroundColor(Color.WHITE);
                iv_bold.setImageResource(R.drawable.ic_baseline_format_underlined_24_black);
            }
            textUnderline = !textUnderline;

        }
    }

    public void alignmentButton(View view) {
        alignment = alignment == 2 ? 0 : alignment + 1;
        int[] arrDraw = new int[]{R.drawable.ic_round_format_align_left_24, R.drawable.ic_baseline_format_align_center_24, R.drawable.ic_baseline_format_align_right_24};
        int[] arr = new int[]{View.TEXT_ALIGNMENT_TEXT_START, View.TEXT_ALIGNMENT_CENTER, View.TEXT_ALIGNMENT_TEXT_END};
        ev_desc.setTextAlignment(arr[alignment]);
        Spannable spannable = ev_desc.getText();
        updatePrevStyles(previousStyledText,spannable);
        ev_desc.setText(spannable);
        ImageView iv = findViewById(R.id.NotesEditor_iv_alignment);
        iv.setImageResource(arrDraw[alignment]);
    }
}
