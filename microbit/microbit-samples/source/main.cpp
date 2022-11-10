#include "MicroBit.h"
#include "bme280.h"
#include "tsl256x.h"
#include "ssd1306.h"

using namespace std;

MicroBit uBit;
MicroBitI2C i2c(I2C_SDA0, I2C_SCL0);
MicroBitPin P0(MICROBIT_ID_IO_P0, MICROBIT_PIN_P0, PIN_CAPABILITY_DIGITAL_OUT);

// string encryptionMessage(string Msg);
// string decryptionMessage(string CTxt);

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

        // Buffer
        PacketBuffer buffer(5);
        buffer[0] = temp_gauche;
        buffer[1] = temp_droite;
        buffer[2] = lumi_gauche;
        buffer[3] = lumi_centre;
        buffer[4] = lumi_droite;

        // DEBUG
        char message[5];
        sprintf(message, "%d.%d, %d%d%d", buffer[0], buffer[1], buffer[2], buffer[3], buffer[4]);

        uBit.display.scroll(message);

        // Envoie
        uBit.radio.datagram.send(buffer);

        uBit.sleep(5000);
    }

    release_fiber();
}

// string encryptionMessage(string Msg)
// {
//     string CTxt = "";

//     int a = 3;

//     int b = 6;

//     for (unsigned int i = 0; i < Msg.length(); i++)

//     {
//         CTxt = CTxt + (char)((((a * Msg[i]) + b) % 26) + 65);
//     }

//     return CTxt;
// }

// string decryptionMessage(string CTxt)
// {
//     string Msg = "";

//     int a = 3;

//     int b = 6;

//     int a_inv = 0;

//     int flag = 0;

//     for (int i = 0; i < 26; i++)

//     {

//         flag = (a * i) % 26;

//         if (flag == 1)

//         {
//             a_inv = i;
//         }
//     }

//     for (unsigned int i = 0; i < CTxt.length(); i++)

//     {

//         Msg = Msg + (char)(((a_inv * ((CTxt[i] - b)) % 26)) + 65);
//     }

//     return Msg;
// }