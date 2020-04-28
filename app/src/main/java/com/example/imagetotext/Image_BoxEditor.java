package com.example.imagetotext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Image_BoxEditor extends AppCompatActivity {
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
                Intent intent = new Intent(Image_BoxEditor.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

