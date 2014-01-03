package nl.rutgerkok.copy.gui;

import java.io.File;

import nl.rutgerkok.rcopy.ConfigFile;
import nl.rutgerkok.rcopy.FileCopier;

/**
 * Opens the window, starts the copy process, updates the settings file.
 * 
 */
public class GuiManager {
    private final ConfigFile configFile;
    private Window window;

    public GuiManager(ConfigFile configFile) {
        this.configFile = configFile;
    }

    /**
     * Opens a new window.
     * 
     * @throws IllegalStateException
     *             If the window was already open.
     */
    public void openWindow() {
        if (this.window != null) {
            throw new IllegalStateException("Already opened!");
        }
        this.window = new Window(this, this.configFile.getAbsoluteFromPath(), this.configFile.getAbsoluteToPath());
    }

    /**
     * Starts the copy process.
     * 
     * @param progressBar
     *            The progress bar.
     * @param fromString
     *            The directory to copy from.
     * @param toString
     *            The directory to copy to.
     * @param onFinished
     *            Will be called from the AWT event thread when everything has
     *            been copied.
     * @throws IllegalArgumentException
     *             If the fromString or toString is invalid.
     */
    public void startCopy(final ProgressBar progressBar, String fromString, String toString, final Runnable onFinished) {
        if (fromString.isEmpty()) {
            throw new IllegalArgumentException("No location given to copy from.");
        }
        if (toString.isEmpty()) {
            throw new IllegalArgumentException("No location given to copy to.");
        }

        final File from = new File(fromString);
        if (!from.exists()) {
            throw new IllegalArgumentException("Directory \"" + fromString + "\" doesn't exist.");
        }
        if (!from.isDirectory()) {
            throw new IllegalArgumentException("File \"" + fromString + "\" is not a directory.");
        }
        final File to = new File(toString);

        // Update config file
        this.configFile.setFrom(from);
        this.configFile.setTo(to);
        this.configFile.writeFile();

        // Start
        new Thread() {
            @Override
            public void run() {
                new FileCopier(new GuiProgressUpdater(progressBar, onFinished), from, to).copyAll();
            }
        }.start();
    }
}
