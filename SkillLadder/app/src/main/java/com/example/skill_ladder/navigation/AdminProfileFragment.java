package com.example.skill_ladder.navigation;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class AdminProfileFragment extends Fragment {
    private FirebaseFirestore db;
//    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private TextView AdminName;
    private TextView AdminNameED;
    private TextView AdminEmail;
    private TextView AdminEmailED;
    private TextView AdminPasswordED;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_profile, container, false);
        db = FirebaseFirestore.getInstance();
        AdminNameED = view.findViewById(R.id.AdminProfileDataText01);
        AdminEmailED = view.findViewById(R.id.AdminProfileDataText02);
        AdminPasswordED = view.findViewById(R.id.AdminProfileDataText04);
        AdminName = view.findViewById(R.id.AdminProfiletextView01);
        AdminEmail = view.findViewById(R.id.AdminProfiletexview02);

        loadAdminProfile();
        return view;

    }
    private void loadAdminProfile() {
        db.collection("admin").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document :task.getResult()){
                        if(document.exists()){
                            Admin admin = document.toObject(Admin.class);
                            AdminNameED.setText(String.valueOf(admin.getName()));
                            AdminEmailED.setText(String.valueOf(admin.getEmail()));
                            AdminPasswordED.setText(String.valueOf(admin.getPassword()));
                            AdminEmail.setText(String.valueOf(admin.getEmail()));
                            AdminName.setText(String.valueOf(admin.getName()));

                        }

                    }
                }


            }
        });
    }
}