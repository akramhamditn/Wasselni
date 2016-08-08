package com.esprit.wasselni.Client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.esprit.wasselni.Entities.Client;
import com.esprit.wasselni.R;
import com.parse.ParseObject;


public class SignUpClientFragment2 extends Fragment {
    EditText _emailText ;
    EditText _passwordText;
    EditText _adresseText,_numCarte;
    String name,lastname,numTel,numCIN,email,password,addresse,numCarte;
    Button _SignupButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_fragment_singup_client2,container,false);
        _emailText = (EditText) v.findViewById(R.id.input_emailClient);
        _passwordText= (EditText) v.findViewById(R.id.input_passwordClient);
        _adresseText= (EditText) v.findViewById(R.id.input_adressClient);
       //_numCarte= (EditText) v.findViewById(R.id.input_numCarte);
        _SignupButton= (Button) v.findViewById(R.id.btn_signupClient);
        name = getArguments().getString("nom");
        lastname = getArguments().getString("prenom");
        numTel = getArguments().getString("numTel");
        numCIN = getArguments().getString("numCIN");


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
                    Log.e("NumCin", numCIN);
                    // Log.e("NumTel",i);

                    testObject.put("Nom", name);
                    testObject.put("Prenom", lastname);
                    testObject.put("Email", email);
                    testObject.put("Password", password);
                    testObject.put("Adresse", addresse);
                    testObject.put("IdUser", numCIN);
                    testObject.put("Etat", "Connecter");
                    testObject.put("Type", "Client");
                    testObject.put("Telephone", String.valueOf(i));
                    testObject.saveInBackground();
                    Log.e("Ajouter", "uiiiiiiiii");
                    Client.id = numCIN;
                    Client.nom = name;
                    Client.prenom = lastname;
                    Client.adresse = addresse;
                    Client.email = email;
                    Client.password = password;
                    Client.telephone = String.valueOf(i);
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("SQLITE", "non");
                    startActivity(intent);
                }


            }
        });
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
