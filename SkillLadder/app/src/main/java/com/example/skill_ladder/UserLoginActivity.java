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
import com.google.firebase.firestore.QuerySnapshot;


public class UserLoginActivity extends AppCompatActivity {

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

                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String UserID = sharedPreferences.getString("UserID", ""); // Default is empty string

                if (UserID.isEmpty()) {
                    customAlert.showCustomAlert(UserLoginActivity.this,"Error ","Please Sign Up First",R.drawable.cancel);
                    return;
                }

                EditText editText01 = findViewById(R.id.CompanyLogineditText01);
                EditText editText02 = findViewById(R.id.CompanyLogineditText02);

                String email = editText01.getText().toString();
                String password = editText02.getText().toString();
                if(email.isEmpty()){
                    customAlert.showCustomAlert(UserLoginActivity.this, "Error !", "Please Fill Email",R.drawable.cancel);
                } else if (password.isEmpty()) {
                    customAlert.showCustomAlert(UserLoginActivity.this, "Error !", "Please Fill Password",R.drawable.cancel);
                }else {
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    firestore.collection("user")
                            .whereEqualTo("email", email).whereEqualTo("password", password).get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (queryDocumentSnapshots.isEmpty()) {
                                        customAlert.showCustomAlert(UserLoginActivity.this,"Error ","Invalid UserName or Password",R.drawable.cancel);
                                    } else {
                                        Intent intent01 = new Intent(UserLoginActivity.this, UserHomeActivity.class);
                                        startActivity(intent01);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    customAlert.showCustomAlert(UserLoginActivity.this,"Error ","Fail to load Data ! ",R.drawable.cancel);
                                }
                            });


//                    customAlert.showCustomAlert(UserLoginActivity.this, "Success !", "Your operation was successful!",R.drawable.checked);

                }
            }
        });




    }
}