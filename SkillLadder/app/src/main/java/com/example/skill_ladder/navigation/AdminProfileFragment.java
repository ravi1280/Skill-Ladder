package com.example.skill_ladder.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skill_ladder.LocationGetActivity;
import com.example.skill_ladder.R;
import com.example.skill_ladder.TestActivity;
import com.example.skill_ladder.model.Admin;
import com.example.skill_ladder.model.customAlert;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class AdminProfileFragment extends Fragment {
    private TextView AdminName;
    private TextView AdminNameED;
    private TextView AdminEmail;
    private TextView AdminEmailED;
    private TextView AdminPasswordED;

    private Button passwordChangeButton, updateButton;
    private FirebaseFirestore firestore;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_profile, container, false);
        AdminNameED = view.findViewById(R.id.AdminProfileDataText01);
        AdminEmailED = view.findViewById(R.id.AdminProfileDataText02);
        AdminPasswordED = view.findViewById(R.id.AdminProfileDataText04);
        AdminName = view.findViewById(R.id.AdminProfiletextView01);
        AdminEmail = view.findViewById(R.id.AdminProfiletexview02);





        loadAdminProfile();

        passwordChangeButton = view.findViewById(R.id.AdminProfileBtn02);
        passwordChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               UpdateBottomSheet();
            }});
        updateButton = view.findViewById(R.id.AdminProfileBtn01);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAdminprofile();
            }});

        return view;

    }



    private void loadAdminProfile() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("AdminUsername", ""); // Default is empty string

        if (username.isEmpty()) {
            Log.e("AdminProfile", "No username found in SharedPreferences");
            return;
        }
        Log.d("AdminProfile", "Fetching admin with email: " + username);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("admin")
                .whereEqualTo("email", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            if (result.isEmpty()) {
                                Log.d("AdminProfile", "No admin found for email: " + username);
                                return;
                            }
                            for (QueryDocumentSnapshot document : result) {
                                if (document.exists()) {
                                    Admin admin = document.toObject(Admin.class);
                                    AdminNameED.setText(admin.getName());
                                    AdminEmailED.setText(admin.getEmail());
                                    AdminPasswordED.setText(admin.getPassword());
                                    AdminEmail.setText(admin.getEmail());
                                    AdminName.setText(admin.getName());

                                    Log.d("AdminProfile", "Admin profile loaded successfully.");
                                }
                            }
                        } else {
                            Log.e("AdminProfile", "Error fetching admin data", task.getException());
                        }
                    }
                });
    }

    private void updateAdminprofile(){

        firestore = FirebaseFirestore.getInstance();
        firestore.collection("admin")
                .whereEqualTo("email", AdminEmailED.getText().toString().trim())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String documentId = document.getId();
                                Map<String, Object> updatedData = new HashMap<>();
                                updatedData.put("name", AdminNameED.getText().toString().trim());
                                firestore.collection("admin")
                                        .document(documentId)
                                        .update(updatedData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                customAlert.showCustomAlert(getContext(), "Success", "Admin Name Updated Successfully!", R.drawable.checked);
                                                AdminName.setText(AdminNameED.getText().toString().trim());
                                                AdminNameED.clearFocus();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                customAlert.showCustomAlert(getContext(), "Error", "Fail to Process !", R.drawable.cancel);
                                            }
                                        });
                            }
                        } else {
                            customAlert.showCustomAlert(getContext(), "Error", "Not Successfully Process !", R.drawable.cancel);
                        }
                    }
                });
    }

    private void UpdateBottomSheet() {


            View bottomSheetView = getLayoutInflater().inflate(R.layout.update_password_bottom, null);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
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
                    customAlert.showCustomAlert(getContext(), "Error", "Please Fill Old Password!", R.drawable.cancel);
                } else if (newPassword.isEmpty()) {
                    customAlert.showCustomAlert(getContext(), "Error", "Please Fill New Password!", R.drawable.cancel);
                } else if (reNewPassword.isEmpty()) {
                    customAlert.showCustomAlert(getContext(), "Error", "Please Fill Re-Type Password!", R.drawable.cancel);
                } else {
                    if (oldpassword.equals(AdminPasswordED.getText().toString().trim())){

                        if(newPassword.equals(reNewPassword)){
                            firestore = FirebaseFirestore.getInstance();
                            firestore.collection("admin")
                                    .whereEqualTo("email", AdminEmail.getText().toString().trim())
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
                                                    firestore.collection("admin")
                                                            .document(documentId)
                                                            .update(updatedData)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    customAlert.showCustomAlert(getContext(), "Success", "Password Updated Successfully!", R.drawable.checked);
                                                                    bottomSheetDialog.dismiss();
                                                                    AdminPasswordED.setText(newPassword);

                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    customAlert.showCustomAlert(getContext(), "Error", "Password Not Updated!", R.drawable.cancel);
                                                                }
                                                            });
                                                }
                                            } else {
                                                customAlert.showCustomAlert(getContext(), "Error", "Old Password Not Matched!", R.drawable.cancel);
                                            }
                                        }
                                    });

                        }else {
                            customAlert.showCustomAlert( getContext(), "Error", "Retype Password Not Matched!", R.drawable.cancel);
                        }

                    }else {
                        customAlert.showCustomAlert(getContext(), "Error", "Old Password Not Matched!", R.drawable.cancel);
                    }


                }
            });
            bottomSheetDialog.show();

    }

}