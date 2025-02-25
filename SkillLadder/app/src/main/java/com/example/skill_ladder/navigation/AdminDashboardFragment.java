package com.example.skill_ladder.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
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
    TextView c01,c02,c03,c04;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        c01 = view.findViewById(R.id.AdminDashBoardC01);
        c02 = view.findViewById(R.id.AdminDashBoardC02);
        c03 = view.findViewById(R.id.AdminDashBoardC03);
        c04 = view.findViewById(R.id.AdminDashBoardC04);

         pieChart01 = view.findViewById(R.id.adminpieChart01);
         pieEntryArrayList = new ArrayList<>();
         loadUsersCount();
        loadLessonsCount();
        loadCompanyCount();
        loadJobTitlesCount();
        loadJobFieldsCount();
        loadjobsCount();

        ConstraintLayout constraintLayout01 = view.findViewById(R.id.AdminDashBoadConstraint01);
        ConstraintLayout constraintLayout02 = view.findViewById(R.id.AdminDashBoadConstraint02);

        SpringAnimation springAnimation01 = new SpringAnimation(constraintLayout01, DynamicAnimation.TRANSLATION_X, 0);
        SpringForce springForce01 = new SpringForce();
        springForce01.setStiffness(SpringForce.STIFFNESS_VERY_LOW);
        springForce01.setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);
        springForce01.setFinalPosition(0);
        constraintLayout01.setTranslationX(-1000);
        springAnimation01.setSpring(springForce01);
        constraintLayout01.postDelayed(() -> springAnimation01.start(), 400);

        SpringAnimation springAnimation02 = new SpringAnimation(constraintLayout02, DynamicAnimation.TRANSLATION_X, 0);
        SpringForce springForce02 = new SpringForce();
        springForce02.setStiffness(SpringForce.STIFFNESS_VERY_LOW);
        springForce02.setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);
        springForce02.setFinalPosition(0);
        constraintLayout02.setTranslationX(1000);
        springAnimation02.setSpring(springForce02);
        constraintLayout02.postDelayed(() -> springAnimation02.start(), 400);


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
                    c01.setText(String.valueOf(userCount));

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
                    c02.setText(String.valueOf(lessonsCount));

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
                    c03.setText(String.valueOf(CompanyCount));

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
    private void loadjobsCount() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("jobs")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    long JobCount = queryDocumentSnapshots.size();
                    pieEntryArrayList.add(new PieEntry(JobCount, "Jobs"));
                    updatePieChart();
                    c04.setText(String.valueOf(JobCount));

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
        colorArrayList.add(getResources().getColor(R.color.chart03));

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
