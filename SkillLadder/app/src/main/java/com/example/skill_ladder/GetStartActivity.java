package com.example.skill_ladder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.skill_ladder.model.customAlert;

public class GetStartActivity extends AppCompatActivity {
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_start);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if(!isConnected()){
            customAlert.showCustomAlert(GetStartActivity.this,"Connection Error ","Please Check Your Network Connection !",R.drawable.no_wifi);
        }else {
            CardView cardView01 = findViewById(R.id.getStartCardView01);
            cardView01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                    Boolean islogin = sharedPreferences.getBoolean("UserIsLoggedIn", false);
                    if(islogin){
                        Intent intent01 = new Intent(GetStartActivity.this,UserHomeActivity.class);
                        startActivity(intent01);
                    }else {

                    Intent intent01 = new Intent(GetStartActivity.this,UserLoginActivity.class);
                    startActivity(intent01);
                    }
                }
            });
            CardView cardView02 = findViewById(R.id.getStartCardView02);
            cardView02.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = getSharedPreferences("CompanyPrefs", Context.MODE_PRIVATE);
                    Boolean islogin = sharedPreferences.getBoolean("companyIsLoggedIn", false);
                    if(islogin){
                        Intent intent02 = new Intent(GetStartActivity.this,JobCompanyHomeActivity.class);
                        startActivity(intent02);
                    }else {
                    Intent intent02 = new Intent(GetStartActivity.this,CompanyLogInActivity.class);
                    startActivity(intent02);
                    }
                }
            });
        }



    }
    private Boolean isConnected(){
        ConnectivityManager connectivityManager=(ConnectivityManager)getApplicationContext().getSystemService(context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo()!=null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}