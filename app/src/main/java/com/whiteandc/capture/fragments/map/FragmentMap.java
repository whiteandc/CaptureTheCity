package com.whiteandc.capture.fragments.map;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.whiteandc.capture.data.MonumentList;
import com.whiteandc.capture.data.MonumentLoader;


/**
 * Created by Blanca on 21-01-2015.
 */
public class FragmentMap extends MapFragment {

    static final LatLng MADRID = new LatLng(40.427505, -3.705286);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        GoogleMap googleMap= getMap();

        addMarkers(googleMap);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MADRID, 14));
        //googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        return view;
    }

    private void addMarkers(GoogleMap map) {
        addOneMarker(map, MonumentLoader.FUENTE_CIBELES);
        addOneMarker(map, MonumentLoader.PUERTA_ALCALA);
        addOneMarker(map, MonumentLoader.CALLE_ALCALA);
        addOneMarker(map, MonumentLoader.CATEDRAL_ALMUDENA);
        addOneMarker(map, MonumentLoader.TEMPLO_DEBOD);
        addOneMarker(map, MonumentLoader.PALACIO_REAL);
        addOneMarker(map, MonumentLoader.PLAZA_MAYOR);
        addOneMarker(map, MonumentLoader.RETIRO);
        addOneMarker(map, MonumentLoader.SOL);
        addOneMarker(map, MonumentLoader.PALACIO_COMU);
        addOneMarker(map, MonumentLoader.TEATRO);
        addOneMarker(map, MonumentLoader.TELEFERICO);
    }

    private void addOneMarker(GoogleMap map, String monument) {
        LatLng latLng= MonumentList.getMonument(monument).getLatLng();
        Marker monumentMarker = map.addMarker(new MarkerOptions().position(latLng)
                .title(monument));
    }


}