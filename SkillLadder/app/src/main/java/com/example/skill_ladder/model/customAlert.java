package com.example.skill_ladder.model;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.skill_ladder.R;

public class customAlert {
    public static void showCustomAlert(Context context, String title, String message, int iconRes) {
        // Inflate custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_alert, null);


        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvMessage = view.findViewById(R.id.tvMessage);
        ImageView imgIcon = view.findViewById(R.id.imgIcon);
        Button btnOk = view.findViewById(R.id.btnOk);


        tvTitle.setText(title);
        tvMessage.setText(message);
        imgIcon.setImageResource(iconRes);


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        alertDialog.show();


        btnOk.setOnClickListener(v -> alertDialog.dismiss());
    }
}
