package com.example.skill_ladder;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import com.example.skill_ladder.model.SQLiteHelper;
import com.example.skill_ladder.model.SubTopic;
import com.example.skill_ladder.model.showCustomToast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LessonSummaryActivity extends AppCompatActivity {
    LessonTopicListAdapter lessonTopicListAdapter;
    RecyclerView recyclerView01;

     String lessonId;
    FirebaseFirestore db;
    List<SubTopic> subTopicsList ;
    TextView lessonNameTV,progress;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lesson_summary);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        lessonId = intent.getStringExtra("lessonId");
        String lessonName01 = intent.getStringExtra("lessonName");

        lessonNameTV = findViewById(R.id.LessonSummeryTV01);
        lessonNameTV.setText(lessonName01);

        progress = findViewById(R.id.LessonProgressDetails);
        progressBar = findViewById(R.id.progressBar01);

        recyclerView01 = findViewById(R.id.LessonSummeryRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LessonSummaryActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView01.setLayoutManager(linearLayoutManager);
        subTopicsList = new ArrayList<>();
        lessonTopicListAdapter = new LessonTopicListAdapter(subTopicsList,lessonId);
        recyclerView01.setAdapter(lessonTopicListAdapter);

        db = FirebaseFirestore.getInstance();
        getSubTopicsByLessonName(lessonName01);

        getProgress();
    }

    private void getProgress(){
        SQLiteHelper sqLiteHelper = new SQLiteHelper(
                LessonSummaryActivity.this,
                "lessonProgress.db",
                null,
                1
        );
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase sqLiteDatabase = sqLiteHelper.getReadableDatabase();
                Cursor cursor = sqLiteDatabase.query(
                        "MyLessonProgress",
                        new String[]{"lesson_progress"},
                        "lesson_id = ?",
                        new String[]{lessonId},
                        null,
                        null,
                        null
                );

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        int progressValue = 0;
                        if (cursor != null && cursor.moveToFirst()) {
                            progressValue = cursor.getInt(0);
                            cursor.close();
                        }
                        progressBar.setProgress(progressValue);
                        progressBar.setMax(100);
                        progress.setText(String.valueOf(progressValue)+"%");
                    }
                });
            }
        }).start();

    }

    private void getSubTopicsByLessonName(String lessonName) {

        db.collection("lessons")
                .whereEqualTo("lessonName", lessonName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        subTopicsList.clear();
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            List<Map<String, Object>> subTopics = (List<Map<String, Object>>) document.get("subTopics");

                            if (subTopics != null) {
                                for (Map<String, Object> subTopicMap : subTopics) {
                                    String name = (String) subTopicMap.get("subTopicName");
                                    String content = (String) subTopicMap.get("contentText");
                                    String webUrl = (String) subTopicMap.get("webUrl");
                                    String ytUrl = (String) subTopicMap.get("ytVideoUrl");

                                    SubTopic subTopic = new SubTopic(name, content, webUrl, ytUrl);
                                    subTopicsList.add(subTopic);
                                }


                                displaySubTopics();
                                lessonTopicListAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        showCustomToast.showToast(LessonSummaryActivity.this, "No lessons found with this name", R.drawable.cancel);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                                showCustomToast.showToast(LessonSummaryActivity.this, "Error fetching subtopics", R.drawable.cancel);
                    }
                });
    }

    private void displaySubTopics() {
        for (SubTopic subTopic : subTopicsList) {
            Log.d("SubTopic", "Name: " + subTopic.getSubTopicName() + ", Content: " + subTopic.getContentText());
        }
    }


}


class LessonTopicListAdapter extends RecyclerView.Adapter<LessonTopicListAdapter.LessonTopicViewHolder> {
    private final List<SubTopic> lessonTopics;
    private final String lessonId;

    public LessonTopicListAdapter(List<SubTopic> lessonTopics,String lessonId) {
        this.lessonTopics = lessonTopics;
        this.lessonId = lessonId;
    }

    static class LessonTopicViewHolder extends RecyclerView.ViewHolder {
        TextView topicView;

        View ContainerView;

        public LessonTopicViewHolder(@NonNull View itemView) {
            super(itemView);

            topicView = itemView.findViewById(R.id.LessonContentTopicItem01);
            ContainerView = itemView;
        }
    }

    @NonNull
    @Override
    public LessonTopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.lesson_content_topic_item, parent, false);
        return new LessonTopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonTopicViewHolder holder, int position) {

        SubTopic lessonTopic = lessonTopics.get(position);
        holder.topicView.setText(lessonTopic.getSubTopicName());

        holder.ContainerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent01 = new Intent(view.getContext(),LessonContentActivity.class);
                intent01.putExtra("mainTopic",lessonTopic.getSubTopicName());
                intent01.putExtra("ContentText",lessonTopic.getContentText());
                intent01.putExtra("WebUrl",lessonTopic.getWebUrl());
                intent01.putExtra("YtVideoUrl",lessonTopic.getYtVideoUrl());
                intent01.putExtra("lessonId",lessonId);


                int totalSubtopics = lessonTopics.size();
                int progressPercentage = ((position + 1) * 100) / totalSubtopics;

                intent01.putExtra("subtopic_progress", progressPercentage);


                if (position == lessonTopics.size() - 1) {
                    intent01.putExtra("is_last_subtopic", true);
                } else {
                    intent01.putExtra("is_last_subtopic", false);
                }

                view.getContext().startActivity(intent01);

            }
        });
    }

    @Override
    public int getItemCount() {
        return lessonTopics.size();
    }
}
