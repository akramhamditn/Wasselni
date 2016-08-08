package com.esprit.wasselni.Chauffeur;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.esprit.wasselni.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import com.esprit.wasselni.Entities.Chauffeur;


public class ProfilChauffeurFragment2 extends Fragment {



    EditText _emailText ;
    EditText _passwordText;
    EditText _adresseText,_numCarte;
    String name,lastname,numTel,numPermis,email,password,addresse,numCarte;
    Button _SignupButton;
    int i;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_fragment_modifier_chauffeur2,container,false);

        _emailText = (EditText) v.findViewById(R.id.input_emailChauffeur);
        _passwordText= (EditText) v.findViewById(R.id.input_passwordChauffeur);
        _adresseText= (EditText) v.findViewById(R.id.input_adressChauffeur);
       // _numCarte= (EditText) v.findViewById(R.id.input_numCarteChauffeur);
        _SignupButton= (Button) v.findViewById(R.id.btn_signupChauffeur);


        _emailText.setText(Chauffeur.email);
        _passwordText.setText(Chauffeur.password);
        _adresseText.setText(Chauffeur.adresse);
        name = getArguments().getString("nom");
        lastname = getArguments().getString("prenom");
        numTel = getArguments().getString("numTel");
        numPermis = getArguments().getString("numPermis");
        _SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = _emailText.getText().toString();
                password = _passwordText.getText().toString();
                addresse = _adresseText.getText().toString();
                //numCarte = _numCarte.getText().toString();
                i = Integer.parseInt(numTel);
                if(validate()) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Utilisateur");
                    // Retrieve the object by id
                    query.getInBackground(Chauffeur.idObj, new GetCallback<ParseObject>() {
                        public void done(ParseObject testObject, ParseException e) {
                            if (e == null) {
                                // Now let's update it with some new data. In this case, only cheatMode and score
                                // will get sent to the Parse Cloud. playerName hasn't changed.
                                testObject.put("Nom", name);
                                Log.e("", name);
                                testObject.put("Prenom", lastname);
                                Log.e("", lastname);
                                testObject.put("Email", email);
                                Log.e("", email);
                                testObject.put("Password", password);
                                Log.e("", password);
                                testObject.put("Adresse", addresse);
                                Log.e("", addresse);
                                testObject.put("NumeroPermis", numPermis);
                                Log.e("", numPermis);
                                testObject.put("Telephone", String.valueOf(i));
                                Log.e("", String.valueOf(i));
                                //testObject.put("IdUser", numCarte);
                                testObject.saveInBackground();
                            }
                        }
                    });
                    final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Update...");
                    progressDialog.show();
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {

                                    // onLoginFailed();
                                    progressDialog.dismiss();
                                }
                            }, 3000);
                }
            }
        });
        return v;
    }
    public boolean validate() {
        boolean valid = true;



        if (email.isEmpty()|| !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("exemple@exemple.com");
            valid = false;
        } else {
            _emailText.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("entre 4 et 10 characters alphanumeric ");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (addresse.isEmpty() || addresse.length() < 4 || addresse.length() > 10 ) {
            _adresseText.setError("entre 4 et 10 characters");
            valid = false;
        } else {
            _adresseText.setError(null);
        }


        return valid;
    }
}
