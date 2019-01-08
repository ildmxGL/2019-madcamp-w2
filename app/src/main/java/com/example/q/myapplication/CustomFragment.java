package com.example.q.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;


public class CustomFragment extends Fragment {

    private int timer = 0;
    private GoogleMap googleMap;
    private SupportMapFragment mapView;
    private LatLng ex_point;
    private LatLng current_point;
    private boolean isInit = false;
    private boolean mapsSupported = true;

    private boolean isBtnClickStart = false;
    Handler handler;
    Handler time_handler;

    private TextView tv_timer;


    void setupMap() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            Toast.makeText(getContext(), "위치 권한이 없습니다. 설정에서 권한 요청을 승인해주세요.", Toast.LENGTH_SHORT).show();
        else {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            LocationServices.getFusedLocationProviderClient(getActivity()).getLastLocation().addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                }
            });
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_custom, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MarkerOptions markerOptions = new MarkerOptions();

                CustomFragment.this.googleMap = googleMap;
                googleMap.moveCamera(CameraUpdateFactory.zoomTo(1));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.549573, 126.989079)));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));


                // Permission check
                ArrayList<String> permissionList = new ArrayList<>();
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                else
                    setupMap();
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    permissionList.add(Manifest.permission.CALL_PHONE);

                String[] permissionRequests = new String[permissionList.size()];
                for (int i = 0; i < permissionRequests.length; i++) {
                    permissionRequests[i] = permissionList.get(i);
                }
                if (permissionRequests.length > 0)
                    ActivityCompat.requestPermissions(getActivity(), permissionRequests, 0);
            }
        });

        View rootView = getView();

        ex_point = new LatLng(0, 0);
        current_point = new LatLng(0, 0);
        final Button btn_timer_start = (Button) rootView.findViewById(R.id.btn_timer_start);
        final Button btn_timer_finish = (Button) rootView.findViewById(R.id.btn_timer_finish);
        final Button btn_timer_reset = (Button) rootView.findViewById(R.id.btn_timer_reset);
        tv_timer = (TextView) rootView.findViewById(R.id.tv_timer);

        btn_timer_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_timer_start) {
                    if (isBtnClickStart == true) {
                        Toast.makeText(getActivity(), "이미 시작되었습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(getActivity(), "타이머를 시작합니다.", Toast.LENGTH_SHORT).show();
                    isBtnClickStart = true;

                }
            }
        });

        btn_timer_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_timer_finish) {
                    if (isBtnClickStart == true) {
                        Toast.makeText(getActivity(), "타이머를 멈춥니다.", Toast.LENGTH_SHORT).show();
                        time_handler.removeMessages(0);
                        isBtnClickStart = false;

                    } else {
                        Toast.makeText(getActivity(), "타이머가 시작되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_timer_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_timer_reset) {
                    if (isBtnClickStart != true) {
                        ;
                    } else {
                        time_handler.removeMessages(0);
                    }
                    Toast.makeText(getActivity(), "타이머를 리셋합니다.", Toast.LENGTH_SHORT).show();
                    timer = 0;
                    tv_timer.setText("주행시간: " + timer + "초");
                    isBtnClickStart = false;
                }
            }
        });
    }
}