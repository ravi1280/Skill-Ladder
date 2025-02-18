package com.example.skill_ladder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.skill_ladder.admin.AdminHomeActivity;
import com.example.skill_ladder.model.customAlert;
import com.example.skill_ladder.model.showCustomToast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import java.util.HashMap;
import java.util.concurrent.Executor;


public class AdminLoginActivity extends AppCompatActivity {
    Context context;
    TextView email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button button= findViewById(R.id.AdminLoginBtn01);

        email = findViewById(R.id.AdminLoginedText01);
        password = findViewById(R.id.AdminLoginedText02);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logInProcess();

                }

        });
    }
    private void logInProcess(){

        String email01 = email.getText().toString();
        String password01 = password.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(email01.isEmpty()){
            customAlert.showCustomAlert(AdminLoginActivity.this, "Email is required", "Please enter your email", R.drawable.cancel);
            email.setError("Email is required");
            email.requestFocus();
            return;

        }
        else if(password01.isEmpty()){
            customAlert.showCustomAlert(AdminLoginActivity.this, "Password is required", "Please enter your password", R.drawable.cancel);
            password.setError("Password is required");
            password.requestFocus();
            return;
        }else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("admin").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.get("email").equals(email01) && document.get("password").equals(password01)) {



                                checkBiometricSupport();


                            } else {
                                customAlert.showCustomAlert(AdminLoginActivity.this, "Invalid Credentials", "Please enter valid email and password", R.drawable.cancel);
                            }
                        }
                    }
                }
            });
        }
    }

    private void checkBiometricSupport() {
        androidx.biometric.BiometricManager biometricManager = androidx.biometric.BiometricManager.from(AdminLoginActivity.this);
        switch (biometricManager.canAuthenticate()) {
            case androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS:
                authenticateUser();
                break;
            case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                showCustomToast.showToast(AdminLoginActivity.this, "No fingerprint sensor detected!",R.drawable.cancel);
                break;
            case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                showCustomToast.showToast(AdminLoginActivity.this, "Fingerprint sensor is not available!",R.drawable.cancel);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                showCustomToast.showToast(AdminLoginActivity.this, "No fingerprint enrolled! Please add a fingerprint.",R.drawable.cancel);
                break;
        }
    }

    private void authenticateUser() {
        Executor executor = ContextCompat.getMainExecutor(this);
        androidx.biometric.BiometricPrompt biometricPrompt = new androidx.biometric.BiometricPrompt(this, executor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                showCustomToast.showToast(AdminLoginActivity.this, "Authentication error: " + errString,R.drawable.cancel);

            }

            @Override
            public void onAuthenticationSucceeded(androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                String email01 = email.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("AdminUsername", email01);
                editor.putBoolean("AdminIsLoggedIn", true);
                editor.putLong("AdminTimestamp", System.currentTimeMillis());
                editor.apply();

                showCustomToast.showToast(AdminLoginActivity.this, "Login Successful! ✅",R.drawable.checked);

                Intent intent = new Intent(AdminLoginActivity.this, AdminHomeActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                showCustomToast.showToast(AdminLoginActivity.this, "Authentication Failed! Try Again ! ✅",R.drawable.cancel);
            }
        });

        androidx.biometric.BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Admin Login")
                .setSubtitle("Use your fingerprint to login")
                .setNegativeButtonText("Cancel")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }
}