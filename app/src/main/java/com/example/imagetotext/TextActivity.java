package com.example.imagetotext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TextActivity extends AppCompatActivity {
    public Button copyButton;
    public Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        copyButton = findViewById(R.id.button_copy);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getIntent as text from previous
            }
        });
        backButton = findViewById(R.id.button_back2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(TextActivity.this, BoxActivity.class);
                startActivity(intent1);
            }
        });
    }
}
