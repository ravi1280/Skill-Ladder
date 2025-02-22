package com.example.skill_ladder.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.skill_ladder.R;
import com.example.skill_ladder.model.customAlert;
import com.example.skill_ladder.model.showCustomToast;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdminDashboardFragment extends Fragment {
    PieChart pieChart01;
    ArrayList<PieEntry> pieEntryArrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

         pieChart01 = view.findViewById(R.id.adminpieChart01);
         pieEntryArrayList = new ArrayList<>();
         loadUsersCount();
        loadLessonsCount();
        loadCompanyCount();
        loadJobTitlesCount();
        loadJobFieldsCount();

        return view;
    }

    private void loadUsersCount() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("user")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    long userCount = queryDocumentSnapshots.size();
                    pieEntryArrayList.add(new PieEntry(userCount, "Uesrs"));
                    updatePieChart();

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showCustomToast.showToast(getContext(), "Error To load User Data !", R.drawable.cancel);
                    }
                });

    }
    private void loadLessonsCount() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("lessons")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    long lessonsCount = queryDocumentSnapshots.size();
                    pieEntryArrayList.add(new PieEntry(lessonsCount, "Lessons"));
                    updatePieChart();

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showCustomToast.showToast(getContext(), "Error To load lessons Data !", R.drawable.cancel);
                    }
                });

    }
    private void loadCompanyCount() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("company")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    long CompanyCount = queryDocumentSnapshots.size();
                    pieEntryArrayList.add(new PieEntry(CompanyCount, "Company"));
                    updatePieChart();

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showCustomToast.showToast(getContext(), "Error To load Company Data !", R.drawable.cancel);
                    }
                });

    }
    private void loadJobTitlesCount() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("JobTitles")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    long JobTitlesCount = queryDocumentSnapshots.size();
                    pieEntryArrayList.add(new PieEntry(JobTitlesCount, "Job Titles"));
                    updatePieChart();

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showCustomToast.showToast(getContext(), "Error To load Job Titles Data !", R.drawable.cancel);
                    }
                });

    }
    private void loadJobFieldsCount() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("JobFields")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    long JobFieldsCount = queryDocumentSnapshots.size();
                    pieEntryArrayList.add(new PieEntry(JobFieldsCount, "Job Fields"));
                    updatePieChart();

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showCustomToast.showToast(getContext(), "Error To load Job Fields Data !", R.drawable.cancel);
                    }
                });

    }
    private void updatePieChart() {
        PieDataSet pieDataSet = new PieDataSet(pieEntryArrayList, "Data");

        ArrayList<Integer> colorArrayList = new ArrayList<>();
        colorArrayList.add(getResources().getColor(R.color.chart01));
        colorArrayList.add(getResources().getColor(R.color.chart02));
        colorArrayList.add(getResources().getColor(R.color.chart03));
        colorArrayList.add(getResources().getColor(R.color.chart04));
        colorArrayList.add(getResources().getColor(R.color.chart05));

        pieDataSet.setColors(colorArrayList);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(15);
        pieData.setValueTextColor(getResources().getColor(R.color.white));

        pieChart01.setData(pieData);
        pieChart01.animateY(3000, Easing.EaseInCirc);
        pieChart01.setCenterText("Information");
        pieChart01.setCenterTextColor(getResources().getColor(R.color.UserLoginBtn));

        pieChart01.getDescription().setEnabled(false);
        pieChart01.invalidate(); // Refresh chart
    }
}
