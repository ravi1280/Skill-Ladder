package com.example.skill_ladder;

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

import com.example.skill_ladder.model.Company;
import com.example.skill_ladder.model.customAlert;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class CompanySignUpActivity extends AppCompatActivity {

    String name;
    String mobile;
    String email;
    String password;
    String documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_company_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button button01 = findViewById(R.id.CompanySignUpBtn01);
        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editText01 = findViewById(R.id.CompanySignUpeditText01);
                EditText editText02 = findViewById(R.id.CompanySignUpeditText02);
                EditText editText03 = findViewById(R.id.CompanySignUpeditText03);
                EditText editText04 = findViewById(R.id.CompanySignUpeditText04);

                name = editText01.getText().toString();
                mobile = editText02.getText().toString();
                email = editText03.getText().toString();
                password = editText04.getText().toString();

                if (name.isEmpty()) {
                    customAlert.showCustomAlert(CompanySignUpActivity.this, "Error ", "Please Fill The Company Name", R.drawable.cancel);
                } else if (mobile.isEmpty()) {
                    customAlert.showCustomAlert(CompanySignUpActivity.this, "Error ", "Please Fill The Company Mobile", R.drawable.cancel);

                } else if (email.isEmpty()) {
                    customAlert.showCustomAlert(CompanySignUpActivity.this, "Error ", "Please Fill The Company Email", R.drawable.cancel);

                } else if (password.isEmpty()) {
                    customAlert.showCustomAlert(CompanySignUpActivity.this, "Error ", "Please Fill The Company Password", R.drawable.cancel);

                } else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("company")
                            .whereEqualTo("email", email)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        customAlert.showCustomAlert(CompanySignUpActivity.this, "Error ", "Email already exists!", R.drawable.cancel);
                                    } else {
                                        Map<String, Object> CompanyDetails01 = new HashMap<>();
                                        CompanyDetails01.put("name", name);
                                        CompanyDetails01.put("mobile", mobile);
                                        CompanyDetails01.put("email",email);
                                        CompanyDetails01.put("password", password);
                                        CompanyDetails01.put("isActive", true);


                                        db.collection("company").add(CompanyDetails01)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {

                                                        Intent intent = new Intent(CompanySignUpActivity.this, CompanyLogInActivity.class);
                                                        startActivity(intent);

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        customAlert.showCustomAlert(CompanySignUpActivity.this, "Error ", "Error in Saving Data", R.drawable.cancel);

                                                    }
                                                });
                                    }
                                }
                            });
                }

            }
        });

        TextView textView01 = findViewById(R.id.CompanySignUpTV004);
        textView01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent01 = new Intent(CompanySignUpActivity.this, CompanyLogInActivity.class);
                startActivity(intent01);
            }
        });
    }


}