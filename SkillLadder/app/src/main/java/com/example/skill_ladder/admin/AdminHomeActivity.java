package com.example.skill_ladder.admin;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.skill_ladder.R;
import com.example.skill_ladder.navigation.AdminDashboardFragment;
import com.example.skill_ladder.navigation.AdminProfileFragment;
import com.example.skill_ladder.navigation.GetReportsFragment;
import com.example.skill_ladder.navigation.ManageCompanyFragment;
import com.example.skill_ladder.navigation.ManageLessonsFragment;
import com.example.skill_ladder.navigation.ManageOtherFragment;
import com.example.skill_ladder.navigation.ManageUsersFragment;
import com.google.android.material.navigation.NavigationView;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout01), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        loadFragment(new AdminDashboardFragment());

        DrawerLayout drawerLayout= findViewById(R.id.drawerLayout01);
        Toolbar toolbar = findViewById(R.id.toolbar001);
        NavigationView navigationView= findViewById(R.id.navigationView01);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(navigationView);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()== R.id.AdminDashBoard){
                    loadFragment(new AdminDashboardFragment());
                } else if (item.getItemId()==R.id.adminUserManage) {
                    loadFragment(new ManageUsersFragment());
                }else if (item.getItemId()==R.id.AdminCompanyManage) {
                    loadFragment(new ManageCompanyFragment());
                }else if (item.getItemId()==R.id.AdminlessonManage) {
                    loadFragment(new ManageLessonsFragment());
                }else if (item.getItemId()==R.id.AdminProfile) {
                    loadFragment(new AdminProfileFragment());
                }else if (item.getItemId()==R.id.ManageOther) {
                    loadFragment(new ManageOtherFragment());
                }else if (item.getItemId()==R.id.getReport) {
                    loadFragment(new GetReportsFragment());
                }
                toolbar.setSubtitle(item.getTitle());
                drawerLayout.closeDrawers();
                return false;
            }
        });


    }
    private void loadFragment(Fragment fragment){

        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView001,fragment,null)
                .setReorderingAllowed(true)
                .commit();

    }
}