package com.example.skill_ladder;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btn01 = findViewById(R.id.UserProfileBtn01);



        Button showBottomSheetButton = findViewById(R.id.UserProfileBtn02);

        showBottomSheetButton.setOnClickListener(v -> {

            View bottomSheetView = getLayoutInflater().inflate(R.layout.update_password_bottom, null);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(UserProfileActivity.this);
            bottomSheetDialog.setContentView(bottomSheetView);
            Button actionOne = bottomSheetView.findViewById(R.id.UserUpdatePasswordBtn);

            actionOne.setOnClickListener(view -> {
                Toast.makeText(UserProfileActivity.this, "Action One Clicked", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });
            
            bottomSheetDialog.show();
        });
    }
}