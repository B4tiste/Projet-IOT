# Projet-IOT

Repository des fichiers sources du projet IOT 4IRC

Branche : **dev_batiste**

```mermaid
graph TD
    A[Application Android] --> B[Serveur Python]
    B --> A
    
    B --> C[MicroBit Communication]
    C --> B
    
    C --> D[MicroBit Capteur]
    D --> C
    D --> H[Ecran OLED]
    
    E[Capteurs] --> D
    F[Température]
    G[Luminosité]
    F & G --> E
```


# Weather sensor 

## Infos câblage : 
* Noire : GND - Ground
* Rouge : 3.3V
* Blanc : SDA - Bidirectional Serial Data for I2C bus
* Jaune : SCL : Clock for I2C bus

## Infos capteurs :
* U1 : **VEML6070** UVA Sensor - Capteur Ultraviolet
* U2 : **TSL2561** Ambiant light - Luminosité
* U3 : **BME280** Temperature, Barometric & Humidity 

## Adresses I2C :
* **VEML6070** : 0x70 / 0x71 and 0x73 (LSB and MSB are read on two different addresses
* **TSL2561** : 0x52 / 0x53 (Pin Addr Sel (pin2 of tsl256x) connected to GND)
* **BME280** : 0xEC / 0xED

### Infrastructure objet-passerelle

- Mise en place du réseau
		○ Canal bi-directionnel
		○ Sécurité des données envoyées
    
- Configuration des capteurs
  - Température
  -  Luminostié
  -  Données envoyé dans l'ordre demandé par le serveur
  -  OLED
    -  Affichage des données envoyées depuis le microbit
    -   Communication avec la passerelle
        -   Format de données brute
        -   -> à remplacer par le format json
        -   TL : Température puis Luminosité
        -   LT : Luminosité et Température
- configuration de la passerelle et serveur
    - Mettre à disposition un serveur qui écoute 
        - Stocke les données en brut puis en base de données
          - Définition du format d'échange
          - faire une interface web avec Grafana
          - Gestion de plusieurs objets
- Application Android
  - 1 - choix de l'affichage
    - Définir l'ordre d'affichage des données collectées 
  - 2 - définir le serveur de destination
    - Port d'écoute
    - Envoie
    - UDP
  - 3 - Communication bidirectionnelle avec le smartphone
    - Écoute des messages envoyés du serveur
