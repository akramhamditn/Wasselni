package com.esprit.wasselni.Client;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.esprit.wasselni.Entities.Client;
import com.esprit.wasselni.R;


public class ProfilClientFragment1 extends Fragment {



    EditText _nameText ;
    EditText _lastnameText;
    EditText _numTelText,_numCIN;
    Button _nextSignupButton;
    String nom ;
    String prenom ;
    String numTel ;
    String numCIN ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_fragment_modifier_client1,container,false);

        _nameText = (EditText) v.findViewById(R.id.input_nomClient);
        _lastnameText= (EditText) v.findViewById(R.id.input_prenom);
        _numTelText= (EditText) v.findViewById(R.id.input_telClient);
        _numCIN= (EditText) v.findViewById(R.id.input_cinClient);
        _nextSignupButton= (Button) v.findViewById(R.id.btn_nextClient);
        _nameText.setText(Client.nom);
        _lastnameText.setText(Client.prenom);
        _numTelText.setText(Client.telephone);
        _numCIN.setText(Client.id);
        _nextSignupButton= (Button) v.findViewById(R.id.btn_nextClient);
        _nextSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new ProfilClientFragment2();
                nom = _nameText.getText().toString();
                prenom =_lastnameText.getText().toString();
                numTel = _numTelText.getText().toString();
                numCIN = _numCIN.getText().toString();
                if (validate()) {

                    //Passer des parametres
                    Bundle client = new Bundle();
                    client.putString("nom", _nameText.getText().toString());
                    client.putString("prenom", _lastnameText.getText().toString());
                    client.putString("numTel", _numTelText.getText().toString());
                    client.putString("numCIN", _numCIN.getText().toString());
                    fragment.setArguments(client);

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.commit();
                }
            }
        });
        return v;
    }
    public boolean validate() {
        boolean valid = true;



        if (nom.isEmpty()) {
            _nameText.setError("entre votre nom");
            valid = false;
        } else {
            _nameText.setError(null);
        }
        if (prenom.isEmpty()) {
            _lastnameText.setError("entre votre prenom");
            valid = false;
        } else {
            _lastnameText.setError(null);
        }

        if (numTel.isEmpty() || numTel.length() == 7 ) {
            _numTelText.setError("le numero de telephone se compose de 8 chiffre");
            valid = false;
        } else {
            _numTelText.setError(null);
        }
        if (numCIN.isEmpty() || numCIN.length() == 7 ) {
            _numCIN.setError("le numero de cin se compose de 8 chiffre");
            valid = false;
        } else {
            _numCIN.setError(null);
        }

        return valid;
    }
}
