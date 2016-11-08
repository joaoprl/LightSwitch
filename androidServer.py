
import socket
import sys
import threading

class Server:
    def __init__(self, port, relay):
        self.port = port

        self.running = False
        self.thread = threading.Thread(target = self.__startServer)
        self.thread.setDaemon(True) # dies with main thread

        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.bind(('', port))

        self.relay = relay

    def __startServer(self):
        self.sock.listen(1)

        while self.running:
            conn, addr = self.sock.accept()

            print "connected to " + addr
            isConnected = True

            while(isConnected):
                try:
                    buf = conn.recv(8)

                    if ord(buf[0]) == 1:
                        relay.switch()

                except(socket.error, IndexError):
                    isConnected = False
                    print "disconnected from " + addr

            if(isConnected):
                conn.close()

    def run(self):
        self.running = True
        self.thread.start()

    def stop(self):
        self.running = False
