package ui;

import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {
    
    public MainGUI(String nombre) {
        // Configuración de la ventana
        setTitle("Bienvenido");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setLayout(new BorderLayout());

        // Panel principal con BoxLayout (eje Y para apilar elementos verticalmente)
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Márgenes

        // Etiqueta de bienvenida
        JLabel welcomeLabel = new JLabel("Bienvenido, " + nombre );
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botón de cerrar sesión
        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.addActionListener(e -> {
            new LoginGUI().setVisible(true); // Abre la ventana de login
            dispose(); // Cierra la ventana actual
        });

        // Agregar componentes al panel
        panel.add(welcomeLabel);
        panel.add(Box.createVerticalStrut(20)); // Espacio entre elementos
        panel.add(logoutButton);

        // Agregar panel a la ventana
        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }
}
