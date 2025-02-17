package com.example.skill_ladder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.skill_ladder.model.customAlert;
import com.example.skill_ladder.model.job;
import com.example.skill_ladder.model.showCustomToast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddJobsActivity extends AppCompatActivity {

    EditText companyName, companyMobile, companyEmail,jobTitle;
    TextView jobDate;
    String IntendJobId,IntentJobTitle,jobClosingDate;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_jobs);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        IntendJobId = intent.getStringExtra("jobId");
        IntentJobTitle = intent.getStringExtra("jobTitle");
        jobClosingDate = intent.getStringExtra("jobClosingDate");

        companyName = findViewById(R.id.AddJobEditText01);
        companyEmail = findViewById(R.id.AddJobEditText02);
        companyMobile = findViewById(R.id.AddJobEditText03);
        jobTitle = findViewById(R.id.AddJobEditText04);
        jobDate = findViewById(R.id.AddjobDateText);



        loadCompanyData();

        ImageView imageView01 = findViewById(R.id.AddJobBackBtn01);
        imageView01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button button01 = findViewById(R.id.AddJobBtn);
        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addJob();
            }
        });

        CalendarView calendarView = findViewById(R.id.calendarView01);

        if(IntendJobId!=null){
            jobTitle.setText(IntentJobTitle);
            jobDate.setText(jobClosingDate);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            try {
                Date date = dateFormat.parse(jobClosingDate);
                if (date != null) {
                    calendarView.setDate(date.getTime());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int y, int m, int d) {
                jobDate.setText(String.valueOf(y)+"/"+String.valueOf(m+1)+"/"+String.valueOf(d));

            }
        });

    }
    private void loadCompanyData() {
        SharedPreferences sharedPreferences = getSharedPreferences("CompanyPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("companyEmail", "");

        firestore = FirebaseFirestore.getInstance();
        firestore.collection("company")
                .whereEqualTo("email",email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()) {
                            customAlert.showCustomAlert(AddJobsActivity.this, "Error", "Empty company data", R.drawable.cancel);
                        }else {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                companyName.setText(documentSnapshot.getString("name"));
                                companyEmail.setText(documentSnapshot.getString("email"));
                                companyMobile.setText(documentSnapshot.getString("mobile"));
                            }

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        customAlert.showCustomAlert( AddJobsActivity.this, "Error", "Error loading company data",R.drawable.cancel);
                    }
                });

    }

    private void addJob() {
        String name = companyName.getText().toString();
        String email = companyEmail.getText().toString();
        String mobile = companyMobile.getText().toString();
        String title = jobTitle.getText().toString();
        String date = jobDate.getText().toString();

        if (name.isEmpty()) {
            customAlert.showCustomAlert(AddJobsActivity.this, "Error", "Please fill the company name", R.drawable.cancel);
        } else if (email.isEmpty()) {
            customAlert.showCustomAlert(AddJobsActivity.this, "Error", "Please fill the company email", R.drawable.cancel);
        } else if (mobile.isEmpty()) {
            customAlert.showCustomAlert(AddJobsActivity.this, "Error", "Please fill the company mobile", R.drawable.cancel);
        } else if (title.isEmpty()) {
            customAlert.showCustomAlert(AddJobsActivity.this, "Error", "Please fill the job title", R.drawable.cancel);
        } else if (date.isEmpty()) {
            customAlert.showCustomAlert(AddJobsActivity.this, "Error", "Please Select the job date", R.drawable.cancel);
        } else {
            if(IntendJobId!=null){
                Map<String, Object> jobData = new HashMap<>();
                jobData.put("JobTitle", title);
                jobData.put("ClosingDate", date);
                firestore = FirebaseFirestore.getInstance();
                firestore.collection("jobs").document(IntendJobId)
                        .update(jobData)
                        .addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                customAlert.showCustomAlert(AddJobsActivity.this, "Success", "Job Update successfully", R.drawable.checked);
                                jobTitle.setText("");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                customAlert.showCustomAlert(AddJobsActivity.this, "Error", "Error adding job", R.drawable.cancel);
                            }
                        });

            } else {
                Map<String, Object> jobData = new HashMap<>();
                jobData.put("CompanyName", name);
                jobData.put("CompanyMobile", mobile);
                jobData.put("CompanyEmail", email);
                jobData.put("JobTitle", title);
                jobData.put("ClosingDate", date);

                firestore = FirebaseFirestore.getInstance();
                firestore.collection("jobs")
                        .add(jobData)
                        .addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
//                                showCustomToast.showToast(AddJobsActivity.this,"Success",R.drawable.checked);
                                customAlert.showCustomAlert(AddJobsActivity.this, "Success", "Job added successfully", R.drawable.checked);
                                jobTitle.setText("");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                customAlert.showCustomAlert(AddJobsActivity.this, "Error", "Error adding job", R.drawable.cancel);
                            }
                        });
            }
        }

    }

}