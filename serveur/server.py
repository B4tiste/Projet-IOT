# Program to control passerelle between Android application
# and micro-controller through USB tty
import json
import re
import socketserver
import serial
import threading

from serveur.datas.Sensor import Sensor

HOST = "192.168.1.24"
UDP_PORT = 10000
MICRO_COMMANDS = ["TL", "LT"]
FILENAME = "../values.txt"
LAST_VALUE = bytes("[" +
                   str(Sensor(0, "Thermometre", "18°c", "T")) + "," +
                   str(Sensor(1, "Capteur de luminosité", "500", "L")) +
                   "]", "utf-8")


class ThreadedUDPRequestHandler(socketserver.BaseRequestHandler):

    def handle(self):
        data = self.request[0].strip()
        dataStr = self.decode(data.decode("utf-8"))
        socket = self.request[1]
        current_thread = threading.current_thread()
        print("{}: client: {}, wrote: {}".format(
            current_thread.name, self.client_address, data))
        if dataStr != "":
            regex = re.compile(r'setOrder\((([A-z])|(([A-z],)+[A-z]))\)')
            if dataStr in MICRO_COMMANDS:  # Send message through UART
                print(dataStr)
                sendUARTMessage(data)
            elif dataStr == "getValues()":  # Sent last value received from micro-controller
                print("getValues():", json.dumps(Sensor.get_sensors()))
                message_to_send = bytes(self.encode(json.dumps(Sensor.get_sensors())), "utf-8")
                socket.sendto(message_to_send, (self.client_address[0], UDP_PORT))
                print("message send: {}".format(message_to_send))
                # TODO: Create last_values_received as global variable
            elif regex.match(dataStr + ""):  # register new order to micro-controller
                regex = re.compile(r'\((([A-z])|(([A-z],)+[A-z]))\)')
                orders = regex.findall(dataStr)[0][0].split(",")
                print(dataStr + ":", orders)
                message_to_send = bytes(self.encode("ok"), "utf-8")
                socket.sendto(message_to_send, (self.client_address[0], UDP_PORT))
                print("message send: {}".format(message_to_send))
                # TODO: Send message to micro-controller
            else:
                print("Unknown message: ", dataStr)

    def cesar(self, message, decalage):
        message_encode = ""
        for i in range(0, len(message)):
            char_to_int = ord(message[i]) + decalage
            int_to_char = chr(char_to_int)
            message_encode += int_to_char
        return message_encode

    def encode(self, message):
        return self.cesar(message, 3)

    def decode(self, message):
        return self.cesar(message, -3)


class ThreadedUDPServer(socketserver.ThreadingMixIn, socketserver.UDPServer):
    pass


# send serial message
SERIALPORT = "COM1"
BAUDRATE = 115200
ser = serial.Serial()


def initUART():
    # ser = serial.Serial(SERIALPORT, BAUDRATE)
    ser.port = SERIALPORT
    ser.baudrate = BAUDRATE
    ser.bytesize = serial.EIGHTBITS  # number of bits per bytes
    ser.parity = serial.PARITY_NONE  # set parity check: no parity
    ser.stopbits = serial.STOPBITS_ONE  # number of stop bits
    ser.timeout = None  # block read

    # ser.timeout = 0             #non-block read
    # ser.timeout = 2              #timeout block read
    ser.xonxoff = False  # disable software flow control
    ser.rtscts = False  # disable hardware (RTS/CTS) flow control
    ser.dsrdtr = False  # disable hardware (DSR/DTR) flow control
    # ser.writeTimeout = 0     #timeout for write

    print('Starting Up Serial Monitor')
    try:
        ser.open()
    except serial.SerialException:
        print("Serial {} port not available".format(SERIALPORT))
        exit()


def sendUARTMessage(msg):
    ser.write(msg)
    print("Message <" + msg + "> sent to micro-controller.")


# Main program logic follows:
if __name__ == '__main__':
    try:
        initUART()
    except:
        pass

    f = open(FILENAME, "a")
    print('Press Ctrl-C to quit.')

    server = ThreadedUDPServer((HOST, UDP_PORT), ThreadedUDPRequestHandler)

    server_thread = threading.Thread(target=server.serve_forever)
    server_thread.daemon = True

    try:
        server_thread.start()
        print("Server started at {} port {}".format(HOST, UDP_PORT))

        if ser.isOpen():
            while ser.isOpen():
                # time.sleep(100)
                if ser.inWaiting() > 0:  # if incoming bytes are waiting
                    data_bytes = ser.read(ser.inWaiting())
                    data_str = data_bytes.decode('utf-8')
                    f.write(data_str + "\n")
                    LAST_VALUE = data_str
                    print(data_str)

        while (1):
            continue

    except (KeyboardInterrupt, SystemExit):
        server.shutdown()
        server.server_close()
        f.close()
        ser.close()
        exit()
