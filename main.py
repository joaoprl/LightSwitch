
import usbrelay
import androidServer
import clap
import sys

def main():
    if(len(sys.argv) == 2):
        port = sys.argv[1]
    else:        
        port = 20000

    relay = usbrelay.Relay()
    relay.open()
    
    server = androidServer.Server(20000, relay)
    server.run()

    clapListener = clap.ClapListener(relay)
#    clapListener.run()

    string = ""
    while string != "exit":
        try:
            string = raw_input()
        except (EOFError, KeyboardInterrupt):
            string = "exit"

    clapListener.stop()
    server.stop()
    relay.close()

if __name__ == "__main__":
    main()
