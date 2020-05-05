package com.example.imagetotext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class TextActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        final EditText editText = findViewById(R.id.editText);
        Intent intent = getIntent();

        String text = intent.getExtras().getString("text", "");
        editText.setText(text, TextView.BufferType.NORMAL);

        ImageButton copyButton = findViewById(R.id.button_copy);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cbManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Text",
                        editText.getText());
                cbManager.setPrimaryClip(clip);
                TextView copyConfirmation = findViewById(R.id.textView);
                copyConfirmation.setVisibility(View.VISIBLE);
            }
        });

        Button backButton = findViewById(R.id.button_back2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(TextActivity.this, BoxActivity.class);
                startActivity(intent1);
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView copyConfirmation = findViewById(R.id.textView);
                copyConfirmation.setVisibility(View.INVISIBLE);
            }
        });
    }
}
