package com.example.skill_ladder.navigation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.skill_ladder.R;

import java.util.ArrayList;
import java.util.List;

public class ManageLessonsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_manage_lessons, container, false);

        RecyclerView ManageUserRecyclerView = view.findViewById(R.id.ManageLessonRV01);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ManageUserRecyclerView.setLayoutManager(linearLayoutManager);

        List<Lesson> lDetails = new ArrayList<>();
        lDetails.add(new Lesson("Software Engineer","Active"));
        lDetails.add(new Lesson("Software Engineer","Active"));
        lDetails.add(new Lesson("Software Engineer","Active"));
        lDetails.add(new Lesson("Software Engineer","Deactive"));
        lDetails.add(new Lesson("Software Engineer","Active"));
        lDetails.add(new Lesson("Software Engineer","Active"));
        lDetails.add(new Lesson("Software Engineer","Active"));
        lDetails.add(new Lesson("Software Engineer","Active"));
        lDetails.add(new Lesson("Software Engineer","Active"));

        mLessonListAdapter jobListAdapter = new mLessonListAdapter(lDetails);
        ManageUserRecyclerView.setAdapter(jobListAdapter);

        return view;
    }
}
class Lesson {
    String LessonName;
    String Status;
    public Lesson(String name,String status ) {

        this.LessonName = name;
        this.Status = status;
    }
}
class mLessonListAdapter extends RecyclerView.Adapter<mLessonListAdapter.mLessonViewHolder> {
    private final List<Lesson> lessondetails;

    public mLessonListAdapter(List<Lesson> ldetails) {
        this.lessondetails = ldetails;
    }

    static class mLessonViewHolder extends RecyclerView.ViewHolder {

        TextView LessonName;
        Button active;
        View ContainerView;
        public mLessonViewHolder(@NonNull View itemView) {
            super(itemView);
            LessonName = itemView.findViewById(R.id.ManageLessonTV01);
            active = itemView.findViewById(R.id.ManageLessonBtn01);
            ContainerView = itemView;
        }
    }

    @NonNull
    @Override
    public mLessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.manage_lesson_item, parent, false);
        return new mLessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mLessonViewHolder holder, int position) {

        Lesson lDetails = lessondetails.get(position);
        holder.LessonName.setText(lDetails.LessonName);
        holder.active.setText(lDetails.Status);
        String Status = lDetails.Status.toString();

        holder.active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Status.equals("Active")){
                    holder.active.setText("Deactive");
                }else {
                    holder.active.setText("Active");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lessondetails.size();
    }
}