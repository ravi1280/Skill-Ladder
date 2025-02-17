package com.example.skill_ladder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.skill_ladder.model.customAlert;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class UserLoginActivity extends AppCompatActivity {
    String email,password;

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

        TextView textView= findViewById(R.id.CompanyLogInTV04);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLoginActivity.this,UserSignUpActivity.class);
                startActivity(intent);
            }
        });

        Button btn01 = findViewById(R.id.CompanySignUpBtn01);
        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editText01 = findViewById(R.id.CompanyLogineditText01);
                EditText editText02 = findViewById(R.id.CompanyLogineditText02);

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
}