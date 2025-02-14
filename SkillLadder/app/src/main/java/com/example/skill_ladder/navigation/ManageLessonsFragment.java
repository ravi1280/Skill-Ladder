package com.example.skill_ladder.navigation;

import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skill_ladder.R;
import com.example.skill_ladder.admin.AdminAddLessonActivity;
import com.example.skill_ladder.model.Company;
import com.example.skill_ladder.model.JobField;
import com.example.skill_ladder.model.JobTitle;
import com.example.skill_ladder.model.Lesson;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ManageLessonsFragment extends Fragment {
    mLessonListAdapter lessonListAdapter;
    List<Lesson> lDetails;
    RecyclerView ManageLessonRecyclerView;
    Spinner spinner;
    String SelectedName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_manage_lessons, container, false);

        FloatingActionButton FAB = view.findViewById(R.id.FABAddLesson01);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent01 = new Intent(requireActivity(), AdminAddLessonActivity.class);
                startActivity(intent01);

            }
        });
        spinner = view.findViewById(R.id.ManageLessonspinner001);
        loadSpinner();

       ManageLessonRecyclerView = view.findViewById(R.id.ManageLessonRV01);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ManageLessonRecyclerView.setLayoutManager(linearLayoutManager);
        lDetails = new ArrayList<>();
        lessonListAdapter = new mLessonListAdapter(lDetails);
        ManageLessonRecyclerView.setAdapter(lessonListAdapter);

        loadLessonDate();

        return view;
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
//                SelectedID = selectedField.getId();
                SelectedName =selectedField.getName();
                loadjobTitlesData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void loadjobTitlesData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if(SelectedName.equals("Select Field ---")){
            loadLessonDate();
            return;
        }

        db.collection("lessons")
                .whereEqualTo("jobField", SelectedName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    lDetails.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String id = documentSnapshot.getId();
                        String lessonName = documentSnapshot.getString("lessonName");
                        boolean isActive = Boolean.TRUE.equals(documentSnapshot.getBoolean("active"));

                        lDetails.add(new Lesson(id,lessonName,isActive));
                    }
                    lessonListAdapter.notifyDataSetChanged();

                })
                .addOnFailureListener(e -> {

                });
    }

    private void loadLessonDate() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("lessons")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    lDetails.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String id = documentSnapshot.getId();
                        String lessonName = documentSnapshot.getString("lessonName");
                        boolean isActive = Boolean.TRUE.equals(documentSnapshot.getBoolean("active"));

                        lDetails.add(new Lesson(id,lessonName,isActive));
                    }
                    lessonListAdapter.notifyDataSetChanged();

                })
                .addOnFailureListener(e -> {
                });
    }
}

class mLessonListAdapter extends RecyclerView.Adapter<mLessonListAdapter.mLessonViewHolder> {
    private final List<Lesson> lessondetails;

    public mLessonListAdapter(List<Lesson> ldetails) {
        this.lessondetails = ldetails;
    }

    static class mLessonViewHolder extends RecyclerView.ViewHolder {

        TextView LessonName;
        Button active;
        View ContainerView;
        public mLessonViewHolder(@NonNull View itemView) {
            super(itemView);
            LessonName = itemView.findViewById(R.id.ManageLessonTV01);
            active = itemView.findViewById(R.id.ManageLessonBtn01);
            ContainerView = itemView;
        }
    }

    @NonNull
    @Override
    public mLessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.manage_lesson_item, parent, false);
        return new mLessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mLessonViewHolder holder, int position) {

        Lesson lDetails = lessondetails.get(position);
        holder.LessonName.setText(lDetails.getLessonName());
        holder.active.setText(lDetails.isActive() ? "Active" : "Deactive");
        if(lDetails.isActive()){
            holder.active.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.chart03));
        }else {
            holder.active.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.chart05));
        }

        holder.active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleLessonStatus(lDetails, holder);

            }
        });

    }
    private void toggleLessonStatus(Lesson lesson, mLessonListAdapter.mLessonViewHolder holder) {
        boolean newStatus = !lesson.isActive();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("lessons").document(lesson.getId())
                .update("active", newStatus)
                .addOnSuccessListener(aVoid -> {

                    lesson.setActive(newStatus);
                    holder.active.setText(newStatus ? "Deactivate" : "Activate");
                    if(holder.active.getText()=="Activate"){
                        holder.active.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.chart05));
                    }else {
                        holder.active.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.chart03));

                    }


                    Toast.makeText(holder.itemView.getContext(), "Status updated", Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(holder.itemView.getContext(), "Failed to update", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return lessondetails.size();
    }
}