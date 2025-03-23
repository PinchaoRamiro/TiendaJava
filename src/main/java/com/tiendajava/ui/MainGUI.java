package com.tiendajava.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class MainGUI extends JFrame {
    
    public MainGUI(String name, String lastName) {
        setTitle("Welcome");
        setSize(400, 500); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        UIManager.put("Panel.background", new Color(45, 45, 45));
        UIManager.put("Label.foreground", Color.WHITE);
        UIManager.put("Button.background", new Color(70, 130, 180));
        UIManager.put("Button.foreground", Color.WHITE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
        panel.setBackground(new Color(45, 45, 45)); 

        JLabel welcomeLabel = new JLabel("Welcome, " + name + " " + lastName);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.addActionListener(e -> {
            new LoginGUI().setVisible(true);
            dispose();
        });

        panel.add(welcomeLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(logoutButton);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }
}
