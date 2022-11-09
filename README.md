# Projet-IOT

Repository des fichiers sources du projet IOT 4IRC

Branche : **dev_batiste**

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

