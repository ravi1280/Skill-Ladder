package com.example.skill_ladder;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.skill_ladder.model.AppConfig;
import com.example.skill_ladder.model.Cart;
import com.example.skill_ladder.model.SQLiteHelper;
import com.example.skill_ladder.model.showCustomToast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PaymentSuccessActivity extends AppCompatActivity {
    String lessonId,userId;
    ArrayList<String> lessonIds;
    SQLiteHelper sqLiteHelper;

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
        userId = intent.getStringExtra("UserId");
        lessonIds = getIntent().getStringArrayListExtra("lessonIds");


         sqLiteHelper = new SQLiteHelper(
                PaymentSuccessActivity.this,
                "lessonProgress.db",
                null,
                1
        );

        Button button01= findViewById(R.id.PaymentSuccessBtn01);
        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userId != null) {
                    cartCheckOut();
                } else {
                    buySingleLessonProcess();
                }
            }
        });
    }
    private void buySingleLessonProcess(){
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
                                Toast.makeText(PaymentSuccessActivity.this, "Add to My lessons", Toast.LENGTH_SHORT).show();
                            }
                        });
                        sqLiteDatabase.close();
                    }
                }).start();

        Intent intent = new Intent(PaymentSuccessActivity.this, MyLessonsActivity.class);
        startActivity(intent);
        finish();
    }
    private  void cartCheckOut(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    Gson gson = new Gson();
                    JsonObject cartJson = new JsonObject();
                    cartJson.addProperty("userId",userId);

                    RequestBody jsonRequestBody = RequestBody.create(gson.toJson(cartJson), MediaType.get("application/json"));

                    Request request = new Request.Builder()
                            .url(AppConfig.BASE_URL+"/CheckOutCart")
                            .post(jsonRequestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseText = response.body().string();

                    if (responseText.equals("successfully!")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                savedateTomylesson();

                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showCustomToast.showToast(PaymentSuccessActivity.this, responseText, R.drawable.cancel);
                            }
                        });
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
    private void savedateTomylesson(){
        if (lessonIds != null) {
            for (String id : lessonIds) {
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("lesson_id", id);
                                contentValues.put("lesson_progress", 0);
                                long insertId = sqLiteDatabase.insert("MyLessonProgress", null, contentValues);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showCustomToast.showToast(PaymentSuccessActivity.this, "Successfully !", R.drawable.cancel);
                                    }
                                });
                                sqLiteDatabase.close();
                            }
                        }).start();

            }
            Intent intent = new Intent(PaymentSuccessActivity.this, MyLessonsActivity.class);
            startActivity(intent);
            finish();
        }else {
            showCustomToast.showToast(PaymentSuccessActivity.this, "No Lesson Ids", R.drawable.cancel);

        }

    }
}