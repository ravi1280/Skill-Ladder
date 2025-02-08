package com.example.skill_ladder.navigation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.skill_ladder.R;
import com.example.skill_ladder.model.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;


public class AdminProfileFragment extends Fragment {
    private TextView AdminName;
    private TextView AdminNameED;
    private TextView AdminEmail;
    private TextView AdminEmailED;
    private TextView AdminPasswordED;


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
        return view;

    }

    private void loadAdminProfile() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", ""); // Default is empty string

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
    
}