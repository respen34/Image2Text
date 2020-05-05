package com.example.imagetotext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.graphics.Bitmap;

import com.theartofdev.edmodo.cropper.CropImageView;


public class BoxActivity extends AppCompatActivity {
    private CropImageView cropImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);

        cropImageView = findViewById(R.id.cropImageView);
        cropImageView.setImageBitmap(MainActivity.getCapturedImage());

        Button confirmButton = findViewById(R.id.button_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bitmap bitmap = MainActivity.getCapturedImage();
                Bitmap bitmap = cropImageView.getCroppedImage();
                Tesseract.setUpTesseract(BoxActivity.this);
                String text = Tesseract.convertImage(bitmap);
                //System.out.println(text);
                Intent intent = new Intent(BoxActivity.this, TextActivity.class);
                intent.putExtra("text", text);
                startActivity(intent);
            }
        });

        Button backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(BoxActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });

    }
}

