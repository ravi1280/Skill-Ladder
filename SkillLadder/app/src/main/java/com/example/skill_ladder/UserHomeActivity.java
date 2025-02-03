package com.example.skill_ladder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView imageViewHome01 = findViewById(R.id.HomeUserSearchImageView);
        imageViewHome01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent01 = new Intent(UserHomeActivity.this,SearchActivity.class);
                startActivity(intent01);

            }
        });
        ImageView imageViewHome02 = findViewById(R.id.HomeUserimageView);
        imageViewHome02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent01 = new Intent(UserHomeActivity.this,UserProfileActivity.class);
                startActivity(intent01);

            }
        });
        ImageView imageViewHome03 = findViewById(R.id.HomeUserLessonImageView);
        imageViewHome03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent01 = new Intent(UserHomeActivity.this,MyLessonsActivity.class);
                startActivity(intent01);

            }
        });
        ImageView imageViewHome04 = findViewById(R.id.HomeUserJobImageView);
        imageViewHome04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent01 = new Intent(UserHomeActivity.this,JobViewActivity.class);
                startActivity(intent01);

            }
        });

    }
}