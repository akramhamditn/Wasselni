package com.esprit.wasselni.Chauffeur;

/**
 * Created by hamdi on 14/11/2015.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.esprit.wasselni.Adapters.DirectionsJSONParser;
import com.esprit.wasselni.Entities.Chauffeur;
import com.esprit.wasselni.Entities.Demande;
import com.esprit.wasselni.Outils.ProgressHelper;
import com.esprit.wasselni.R;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mbanje.kurt.fabbutton.FabButton;

//import android.location.Location;

public class MapChauffeurDetailsFragment extends Fragment implements LocationListener{

    private FABProgressCircle fabProgressCircle;
    private boolean taskRunning;

    static final LatLng ESPRIT = new LatLng(36.8999119,10.1882127);
    static LatLng KIEL = new LatLng(53.551, 9.993);
    private GoogleMap map;
    TextView _nomDet;
    TextView _desDet;
    TextView _disDet;
    TextView _prixDet;
    TextView _telDet;
    File imagePath;
    Bitmap bitmap;
    //LocaFabion location;
    ArrayList<LatLng> markerPoints;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map_detail, container, false);
       // FloatingActionButton myFab = (FloatingActionButton) v.findViewById(R.id.fab);
        final FabButton myFab = (FabButton) v.findViewById(R.id.fab);
        final ImageButton myImg = (ImageButton) v.findViewById(R.id.share);
        _desDet = (TextView) v.findViewById(R.id.desDet);
        _disDet = (TextView) v.findViewById(R.id.disDet);
        _prixDet = (TextView) v.findViewById(R.id.prixDet);
        _nomDet = (TextView) v.findViewById(R.id.nomDet);
        _telDet = (TextView) v.findViewById(R.id.telDet);
        //fabProgressCircle = (FABProgressCircle) v.findViewById(R.id.fabProgressCircle);
        final ProgressHelper helper = new ProgressHelper(myFab,getActivity());
        FragmentManager myFM = getActivity().getSupportFragmentManager();
        final String item =getArguments().getString("id");
        String prix =getArguments().getString("prix");
        String dis =getArguments().getString("dis");
        String des =getArguments().getString("des");

        _desDet.setText(des);
        if(Double.valueOf(dis) > 100.0) {
            _disDet.setText("distance non calculé");

        }else {
            _disDet.setText(dis + " Km");

        }
        if(Double.valueOf(prix) > 100.0) {
            _prixDet.setText("prix non calculé");
        }else {
            _prixDet.setText(prix+"0 Dt");
        }

        double latDes = getArguments().getDouble("latDes");
        double lonDes = getArguments().getDouble("lonDes");
        map = ((SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);
        Log.e("Iccccccci", String.valueOf(Chauffeur.latitude));
        Log.e("Iccccccci", String.valueOf(Chauffeur.longitude));
        //*******************Get corse******************
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Utilisateur");
        query.whereEqualTo("IdUser", getArguments().getString("idUser"));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                _nomDet.setText(objects.get(0).getString("Nom") + " " + objects.get(0).getString("Prenom"));
                _telDet.setText(objects.get(0).getString("Telephone"));
            }

        });
        _telDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + _telDet.getText()));
                startActivity(callIntent);
            }
        });
        //*******************Get corse******************
        Marker hamburg = map.addMarker(new MarkerOptions().position(new LatLng(Chauffeur.latitude,Chauffeur.longitude))
                .title("Hamburg")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.mapmarkertaxi)));
        Marker hamburg1 = map.addMarker(new MarkerOptions().position(new LatLng(latDes, lonDes))
                .title("Hamburg")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.markerhome)));
        //Marker hamburgg = map.addMarker(new MarkerOptions().position(new LatLng(ESPRIT.latitude,ESPRIT.longitude))
              //  .title("Hamburg"));
        //Log.e("Map positon", String.valueOf(location.getLatitude()));
        //Log.e("Ma positon", String.valueOf(location.getLongitude()));
        // Move the camera instantly to hamburg with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Chauffeur.latitude,Chauffeur.longitude), 10));

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        //...

        String url = getDirectionsUrl(new LatLng(Demande.latitude, Demande.longitude), new LatLng(Chauffeur.latitude,Chauffeur.longitude));

        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
        myImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureScreen();
                openShareImageDialog(imagePath);
            }
        });
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Demande");
                // Retrieve the object by id
                query.getInBackground(item, new GetCallback<ParseObject>() {
                    public void done(ParseObject gameScore, ParseException e) {
                        if (e == null) {
                            // Now let's update it with some new data. In this case, only cheatMode and score
                            // will get sent to the Parse Cloud. playerName hasn't changed.
                            gameScore.put("Etat", "accepter");
                            gameScore.put("NumeroPermis", Chauffeur.id);
                            gameScore.saveInBackground();

                        }
                    }
                });
                helper.startDeterminate();
                myFab.setEnabled(false);
            }
        });
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
    public void captureScreen() {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                try {
                    getView().setDrawingCacheEnabled(true);
                    Bitmap backBitmap = getView().getDrawingCache();
                    Bitmap bmOverlay = Bitmap.createBitmap(
                            backBitmap.getWidth(), backBitmap.getHeight(),
                            backBitmap.getConfig());
                    Canvas canvas = new Canvas(bmOverlay);
                    canvas.drawBitmap(snapshot, new Matrix(), null);
                    canvas.drawBitmap(backBitmap, 0, 0, null);
                     //out = new FileOutputStream(
                           // Environment.getExternalStorageDirectory()
                                   // + "/MapScreenShot"
                                    //+ System.currentTimeMillis() + ".png");
                    imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
                    FileOutputStream fos;
                    fos = new FileOutputStream(imagePath);
                    bmOverlay.compress(Bitmap.CompressFormat.PNG, 90, fos);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        map.snapshot(callback);

    }
    public void openShareImageDialog(File imagePath)
    {
        File file = new File(String.valueOf(imagePath));
        Intent share = new Intent(Intent.ACTION_SEND);

        // If you want to share a png image only, you can do:
        // setType("image/png"); OR for jpeg: setType("image/jpeg");
        share.setType("image/*");

        // Make sure you put example png image named myImage.png in your
        // directory
        //String imagePath = Environment.getExternalStorageDirectory()
        //+ "/myImage.png";

        //File imageFileToShare = new File(imagePath);
        Uri uri = Uri.fromFile(file);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Image!"));
    }

}
