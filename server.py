# Program to control passerelle between Android application
# and micro-controller through USB tty
import time
import argparse
import signal
import sys
import socket
import socketserver
import serial
import threading

HOST = "192.168.176.122"
UDP_PORT = 10000
MICRO_COMMANDS = ["TL", "LT"]
FILENAME = "values.txt"
LAST_VALUE = bytes("je reçois les données mec", "utf-8")


class ThreadedUDPRequestHandler(socketserver.BaseRequestHandler):

    def handle(self):
        data = self.request[0].strip()
        dataStr = data.decode("utf-8")
        socket = self.request[1]
        current_thread = threading.current_thread()
        print("{}: client: {}, wrote: {}".format(
            current_thread.name, self.client_address, data))
        if dataStr != "":
            if dataStr in MICRO_COMMANDS:  # Send message through UART
                print(dataStr)
                sendUARTMessage(data)
            elif dataStr == "getValues()":  # Sent last value received from micro-controller
                print("getValues():", LAST_VALUE)
                socket.sendto(LAST_VALUE, (self.client_address[0], UDP_PORT))
                # TODO: Create last_values_received as global variable
            else:
                print("Unknown message: ", dataStr)


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

        if (ser.isOpen()):
            while ser.isOpen():
                # time.sleep(100)
                if (ser.inWaiting() > 0):  # if incoming bytes are waiting
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
