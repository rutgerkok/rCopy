package nl.rutgerkok.rsync;

import java.awt.GraphicsEnvironment;
import java.io.File;

import nl.rutgerkok.rsync.gui.GuiManager;

public class Main {
    public static void main(String[] args) {
        String configFileName = "settings.ini";
        boolean noGui = GraphicsEnvironment.isHeadless();
        for (String arg : args) {
            if (arg.equalsIgnoreCase("nogui")) {
                noGui = true;
            } else {
                configFileName = args[0];
            }
        }

        ConfigFile configFile = new ConfigFile(new File(configFileName));
        if (noGui) {
            // Start without a gui
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
