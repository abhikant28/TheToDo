package akw.android.thetodo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class ImageEditor extends AppCompatActivity {

    private ImageView iv_image;
    private Button b_cancel;
    private Button b_accept;
    private Bitmap BImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_editor);
        iv_image = findViewById(R.id.ImageEditor_ImageView_Image);
        b_accept = findViewById(R.id.ImagEditor_Button_Accept);
        b_cancel = findViewById(R.id.ImagEditor_Button_Cancel);

        if (getIntent().getExtras() != null) {
            //Log.i("INTENT EXTRA::::::::::: ", "Not null");
            BImg = (Bitmap) getIntent().getExtras().get("img");
        }

        b_accept.setOnClickListener(view -> {
            Intent data = new Intent();
            data.setData(Uri.parse(String.valueOf(BImg)));
            setResult(RESULT_OK, data);
            finish();
        });

        b_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}