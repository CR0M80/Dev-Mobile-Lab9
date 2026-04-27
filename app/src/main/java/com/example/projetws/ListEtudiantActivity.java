package com.example.projetws;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projetws.adapter.EtudiantAdapter;
import com.example.projetws.beans.Etudiant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ListEtudiantActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EtudiantAdapter adapter;
    private RequestQueue requestQueue;

    // L'URL de lecture (Avec le "c" de project !)
    private static final String loadUrl = "http://10.0.2.2/project/ws/LoadEtudiant.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_etudiant);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestQueue = Volley.newRequestQueue(this);

        chargerEtudiants();

    }

    private void chargerEtudiants() {
        StringRequest request = new StringRequest(Request.Method.GET, loadUrl,
                response -> {
                    // Magie de Gson : transforme le JSON du PHP en Liste Java
                    Type type = new TypeToken<List<Etudiant>>(){}.getType();
                    List<Etudiant> etudiants = new Gson().fromJson(response, type);

                    // On donne la liste à l'Adapter pour qu'il dessine l'écran
                    adapter = new EtudiantAdapter(this, etudiants);
                    recyclerView.setAdapter(adapter);
                },
                error -> {
                    Toast.makeText(this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
                }
        );
        requestQueue.add(request);
    }
}