# Light Switch
This project uses [hidapi] to communicate with an usb-relay that is connected to a lamp.  

In how many ways can a programmer switch on a lightbulb?

### Setup
TODO: add image describing the setup

### Software dependencies
* [Python2.7]
	* [pyalsaaudio](https://github.com/larsimmisch/pyalsaaudio/#installation)
	* [hidapi] ([Cython](http://cython.org/#download), libusb and libudev on Linux)
* Android 4.0 (IceCreamSandwich or greater) or SDK

### Implemented
* Smartphone switch button
* Clapping (noise)

### To implement
* Smartphone wakeup alarm
* Current ambient light level
* Movement detection

### Contributing
Suggestions are welcome. Please send a pull-request with new suggestions or implemented modules.

### Modules
#### hidapi
The [usbrelay](usbrelay.py) file, implemented using the [Python2.7] [hidapi] to communicate with the relay via USB.

#### Clap
The [clap](clap.py) file, implemented using the [pyalsaaudio] package. The idea is to detect an audio-peak (a clap) using the mic.

#### Server
The [androidServer](androidServer.py) file, used to communicate with the Android module

#### Android
The [Android-Studio](Android-Studio) directory contains the Android app source code. This module communicates with the server, implemented using [Python2.7].

## License
This project is under MIT license
For more information, read the [LICENSE](LICENSE) file

[hidapi]: https://pypi.python.org/pypi/hidapi/0.7.99.post10
[Python2.7]: https://www.python.org/download/releases/2.7/
[pyalsaaudio]: https://github.com/larsimmisch/pyalsaaudio/
