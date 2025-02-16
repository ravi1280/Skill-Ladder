package com.example.skill_ladder;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.skill_ladder.model.SQLiteHelper;

public class PaymentSuccessActivity extends AppCompatActivity {
    String lessonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_success);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        lessonId = intent.getStringExtra("lessonId");

        Button button01= findViewById(R.id.PaymentSuccessBtn01);
        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteHelper sqLiteHelper = new SQLiteHelper(
                        PaymentSuccessActivity.this,
                        "lessonProgress.db",
                        null,
                        1

                );
                new Thread(
                        new Runnable() {
                    @Override
                    public void run() {
                        SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("lesson_id", lessonId);
                        contentValues.put("lesson_progress", 0);
                        long insertId = sqLiteDatabase.insert("MyLessonProgress", null, contentValues);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PaymentSuccessActivity.this, "my" + String.valueOf(insertId), Toast.LENGTH_SHORT).show();

                            }
                        });

                        sqLiteDatabase.close();

                    }
                }).start();

                Intent intent = new Intent(PaymentSuccessActivity.this,MyLessonsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}