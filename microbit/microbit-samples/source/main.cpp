#include "MicroBit.h"
#include "MicroBitUARTService.h"

MicroBit uBit;

char prefix = 'b';

int connected = 0;

// ================================================================================

void onButton(MicroBitEvent e)
{
    if (e.source == MICROBIT_ID_BUTTON_A)
    {
        uBit.serial.printf("BUTTON A: ");
        // read the uart
        ManagedString s = uBit.serial.read();
        uBit.serial.printf("%s ", s.toCharArray());
        uBit.display.scroll("A");
    }

    if (e.source == MICROBIT_ID_BUTTON_B)
    {
        uBit.serial.printf("BUTTON B: ");
        uBit.display.scroll("B");
        // uBit.display.print("B");
    }   

    uBit.serial.printf("\r\n");
}

// ================================================================================

void onSerial(MicroBitEvent)
{
    ManagedString rec;

    rec = uBit.serial.readUntil(ManagedString("\r\n"), ASYNC);

    uBit.serial.printf("Recu\n");
    uBit.serial.printf("%s", rec.toCharArray());
}

char *decrypt(const char *encrypted)
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

char *encrypt(const char *decrypted)
{
    char *encrypted = new char[strlen(decrypted) + 1];
    char *begin = encrypted;

    while (*decrypted != '\0')
    {
        *encrypted++ = *decrypted++ + 3;
    }
    *encrypted = '\0';

    return begin;
}

void onData(MicroBitEvent)
{
    // Message reçu par Radio Frequency
    ManagedString message = uBit.radio.datagram.recv();

    char first_decrypted = decrypt(message.toCharArray())[0];
    
    // Si le premier caractère est 'b', on déchiffre le message et on le scroll
    if (first_decrypted == prefix)
    {
        // uBit.display.scroll(decrypt(message.toCharArray()));
        uBit.serial.printf("%s\r \n", decrypt(message.toCharArray()));
    }

}

int main()
{
    // Initialise the micro:bit runtime.
    uBit.init();

    uBit.display.scroll("X");

    uBit.serial.baud(115200);

    uBit.messageBus.listen(MICROBIT_ID_SERIAL, MICROBIT_SERIAL_EVT_DELIM_MATCH, onSerial);

    uBit.serial.eventOn(ManagedString("\n"), ASYNC);

    uBit.serial.read(ASYNC);

    uBit.messageBus.listen(MICROBIT_ID_RADIO, MICROBIT_RADIO_EVT_DATAGRAM, onData);

    // Create a message bus that listens to button events
    // uBit.messageBus.listen(MICROBIT_ID_BUTTON_A, MICROBIT_BUTTON_EVT_CLICK, onButton);
    // uBit.messageBus.listen(MICROBIT_ID_BUTTON_B, MICROBIT_BUTTON_EVT_CLICK, onButton);

    uBit.radio.enable();
    uBit.radio.setGroup(14);

    // Init du message chiffré
    char *cipher = NULL;

    int cpt = 1;
    // Création du message "LT"
    char message[3] = "LT";

    while (true)
    {
        if (cpt) strcpy(message, "LT");
        else strcpy(message, "TL");

        cpt = !cpt;

        // Chiffrement du message
        cipher = encrypt(message);

        uBit.radio.datagram.send(cipher);

        // uBit.display.scroll(message);

        uBit.sleep(5000);
    }

    release_fiber();
}