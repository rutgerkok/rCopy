package nl.rutgerkok.copy.gui;

public interface FromToProvider {

    /**
     * Gets the location the user entered to copy the files from.
     * 
     * @return The location.
     */
    String getFrom();

    /**
     * Gets the location the user entered to copy the files to.
     * 
     * @return The location.
     */
    String getTo();

}