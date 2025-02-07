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

public class ManageOtherFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_other, container, false);


        RecyclerView ManageOtherFieldRV = view.findViewById(R.id.ManageOtherRecyclerView01);
        LinearLayoutManager linearLayoutManager01 = new LinearLayoutManager(requireActivity());
        linearLayoutManager01.setOrientation(LinearLayoutManager.HORIZONTAL);
        ManageOtherFieldRV.setLayoutManager(linearLayoutManager01);


        RecyclerView ManageOtherTitledRV = view.findViewById(R.id.ManageOtherRecyclerView02);
        LinearLayoutManager linearLayoutManager02 = new LinearLayoutManager(requireActivity());
        linearLayoutManager02.setOrientation(LinearLayoutManager.HORIZONTAL);
        ManageOtherTitledRV.setLayoutManager(linearLayoutManager02);

        List<Field> fieldDetails = new ArrayList<>();
        fieldDetails.add(new Field("Management","Active"));
        fieldDetails.add(new Field("Management","Active"));
        fieldDetails.add(new Field("Management","Deactive"));
        fieldDetails.add(new Field("Management","Active"));
        fieldDetails.add(new Field("Management","Deactive"));

        FieldListAdapter fieldListAdapter = new FieldListAdapter(fieldDetails);
        ManageOtherFieldRV.setAdapter(fieldListAdapter);

        List<Title> titleDetails = new ArrayList<>();
        titleDetails.add(new Title("Quality Assursance","Active"));
        titleDetails.add(new Title("Quality Assursance","Active"));
        titleDetails.add(new Title("Quality Assursance","Deactive"));
        titleDetails.add(new Title("Quality Assursance","Active"));
        titleDetails.add(new Title("Quality Assursance","Deactive"));

        TitleListAdapter titleListAdapter = new TitleListAdapter(titleDetails);
        ManageOtherTitledRV.setAdapter(titleListAdapter);
        return view;
    }
}

class Field {
    String FiledName;
    String FStatus;
    public Field(String name,String status ) {

        this.FiledName = name;
        this.FStatus = status;
    }
}

class FieldListAdapter extends RecyclerView.Adapter<FieldListAdapter.FieldViewHolder> {
    private final List<Field> fielddetails;

    public FieldListAdapter(List<Field> fdetails) {
        this.fielddetails = fdetails;
    }

    static class FieldViewHolder extends RecyclerView.ViewHolder {

        TextView fName;
        Button active;
        View ContainerView;
        public FieldViewHolder(@NonNull View itemView) {
            super(itemView);
            fName = itemView.findViewById(R.id.ManageFieldtextView01);
            active = itemView.findViewById(R.id.ManageFieldBtn);
            ContainerView = itemView;
        }
    }

    @NonNull
    @Override
    public FieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.manage_field_item, parent, false);
        return new FieldViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull FieldViewHolder holder, int position) {
        Field FDetails = fielddetails.get(position);
        holder.fName.setText(FDetails.FiledName);
        holder.active.setText(FDetails.FStatus);

        String Status = FDetails.FStatus.toString();

        holder.active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (FDetails.FStatus.equalsIgnoreCase("Active")) {
                    FDetails.FStatus = "Deactive";
                } else {
                    FDetails.FStatus = "Active";
                }

                // Update button text
                holder.active.setText(FDetails.FStatus);

                // Notify the adapter to refresh the view
                notifyItemChanged(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return fielddetails.size();
    }
}



class Title {
    String TitleName;
    String TStatus;
    public Title(String name,String status ) {

        this.TitleName = name;
        this.TStatus = status;
    }
}
class TitleListAdapter extends RecyclerView.Adapter<TitleListAdapter.TitleViewHolder> {
    private final List<Title> titledetails;

    public TitleListAdapter(List<Title> tdetails) {
        this.titledetails = tdetails;
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {

        TextView TitleName;
        Button active01;
        View ContainerView;
        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            TitleName = itemView.findViewById(R.id.manageJobTitleTextView01);
            active01 = itemView.findViewById(R.id.manageJobTitleButton1);
            ContainerView = itemView;
        }
    }

    @NonNull
    @Override
    public TitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.manage_job_title_item, parent, false);
        return new TitleViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TitleViewHolder holder, int position) {
        Title TDetails = titledetails.get(position);
        holder.TitleName.setText(TDetails.TitleName);
        holder.active01.setText(TDetails.TStatus);
//        String Status = TDetails.TStatus.toString();

        holder.active01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TDetails.TStatus.equalsIgnoreCase("Active")) {
                    TDetails.TStatus = "Deactive";
                } else {
                    TDetails.TStatus = "Active";
                }

                holder.active01.setText(TDetails.TStatus);
                notifyItemChanged(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return titledetails.size();
    }
}