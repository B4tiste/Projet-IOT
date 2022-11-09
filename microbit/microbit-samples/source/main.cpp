#include "MicroBit.h"
#include "bme280.h"
#include "tsl256x.h"

MicroBit uBit;
MicroBitI2C i2c(I2C_SDA0, I2C_SCL0);

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

    while (true)
    {
        // Récupération des données sur les capteurs
        bme.sensor_read(&pressure, &temp, &humidite);
        tsl.sensor_read(&comb, &ir, &lux);

        // Temperature
        int tmp = bme.compensate_temperature(temp);
        ManagedString display = "T:" + ManagedString(tmp / 100) + "." + (tmp > 0 ? ManagedString(tmp % 100) : ManagedString((-tmp) % 100)) + " C";
        uBit.display.scroll(display.toCharArray());

        // Humidité
        // int pres = bme.compensate_pressure(pressure) / 100;
        // display = "H:" + ManagedString(hum / 100) + "." + ManagedString(tmp % 100) + " rH";
        // uBit.display.scroll(display.toCharArray());

        // Pression
        // int hum = bme.compensate_humidity(humidite);
        // display = "P:" + ManagedString(pres) + " hPa";
        // uBit.display.scroll(display.toCharArray());

        // Luminosité
        display = "L:" + ManagedString((int) lux);
        uBit.display.scroll(display.toCharArray());
        

        uBit.sleep(1000);
    }

    release_fiber();
}
