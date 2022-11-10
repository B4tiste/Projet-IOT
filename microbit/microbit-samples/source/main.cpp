#include "MicroBit.h"

#define PREFIX "/b/"
#define PrivateKey "/b/"

MicroBit uBit;

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


void onData(MicroBitEvent)
{
    // Message reçu par Radio Frequency
    ManagedString message = uBit.radio.datagram.recv();
    uBit.display.scroll(decrypt(message.toCharArray()));
}

int main()
{
    // Initialise the micro:bit runtime.
    uBit.init();

    uBit.messageBus.listen(MICROBIT_ID_RADIO, MICROBIT_RADIO_EVT_DATAGRAM, onData);

    uBit.radio.setGroup(14);

    uBit.radio.enable();

    release_fiber();
}