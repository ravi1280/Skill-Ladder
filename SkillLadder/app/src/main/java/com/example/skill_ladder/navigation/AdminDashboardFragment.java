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
        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        // Find PieChart in the inflated layout
        PieChart pieChart01 = view.findViewById(R.id.adminpieChart01);

        // Create Pie Entries
        ArrayList<PieEntry> pieEntryArrayList = new ArrayList<>();
        pieEntryArrayList.add(new PieEntry(35, "Android"));
        pieEntryArrayList.add(new PieEntry(45, "iOS"));
        pieEntryArrayList.add(new PieEntry(15, "ReactNative"));
        pieEntryArrayList.add(new PieEntry(10, "Flutter"));
        pieEntryArrayList.add(new PieEntry(50, "Swift"));

        // Create PieDataSet
        PieDataSet pieDataSet = new PieDataSet(pieEntryArrayList, "Mobile Development");

        // Set Colors
        ArrayList<Integer> colorArrayList = new ArrayList<>();
        colorArrayList.add(getResources().getColor(R.color.chart01));
        colorArrayList.add(getResources().getColor(R.color.chart02));
        colorArrayList.add(getResources().getColor(R.color.chart03));
        colorArrayList.add(getResources().getColor(R.color.chart04));
        colorArrayList.add(getResources().getColor(R.color.chart05));
        pieDataSet.setColors(colorArrayList);

        // Create PieData
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(15);
        pieData.setValueTextColor(getResources().getColor(R.color.white));

        // Set Data to PieChart
        pieChart01.setData(pieData);
        pieChart01.animateY(3000, Easing.EaseInCirc);
        pieChart01.setCenterText("Information");
        pieChart01.setCenterTextColor(getResources().getColor(R.color.UserLoginBtn));

        // Remove Description
        pieChart01.getDescription().setEnabled(false);
        pieChart01.invalidate(); // Refresh Chart

        return view; // Return the view after everything is set up
    }
}
