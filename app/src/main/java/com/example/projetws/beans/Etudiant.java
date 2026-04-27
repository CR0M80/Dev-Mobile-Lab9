package com.example.projetws.beans;

public class Etudiant {

    // 1. Les attributs (Doivent correspondre exactement aux clés du JSON renvoyé par PHP)
    private int id;
    private String nom;
    private String prenom;
    private String ville;
    private String sexe;

    // 2. Le constructeur vide (Obligatoire pour que Gson puisse créer l'objet)
    public Etudiant() {
    }

    // 3. Le constructeur complet
    public Etudiant(int id, String nom, String prenom, String ville, String sexe) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.ville = ville;
        this.sexe = sexe;
    }

    // 4. Les Getters (Pour lire les données dans l'Adapter)
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getVille() { return ville; }
    public String getSexe() { return sexe; }

    // 5. Les Setters (Pour modifier les données si besoin)
    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setVille(String ville) { this.ville = ville; }
    public void setSexe(String sexe) { this.sexe = sexe; }

    // 6. La méthode toString (Pour un affichage propre dans le Logcat)
    @Override
    public String toString() {
        return "Etudiant{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", ville='" + ville + '\'' +
                ", sexe='" + sexe + '\'' +
                '}';
    }
}