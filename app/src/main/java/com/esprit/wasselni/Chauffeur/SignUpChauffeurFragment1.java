package com.esprit.wasselni.Chauffeur;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.esprit.wasselni.R;


public class SignUpChauffeurFragment1 extends Fragment {



    EditText _nameText ;
    EditText _lastnameText;
    EditText _numTelText,_numPermis;
    Button _nextSignupButton;
    String nom ;
    String prenom ;
    String numTel ;
    String numPermis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_fragment_singup_chauffeur1,container,false);

        _nameText = (EditText) v.findViewById(R.id.input_nomChaffeur);
        _lastnameText= (EditText) v.findViewById(R.id.input_prenomChaffeur);
        _numTelText= (EditText) v.findViewById(R.id.input_telChaffeur);
        _numPermis= (EditText) v.findViewById(R.id.input_cinChaffeur);
        _nextSignupButton= (Button) v.findViewById(R.id.btn_nextCauffeur);
        _nextSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new SignUpChauffeurFragment2();
                nom = _nameText.getText().toString();
                prenom =_lastnameText.getText().toString();
                numTel = _numTelText.getText().toString();
                numPermis = _numPermis.getText().toString();
                if(validate()) {
                    //Passage des parametres
                    Bundle chauffeur = new Bundle();
                    chauffeur.putString("nom", _nameText.getText().toString());
                    chauffeur.putString("prenom", _lastnameText.getText().toString());
                    chauffeur.putString("numTel", _numTelText.getText().toString());
                    chauffeur.putString("numPermis", _numPermis.getText().toString());
                    fragment.setArguments(chauffeur);

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameSignupChauffeur, fragment);
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
        if (numPermis.isEmpty() || numPermis.length() == 7 ) {
            _numPermis.setError("le numero de permis se compose de 8 chiffre");
            valid = false;
        } else {
            _numPermis.setError(null);
        }

        return valid;
    }
}
