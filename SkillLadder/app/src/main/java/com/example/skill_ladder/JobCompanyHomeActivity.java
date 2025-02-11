package com.example.skill_ladder;

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
import com.example.skill_ladder.model.customAlert;
import com.example.skill_ladder.model.job;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
            }
        });

        FloatingActionButton fab = findViewById(R.id.CompanyHomeAddButton01);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent02 = new Intent(JobCompanyHomeActivity.this,AddJobsActivity.class);
                startActivity(intent02);
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

//        loadJobs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        firestore = FirebaseFirestore.getInstance();
        loadJobs();
    }

    private void loadJobs() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
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
                            String jobTitle = doc.getString("JobTitle");
                            String date = doc.getString("ClosingDate");

                            if (jobTitle != null && date != null) {
                                jobdetails.add(new CompanyJob(jobTitle, date));
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

    String Jobtitle;
    String Jobclosedate;

    public CompanyJob(String jobtitle,String jobclosedate ) {

        this.Jobclosedate = jobclosedate;
        this.Jobtitle = jobtitle;
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
        String company = jobDetails.Jobtitle.toString();

        holder.ContainerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(holder.itemView.getContext(),company,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobdetails.size();
    }
}