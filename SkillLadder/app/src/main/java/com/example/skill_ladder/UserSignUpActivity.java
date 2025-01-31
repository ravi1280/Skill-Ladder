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

public class UserSignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btn01 = findViewById(R.id.CompanyLoginBtn01);
        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText01 = findViewById(R.id.CompanyLogineditText01);
                EditText editText02 = findViewById(R.id.CompanyLogineditText02);
                EditText editText03 = findViewById(R.id.CompanySignUpeditText03);
                EditText editText04 = findViewById(R.id.CompanySignUpeditText04);

                String fullName = editText01.getText().toString();
                String mobile = editText02.getText().toString();
                String email = editText03.getText().toString();
                String password = editText04.getText().toString();

                if(fullName.isEmpty()){
                    customAlert.showCustomAlert(UserSignUpActivity.this, "Error !", "Please Fill Full Name",R.drawable.cancel);
                } else if (mobile.isEmpty()) {
                    customAlert.showCustomAlert(UserSignUpActivity.this, "Error !", "Please Fill Mobile",R.drawable.cancel);
                } else if (email.isEmpty()) {
                    customAlert.showCustomAlert(UserSignUpActivity.this, "Error !", "Please Fill Email",R.drawable.cancel);
                } else if (password.isEmpty()) {
                    customAlert.showCustomAlert(UserSignUpActivity.this, "Error !", "Please Fill Password",R.drawable.cancel);
                }else {
                    customAlert.showCustomAlert(UserSignUpActivity.this, "Success !", "Your operation was successful!",R.drawable.checked);
                }
            }
        });


        TextView textView= findViewById(R.id.CompanySignUpTV04);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSignUpActivity.this,UserLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}