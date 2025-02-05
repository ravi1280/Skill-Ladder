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


public class ManageUsersFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_manage_users, container, false);

        RecyclerView ManageUserRecyclerView = view.findViewById(R.id.manageUserRV01);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ManageUserRecyclerView.setLayoutManager(linearLayoutManager);

        List<User> userDetails = new ArrayList<>();
        userDetails.add(new User("Software Engineer","Active"));
        userDetails.add(new User("Software Engineer","Active"));
        userDetails.add(new User("Software Engineer","Active"));
        userDetails.add(new User("Software Engineer","Deactive"));
        userDetails.add(new User("Software Engineer","Active"));
        userDetails.add(new User("Software Engineer","Active"));
        userDetails.add(new User("Software Engineer","Active"));
        userDetails.add(new User("Software Engineer","Active"));
        userDetails.add(new User("Software Engineer","Active"));

        UserListAdapter jobListAdapter = new UserListAdapter(userDetails);
        ManageUserRecyclerView.setAdapter(jobListAdapter);

        return view;
    }
}
class User {
    String UserName;
    String Status;
    public User(String name,String status ) {

        this.UserName = name;
        this.Status = status;
    }
}
class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {
    private final List<User> userdetails;

    public UserListAdapter(List<User> udetails) {
        this.userdetails = udetails;
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView UserName;
        Button active;
        View ContainerView;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            UserName = itemView.findViewById(R.id.manageUserItemTv01);
            active = itemView.findViewById(R.id.manageUserBtn01);
            ContainerView = itemView;
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.manage_user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        User UDetails = userdetails.get(position);
        holder.UserName.setText(UDetails.UserName);
        holder.active.setText(UDetails.Status);
        String Status = UDetails.Status.toString();

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
        return userdetails.size();
    }
}