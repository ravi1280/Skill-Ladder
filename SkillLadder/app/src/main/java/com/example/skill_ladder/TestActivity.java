package com.example.skill_ladder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);


        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "1KpaSQTq52w";
                youTubePlayer.pause();
                youTubePlayer.loadVideo(videoId, 0);
//                youTubePlayer.cueVideo(videoId, 0);
            }

        });
        youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> youTubePlayer.pause());


    }

//new Thread(() -> {
//        try {
//            OkHttpClient client = new OkHttpClient();
//            Gson gson = new Gson();
//
//            // Encode category to safely include in URL
////                            String encodedCategory = URLEncoder.encode(category, "UTF-8");
//
//            // Construct URL with query parameters
//            String url = AppConfig.BASE_URL+"/AddCategory?name=" + category;
//
//            // Make request using GET method
//            Request request = new Request.Builder()
//                    .url(url)
//                    .get()
//                    .build();
//
//            Response response = client.newCall(request).execute();
//            String responseText = response.body().string();
//
//            if (responseText.equals("Category added successfully!")) {
//                runOnUiThread(() -> CustomAlert.showErrorDialog(AdminAddProductsActivity.this, "Success", "Category Added Successfully"));
//
//
//            } else {
//                runOnUiThread(() -> Toast.makeText(AdminAddProductsActivity.this, "Response: " + responseText, Toast.LENGTH_LONG).show());
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            runOnUiThread(() -> Toast.makeText(AdminAddProductsActivity.this, "Upload Failed!", Toast.LENGTH_SHORT).show());
//        }
//    }).start();


}
