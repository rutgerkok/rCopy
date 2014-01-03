package nl.rutgerkok.copy.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nl.rutgerkok.copy.gui.FileChooser;

public class FileChooserPanel extends JPanel {
    private final JTextField textField;

    public FileChooserPanel(String label, String defaultPath) {
        // Align right
        setLayout(new FlowLayout(FlowLayout.RIGHT));

        // Label
        add(new JLabel(label));

        // Text field
        this.textField = new JTextField(defaultPath);
        this.textField.setPreferredSize(new Dimension(250, 22));
        add(this.textField);

        // Browse button
        JButton button = new JButton("Browse...");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                File opened = FileChooser.chooseDirectory();
                if (opened != null) {
                    FileChooserPanel.this.textField.setText(opened.getAbsolutePath());
                }
            }
        });
        add(button);
    }

    public String getText() {
        return this.textField.getText();
    }
}
