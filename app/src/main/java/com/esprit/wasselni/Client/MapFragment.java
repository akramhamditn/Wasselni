package com.esprit.wasselni.Client;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.esprit.wasselni.R;
import com.esprit.wasselni.Entities.Client;
import com.esprit.wasselni.Outils.ProgressHelper;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import mbanje.kurt.fabbutton.FabButton;


public class MapFragment extends Fragment implements GoogleMap.OnMapLongClickListener,GoogleMap.OnMapClickListener,LocationListener {

    private SupportMapFragment mSupportMapFragment;
    private GoogleMap mGoogleMap;
    String address,state,city;
    EditText adresse;
    static final LatLng ESPRIT = new LatLng(36.8984864, 10.1875732);
    static LatLng KIEL = new LatLng(53.551, 9.993);
    private GoogleMap map;
    private double demandeLal;
    private double demandeLon;
    Marker marker;
    Dialog match_text_dialog;
    ListView textlist;
    ArrayList<String> matches_text;
    private static final int REQUEST_CODE = 1234;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, container, false);
        adresse = (EditText) v.findViewById(R.id.input_addr);
        final FabButton myFab = (FabButton) v.findViewById(R.id.fab_adresse);
        final ProgressHelper helper = new ProgressHelper(myFab,getActivity());
        final ImageButton myImg = (ImageButton) v.findViewById(R.id.share);
        //FragmentManager myFM = getActivity().getSupportFragmentManager();

        map = ((SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setOnMapLongClickListener(this);
        map.setMyLocationEnabled(true);
        final Handler handler = new Handler();

        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Utilisateur");
        query.whereEqualTo("Type", "Chauffeur");
        query.whereEqualTo("Etat", "Connecter");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> clientList, ParseException e) {

                if ((e == null) && (clientList.size() != 0)) {
                    Log.e("score", "Retrieved " + clientList.size() + " scores");
                    Log.e("score", clientList.get(0).getString("Type"));
                    for (int i = 0; i < clientList.size(); i++) {
                        //Log.e("Lon",clientList.get(i).getString("Longitude"));
                        //Log.e("Lan",clientList.get(i).getDouble("Latitude"));
                        double Lon = clientList.get(i).getDouble("Longitude");
                        double Lat = clientList.get(i).getDouble("Latitude");
                        KIEL = new LatLng(Lat, Lon);
                        Marker kiel = map.addMarker(new MarkerOptions()
                                .position(KIEL)
                                .title(clientList.get(i).getString("Nom"))
                                .snippet(clientList.get(i).getString("Prenom"))
                                .icon(BitmapDescriptorFactory
                                        .fromResource(R.drawable.mapmarkertaxi)));
                    }
                } else {
                    Log.e("score", "Client ");

                }
            }
        });
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(timertask, 0, 10000);
        // Move the camera instantly to hamburg with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Client.latitude, Client.longitude), 10));

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);

        //...
        myImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()){
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    startActivityForResult(intent, REQUEST_CODE);
                }
                else{
                    Toast.makeText(getContext(), "Plese Connect to Internet", Toast.LENGTH_LONG).show();
                }
            }
        });
        if(!(adresse.getText().toString().equals(null))){
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if((adresse.getText().toString()).equals("")) {
                    Log.e("addreeeeesssssssssseee",adresse.getText().toString());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(v.getContext(), R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Réservation");
                    builder.setMessage("Il faut choisir un destination");
                    builder.setPositiveButton("OK", null);
                    builder.show();


                }
                else{
                    ParseObject testObject = new ParseObject("Demande");
                    testObject.put("CIN", Client.id);
                    testObject.put("Latitude", Client.latitude);
                    testObject.put("Longitude", Client.longitude);
                    testObject.put("LatitudeDestination", demandeLal);
                    testObject.put("LongitudeDestination", demandeLon);
                    testObject.put("Destination", adresse.getText().toString());
                    testObject.put("Etat", "demande");
                    testObject.saveInBackground();
                    helper.startDeterminate();
                    myFab.setEnabled(false);
                }
            }
        });

        }
        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE ) {

            match_text_dialog = new Dialog(getActivity());
            match_text_dialog.setContentView(R.layout.dialog_matches_frag);
            match_text_dialog.setTitle("Select Matching Text");
            textlist = (ListView)match_text_dialog.findViewById(R.id.list);
            matches_text = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            ArrayAdapter<String> adapter =    new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1, matches_text);
            textlist.setAdapter(adapter);
            textlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    adresse.setText(matches_text.get(position));
                    match_text_dialog.hide();
                }
            });
            match_text_dialog.show();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onMapLongClick(LatLng latLng) {
        if (marker != null) {
            marker.remove();
        }
        marker = map.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Destination")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

        double st =  latLng.longitude;
        double stt =  latLng.latitude;
        demandeLal = latLng.latitude;
        demandeLon = latLng.longitude;
        Log.i("", String.valueOf(st));
        Log.i("", String.valueOf(stt));
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this.getContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(stt,st, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

         address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
         city = addresses.get(0).getLocality();
         state = addresses.get(0).getAdminArea();
        Log.e("address", state);
//        Log.e("address",city);
        Log.e("address", address);
        adresse.setText(address);
        Log.e("address", adresse.getText().toString());
    }
    public  boolean isConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net!=null && net.isAvailable() && net.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onLocationChanged(Location location) {
        map.clear();

        MarkerOptions mp = new MarkerOptions();

        mp.position(new LatLng(location.getLatitude(), location.getLongitude()));

        mp.title("my position");

        map.addMarker(mp);

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 16));
        Log.e("Alt", String.valueOf(location.getLatitude()));
        Log.e("Lon", String.valueOf(location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager mFragmentManager = getChildFragmentManager();
        mSupportMapFragment = (SupportMapFragment) mFragmentManager.findFragmentById(R.id.map);

    }


}
