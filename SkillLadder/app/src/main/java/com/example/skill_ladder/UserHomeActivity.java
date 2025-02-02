package com.example.skill_ladder;

import android.os.Bundle;
import android.widget.Button;
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
//        Button btnHome = findViewById(R.id.btn_home);
//        Button btnLessons = findViewById(R.id.btn_lessons);
//        Button btnProfile = findViewById(R.id.btn_profile);

//        btnHome.setOnClickListener(v -> {
//            Toast.makeText(UserHomeActivity.this, "Home Clicked", Toast.LENGTH_SHORT).show();
//        });
//
//        btnLessons.setOnClickListener(v -> {
//            Toast.makeText(UserHomeActivity.this, "Lessons Clicked", Toast.LENGTH_SHORT).show();
//        });
//
//        btnProfile.setOnClickListener(v -> {
//            Toast.makeText(UserHomeActivity.this, "Profile Clicked", Toast.LENGTH_SHORT).show();
//        });
    }
}