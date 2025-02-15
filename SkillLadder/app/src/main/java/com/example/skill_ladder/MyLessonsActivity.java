package com.example.skill_ladder;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.example.skill_ladder.model.Lesson;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MyLessonsActivity extends AppCompatActivity {
    RecyclerView recyclerView01;
    List<Lesson> myLessonTitle;
    MyLessonAdapter mylessonAdapter01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_lessons);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView imageView01 = findViewById(R.id.MylessonBackimageView);
        imageView01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView01 = findViewById(R.id.MylessonRecyclerView01);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyLessonsActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView01.setLayoutManager(linearLayoutManager);
        myLessonTitle = new ArrayList<>();
        mylessonAdapter01 = new MyLessonAdapter(myLessonTitle);
        recyclerView01.setAdapter(mylessonAdapter01);

        loadMylesson();

    }
    private void loadMylesson(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("lessons")
                .whereEqualTo("active", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    myLessonTitle.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String id = documentSnapshot.getId();
                        String lessonName = documentSnapshot.getString("lessonName");
                        Integer lessonPrice = documentSnapshot.getLong("price").intValue();
                        boolean isActive = Boolean.TRUE.equals(documentSnapshot.getBoolean("active"));

                        myLessonTitle.add(new Lesson(id,lessonName,lessonPrice,isActive));
                    }
                    mylessonAdapter01.notifyDataSetChanged();

                })
                .addOnFailureListener(e -> {

                });
    }
}

class MyLessonAdapter extends RecyclerView.Adapter<MyLessonAdapter.MyLessonViewHolder> {
    private final List<Lesson> mylessondetails;
    public MyLessonAdapter(List<Lesson> myLessondetails) {

        this.mylessondetails = myLessondetails;
    }

    static class MyLessonViewHolder extends RecyclerView.ViewHolder {
        TextView myLessonName;
        View ContainerView;
        Button myLessonbtn;

        public MyLessonViewHolder(@NonNull View itemView) {
            super(itemView);
            myLessonName = itemView.findViewById(R.id.tvLessonTitle);
            myLessonbtn = itemView.findViewById(R.id.courseContinueBtn01);
            ContainerView = itemView;
        }
    }

    @NonNull
    @Override
    public MyLessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.lesson_view_item, parent, false);
        return new MyLessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyLessonViewHolder holder, int position) {

        Lesson myLessondetailss = mylessondetails.get(position);
        holder.myLessonName.setText(myLessondetailss.getLessonName());

        holder.myLessonbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(holder.itemView.getContext(), "Lesson Success"+myLessondetailss.getId(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(holder.itemView.getContext(), LessonSummaryActivity.class);
                intent.putExtra("lessonId", myLessondetailss.getId());
                intent.putExtra("lessonName", myLessondetailss.getLessonName());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mylessondetails.size();
    }
}