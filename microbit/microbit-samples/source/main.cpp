#include "MicroBit.h"

#define PREFIX "/b/"
#define PrivateKey "/b/"

MicroBit uBit;

void onData(MicroBitEvent)
{
    // Buffer qui contient le message reçu par Radio Frequency
    PacketBuffer b = uBit.radio.datagram.recv();

    char message[20];
    sprintf(message, "T: %d.%d | L: %d%d%d", b[0], b[1], b[2], b[3], b[4]);
    uBit.display.scroll(message);
}

int main()
{
    // Initialise the micro:bit runtime.
    uBit.init();

    uBit.messageBus.listen(MICROBIT_ID_RADIO, MICROBIT_RADIO_EVT_DATAGRAM, onData);

    uBit.radio.setGroup(14);

    uBit.radio.enable();
}