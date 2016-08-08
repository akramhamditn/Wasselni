package com.esprit.wasselni.Chauffeur;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.esprit.wasselni.Entities.Chauffeur;
import com.esprit.wasselni.R;
import com.parse.ParseObject;


public class SignUpChauffeurFragment2 extends Fragment {



    EditText _emailText ;
    EditText _passwordText;
    EditText _adresseText,_numCarte;
    String name,lastname,numTel,numPermis,email,password,addresse,numCarte;
    Button _SignupButton;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_fragment_singup_chauffeur2,container,false);

        _emailText = (EditText) v.findViewById(R.id.input_emailChauffeur);
        _passwordText= (EditText) v.findViewById(R.id.input_passwordChauffeur);
        _adresseText= (EditText) v.findViewById(R.id.input_adressChauffeur);
       // _numCarte= (EditText) v.findViewById(R.id.input_numCarteChauffeur);
        _SignupButton= (Button) v.findViewById(R.id.btn_signupChauffeur);
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

                int i = Integer.parseInt(numTel);
                if(validate()) {
                    ParseObject testObject = new ParseObject("Utilisateur");
                    Log.e("Name", name);
                    Log.e("LasteName", lastname);
                    Log.e("Email", email);
                    Log.e("Password", password);
                    Log.e("Addresse", addresse);
                    Log.e("NumCin", numPermis);
                    // Log.e("NumTel",i);

                    testObject.put("Nom", name);
                    testObject.put("Prenom", lastname);
                    testObject.put("Email", email);
                    testObject.put("Password", password);
                    testObject.put("Adresse", addresse);
                    testObject.put("IdUser", numPermis);
                    testObject.put("Etat", "Connecter");
                    testObject.put("Type", "Chauffeur");
                    testObject.put("Telephone", String.valueOf(i));
                    testObject.saveInBackground();
                    Log.e("Ajouter", "uiiiiiiiii");
                    Chauffeur.id = numPermis;
                    Chauffeur.nom = name;
                    Chauffeur.prenom = lastname;
                    Chauffeur.adresse = addresse;
                    Chauffeur.email = email;
                    Chauffeur.password = password;
                    Chauffeur.telephone = String.valueOf(i);
                    Intent intent = new Intent(v.getContext(), MainChauffeurActivity.class);
                    intent.putExtra("SQLITE", "non");
                    startActivity(intent);
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
