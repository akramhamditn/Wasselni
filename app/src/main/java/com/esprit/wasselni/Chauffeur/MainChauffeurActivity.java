package com.esprit.wasselni.Chauffeur;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.esprit.wasselni.AboutFragment;
import com.esprit.wasselni.Services.DemandeService;
import com.esprit.wasselni.Entities.Chauffeur;
import com.esprit.wasselni.Outils.GPSTracker;
import com.esprit.wasselni.Services.LocationService;
import com.esprit.wasselni.LoginActivity;
import com.esprit.wasselni.NoWifiFragment;
import com.esprit.wasselni.R;
import com.esprit.wasselni.TrajetsFragment;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MainChauffeurActivity extends AppCompatActivity {
    public static FragmentManager fragmentManager;
    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private boolean disableEvent;
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chauffeur);
        Intent ii =new Intent(MainChauffeurActivity.this, DemandeService.class);
        stopService(ii);
        Intent i1i =new Intent(MainChauffeurActivity.this, LocationService.class);
        startService(i1i);
        disableEvent = true;
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
            DifferentMenuFragment fragment = new DifferentMenuFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
        }

        //**************************************************************************
        // check if GPS enabled
        GPSTracker gpsTracker = new GPSTracker(this);

        if (gpsTracker.getIsGPSTrackingEnabled()!= true) {


            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }

        //**************************************************************************
      /*  LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider;
        provider = locationManager.getBestProvider(criteria, true);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }*/
        /*Location myLocation = locationManager.getLastKnownLocation(provider);
        //##########################################################################
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10,);
        myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        double longitude = myLocation.getLongitude();
        double latitude = myLocation.getLatitude();
        Log.e("Latitude", String.valueOf(latitude));
        Log.e("Longitude", String.valueOf(longitude));*/
        //##########################################################################

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

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
                        query.getInBackground(Chauffeur.idObj, new GetCallback<ParseObject>() {
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
                        Intent i = new Intent(MainChauffeurActivity.this,LoginActivity.class);
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
                        ProfilChauffeurFragment1 fragment = new ProfilChauffeurFragment1();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.commit();
                        return true;
                    //case R.id.drafts:
                      //  Toast.makeText(getApplicationContext(),"Drafts Selected",Toast.LENGTH_SHORT).show();
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
                        DifferentMenuFragment fragment3 = new DifferentMenuFragment();
                        FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction3.replace(R.id.frame, fragment3);
                        fragmentTransaction3.commit();
                        return true;
                    /*case R.id.trash:
                        Toast.makeText(getApplicationContext(),"Trash Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.spam:
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

   /* @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }*/
}
