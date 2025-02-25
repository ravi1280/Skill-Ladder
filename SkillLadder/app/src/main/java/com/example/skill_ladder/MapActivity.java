package com.example.skill_ladder;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.skill_ladder.model.job;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MapActivity extends AppCompatActivity {
    String latitude,longitude,companyEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        companyEmail = intent.getStringExtra("companyEmail");
        searchCompanyLOcation();


    }
    private void searchCompanyLOcation(){
        FirebaseFirestore firestore= FirebaseFirestore.getInstance();
        firestore.collection("company")
                .whereEqualTo("email",companyEmail)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    latitude = doc.getString("latitude");
                    longitude = doc.getString("longitude");

                }
                loadMap();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
    private  void loadMap(){
        SupportMapFragment mapFragment= new SupportMapFragment();
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.MapFragment001,mapFragment);
        fragmentTransaction.commit();

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                Log.e("Map","Map ius ready to load ");
//                double lat = 7.21894721175205;
//                double ati = 79.86721687374857;
                double lat = Double.parseDouble(latitude);
                double ati = Double.parseDouble(longitude);

                if (latitude == null || longitude == null || latitude.isEmpty() || longitude.isEmpty()) {
                    Log.e("MapActivity", "Latitude or Longitude is null or empty. Using default values.");
                    lat = 7.21894721175205;  // Default values
                    ati = 79.86721687374857;
                }
                LatLng latLng= new LatLng(lat, ati);
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                        new CameraPosition.Builder().target(latLng).zoom(18).build()
                ));
                googleMap
                        .addMarker(new MarkerOptions().position(latLng).title("rider")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.locationmark)));

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        marker.setTitle("Company Location");
                        Log.e("Map","Click Map Marker");
                        return false;
                    }
                });
            }
        });
    }
}