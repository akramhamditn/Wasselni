package com.esprit.wasselni;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.esprit.wasselni.Chauffeur.MainChauffeurActivity;
import com.esprit.wasselni.Chauffeur.SignupChauffeurActivity;
import com.esprit.wasselni.Client.MainActivity;
import com.esprit.wasselni.Client.SignupActivity;
import com.esprit.wasselni.Entities.Chauffeur;
import com.esprit.wasselni.Entities.Client;
import com.esprit.wasselni.Entities.Trajets;
import com.esprit.wasselni.Entities.User;
import com.esprit.wasselni.sqlite.DBHelper;
import com.esprit.wasselni.sqlite.DBHelperTrajes;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    DBHelper db;
    DBHelperTrajes dbTrajes;
    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_login)
    Button _loginButton;
    @InjectView(R.id.link_signupClient)
    TextView _signupLinkCilent;
    @InjectView(R.id.link_signupChauffeur)
    TextView _signupLinkChauffeur;
    @InjectView(R.id.remeberBox)
    CheckBox rememberMeCbx ;

    SwitchCompat switchCompat;
    String email;
    String password;
    //TODO Set Preferences fileName
    SharedPreferences preferences ;
    boolean swithcOrNo = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //TODO Get Preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        ButterKnife.inject(this);
        _emailText.setText(preferences.getString("Unm", ""));
        _passwordText.setText(preferences.getString("Psw", ""));
        //base données local wa7da trajet w w7da profil
        db = new DBHelper(this);
        dbTrajes = new DBHelperTrajes(this);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(isNetworkConnected() == true){
                    Log.e("Internet","oui");
                    login();
                }else{
                    Log.e("Internet","Non");
                    //recuperer de la base interne
                    boolean cnx = db.getCandidat(_emailText.getText().toString(),_passwordText.getText().toString());
                    if (cnx = true){
                        Intent i = new Intent(LoginActivity.this,MainActivity.class);
                        i.putExtra("SQLITE","oui");
                        startActivity(i);
                    }
                }

            }
        });

        _signupLinkCilent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                //intent.putExtra("type","signup");
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
        _signupLinkChauffeur.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupChauffeurActivity.class);
                //intent.putExtra("type","signup");
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }
//deactiver le boutton login
        //_loginButton.setEnabled(false);
//afficher loading
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();

        // TODO:LOGIN
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Utilisateur");
            query.whereEqualTo("Email", email);
            query.whereEqualTo("Password", password);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> clientList, ParseException e) {
                    if((clientList.size() == 0)){
                        onLoginFailed();
                        progressDialog.hide();
                    }
                    else  if ((clientList.size() != 0)&(clientList.get(0).getString("Type").equals("Client"))) {
                        Log.e("score", "Retrieved " + clientList.size() + " scores");
                        Log.e("score", clientList.get(0).getString("Type"));
                        Client.idObj = clientList.get(0).getObjectId();
                        Client.id = clientList.get(0).getString("IdUser");
                        Log.e("score", Client.id);
                        Client.nom = clientList.get(0).getString("Nom");
                        Log.e("score", clientList.get(0).getString("Nom"));
                        Client.prenom = clientList.get(0).getString("Prenom");
                        Log.e("score", clientList.get(0).getString("Prenom"));
                        Client.adresse = clientList.get(0).getString("Adresse");
                        Log.e("score", clientList.get(0).getString("Adresse"));
                        Client.email = clientList.get(0).getString("Email");
                        Log.e("score", clientList.get(0).getString("Email"));
                        Client.password = clientList.get(0).getString("Password");
                        Log.e("score", clientList.get(0).getString("Password"));
                        Client.telephone = clientList.get(0).getString("Telephone");
                        Log.e("score", String.valueOf(clientList.get(0).getInt("Telephone")));
                        Client.latitude = clientList.get(0).getDouble("Latitude");
                        Client.longitude = clientList.get(0).getDouble("Longitude");
                        //***********com.esprit.wasselni.SQLite User*************
                        User u = new User(Client.id, Client.nom, Client.prenom, Client.adresse, Client.telephone, Client.email, Client.password, "Client");
                        db.insertUser(u);
                        //***********com.esprit.wasselni.SQLite User*************

                        //***********Make user connect*************
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Utilisateur");
                        // Retrieve the object by id
                        query.getInBackground(Client.idObj, new GetCallback<ParseObject>() {
                            public void done(ParseObject testObject, ParseException e) {
                                if (e == null) {
                                    // Now let's update it with some new data. In this case, only cheatMode and score
                                    // will get sent to the Parse Cloud. playerName hasn't changed.
                                    testObject.put("Etat", "Connecter");
                                    testObject.saveInBackground();

                                }
                            }
                        });
                        //***********Make user connect*************

                        //***********com.esprit.wasselni.SQLite Trajes*************
                        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Demande");
                        Log.e("",Client.id);
                        query1.whereEqualTo("CIN", Client.id);
                        query1.whereEqualTo("Etat", "vue");
                        query1.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> trajetsList, ParseException e) {
                                for(int i=0;i<= (trajetsList.size()-1);i++){
                                    //*****************Addresse*****************
                                    Geocoder geocoder;
                                    List<Address> addresses = null;
                                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                                    try {
                                        addresses = geocoder.getFromLocation(trajetsList.get(i).getDouble("Latitude"),trajetsList.get(i).getDouble("Longitude"), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                    } catch (IOException ee) {
                                        e.printStackTrace();
                                    }

                                    String address = addresses.get(0).getAddressLine(0);
                                    //*****************Addresse*****************
                                    Trajets trajets = new Trajets(trajetsList.get(i).getObjectId(),
                                            trajetsList.get(i).getString("CIN"),
                                            trajetsList.get(i).getString("NumeroPermis"),
                                            trajetsList.get(i).getString("Destination"),
                                            address,
                                            trajetsList.get(i).getString("Longitude"),
                                            trajetsList.get(i).getString("LatitudeDestination"),
                                            trajetsList.get(i).getString("LongitudeDestination")
                                            );
                                    trajets.toString();
                                    dbTrajes.insertTrajets(trajets);
                                }
                            }});
                        //***********com.esprit.wasselni.SQLite Trajes*************
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        if (rememberMeCbx.isChecked()) {
                            //TODO Set preferences Value
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("Unm", email);
                            editor.putString("Psw", password);
                            editor.commit();
                        } else {
                            //TODO Set preferences Value
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("Unm", "");
                            editor.putString("Psw", "");
                            editor.commit();
                        }
                        intent.putExtra("SQLITE","non");
                        startActivity(intent);
                    } else if ((clientList.size() != 0)&(clientList.get(0).getString("Type").equals("Chauffeur"))) {
                        Log.e("score", "Retrieved " + clientList.size() + " scores");
                        Log.e("score", clientList.get(0).getString("Type"));
                        Chauffeur.idObj = clientList.get(0).getObjectId();
                        Chauffeur.id = clientList.get(0).getString("IdUser");
                        Chauffeur.nom = clientList.get(0).getString("Nom");
                        Chauffeur.prenom = clientList.get(0).getString("Prenom");
                        Chauffeur.adresse = clientList.get(0).getString("Adresse");
                        Chauffeur.email = clientList.get(0).getString("Email");
                        Chauffeur.password = clientList.get(0).getString("Password");
                        Chauffeur.telephone = clientList.get(0).getString("Telephone");
                        Chauffeur.latitude = clientList.get(0).getDouble("Latitude");
                        Chauffeur.longitude = clientList.get(0).getDouble("Longitude");
                        //***********com.esprit.wasselni.SQLite User*************
                        User u = new User(Chauffeur.id, Chauffeur.nom, Chauffeur.prenom, Chauffeur.adresse, Chauffeur.telephone, Chauffeur.email, Chauffeur.password, "Chauffeur");
                        db.insertUser(u);
                        //***********com.esprit.wasselni.SQLite User*************

                        //***********Make user connect*************
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Utilisateur");
                        // Retrieve the object by id
                        query.getInBackground(Chauffeur.idObj, new GetCallback<ParseObject>() {
                            public void done(ParseObject testObject, ParseException e) {
                                if (e == null) {
                                    // Now let's update it with some new data. In this case, only cheatMode and score
                                    // will get sent to the Parse Cloud. playerName hasn't changed.
                                    testObject.put("Etat", "Connecter");
                                    testObject.saveInBackground();

                                }
                            }
                        });
                        //***********Make user connect*************

                        //***********com.esprit.wasselni.SQLite Trajes*************
                        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Demande");
                        query2.whereEqualTo("NumeroPermis", Chauffeur.id);
                        query2.whereEqualTo("Etat", "vue");
                        query2.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> trajetsList, ParseException e) {
                                for (int i = 0; i <= (trajetsList.size()-1); i++) {
                                    //*****************Addresse*****************
                                    Geocoder geocoder;
                                    List<Address> addresses = null;
                                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                                    try {
                                        addresses = geocoder.getFromLocation(trajetsList.get(i).getDouble("Latitude"),trajetsList.get(i).getDouble("Longitude"), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                    } catch (IOException ee) {
                                        e.printStackTrace();
                                    }

                                    String address = addresses.get(0).getAddressLine(0);
                                    //*****************Addresse*****************
                                    Trajets trajets = new Trajets(trajetsList.get(i).getObjectId(),
                                            trajetsList.get(i).getString("CIN"),
                                            trajetsList.get(i).getString("NumeroPermis"),
                                            trajetsList.get(i).getString("Destination"),
                                            address,
                                            trajetsList.get(i).getString("Longitude"),
                                            trajetsList.get(i).getString("LatitudeDestination"),
                                            trajetsList.get(i).getString("LongitudeDestination")
                                    );
                                    dbTrajes.insertTrajets(trajets);
                                }
                            }
                        });
                        //***********com.esprit.wasselni.SQLite Trajes*************
                        Intent intent = new Intent(getApplicationContext(), MainChauffeurActivity.class);
                        if (rememberMeCbx.isChecked()) {
                            //TODO Set preferences Value
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("Unm", email);
                            editor.putString("Psw", password);
                            editor.commit();
                        } else {
                            //TODO Set preferences Value
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("Unm", "");
                            editor.putString("Psw", "");
                            editor.commit();
                        }
                        intent.putExtra("SQLITE","non");
                        startActivity(intent);
                    } else  {
                        onLoginFailed();
                    }
                }
            });




        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        //onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 30000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();

    }

    public void onLoginFailed() {
       // Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        Snackbar snackbar = Snackbar.make(_loginButton, "Verifier votre Email ou Mot de passe", Snackbar.LENGTH_LONG)
                .setAction("Action", null);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        sbView.setBackgroundColor(0xfffaba10);
        snackbar.show();
    }
//verifier le mail e le pass saisie
    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
    //boutton remeber me checked
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (switchCompat.isChecked()) {
            swithcOrNo = true;
            Log.e("Swiiiiiitch", "swiiiitch");
        }
    }
    //tester internet
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }
    //tester internet

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
