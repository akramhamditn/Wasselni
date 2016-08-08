package com.esprit.wasselni;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class ListFragment extends Fragment {


    static final LatLng ESPRIT = new LatLng(36.8984864, 10.1875732);
    static LatLng KIEL = new LatLng(53.551, 9.993);
    private GoogleMap map;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, container, false);

        // map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map))
        //        .getMap();
        FragmentManager myFM = getActivity().getSupportFragmentManager();

        /*final SupportMapFragment mapp = (SupportMapFragment) myFM
                .findFragmentById(R.id.map);
        map = mapp.getMap();*/
        map = ((SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map)).getMap();

        //***************************************************
        /*LocationManager service = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return null;
        }
        Location location = service.getLastKnownLocation(provider);*/

        //***************************************************





        Marker hamburg = map.addMarker(new MarkerOptions().position(ESPRIT)
                .title("Hamburg"));

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Utilisateur");
        query.whereEqualTo("Type", "Chauffeur");
        query.whereEqualTo("Etat", "Connecter");
        //query.whereEqualTo("Type", "Client");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> clientList, ParseException e) {

                if ((e == null) && (clientList.size() != 0)) {
                    Log.e("score", "Retrieved " + clientList.size() + " scores");
                    Log.e("score", clientList.get(0).getString("Type"));
                    for (int i= 0;i<clientList.size();i++){
                        //Log.e("Lon",clientList.get(i).getString("Longitude"));
                        //Log.e("Lan",clientList.get(i).getDouble("Latitude"));
                        double Lon = clientList.get(i).getDouble("Longitude");
                        double Lat = clientList.get(i).getDouble("Latitude");
                         KIEL = new LatLng(Lat, Lon);
                        Marker kiel = map.addMarker(new MarkerOptions()
                                .position(KIEL)
                                .title("Kiel")
                                .snippet("Kiel is cool")
                                .icon(BitmapDescriptorFactory
                                        .fromResource(R.drawable.mapmarkertaxi)));
                    }
                } else {
                    Log.e("score", "Client ");

                }
            }
        });







        Marker kiel = map.addMarker(new MarkerOptions()
                .position(KIEL)
                .title("Kiel")
                .snippet("Kiel is cool")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.check)));

        // Move the camera instantly to hamburg with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ESPRIT, 20));

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);

        //...

        return v;
    }
}
