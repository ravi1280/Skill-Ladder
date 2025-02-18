package com.example.skill_ladder.navigation;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.skill_ladder.model.User;
import com.example.skill_ladder.model.showCustomToast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ManageUsersFragment extends Fragment {
    RecyclerView ManageUserRecyclerView;
    List<User> userDetails;
    UserListAdapter userListAdapter;
    EditText searchtext ;
    ImageView search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_manage_users, container, false);



        ManageUserRecyclerView = view.findViewById(R.id.manageUserRV01);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ManageUserRecyclerView.setLayoutManager(linearLayoutManager);
        userDetails = new ArrayList<>();
        userListAdapter = new UserListAdapter(userDetails);
        ManageUserRecyclerView.setAdapter(userListAdapter);
        loadUsers();

        searchtext= view.findViewById(R.id.manageUsereditText01);
        search = view.findViewById(R.id.manageUserSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchtxt = searchtext.getText().toString();
                if(searchtxt.isEmpty()){
                    loadUsers();
                }else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("user")
                            .whereGreaterThanOrEqualTo("fullName", searchtxt)
                            .whereLessThanOrEqualTo("fullName", searchtxt + "\uf8ff")
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {

                                userDetails.clear();
                                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                    String userId =  doc.getId();
                                    String fullName =  doc.getString("fullName");
                                    String email =  doc.getString("email");
                                    String mobile =  doc.getString("mobile");
                                    boolean isActive = Boolean.TRUE.equals(doc.getBoolean("isActive"));

                                    userDetails.add(new User(userId,fullName,email,mobile,isActive));
                                }
                                userListAdapter.notifyDataSetChanged();

                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showCustomToast.showToast(requireActivity(),"Can't Load User Data !",R.drawable.cancel);
                                }
                            });

                }

            }
        });

        return view;
    }
    private void loadUsers(){

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("user").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                userDetails.clear();
                for(DocumentSnapshot doc:queryDocumentSnapshots){
                    String userId =  doc.getId();
                    String fullName =  doc.getString("fullName");
                    String email =  doc.getString("email");
                    String mobile =  doc.getString("mobile");
                    boolean isActive = Boolean.TRUE.equals(doc.getBoolean("isActive"));

                    userDetails.add(new User(userId,fullName,email,mobile,isActive));
                }
                userListAdapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showCustomToast.showToast(requireActivity(),"Can't load User Data !",R.drawable.cancel);

            }
        });
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
        holder.UserName.setText(UDetails.getFullName());
        holder.active.setText(UDetails.isActive() ? "Deactivate" : "Activate");
        if(UDetails.isActive()){
            holder.active.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.chart03));
        }else {
            holder.active.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.chart05));
        }

        holder.active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleUserStatus(UDetails, holder);
            }
        });
        holder.ContainerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                LoadUserBottomSheet(view.getContext() ,UDetails.getFullName(),UDetails.getEmail(),UDetails.getMobile(),UDetails.isActive());
                return true;
            }
        });

    }

    private void toggleUserStatus(User user, UserListAdapter.UserViewHolder holder) {
        boolean newStatus = !user.isActive();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user").document(user.getId())
                .update("isActive", newStatus)
                .addOnSuccessListener(aVoid -> {

                    user.setActive(newStatus);
                    holder.active.setText(newStatus ? "Deactivate" : "Activate");
                    if(holder.active.getText()=="Activate"){
                        holder.active.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.chart05));
                    }else {
                        holder.active.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.chart03));

                    }
                    showCustomToast.showToast(holder.itemView.getContext(),"Status updated",R.drawable.checked);

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showCustomToast.showToast(holder.itemView.getContext(),"Status updated Fail !",R.drawable.cancel);

                    }
                });
    }
    private void LoadUserBottomSheet(Context context,String name, String email, String mobile, Boolean status){

        View bottomSheetView = LayoutInflater.from(context).inflate( R.layout.user_details_bottom,null );
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(bottomSheetView);

      EditText Ufname = bottomSheetView.findViewById(R.id.ManageUserDetailsED01);
      EditText Uemail = bottomSheetView.findViewById(R.id.ManageUserDetailsED02);
      EditText Umobile = bottomSheetView.findViewById(R.id.ManageUserDetailsED03);
      EditText Ustatus = bottomSheetView.findViewById(R.id.ManageUserDetailsED04);
      Button btn01 = bottomSheetView.findViewById(R.id.UserDetailsbutton01);

        Ufname.setText(name);
        Uemail.setText(email);
        Umobile.setText(mobile);
        if(status){
            Ustatus.setText("Active");
        }else {
            Ustatus.setText("Deactive");

        }
        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();

    }

    @Override
    public int getItemCount() {
        return userdetails.size();
    }
}