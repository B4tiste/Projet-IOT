#include "MicroBit.h"
#include "ssd1306.h"


MicroBit uBit;
MicroBitI2C i2c(I2C_SDA0,I2C_SCL0);
MicroBitPin P0(MICROBIT_ID_IO_P0, MICROBIT_PIN_P0, PIN_CAPABILITY_DIGITAL_OUT);

void onButton(MicroBitEvent e)
{
    if (e.source == MICROBIT_ID_BUTTON_A)
        uBit.display.scroll("C");
        

    if (e.source == MICROBIT_ID_BUTTON_B)
        uBit.display.scroll("D"); 
}

// Coller le contenu du dossier ssd1306 !

int main()
{
    // Initialise the micro:bit runtime.
    uBit.init();

    uBit.messageBus.listen(MICROBIT_ID_BUTTON_A, MICROBIT_EVT_ANY, onButton);

    // Ecriture sur l'écran OLED
    ssd1306 screen(&uBit, &i2c, &P0);
    while(true)
    {
        screen.display_line(1,0,"Temperature :");
        screen.display_line(3,6,"29'C");
        screen.display_line(5,0,"Luminosite :");
        screen.display_line(7,6,"15%");
        screen.update_screen();
        uBit.sleep(1000);
    }

    release_fiber();

}

