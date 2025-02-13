package com.example.skill_ladder.navigation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skill_ladder.R;
import com.example.skill_ladder.model.JobField;
import com.example.skill_ladder.model.JobTitle;
import com.example.skill_ladder.model.customAlert;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageOtherFragment extends Fragment {

    EditText jobFiled,jobTitle01;
    RecyclerView ManageOtherFieldRV,ManageOtherTitledRV;
    List<JobField> jobFieldList;
    List<JobTitle> titleDetails;
    FieldListAdapter fieldListAdapter;
    TitleListAdapter titleListAdapter;
    Spinner spinner;
    String SelectedID,SelectedName;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_other, container, false);

        jobFiled = view.findViewById(R.id.addFieldEditText);
        jobTitle01 = view.findViewById(R.id.JobTitleEditText);
        spinner =view.findViewById(R.id.SelectFieldspinner);

        Button button01= view.findViewById(R.id.AddFieldBtn01);
        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFiled();

            }
        });

        loadSpinner();
        Button button02= view.findViewById(R.id.addJobTitleBtn01);
        button02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addTitle();

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
        titleDetails = new ArrayList<>();
        titleListAdapter = new TitleListAdapter(titleDetails);
        ManageOtherTitledRV.setAdapter(titleListAdapter);

//        loadjobTitles();

        return view;
    }
    public void refresh(){
        loadjobFields();
        loadjobTitles();
        loadSpinner();
    }

    private void loadjobFields(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("JobFields")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    jobFieldList.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String id = documentSnapshot.getId();
                        String name = documentSnapshot.getString("name");
                        boolean isActive = Boolean.TRUE.equals(documentSnapshot.getBoolean("isActive"));

                        jobFieldList.add(new JobField(id, name, isActive));
                    }
                    fieldListAdapter.notifyDataSetChanged();

                })
                .addOnFailureListener(e -> {
                });
    }
    private void loadjobTitles(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("JobTitles")
                .whereEqualTo("fieldId", SelectedID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    titleDetails.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String id = documentSnapshot.getId(); // Get the document ID
                        String fieldId = documentSnapshot.getString("fieldId");
                        String fieldName = documentSnapshot.getString("fieldName");
                        String name = documentSnapshot.getString("name");
                        boolean isActive = Boolean.TRUE.equals(documentSnapshot.getBoolean("isActive"));

                        titleDetails.add(new JobTitle(id,name,fieldId,fieldName,isActive));
                    }
                    titleListAdapter.notifyDataSetChanged();

                })
                .addOnFailureListener(e -> {
                });
    }


    private void addFiled(){
        String jobfiled = jobFiled.getText().toString();
        if(jobfiled.isEmpty()){
            customAlert.showCustomAlert(getContext(),"Error","Please Fill Job field",R.drawable.cancel);

        }else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, Object> jobField = new HashMap<>();
            jobField.put("name", jobfiled);
            jobField.put("isActive", true);

            db.collection("JobFields")
                    .add(jobField)
                    .addOnSuccessListener(documentReference -> {
                        customAlert.showCustomAlert(getContext(),"Success","Successfully Add Field",R.drawable.checked);
                        jobFiled.setText(""); Toast.makeText(getContext(),"Job field added with ID: " + documentReference.getId(),Toast.LENGTH_SHORT).show();
                        refresh();

                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(),"Job field added Error" ,Toast.LENGTH_SHORT).show();
                        customAlert.showCustomAlert(getContext(),"Error","Job field added Error",R.drawable.cancel);

                    });


        }


    }
    public void loadSpinner(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("JobFields")
                .whereEqualTo("isActive", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<JobField> jobFieldList = new ArrayList<>();
                    List<String> fieldNames = new ArrayList<>();

                    jobFieldList.add(new JobField("", "Select Field ---", true));
                    fieldNames.add("Select Field ---");

                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        JobField jobField = document.toObject(JobField.class);
                        if (jobField != null) {
                            jobField.setId(document.getId());
                            jobFieldList.add(jobField);
                            fieldNames.add(jobField.getName());
                        }
                    }

                    updateSpinner(fieldNames, jobFieldList);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error fetching job fields", e);
                });

    }
    private void updateSpinner(List<String> fieldNames, List<JobField> jobFieldList) {


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, fieldNames);
        adapter.setDropDownViewResource(R.layout.coustom_spinner_dropdown);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JobField selectedField = jobFieldList.get(position);
                Log.d("Spinner", "Selected Job Field ID: " + selectedField.getId());
                SelectedID = selectedField.getId();
                SelectedName =selectedField.getName();
                loadjobTitles();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    private void addTitle(){
        String title01 = jobTitle01.getText().toString();

        if(title01.isEmpty()){
            customAlert.showCustomAlert(getContext(),"Error","Please Fill Job Title ",R.drawable.cancel);

        }else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, Object> jobTitle = new HashMap<>();
            jobTitle.put("name", title01);
            jobTitle.put("fieldId",SelectedID);  // Link to JobField
            jobTitle.put("fieldName",SelectedName); // Store field name
            jobTitle.put("isActive", true);

            db.collection("JobTitles")
                    .add(jobTitle)
                    .addOnSuccessListener(documentReference -> {
                        Log.d("Firestore", "Job Title added with ID: " + documentReference.getId());
                        Toast.makeText(getContext(), "Job Title Added", Toast.LENGTH_SHORT).show();
                        jobTitle01.setText("");
                        loadjobTitles();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Error adding job title", e);
                        Toast.makeText(getContext(), "Failed to add Job Title", Toast.LENGTH_SHORT).show();
                    });

        }

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
        if(FDetails.isActive()){
            holder.jobFieldStatus.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.chart03));
        }else {
            holder.jobFieldStatus.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.chart05));
        }

        holder.jobFieldStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleJobFieldStatus(FDetails, holder);

            }
        });

    }
    private void toggleJobFieldStatus(JobField jobField, FieldViewHolder holder) {
        boolean newStatus = !jobField.isActive();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("JobFields").document(jobField.getId())
                .update("isActive", newStatus)
                .addOnSuccessListener(aVoid -> {

                    jobField.setActive(newStatus);
                    holder.jobFieldStatus.setText(newStatus ? "Deactivate" : "Activate");
                    if(holder.jobFieldStatus.getText()=="Activate"){
                        holder.jobFieldStatus.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.chart05));
                    }else {
                        holder.jobFieldStatus.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.chart03));

                    }


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




class TitleListAdapter extends RecyclerView.Adapter<TitleListAdapter.TitleViewHolder> {
    private final List<JobTitle> titledetails;

    public TitleListAdapter(List<JobTitle> tdetails) {
        this.titledetails = tdetails;
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {

        TextView TitleName,FieldName;
        Button active01;
        View ContainerView;
        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            TitleName = itemView.findViewById(R.id.manageJobTitleTextView01);
            FieldName = itemView.findViewById(R.id.manageJobTitleTextView02);
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
        JobTitle TDetails = titledetails.get(position);
        holder.TitleName.setText(TDetails.getName());
        holder.FieldName.setText(TDetails.getFieldName());
        holder.active01.setText(TDetails.isActive() ? "Deactivate" : "Activate");
        if(TDetails.isActive()){
            holder.active01.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.chart03));
        }else {
            holder.active01.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.chart05));
        }

        holder.active01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleJobTitleStatus(TDetails, holder);
            }
        });


    }

    private void toggleJobTitleStatus(JobTitle jobTitle, TitleListAdapter.TitleViewHolder holder) {
        boolean newStatus01 = !jobTitle.isActive();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("JobTitles").document(jobTitle.getId())
                .update("isActive", newStatus01)
                .addOnSuccessListener(aVoid -> {

                    jobTitle.setActive(newStatus01);
                    holder.active01.setText(newStatus01 ? "Deactivate" : "Activate");
                    if(holder.active01.getText()=="Activate"){
                        holder.active01.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.chart05));
                    }else {
                        holder.active01.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.chart03));

                    }


                    Toast.makeText(holder.itemView.getContext(), "Status updated", Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(holder.itemView.getContext(), "Failed to update", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return titledetails.size();
    }
}





