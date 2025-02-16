package com.example.skill_ladder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.skill_ladder.model.customAlert;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class UserProfileActivity extends AppCompatActivity {

    String fullName,email,mobile,password;

    EditText text01,text02,text03,text04;
    TextView tv01,tv02;

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


        tv01 = findViewById(R.id.UserProfiletextView01);
        tv02 = findViewById(R.id.UserProfiletexview02);

        text01 = findViewById(R.id.UserProfileDataText01);
        text02 = findViewById(R.id.UserProfileDataText02);
        text03 = findViewById(R.id.UserProfileDataText03);
        text04 = findViewById(R.id.UserProfileDataText04);

        setData();

        ImageView imageView01 = findViewById(R.id.UserProfileBackIcon);
        imageView01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

        ImageView logoutImage= findViewById(R.id.UsreLogout);
        logoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });

        Button btn01 = findViewById(R.id.UserProfileBtn01);
        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserProfile();
            }
        });

        Button showBottomSheetButton = findViewById(R.id.UserProfileBtn02);
        showBottomSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetProcess();
            }
        });

    }
    private void updateUserProfile() {
        String fullName = text01.getText().toString().trim();
//        String email = text02.getText().toString().trim();
        String mobile = text03.getText().toString().trim();
//        String password = text04.getText().toString().trim();

        if (fullName.isEmpty()) {
            customAlert.showCustomAlert(this, "Error", "Please Fill The Full Name!", R.drawable.cancel);
        } else if (mobile.isEmpty()) {
            customAlert.showCustomAlert(this, "Error", "Please Fill The Mobile!", R.drawable.cancel);
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("UserFullName", fullName);
            editor.putString("UserMobile", mobile);
            editor.apply();

            text01.setText(fullName);
            tv01.setText(fullName);
            text03.setText(mobile);


            customAlert.showCustomAlert(this, "Success", "Profile Updated Successfully!", R.drawable.checked);
        }
    }
    private void bottomSheetProcess(){
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
            } else if (!reNewPassword.equals(newPassword)) {
                customAlert.showCustomAlert(UserProfileActivity.this, "Error", "Re-Type Password Doesn't Match !", R.drawable.cancel);
            } else if (!oldpassword.equals(text04.getText().toString())) {
                customAlert.showCustomAlert(UserProfileActivity.this, "Error", "Old Password Doesn't Match !", R.drawable.cancel);
            }else {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("UserPassword", newPassword);
                editor.apply();

                text04.setText(newPassword);

                customAlert.showCustomAlert(UserProfileActivity.this, "Success", "Password Updated Successfully!", R.drawable.checked);
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();

    }
    private void setData(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String UserName = sharedPreferences.getString("UserFullName", "");
        String UserEmail = sharedPreferences.getString("UserEmail", "");
        String UserMobile = sharedPreferences.getString("UserMobile", "");
        String UserPassword = sharedPreferences.getString("UserPassword", "password");

        if(UserName.isEmpty()){
            customAlert.showCustomAlert(UserProfileActivity.this, "Error", "No Data to Set", R.drawable.cancel);
            finish();
        }else {

        tv01.setText(UserName);
        tv02.setText(UserEmail);

        text01.setText(UserName);
        text02.setText(UserEmail);
        text03.setText(UserMobile);
        text04.setText(UserPassword);
        }

    }

    private void Logout(){
        View bottomSheetView = getLayoutInflater().inflate(R.layout.logout_bottom, null);
        BottomSheetDialog bottomSheetDialog02 = new BottomSheetDialog(UserProfileActivity.this);
        bottomSheetDialog02.setContentView(bottomSheetView);

        Button logout = bottomSheetView.findViewById(R.id.LogoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("UserFullName");
                editor.remove("UserEmail");
                editor.remove("UserMobile");
                editor.remove("UserPassword");
                editor.remove("UserID");
                editor.apply();

                customAlert.showCustomAlert(UserProfileActivity.this, "Success", "Logout Successfully!", R.drawable.checked);
                bottomSheetDialog02.dismiss();

                Intent intent = new Intent(UserProfileActivity.this,UserLoginActivity.class);
                startActivity(intent);

            }
        });
        bottomSheetDialog02.show();


    }
}