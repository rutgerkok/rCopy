package nl.rutgerkok.copy.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.swing.SwingUtilities;

import nl.rutgerkok.rcopy.ProgressUpdater;

public class GuiProgressUpdater implements ProgressUpdater {
    private static class Directory {
        private final float maxProgress;
        private final float minProgress;
        private final float progressPerStep;

        public Directory(float minProgress, float maxProgress, int files) {
            this.minProgress = minProgress;
            this.maxProgress = maxProgress;
            this.progressPerStep = (maxProgress - minProgress) / files;
        }

        public float getMaxProgress() {
            return this.maxProgress;
        }

        public float getNextProgress(int i) {
            return getProgress(i + 1);
        }

        public float getProgress(int i) {
            return this.minProgress + this.progressPerStep * i;
        }
    }
    private final Runnable callback;
    private Deque<Directory> directories;

    private final ProgressBar progressBar;

    /**
     * Creates a GUI-based progress updater.
     * 
     * @param progressBar
     *            Will be updated.
     * @param callback
     *            Called when all files have been copied. Will be run from the
     *            AWT event thread.
     */
    public GuiProgressUpdater(ProgressBar progressBar, Runnable callback) {
        this.directories = new ArrayDeque<Directory>();
        this.progressBar = progressBar;
        this.callback = callback;
    }

    @Override
    public void onDirectoryEnd() {
        Directory directory = this.directories.pop();
        updateProgress(directory.getMaxProgress(), "Done");

        if (this.directories.isEmpty()) {
            // We're done!
            SwingUtilities.invokeLater(this.callback);
        }
    }

    @Override
    public void onDirectoryStart(File directory, int indexInParentDirectory, int filesInNewDirectory) {
        if (this.directories.isEmpty()) {
            this.directories.push(new Directory(0, 1, filesInNewDirectory));
        } else {
            Directory above = this.directories.peek();
            float minProgress = above.getProgress(indexInParentDirectory);
            float maxProgress = above.getNextProgress(indexInParentDirectory);
            this.directories.push(new Directory(minProgress, maxProgress, filesInNewDirectory));
        }

    }

    @Override
    public void onFileCopy(File file, int numberInDirectory, int filesInDirectory) {
        updateProgress(this.directories.peek().getProgress(numberInDirectory), numberInDirectory + "/" + filesInDirectory + " in dir");
    }

    @Override
    public void onFileError(File file, IOException exception, int numberInDirectory, int filesInDirectory) {
        updateProgress(this.directories.peek().getProgress(numberInDirectory), "Error");
    }

    @Override
    public void onFileSkip(File file, int numberInDirectory, int filesInDirectory) {
        updateProgress(this.directories.peek().getProgress(numberInDirectory), numberInDirectory + "/" + filesInDirectory + " in dir");
    }

    /**
     * Updates the progress.
     * 
     * @param progress
     *            The progress between 0 and 1.
     * @param shortStatus
     *            A very short status message.
     */
    private void updateProgress(final float progress, final String shortStatus) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GuiProgressUpdater.this.progressBar.setProgress(progress, shortStatus);
            }
        });
    }

}
