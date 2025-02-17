package com.example.skill_ladder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.skill_ladder.model.JobField;
import com.example.skill_ladder.model.customAlert;
import com.example.skill_ladder.model.job;
import com.example.skill_ladder.model.showCustomToast;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class JobCompanyHomeActivity extends AppCompatActivity {
    private CompanyJobListAdapter companyJobListAdapter;
    private List<CompanyJob> jobdetails;
    private LottieAnimationView lottieAnimationView;
    private RecyclerView recyclerView;
    FirebaseFirestore firestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_company_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView imageViewprofile = findViewById(R.id.CompanyhomeUserIcon);
        imageViewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent02 = new Intent(JobCompanyHomeActivity.this,CompanyUpdateProfileActivity.class);
                startActivity(intent02);
//                finish();
            }
        });


        FloatingActionButton fab = findViewById(R.id.CompanyHomeAddButton01);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("CompanyPrefs", MODE_PRIVATE);
                String companyEmail = sharedPreferences.getString("companyEmail", "");

                firestore = FirebaseFirestore.getInstance();
                firestore.collection("company")
                        .whereEqualTo("email", companyEmail)
                        .whereEqualTo("isActive", true)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                Intent intent02 = new Intent(JobCompanyHomeActivity.this, AddJobsActivity.class);
                                startActivity(intent02);
                            } else {
                                customAlert.showCustomAlert(JobCompanyHomeActivity.this, "Error",
                                        "You do not have access to add a job. Please ensure your company is active.", R.drawable.cancel);
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e("Firestore", "Error fetching job fields", e);
                        });

            }
        });

       lottieAnimationView = findViewById(R.id.lottie_view02);

         recyclerView = findViewById(R.id.CompanyHomeRV01);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(JobCompanyHomeActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

         jobdetails = new ArrayList<>();
        companyJobListAdapter = new CompanyJobListAdapter(jobdetails);
        recyclerView.setAdapter(companyJobListAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        firestore = FirebaseFirestore.getInstance();
        loadJobs();
    }

    private void loadJobs() {
        SharedPreferences sharedPreferences = getSharedPreferences("CompanyPrefs", MODE_PRIVATE);
        String companyEmail = sharedPreferences.getString("companyEmail", "");

        if (companyEmail.isEmpty()) {
            Log.e("companyEmail", "No companyEmail found in SharedPreferences");
            return;
        }
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("jobs")
                .whereEqualTo("CompanyEmail", companyEmail)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
                        if (error != null) {
                            customAlert.showCustomAlert(JobCompanyHomeActivity.this, "Error", " Fail to load data", R.drawable.cancel);
                            return;
                        }

                        jobdetails.clear();

                        for (QueryDocumentSnapshot doc : value) {
                            String id = doc.getId();
                            String jobTitle = doc.getString("JobTitle");
                            String date = doc.getString("ClosingDate");

                            if (jobTitle != null && date != null) {
                                jobdetails.add(new CompanyJob(jobTitle, date,id));
                            }
                        }

                        runOnUiThread(() -> {
                            if (jobdetails.isEmpty()) {
                                recyclerView.setVisibility(View.GONE);
                                lottieAnimationView.setVisibility(View.VISIBLE);
                            } else {
                                recyclerView.setVisibility(View.VISIBLE);
                                lottieAnimationView.setVisibility(View.GONE);
                            }

                            companyJobListAdapter.notifyDataSetChanged();
                        });

                    }
                });
    }
}
class CompanyJob {

    String JobId;
    String Jobtitle;
    String Jobclosedate;

    public CompanyJob(String jobtitle,String jobclosedate,String jobId ) {

        this.Jobclosedate = jobclosedate;
        this.Jobtitle = jobtitle;
        this.JobId = jobId;
    }
}
class CompanyJobListAdapter extends RecyclerView.Adapter<CompanyJobListAdapter.CompanyJobViewHolder> {
    private final List<CompanyJob> jobdetails;

    public CompanyJobListAdapter(List<CompanyJob> jobdetails) {
        this.jobdetails = jobdetails;
    }

    static class CompanyJobViewHolder extends RecyclerView.ViewHolder {

        TextView CompanyJobTitle;
        TextView CompanyJobClosingDate;
        View ContainerView;
        public CompanyJobViewHolder(@NonNull View itemView) {
            super(itemView);
            CompanyJobTitle = itemView.findViewById(R.id.CompanyJobViewTV01);
            CompanyJobClosingDate = itemView.findViewById(R.id.CompanyJobViewTV02);
            ContainerView = itemView;
        }
    }

    @NonNull
    @Override
    public CompanyJobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.job_company_job_item, parent, false);
        return new CompanyJobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyJobViewHolder holder, int position) {

        CompanyJob jobDetails = jobdetails.get(position);
        holder.CompanyJobTitle.setText(jobDetails.Jobtitle);
        holder.CompanyJobClosingDate.setText(jobDetails.Jobclosedate);
        String jobId = jobDetails.JobId.toString();

        holder.ContainerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(holder.itemView.getContext(),AddJobsActivity.class);
                intent.putExtra("jobId",jobId);
                intent.putExtra("jobTitle",jobDetails.Jobtitle);
                intent.putExtra("jobClosingDate",jobDetails.Jobclosedate);
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.ContainerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showCustomToast.showToast(holder.ContainerView.getContext(),"Loong Press",R.drawable.checked);
                OpenBottomSheet(view.getContext());

                return true;
            }
        });
    }

    private void OpenBottomSheet(Context context) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View bottomSheetView = LayoutInflater.from(context).inflate(
                R.layout.job_delete_bottom,
                null
        );

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    @Override
    public int getItemCount() {
        return jobdetails.size();
    }
}