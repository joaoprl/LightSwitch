
import hid

class Relay:
    vendor_id = 5824

    def __init__(self):
        try:
            self.path = hid.enumerate(Relay.vendor_id)[0]['path']
        except(IndexError):
            raise RuntimeError("No relay was found")
            
        self.device = hid.device()
        self.isOpen = False

    def open(self):
        if(not self.isOpen):
            self.device.open_path(self.path) # open by path
            self.isOpen = True

    def turnOn(self):
        if(self.isOpen):
            self.device.write([0xff, 1])

    def turnOff(self):
        if(self.isOpen):
            self.device.write([0xfd, 1])

    def close(self):
        if(self.isOpen):
            self.device.close()
            self.isOpen = False

    def switch(self):
        if(self.isOpen):
            if(self.status() == 1):
                self.turnOff()
            else:
                self.turnOn()

    def status(self):
        if(self.isOpen):
            return self.device.get_feature_report(0x0, 9)[8]

    def get_manufacturer_string(self):
        if(self.isOpen):
            return self.device.get_manufacturer_string()

    def get_serial_number_string(self):
        if(self.isOpen):
            return self.device.get_serial_number_string()
