package ui;

import javax.swing.*;

public class MainGUI extends JFrame {
    public MainGUI(String nombre) {
        setTitle("Bienvenido");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel welcomeLabel = new JLabel("Bienvenido " + nombre);
        welcomeLabel.setBounds(50, 30, 200, 25);
        add(welcomeLabel);

        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.setBounds(80, 70, 140, 25);
        add(logoutButton);

        logoutButton.addActionListener(e -> {
            new LoginGUI().setVisible(true);
            dispose();
        });

        setVisible(true);
    }
}
