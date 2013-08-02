package nl.rutgerkok.rsync;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Represents the config file.
 * 
 */
public class ConfigFile {
    private File copyFrom;
    private File copyTo;

    public ConfigFile(File file) {
        file = file.getAbsoluteFile();
        readFile(file);
        writeFile(file);
    }

    private void readFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    switch (parts[0]) {
                        case "copyFrom":
                            copyFrom = new File(parts[1]);
                            break;
                        case "copyTo":
                            copyTo = new File(parts[1]);
                            break;
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

    private void writeFile(File file) {
        try {
            if (file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            BufferedWriter writer;
            writer = new BufferedWriter(new FileWriter(file));
            writer.write("copyFrom:" + copyFrom);
            writer.newLine();
            writer.write("copyTo:" + copyTo);
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.err.println("Error in saving config file");
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
    public boolean readyToCopy() {
        return copyFrom != null && copyFrom.exists() && copyFrom.isDirectory() && copyTo != null && copyTo.exists() && copyTo.isDirectory();
    }

    /**
     * Gets the directory where the files must be copied from. Make sure that
     * the settings are valid first by calling {@link #readyToCopy()}.
     * 
     * @return The directory.
     */
    public File getFrom() {
        return copyFrom;
    }
    
    /**
     * Gets the directory where the files must be copied to. Make sure that
     * the settings are valid first by calling {@link #readyToCopy()}.
     * 
     * @return The directory.
     */
    public File getTo() {
        return copyTo;
    }
}
