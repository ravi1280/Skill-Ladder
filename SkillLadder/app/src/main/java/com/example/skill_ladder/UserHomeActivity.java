package com.example.skill_ladder;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.skill_ladder.model.Lesson;
import com.example.skill_ladder.model.job;
import com.example.skill_ladder.model.showCustomToast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UserHomeActivity extends AppCompatActivity {

    private RecyclerView nestedRecyclerView,latestLesson;
    private ArrayList<JobField01>  jobFieldList;
    private JobFieldAdapter01 jobFieldAdapter;

    private ArrayList<Lesson>  lessonList;
    private LessonAdapter01 LessonAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String UserName = sharedPreferences.getString("UserFullName", "");
        TextView name =findViewById(R.id.UserHomeNameTV001);
        name.setText("Hello "+UserName);

        ImageSlider imageSlider= findViewById(R.id.ImageSlider001);
        ArrayList<SlideModel> imagelist = new ArrayList<>();
        imagelist.add(new SlideModel(R.drawable.image1, ScaleTypes.CENTER_INSIDE));
        imagelist.add(new SlideModel(R.drawable.image2, ScaleTypes.CENTER_INSIDE));
        imagelist.add(new SlideModel(R.drawable.image3, ScaleTypes.CENTER_INSIDE));
        imagelist.add(new SlideModel(R.drawable.image4, ScaleTypes.CENTER_INSIDE));

        imageSlider.setImageList(imagelist,ScaleTypes.CENTER_INSIDE);




        latestLesson= findViewById(R.id.LatestLessonrecyclerView);
        LinearLayoutManager linearLayoutManager01 =new LinearLayoutManager(UserHomeActivity.this);
        linearLayoutManager01.setOrientation(RecyclerView.HORIZONTAL);
        latestLesson.setLayoutManager(linearLayoutManager01);
        lessonList = new ArrayList<>();
        LessonAdapter = new LessonAdapter01( lessonList);
        latestLesson.setAdapter(LessonAdapter);

        loadLatestLeson();

        nestedRecyclerView= findViewById(R.id.nestedRecyclerView);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(UserHomeActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        nestedRecyclerView.setLayoutManager(linearLayoutManager);

        // Create sample data
        jobFieldList = new ArrayList<>();
        jobFieldAdapter = new JobFieldAdapter01(this, jobFieldList);
        nestedRecyclerView.setAdapter(jobFieldAdapter);
        loadJobField();

        FloatingActionButton buttonFAB = findViewById(R.id.floatingActionButton2);
        buttonFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent01 = new Intent(UserHomeActivity.this,UserCartActivity.class);
                startActivity(intent01);
            }
        });
        ImageView imageViewHome01 = findViewById(R.id.HomeUserSearchImageView);
        imageViewHome01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent01 = new Intent(UserHomeActivity.this,SearchActivity.class);
                startActivity(intent01);

            }
        });
        ImageView imageViewHome02 = findViewById(R.id.HomeUserimageView);
        imageViewHome02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent01 = new Intent(UserHomeActivity.this,UserProfileActivity.class);
                startActivity(intent01);

            }
        });
        ImageView imageViewHome03 = findViewById(R.id.HomeUserLessonImageView);
        imageViewHome03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent01 = new Intent(UserHomeActivity.this,MyLessonsActivity.class);
                startActivity(intent01);

            }
        });
        ImageView imageViewHome04 = findViewById(R.id.HomeUserJobImageView);
        imageViewHome04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent01 = new Intent(UserHomeActivity.this,JobViewActivity.class);
                startActivity(intent01);

            }
        });

    }
    private void loadLatestLeson(){
        FirebaseFirestore firestore= FirebaseFirestore.getInstance();
        firestore.collection("lessons")
                .limit(5)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        lessonList.clear();
                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
                            String id = doc.getId();
                            String lessonName = doc.getString("lessonName");
                            Integer lessonPrice = doc.getLong("price").intValue();
                            boolean isActive = Boolean.TRUE.equals(doc.getBoolean("active"));

                            lessonList.add(new Lesson(id,lessonName,lessonPrice,isActive));
                        }
                        LessonAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showCustomToast.showToast(UserHomeActivity.this, "Can not Fetch Latest Lessons details !", R.drawable.cancel);
                    }
                });
    }
    private void loadJobField(){
        FirebaseFirestore firestore= FirebaseFirestore.getInstance();
        firestore.collection("JobFields")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                jobFieldList.clear();
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    String jfname = doc.getString("name");
                    jobFieldList.add(new JobField01(jfname));
                }
                jobFieldAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showCustomToast.showToast(UserHomeActivity.this, "Can not Fetch JOb Fields details !", R.drawable.cancel);

            }
        });

    }
}
class LessonAdapter01 extends RecyclerView.Adapter<LessonAdapter01.LessonHolder>{

    private List<Lesson> LessonList;

    public LessonAdapter01(List<Lesson> LessonList01) {
        this.LessonList = LessonList01;
    }

    static class LessonHolder extends RecyclerView.ViewHolder{
        TextView lessonTitle,lessonPrice;
        Button view;
        View ContainerView;
        public LessonHolder(@NonNull View itemView) {
            super(itemView);
            lessonTitle = itemView.findViewById(R.id.LatestLessonTV01);
            lessonPrice = itemView.findViewById(R.id.LatestLessonTV02);
            view = itemView.findViewById(R.id.LatestLessonButton);
            ContainerView =itemView;
        }
    }
    @NonNull
    @Override
    public LessonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.latest_lesson_item,parent,false);
        return new LessonHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonHolder holder, int position) {
        Lesson lessondetailss = LessonList.get(position);
        holder.lessonTitle.setText(lessondetailss.getLessonName());
        holder.lessonPrice.setText("$ "+lessondetailss.getPrice().toString());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent01 = new Intent(holder.itemView.getContext(),SearchActivity.class);
                        intent01.putExtra("lessonId001",lessondetailss.getId());
                        holder.itemView.getContext().startActivity(intent01);

            }
        });
    }

    @Override
    public int getItemCount() {
        return LessonList.size();
    }
}

class JobTitle01 {
    private String titleName;

    public JobTitle01(String titleName) {
        this.titleName = titleName;
    }

    public String getTitleName() {
        return titleName;
    }
}
class jobTitleAdapter01 extends RecyclerView.Adapter<jobTitleAdapter01.jobTitleHolder>{

    private List<JobTitle01> jobTitleList;

    public jobTitleAdapter01(List<JobTitle01> jobTitleList) {
        this.jobTitleList = jobTitleList;
    }

    static class jobTitleHolder extends RecyclerView.ViewHolder{
        TextView tvJobTitle;
        View ContainerView;
        public jobTitleHolder(@NonNull View itemView) {
            super(itemView);
            tvJobTitle = itemView.findViewById(R.id.JobTitleTxt001);
            ContainerView =itemView;
        }
    }
    @NonNull
    @Override
    public jobTitleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_title_view_item,parent,false);
        return new jobTitleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull jobTitleHolder holder, int position) {
       JobTitle01 jobTdetailss = jobTitleList.get(position);
        holder.tvJobTitle.setText(jobTdetailss.getTitleName());
        holder.ContainerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        Intent intent = new Intent(holder.itemView.getContext(),SearchActivity.class);
                        intent.putExtra("JobTitleName",jobTdetailss.getTitleName());
                        holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobTitleList.size();
    }

}
class JobField01 {
    private String fieldName;

    public JobField01(String fieldName) {
        this.fieldName = fieldName;
    }
    public String getFieldName() {
        return fieldName;
    }

}

class JobFieldAdapter01 extends RecyclerView.Adapter<JobFieldAdapter01.ViewHolder> {
    private Context context;
    private List<JobField01> jobFieldList;

    public JobFieldAdapter01(Context context, List<JobField01> jobFieldList) {
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
        JobField01 jobField = jobFieldList.get(position);
        holder.tvJobField.setText(jobField.getFieldName());
        List<JobTitle01> jobTitledetails = new ArrayList<>();


        jobTitleAdapter01 jobTitleAdapter22 = new jobTitleAdapter01(jobTitledetails);
        holder.rvJobTitles.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.rvJobTitles.setAdapter(jobTitleAdapter22);

        // Query Firestore for job titles based on fieldName
        FirebaseFirestore firestore= FirebaseFirestore.getInstance();
        firestore.collection("JobTitles")
                .whereEqualTo("fieldName",jobField.getFieldName())
                .whereEqualTo("isActive", true)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                jobTitledetails.clear();
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    String cname = doc.getString("name");

                    jobTitledetails.add(new JobTitle01(cname));
                }
                jobTitleAdapter22.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firestore", "Error fetching job titles", e);
            }
        });

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
