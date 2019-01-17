# jpixelflut-client
This is a simple pixelflut-client written in java.
It can process images and send the pixels to a server. It even supports multithreading.

## Basic usage
```console
java -jar jpixelflut.jar -ip 192.168.0.20 -port 1337 -img path_to_image.png -threads 4
```
## Building
With the command `gradle build` the program gets compiled and packed into a runnable .jar file. 
The program can be found in `build/libs/jpixelflut-client.jar`.
