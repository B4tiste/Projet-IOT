#include "MicroBit.h"
#include "bme280.h"
#include "tsl256x.h"
#include "ssd1306.h"

MicroBit uBit;
MicroBitI2C i2c(I2C_SDA0, I2C_SCL0);
MicroBitPin P0(MICROBIT_ID_IO_P0, MICROBIT_PIN_P0, PIN_CAPABILITY_DIGITAL_OUT);

// Coller le contenu du dossier ssd1306 !

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

    ssd1306 screen(&uBit, &i2c, &P0);
    while (true)
    {
        // Récupération des données sur les capteurs
        bme.sensor_read(&pressure, &temp, &humidite);
        tsl.sensor_read(&comb, &ir, &lux);

        // Temperature
        int tmp = bme.compensate_temperature(temp);
        char display_temp[6];
        sprintf(display_temp,"%2d.%2d C",tmp / 100,tmp % 100);
        // Humidité
        // int pres = bme.compensate_pressure(pressure) / 100;
        // display = "H:" + ManagedString(hum / 100) + "." + ManagedString(tmp % 100) + " rH";
        // uBit.display.scroll(display.toCharArray());

        // Pression
        // int hum = bme.compensate_humidity(humidite);
        // display = "P:" + ManagedString(pres) + " hPa";
        // uBit.display.scroll(display.toCharArray());

        // Luminosité
        char display_lum[6];
        sprintf(display_lum, "%5d", (int) lux);
        
        screen.display_line(1,0,"Temperature :");
        screen.display_line(3,5, display_temp);
        screen.display_line(5,0,"Luminosite :");
        screen.display_line(7,5, display_lum);
        screen.update_screen();

        uBit.sleep(1000);
    }

    release_fiber();
}

