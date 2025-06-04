package com.tiendajava.ui.components;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class FooterPanel extends JPanel {
    
    public FooterPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBackground(UITheme.getSecondaryColor());
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.getTertiaryColor()));
        setPreferredSize(new Dimension(0, 60));

        JPanel footerContent = new JPanel();
        footerContent.setLayout(new FlowLayout(FlowLayout.CENTER));
        footerContent.setBackground(UITheme.getSecondaryColor());
        
        JLabel githubIcon = createLinkLabel("/icons/github.png", "https://github.com/PinchaoRamiro");

        githubIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
        JLabel linkedinIcon = createLinkLabel("/icons/linkedin.png", "https://www.linkedin.com/in/ramiro-pinchao-5038a931b/");
        linkedinIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));

        JLabel copyrightText = new JLabel("© 2025 Ramiro Pinchao. All rights reserved.");
        copyrightText.setFont(Fonts.NORMAL_FONT);
        copyrightText.setForeground(UITheme.getTextColor());
        copyrightText.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        footerContent.add(githubIcon, FlowLayout.LEFT);
        footerContent.add(linkedinIcon, FlowLayout.LEFT);
        footerContent.add(copyrightText, FlowLayout.LEFT);
        footerContent.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Espaciado interno

        add(footerContent);
    }

    private JLabel createLinkLabel(String iconPath, String url) {
        // Crear íconos clickeables
        ImageIcon icon = AppIcons.getIcon(iconPath);
        JLabel label = ButtonFactory.createIconButton(icon, url, () -> {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                NotificationHandler.error( "Cannot open: " + url);
            }
        });
        return label;
    }
}
