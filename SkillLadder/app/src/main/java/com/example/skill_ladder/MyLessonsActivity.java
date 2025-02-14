package com.example.skill_ladder;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class MyLessonsActivity extends AppCompatActivity {

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
                onBackPressed();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.MylessonRecyclerView01);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyLessonsActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<MyLessonTitle> LessonTitle = new ArrayList<>();
        LessonTitle.add(new MyLessonTitle("Java institute01"));
        LessonTitle.add(new MyLessonTitle("Java institute02"));
        LessonTitle.add(new MyLessonTitle("Java institute03"));
        LessonTitle.add(new MyLessonTitle("Java institute04"));
        LessonTitle.add(new MyLessonTitle("Java institute05"));
        LessonTitle.add(new MyLessonTitle("Java institute06"));

        MyLessonAdapter mylessonAdapter01 = new MyLessonAdapter(LessonTitle);
        recyclerView.setAdapter(mylessonAdapter01);

    }
}

class MyLessonTitle {
    String myLessontitleName;
    public MyLessonTitle(String name ) {
        this.myLessontitleName = name;

    }
}
class MyLessonAdapter extends RecyclerView.Adapter<MyLessonAdapter.MyLessonViewHolder> {
    private final List<MyLessonTitle> mylessondetails;
    public MyLessonAdapter(List<MyLessonTitle> myLessondetails) {

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

        MyLessonTitle myLessondetailss = mylessondetails.get(position);
        holder.myLessonName.setText(myLessondetailss.myLessontitleName);

        String mylessonName01 = myLessondetailss.myLessontitleName;
        holder.myLessonbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(holder.itemView.getContext(), LessonSuccessActivity.class);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mylessondetails.size();
    }
}