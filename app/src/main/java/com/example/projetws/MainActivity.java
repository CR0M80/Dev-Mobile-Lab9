package com.example.projetws;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projetws.beans.Etudiant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nom, prenom;
    private Spinner ville;
    private RadioButton m, f;
    private Button add;
    private RequestQueue requestQueue;

    // L'URL 10.0.2.2 pointe vers le localhost de ton PC XAMPP depuis l'émulateur
    private static final String insertUrl = "http://10.0.2.2/project/ws/CreateEtudiant.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        ville = findViewById(R.id.ville);
        m = findViewById(R.id.m);
        f = findViewById(R.id.f);
        add = findViewById(R.id.add);

        requestQueue = Volley.newRequestQueue(this);
        add.setOnClickListener(this);
        Button btnListe = findViewById(R.id.btnListe);
        btnListe.setOnClickListener(v -> {
            startActivity(new android.content.Intent(MainActivity.this, ListEtudiantActivity.class));
        });
    }

    @Override
    public void onClick(View v) {
        if (v == add) {
            envoyerEtudiant();
        }
    }

    private void envoyerEtudiant() {
        StringRequest request = new StringRequest(Request.Method.POST, insertUrl,
                response -> {
                    // Cette partie s'exécute si le PHP répond avec succès
                    Log.d("RESPONSE", response);
                    Toast.makeText(this, "Étudiant ajouté avec succès !", Toast.LENGTH_SHORT).show();

                    // On demande à Gson de transformer le texte JSON en liste d'objets Java
                    Type type = new TypeToken<Collection<Etudiant>>(){}.getType();
                    Collection<Etudiant> etudiants = new Gson().fromJson(response, type);

                    // On affiche chaque étudiant dans la console (Logcat)
                    for (Etudiant e : etudiants) {
                        Log.d("ETUDIANT", e.toString());
                    }

                    // On vide les champs
                    nom.setText("");
                    prenom.setText("");
                },
                error -> {
                    // Cette partie s'exécute s'il y a un problème de connexion
                    Log.e("VOLLEY", "Erreur : " + error.getMessage());
                    Toast.makeText(this, "Erreur de connexion au serveur", Toast.LENGTH_LONG).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Ici on prépare le "colis" à envoyer au PHP
                String sexe = m.isChecked() ? "homme" : "femme";
                Map<String, String> params = new HashMap<>();
                params.put("nom", nom.getText().toString());
                params.put("prenom", prenom.getText().toString());
                params.put("ville", ville.getSelectedItem().toString());
                params.put("sexe", sexe);
                return params;
            }
        };

        // On place la requête dans la file d'attente pour qu'elle s'exécute
        requestQueue.add(request);
    }
}