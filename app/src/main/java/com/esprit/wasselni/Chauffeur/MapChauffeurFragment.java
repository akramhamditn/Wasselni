package com.esprit.wasselni.Chauffeur;

/**
 * Created by hamdi on 14/11/2015.
 */

import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esprit.wasselni.Adapters.DirectionsJSONParser;
import com.esprit.wasselni.Entities.Chauffeur;
import com.esprit.wasselni.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import android.location.Location;

public class MapChauffeurFragment extends Fragment implements LocationListener {


    static final LatLng ESPRIT = new LatLng(36.8999119,10.1882127);
    static LatLng KIEL = new LatLng(53.551, 9.993);
    private GoogleMap map;
    //Location location;
    ArrayList<LatLng> markerPoints;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map_path, container, false);
        FragmentManager myFM = getActivity().getSupportFragmentManager();
        double latDes = getArguments().getDouble("latDes");
        double lonDes = getArguments().getDouble("lonDes");
        map = ((SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true);
        Log.e("Iccccccci", String.valueOf(Chauffeur.latitude));
        Log.e("Iccccccci", String.valueOf(Chauffeur.longitude));
        Marker hamburg = map.addMarker(new MarkerOptions().position(new LatLng(Chauffeur.latitude, Chauffeur.longitude))
                .title("Hamburg")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.mapmarkertaxi)));

        Marker hamburg1 = map.addMarker(new MarkerOptions().position(new LatLng(latDes, lonDes))
                .title("Hamburg")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.markerhome)));
        Marker hamburgg = map.addMarker(new MarkerOptions().position(new LatLng(ESPRIT.latitude,ESPRIT.longitude))
                .title("Hamburg"));
        //Log.e("Map positon", String.valueOf(location.getLatitude()));
        //Log.e("Ma positon", String.valueOf(location.getLongitude()));


        // Move the camera instantly to hamburg with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Chauffeur.latitude,Chauffeur.longitude), 10));

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        //...

        String url = getDirectionsUrl(ESPRIT, new LatLng(Chauffeur.latitude,Chauffeur.longitude));

        DownloadTask downloadTask = new DownloadTask();

        downloadTask.execute(url);
        return v;
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
        Log.e("Map positon", String.valueOf(location.getLatitude()));
        Log.e("Ma positon", String.valueOf(location.getLongitude()));


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

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            //Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }


    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.BLUE);
            }
            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
        }
    }

}
