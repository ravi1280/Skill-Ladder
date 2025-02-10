package com.example.skill_ladder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import com.example.skill_ladder.model.customAlert;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.Map;

public class CompanyLogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_company_log_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Button button01 = findViewById(R.id.CompanyLOGINBtn01);
        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String companyEmail = sharedPreferences.getString("companyEmail", ""); // Default is empty string

                if (companyEmail.isEmpty()) {
                    Log.e("companyEmail", "No username found in SharedPreferences");
                    customAlert.showCustomAlert(CompanyLogInActivity.this,"Error ","Please Sign Up First",R.drawable.cancel);
                    return;
                }
                Log.d("companyEmail", "companyEmail " + companyEmail);


                EditText editText01 =findViewById(R.id.CompanyLogineditText01);
                EditText editText02 =findViewById(R.id.CompanyLogineditText02);

                String email = editText01.getText().toString();
                String password = editText02.getText().toString();

                if(email.isEmpty()){
                    customAlert.showCustomAlert(CompanyLogInActivity.this,"Error ","Please Fill The Company UserName",R.drawable.cancel);
                } else if (password.isEmpty()) {
                    customAlert.showCustomAlert(CompanyLogInActivity.this,"Error ","Please Fill The Company Password",R.drawable.cancel);

                }else {
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    firestore.collection("company")
                            .whereEqualTo("email", email).whereEqualTo("password", password).get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (queryDocumentSnapshots.isEmpty()) {
                                        customAlert.showCustomAlert(CompanyLogInActivity.this,"Error ","Invalid UserName or Password",R.drawable.cancel);
                                    } else {
                                        customAlert.showCustomAlert(CompanyLogInActivity.this,"Success ","Login Success",R.drawable.checked);
                                        Intent intent01 = new Intent(CompanyLogInActivity.this, JobCompanyHomeActivity.class);
                                        startActivity(intent01);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    customAlert.showCustomAlert(CompanyLogInActivity.this,"Error ","Invalid UserName or Password",R.drawable.cancel);
                                }
                            });

                }

            }
        });

        TextView textView01 = findViewById(R.id.CompanyLogInTV04);
        textView01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent01 = new Intent(CompanyLogInActivity.this,CompanySignUpActivity.class);
                startActivity(intent01);
            }
        });


    }
}