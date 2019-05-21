# jpixelflut-client
This is a simple pixelflut-client written in java.
It can process images and send the pixels to a server. It even supports multithreading.

## Basic usage
```console
java -jar jpixelflut.jar -ip 192.168.0.20 -port 1337 -img path_to_image.png -threads 4
```


## Development
### Building
With the command `gradle build` the program is compiled and packed into a runnable .jar file. 
The program can be found in `build/libs/jpixelflut-client.jar`.

### Profiling
Use `java -agentlib:hprof=cpu=samples -jar build/libs/jpixelflut-client.jar` to profile the application and find bottlenecks.


## What is Pixelflut?
See [https://github.com/defnull/pixelflut](https://github.com/defnull/pixelflut) for more details, client and server software.
