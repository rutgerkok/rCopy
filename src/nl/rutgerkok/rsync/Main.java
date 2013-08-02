package nl.rutgerkok.rsync;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        String configFileName = "settings.ini";
        if(args.length == 1) {
            configFileName = args[0];
        }
        ConfigFile configFile = new ConfigFile(new File(configFileName));
        if(!configFile.readyToCopy()) {
            System.err.println("Please enter valid path in the config file (" + configFileName + ")");
        } else {
            new FileCopier(configFile.getFrom(), configFile.getTo()).copyAll();
            System.out.println("Done");
        }
    }
}
