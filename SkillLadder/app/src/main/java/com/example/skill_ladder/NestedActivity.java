package com.example.skill_ladder;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NestedActivity extends AppCompatActivity {
    private RecyclerView rvJobFields;
    private JobFieldAdapter jobFieldAdapter;
    private List<JobField> jobFieldList;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nested);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
}
 class JobField {
    private String fieldName;
    private List<JobTitle> jobTitles;

    public JobField(String fieldName, List<JobTitle> jobTitles) {
        this.fieldName = fieldName;
        this.jobTitles = jobTitles;
    }

    public String getFieldName() {
        return fieldName;
    }

    public List<JobTitle> getJobTitles() {
        return jobTitles;
    }
}

 class JobTitle {
    private String titleName;

    public JobTitle(String titleName) {
        this.titleName = titleName;
    }

    public String getTitleName() {
        return titleName;
    }
}

 class JobTitleAdapter extends RecyclerView.Adapter<JobTitleAdapter.ViewHolder> {
    private List<JobTitle> jobTitleList;

    public JobTitleAdapter(List<JobTitle> jobTitleList) {
        this.jobTitleList = jobTitleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job_title, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvJobTitle.setText(jobTitleList.get(position).getTitleName());
    }

    @Override
    public int getItemCount() {
        return jobTitleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJobTitle = itemView.findViewById(R.id.textJobTitle);
        }
    }
}
 class JobFieldAdapter extends RecyclerView.Adapter<JobFieldAdapter.ViewHolder> {
    private Context context;
    private List<JobField> jobFieldList;

    public JobFieldAdapter(Context context, List<JobField> jobFieldList) {
        this.context = context;
        this.jobFieldList = jobFieldList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_job_field, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JobField jobField = jobFieldList.get(position);
        holder.tvJobField.setText(jobField.getFieldName());

        // Setup horizontal RecyclerView for job titles
        JobTitleAdapter jobTitleAdapter = new JobTitleAdapter(jobField.getJobTitles());
        holder.rvJobTitles.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.rvJobTitles.setAdapter(jobTitleAdapter);
    }

    @Override
    public int getItemCount() {
        return jobFieldList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobField;
        RecyclerView rvJobTitles;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJobField = itemView.findViewById(R.id.textJobField);
            rvJobTitles = itemView.findViewById(R.id.recyclerViewJobTitles);
        }
    }
}
