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

public class CompanySignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_company_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button button01 = findViewById(R.id.CompanySignUpBtn01);
        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editText01 =findViewById(R.id.CompanySignUpeditText01);
                EditText editText02 =findViewById(R.id.CompanySignUpeditText02);
                EditText editText03 =findViewById(R.id.CompanySignUpeditText03);
                EditText editText04 =findViewById(R.id.CompanySignUpeditText04);

                String name = editText01.getText().toString();
                String mobile = editText02.getText().toString();
                String email = editText03.getText().toString();
                String password = editText04.getText().toString();

                if(name.isEmpty()){
                    customAlert.showCustomAlert(CompanySignUpActivity.this,"Error ","Please Fill The Company Name",R.drawable.cancel);
                } else if (mobile.isEmpty()) {
                    customAlert.showCustomAlert(CompanySignUpActivity.this,"Error ","Please Fill The Company Mobile",R.drawable.cancel);

                } else if (email.isEmpty()) {
                    customAlert.showCustomAlert(CompanySignUpActivity.this,"Error ","Please Fill The Company Email",R.drawable.cancel);

                } else if (password.isEmpty()) {
                    customAlert.showCustomAlert(CompanySignUpActivity.this,"Error ","Please Fill The Company Password",R.drawable.cancel);

                }else {
                    customAlert.showCustomAlert(CompanySignUpActivity.this,"Success ","Your Action is Succesfully ! ",R.drawable.checked);

                }


            }
        });

        TextView textView01 = findViewById(R.id.CompanySignUpTV004);
        textView01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent01 = new Intent(CompanySignUpActivity.this,CompanyLogInActivity.class);
                startActivity(intent01);
            }
        });
    }
}