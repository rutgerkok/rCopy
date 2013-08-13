package nl.rutgerkok.rsync.gui;

import java.io.File;

import javax.swing.JFileChooser;

public class FileChooser {
    /**
     * Opens a file chooser. Returns the selected file. Returns null when no
     * file was selected.
     * 
     * @return The file, or null if nothing was selected.
     */
    public static File chooseDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }
}
