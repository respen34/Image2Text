package com.example.imagetotext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public abstract class text_Copy extends AppCompatActivity {
    public Button copyObj;
    public Button backObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text__copy);
        copyObj = (Button) findViewById(R.id.confirmButton);
        copyObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getIntent as text from previous
            }
        });
        backObj = (Button) findViewById(R.id.changePhotoButton);
        backObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(text_Copy.this, Image_BoxEditor.class);
                startActivity(intent1);
            }
        });
    }
}
