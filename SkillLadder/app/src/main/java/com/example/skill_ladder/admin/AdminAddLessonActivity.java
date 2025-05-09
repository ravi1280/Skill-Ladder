package com.example.skill_ladder.admin;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.skill_ladder.R;
import com.example.skill_ladder.model.JobField;
import com.example.skill_ladder.model.JobTitle;
import com.example.skill_ladder.model.Lesson;
import com.example.skill_ladder.model.SubTopic;
import com.example.skill_ladder.model.showCustomToast;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdminAddLessonActivity extends AppCompatActivity {

    private EditText  lessonNameEditText,lessonPriceEditText, subTopicNameEditText, contentTextEditText, webUrlEditText, ytVideoUrlEditText;
    private Button addSubTopicButton, saveLessonButton;
    private Spinner SpjobField, SpjobTitle ;
    private List<SubTopic> subTopics = new ArrayList<>();

    private String SelectedJobFiledID,SelectedJobFieldName,SelectedJobTitleName;
    private FirebaseFirestore db;
    private static final int SHAKE_THRESHOLD = 800;
    private long lastShakeTime = 0;
    private float lastX = 0, lastY = 0, lastZ = 0;

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


        SpjobField = findViewById(R.id.JobFieldSpinner);
        SpjobTitle = findViewById(R.id.JobtitleSpinner);


        lessonNameEditText = findViewById(R.id.lessonNameEditText);
        subTopicNameEditText = findViewById(R.id.subTopicNameEditText);
        contentTextEditText = findViewById(R.id.contentTextEditTextMulty);
        webUrlEditText = findViewById(R.id.webUrlEditText);
        ytVideoUrlEditText = findViewById(R.id.ytVideoUrlEditText);
        lessonPriceEditText = findViewById(R.id.lessonPriceEditText);



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

        loadJobFieldSpinner();
        loadJobTitleSpinner();

        ImageView back = findViewById(R.id.AddLessonBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sensors();
    }
    private void sensors() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        SensorEventListener sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float[] values = event.values;
                float x = values[0];
                float y = values[1];
                float z = values[2];

                float X = x - lastX;
                float Y = y - lastY;
                float Z = z - lastZ;

                float shake = X * X + Y * Y + Z * Z;

                if (shake > SHAKE_THRESHOLD && (System.currentTimeMillis() - lastShakeTime) > 500) {

                    showCustomToast.showToast(AdminAddLessonActivity.this, "Refresh Field !", R.drawable.checked);
                    if (lessonNameEditText.getText().toString().isEmpty() && subTopicNameEditText.getText().toString().isEmpty() && contentTextEditText.getText().toString().isEmpty() && webUrlEditText.getText().toString().isEmpty() && ytVideoUrlEditText.getText().toString().isEmpty() && lessonPriceEditText.getText().toString().isEmpty()) {
                        return;
                    }
                    lessonNameEditText.setText("");
                    subTopicNameEditText.setText("");
                    contentTextEditText.setText("");
                    webUrlEditText.setText("");
                    ytVideoUrlEditText.setText("");
                    lessonPriceEditText .setText("");

                    lastShakeTime = System.currentTimeMillis();
                }
                lastX = x;
                lastY = y;
                lastZ = z;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_UI);
    }
    private void loadJobFieldSpinner(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("JobFields")
                .whereEqualTo("isActive", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<JobField> jobFieldList = new ArrayList<>();
                    List<String> fieldNames = new ArrayList<>();

                    jobFieldList.add(new JobField("", "Select Field ---", true));
                    fieldNames.add("Select Field ---");

                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        JobField jobField = document.toObject(JobField.class);
                        if (jobField != null) {
                            jobField.setId(document.getId());
                            jobFieldList.add(jobField);
                            fieldNames.add(jobField.getName());
                        }
                    }

                    updateFieldSpinner(fieldNames, jobFieldList);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error fetching job fields", e);
                });

    }

    private void updateFieldSpinner(List<String> fieldNames, List<JobField> jobFieldList) {


        ArrayAdapter<String> adapter = new ArrayAdapter<>(AdminAddLessonActivity.this,android.R.layout.simple_spinner_item, fieldNames);
        adapter.setDropDownViewResource(R.layout.coustom_spinner_dropdown);
        SpjobField.setAdapter(adapter);


        SpjobField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JobField selectedField = jobFieldList.get(position);
                Log.d("Spinner", "Selected Job Field ID: " + selectedField.getId());
                SelectedJobFiledID = selectedField.getId();
                SelectedJobFieldName =selectedField.getName();

                loadJobTitleSpinner();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadJobTitleSpinner(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("JobTitles")
                .whereEqualTo("fieldId", SelectedJobFiledID)
                .whereEqualTo("isActive", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<JobTitle> JobTitleList = new ArrayList<>();
                    List<String> titleNames = new ArrayList<>();

//                    JobTitleList.add(new JobTitle("", "Select Field ---", true));
//                    fieldNames.add("Select Field ---");

                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        JobTitle jobTitle = document.toObject(JobTitle.class);
                        if (jobTitle != null) {
                            jobTitle.setId(document.getId());

                            JobTitleList.add(jobTitle);
                            titleNames.add(jobTitle.getName());
                        }
                    }

                    updateTitleSpinner(titleNames, JobTitleList);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error fetching job fields", e);
                });

    }

    private void updateTitleSpinner(List<String> titleNames, List<JobTitle> JobTitleList) {


        ArrayAdapter<String> adapter = new ArrayAdapter<>(AdminAddLessonActivity.this,android.R.layout.simple_spinner_item, titleNames);
        adapter.setDropDownViewResource(R.layout.coustom_spinner_dropdown);
        SpjobTitle.setAdapter(adapter);


        SpjobTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (JobTitleList.isEmpty() || position < 0 || position >= JobTitleList.size()) {
                    return;
                }
                JobTitle selectedTitle = JobTitleList.get(position);
                Log.d("Spinner", "Selected Job Field ID: " + selectedTitle.getId());
//                SelectedJobFiledID = selectedTitle.getId();
                SelectedJobTitleName =selectedTitle.getName();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void addSubTopic() {
        String subTopicName = subTopicNameEditText.getText().toString().trim();
        String contentText = contentTextEditText.getText().toString().trim();
        String webUrl = webUrlEditText.getText().toString().trim();
        String ytVideoUrl = ytVideoUrlEditText.getText().toString().trim();


        if (subTopicName.isEmpty() || contentText.isEmpty() || webUrl.isEmpty() || ytVideoUrl.isEmpty()) {
            showCustomToast.showToast(AdminAddLessonActivity.this, "Please fill in all fields !", R.drawable.cancel);

            return;
        }

        SubTopic newSubTopic = new SubTopic(subTopicName, contentText, webUrl, ytVideoUrl);
        subTopics.add(newSubTopic);
        subTopicNameEditText.setText("");
        contentTextEditText.setText("");
        webUrlEditText.setText("");
        ytVideoUrlEditText.setText("");

        showCustomToast.showToast(AdminAddLessonActivity.this, "Subtopic added !", R.drawable.checked);
    }

    private void saveLessonToFirestore() {

        String lessonName = lessonNameEditText.getText().toString().trim();
        String lessonPrice = lessonPriceEditText.getText().toString().trim();

        if (lessonName.isEmpty() || lessonPrice.isEmpty() || subTopics.isEmpty()) {
            showCustomToast.showToast(AdminAddLessonActivity.this, "Please fill in all fields !", R.drawable.cancel);

            return;
        }
        int lessonPrice01;
        try {
            lessonPrice01 = Integer.parseInt(lessonPrice);
        } catch (NumberFormatException e) {
            showCustomToast.showToast(AdminAddLessonActivity.this, "Invalid price! Please enter a valid number !", R.drawable.cancel);
            return;
        }

        Lesson newLesson = new Lesson(SelectedJobFieldName, SelectedJobTitleName, lessonName, lessonPrice01, subTopics, true);

        db.collection("lessons")
                .add(newLesson)
                .addOnSuccessListener(newDocumentReference -> {
                    Log.d("Firestore", "DocumentSnapshot added with ID: " + newDocumentReference.getId());
                    showCustomToast.showToast(AdminAddLessonActivity.this, "Lesson added !", R.drawable.checked);

                    SpjobField.setSelection(0);
                    SpjobTitle.setSelection(0);
                    lessonNameEditText.setText("");
                    lessonPriceEditText.setText("");
                    subTopics.clear();
                })
                .addOnFailureListener(e -> {
                    showCustomToast.showToast(AdminAddLessonActivity.this, "Error adding lesson", R.drawable.cancel);

                });
    }

}