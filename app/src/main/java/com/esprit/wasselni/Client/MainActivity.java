package com.esprit.wasselni.Client;

import android.annotation.TargetApi;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.esprit.wasselni.AboutFragment;
import com.esprit.wasselni.Services.DemandeService;
import com.esprit.wasselni.Outils.GPSTracker;
import com.esprit.wasselni.Services.LocationService;
import com.esprit.wasselni.LoginActivity;
import com.esprit.wasselni.NoWifiFragment;
import com.esprit.wasselni.R;
import com.esprit.wasselni.TrajetsFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import com.esprit.wasselni.Entities.Client;


public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    public static FragmentManager fragmentManager;
    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    static boolean serviceEtat = false;
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i=new Intent(MainActivity.this, DemandeService.class);
        startService(i);
        Intent i1i =new Intent(MainActivity.this, LocationService.class);
        startService(i1i);
        buildGoogleApiClient();
        fragmentManager = getSupportFragmentManager();
        Bundle bundle = getIntent().getExtras();
        //Extract the data…
        String typeCNX = bundle.getString("SQLITE");
        if(typeCNX.equals("oui")){
            NoWifiFragment fragment = new NoWifiFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
        }
        else {
        MapFragment fragment = new MapFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
        }
        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        GPSTracker gpsTracker = new GPSTracker(this);

        if (gpsTracker.getIsGPSTrackingEnabled()!= true) {


            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.inbox:
                        //***********Make user connect*************
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Utilisateur");
                        // Retrieve the object by id
                        query.getInBackground(Client.idObj, new GetCallback<ParseObject>() {
                            public void done(ParseObject testObject, ParseException e) {
                                if (e == null) {
                                    // Now let's update it with some new data. In this case, only cheatMode and score
                                    // will get sent to the Parse Cloud. playerName hasn't changed.
                                    testObject.put("Etat", "Deconnecter");
                                    testObject.saveInBackground();

                                }
                            }
                        });
                        //***********Make user connect*************
                        Intent i = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(i);
                        return true;

                    // For rest of the options we just show a toast on click

                    case R.id.starred:
                        fragmentManager = getSupportFragmentManager();
                        TrajetsFragment fragment1 = new TrajetsFragment();
                        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction1.replace(R.id.frame, fragment1);
                        fragmentTransaction1.commit();
                        return true;
                    case R.id.sent_mail:
                        fragmentManager = getSupportFragmentManager();
                        ProfilClientFragment1 fragment = new ProfilClientFragment1();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.commit();
                        return true;
                    //case R.id.drafts:
                        //Toast.makeText(getApplicationContext(),"Drafts Selected",Toast.LENGTH_SHORT).show();
                        //return true;
                    case R.id.allmail:
                        fragmentManager = getSupportFragmentManager();
                        AboutFragment fragment2 = new AboutFragment();
                        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.frame, fragment2);
                        fragmentTransaction2.commit();
                        return true;
                    case R.id.map:
                        fragmentManager = getSupportFragmentManager();
                        MapFragment fragment3 = new MapFragment();
                        FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction3.replace(R.id.frame, fragment3);
                        fragmentTransaction3.commit();
                        return true;
                   /* case R.id.spam:
                        Toast.makeText(getApplicationContext(),"Spam Selected",Toast.LENGTH_SHORT).show();
                        return true;*/
                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();






    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("uiii","nnn");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    //recuperer lan et lalt de ta position dans la map tab3ek
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    //recuperer votre la et long
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Log.e("MainActivity", String.valueOf(mLastLocation.getLatitude()));
            Log.e("MainActivity", String.valueOf(mLastLocation.getLongitude()));
           /* mLatitudeText.setText(String.format("%s: %f", mLatitudeLabel,
                    mLastLocation.getLatitude()));
            mLongitudeText.setText(String.format("%s: %f", mLongitudeLabel,
                    mLastLocation.getLongitude()));*/
        } else {
            Toast.makeText(this,"No location detected. Make sure location is enabled on the device.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i("MainAtivity", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i("MainAtivity", "Connection suspended");
        mGoogleApiClient.connect();
    }
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}
