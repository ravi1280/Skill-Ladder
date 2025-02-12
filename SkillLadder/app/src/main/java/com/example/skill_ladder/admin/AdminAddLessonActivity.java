package com.example.skill_ladder.admin;

import android.os.Bundle;
import android.util.Log;
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
//    private Spinner jobFieldSpinner;
//    private OkHttpClient client;
//    private static final String API_URL = "https://special-lamprey-charmed.ngrok-free.app/Skill-Ladder/LoadJobFields";
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

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Add Subtopic on Button Click
        addSubTopicButton.setOnClickListener(v -> addSubTopic());

        // Save Lesson on Button Click
        saveLessonButton.setOnClickListener(v -> saveLessonToFirestore());

    }

//    private void loadJobFields() {
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OkHttpClient client = new OkHttpClient();
//                Request request = new Request.Builder()
//                        .url(API_URL)  // Set the API endpoint URL
//                        .build();
//
//                try {
//                    // Execute the request synchronously
//                    Response response = client.newCall(request).execute();
//
//                    // Check if the response was successful (HTTP 200 OK)
//                    if (!response.isSuccessful()) {
//                        Log.e("JobFieldError", "Server response error: " + response.code());
//                        runOnUiThread(() -> {
//                            Toast.makeText(AdminAddLessonActivity.this, "Error loading job fields. Server responded with: " + response.code(), Toast.LENGTH_SHORT).show();
//                        });
//                        return; // Stop further processing if response is not successful
//                    }
//
//                    // Use try-with-resources to safely handle the response body
//                    try (ResponseBody responseBody = response.body()) {
//                        if (responseBody == null) {
//                            // Log error and notify the user if the response body is null
//                            Log.e("JobFieldError", "Response body is null");
//                            runOnUiThread(() -> {
//                                Toast.makeText(AdminAddLessonActivity.this, "Received empty response from server.", Toast.LENGTH_SHORT).show();
//                            });
//                            return;
//                        }
//
//                        // Read response data as a string
//                        String responseData = responseBody.string();
//                        List<String> jobFields = new ArrayList<>();
//
//                        // Parse JSON array from response
//                        JSONArray jsonArray = new JSONArray(responseData);
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jobFieldObj = jsonArray.getJSONObject(i);
//                            // Extract "name" field from JSON object, defaulting to "Unknown" if missing
//                            String fieldName = jobFieldObj.optString("name", "Unknown");
//                            jobFields.add(fieldName);
//                        }
//
//                        // Update UI with the retrieved job fields
//                        runOnUiThread(() -> {
//                            if (jobFields.isEmpty()) {
//                                // Show a message if no job fields were found
//                                Toast.makeText(AdminAddLessonActivity.this, "No job fields available.", Toast.LENGTH_SHORT).show();
//                            } else {
//                                // Populate the Spinner with job fields
//                                ArrayAdapter<String> adapter = new ArrayAdapter<>(AdminAddLessonActivity.this, android.R.layout.simple_spinner_dropdown_item, jobFields);
//                                jobFieldSpinner.setAdapter(adapter);
//                            }
//                        });
//
//                    } catch (JSONException e) {
//                        // Log and notify the user if JSON parsing fails
//                        Log.e("JobFieldError", "JSON Parsing error: " + e.getMessage(), e);
//                        runOnUiThread(() -> {
//                            Toast.makeText(AdminAddLessonActivity.this, "Error processing job fields data.", Toast.LENGTH_SHORT).show();
//                        });
//                    }
//
//                } catch (IOException e) {
//                    // Log error if the network request fails
//                    Log.e("JobFieldError", "Network request failed: " + e.getMessage(), e);
//                    runOnUiThread(() -> {
//                        Toast.makeText(AdminAddLessonActivity.this, "Failed to load job fields. Check your internet connection.", Toast.LENGTH_SHORT).show();
//                    });
//                }
//            }
//        }).start();
//
//    }

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
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error saving lesson", Toast.LENGTH_SHORT).show();
                    Log.w("MainActivity", "Error saving lesson", e);
                });
    }

}