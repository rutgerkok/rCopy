package nl.rutgerkok.rcopy;

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
    private final int indexInParentDirectory;
    private final ProgressUpdater progressUpdater;
    private final File to;

    public FileCopier(ProgressUpdater progressUpdater, File from, File to) {
        this(progressUpdater, 0, from, to);
    }

    public FileCopier(ProgressUpdater progressUpdater, int indexInParentDirectory, File from, File to) {
        this.progressUpdater = progressUpdater;
        this.from = from;
        this.to = to;
        this.indexInParentDirectory = indexInParentDirectory;
        this.filesInFrom = from.list();
    }

    /**
     * Copies a file or a directory, can be a directory.
     * 
     * @param originalName
     *            The original name of the file.
     */
    private void copy(int i, String originalName) {
        File original = new File(this.from, originalName);
        File destination = new File(this.to, originalName);
        if (original.isDirectory()) {
            // We have a directory to copy
            if (destination.isFile()) {
                destination.delete();
            }
            if (!destination.exists()) {
                destination.mkdirs();
            }
            new FileCopier(this.progressUpdater, i, original, destination).copyAll();
        } else {
            // We have a file to copy
            if (!destination.exists() || hasDifferentLastModifiedDate(destination, original)) {
                try {
                    copyFile(original, destination);
                    this.progressUpdater.onFileCopy(original, i, this.filesInFrom.length);
                } catch (IOException e) {
                    this.progressUpdater.onFileError(original, e, i, this.filesInFrom.length);
                }
            } else {
                this.progressUpdater.onFileSkip(original, i, this.filesInFrom.length);
            }
        }
    }
    
    private boolean hasDifferentLastModifiedDate(File file1, File file2) {
        long difference = file1.lastModified() - file2.lastModified();
        difference = Math.abs(difference);
        return (difference > 100L);
    }

    /**
     * Copies all files.
     */
    public void copyAll() {
        this.progressUpdater.onDirectoryStart(this.from, this.indexInParentDirectory, this.filesInFrom.length);
        if (this.filesInFrom == null) {
            System.err.println("Unable to copy null array. Connection lost?");
            return;
        }
        for (int i = 0; i < this.filesInFrom.length; i++) {
            copy(i, this.filesInFrom[i]);
        }
        this.progressUpdater.onDirectoryEnd();
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
