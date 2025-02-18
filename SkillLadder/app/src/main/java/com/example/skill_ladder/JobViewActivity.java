package com.example.skill_ladder;

import android.content.Intent;
import android.net.Uri;
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

import com.example.skill_ladder.model.job;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class JobViewActivity extends AppCompatActivity {
    List<job> jobdetails;
    JobListAdapter jobListAdapter;
    RecyclerView recyclerView;

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

        ImageView imageView = findViewById(R.id.JobviewBackImg01);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.jobviewRecyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(JobViewActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        jobdetails = new ArrayList<>();
        jobListAdapter = new JobListAdapter(jobdetails);
        recyclerView.setAdapter(jobListAdapter);

        loadJobs();
    }
    private void loadJobs(){
        FirebaseFirestore firestore= FirebaseFirestore.getInstance();
        firestore.collection("jobs").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                jobdetails.clear();
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    String cname = doc.getString("CompanyName");
                    String cmobile = doc.getString("CompanyMobile");
                    String cemail = doc.getString("CompanyEmail");
                    String jtitle = doc.getString("JobTitle");
                    String jclosingDate = doc.getString("ClosingDate");
                    jobdetails.add(new job(cname,cemail,cmobile,jtitle,jclosingDate));
                }
                jobListAdapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}



class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.JobViewHolder> {
    private final List<job> jobdetails;

    public JobListAdapter(List<job> jobdetails) {
        this.jobdetails = jobdetails;
    }

    static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView CompanyName;
        TextView CompanyEmail;
        TextView CompanyJobTitle;
        TextView CompanyJobClosingDate;

        ImageView callImage, smsImage;

        View ContainerView;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            CompanyName = itemView.findViewById(R.id.jobCompanyName);
            CompanyEmail = itemView.findViewById(R.id.jobCompanyEmail);
            CompanyJobTitle = itemView.findViewById(R.id.CompanyTV01);
            CompanyJobClosingDate = itemView.findViewById(R.id.jobCompanyTV02);
            callImage = itemView.findViewById(R.id.CompanyCallImageView);
            smsImage = itemView.findViewById(R.id.CompanySmsImageView);
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

        job jobDetails = jobdetails.get(position);
        holder.CompanyName.setText(jobDetails.getCompanyName());
        holder.CompanyEmail.setText(jobDetails.getCompanyEmail());
        holder.CompanyJobTitle.setText(jobDetails.getJobTitle());
        holder.CompanyJobClosingDate.setText(jobDetails.getJobDate());

        String cname = jobDetails.getCompanyName();
        String cnumber = jobDetails.getCompanyMobile();

        holder.callImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:"+jobDetails.getCompanyMobile()));
                view.getContext().startActivity(i);

            }
        });
        holder.smsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + jobDetails.getCompanyMobile())); // Replace contact.getMobile() with the desired phone number
                intent.putExtra("sms_body", "Hello! Start Your Conversation."); // Optional: Pre-fill the SMS body

                view.getContext().startActivity(intent);
            }
        });

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