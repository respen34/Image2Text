package com.example.imagetotext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.graphics.Bitmap;
import android.widget.ImageView;


public class BoxActivity extends AppCompatActivity {
    public Button confirmButton;
    public Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);

        confirmButton = findViewById(R.id.button_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoxActivity.this, TextActivity.class);
                startActivity(intent);
            }
        });

        backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(BoxActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });

        Bitmap bitmap = MainActivity.getCapturedImage();
        ImageView imageView = findViewById(R.id.imageView);
        //Bitmap bmp = BitmapFactory.decodeByteArray(buffer, start, a);
        imageView.setImageBitmap(bitmap);
    }
}

