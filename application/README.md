# Création de l’application Android

La dernière partie de la mise en place de l’architecture IoT consiste à développer une application
Android qui permet de contrôler l’ordre d’affichage des données collectées sur un des objets en
particulier. Ainsi, l’application a deux fonctionnalités, le choix de l’affichage des données d’un des
objets et le choix du serveur sur lequel cela s’applique.

Pour simplifier, on assume qu’il y a seulement un objet associé à chaque serveur et donc on doit
choisir seulement l’adresse du serveur de destination et pas un des objets qui lui sont associés.

Pour faciliter le développement de votre application, les données seront envoyées via le protocole
UDP, et votre Smartphone sera connecté au serveur via WiFi si vous êtes en physique ou via le
réseau interne de votre PC si vous êtes en simulateur.


## Exercice 1 : choix d’affichage

Dans un premier temps votre application devra être en mesure de définir un ordre d’affichage
pour les 3 différentes données collectées (Humidité, Luminosité, Température). Vous pouvez, pour ceci, afficher les trois données à l’écran
et par simple pression du doigt définir l’ordre d’affichage des données. Ceci n’est qu’un exemple, et
le choix d’interface visuel de votre application reste libre.


## Exercice 2 : définir le serveur de destination

En plus de choisir l’ordre d’affichage des données, votre application doit être en mesure de choisir
le serveur de destination. En effet, le but est de pouvoir contrôler l’affichage des données pour
chaque objet via le serveur avec lequel il communique. Ainsi, le choix du serveur dans l’application
est indispensable. La configuration du serveur dans l’application Android se fait via l’adresse IP
du serveur et son port d’écoute (par défaut : 10000). Donc dans votre application Android, vous
devez disposer de deux champs dans lequel il sera possible de saisir une adresse IP et un port, qui
permettront la communication avec le serveur souhaité.

De plus, la communication sera effectuée via le protocole UDP, et aucun ACK n’est demandé.
Ainsi, votre application doit seulement faire de l’émission en direction du serveur, sans se préoccuper
de devoir réceptionner des paquets. Les données envoyées par votre application seront les 2 lettres
majuscules indiquant l’ordre d’affichage dans l’écran OLED.

## Exercice 3 : Communication bidirectionnelle avec le Smartphone

Dans l’objectif d’être capable d’afficher les données également sur votre Smartphone, votre
application doit être en mesure de recevoir des messages venant du serveur. Tout comme le serveur
envoie des données à l’objet, ce dernier devra pouvoir envoyer les mêmes données avec le même
format à votre Smartphone. Ce dernier devra être capable de réceptionner les données et de les
afficher dans l’application créée précédemment.

De plus, le Smartphone ne doit réceptionner que les données émises depuis le serveur avec lequel
il est connecté (défini dans l’exercice précédent).
