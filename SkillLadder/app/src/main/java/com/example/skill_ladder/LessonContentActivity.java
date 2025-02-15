package com.example.skill_ladder;

import android.content.Intent;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;


public class LessonContentActivity extends AppCompatActivity {
    String  subTopic, contentText, webUrl, ytVideoUrl;
    private String ytAPIKey = "AIzaSyCGEAN5HBQyUpH_ijZoInsvqDupSEGtmMc";

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
        boolean isLastSubtopic = i.getBooleanExtra("is_last_subtopic", false);

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

        if(isLastSubtopic){
            Toast.makeText(this, "Last Subtopic", Toast.LENGTH_SHORT).show();
            fab.setOnClickListener(view -> {
                Intent intent = new Intent(LessonContentActivity.this, LessonSuccessActivity.class);
                startActivity(intent);
                finish();
            });
        }else {
            fab.setOnClickListener(view -> {
                finish();
            });
        }

    }
}