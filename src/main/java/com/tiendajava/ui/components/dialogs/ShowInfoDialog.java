package com.tiendajava.ui.components.dialogs;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class ShowInfoDialog extends JDialog {

    private final JPanel infoPanel = new JPanel(new GridBagLayout());

    public ShowInfoDialog(String title, Map<String, String> infoData) {
        setTitle(title);
        setModal(true);
        setSize(450, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.getPrimaryColor());
        setLayout(new BorderLayout());
        
        infoPanel.setBackground(UITheme.getPrimaryColor());
        infoPanel.setBorder(UIUtils.getDefaultPadding());

        JScrollPane scrollPane = new JScrollPane(infoPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(UITheme.getPrimaryColor());
        scrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());
        scrollPane.getHorizontalScrollBar().setUI(UIUtils.createDarkScrollBar());

        add(scrollPane, BorderLayout.CENTER);

        buildInfo(infoData);

        // Botón de Cerrar
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(UITheme.getPrimaryColor());

        JButton closeButton = ButtonFactory.createSecondaryButton("Close", null, this::dispose);
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void buildInfo(Map<String, String> infoData) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;
        for (Map.Entry<String, String> entry : infoData.entrySet()) {
            // Etiqueta
            gbc.gridx = 0;
            gbc.gridy = row;
            JLabel keyLabel = new JLabel(entry.getKey() + ":");
            keyLabel.setForeground(UITheme.getTextColor());
            keyLabel.setFont(Fonts.SUBTITLE_FONT);
            infoPanel.add(keyLabel, gbc);

            // Valor
            gbc.gridx = 1;
            JLabel valueLabel = new JLabel(entry.getValue());
            valueLabel.setForeground(UITheme.getTextColor());
            valueLabel.setFont(Fonts.NORMAL_FONT);
            infoPanel.add(valueLabel, gbc);

            row++;
        }
    }
}
