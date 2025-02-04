package clase.Futbol_App;
import java.awt.*;
import javax.swing.*;

public class SplashScreen extends JDialog {
    public JProgressBar progressBar;

    public SplashScreen() {
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setUndecorated(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JLabel logoLabel = new JLabel("Aplicación Futbol", JLabel.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(new Color(0, 102, 204));

        JLabel imageLabel = new JLabel(new ImageIcon("img/screen.png")); // Cambiar por una imagen válida.
        JLabel messageLabel = new JLabel("Cargando recursos, por favor espere... :)", JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setForeground(Color.GRAY);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(0, 102, 204));

        mainPanel.add(logoLabel, BorderLayout.NORTH);
        mainPanel.add(imageLabel, BorderLayout.CENTER);
        mainPanel.add(messageLabel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        add(progressBar, BorderLayout.SOUTH);
        
        setVisible(rootPaneCheckingEnabled);
    }

    public void updateProgress(int value) {
        progressBar.setValue(value);
    }
}

