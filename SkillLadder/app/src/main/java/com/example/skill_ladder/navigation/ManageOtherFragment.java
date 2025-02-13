package com.example.skill_ladder.navigation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skill_ladder.R;
import com.example.skill_ladder.model.JobField;
import com.example.skill_ladder.model.customAlert;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageOtherFragment extends Fragment {

    EditText jobFiled,jobTitle;
    RecyclerView ManageOtherFieldRV,ManageOtherTitledRV;
    List<JobField> jobFieldList;
    FieldListAdapter fieldListAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_other, container, false);

        jobFiled = view.findViewById(R.id.addFieldEditText);
        String filed = jobFiled.getText().toString();

        Button button01= view.findViewById(R.id.AddFieldBtn01);
        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFiled();

            }
        });

        ManageOtherFieldRV = view.findViewById(R.id.ManageOtherRecyclerView01);
        LinearLayoutManager linearLayoutManager01 = new LinearLayoutManager(requireActivity());
        linearLayoutManager01.setOrientation(LinearLayoutManager.HORIZONTAL);
        ManageOtherFieldRV.setLayoutManager(linearLayoutManager01);
        jobFieldList = new ArrayList<>();
        fieldListAdapter = new FieldListAdapter(jobFieldList);
        ManageOtherFieldRV.setAdapter(fieldListAdapter);

        loadjobFields();










        ManageOtherTitledRV = view.findViewById(R.id.ManageOtherRecyclerView02);
        LinearLayoutManager linearLayoutManager02 = new LinearLayoutManager(requireActivity());
        linearLayoutManager02.setOrientation(LinearLayoutManager.HORIZONTAL);
        ManageOtherTitledRV.setLayoutManager(linearLayoutManager02);
        List<Title> titleDetails = new ArrayList<>();
        titleDetails.add(new Title("Quality Assursance","Active"));
        titleDetails.add(new Title("Quality Assursance","Active"));
        titleDetails.add(new Title("Quality Assursance","Deactive"));
        titleDetails.add(new Title("Quality Assursance","Active"));
        titleDetails.add(new Title("Quality Assursance","Deactive"));

        TitleListAdapter titleListAdapter = new TitleListAdapter(titleDetails);
        ManageOtherTitledRV.setAdapter(titleListAdapter);
        return view;
    }
    private void loadjobFields(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("JobFields")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    jobFieldList.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String id = documentSnapshot.getId(); // Get the document ID
                        String name = documentSnapshot.getString("name");
                        boolean isActive = Boolean.TRUE.equals(documentSnapshot.getBoolean("isActive"));

                        jobFieldList.add(new JobField(id, name, isActive));
                    }
                    fieldListAdapter.notifyDataSetChanged();

                })
                .addOnFailureListener(e -> {
                });
    }

    private void addFiled(){
        String jobfiled = jobFiled.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> jobField = new HashMap<>();
        jobField.put("name", jobfiled);
        jobField.put("isActive", true);

        db.collection("JobFields")
                .add(jobField)
                .addOnSuccessListener(documentReference -> {
                    customAlert.showCustomAlert(getContext(),"Success","Successfully Add Field",R.drawable.checked);
                    jobFiled.setText(""); Toast.makeText(getContext(),"Job field added with ID: " + documentReference.getId(),Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(),"Job field added Error" ,Toast.LENGTH_SHORT).show();
                    customAlert.showCustomAlert(getContext(),"Error","Job field added Error",R.drawable.cancel);

                });

    }
}

class FieldListAdapter extends RecyclerView.Adapter<FieldListAdapter.FieldViewHolder> {
    private final List<JobField> fielddetails;

    public FieldListAdapter(List<JobField> fdetails) {
        this.fielddetails = fdetails;
    }

    static class FieldViewHolder extends RecyclerView.ViewHolder {

        TextView jobFieldNameTextView;
        Button jobFieldStatus;
        View ContainerView;
        public FieldViewHolder(@NonNull View itemView) {
            super(itemView);
            jobFieldNameTextView = itemView.findViewById(R.id.ManageFieldtextView01);
            jobFieldStatus = itemView.findViewById(R.id.ManageFieldBtn);
            ContainerView = itemView;
        }
    }

    @NonNull
    @Override
    public FieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.manage_field_item, parent, false);
        return new FieldViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull FieldViewHolder holder, int position) {
        JobField FDetails = fielddetails.get(position);
        holder.jobFieldNameTextView.setText(FDetails.getName());
        holder.jobFieldStatus.setText(FDetails.isActive() ? "Deactivate" : "Activate");
        holder.jobFieldStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleJobFieldStatus(FDetails, holder);
            }
        });

    }
    private void toggleJobFieldStatus(JobField jobField, FieldViewHolder holder) {
        boolean newStatus = !jobField.isActive(); // Toggle status

        // Update Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("JobFields").document(jobField.getId())
                .update("isActive", newStatus)
                .addOnSuccessListener(aVoid -> {
                    // Update local object
                    jobField.setActive(newStatus);

                    // Update UI
//                    holder.jobFieldNameTextView.setText(newStatus ? "Active" : "Inactive");

                    holder.jobFieldStatus.setText(newStatus ? "Deactivate" : "Activate");

                    Toast.makeText(holder.itemView.getContext(), "Status updated", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(holder.itemView.getContext(), "Failed to update", Toast.LENGTH_SHORT).show();
                });
    }


    @Override
    public int getItemCount() {
        return fielddetails.size();
    }
}



class Title {
    String TitleName;
    String TStatus;
    public Title(String name,String status ) {

        this.TitleName = name;
        this.TStatus = status;
    }
}
class TitleListAdapter extends RecyclerView.Adapter<TitleListAdapter.TitleViewHolder> {
    private final List<Title> titledetails;

    public TitleListAdapter(List<Title> tdetails) {
        this.titledetails = tdetails;
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {

        TextView TitleName;
        Button active01;
        View ContainerView;
        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            TitleName = itemView.findViewById(R.id.manageJobTitleTextView01);
            active01 = itemView.findViewById(R.id.manageJobTitleButton1);
            ContainerView = itemView;
        }
    }

    @NonNull
    @Override
    public TitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.manage_job_title_item, parent, false);
        return new TitleViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TitleViewHolder holder, int position) {
        Title TDetails = titledetails.get(position);
        holder.TitleName.setText(TDetails.TitleName);
        holder.active01.setText(TDetails.TStatus);
//        String Status = TDetails.TStatus.toString();

        holder.active01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TDetails.TStatus.equalsIgnoreCase("Active")) {
                    TDetails.TStatus = "Deactive";
                } else {
                    TDetails.TStatus = "Active";
                }

                holder.active01.setText(TDetails.TStatus);
                notifyItemChanged(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return titledetails.size();
    }
}