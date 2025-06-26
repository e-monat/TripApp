# 📍 Mon Carnet de Trajets

Application Android permettant de créer, visualiser, modifier et supprimer des trajets géolocalisés, tout en utilisant Firebase, OpenStreetMap (OsmDroid) et une architecture MVVM propre.

## 🔍 Fonctionnalités principales

-Authentification Firebase : connexion, inscription, réinitialisation de mot de passe

-Carte interactive OsmDroid : affichage des trajets, capture de positions

-Gestion des trajets :

-Ajouter une position

-Enregistrer un trajet (titre, description)

-Modifier un trajet

-Supprimer un trajet

-Visualiser les détails et la carte

-Persistance Firebase Firestore : chaque utilisateur voit ses propres trajets

-Interface multilingue (français / anglais) : détecte automatiquement la langue du téléphone

-Mode sombre : activable depuis les paramètres

-Recherche et tri des trajets (bonus)

## 🏗️ Architecture

Le projet suit une architecture MVVM stricte :

Model : Trip.kt, GeoPosition.kt

ViewModel : centralisation des données (implémentation prévue)

View : Fragments, Activities, RecyclerViews

Data Binding : utilisé avec les layouts XML

LiveData : utilisé pour mettre à jour l’interface dynamiquement

## 🗺️ Technologies utilisées

-Android SDK + Kotlin

-Firebase Authentication & Firestore

-OsmDroid pour l’affichage de la carte

-Jetpack Components (Navigation, ViewModel, etc.)

-SharedPreferences pour thème et langue

-Multilingue avec strings.xml et strings-fr.xml

## 🧪 Pour exécuter le projet

Cloner ce dépôt ou importer dans Android Studio

Configurer Firebase :

-Ajouter google-services.json

-Activer Authentication (Email/Password)

-Créer une collection Trips dans Firestore

Vérifier les permissions dans le AndroidManifest.xml

Lancer sur un appareil ou émulateur avec accès à Internet et à la localisation

## 📁 Structure

```
com.example.inventory
│
├── data/
│   └── Trip.kt, GeoPosition.kt
│
├── tools/
│   └── SharedPrefHelper.kt, LocaleHelper.kt
│
├── ui/
│   ├── activities : LoginActivity, SignUpActivity, ForgotPasswordActivity, SplashActivity
│   ├── fragments : MapFragment, TripsFragment, TripDetailFragment, SetingsFragment
│   └── MainActivity.kt
│
├── res/
│   ├── layout/
│   ├── values/strings.xml
│   ├── values-fr/strings.xml
```

## ✅ Améliorations possibles

-Implémenter un ViewModel dédié par écran avec LiveData

-Ajouter la pagination dans la liste des trajets

-Ajouter une base de données locale (Room) pour support hors-ligne

-Support de trajets multi-utilisateurs ou collaboratifs

-Amélioration visuelle (animations, styles)