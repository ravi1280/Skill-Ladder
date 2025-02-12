package com.example.skill_ladder.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.skill_ladder.R;
import com.example.skill_ladder.model.Lesson;
import com.example.skill_ladder.model.SubTopic;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdminAddLessonActivity extends AppCompatActivity {

    private EditText jobFieldEditText, jobTitleEditText, lessonNameEditText, subTopicNameEditText, contentTextEditText, webUrlEditText, ytVideoUrlEditText, StatusEditText01;
    private Button addSubTopicButton, saveLessonButton;
    private List<SubTopic> subTopics = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_add_lesson);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

// Initialize Views
        jobFieldEditText = findViewById(R.id.jobFieldEditText);
        jobTitleEditText = findViewById(R.id.jobTitleEditText);
        lessonNameEditText = findViewById(R.id.lessonNameEditText);
        subTopicNameEditText = findViewById(R.id.subTopicNameEditText);
        contentTextEditText = findViewById(R.id.contentTextEditText);
        webUrlEditText = findViewById(R.id.webUrlEditText);
        ytVideoUrlEditText = findViewById(R.id.ytVideoUrlEditText);

        StatusEditText01 = findViewById(R.id.StatusEditText01);
        StatusEditText01.setText("Active");
        StatusEditText01.setEnabled(false);

        addSubTopicButton = findViewById(R.id.addSubTopicButton);
        saveLessonButton = findViewById(R.id.saveLessonButton);

        db = FirebaseFirestore.getInstance();
        addSubTopicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSubTopic();
            }
        });
        saveLessonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLessonToFirestore();
            }
        });

    }

    private void addSubTopic() {
        String subTopicName = subTopicNameEditText.getText().toString().trim();
        String contentText = contentTextEditText.getText().toString().trim();
        String webUrl = webUrlEditText.getText().toString().trim();
        String ytVideoUrl = ytVideoUrlEditText.getText().toString().trim();

        // Basic validation
        if (subTopicName.isEmpty() || contentText.isEmpty() || webUrl.isEmpty() || ytVideoUrl.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create and add new subtopic to the list
        SubTopic newSubTopic = new SubTopic(subTopicName, contentText, webUrl, ytVideoUrl);
        subTopics.add(newSubTopic);

        // Clear input fields for the next subtopic
        subTopicNameEditText.setText("");
        contentTextEditText.setText("");
        webUrlEditText.setText("");
        ytVideoUrlEditText.setText("");

        Toast.makeText(this, "Subtopic added!", Toast.LENGTH_SHORT).show();
    }

    private void saveLessonToFirestore() {
        String jobField = jobFieldEditText.getText().toString().trim();
        String jobTitle = jobTitleEditText.getText().toString().trim();
        String lessonName = lessonNameEditText.getText().toString().trim();
        String lessonStatus = StatusEditText01.getText().toString().trim();

        // Validate inputs
        if (jobField.isEmpty() || jobTitle.isEmpty() || lessonName.isEmpty() || subTopics.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create lesson object
        Lesson lesson = new Lesson(jobField, jobTitle, lessonName, lessonStatus, subTopics);

        // Add lesson to Firestore
        DocumentReference lessonRef = db.collection("lessons").document(); // Auto-generate a unique ID
        lessonRef.set(lesson)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Lesson saved successfully!", Toast.LENGTH_SHORT).show();
                    Log.d("MainActivity", "Lesson saved to Firestore!");

                     jobFieldEditText.setText("");
                     jobTitleEditText.setText("");
                     lessonNameEditText.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error saving lesson", Toast.LENGTH_SHORT).show();
                    Log.w("MainActivity", "Error saving lesson", e);
                });
    }

}