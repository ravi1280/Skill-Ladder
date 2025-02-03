package com.example.skill_ladder;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class JobTitleRelatedLessonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_title_related_lesson);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.relatedLessonReV01);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(JobTitleRelatedLessonActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<LessonTitle> LessonTitle = new ArrayList<>();
        LessonTitle.add(new LessonTitle("Java institute01"));
        LessonTitle.add(new LessonTitle("Java institute02"));
        LessonTitle.add(new LessonTitle("Java institute03"));
        LessonTitle.add(new LessonTitle("Java institute04"));
        LessonTitle.add(new LessonTitle("Java institute05"));
        LessonTitle.add(new LessonTitle("Java institute06"));

        LessonAdapter lessonAdapter01 = new LessonAdapter(LessonTitle);
        recyclerView.setAdapter(lessonAdapter01);
    }
}
class LessonTitle {
    String LessontitleName;
    public LessonTitle(String name ) {
        this.LessontitleName = name;

    }
}
class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {
    private final List<LessonTitle> lessondetails;
    public LessonAdapter(List<LessonTitle> jobdetails) {
        this.lessondetails = jobdetails;
    }

    static class LessonViewHolder extends RecyclerView.ViewHolder {
        TextView LessonName;
        View ContainerView;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            LessonName = itemView.findViewById(R.id.ByLessonTitle01);
            ContainerView = itemView;
        }
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.lesson_buy_item, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {

        LessonTitle lessonDetails = lessondetails.get(position);
        holder.LessonName.setText(lessonDetails.LessontitleName);

        String lessonName = lessonDetails.LessontitleName;
        holder.ContainerView.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public int getItemCount() {
        return lessondetails.size();
    }
}