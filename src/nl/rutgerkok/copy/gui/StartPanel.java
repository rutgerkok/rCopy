package nl.rutgerkok.copy.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class StartPanel extends JPanel implements ProgressBar {
    private final JProgressBar progressBar;
    private final JLabel progressLabel;

    public StartPanel(final GuiManager manager, final FromToProvider fromTo) {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        // Create progress bar and label
        this.progressBar = new JProgressBar(0, 200);
        this.progressBar.setPreferredSize(new Dimension(200, 22));
        this.progressLabel = new JLabel("Press start");

        // Create start button
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent event) {
                try {
                    // Start copying
                    manager.startCopy(StartPanel.this, fromTo.getFrom(), fromTo.getTo(), new Runnable() {
                        @Override
                        public void run() {
                            // Reset button when we're done
                            ((JButton) event.getSource()).setEnabled(true);
                        }
                    });
                    // Disable the button
                    ((JButton) event.getSource()).setEnabled(false);
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(null, "Cannot copy files. " + e.getMessage(), "Cannot copy files", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(startButton);
        add(this.progressBar);
        add(this.progressLabel);
    }

    @Override
    public void setProgress(float progress, String shortStatus) {
        int progressInt = (int) (this.progressBar.getMinimum() + progress * (this.progressBar.getMaximum() - this.progressBar.getMinimum()));
        this.progressBar.setValue(progressInt);
        this.progressLabel.setText(shortStatus);
    }
}
