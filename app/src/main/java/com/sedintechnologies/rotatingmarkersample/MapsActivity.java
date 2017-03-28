package com.sedintechnologies.rotatingmarkersample;

import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.view.animation.LinearInterpolator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

  private GoogleMap mMap;
  private Marker marker;
  private Handler handler = new Handler();
  int i = 10;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment =
        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
    handler.postDelayed(runnable, 5000);
  }

  @Override public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;

    // Add a marker in Sydney and move the camera
    LatLng sydney = new LatLng(-34, 151);
    marker = mMap.addMarker(new MarkerOptions().position(sydney).title("Perth"));
    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.car));
    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
  }

  Runnable runnable = new Runnable() {
    @Override public void run() {
      rotateMarker(marker,(i = i + 5),5);
      handler.postDelayed(this, 2000);
    }
  };

  public void rotateMarker(final Marker marker, final float toRotation, final float st) {
    final Handler handler = new Handler();
    final long start = SystemClock.uptimeMillis();
    final float startRotation = st;
    final long duration = 1555;

    final LinearInterpolator interpolator = new LinearInterpolator();

    handler.post(new Runnable() {
      @Override
      public void run() {
        long elapsed = SystemClock.uptimeMillis() - start;
        float t = interpolator.getInterpolation((float) elapsed / duration);

        float rot = t * toRotation + (1 - t) * startRotation;

        marker.setRotation(-rot > 180 ? rot / 2 : rot);
        if (t < 1.0) {
          // Post again 16ms later.
          handler.postDelayed(this, 16);
        }
      }
    });
  }


  public void animateMarker(final LatLng toPosition,final boolean hideMarke) {
    final Handler handler = new Handler();
    final long start = SystemClock.uptimeMillis();
    Projection proj = mMap.getProjection();
    Point startPoint = proj.toScreenLocation(marker.getPosition());
    final LatLng startLatLng = proj.fromScreenLocation(startPoint);
    final long duration = 5000;

    final LinearInterpolator interpolator = new LinearInterpolator();

    handler.post(new Runnable() {
      @Override
      public void run() {
        long elapsed = SystemClock.uptimeMillis() - start;
        float t = interpolator.getInterpolation((float) elapsed
            / duration);
        double lng = t * toPosition.longitude + (1 - t)
            * startLatLng.longitude;
        double lat = t * toPosition.latitude + (1 - t)
            * startLatLng.latitude;
        marker.setPosition(new LatLng(lat, lng));

        if (t < 1.0) {
          // Post again 16ms later.
          handler.postDelayed(this, 16);
        } else {
          if (hideMarke) {
            marker.setVisible(false);
          } else {
            marker.setVisible(true);
          }
        }
      }
    });
  }
}
