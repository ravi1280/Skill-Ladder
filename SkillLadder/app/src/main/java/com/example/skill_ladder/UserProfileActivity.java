package com.example.skill_ladder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.skill_ladder.model.customAlert;
import com.example.skill_ladder.model.showCustomToast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {

    String fullName,email,mobile,password;

    EditText text01,text02,text03,text04;
    TextView tv01,tv02;
    private FirebaseFirestore firestore;
    private ImageView userImage;
    private static final int REQUEST_CODE_SELECT_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        firestore = FirebaseFirestore.getInstance();

        tv01 = findViewById(R.id.UserProfiletextView01);
        tv02 = findViewById(R.id.UserProfiletexview02);

        text01 = findViewById(R.id.UserProfileDataText01);
        text02 = findViewById(R.id.UserProfileDataText02);
        text03 = findViewById(R.id.UserProfileDataText03);
        text04 = findViewById(R.id.UserProfileDataText04);

        setData();

        ImageView imageView01 = findViewById(R.id.UserProfileBackIcon);
        imageView01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

        ImageView logoutImage= findViewById(R.id.UsreLogout);
        logoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });

        Button btn01 = findViewById(R.id.UserProfileBtn01);
        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserProfile();
            }
        });
        userImage= findViewById(R.id.UserProfileImageView);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
            }
        });
        loadImageFromInternalStorage();

        userImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteImageFromInternalStorage();
                return true;
            }
        });

        Button showBottomSheetButton = findViewById(R.id.UserProfileBtn02);
        showBottomSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetProcess();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_SELECT_IMAGE) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                try {
                    Bitmap selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    userImage.setImageBitmap(selectedImageBitmap);
                    saveImageToInternalStorage(selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                    showCustomToast.showToast(UserProfileActivity.this, "Failed to load image", R.drawable.cancel);
                }
            }
        }
    }
    private void saveImageToInternalStorage(Uri selectedImageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
            File outputFile = new File(getFilesDir(), "User_profile_image.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, length);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();

            showCustomToast.showToast(UserProfileActivity.this, "Image Saved !", R.drawable.checked);
        } catch (IOException e) {
            e.printStackTrace();
            showCustomToast.showToast(UserProfileActivity.this, "Error Saving Image", R.drawable.cancel);
        }
    }
    private void loadImageFromInternalStorage() {
        try {
            File imageFile = new File(getFilesDir(), "User_profile_image.jpg");
            if (imageFile.exists()) {
                FileInputStream fileInputStream = new FileInputStream(imageFile);
                Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                userImage.setImageBitmap(bitmap);
                fileInputStream.close();
            } else {
                showCustomToast.showToast(UserProfileActivity.this, "Please add Profile Image !", R.drawable.warning);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showCustomToast.showToast(UserProfileActivity.this, "Error Loading Image", R.drawable.cancel);
        }
    }
    private void deleteImageFromInternalStorage() {
        try {
            File imageFile = new File(getFilesDir(), "User_profile_image.jpg");
            if (imageFile.exists()) {

                boolean isDeleted = imageFile.delete();

                if (isDeleted) {
                    showCustomToast.showToast(UserProfileActivity.this, "Image Deleted", R.drawable.checked);
                    userImage.setImageBitmap(null);
                } else {
                    showCustomToast.showToast(UserProfileActivity.this, "Error Deleting Image", R.drawable.cancel);
                }
            } else {
                showCustomToast.showToast(UserProfileActivity.this, "Image Not Found", R.drawable.cancel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showCustomToast.showToast(UserProfileActivity.this, "Error Deleting Image", R.drawable.cancel);
        }
        recreate();
    }

    private void updateUserProfile() {
        String fullName = text01.getText().toString().trim();
        String mobile = text03.getText().toString().trim();

        if (fullName.isEmpty()) {
            customAlert.showCustomAlert(this, "Error", "Please Fill The Full Name!", R.drawable.cancel);
        } else if (mobile.isEmpty()) {
            customAlert.showCustomAlert(this, "Error", "Please Fill The Mobile!", R.drawable.cancel);
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("UserFullName", fullName);
            editor.putString("UserMobile", mobile);
            editor.apply();

            text01.setText(fullName);
            tv01.setText(fullName);
            text03.setText(mobile);

            updateFireBase();

        }
    }
    private void updateFireBase(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String UserId = sharedPreferences.getString("UserID", "");

        String fullName = text01.getText().toString().trim();
        String mobile = text03.getText().toString().trim();

        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("fullName", fullName);
        updatedData.put("mobile", mobile);

        firestore.collection("user")
                .document(UserId)
                .update(updatedData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        recreate();
                        customAlert.showCustomAlert(UserProfileActivity.this, "Success", "Successfully Update !", R.drawable.checked);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        customAlert.showCustomAlert(UserProfileActivity.this, "Error", "Can not  Update !", R.drawable.cancel);

                    }
                });

    }
    private void bottomSheetProcess(){
        View bottomSheetView = getLayoutInflater().inflate(R.layout.update_password_bottom, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(UserProfileActivity.this);
        bottomSheetDialog.setContentView(bottomSheetView);

        Button actionOne = bottomSheetView.findViewById(R.id.UserUpdatePasswordBtn);
        EditText text01 = bottomSheetView.findViewById(R.id.UserOldPassword);
        EditText text02 = bottomSheetView.findViewById(R.id.UserNewPassword);
        EditText text03 = bottomSheetView.findViewById(R.id.UserReTypePassword);

        actionOne.setOnClickListener(view -> {
            String oldpassword = text01.getText().toString().trim();
            String newPassword = text02.getText().toString().trim();
            String reNewPassword = text03.getText().toString().trim();

            if (oldpassword.isEmpty()) {
                customAlert.showCustomAlert(UserProfileActivity.this, "Error", "Please Fill Old Password!", R.drawable.cancel);
            } else if (newPassword.isEmpty()) {
                customAlert.showCustomAlert(UserProfileActivity.this, "Error", "Please Fill New Password!", R.drawable.cancel);
            } else if (reNewPassword.isEmpty()) {
                customAlert.showCustomAlert(UserProfileActivity.this, "Error", "Please Fill Re-Type Password!", R.drawable.cancel);
            } else if (!reNewPassword.equals(newPassword)) {
                customAlert.showCustomAlert(UserProfileActivity.this, "Error", "Re-Type Password Doesn't Match !", R.drawable.cancel);
            } else if (!oldpassword.equals(text04.getText().toString())) {
                customAlert.showCustomAlert(UserProfileActivity.this, "Error", "Old Password Doesn't Match !", R.drawable.cancel);
            }else {
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("UserPassword", newPassword);
                editor.apply();

                text04.setText(newPassword);

                String UserId = sharedPreferences.getString("UserID", "");

                Map<String, Object> updatedData = new HashMap<>();
                updatedData.put("password", newPassword);
                firestore.collection("user")
                        .document(UserId)
                        .update(updatedData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                customAlert.showCustomAlert(UserProfileActivity.this, "Success", "Password Updated Successfully!", R.drawable.checked);
                                bottomSheetDialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                customAlert.showCustomAlert(UserProfileActivity.this, "Error", "Password Not Updated!", R.drawable.cancel);
                            }
                        });

                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();

    }
    private void setData(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String UserName = sharedPreferences.getString("UserFullName", "");
        String UserEmail = sharedPreferences.getString("UserEmail", "");
        String UserMobile = sharedPreferences.getString("UserMobile", "");
        String UserPassword = sharedPreferences.getString("UserPassword", "password");

        if(UserName.isEmpty()){
            customAlert.showCustomAlert(UserProfileActivity.this, "Error", "No Data to Set", R.drawable.cancel);
            finish();
        }else {

        tv01.setText(UserName);
        tv02.setText(UserEmail);

        text01.setText(UserName);
        text02.setText(UserEmail);
        text03.setText(UserMobile);
        text04.setText(UserPassword);
        }

    }

    private void Logout(){
        View bottomSheetView = getLayoutInflater().inflate(R.layout.logout_bottom, null);
        BottomSheetDialog bottomSheetDialog02 = new BottomSheetDialog(UserProfileActivity.this);
        bottomSheetDialog02.setContentView(bottomSheetView);

        Button logout = bottomSheetView.findViewById(R.id.LogoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("UserFullName");
                editor.remove("UserEmail");
                editor.remove("UserMobile");
                editor.remove("UserPassword");
                editor.remove("UserID");
                editor.remove("UserIsLoggedIn");
                editor.apply();

                customAlert.showCustomAlert(UserProfileActivity.this, "Success", "Logout Successfully!", R.drawable.checked);
                bottomSheetDialog02.dismiss();

                deleteImageFromInternalStorage();
                Intent intent = new Intent(UserProfileActivity.this,UserLoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
        bottomSheetDialog02.show();


    }
}