package com.example.projetws.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projetws.R;
import com.example.projetws.beans.Etudiant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EtudiantAdapter extends RecyclerView.Adapter<EtudiantAdapter.EtudiantViewHolder> {

    private List<Etudiant> etudiants;
    private Context context;

    // ATTENTION : J'ai mis "project" (avec un C) comme on l'a corrigé tout à l'heure.
    // Vérifie que cela correspond bien au nom de ton dossier dans htdocs !
    private static final String DELETE_URL = "http://10.0.2.2/project/ws/deleteEtudiant.php";
    private static final String UPDATE_URL = "http://10.0.2.2/project/ws/updateEtudiant.php";

    public EtudiantAdapter(Context context, List<Etudiant> etudiants) {
        this.context = context;
        this.etudiants = etudiants;
    }

    @NonNull
    @Override
    public EtudiantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.etudiant_item, parent, false);
        final EtudiantViewHolder holder = new EtudiantViewHolder(v);

        // Au clic sur un étudiant dans la liste
        holder.itemView.setOnClickListener(view -> {
            int position = holder.getAdapterPosition();
            Etudiant current = etudiants.get(position);

            String[] options = {"Modifier", "Supprimer"};
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Action pour " + current.getNom());

            builder.setItems(options, (dialog, which) -> {
                if (which == 0) {
                    showUpdateDialog(current, position); // Lancer la modification
                } else if (which == 1) {
                    confirmDelete(current, position); // Lancer la suppression
                }
            });
            builder.show();
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EtudiantViewHolder holder, int position) {
        Etudiant e = etudiants.get(position);
        holder.tvId.setText(String.valueOf(e.getId()));
        holder.tvNomPrenom.setText(e.getNom() + " " + e.getPrenom());
        holder.tvVilleSexe.setText(e.getVille() + " - " + e.getSexe());
    }

    @Override
    public int getItemCount() {
        return etudiants.size();
    }

    // =========================================================
    // MÉTHODE DE MODIFICATION (UPDATE)
    // =========================================================
    private void showUpdateDialog(Etudiant current, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Modifier l'étudiant");

        // Création dynamique d'un formulaire pour modifier le nom et prénom
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText editNom = new EditText(context);
        editNom.setText(current.getNom());
        editNom.setHint("Nom");
        layout.addView(editNom);

        final EditText editPrenom = new EditText(context);
        editPrenom.setText(current.getPrenom());
        editPrenom.setHint("Prénom");
        layout.addView(editPrenom);

        builder.setView(layout);
        builder.setPositiveButton("Valider", (dialog, which) -> {
            String newNom = editNom.getText().toString();
            String newPrenom = editPrenom.getText().toString();

            // Envoi de la requête au serveur PHP
            StringRequest request = new StringRequest(Request.Method.POST, UPDATE_URL,
                    response -> {
                        // Si le serveur répond OK, on met à jour l'objet et l'écran
                        current.setNom(newNom);
                        current.setPrenom(newPrenom);
                        notifyItemChanged(position);
                        Toast.makeText(context, "Modifié avec succès !", Toast.LENGTH_SHORT).show();
                    },
                    error -> Toast.makeText(context, "Erreur réseau: Impossible de modifier", Toast.LENGTH_LONG).show()) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", String.valueOf(current.getId()));
                    params.put("nom", newNom);
                    params.put("prenom", newPrenom);
                    params.put("ville", current.getVille());
                    params.put("sexe", current.getSexe());
                    return params;
                }
            };
            Volley.newRequestQueue(context).add(request);
        });
        builder.setNegativeButton("Annuler", null);
        builder.show();
    }

    // =========================================================
    // MÉTHODE DE SUPPRESSION (DELETE)
    // =========================================================
    private void confirmDelete(Etudiant current, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Confirmation")
                .setMessage("Voulez-vous vraiment supprimer " + current.getNom() + " ?")
                .setPositiveButton("Oui", (d, w) -> {

                    // Envoi de la requête de suppression au PHP
                    StringRequest request = new StringRequest(Request.Method.POST, DELETE_URL,
                            response -> {
                                // Si le serveur a bien supprimé, on l'enlève de la liste visuelle
                                etudiants.remove(position);
                                notifyItemRemoved(position);
                                Toast.makeText(context, "Supprimé avec succès !", Toast.LENGTH_SHORT).show();
                            },
                            error -> Toast.makeText(context, "Erreur réseau: Impossible de supprimer", Toast.LENGTH_LONG).show()) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("id", String.valueOf(current.getId()));
                            return params;
                        }
                    };
                    Volley.newRequestQueue(context).add(request);
                })
                .setNegativeButton("Non", null)
                .show();
    }

    // =========================================================
    // VIEWHOLDER
    // =========================================================
    public class EtudiantViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvNomPrenom, tvVilleSexe;

        public EtudiantViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvNomPrenom = itemView.findViewById(R.id.tvNomPrenom);
            tvVilleSexe = itemView.findViewById(R.id.tvVilleSexe);
        }
    }
}