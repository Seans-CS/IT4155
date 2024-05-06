package com.example.mytestapp;

import android.content.Context;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;

public class ApartmentsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<Apartment> apartments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_apartments, container, false);

        listView = rootView.findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        apartments = new ArrayList<>();
        apartments.add(new Apartment("Rush Student Living", new LatLng(35.3026, -80.7276)));
        apartments.add(new Apartment("University Crossings Charlotte", new LatLng(35.3005, -80.7323)));
        apartments.add(new Apartment("The Edge", new LatLng(35.3107, -80.7245)));

        for (Apartment apartment : apartments) {
            adapter.add(apartment.getName());
        }

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.mapContainer, mapFragment).commit();
        mapFragment.getMapAsync(this);

        // Handle list item clicks
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Apartment selectedApartment = apartments.get(position);
                LatLng location = selectedApartment.getLocation();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 6f));
            }
        });

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mMap != null) {
            mMap.setOnMarkerClickListener(this);

            for (Apartment apartment : apartments) {
                mMap.addMarker(new MarkerOptions().position(apartment.getLocation()).title(apartment.getName()));
            }

            if (!apartments.isEmpty()) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(apartments.get(0).getLocation(), 12f));
            }
        } else {
            Toast.makeText(getContext(), "Map is not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Handle marker click
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            googleApiAvailability.getErrorDialog(requireActivity(), resultCode, 0).show();
        }
    }

    private static class Apartment {
        private String name;
        private LatLng location;

        public Apartment(String name, LatLng location) {
            this.name = name;
            this.location = location;
        }

        public String getName() {
            return name;
        }

        public LatLng getLocation() {
            return location;
        }
    }
}
