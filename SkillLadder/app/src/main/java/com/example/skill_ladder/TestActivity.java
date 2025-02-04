package com.example.skill_ladder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<JobField> jobFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        recyclerView = findViewById(R.id.recyclerViewJobFields);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample Data
        jobFields = new ArrayList<>();
        jobFields.add(new JobField("Management", Arrays.asList("Project Manager", "HR Manager", "Operations Manager")));
        jobFields.add(new JobField("IT", Arrays.asList("Software Engineer", "Data Scientist", "System Administrator")));
        jobFields.add(new JobField("Marketing", Arrays.asList("SEO Specialist", "Social Media Manager", "Brand Manager")));

        recyclerView.setAdapter(new ParentAdapter(jobFields));
    }

    // JobField Model (Contains Job Titles)
    static class JobField {
        String fieldName;
        List<String> jobTitles;

        public JobField(String fieldName, List<String> jobTitles) {
            this.fieldName = fieldName;
            this.jobTitles = jobTitles;
        }
    }

    // Parent Adapter (Vertical RecyclerView)
    class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ViewHolder> {

        private final List<JobField> jobFieldList;

        public ParentAdapter(List<JobField> jobFieldList) {
            this.jobFieldList = jobFieldList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job_field, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            JobField jobField = jobFieldList.get(position);
            holder.textJobField.setText(jobField.fieldName);

            // Set up Child RecyclerView
            holder.recyclerViewJobTitles.setLayoutManager(new LinearLayoutManager(com.example.skill_ladder.TestActivity.this, LinearLayoutManager.HORIZONTAL, false));
            holder.recyclerViewJobTitles.setAdapter(new ChildAdapter(jobField.jobTitles));
        }

        @Override
        public int getItemCount() {
            return jobFieldList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textJobField;
            RecyclerView recyclerViewJobTitles;

            public ViewHolder(View itemView) {
                super(itemView);
                textJobField = itemView.findViewById(R.id.textJobField);
                recyclerViewJobTitles = itemView.findViewById(R.id.recyclerViewJobTitles);
            }
        }
    }

    // Child Adapter (Horizontal RecyclerView)
    class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {

        private final List<String> jobTitles;

        public ChildAdapter(List<String> jobTitles) {
            this.jobTitles = jobTitles;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job_title, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String jobTitle = jobTitles.get(position);
            holder.textJobTitle.setText(jobTitle);
            holder.textJobTitle.setOnClickListener(v -> {
                // Handle Job Title Click (Navigate or Show Details)
            });
        }

        @Override
        public int getItemCount() {
            return jobTitles.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textJobTitle;

            public ViewHolder(View itemView) {
                super(itemView);
                textJobTitle = itemView.findViewById(R.id.textJobTitle);
            }
        }
    }
}
