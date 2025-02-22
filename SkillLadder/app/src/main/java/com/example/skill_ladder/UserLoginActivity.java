package com.example.skill_ladder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.skill_ladder.model.AppConfig;
import com.example.skill_ladder.model.Cart;
import com.example.skill_ladder.model.CodeGenarete;
import com.example.skill_ladder.model.customAlert;
import com.example.skill_ladder.model.showCustomToast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class UserLoginActivity extends AppCompatActivity {
    String email,password;
    String code;
    Boolean userValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userValid = false;
        TextView textView1= findViewById(R.id.UserLogInTV05);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               forgetPassword();
            }
        });

        TextView textView= findViewById(R.id.UserLogInTV04);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLoginActivity.this,UserSignUpActivity.class);
                startActivity(intent);
            }
        });

        Button btn01 = findViewById(R.id.UserLoginBtn01);
        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editText01 = findViewById(R.id.UserLogineditText01);
                EditText editText02 = findViewById(R.id.UserLogineditText02);

                 email = editText01.getText().toString();
                 password = editText02.getText().toString();
                if(email.isEmpty()){
                    customAlert.showCustomAlert(UserLoginActivity.this, "Error !", "Please Fill Email",R.drawable.cancel);
                } else if (password.isEmpty()) {
                    customAlert.showCustomAlert(UserLoginActivity.this, "Error !", "Please Fill Password",R.drawable.cancel);
                }else {
                    searchFirebase();

                }
            }
        });

    }

    private  void searchFirebase(){

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("user")
                .whereEqualTo("email", email).whereEqualTo("password", password).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            customAlert.showCustomAlert(UserLoginActivity.this,"Error ","Invalid UserName or Password",R.drawable.cancel);
                        } else {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                                String documentId = document.getId();
                                String name = document.getString("fullName");
                                String email = document.getString("email");
                                String mobile = document.getString("mobile");
                                String password = document.getString("password");

                                SharedPreferences userSharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = userSharedPreferences.edit();

                                editor.putString("UserID", documentId);
                                editor.putString("UserFullName", name);
                                editor.putString("UserEmail", email);
                                editor.putString("UserMobile", mobile);
                                editor.putString("UserPassword", password);
                                editor.putBoolean("UserIsLoggedIn", true);
                                editor.apply();

                                Intent intent01 = new Intent(UserLoginActivity.this, UserHomeActivity.class);
                                startActivity(intent01);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        customAlert.showCustomAlert(UserLoginActivity.this,"Error ","Fail to load Data ! ",R.drawable.cancel);
                    }
                });
    }
    private void forgetPassword() {
        CheckValidUser();
        if(userValid){
        code = CodeGenarete.generateUniqueCode();
        EditText editText01 = findViewById(R.id.UserLogineditText01);
        String email = editText01.getText().toString();
        if (!email.isEmpty()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        OkHttpClient okHttpClient = new OkHttpClient();
                        String url = AppConfig.BASE_URL + "/ForgetPassword?code=" + code+"&email="+email;

                        Request request = new Request.Builder()
                                .url(url)
                                .get()
                                .build();

                        Response response = okHttpClient.newCall(request).execute();
                        String responseText = response.body().string();

                        if (responseText.equals("success")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    customAlert.showCustomAlert(UserLoginActivity.this,"Success ","Password Reset Link Sent to Your Email",R.drawable.checked);
                                OpenBottomSheet();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    customAlert.showCustomAlert(UserLoginActivity.this,"Error ","Fail to Send Email",R.drawable.cancel);
                                }
                            });
                        }

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
            }).start();

        }else {
            customAlert.showCustomAlert(UserLoginActivity.this,"Error ","Please Fill Email",R.drawable.cancel);

        }}

    }
    private void CheckValidUser() {
        EditText editText01 = findViewById(R.id.UserLogineditText01);
        String email = editText01.getText().toString();
        if (email.isEmpty()) {
            customAlert.showCustomAlert(UserLoginActivity.this, "Error", "Please Fill Email", R.drawable.cancel);
        }else {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("user")
                    .whereEqualTo("email", email).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.isEmpty()) {
                                customAlert.showCustomAlert(UserLoginActivity.this, "Error", "Invalid Email", R.drawable.cancel);
                            }else {
                                userValid = true;
                            }
                        }
                    });
        }
    }
    private void OpenBottomSheet() {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.forget_password_bottom, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(UserLoginActivity.this);
        bottomSheetDialog.setContentView(bottomSheetView);

        Button action = bottomSheetView.findViewById(R.id.forgetPasswordBtn);
        EditText text01 = bottomSheetView.findViewById(R.id.forgetPasswordED01);
        EditText text02 = bottomSheetView.findViewById(R.id.forgetPasswordED02);
        EditText text03 = bottomSheetView.findViewById(R.id.forgetPasswordED03);

        action.setOnClickListener(view -> {
            String code01 = text01.getText().toString().trim();
            String newPassword = text02.getText().toString().trim();
            String reNewPassword = text03.getText().toString().trim();

            if (code01.isEmpty()) {
                customAlert.showCustomAlert(UserLoginActivity.this, "Error", "Please Verification Code!", R.drawable.cancel);
            } else if (newPassword.isEmpty()) {
                customAlert.showCustomAlert(UserLoginActivity.this, "Error", "Please Fill New Password!", R.drawable.cancel);
            } else if (reNewPassword.isEmpty()) {
                customAlert.showCustomAlert(UserLoginActivity.this, "Error", "Please Fill Re-Type Password!", R.drawable.cancel);
            } else if (!reNewPassword.equals(newPassword)) {
                customAlert.showCustomAlert(UserLoginActivity.this, "Error", "Re-Type Password Doesn't Match !", R.drawable.cancel);
            } else if (!code.equals(code01)) {
                customAlert.showCustomAlert(UserLoginActivity.this, "Error", "Verification Code Doesn't Match !", R.drawable.cancel);
            }else {
                EditText editText01 = findViewById(R.id.UserLogineditText01);
                String email001 = editText01.getText().toString();

                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                firestore.collection("user")
                        .whereEqualTo("email", email001)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    String documentId = document.getId();
                                    firestore.collection("user")
                                            .document(documentId)
                                            .update("password", newPassword)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    customAlert.showCustomAlert(UserLoginActivity.this, "Success", "Password Reset Successfully", R.drawable.checked);
                                                    bottomSheetDialog.dismiss();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    customAlert.showCustomAlert(UserLoginActivity.this, "Error", "Fail to Reset Password !", R.drawable.cancel);
                                                }
                                            });
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                customAlert.showCustomAlert(UserLoginActivity.this, "Error", "Cannot Search User !", R.drawable.cancel);
                            }
                        });

                //snackBar
//                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }
}