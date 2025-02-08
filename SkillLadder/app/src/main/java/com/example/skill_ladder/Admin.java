package com.example.skill_ladder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import com.example.skill_ladder.admin.AdminHomeActivity;

public class Admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView imageView = findViewById(R.id.adminSplashImage01);
        SpringAnimation springAnimation = new SpringAnimation(imageView, DynamicAnimation.TRANSLATION_Y);

        SpringForce springForce = new SpringForce();
        springForce.setStiffness(SpringForce.STIFFNESS_LOW);
        springForce.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springForce.setFinalPosition(600f);

        springAnimation.setSpring(springForce);
        springAnimation.start();

        new Handler().postDelayed(()->{
//            Intent intent = new Intent(Admin.this, AdminLoginActivity.class);
//            startActivity(intent);
//            finish();
            checkAndDeleteSharedPreferences();
        },2000);
    }

    private void checkAndDeleteSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
        long savedTime = sharedPreferences.getLong("timestamp", 0);
        long currentTime = System.currentTimeMillis();

        if ((currentTime - savedTime) > 60000) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Log.d("SharedPreferences", "Data expired. SharedPreferences cleared.");
            Intent intent = new Intent(Admin.this, AdminLoginActivity.class);
            startActivity(intent);
            finish();

        } else {
            Intent intent = new Intent(Admin.this, AdminHomeActivity.class);
            startActivity(intent);
            finish();
            Log.d("SharedPreferences", "Data is still valid.");
        }
    }

}