package com.example.mytestapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class ApartmentsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_apartments, container, false);

        // Programmatically create SupportMapFragment
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.mapContainer, mapFragment).commit();
        mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mMap != null) {
            // Sample coordinates for demonstration purposes
            List<LatLng> apartmentLocations = new ArrayList<>();
            apartmentLocations.add(new LatLng(35.2271, -80.8431)); // Charlotte, NC
            apartmentLocations.add(new LatLng(35.2072, -80.8294)); // Another apartment in Charlotte, NC
            // Add more apartment coordinates as needed

            // Add markers for each apartment
            for (LatLng apartmentLocation : apartmentLocations) {
                mMap.addMarker(new MarkerOptions().position(apartmentLocation).title("Apartment"));
            }

            // Move camera to the first apartment location
            if (!apartmentLocations.isEmpty()) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(apartmentLocations.get(0), 12f));
            }
        } else {
            Toast.makeText(getContext(), "Map is not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Check Google Play Services availability
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            googleApiAvailability.getErrorDialog(requireActivity(), resultCode, 0).show();
        }
    }
}
