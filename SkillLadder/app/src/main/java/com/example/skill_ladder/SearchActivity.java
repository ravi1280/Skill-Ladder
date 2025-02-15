package com.example.skill_ladder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import com.example.skill_ladder.admin.AdminAddLessonActivity;
import com.example.skill_ladder.model.JobField;
import com.example.skill_ladder.model.JobTitle;
import com.example.skill_ladder.model.Lesson;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    RecyclerView searchRecyclerView;
    SearchLessonAdapter SearchlessonAdapter01;
    List<Lesson> SearchLessonTitle;
    EditText searchEditText;
    ImageView SearchIcon;
    Spinner spinner01, spinner02;
    String fieldName, titleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView imageView01 = findViewById(R.id.SearchBackimageView01);
        imageView01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchEditText = findViewById(R.id.SearcheditTextText01);
        SearchIcon = findViewById(R.id.SearchIcon01);
        SearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadsearchTextLesson();
            }
        });

        spinner01 = findViewById(R.id.jobFieldSpinner001);
        loadSpinner01();
        spinner02 = findViewById(R.id.jobTitleSpinner002);
        loadSpinner02();


        searchRecyclerView = findViewById(R.id.searchRecyclerView01);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        searchRecyclerView.setLayoutManager(linearLayoutManager);
        SearchLessonTitle = new ArrayList<>();
        SearchlessonAdapter01 = new SearchLessonAdapter(SearchLessonTitle);
        searchRecyclerView.setAdapter(SearchlessonAdapter01);

        loadLesson();
    }

    public void loadSpinner01() {
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

                    updateSpinner01(fieldNames, jobFieldList);

                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error fetching job fields", e);
                });
    }

    private void updateSpinner01(List<String> fieldNames, List<JobField> jobFieldList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fieldNames);
        adapter.setDropDownViewResource(R.layout.coustom_spinner_dropdown);
        spinner01.setAdapter(adapter);

        spinner01.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                JobField selectedField = jobFieldList.get(i);
                Log.d("Spinner", "Selected Job Field ID: " + selectedField.getId());
                fieldName = selectedField.getName();
                loadSpinner02();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadSpinner02(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("JobTitles")
                .whereEqualTo("fieldName", fieldName)
                .whereEqualTo("isActive", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<JobTitle> JobTitleList = new ArrayList<>();
                    List<String> titleNames = new ArrayList<>();

                    JobTitleList.add(new JobTitle("","","", "Select Title ---", true));
                    titleNames.add("Select Title ---");

                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        JobTitle jobTitle = document.toObject(JobTitle.class);
                        if (jobTitle != null) {
                            jobTitle.setId(document.getId());

                            JobTitleList.add(jobTitle);
                            titleNames.add(jobTitle.getName());
                        }
                    }

                    updateSpinner02(titleNames, JobTitleList);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error fetching job fields", e);
                });

    }

    private void updateSpinner02(List<String> titleNames, List<JobTitle> JobTitleList) {


        ArrayAdapter<String> adapter = new ArrayAdapter<>(SearchActivity.this,android.R.layout.simple_spinner_item, titleNames);
        adapter.setDropDownViewResource(R.layout.coustom_spinner_dropdown);
        spinner02.setAdapter(adapter);


        spinner02.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (JobTitleList.isEmpty() || position < 0 || position >= JobTitleList.size()) {
                    return;
                }
                JobTitle selectedTitle = JobTitleList.get(position);
                Log.d("Spinner", "Selected Job Field ID: " + selectedTitle.getId());

                titleName =selectedTitle.getName();
                loadTitleLesson();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadLesson(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("lessons")
                .whereEqualTo("active", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    SearchLessonTitle.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String id = documentSnapshot.getId();
                        String lessonName = documentSnapshot.getString("lessonName");
                        Integer lessonPrice = documentSnapshot.getLong("price").intValue();
                        boolean isActive = Boolean.TRUE.equals(documentSnapshot.getBoolean("active"));

                        SearchLessonTitle.add(new Lesson(id,lessonName,lessonPrice,isActive));
                    }
                    SearchlessonAdapter01.notifyDataSetChanged();

                })
                .addOnFailureListener(e -> {

                });
    }
    private void loadTitleLesson(){
        String titleName01 = spinner02.getSelectedItem().toString();
        if(titleName01.equals("Select Title ---")){
            loadLesson();
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("lessons")
                .whereEqualTo("active", true)
                .whereEqualTo("jobTitle", titleName01)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    SearchLessonTitle.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String id = documentSnapshot.getId();
                        String lessonName = documentSnapshot.getString("lessonName");
                        Integer lessonPrice = documentSnapshot.getLong("price").intValue();
                        boolean isActive = Boolean.TRUE.equals(documentSnapshot.getBoolean("active"));

                        SearchLessonTitle.add(new Lesson(id,lessonName,lessonPrice,isActive));
                    }
                    SearchlessonAdapter01.notifyDataSetChanged();

                })
                .addOnFailureListener(e -> {

                });
    }

    private void loadsearchTextLesson(){
        String searchValue = searchEditText.getText().toString();

        if(searchValue.isEmpty()){
            loadLesson();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("lessons")
                .whereGreaterThanOrEqualTo("lessonName", searchValue)
                .whereLessThanOrEqualTo("lessonName", searchValue + "\uf8ff")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    SearchLessonTitle.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String id = documentSnapshot.getId();
                        String lessonName = documentSnapshot.getString("lessonName");
                        Integer lessonPrice = documentSnapshot.getLong("price").intValue();
                        boolean isActive = Boolean.TRUE.equals(documentSnapshot.getBoolean("active"));

                        SearchLessonTitle.add(new Lesson(id,lessonName,lessonPrice,isActive));
                    }
                    SearchlessonAdapter01.notifyDataSetChanged();

                })
                .addOnFailureListener(e -> {

                });
    }
}

class SearchLessonAdapter extends RecyclerView.Adapter<SearchLessonAdapter.SearchLessonViewHolder> {
    private final List<Lesson> Searchlessondetails;
    public SearchLessonAdapter(List<Lesson> Searchlessondetails) {
        this.Searchlessondetails = Searchlessondetails;
    }

    static class SearchLessonViewHolder extends RecyclerView.ViewHolder {
        TextView LessonName;
        Button LessonBtn;
        ImageView LessonCartBtn;
        TextView LessonPrice;
        View ContainerView;


        public SearchLessonViewHolder(@NonNull View itemView) {
            super(itemView);
            LessonName = itemView.findViewById(R.id.ByLessonTitle01);
            LessonPrice = itemView.findViewById(R.id.ByLessonTitle02);
            LessonBtn = itemView.findViewById(R.id.courseBuyBtn01);
            LessonCartBtn = itemView.findViewById(R.id.courseBuyBtn02);
            ContainerView = itemView;
        }
    }

    @NonNull
    @Override
    public SearchLessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.lesson_buy_item, parent, false);
        return new SearchLessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchLessonViewHolder holder, int position) {

        Lesson lessonDetails = Searchlessondetails.get(position);
        holder.LessonName.setText(lessonDetails.getLessonName());
        holder.LessonPrice.setText("$ " + lessonDetails.getPrice());

        String lessonName = lessonDetails.getLessonName();

        holder.LessonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(holder.itemView.getContext());
                View sheetView = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.buy_lesson_bottom_sheet, null);
                TextView textView = sheetView.findViewById(R.id.lessonsheetTV01);
                textView.setText(lessonName);
                Button buyButton = sheetView.findViewById(R.id.lessonBuysheetdBtn01);

                buyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(holder.itemView.getContext(), PaymentSuccessActivity.class);
                        holder.itemView.getContext().startActivity(intent);
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();
            }
        });
        holder.LessonCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(holder.itemView.getContext(), "Added to Cart"+lessonDetails.getId(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return Searchlessondetails.size();
    }
}