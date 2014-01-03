package nl.rutgerkok.rcopy;

import java.io.File;
import java.io.IOException;

public class ConsoleProgressUpdater implements ProgressUpdater {
    private String prefix = "";

    @Override
    public void onDirectoryEnd() {
        if (this.prefix.length() > 0) {
            this.prefix = this.prefix.substring(0, this.prefix.length() - 1);
        }
    }

    @Override
    public void onDirectoryStart(File directory, int indexInParentDirectory, int filesInNewDirectory) {
        this.prefix += "  ";
    }

    @Override
    public void onFileCopy(File file, int numberInDirectory, int filesInDirectory) {
        System.out.println(this.prefix + "Copying file " + file.getAbsolutePath() + " (" + (numberInDirectory + 1) + "/" + filesInDirectory + ")");
    }

    @Override
    public void onFileError(File file, IOException exception, int numberInDirectory, int filesInDirectory) {
        System.err.println(this.prefix + "Error copying file " + file.getAbsolutePath());
        exception.printStackTrace();
    }

    @Override
    public void onFileSkip(File file, int numberInDirectory, int filesInDirectory) {
        System.out.println(this.prefix + "Skipping copy of file " + file.getAbsolutePath() + " (" + (numberInDirectory + 1) + "/" + filesInDirectory + ")");
    }

}
