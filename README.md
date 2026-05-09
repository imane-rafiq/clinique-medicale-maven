# Application Gestion Clinique Médicale

Projet Java Swing + MySQL + JDBC en architecture MVC.

## Fonctionnalités
- Authentification avec rôles : ADMIN, MEDECIN, SECRETAIRE
- CRUD Patients
- CRUD Médecins
- CRUD Rendez-vous
- Notification médecin avec Observer
- Connexion MySQL avec Singleton
- Export rendez-vous en Excel CSV simple
- Export rendez-vous en PDF simple imprimable HTML

## Base de données
Exécuter le fichier `database.sql` dans MySQL.

## Configuration
Modifier les paramètres MySQL dans :
`src/util/DBConnection.java`

```java
private static final String URL = "jdbc:mysql://localhost:3306/clinique_db";
private static final String USER = "root";
private static final String PASSWORD = "";
```

## Lancement
Compiler et exécuter `Main.java`.

Comptes de test :
- admin / admin123
- med1 / med123
- sec1 / sec123
