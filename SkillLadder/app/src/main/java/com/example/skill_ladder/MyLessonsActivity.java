package com.example.skill_ladder;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.skill_ladder.model.SQLiteHelper;
import com.example.skill_ladder.model.showCustomToast;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
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

        List<String> lessonIdsSQLite = getLessonIdsFromSQLite();
        if (!lessonIdsSQLite.isEmpty()) {
                    loadMylesson(lessonIdsSQLite);
        }else {
            showCustomToast.showToast(MyLessonsActivity.this,"No Lessons !",R.drawable.cancel);
        }

    }
    private List<String> getLessonIdsFromSQLite(){
        List<String> lessonIds = new ArrayList<>();
        SQLiteHelper sqLiteHelper = new SQLiteHelper(
                MyLessonsActivity.this,
                "lessonProgress.db",
                null,
                1
        );

                SQLiteDatabase sqLiteDatabase = sqLiteHelper.getReadableDatabase();
                Cursor  cursor =sqLiteDatabase.query(
                        "MyLessonProgress",
                        new String[]{"lesson_id"},
                        null,
                        null,
                        null,
                        null,
                        null);
                if (cursor.moveToFirst()) {
                    do {
                        int columnIndex = cursor.getColumnIndex("lesson_id");
                        if (columnIndex != -1) {
                            lessonIds.add(cursor.getString(columnIndex));
                        }
                    } while (cursor.moveToNext());
                }
                cursor.close();
                sqLiteDatabase.close();

                return lessonIds;

    }
    private void loadMylesson(List<String> lessonIds){

        List<Lesson> lessonList = new ArrayList<>();

        if (lessonIds.isEmpty()) {
            showCustomToast.showToast(MyLessonsActivity.this, "No lessons found in SQLite!", R.drawable.cancel);
            return;
        }
        List<List<String>> lessonBatches = splitList(lessonIds, 10);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for (List<String> batch : lessonBatches) {
            db.collection("lessons")
                    .whereIn(FieldPath.documentId(), batch)
                    .whereEqualTo("active", true)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        myLessonTitle.clear();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String id = documentSnapshot.getId();
                            String lessonName = documentSnapshot.getString("lessonName");
                            Integer lessonPrice = documentSnapshot.getLong("price").intValue();
                            boolean isActive = Boolean.TRUE.equals(documentSnapshot.getBoolean("active"));

                            myLessonTitle.add(new Lesson(id, lessonName, lessonPrice, isActive));
                        }
                        mylessonAdapter01.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        showCustomToast.showToast(MyLessonsActivity.this, "Failed to load lessons!", R.drawable.cancel);
                    });

        }
    }
    private List<List<String>> splitList(List<String> list, int chunkSize) {
        List<List<String>> chunks = new ArrayList<>();
        for (int i = 0; i < list.size(); i += chunkSize) {
            chunks.add(list.subList(i, Math.min(list.size(), i + chunkSize)));
        }
        return chunks;
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