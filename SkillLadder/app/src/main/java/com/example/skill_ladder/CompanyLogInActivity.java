package com.example.skill_ladder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.skill_ladder.model.customAlert;

public class CompanyLogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_company_log_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button button01 = findViewById(R.id.CompanyLOGINBtn01);
        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editText01 =findViewById(R.id.CompanyLogineditText01);
                EditText editText02 =findViewById(R.id.CompanyLogineditText02);

                String email = editText01.getText().toString();
                String password = editText02.getText().toString();

                if(email.isEmpty()){
                    customAlert.showCustomAlert(CompanyLogInActivity.this,"Error ","Please Fill The Company UserName",R.drawable.cancel);
                } else if (password.isEmpty()) {
                    customAlert.showCustomAlert(CompanyLogInActivity.this,"Error ","Please Fill The Company Password",R.drawable.cancel);

                }else {
                    customAlert.showCustomAlert(CompanyLogInActivity.this,"Success ","Your Action is Successfully ! ",R.drawable.checked);

                }


            }
        });

        TextView textView01 = findViewById(R.id.CompanyLogInTV04);
        textView01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent01 = new Intent(CompanyLogInActivity.this,CompanySignUpActivity.class);
                startActivity(intent01);
            }
        });
    }
}