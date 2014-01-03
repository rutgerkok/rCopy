package nl.rutgerkok.copy.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.UIManager;

import nl.rutgerkok.rcopy.Main;

/**
 * Represents the window with all it's panels.
 * 
 */
public class Window {
    public Window(GuiManager manager, String defaultFrom, String defaultTo) {
        // Set the look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create the window
        JFrame frame = new JFrame();
        frame.setBounds(100, 100, 420, 150);
        frame.setTitle(Main.NAME + " v" + Main.VERSION);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add panels and make window visible
        SettingsPanel settingsPanel = new SettingsPanel(defaultFrom, defaultTo);
        frame.add(settingsPanel, BorderLayout.CENTER);
        frame.add(new StartPanel(manager, settingsPanel), BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}
