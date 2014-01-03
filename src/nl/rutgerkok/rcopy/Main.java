package nl.rutgerkok.rcopy;

import java.awt.GraphicsEnvironment;
import java.io.File;

import nl.rutgerkok.copy.gui.GuiManager;

public class Main {
    public static final String NAME = "rCopy";
    public static final String VERSION = "1.1";
    
    public static void main(String[] args) {
        String configFileName = "settings.ini";
        boolean noGui = GraphicsEnvironment.isHeadless();
        for (String arg: args) {
            if (arg.equalsIgnoreCase("nogui")) {
                noGui = true;
            } else {
                configFileName = arg;
            }
        }

        ConfigFile configFile = new ConfigFile(new File(configFileName));
        if (noGui) {
            // Start without a gui
            System.out.println("Starting " + NAME + " v" + VERSION);
            if (!configFile.readyToCopy()) {
                System.err.println("Please enter valid path in the config file (" + configFileName + ")");
            } else {
                new FileCopier(new ConsoleProgressUpdater(), configFile.getFrom(), configFile.getTo()).copyAll();
                System.out.println("Done");
            }
        } else {
            // Start with a gui
            new GuiManager(configFile).openWindow();
        }
    }
}
