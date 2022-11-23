#include "MicroBit.h"
#include "bme280.h"
#include "tsl256x.h"
#include "ssd1306.h"
#include <string.h>

MicroBit uBit;
MicroBitI2C i2c(I2C_SDA0, I2C_SCL0);
MicroBitPin P0(MICROBIT_ID_IO_P0, MICROBIT_PIN_P0, PIN_CAPABILITY_DIGITAL_OUT);

char *encrypt(char *not_encrypted);
char *decrypt(char *encrypted);

int main()
{
    // Initialise the micro:bit runtime.
    uBit.init();

    // Init liaison i2c capteur Temp/Humi/Press
    bme280 bme(&uBit, &i2c);
    uint32_t pressure = 0;
    int32_t temp = 0;
    uint16_t humidite = 0;

    // Init laison i2c capteur Luminosité
    tsl256x tsl(&uBit, &i2c);
    uint16_t comb = 0;
    uint16_t ir = 0;
    uint32_t lux = 0;

    // Init de la liaison entre l'écran
    ssd1306 screen(&uBit, &i2c, &P0);

    // Init de la transmission radio sur la fréquence n°14
    uBit.radio.enable();
    uBit.radio.setGroup(14);

    // Init du message chiffré
    char *cipher = NULL;

    while (true)
    {
        // Récupération des données sur les capteurs
        bme.sensor_read(&pressure, &temp, &humidite);
        tsl.sensor_read(&comb, &ir, &lux);

        // Temperature
        int tmp = bme.compensate_temperature(temp);
        char display_temp[6];
        sprintf(display_temp, "%2d.%2d C", tmp / 100, tmp % 100);

        // Luminosité
        char display_lum[6];
        sprintf(display_lum, "%5d", (int)lux);

        // Affichage des informations sur l'écran
        screen.display_line(1, 0, "Temperature :");
        screen.display_line(3, 5, display_temp);
        screen.display_line(5, 0, "Luminosite :");
        screen.display_line(7, 5, display_lum);
        screen.update_screen();

        // Envoie des informations par radio fréquence
        // Parsing
        // Température
        int temp_gauche = tmp / 100;
        int temp_droite = tmp % 100;
        // Luminosité
        int lumi_gauche = lux / 10000;
        int lumi_centre = lux / 100 % 100;
        int lumi_droite = lux % 100;

        // Création du message
        char message[5];
        sprintf(message, "%d.%d, %d%d%d", temp_gauche, temp_droite, lumi_gauche, lumi_centre, lumi_droite);

        // Encrypt
        cipher = encrypt(message);

        uBit.display.scroll(cipher);

        // Envoie
        uBit.radio.datagram.send(cipher);

        uBit.sleep(5000);
    }

    release_fiber();
}

// Permet de chiffrer une chaine de caractère via un césar
char *encrypt(char *not_encrypted)
{
    char *encrypted = new char[strlen(not_encrypted) + 1];
    char *begin = encrypted;

    while (*not_encrypted != '\0')
    {
        *encrypted++ = *not_encrypted++ + 3;
    }
    *encrypted = '\0';

    return begin;
}

// Permet de déchiffrer une chaines de caractères chiffrés
char *decrypt(char *encrypted)
{
    char *decrypted = new char[strlen(encrypted) + 1];
    char *begin = decrypted;

    while (*encrypted != '\0')
    {
        *decrypted++ = *encrypted++ - 3;
    }
    *decrypted = '\0';

    return begin;
}
