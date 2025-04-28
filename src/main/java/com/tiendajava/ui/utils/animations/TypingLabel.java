package com.tiendajava.ui.utils.animations;

import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.UIManager;

public class TypingLabel extends JLabel {
    
    private String fullText = "";
    private int currentIndex = 0;
    private Timer typingTimer;

    public TypingLabel(String text, int delayMillis) {
        setForeground(UIManager.getColor("Label.foreground")); 
        setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 34)); 
        
        this.fullText = text;

        typingTimer = new Timer(delayMillis, (ActionEvent e) -> {
            if (currentIndex < fullText.length()) {
                setText(fullText.substring(0, currentIndex + 1));
                currentIndex++;
            } else {
                typingTimer.stop();
            }
        });
    }

    public void startTyping() {
        currentIndex = 0;
        setText("");
        typingTimer.start();
    }
}
