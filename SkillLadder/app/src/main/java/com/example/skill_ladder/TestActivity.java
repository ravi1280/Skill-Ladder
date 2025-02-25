package com.example.skill_ladder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TestActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SELECT_IMAGE = 1;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        generatePDF();


    }
    private void generatePDF() {
        try {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/Report.pdf";
            PdfWriter writer = new PdfWriter(path);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            document.add(new Paragraph("SkillLadder Report"));
            document.add(new Paragraph("Generated on: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date())));

            // Add table data
            Table table = new Table(3);
            table.addCell("User Name");
            table.addCell("Lesson Purchased");
            table.addCell("Date");

            table.addCell("John Doe");
            table.addCell("Android Development");
            table.addCell("2025-02-21");

            table.addCell("John Doe");
            table.addCell("Android Development");
            table.addCell("2025-02-21");

            table.addCell("John Doe");
            table.addCell("Android Development");
            table.addCell("2025-02-21");

            document.add(table);
            document.close();

            Toast.makeText(this, "PDF saved in Downloads", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


//    private GoogleMap googleMap;
//    private FusedLocationProviderClient fusedLocationClient;
//    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_user_map_view);
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//        // Retrieve the existing SupportMapFragment
//        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.user_map_framlayout_2);
//
//        // If fragment is not already added, create a new instance
//        if (supportMapFragment == null) {
//            supportMapFragment = SupportMapFragment.newInstance();
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.add(R.id.user_map_framlayout_2, supportMapFragment);
//            fragmentTransaction.commit();
//        }
//
//        if (supportMapFragment != null) {
//            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
//                @Override
//                public void onMapReady(@NonNull GoogleMap map) {
//
//
//                    googleMap = map;
//                    getCurrentLocation(); // Fetch and set user location
//                    addDefaultLocation();
//                }
//            });
//        }
//    }
//
//    ////////////////////////////////////////////////////////////////////////////
//
//    private void drawRoute(LatLng userLocation, LatLng destination) {
//        String apiKey = "AIzaSyBZGXPBpjrbfFct9SbqsGW5px4o_KIKOyY"; // Replace with your actual API key
//
//        String url = "https://maps.googleapis.com/maps/api/directions/json?"
//                + "origin=" + userLocation.latitude + "," + userLocation.longitude
//                + "&destination=" + destination.latitude + "," + destination.longitude
//                + "&key=" + apiKey;
//
//        Log.d("Directions URL", "Request URL: " + url);  // Debugging line to check the URL
//
//        new Thread(() -> {
//            try {
//                URL connectionUrl = new URL(url);
//                HttpURLConnection connection = (HttpURLConnection) connectionUrl.openConnection();
//                connection.setRequestMethod("GET");
//                connection.connect();
//
//                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                StringBuilder response = new StringBuilder();
//                String line;
//                while ((line = bufferedReader.readLine()) != null) {
//                    response.append(line);
//                }
//                bufferedReader.close();
//
//                JSONObject jsonResponse = new JSONObject(response.toString());
//                JSONArray routes = jsonResponse.getJSONArray("routes");
//
//                if (routes.length() > 0) {
//                    JSONObject route = routes.getJSONObject(0);
//                    JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
//                    String encodedPolyline = overviewPolyline.getString("points");
//
//                    Log.d("Polyline", "Encoded Polyline: " + encodedPolyline); // Debugging line to check the polyline
//
//                    runOnUiThread(() -> {
//                        List<LatLng> points = decodePolyline(encodedPolyline);
//                        googleMap.addPolyline(new PolylineOptions()
//                                .addAll(points)
//                                .width(10)
//                                .color(Color.BLUE));
//                    });
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
//
//
//
//    private List<LatLng> decodePolyline(String encoded) {
//        List<LatLng> polyline = new ArrayList<>();
//        int index = 0, len = encoded.length();
//        int lat = 0, lng = 0;
//
//        while (index < len) {
//            int b, shift = 0, result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1F) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int deltaLat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lat += deltaLat;
//
//            shift = 0;
//            result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1F) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int deltaLng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lng += deltaLng;
//
//            polyline.add(new LatLng(lat / 1E5, lng / 1E5));
//        }
//        return polyline;
//    }
//
//    private void addDefaultLocation() {
//        if (googleMap == null) return; // Avoid null pointer errors
//
//        // Define default location (TechknowLK Pvt Ltd)
//        LatLng defaultLocation = new LatLng(6.87387284485117, 79.8879499245235);
//
//        // Add marker for default location
//        googleMap.addMarker(new MarkerOptions()
//                .position(defaultLocation)
//                .title("TechknowLK Pvt Ltd")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_shipped_order_icon)));
//
//        // Move camera to the default location
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));
//    }
//
//
//
//    private void getCurrentLocation() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    LOCATION_PERMISSION_REQUEST_CODE);
//            return;
//        }
//
//        fusedLocationClient.getLastLocation().addOnSuccessListener(this, newLocation -> {
//            if (newLocation != null && googleMap != null) {
//                LatLng userLatLng = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());
//                LatLng defaultLocation = new LatLng(6.87387284485117, 79.8879499245235);
//
//                Log.i("muLocation",String.valueOf(userLatLng));
//
//                googleMap.addMarker(new MarkerOptions()
//                        .position(userLatLng)
//                        .title("Your Location")
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon_2)));
//
//                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15));
//
//                // Draw route to default location
//                drawRoute(userLatLng, defaultLocation);
//            }
//        });
//    }
//
//    // Handle permission request result
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getCurrentLocation();
//            } else {
//                Log.e("PermissionError", "Location permission denied");
//            }
//        }
//    }
//}