package nl.rutgerkok.rcopy;

import java.io.File;
import java.io.IOException;

public interface ProgressUpdater {
    /**
     * Called when we are done copying all the files in a subdirectory.
     */
    public void onDirectoryEnd();

    /**
     * Called when a new subdirectory is opened for reading.
     * 
     * @param directory
     *            The directory that is opened.
     * @param numberInParentDirectory
     *            The index of the directory in it's parent directory.
     *            Zero-based.
     * @param filesInNewDirectory
     *            The number of files that this directory contains.
     */
    public void onDirectoryStart(File directory, int numberInParentDirectory, int filesInNewDirectory);

    /**
     * Called when a file has been successfully copied.
     * 
     * @param file
     *            The file that was copied.
     * @param numberInDirectory
     *            The index of the file in it's directory. Zero-based.
     * @param filesInDirectory
     *            The total number of files in it's directory.
     */
    public void onFileCopy(File file, int numberInDirectory, int filesInDirectory);

    /**
     * Called when a file couldn't be copied.
     * 
     * @param file
     *            The file.
     * @param exception
     *            What went wrong.
     * @param numberInDirectory
     *            The index of the file in it's directory. Zero-based.
     * @param filesInDirectory
     *            The total number of files in it's directory.
     */
    public void onFileError(File file, IOException exception, int numberInDirectory, int filesInDirectory);

    /**
     * Called when a file is skipped because it is already up to date.
     * 
     * @param file
     *            The file that wasn't copied.
     * @param numberInDirectory
     *            The index of the file in it's directory. Zero-based.
     * @param filesInDirectory
     *            The total number of files in it's directory.
     */
    public void onFileSkip(File file, int numberInDirectory, int filesInDirectory);

}
