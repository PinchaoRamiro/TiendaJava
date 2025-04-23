package com.tiendajava.ui.components;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class FooterPanel extends JPanel {
  public FooterPanel() {
      setLayout((LayoutManager) new FlowLayout(FlowLayout.CENTER));
      setBackground(UITheme.getSecondaryColor());
      setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, UITheme.getTertiaryColor()));
      setPreferredSize(new Dimension(0, 40)); 

      JLabel githubLink = new JLabel("<html><a href=''>GitHub</a></html>");
      githubLink.setForeground(UITheme.getTextColor());
      githubLink.setFont(Fonts.BOLD_NFONT);
      githubLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      githubLink.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
              try {
                  Desktop.getDesktop().browse(new URI("https://github.com/PinchaoRamiro"));
              } catch (URISyntaxException | IOException ex) {
              }
          }
      });

      add(githubLink);
  }
}
