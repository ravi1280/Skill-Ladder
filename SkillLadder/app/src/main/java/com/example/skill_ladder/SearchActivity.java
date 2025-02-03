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

public class SearchActivity extends AppCompatActivity {

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
                onBackPressed();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.searchRecyclerView01);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<SearchLessonTitle> SearchLessonTitle = new ArrayList<>();
        SearchLessonTitle.add(new SearchLessonTitle("Java institute01"));
        SearchLessonTitle.add(new SearchLessonTitle("Java institute02"));
        SearchLessonTitle.add(new SearchLessonTitle("Java institute03"));
        SearchLessonTitle.add(new SearchLessonTitle("Java institute04"));
        SearchLessonTitle.add(new SearchLessonTitle("Java institute05"));
        SearchLessonTitle.add(new SearchLessonTitle("Java institute06"));

        SearchLessonAdapter SearchlessonAdapter01 = new SearchLessonAdapter(SearchLessonTitle);
        recyclerView.setAdapter(SearchlessonAdapter01);
    }
}

class SearchLessonTitle {
    String LessontitleName;
    public SearchLessonTitle(String name ) {
        this.LessontitleName = name;

    }
}

class SearchLessonAdapter extends RecyclerView.Adapter<SearchLessonAdapter.SearchLessonViewHolder> {
    private final List<SearchLessonTitle> Searchlessondetails;
    public SearchLessonAdapter(List<SearchLessonTitle> Searchlessondetails) {
        this.Searchlessondetails = Searchlessondetails;
    }

    static class SearchLessonViewHolder extends RecyclerView.ViewHolder {
        TextView LessonName;
        TextView LessonBtn;
        View ContainerView;

        public SearchLessonViewHolder(@NonNull View itemView) {
            super(itemView);
            LessonName = itemView.findViewById(R.id.ByLessonTitle01);
            LessonBtn= itemView.findViewById(R.id.courseBuyBtn01);
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

        SearchLessonTitle lessonDetails = Searchlessondetails.get(position);
        holder.LessonName.setText(lessonDetails.LessontitleName);

        String lessonName = lessonDetails.LessontitleName;
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
    }

    @Override
    public int getItemCount() {
        return Searchlessondetails.size();
    }
}