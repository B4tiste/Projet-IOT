#include "MicroBit.h"

MicroBit uBit;

char prefix = 'b';

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
        uBit.display.scroll(decrypt(message.toCharArray()));
    }

}

int main()
{
    // Initialise the micro:bit runtime.
    uBit.init();

    uBit.messageBus.listen(MICROBIT_ID_RADIO, MICROBIT_RADIO_EVT_DATAGRAM, onData);

    uBit.radio.setGroup(14);

    uBit.radio.enable();

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

        uBit.display.scroll(message);

        uBit.sleep(5000);
    }

    release_fiber();
}