package com.example.skill_ladder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LessonContentActivity extends AppCompatActivity {
    String  subTopic, contentText, webUrl, ytVideoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lesson_content);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FloatingActionButton fab = findViewById(R.id.LessonContendFAB);
        fab.setOnClickListener(view -> {
            finish();
        });

        TextView textView01 = findViewById(R.id.LessonContentTV01);

        TextView textView02 = findViewById(R.id.LessonDetailTV01);
        TextView textView03 = findViewById(R.id.LessonDetailTV02);
        TextView textView04 = findViewById(R.id.LessonDetailTV03);
        TextView textView05 = findViewById(R.id.LessonDetailTV04);

        Intent i = getIntent();
        subTopic =i.getStringExtra("mainTopic");
        contentText =i.getStringExtra("ContentText");
        webUrl =i.getStringExtra("WebUrl");
        ytVideoUrl =i.getStringExtra("YtVideoUrl");

        textView01.setText(subTopic);
        textView02.setText(subTopic);
        textView03.setText(contentText);
        textView04.setText(webUrl);
        textView05.setText(ytVideoUrl);
    }
}