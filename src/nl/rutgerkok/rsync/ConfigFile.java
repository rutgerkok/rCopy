package nl.rutgerkok.rsync;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Represents the config file. Everything is this class is synchroinized, so it
 * is thread-safe.
 */
public class ConfigFile {
    private File copyFrom;
    private File copyTo;
    private final File settingsFile;

    public ConfigFile(File file) {
        this.settingsFile = file.getAbsoluteFile();
        readFile();
        writeFile();
    }

    /**
     * Gets the directory where the files must be copied from. Make sure that
     * the settings are valid first by calling {@link #readyToCopy()}.
     * 
     * @return The directory.
     */
    public synchronized File getFrom() {
        return this.copyFrom;
    }

    /**
     * Gets the directory where the files must be copied to. Make sure that the
     * settings are valid first by calling {@link #readyToCopy()}.
     * 
     * @return The directory.
     */
    public synchronized File getTo() {
        return this.copyTo;
    }

    /**
     * Reads (reloads) the config file.
     */
    public synchronized void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.settingsFile));
            String line = reader.readLine();
            while (line != null) {
                // Read each line
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    // Skip empty settings
                    if(!parts[1].isEmpty()) {
                        // Read setting
                        switch (parts[0]) {
                            case "copyFrom":
                                this.copyFrom = new File(parts[1]);
                                break;
                            case "copyTo":
                                this.copyTo = new File(parts[1]);
                                break;
                        }
                    }
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            System.err.println("Error in loading config file");
            e.printStackTrace();
        }
    }

    /**
     * Returns whether all settings have been found in the config file and are
     * correct.
     * 
     * @return Whether all settings have been found in the config file and are
     *         correct.
     */
    public synchronized boolean readyToCopy() {
        return this.copyFrom != null && this.copyFrom.exists() && this.copyFrom.isDirectory() && this.copyTo != null && this.copyTo.exists() && this.copyTo.isDirectory();
    }

    /**
     * Sets the directory where the files must be copied from.
     * 
     * @param from
     *            The directory.
     */
    public synchronized void setFrom(File from) {
        this.copyFrom = from;
    }

    /**
     * Sets the directory where the files must be copied to.
     * 
     * @param to
     *            The directory.
     * @param to
     */
    public synchronized void setTo(File to) {
        this.copyTo = to;
    }

    /**
     * Writes (updates) the config file.
     */
    public synchronized void writeFile() {
        try {
            if (this.settingsFile.exists()) {
                this.settingsFile.getParentFile().mkdirs();
                this.settingsFile.createNewFile();
            }
            BufferedWriter writer;
            writer = new BufferedWriter(new FileWriter(this.settingsFile));
            writer.write("copyFrom:" + (copyFrom == null? "" : copyFrom));
            writer.newLine();
            writer.write("copyTo:" + (copyTo == null? "" : copyTo));
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.err.println("Error in saving config file");
            e.printStackTrace();
        }
    }

    /**
     * Gets the directory path where the files must be copied from. If no path has
     * been specified an empty string will be returned.
     * 
     * @return The directory path.
     */
    public String getAbsoluteFromPath() {
        File from = getFrom();
        if (from == null) {
            return "";
        } else {
            return from.getAbsolutePath();
        }
    }

    /**
     * Gets the directory path where the files must be copied to. If no path has
     * been specified an empty string will be returned.
     * 
     * @return The directory path.
     */
    public String getAbsoluteToPath() {
        File to = getTo();
        if (to == null) {
            return "";
        } else {
            return to.getAbsolutePath();
        }
    }
}
