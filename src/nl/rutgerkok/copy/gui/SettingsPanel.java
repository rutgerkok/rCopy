package nl.rutgerkok.copy.gui;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class SettingsPanel extends JPanel implements FromToProvider {
    private final FileChooserPanel textFieldFrom;
    private final FileChooserPanel textFieldTo;

    public SettingsPanel(String defaultFrom, String defaultTo) {
        setLayout(new GridLayout(2, 1));

        // From field
        this.textFieldFrom = new FileChooserPanel("From: ", defaultFrom);
        add(this.textFieldFrom);

        // To field
        this.textFieldTo = new FileChooserPanel("To: ", defaultTo);
        add(this.textFieldTo);
    }

    @Override
    public String getFrom() {
        return this.textFieldFrom.getText();
    }

    @Override
    public String getTo() {
        return this.textFieldTo.getText();
    }
}
