


# GoogleMapLocationLab

https://github.com/user-attachments/assets/154cb731-572d-41db-9cd0-a04dd815da31



## Objectif

Ce lab a pour objectif de créer une application Android simple qui affiche une Google Map, demande la permission de localisation, écoute les changements de position, affiche un marker et zoome automatiquement sur la position actuelle.

## Fonctionnalités

- Affichage Google Maps
- Clé API Google Maps
- Permission localisation
- Permission runtime
- Écoute GPS/Réseau
- Marker sur la position
- Zoom automatique
- Boîte de dialogue si GPS désactivé

## Technologies utilisées

- Java
- Android Studio
- Google Maps SDK for Android
- LocationManager
- LocationListener
- AlertDialog

## Structure du projet

- `MapsActivity.java` : activité principale qui affiche la carte et gère la localisation.
- `activity_maps.xml` : layout contenant le fragment Google Map.
- `google_maps_api.xml` : fichier contenant la clé API Google Maps.
- `AndroidManifest.xml` : fichier de configuration avec les permissions et la clé API.

## Description des étapes

### Étape 1 : Création du projet

Le projet a été créé avec le template Google Maps Activity dans Android Studio. Ce template prépare une activité avec une carte Google Maps prête à être utilisée.

### Étape 2 : Génération de la clé API

La clé API Google Maps doit être créée dans Google Cloud Console. Il faut activer Maps SDK for Android, puis ajouter la clé dans `google_maps_api.xml`.
<img width="901" height="921" alt="image" src="https://github.com/user-attachments/assets/2e5c3a46-f341-4e35-818e-42dfdca2071d" />


### Étape 3 : Ajout des permissions

Les permissions `ACCESS_FINE_LOCATION` et `INTERNET` ont été ajoutées dans `AndroidManifest.xml`. La première permet d'utiliser la localisation précise, la deuxième permet de charger les tuiles Google Maps.

### Étape 4 : Permission runtime

Depuis Android 6, déclarer une permission dans le Manifest ne suffit pas. L'application doit demander la permission pendant l'exécution avec `requestPermissions()`.

### Étape 5 : Initialisation de la carte

La méthode `onMapReady()` est appelée quand la Google Map est prête. Elle permet d'ajouter un marker initial et de démarrer la logique de localisation.

### Étape 6 : Écoute de localisation

`LocationManager` écoute les changements de position avec le provider réseau ou le provider GPS. Quand une nouvelle position arrive, `onLocationChanged()` est appelée.

### Étape 7 : Ajout du marker

Chaque nouvelle position est convertie en objet `LatLng`. Dans cette version propre, un seul marker est créé puis déplacé vers la nouvelle position.

### Étape 8 : Zoom sur la position

`CameraUpdateFactory.newLatLngZoom()` centre la carte sur la position actuelle et applique un zoom de niveau `15f`.

### Étape 9 : GPS désactivé

Si la localisation ou le GPS est désactivé, l'application affiche une `AlertDialog`. Le bouton positif ouvre les paramètres de localisation avec `Settings.ACTION_LOCATION_SOURCE_SETTINGS`.

## Résultat attendu

- La carte apparaît.
- La popup de permission apparaît.
- La position actuelle est détectée.
- Un marker apparaît sur la position.
- La carte zoome vers la position actuelle.
- Si le GPS est désactivé, une boîte de dialogue demande de l'activer.



## Explication des concepts

### 1. Google Maps Activity

Google Maps Activity est un template Android Studio qui crée un écran de carte prêt à l'emploi. Il génère généralement `MapsActivity.java`, `activity_maps.xml` et `google_maps_api.xml`.

<img width="1308" height="1416" alt="image" src="https://github.com/user-attachments/assets/b5777018-0065-4f97-ad20-60d0d9f74d68" />

<img width="1133" height="388" alt="image" src="https://github.com/user-attachments/assets/e25d3f6c-5c5b-401a-b415-833a4a1bd332" />



### 2. google_maps_key

`google_maps_key` est la clé API obligatoire pour afficher Google Maps dans l'application. Elle doit être créée dans Google Cloud Console après activation de Maps SDK for Android.

### 3. Permissions du Manifest

`ACCESS_FINE_LOCATION` autorise la localisation précise. `INTERNET` permet à Google Maps de charger les données et les tuiles de carte.

### 4. Permission runtime

Depuis Android 6, les permissions dangereuses doivent être demandées pendant l'exécution. L'application utilise donc `requestPermissions()` pour demander l'accord de l'utilisateur.

### 5. LocationManager

`LocationManager` est le service Android utilisé pour accéder aux providers de localisation comme le réseau ou le GPS.

### 6. NETWORK_PROVIDER

`NETWORK_PROVIDER` utilise le Wi-Fi et le réseau mobile. Il est souvent rapide et fonctionne mieux en intérieur, mais il est moins précis que le GPS.

### 7. GPS_PROVIDER

`GPS_PROVIDER` est plus précis, mais il peut être plus lent et fonctionne mieux à l'extérieur avec un ciel dégagé.

### 8. LocationListener

`LocationListener` reçoit les mises à jour de localisation. `onLocationChanged()` est appelée quand la position change, et `onProviderDisabled()` est appelée quand un provider est désactivé.

### 9. Marker

Un `Marker` représente un point visible sur la carte. `addMarker()` ajoute un marker à une position `LatLng`.

### 10. Camera et zoom

`moveCamera()` déplace la caméra immédiatement. `animateCamera()` la déplace avec une animation. `newLatLngZoom(position, 15f)` centre la carte sur la position et zoome au niveau quartier/rue.

### 11. AlertDialog

`AlertDialog` permet de demander à l'utilisateur d'activer la localisation. Le bouton positif ouvre les paramètres, et le bouton négatif ferme la boîte de dialogue.

## Conclusion

Ce lab montre comment intégrer Google Maps et les services de localisation Android tout en respectant les règles de permission runtime.
