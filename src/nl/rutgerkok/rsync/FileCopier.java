package nl.rutgerkok.rsync;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * A file copier that copies a directory structure. Ignores unchanged files.
 * 
 */
public class FileCopier {
    private String[] filesInFrom;
    private final File from;
    private final File to;

    public FileCopier(File from, File to) {
        this.from = from;
        this.to = to;
        filesInFrom = from.list();
    }

    /**
     * Copies all files.
     */
    public void copyAll() {
        if(filesInFrom == null) {
            System.err.println("Unable to copy null array. Connection lost?");
            return;
        }
        for (int i = 0; i < filesInFrom.length; i++) {
            copy(i, filesInFrom[i]);
        }
    }

    /**
     * Copies a file or a directory, can be a directory.
     * 
     * @param originalName
     *            The original name of the file.
     */
    private void copy(int i, String originalName) {
        File original = new File(from, originalName);
        File destination = new File(to, originalName);
        if (original.isDirectory()) {
            // We have a directory to copy
            if (destination.isFile()) {
                destination.delete();
            }
            if (!destination.exists()) {
                destination.mkdirs();
            }
            new FileCopier(original, destination).copyAll();
        } else {
            // We have a file to copy
            if (!destination.exists() || destination.lastModified() < original.lastModified()) {
                try {
                    System.out.println("Copying file " + originalName + " (" + i + "/" + filesInFrom.length + ")");
                    copyFile(original, destination);
                } catch (IOException e) {
                    System.err.println("Error copying file " + original + " to " + destination);
                    e.printStackTrace();
                }
            } else {
                System.out.println("Skipping copy of file " + originalName + " (" + i + "/" + filesInFrom.length + ")");
            }
        }
    }

    /**
     * Copies one file, cannot be a directory.
     * 
     * @param sourceFile
     *            The file to copy from.
     * @param destFile
     *            The file to copy to.
     * @throws IOException
     *             If something went wrong.
     */
    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
        destFile.setLastModified(sourceFile.lastModified());
    }
}
