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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.skill_ladder.admin.AdminHomeActivity;
import com.example.skill_ladder.model.customAlert;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class AdminLoginActivity extends AppCompatActivity {
    Context context;

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

        TextView email = findViewById(R.id.AdminLoginedText01);
        TextView password = findViewById(R.id.AdminLoginedText02);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email01 = email.getText().toString();
                String password01 = password.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if(email01.isEmpty()){
                    customAlert.showCustomAlert(AdminLoginActivity.this, "Email is required", "Please enter your email", R.drawable.cancel);
                    email.setError("Email is required");
                    email.requestFocus();
                    return;

                }
//                else if(email01.matches(emailPattern)){
//                    customAlert.showCustomAlert(AdminLoginActivity.this, "Invalid Email", "Please enter valid email", R.drawable.cancel);
//                    email.setError("Invalid Email");
//                    email.requestFocus();
//                    return;
//                }
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


                                        // Get SharedPreferences instance
                                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        editor.putString("username", email01);
                                        editor.putBoolean("isLoggedIn", true);
                                        editor.apply();

                                        String username = sharedPreferences.getString("username", "DefaultUser");
                                        Log.d("username", username);

                                        Intent intent = new Intent(AdminLoginActivity.this, AdminHomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        customAlert.showCustomAlert(AdminLoginActivity.this, "Invalid Credentials", "Please enter valid email and password", R.drawable.cancel);
                                    }
                                }
                            }
                        }
                    });
                }

                }

        });
    }
}