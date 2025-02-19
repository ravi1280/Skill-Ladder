package com.example.skill_ladder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.skill_ladder.model.Admin;
import com.example.skill_ladder.model.Company;
import com.example.skill_ladder.model.customAlert;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CompanyUpdateProfileActivity extends AppCompatActivity {
    private  TextView CompanyNameView, CompanyEmailView;

    private EditText CompanyNameEdit, CompanyEmailEdit, CompanyMobileEdit, CompanyPasswordEdit;
    private FirebaseFirestore firestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_company_update_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CompanyNameView = findViewById(R.id.CompanyProfiletextView01);
        CompanyEmailView = findViewById(R.id.CompanyProfiletexview02);

        CompanyNameEdit = findViewById(R.id.CompanyProfileDataText01);
        CompanyEmailEdit = findViewById(R.id.CompanyProfileDataText02);
        CompanyMobileEdit = findViewById(R.id.CompanyProfileDataText03);
        CompanyPasswordEdit = findViewById(R.id.CompanyProfileDataText04);


        fillProfileDetails();

        ImageView imageView01 = findViewById(R.id.CompanyProfileBackIcon);
        imageView01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageView logout =findViewById(R.id.CompanyLogOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                companyLogout();
            }
        });

        Button updateButton = findViewById(R.id.CompanyProfileBtn01);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileUpdateDetails();
            }
        });

        Button buttonlocation = findViewById(R.id.CompanyLocationBtn01);
        buttonlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyUpdateProfileActivity.this, LocationGetActivity.class);
                startActivity(intent);
            }
        });

        Button showBottomSheetButton = findViewById(R.id.CompanyProfileBtn02);

        showBottomSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });
    }

    private void fillProfileDetails (){
        SharedPreferences sharedPreferences = getSharedPreferences("CompanyPrefs", MODE_PRIVATE);
        String companyEmail = sharedPreferences.getString("companyEmail", "");

        if (companyEmail.isEmpty()) {
            Log.e("companyEmail", "No companyEmail found in SharedPreferences");
            return;
        }
        Log.d("companyEmail", "companyEmail " + companyEmail);

        firestore = FirebaseFirestore.getInstance();
        firestore.collection("company")
                .whereEqualTo("email", companyEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Log.d("CompanyProfile", "No company found for email: " + companyEmail);
                        return;
                    }
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        if (document.exists()) {
                            CompanyNameView.setText(document.getString("name"));
                            CompanyEmailView.setText(document.getString("email"));
                            CompanyNameEdit.setText(document.getString("name"));
                            CompanyEmailEdit.setText(document.getString("email"));
                            CompanyMobileEdit.setText( document.getString("mobile"));
                            CompanyPasswordEdit.setText(document.getString("password"));
                            Log.d("CompanyProfile", "Company profile loaded successfully.");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("CompanyProfile", "Error fetching company data", e);
                    }
                });
    }
    private void profileUpdateDetails(){
        String name= CompanyNameEdit.getText().toString();
        String mobile= CompanyMobileEdit.getText().toString();
        if (name.isEmpty()){
            customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Name Field is Empty !", R.drawable.cancel);

        }else if(mobile.isEmpty()){
            customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Mobile Field is Empty!", R.drawable.cancel);

        }else {
            SharedPreferences sharedPreferences = getSharedPreferences("CompanyPrefs", MODE_PRIVATE);
            String companyEmail = sharedPreferences.getString("companyEmail", "");

            if (companyEmail.isEmpty()) {
                customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Data Lost from mobile !", R.drawable.cancel);

                return;
            }
            Log.d("companyEmail", "companyEmail " + companyEmail);

            firestore = FirebaseFirestore.getInstance();
            firestore.collection("company")
                    .whereEqualTo("email", companyEmail)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.isEmpty()) {
                                Log.d("Firestore", "No company found with email: " + companyEmail);
                                return;
                            }

                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                String documentId = document.getId();


                                Map<String, Object> updatedData = new HashMap<>();
                                updatedData.put("name", name);
                                updatedData.put("mobile", mobile);

                                firestore.collection("company")
                                        .document(documentId)
                                        .update(updatedData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                recreate();
                                                customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Success", "Successfully Update !", R.drawable.checked);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Error updating company profile!", R.drawable.cancel);

                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Firestore", "Error fetching company data", e);
                        }
                    });
        }

    }
    private void companyLogout(){
        View bottomSheetView = getLayoutInflater().inflate(R.layout.logout_bottom, null);
        BottomSheetDialog bottomSheetDialog02 = new BottomSheetDialog(CompanyUpdateProfileActivity.this);
        bottomSheetDialog02.setContentView(bottomSheetView);

        Button logout = bottomSheetView.findViewById(R.id.LogoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("CompanyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("companyEmail");
                editor.remove("companyID");
                editor.remove("companyIsLoggedIn");
                editor.apply();

                customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Success", "Logout Successfully!", R.drawable.checked);
                bottomSheetDialog02.dismiss();

                Intent intent = new Intent(CompanyUpdateProfileActivity.this,CompanyLogInActivity.class);
                startActivity(intent);
                finish();

            }
        });
        bottomSheetDialog02.show();

    }
    private void updatePassword(){

        View bottomSheetView = getLayoutInflater().inflate(R.layout.update_password_bottom, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(CompanyUpdateProfileActivity.this);
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
                customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Please Fill Old Password!", R.drawable.cancel);
            } else if (newPassword.isEmpty()) {
                customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Please Fill New Password!", R.drawable.cancel);
            } else if (reNewPassword.isEmpty()) {
                customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Please Fill Re-Type Password!", R.drawable.cancel);
            } else {
                if (oldpassword.equals(CompanyPasswordEdit.getText().toString().trim())){

                    if(newPassword.equals(reNewPassword)){
                        SharedPreferences sharedPreferences = getSharedPreferences("CompanyPrefs", MODE_PRIVATE);
                        String companyEmail = sharedPreferences.getString("companyEmail", "");
                        firestore = FirebaseFirestore.getInstance();
                        firestore.collection("company")
                                .whereEqualTo("email", companyEmail)
                                .whereEqualTo("password", oldpassword)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                String documentId = document.getId();
                                                Map<String, Object> updatedData = new HashMap<>();
                                                updatedData.put("password", newPassword);
                                                firestore.collection("company")
                                                        .document(documentId)
                                                        .update(updatedData)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                CompanyPasswordEdit.setText(newPassword);
                                                                customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Success", "Password Updated Successfully!", R.drawable.checked);
                                                                bottomSheetDialog.dismiss();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Password Not Updated!", R.drawable.cancel);
                                                            }
                                                        });
                                            }
                                        } else {
                                            customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Old Password Not Matched!", R.drawable.cancel);
                                        }
                                    }
                                });

                    }else {
                        customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Retype Password Not Matched!", R.drawable.cancel);
                    }

                }else {
                    customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Old Password Not Matched!", R.drawable.cancel);
                }

            }
        });
        bottomSheetDialog.show();

    }
}