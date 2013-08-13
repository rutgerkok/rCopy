package nl.rutgerkok.rsync.gui;

public interface ProgressBar {
    /**
     * Sets the progress. Must be called from the AWT-event thread.
     * 
     * @param progress
     *            The progress.
     * @param shortStatus
     *            A very short status message.
     */
    public void setProgress(float progress, String shortStatus);
}
