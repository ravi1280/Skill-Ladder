package com.example.skill_ladder;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;

public class LessonSummaryActivity extends AppCompatActivity {

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

        //set Vertical Linearlayout for RecyclerView
        RecyclerView recyclerView = findViewById(R.id.LessonSummeryRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LessonSummaryActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

//        recyclerView.setLayoutManager(new LinearLayoutManager(LessonSummaryActivity.this));

        List<LessonTopic> lessonTopics = new ArrayList<>();
        lessonTopics.add(new LessonTopic("Introduction to Java", "5 Subtopics", R.drawable.circle));
        lessonTopics.add(new LessonTopic("Object-Oriented Programming", "4 Subtopics", R.drawable.circle));
        lessonTopics.add(new LessonTopic("Exception Handling", "3 Subtopics", R.drawable.circle));
        lessonTopics.add(new LessonTopic("Multithreading", "6 Subtopics", R.drawable.circle));
        lessonTopics.add(new LessonTopic("Android Development", "7 Subtopics", R.drawable.circle));
        lessonTopics.add(new LessonTopic("Android Development", "7 Subtopics", R.drawable.circle));
        lessonTopics.add(new LessonTopic("Android Development", "7 Subtopics", R.drawable.circle));
        lessonTopics.add(new LessonTopic("Android Development", "7 Subtopics", R.drawable.circle));
        lessonTopics.add(new LessonTopic("Android Development", "7 Subtopics", R.drawable.circle));
        lessonTopics.add(new LessonTopic("Android Development", "7 Subtopics", R.drawable.circle));

        LessonTopicListAdapter lessonTopicListAdapter = new LessonTopicListAdapter(lessonTopics);
        recyclerView.setAdapter(lessonTopicListAdapter);
    }
}

class LessonTopic {
    String topic;
    String subtopic;
    int iconResId;

    public LessonTopic(String topic, String subtopic, int iconResId) {
        this.topic = topic;
        this.subtopic = subtopic;
        this.iconResId = iconResId;
    }
}

class LessonTopicListAdapter extends RecyclerView.Adapter<LessonTopicListAdapter.LessonTopicViewHolder> {
    private final List<LessonTopic> lessonTopics;

    public LessonTopicListAdapter(List<LessonTopic> lessonTopics) {
        this.lessonTopics = lessonTopics;
    }

    static class LessonTopicViewHolder extends RecyclerView.ViewHolder {
        TextView topicView;
        TextView subtopicView;
        ImageView topicIcon;
        View ContainerView;

        public LessonTopicViewHolder(@NonNull View itemView) {
            super(itemView);
            topicIcon = itemView.findViewById(R.id.imgTopicIcon);
            topicView = itemView.findViewById(R.id.LessonContentTopicItem01);
            subtopicView = itemView.findViewById(R.id.LessonContentTopicItem02);
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

        LessonTopic lessonTopic = lessonTopics.get(position);
        holder.topicView.setText(lessonTopic.topic);
        holder.subtopicView.setText(lessonTopic.subtopic);
        holder.topicIcon.setImageResource(lessonTopic.iconResId);
        holder.ContainerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent01 = new Intent(view.getContext(),LessonContentActivity.class);
                intent01.putExtra("mainTopic",lessonTopic.topic);
                intent01.putExtra("subTopic",lessonTopic.subtopic);
                view.getContext().startActivity(intent01);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lessonTopics.size();
    }
}
