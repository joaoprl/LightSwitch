
import alsaaudio
import time
import audioop
import threading

class ClapListener:
    clapThreshold = 3000

    def __init__(self, relay):
        self.inp = alsaaudio.PCM(alsaaudio.PCM_CAPTURE,alsaaudio.PCM_NONBLOCK)

        # Set attributes: Mono, 8000 Hz, 16 bit little endian samples
        self.inp.setchannels(1)
        self.inp.setrate(8000)
        self.inp.setformat(alsaaudio.PCM_FORMAT_S16_LE)

        # The period size controls the internal number of frames per period.
        # Reads from the device will return this many frames. Each frame being 2 bytes long.
        # This means that the reads below will return either 320 bytes of data
        # or 0 bytes of data. The latter is possible because we are in nonblocking
        # mode.
        self.inp.setperiodsize(160)

        # Relay object
        self.relay = relay

        # Flag: switch only once per peak
        self.hold = False

        self.thread = threading.Thread(target = self.__listen)

    def __listen(self):
        while self.running:
            l,data = self.inp.read() # Read data from mic

            if l: # data was read
                max = audioop.max(data, 2); # max abs input from mic
                if max > ClapListener.clapThreshold and not self.hold:
                    self.relay.switch()
                    self.hold = True
                elif max < ClapListener.clapThreshold:
                    self.hold = False

    def run(self):
        self.running = True
        self.thread.start()

    def stop(self):
        self.running = False
