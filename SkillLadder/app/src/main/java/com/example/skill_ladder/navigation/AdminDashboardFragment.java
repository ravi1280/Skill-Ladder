package com.example.skill_ladder.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.skill_ladder.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import java.util.ArrayList;

public class AdminDashboardFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        PieChart pieChart01 = view.findViewById(R.id.adminpieChart01);

        ArrayList<PieEntry> pieEntryArrayList = new ArrayList<>();
        pieEntryArrayList.add(new PieEntry(35, "Android"));
        pieEntryArrayList.add(new PieEntry(45, "iOS"));
        pieEntryArrayList.add(new PieEntry(15, "ReactNative"));
        pieEntryArrayList.add(new PieEntry(10, "Flutter"));
        pieEntryArrayList.add(new PieEntry(50, "Swift"));

        PieDataSet pieDataSet = new PieDataSet(pieEntryArrayList, "Mobile Development");

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
        pieChart01.invalidate(); // Refresh Chart
        return view;
    }
}
