# 📋 ProjetWS — Android + PHP + MySQL (Volley + Gson) — By SAAD

Application complète de gestion des étudiants basée sur une architecture **Client / Serveur** :

* 🗄️ Base de données MySQL
* 🌐 Web Service PHP (JSON)
* 📱 Application Android
* 🔄 Communication via Volley
* 📦 Parsing JSON avec Gson

---


https://github.com/user-attachments/assets/cf068c5e-df1a-40ee-b81d-cc78eaf623fe


# 🎯 Objectif du projet

Créer une application Android capable de :

* Ajouter un étudiant via API PHP
* Récupérer la liste des étudiants depuis MySQL
* Afficher les données dans un RecyclerView
* Supprimer et modifier un étudiant
* Parser le JSON avec Gson

---

# 🏗️ Architecture

```
Android App (com.example.projetws)
   ↓ Volley (HTTP)
PHP Web Service (htdocs/project/ws/)
   ↓ PDO
MySQL Database (project)
```

---

# 🗄️ Base de données

## 📌 Création

```sql
CREATE DATABASE project;
```

## 📌 Table Etudiant

```sql
CREATE TABLE Etudiant (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nom VARCHAR(50),
  prenom VARCHAR(50),
  ville VARCHAR(50),
  sexe VARCHAR(10)
);
```

## 📌 Exemple de données

```sql
INSERT INTO Etudiant (nom, prenom, ville, sexe)
VALUES 
('AMAR', 'SAAD', 'Rabat', 'homme'),
('SAAD', 'AMAR', 'Marrakech', 'homme');
```

---

# 🌐 Web Service PHP

## 📁 Structure

```
C:\xampp\htdocs\project\
 ├── classes/
 │    └── Etudiant.php
 ├── connexion/
 │    └── Connexion.php
 ├── dao/
 │    └── IDao.php
 ├── service/
 │    └── EtudiantService.php
 └── ws/
      ├── createEtudiant.php
      ├── LoadEtudiant.php
      ├── deleteEtudiant.php
      └── updateEtudiant.php
```

## 🔌 Connexion PDO — `Connexion.php`

```php
$this->connexion = new PDO(
    "mysql:host=localhost;dbname=project;charset=utf8",
    "root",
    ""
);
```

## 📦 Interface IDao — `dao/IDao.php`

```php
interface IDao {
    function create($o);
    function delete($o);
    function update($o);
    function findAll();
    function findById($id);
}
```

## 📦 Service — `service/EtudiantService.php`

```php
class EtudiantService implements IDao {
    public function create($o)   { /* INSERT */ }
    public function findAll()    { /* SELECT * */ }
    public function findById($id){ /* SELECT WHERE id */ }
    public function delete($o)   { /* DELETE WHERE id */ }
    public function update($o)   { /* UPDATE WHERE id */ }
}
```

## 📦 API Endpoints

| Fichier               | Méthode | Description              |
|-----------------------|---------|--------------------------|
| `LoadEtudiant.php`    | GET     | Retourne tous les étudiants en JSON |
| `createEtudiant.php`  | POST    | Ajoute un étudiant       |
| `deleteEtudiant.php`  | POST    | Supprime par `id`        |
| `updateEtudiant.php`  | POST    | Modifie un étudiant      |

---

# 📱 Application Android

## 📁 Structure Java

```
com.example.projetws/
 ├── MainActivity.java
 ├── ListEtudiantActivity.java
 ├── Etudiant.java
 └── adapter/
      └── EtudiantAdapter.java
```

## 🔗 URLs utilisées — Émulateur Android Studio

```java
// MainActivity.java
private static final String insertUrl =
    "http://10.0.2.2/project/ws/CreateEtudiant.php";

// ListEtudiantActivity.java
private static final String loadUrl =
    "http://10.0.2.2/project/ws/LoadEtudiant.php";

// EtudiantAdapter.java
private static final String DELETE_URL =
    "http://10.0.2.2/project/ws/deleteEtudiant.php";
private static final String UPDATE_URL =
    "http://10.0.2.2/project/ws/updateEtudiant.php";
```

> ⚠️ `10.0.2.2` = localhost uniquement sur l'émulateur.
> Sur téléphone réel → remplacer par l'IP locale du PC (ex: `192.168.1.x`)

## 🧩 Technologies utilisées

| Tech               | Usage               |
|--------------------|---------------------|
| Volley             | Requêtes HTTP       |
| Gson               | Parsing JSON        |
| RecyclerView       | Liste dynamique     |
| EditText / Spinner | Formulaire saisie   |
| RadioButton        | Sélection du sexe   |

---

# 📌 Permissions — `AndroidManifest.xml`

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

---

# 📦 Dépendances Gradle

```gradle
implementation "androidx.recyclerview:recyclerview:1.3.2"
implementation "com.android.volley:volley:1.2.1"
implementation "com.google.code.gson:gson:2.10.1"
```

---

# 📱 Fonctionnalités Android

## ➕ Ajouter étudiant
* Formulaire : Nom, Prénom, Ville (Spinner), Sexe (RadioButton)
* Envoi via POST avec Volley
* Retour JSON parsé avec Gson

## 📥 Charger les étudiants
* Requête GET sur `LoadEtudiant.php`
* Parsing avec Gson → `TypeToken<Collection<Etudiant>>`
* Affichage dans RecyclerView

## 🗑️ Supprimer un étudiant
* Envoi de l'`id` en POST sur `deleteEtudiant.php`
* Mise à jour de la liste automatiquement

## ✏️ Modifier un étudiant
* Envoi de tous les champs + `id` en POST sur `updateEtudiant.php`

---

# 🔄 Exemple de requête Volley

```java
StringRequest request = new StringRequest(Request.Method.POST, url,
    response -> {
        Gson gson = new Gson();
        Type type = new TypeToken<Collection<Etudiant>>(){}.getType();
        Collection<Etudiant> list = gson.fromJson(response, type);
    },
    error -> Log.e("VOLLEY_ERROR", error.toString())
);
RequestQueue queue = Volley.newRequestQueue(this);
queue.add(request);
```

---

# 🧪 Test du projet

## ① Tester le PHP dans le navigateur
```
http://localhost/project/ws/LoadEtudiant.php
```
Résultat attendu : `[]` ou liste JSON

## ② Tester sur l'émulateur
1. Démarrer XAMPP (Apache + MySQL en vert)
2. Lancer l'émulateur Android Studio (**image Google APIs**, pas Google Play)
3. Run l'app → Ajouter un étudiant → Vérifier la liste

---

# 🧠 Concepts appris

| Concept   | Description                |
|-----------|----------------------------|
| REST API  | Communication HTTP         |
| JSON      | Format d'échange de données|
| Volley    | Client réseau Android      |
| Gson      | Conversion JSON ↔ Java     |
| PDO       | Connexion sécurisée MySQL  |
| Interface | IDao — contrat de méthodes |
| MVC léger | Séparation service / DAO   |

---

# 🚀 Résultat final

✔ Ajout d'étudiants depuis Android
✔ Récupération depuis MySQL via PHP
✔ Affichage dynamique RecyclerView
✔ Suppression et modification fonctionnelles
✔ Communication REST complète
✔ JSON parsing automatique avec Gson

---

# 📌 Auteur

Projet réalisé dans un cadre pédagogique — **SAAD** 🚀
