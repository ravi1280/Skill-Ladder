package com.example.skill_ladder;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.skill_ladder.model.SQLiteHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;


public class LessonContentActivity extends AppCompatActivity {
    String  subTopic, contentText, webUrl, ytVideoUrl,lessonId;
    int progressPercentage;

//    private String ytAPIKey = "AIzaSyCGEAN5HBQyUpH_ijZoInsvqDupSEGtmMc";

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

        YouTubePlayerView youTubePlayerView = findViewById(R.id.lessonYoutube_player_view);
        getLifecycle().addObserver(youTubePlayerView);


        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "1KpaSQTq52w";
                youTubePlayer.cueVideo(videoId, 0);
            }
        });

        FloatingActionButton fab = findViewById(R.id.LessonContendFAB);

        TextView textView01 = findViewById(R.id.LessonContentTV01);

        TextView textView02 = findViewById(R.id.LessonDetailTV01);
        TextView textView03 = findViewById(R.id.LessonDetailTV02);
        TextView textView04 = findViewById(R.id.LessonDetailTV03);

        Intent i = getIntent();
        subTopic =i.getStringExtra("mainTopic");
        contentText =i.getStringExtra("ContentText");
        webUrl =i.getStringExtra("WebUrl");
        ytVideoUrl =i.getStringExtra("YtVideoUrl");
        lessonId =i.getStringExtra("lessonId");
        boolean isLastSubtopic = i.getBooleanExtra("is_last_subtopic", false);
        progressPercentage = getIntent().getIntExtra("subtopic_progress", 0);

        textView01.setText(subTopic);
        textView02.setText(subTopic);
        textView03.setText(contentText);
        textView04.setText(webUrl);

        textView04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LessonContentActivity.this, WebViewActivity.class);
                intent.putExtra("WebUrl", "https://www.svgrepo.com/");
                startActivity(intent);
            }
        });


            fab.setOnClickListener(view -> {

                if(isLastSubtopic){
                    updateLessonProgress(lessonId, 100);

                    Intent intent = new Intent(LessonContentActivity.this, LessonSuccessActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    updateLessonProgress(lessonId, progressPercentage);
                    finish();
            }
            });
    }
    private void updateLessonProgress(String lessonId01, int progress) {

        SQLiteHelper sqLiteHelper = new SQLiteHelper(
                LessonContentActivity.this,
                "lessonProgress.db",
                null,
                1
        );

        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase sqLiteDatabase =sqLiteHelper.getWritableDatabase();
                ContentValues contentValues= new ContentValues();
                contentValues.put("lesson_progress",progress);
                if(lessonId01!=null){
                   int count = sqLiteDatabase.update(
                            "MyLessonProgress",
                            contentValues,
                            "lesson_id=?",
                            new String[]{lessonId01}
                    );
                    sqLiteDatabase.close();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LessonContentActivity.this, "Update Progress "+count, Toast.LENGTH_SHORT).show();
                        }
                    });

                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LessonContentActivity.this, "No Lesson Id To Update Lesson Progress", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}