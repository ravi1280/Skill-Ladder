package com.example.skill_ladder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class TestActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SELECT_IMAGE = 1;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        imageView = findViewById(R.id.testimageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open gallery to select an image
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
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
                    // Convert the URI to a Bitmap to display it in the ImageView
                    Bitmap selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    imageView.setImageBitmap(selectedImageBitmap);

                    // Save the image to internal storage
                    saveImageToInternalStorage(selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void saveImageToInternalStorage(Uri selectedImageUri) {
        try {
            // Open the input stream of the selected image
            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);

            // Define the output file in internal storage
            File outputFile = new File(getFilesDir(), "profile_image.jpg");

            // Create an output stream to write the file
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, length);
            }

            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();

            // Show a message indicating the image was saved
            Toast.makeText(this, "Image saved to internal storage", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }
    private void loadImageFromInternalStorage() {
        try {
            // Define the path of the saved image in internal storage
            File imageFile = new File(getFilesDir(), "profile_image.jpg");

            if (imageFile.exists()) {
                // Read the image from the internal storage
                FileInputStream fileInputStream = new FileInputStream(imageFile);
                Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);

                // Set the bitmap to the ImageView
                imageView.setImageBitmap(bitmap);

                // Close the input stream
                fileInputStream.close();
            } else {
                Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
        }
    }

}
