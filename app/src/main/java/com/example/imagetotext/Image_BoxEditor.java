package com.example.imagetotext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.graphics.Bitmap;
import android.widget.ImageView;


public class  Image_BoxEditor extends AppCompatActivity {
    public Button confirmObj;
    public Button recaptureObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image__box_editor);
        confirmObj = (Button) findViewById(R.id.confirmButton);
        confirmObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Image_BoxEditor.this, text_Copy.class);
                startActivity(intent);
            }
        });
        recaptureObj = (Button) findViewById(R.id.changePhotoButton);
        recaptureObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Image_BoxEditor.this, MainActivity.class);
                startActivity(intent1);
            }
        });
        Intent intent = getIntent();
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("imageToTransmitKey");
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        //Bitmap bmp = BitmapFactory.decodeByteArray(buffer, start, a);
        imageView.setImageBitmap(bitmap);
    }
}

