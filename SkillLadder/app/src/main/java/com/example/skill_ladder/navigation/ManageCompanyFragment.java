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


public class ManageCompanyFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_manage_company, container, false);
        RecyclerView ManageUserRecyclerView = view.findViewById(R.id.manageCompanyRV01);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ManageUserRecyclerView.setLayoutManager(linearLayoutManager);

        List<ManageCompany> companyDetails = new ArrayList<>();
        companyDetails.add(new ManageCompany("Software Engineer","Active"));
        companyDetails.add(new ManageCompany("Software Engineer","Active"));
        companyDetails.add(new ManageCompany("Software Engineer","Active"));
        companyDetails.add(new ManageCompany("Software Engineer","Deactive"));
        companyDetails.add(new ManageCompany("Software Engineer","Active"));
        companyDetails.add(new ManageCompany("Software Engineer","Active"));
        companyDetails.add(new ManageCompany("Software Engineer","Active"));
        companyDetails.add(new ManageCompany("Software Engineer","Active"));
        companyDetails.add(new ManageCompany("Software Engineer","Active"));

        MCompanyListAdapter companyListAdapter = new MCompanyListAdapter(companyDetails);
        ManageUserRecyclerView.setAdapter(companyListAdapter);
        return view;
    }
}

class ManageCompany {
    String CompanyName;
    String Status;
    public ManageCompany(String Cname,String status ) {

        this.CompanyName = Cname;
        this.Status = status;
    }
}

class MCompanyListAdapter extends RecyclerView.Adapter<MCompanyListAdapter.MCompanyViewHolder> {
    private final List<ManageCompany> mcompanydetails;

    public MCompanyListAdapter(List<ManageCompany> cdetails) {
        this.mcompanydetails = cdetails;
    }

    static class MCompanyViewHolder extends RecyclerView.ViewHolder {

        TextView CompanyName;
        Button active;
        View ContainerView;
        public MCompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            CompanyName = itemView.findViewById(R.id.ManageCompanyTV01);
            active = itemView.findViewById(R.id.ManageCompanyBtn01);
            ContainerView = itemView;
        }
    }

    @NonNull
    @Override
    public MCompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.manage_company_item, parent, false);
        return new MCompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MCompanyViewHolder holder, int position) {

        ManageCompany CDetails = mcompanydetails.get(position);
        holder.CompanyName.setText(CDetails.CompanyName);
        holder.active.setText(CDetails.Status);
        String Status = CDetails.Status.toString();

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
        return mcompanydetails.size();
    }
}