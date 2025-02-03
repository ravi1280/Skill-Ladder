package com.example.skill_ladder;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class JobViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.jobviewRecyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(JobViewActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<jobDetails> jobdetails = new ArrayList<>();
        jobdetails.add(new jobDetails("Java institute01", "javainstitute01@gmail.com","0719681816"));
        jobdetails.add(new jobDetails("Java institute02", "javainstitute02@gmail.com","0719681815"));
        jobdetails.add(new jobDetails("Java institute03", "javainstitute03@gmail.com","0719681814"));
        jobdetails.add(new jobDetails("Java institute04", "javainstitute04@gmail.com","0719681817"));
        jobdetails.add(new jobDetails("Java institute05", "javainstitute05@gmail.com","0719681818"));
        jobdetails.add(new jobDetails("Java institute06", "javainstitute06@gmail.com","0719681819"));

        JobListAdapter jobListAdapter = new JobListAdapter(jobdetails);
        recyclerView.setAdapter(jobListAdapter);
    }
}

class jobDetails {
    String companyName;
    String companyEmail;
    String companyNumber;


    public jobDetails(String name, String email,String number ) {
        this.companyName = name;
        this.companyEmail = email;
        this.companyNumber = number;

    }
}

class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.JobViewHolder> {
    private final List<jobDetails> jobdetails;

    public JobListAdapter(List<jobDetails> jobdetails) {
        this.jobdetails = jobdetails;
    }

    static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView CompanyName;
        TextView CompanyEmail;

        View ContainerView;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            CompanyName = itemView.findViewById(R.id.jobCompanyName);
            CompanyEmail = itemView.findViewById(R.id.jobCompanyEmail);
            ContainerView = itemView;
        }
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.job_view_item, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {

        jobDetails jobDetails = jobdetails.get(position);
        holder.CompanyName.setText(jobDetails.companyName);
        holder.CompanyEmail.setText(jobDetails.companyEmail);

        String cname = jobDetails.companyName;
        String cnumber = jobDetails.companyNumber;

        holder.ContainerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(holder.itemView.getContext(),cnumber,Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return jobdetails.size();
    }
}