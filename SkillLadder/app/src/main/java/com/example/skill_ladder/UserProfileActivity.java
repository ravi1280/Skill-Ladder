package com.example.skill_ladder;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.skill_ladder.model.customAlert;
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
        ImageView imageView01 = findViewById(R.id.UserProfileBackIcon);
        imageView01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Button btn01 = findViewById(R.id.UserProfileBtn01);
        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText text01= findViewById(R.id.UserProfileDataText01);
                EditText text02= findViewById(R.id.UserProfileDataText02);
                EditText text03= findViewById(R.id.UserProfileDataText03);
                EditText text04= findViewById(R.id.UserProfileDataText04);

                String fullName= text01.getText().toString();
                String email= text02.getText().toString();
                String mobile= text03.getText().toString();
                String password= text04.getText().toString();

                if(fullName.isEmpty()){
                    customAlert.showCustomAlert(UserProfileActivity.this,"Error ","Please Fill The Full Name !",R.drawable.cancel);

                }else if (mobile.isEmpty()) {
                    customAlert.showCustomAlert(UserProfileActivity.this,"Error ","Please Fill The Mobile !",R.drawable.cancel);

                }else {
                    customAlert.showCustomAlert(UserProfileActivity.this,"Success ","Success !",R.drawable.checked);

                }
            }
        });


        Button showBottomSheetButton = findViewById(R.id.UserProfileBtn02);
        showBottomSheetButton.setOnClickListener(v -> {

            View bottomSheetView = getLayoutInflater().inflate(R.layout.update_password_bottom, null);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(UserProfileActivity.this);
            bottomSheetDialog.setContentView(bottomSheetView);

            Button actionOne = bottomSheetView.findViewById(R.id.UserUpdatePasswordBtn);
            EditText text01 = bottomSheetView.findViewById(R.id.UserOldPassword);
            EditText text02 = bottomSheetView.findViewById(R.id.UserNewPassword);
            EditText text03 = bottomSheetView.findViewById(R.id.UserReTypePassword);

            actionOne.setOnClickListener(view -> {
                String oldpassword = text01.getText().toString().trim();
                String newPassword = text02.getText().toString().trim();
                String reNewPassword = text03.getText().toString().trim();

                if (oldpassword.isEmpty()) {
                    customAlert.showCustomAlert(UserProfileActivity.this, "Error", "Please Fill Old Password!", R.drawable.cancel);
                } else if (newPassword.isEmpty()) {
                    customAlert.showCustomAlert(UserProfileActivity.this, "Error", "Please Fill New Password!", R.drawable.cancel);
                } else if (reNewPassword.isEmpty()) {
                    customAlert.showCustomAlert(UserProfileActivity.this, "Error", "Please Fill Re-Type Password!", R.drawable.cancel);
                } else {
                    customAlert.showCustomAlert(UserProfileActivity.this, "Success", "Password Updated Successfully!", R.drawable.checked);
                    bottomSheetDialog.dismiss();
                }
            });

            bottomSheetDialog.show();
        });

    }
}