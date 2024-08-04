package com.rninteractivemaps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;

public class MapViewManager extends SimpleViewManager<MapView> {
    private static final String REACT_CLASS = "MapView";
    private MapView mapView;
    private GoogleMap googleMap;
    private List<Marker> markers = new ArrayList<>();
    private Marker currentMarker;
    private ReactApplicationContext reactContext;

    public MapViewManager(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected MapView createViewInstance(ThemedReactContext reactContext) {
        mapView = new MapView(reactContext);
        mapView.onCreate(null);
        mapView.onResume();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        WritableMap event = Arguments.createMap();
                        event.putDouble("lat", marker.getPosition().latitude);
                        event.putDouble("lng", marker.getPosition().longitude);
                        event.putString("title", marker.getTitle());
                        event.putString("id", marker.getSnippet());
                        marker.showInfoWindow();
                        pushEvent(reactContext, "onMarkerPress", event);
                        return true;
                    }
                });
            }
        });
        return mapView;
    }

    private void pushEvent(ThemedReactContext context, String name, WritableMap data) {
        context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(name, data);
    }

    @ReactProp(name = "markers")
    public void setMarkers(MapView view, ReadableArray markersArray) {
        view.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.clear();
                markers.clear();
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                for (int i = 0; i < markersArray.size(); i++) {
                    ReadableMap marker = markersArray.getMap(i);
                    double lat = marker.getDouble("lat");
                    double lng = marker.getDouble("lng");
                    LatLng position = new LatLng(lat, lng);
                    MarkerOptions markerOptions = new MarkerOptions().position(position).title(marker.getString("title")).snippet(marker.getString("id"));

                    Bitmap originalBitmap = BitmapFactory.decodeResource(mapView.getContext().getResources(), R.drawable.marker);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 80, 140, false);
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(scaledBitmap));

                    Marker newMarker = googleMap.addMarker(markerOptions);
                    markers.add(newMarker);
                    builder.include(position);
                }

                LatLngBounds bounds = builder.build();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
        });
    }

    @ReactProp(name = "currentMarker")
    public void setCurrentMarker(MapView view, ReadableMap currentMarkerMap) {
      view.getMapAsync(new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
          if (currentMarkerMap != null) {
            double lat = currentMarkerMap.getDouble("lat");
            double lng = currentMarkerMap.getDouble("lng");
            LatLng position = new LatLng(lat, lng);

            currentMarker = null;
            for (Marker marker : markers) {
              if (marker.getPosition().equals(position)) {
                currentMarker = marker;
                break;
              }
            }

            if (currentMarker != null) {
              currentMarker.showInfoWindow();
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
          }
        };
      });
        // if (currentMarkerMap != null) {
        //     double lat = currentMarkerMap.getDouble("lat");
        //     double lng = currentMarkerMap.getDouble("lng");
        //     LatLng position = new LatLng(lat, lng);

        //     currentMarker = null;
        //     for (Marker marker : markers) {
        //         if (marker.getPosition().equals(position)) {
        //             currentMarker = marker;
        //             break;
        //         }
        //     }

        //     if (currentMarker != null) {
        //         currentMarker.showInfoWindow();
        //     }

        //     googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        // }
    }

    @ReactProp(name = "zoom")
    public void setZoom(MapView view, float zoom) {
      view.getMapAsync(new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
          googleMap.moveCamera(CameraUpdateFactory.zoomTo(zoom));
        }
      });
    }
}
