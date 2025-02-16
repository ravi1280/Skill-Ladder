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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UserSignUpActivity extends AppCompatActivity {

    String documentId,fullName,mobile,email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btn01 = findViewById(R.id.CompanySignUpBtn01);
        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText01 = findViewById(R.id.CompanyLogineditText01);
                EditText editText02 = findViewById(R.id.CompanyLogineditText02);
                EditText editText03 = findViewById(R.id.CompanySignUpeditText03);
                EditText editText04 = findViewById(R.id.CompanySignUpeditText04);

                fullName = editText01.getText().toString();
                mobile = editText02.getText().toString();
                email = editText03.getText().toString();
                password = editText04.getText().toString();

                if(fullName.isEmpty()){
                    customAlert.showCustomAlert(UserSignUpActivity.this, "Error !", "Please Fill Full Name",R.drawable.cancel);
                } else if (mobile.isEmpty()) {
                    customAlert.showCustomAlert(UserSignUpActivity.this, "Error !", "Please Fill Mobile",R.drawable.cancel);
                } else if (email.isEmpty()) {
                    customAlert.showCustomAlert(UserSignUpActivity.this, "Error !", "Please Fill Email",R.drawable.cancel);
                } else if (password.isEmpty()) {
                    customAlert.showCustomAlert(UserSignUpActivity.this, "Error !", "Please Fill Password",R.drawable.cancel);
                }else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("user")
                            .whereEqualTo("email", email)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        customAlert.showCustomAlert(UserSignUpActivity.this, "Error ", "Email already exists!", R.drawable.cancel);
                                    } else {
                                        Map<String, Object> UserDetails01 = new HashMap<>();
                                        UserDetails01.put("fullName", fullName);
                                        UserDetails01.put("mobile", mobile);
                                        UserDetails01.put("email",email);
                                        UserDetails01.put("password", password);
                                        UserDetails01.put("isActive", true);


                                        db.collection("user").add(UserDetails01)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        documentId = documentReference.getId();
                                                        setSharedPreferences();

                                                        Intent intent = new Intent(UserSignUpActivity.this, UserLoginActivity.class);
                                                        startActivity(intent);

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        customAlert.showCustomAlert(UserSignUpActivity.this, "Error ", "Error in Saving Data", R.drawable.cancel);

                                                    }
                                                });
                                    }
                                }
                            });

                }
            }
        });


        TextView textViewLogin= findViewById(R.id.UserLogInTV04);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSignUpActivity.this,UserLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setSharedPreferences() {

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("UserID", documentId);
        editor.putString("UserEmail", email);
        editor.putString("UserMobile", mobile);
        editor.putString("UserFullName", fullName);
        editor.apply();

    }
}