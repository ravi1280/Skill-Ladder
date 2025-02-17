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

//    private void addProduct(int category, int brand, String title, String qty, String price, String description) {
//        new Thread(() -> {
//            try {
//                OkHttpClient client = new OkHttpClient();
//                Gson gson = new Gson();
//
//                Log.i("EStoreTest2", "Category ID : " + category);
//                // Convert JSON data
//                JsonObject productJson = new JsonObject();
//                productJson.addProperty("category", category);
//                productJson.addProperty("brand", brand);
//                productJson.addProperty("title", title);
//                productJson.addProperty("quantity", qty);
//                productJson.addProperty("price", price);
//                productJson.addProperty("description", description);
//
//                RequestBody jsonRequestBody = RequestBody.create(gson.toJson(productJson), MediaType.get("application/json"));
//
//                // Convert images to files
//                File file1 = uriToFile(imageUri1);
//                File file2 = uriToFile(imageUri2);
//                File file3 = uriToFile(imageUri3);
//
//                // Build MultipartBody
//                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//                builder.addFormDataPart("productData", gson.toJson(productJson));
//
//                if (file1 != null) {
//                    builder.addFormDataPart("image1", file1.getName(), RequestBody.create(file1, MediaType.parse("image/*")));
//                }
//                if (file2 != null) {
//                    builder.addFormDataPart("image2", file2.getName(), RequestBody.create(file2, MediaType.parse("image/*")));
//                }
//                if (file3 != null) {
//                    builder.addFormDataPart("image3", file3.getName(), RequestBody.create(file3, MediaType.parse("image/*")));
//                }
//
//                RequestBody requestBody = builder.build();
//
//                // Make request
//                Request request = new Request.Builder()
//                        .url(AppConfig.BASE_URL+"/AddProduct")
//                        .post(requestBody)
//                        .build();
//
//                Response response = client.newCall(request).execute();
//                String responseText = response.body().string();
//                if (responseText.equals("Product added successfully with images!")){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            CustomAlert.showErrorDialog(AdminAddProductsActivity.this, "Success", "Product Added Successfully");
//                            clearField();
//                        }
//                    });
//                }else{
//                    runOnUiThread(() -> Toast.makeText(AdminAddProductsActivity.this, "Response: " + responseText, Toast.LENGTH_LONG).show());
//                }
//
//
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                runOnUiThread(() -> Toast.makeText(AdminAddProductsActivity.this, "Upload Failed!", Toast.LENGTH_SHORT).show());
//            }
//        }).start();
//    }


}
