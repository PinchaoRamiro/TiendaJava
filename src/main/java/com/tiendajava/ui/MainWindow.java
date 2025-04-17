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

import com.tiendajava.ui.utils.UIUtils;

public class MainWindow extends JFrame {

    public MainWindow(String name, String lastName) {
        setTitle("Welcome");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        UIUtils.applyDarkTheme();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        panel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        JLabel welcomeLabel = new JLabel("Welcome, " + name + " " + lastName);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton logoutButton = UIUtils.createButton("Logout", () -> {
            new LoginGUI().setVisible(true);
            dispose();
        });
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(welcomeLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(logoutButton);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }
}
