package com.example.skill_ladder.navigation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skill_ladder.R;
import com.example.skill_ladder.model.Company;
import com.example.skill_ladder.model.JobField;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class ManageCompanyFragment extends Fragment {
    List<Company> companyDetails;
    MCompanyListAdapter companyListAdapter;
    RecyclerView ManageUserRecyclerView;
    EditText searchText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_manage_company, container, false);

        searchText = view.findViewById(R.id.manageCompanyeditText01);
        ImageView imageView = view.findViewById(R.id.manageCompanySearch);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = searchText.getText().toString();
                if(search.isEmpty()){
                    loadCompany();
                }else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("company")
                            .whereGreaterThanOrEqualTo("name", search)
                            .whereLessThanOrEqualTo("name", search + "\uf8ff")
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {

                                companyDetails.clear();
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    String id = documentSnapshot.getId();
                                    String name = documentSnapshot.getString("name");
                                    String mobile = documentSnapshot.getString("mobile");
                                    String email = documentSnapshot.getString("email");
                                    boolean isActive = Boolean.TRUE.equals(documentSnapshot.getBoolean("isActive"));

                                    companyDetails.add(new Company(id, name,mobile,email,isActive));
                                }
                                companyListAdapter.notifyDataSetChanged();

                            })
                            .addOnFailureListener(e -> {
                            });
                }
            }
        });

        ManageUserRecyclerView= view.findViewById(R.id.manageCompanyRV01);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ManageUserRecyclerView.setLayoutManager(linearLayoutManager);
        companyDetails = new ArrayList<>();
        companyListAdapter = new MCompanyListAdapter(companyDetails);
        ManageUserRecyclerView.setAdapter(companyListAdapter);

        loadCompany();
        return view;
    }

    private void loadCompany() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("company")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    companyDetails.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String id = documentSnapshot.getId();
                        String name = documentSnapshot.getString("name");
                        String mobile = documentSnapshot.getString("mobile");
                        String email = documentSnapshot.getString("email");
                        boolean isActive = Boolean.TRUE.equals(documentSnapshot.getBoolean("isActive"));

                        companyDetails.add(new Company(id, name,mobile,email,isActive));
                    }
                    companyListAdapter.notifyDataSetChanged();

                })
                .addOnFailureListener(e -> {
                });
    }
}



class MCompanyListAdapter extends RecyclerView.Adapter<MCompanyListAdapter.MCompanyViewHolder> {
    private final List<Company> mcompanydetails;

    public MCompanyListAdapter(List<Company> cdetails) {
        this.mcompanydetails = cdetails;
    }

    static class MCompanyViewHolder extends RecyclerView.ViewHolder {

        TextView CompanyName,CompanyMobile,CompanyEmail;
        Button active;
        View ContainerView;
        public MCompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            CompanyName = itemView.findViewById(R.id.ManageCompanyTV01);
            CompanyMobile = itemView.findViewById(R.id.ManageCompanyTV02);
            CompanyEmail = itemView.findViewById(R.id.ManageCompanyTV03);
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

        Company CDetails = mcompanydetails.get(position);
        holder.CompanyName.setText(CDetails.getName());
        holder.CompanyMobile.setText(CDetails.getMobile());
        holder.CompanyEmail.setText(CDetails.getEmail());
        holder.active.setText(CDetails.isActive() ? "Deactivate" : "Activate");
        if(CDetails.isActive()){
            holder.active.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.chart03));
        }else {
            holder.active.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.chart05));
        }

        holder.active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleCompanyStatus(CDetails, holder);

            }
        });
    }

    private void toggleCompanyStatus(Company company, MCompanyListAdapter.MCompanyViewHolder holder) {
        boolean newStatus = !company.isActive();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("company").document(company.getId())
                .update("isActive", newStatus)
                .addOnSuccessListener(aVoid -> {

                    company.setActive(newStatus);
                    holder.active.setText(newStatus ? "Deactivate" : "Activate");
                    if(holder.active.getText()=="Activate"){
                        holder.active.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.chart05));
                    }else {
                        holder.active.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.chart03));

                    }


                    Toast.makeText(holder.itemView.getContext(), "Status updated", Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(holder.itemView.getContext(), "Failed to update", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return mcompanydetails.size();
    }
}