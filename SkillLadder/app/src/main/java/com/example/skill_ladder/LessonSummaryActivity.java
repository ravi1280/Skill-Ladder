package com.example.skill_ladder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.example.skill_ladder.model.SubTopic;
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

        TextView lessonNameTV = findViewById(R.id.LessonSummeryTV01);
        lessonNameTV.setText(lessonName01);

        recyclerView01 = findViewById(R.id.LessonSummeryRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LessonSummaryActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView01.setLayoutManager(linearLayoutManager);
        subTopicsList = new ArrayList<>();
        lessonTopicListAdapter = new LessonTopicListAdapter(subTopicsList);
        recyclerView01.setAdapter(lessonTopicListAdapter);

        db = FirebaseFirestore.getInstance();
        getSubTopicsByLessonName(lessonName01);
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

                                // Now, subTopicsList contains all subtopics
                                displaySubTopics();
                                lessonTopicListAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        Toast.makeText(LessonSummaryActivity.this, "No lessons found with this name", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Log.e("Firebase", "Error fetching subtopics", e));
    }

    private void displaySubTopics() {
        for (SubTopic subTopic : subTopicsList) {
            Log.d("SubTopic", "Name: " + subTopic.getSubTopicName() + ", Content: " + subTopic.getContentText());
        }
    }


}


class LessonTopicListAdapter extends RecyclerView.Adapter<LessonTopicListAdapter.LessonTopicViewHolder> {
    private final List<SubTopic> lessonTopics;

    public LessonTopicListAdapter(List<SubTopic> lessonTopics) {
        this.lessonTopics = lessonTopics;
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
                view.getContext().startActivity(intent01);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lessonTopics.size();
    }
}
