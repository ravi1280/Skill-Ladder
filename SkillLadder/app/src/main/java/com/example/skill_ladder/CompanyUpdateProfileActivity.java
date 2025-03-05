package com.example.skill_ladder;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
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
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.skill_ladder.model.Admin;
import com.example.skill_ladder.model.Company;
import com.example.skill_ladder.model.customAlert;
import com.example.skill_ladder.model.showCustomToast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class CompanyUpdateProfileActivity extends AppCompatActivity {
    private  TextView CompanyNameView, CompanyEmailView;

    private EditText CompanyNameEdit, CompanyEmailEdit, CompanyMobileEdit, CompanyPasswordEdit;
    private FirebaseFirestore firestore;
    private ImageView companyImage;
    private static final int REQUEST_CODE_SELECT_IMAGE = 1;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private FusedLocationProviderClient fusedLocationClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_company_update_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        CompanyNameView = findViewById(R.id.CompanyProfiletextView01);
        CompanyEmailView = findViewById(R.id.CompanyProfiletexview02);

        CompanyNameEdit = findViewById(R.id.CompanyProfileDataText01);
        CompanyEmailEdit = findViewById(R.id.CompanyProfileDataText02);
        CompanyMobileEdit = findViewById(R.id.CompanyProfileDataText03);
        CompanyPasswordEdit = findViewById(R.id.CompanyProfileDataText04);

        companyImage= findViewById(R.id.CompanyProfileimageView);
        companyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
            }
        });

        fillProfileDetails();

        companyImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteImageFromInternalStorage();
                return true;
            }
        });

        ImageView imageView01 = findViewById(R.id.CompanyProfileBackIcon);
        imageView01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageView logout =findViewById(R.id.CompanyLogOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                companyLogout();
            }
        });

        Button updateButton = findViewById(R.id.CompanyProfileBtn01);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileUpdateDetails();
            }
        });

//        Button buttonlocation = findViewById(R.id.CompanyLocationBtn01);
//        buttonlocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(CompanyUpdateProfileActivity.this, LocationGetActivity.class);
//                startActivity(intent);
//            }
//        });

        Button showBottomSheetButton = findViewById(R.id.CompanyProfileBtn02);

        showBottomSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });

        Button getLoction = findViewById(R.id.CompanyLocationBtn01);
        getLoction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requestLocationPermission();
            }
        });
        loadImageFromInternalStorage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_SELECT_IMAGE) {
            if (data != null) {
                Uri selectedImageUri = data.getData();

                try {
                    Bitmap selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    companyImage.setImageBitmap(selectedImageBitmap);

                    saveImageToInternalStorage(selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                    showCustomToast.showToast(CompanyUpdateProfileActivity.this, "Failed to load image", R.drawable.cancel);
                }
            }
        }
    }
    private void saveImageToInternalStorage(Uri selectedImageUri) {
        try {

            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
            File outputFile = new File(getFilesDir(), "Company_profile_image.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, length);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
            showCustomToast.showToast(CompanyUpdateProfileActivity.this, "Image saved to internal storage", R.drawable.checked);
        } catch (IOException e) {
            e.printStackTrace();
            showCustomToast.showToast(CompanyUpdateProfileActivity.this, "Failed to save image", R.drawable.cancel);
        }
    }
    private void deleteImageFromInternalStorage() {
        try {
            File imageFile = new File(getFilesDir(), "Company_profile_image.jpg");

            if (imageFile.exists()) {
                boolean isDeleted = imageFile.delete();
                if (isDeleted) {
                    showCustomToast.showToast(CompanyUpdateProfileActivity.this, "Image deleted from internal storage", R.drawable.checked);
                    companyImage.setImageBitmap(null);
                } else {
                    showCustomToast.showToast(CompanyUpdateProfileActivity.this, "Failed to delete image", R.drawable.cancel);
                }
            } else {
                showCustomToast.showToast(CompanyUpdateProfileActivity.this, "Image not found", R.drawable.cancel);

            }
        } catch (Exception e) {
            e.printStackTrace();
            showCustomToast.showToast(CompanyUpdateProfileActivity.this, "Error deleting image", R.drawable.cancel);

        }
        recreate();
    }
    private void loadImageFromInternalStorage() {
        try {

            File imageFile = new File(getFilesDir(), "Company_profile_image.jpg");

            if (imageFile.exists()) {

                FileInputStream fileInputStream = new FileInputStream(imageFile);
                Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                companyImage.setImageBitmap(bitmap);

                fileInputStream.close();
            } else {
                showCustomToast.showToast(CompanyUpdateProfileActivity.this, "Please add Profile Image !", R.drawable.warning);

            }
        } catch (IOException e) {
            e.printStackTrace();
            showCustomToast.showToast(CompanyUpdateProfileActivity.this, "Failed to load image", R.drawable.cancel);

        }
    }

    private void fillProfileDetails (){
        SharedPreferences sharedPreferences = getSharedPreferences("CompanyPrefs", MODE_PRIVATE);
        String companyEmail = sharedPreferences.getString("companyEmail", "");

        if (companyEmail.isEmpty()) {
            Log.e("companyEmail", "No companyEmail found in SharedPreferences");
            return;
        }
        Log.d("companyEmail", "companyEmail " + companyEmail);

        firestore = FirebaseFirestore.getInstance();
        firestore.collection("company")
                .whereEqualTo("email", companyEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Log.d("CompanyProfile", "No company found for email: " + companyEmail);
                        return;
                    }
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        if (document.exists()) {
                            CompanyNameView.setText(document.getString("name"));
                            CompanyEmailView.setText(document.getString("email"));
                            CompanyNameEdit.setText(document.getString("name"));
                            CompanyEmailEdit.setText(document.getString("email"));
                            CompanyMobileEdit.setText( document.getString("mobile"));
                            CompanyPasswordEdit.setText(document.getString("password"));
                            Log.d("CompanyProfile", "Company profile loaded successfully.");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("CompanyProfile", "Error fetching company data", e);
                    }
                });
    }
    private void profileUpdateDetails(){
        String name= CompanyNameEdit.getText().toString();
        String mobile= CompanyMobileEdit.getText().toString();
        if (name.isEmpty()){
            customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Name Field is Empty !", R.drawable.cancel);

        }else if(mobile.isEmpty()){
            customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Mobile Field is Empty!", R.drawable.cancel);

        }else {
            SharedPreferences sharedPreferences = getSharedPreferences("CompanyPrefs", MODE_PRIVATE);
            String companyEmail = sharedPreferences.getString("companyEmail", "");

            if (companyEmail.isEmpty()) {
                customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Data Lost from mobile !", R.drawable.cancel);

                return;
            }
            Log.d("companyEmail", "companyEmail " + companyEmail);

            firestore = FirebaseFirestore.getInstance();
            firestore.collection("company")
                    .whereEqualTo("email", companyEmail)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.isEmpty()) {
                                Log.d("Firestore", "No company found with email: " + companyEmail);
                                return;
                            }

                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                String documentId = document.getId();


                                Map<String, Object> updatedData = new HashMap<>();
                                updatedData.put("name", name);
                                updatedData.put("mobile", mobile);

                                firestore.collection("company")
                                        .document(documentId)
                                        .update(updatedData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                recreate();
                                                customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Success", "Successfully Update !", R.drawable.checked);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Error updating company profile!", R.drawable.cancel);

                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Firestore", "Error fetching company data", e);
                        }
                    });
        }

    }
    private void companyLogout(){
        View bottomSheetView = getLayoutInflater().inflate(R.layout.logout_bottom, null);
        BottomSheetDialog bottomSheetDialog02 = new BottomSheetDialog(CompanyUpdateProfileActivity.this);
        bottomSheetDialog02.setContentView(bottomSheetView);

        Button logout = bottomSheetView.findViewById(R.id.LogoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("CompanyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("companyEmail");
                editor.remove("companyID");
                editor.remove("companyIsLoggedIn");
                editor.apply();

                customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Success", "Logout Successfully!", R.drawable.checked);
                bottomSheetDialog02.dismiss();
                deleteImageFromInternalStorage();

                Intent intent = new Intent(CompanyUpdateProfileActivity.this,CompanyLogInActivity.class);
                startActivity(intent);
                finish();

            }
        });
        bottomSheetDialog02.show();

    }
    private void updatePassword(){

        View bottomSheetView = getLayoutInflater().inflate(R.layout.update_password_bottom, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(CompanyUpdateProfileActivity.this);
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
                customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Please Fill Old Password!", R.drawable.cancel);
            } else if (newPassword.isEmpty()) {
                customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Please Fill New Password!", R.drawable.cancel);
            } else if (reNewPassword.isEmpty()) {
                customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Please Fill Re-Type Password!", R.drawable.cancel);
            } else {
                if (oldpassword.equals(CompanyPasswordEdit.getText().toString().trim())){

                    if(newPassword.equals(reNewPassword)){
                        SharedPreferences sharedPreferences = getSharedPreferences("CompanyPrefs", MODE_PRIVATE);
                        String companyEmail = sharedPreferences.getString("companyEmail", "");
                        firestore = FirebaseFirestore.getInstance();
                        firestore.collection("company")
                                .whereEqualTo("email", companyEmail)
                                .whereEqualTo("password", oldpassword)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                String documentId = document.getId();
                                                Map<String, Object> updatedData = new HashMap<>();
                                                updatedData.put("password", newPassword);
                                                firestore.collection("company")
                                                        .document(documentId)
                                                        .update(updatedData)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                CompanyPasswordEdit.setText(newPassword);
                                                                customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Success", "Password Updated Successfully!", R.drawable.checked);
                                                                bottomSheetDialog.dismiss();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Password Not Updated!", R.drawable.cancel);
                                                            }
                                                        });
                                            }
                                        } else {
                                            customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Old Password Not Matched!", R.drawable.cancel);
                                        }
                                    }
                                });

                    }else {
                        customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Retype Password Not Matched!", R.drawable.cancel);
                    }

                }else {
                    customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Old Password Not Matched!", R.drawable.cancel);
                }

            }
        });
        bottomSheetDialog.show();

    }


    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app requires location access to fetch your current location. Please allow it.")
                        .setPositiveButton("OK", (dialog, which) ->
                                ActivityCompat.requestPermissions(CompanyUpdateProfileActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                        LOCATION_PERMISSION_REQUEST_CODE))
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .show();
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            getLocationBottomsheet();
        }
    }

    private void getLocationBottomsheet(){
        View bottomSheetView = getLayoutInflater().inflate(R.layout.get_location_bottom, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(CompanyUpdateProfileActivity.this);
        bottomSheetDialog.setContentView(bottomSheetView);


        Button getbtn = bottomSheetView.findViewById(R.id.getLctBottomBtn);
        EditText text01 = bottomSheetView.findViewById(R.id.LatitudeED01);
        EditText text02 = bottomSheetView.findViewById(R.id.LongitudeED01);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.getCurrentLocation(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                text01.setText(String.valueOf(latitude));
                                text02.setText(String.valueOf(longitude));
                            } else {
                                Toast.makeText(CompanyUpdateProfileActivity.this, "Failed to get location! Try moving outdoors.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CompanyUpdateProfileActivity.this, "Error getting location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }



        getbtn.setOnClickListener(view -> {
            String latitude = text01.getText().toString().trim();
            String longitude = text02.getText().toString().trim();

            SharedPreferences sharedPreferences = getSharedPreferences("CompanyPrefs", MODE_PRIVATE);
            String companyEmail = sharedPreferences.getString("companyEmail", "");
            firestore = FirebaseFirestore.getInstance();
            firestore.collection("company")
                    .whereEqualTo("email", companyEmail)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String documentId = document.getId();
                                    Map<String, Object> updatedData = new HashMap<>();
                                    updatedData.put("latitude", latitude);
                                    updatedData.put("longitude", longitude);
                                    firestore.collection("company")
                                            .document(documentId)
                                            .update(updatedData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("companyLocation","Active");
                                                    editor.apply();
                                                    customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Success", "Get Location Successfully!", R.drawable.checked);
                                                    bottomSheetDialog.dismiss();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Location Not Updated!", R.drawable.cancel);
                                                }
                                            });
                                }
                            } else {
                                customAlert.showCustomAlert(CompanyUpdateProfileActivity.this, "Error", "Old Password Not Matched!", R.drawable.cancel);
                            }
                        }
                    });


        });
        bottomSheetDialog.show();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    new AlertDialog.Builder(this)
                            .setTitle("Permission Required")
                            .setMessage("Location permission is required to use this feature. Please enable it in Settings.")
                            .setPositiveButton("Open Settings", (dialog, which) -> {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                            .show();
                } else {
                    Toast.makeText(this, "Location permission denied!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}