package com.tiendajava.ui.components.dialogs;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.NotificationHandler;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class DeleteConfirmDialog extends JDialog {

    private final Runnable onConfirm;

    public DeleteConfirmDialog(String entityName, Runnable onConfirm) {
        this.onConfirm = onConfirm;

        setTitle("Confirm Deletion");
        setModal(true);
        setSize(350, 200);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.getPrimaryColor());

        buildDialog(entityName);
    }

    private void buildDialog(String entityName) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.getPrimaryColor());
        panel.setBorder(UIUtils.getDefaultPadding());

        JLabel confirmLabel = new JLabel(
            "Are you sure you want to delete" + entityName + "?",
            JLabel.CENTER
        );
        confirmLabel.setFont(Fonts.SUBTITLE_FONT);
        confirmLabel.setForeground(UITheme.getErrorColor());
        panel.add(confirmLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(UITheme.getPrimaryColor());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 12, 6, 12);

        JButton yesBtn = ButtonFactory.createDangerButton("Delete", null, this::confirm);
        JButton noBtn = ButtonFactory.createSecondaryButton("Cancel", null, this::cancel);

        gbc.gridx = 0;
        buttonPanel.add(yesBtn, gbc);

        gbc.gridx = 1;
        buttonPanel.add(noBtn, gbc);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void confirm() {
        onConfirm.run();
        NotificationHandler.success("Deleted successfully!");
        dispose();
    }

    private void cancel() {
        dispose();
    }
}


/*
 * private void deleteProduct(Product product) {
    new DeleteConfirmDialog(product.getName(), () -> {
        productService.deleteProduct(product.getProductId());
        loadProducts(); // recargar productos
    }).setVisible(true);
    }

    private void deleteUser(User user) {
    new DeleteConfirmDialog(user.getEmail(), () -> {
        userService.deleteUser(user.getId());
        loadUsers(); // recargar usuarios
    }).setVisible(true);
    }


 */