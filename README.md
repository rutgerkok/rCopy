## rCopy

Ultra-simple file copy tool. If the file in the directory to which you are copying already contains a file that was modified at the same time, the file won't be copied.

## Running with a GUI
`java -jar rCopy.jar [path/to/settings/file]`

A simple GUI will pop up. Settings will be saved to the provided settings file. If you don't give the path, `settings.ini` will be used.

![GUI](http://rutgerkok.nl/afbeeldingen/rcopy.png)

## Running without a GUI

`java -jar rCopy.jar nogui [path/to/settings/file]`

If you don't give the path, `settings.ini` will be used.

When running for the first time, the configuration file will be created. You must edit the paths in there. Both directories must exist already. You can then run the program again to copy all files.
