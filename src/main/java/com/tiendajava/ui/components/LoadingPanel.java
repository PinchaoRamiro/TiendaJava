package com.tiendajava.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class LoadingPanel extends JPanel {
    private int angle = 0;
    private Timer timer;
    private JLabel messageLabel;
    
    public LoadingPanel() {
        this(null);
    }
    
    public LoadingPanel(String message) {
        setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 0, 150)); 
        setOpaque(true);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        
        JPanel spinnerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintSpinner(g);
            }
        };
        spinnerPanel.setOpaque(false);
        spinnerPanel.setPreferredSize(new Dimension(60, 60));
        
        messageLabel = new JLabel(message != null ? message : "Cargando...", SwingConstants.CENTER);
        messageLabel.setFont(Fonts.NORMAL_FONT);
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        centerPanel.add(spinnerPanel, BorderLayout.CENTER);
        centerPanel.add(messageLabel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
        
        timer = new Timer(40, (ActionEvent e) -> {
            angle = (angle + 10) % 360;
            repaint();
        });
    }
    
    private void paintSpinner(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = 20;
        
        for (int i = 0; i < 8; i++) {
            double theta = Math.toRadians(angle + i * 45);
            int x = (int) (centerX + radius * Math.cos(theta));
            int y = (int) (centerY + radius * Math.sin(theta));
            
            int alpha = 255 - (i * 30);
            if (alpha < 50) alpha = 50;
            
            g2d.setColor(new Color(UITheme.getFocusColor().getRed(), 
                                 UITheme.getFocusColor().getGreen(), 
                                 UITheme.getFocusColor().getBlue(), alpha));
            g2d.fillOval(x - 4, y - 4, 8, 8);
        }
        
        g2d.dispose();
    }
    
    public void startAnimation() {
        timer.start();
    }
    
    public void stopAnimation() {
        timer.stop();
    }
    
    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}