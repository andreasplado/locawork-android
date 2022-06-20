package org.locawork.util;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class GooglemapUtil {

    private LocationUtil locationUtil;

    public GooglemapUtil(LocationUtil locationUtil){
        this.locationUtil = locationUtil;
    }

    public void centerCamera(GoogleMap googleMap, LatLng latLng) {
        CameraPosition obj_cam = new CameraPosition.Builder()
                .target(latLng)
                .zoom(googleMap.getCameraPosition().zoom)
                .tilt(googleMap.getCameraPosition().tilt)
                .bearing(googleMap.getCameraPosition().bearing)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(obj_cam));
    }
}
