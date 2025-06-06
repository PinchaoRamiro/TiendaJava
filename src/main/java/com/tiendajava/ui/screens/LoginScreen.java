package com.tiendajava.ui.screens;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.tiendajava.model.Session;
import com.tiendajava.model.User;
import com.tiendajava.service.UserService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UIUtils;
import com.tiendajava.utils.ApiResponse;

public class LoginScreen extends JPanel {
  private final MainUI parent;
  private final JTextField emailField;
  private final JPasswordField passwordField;
  private final UserService userService = new UserService();

  public LoginScreen(MainUI mainFrame) {
    this.parent = mainFrame;
    setLayout(new BorderLayout());

    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(15, 10, 15, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    ImageIcon userIcon = AppIcons.USER_CHECK_ICON;
    JLabel title = new JLabel("Login", userIcon, SwingConstants.CENTER);
    title.setFont(Fonts.TITLE_FONT);
    gbc.gridwidth = 2;
    gbc.gridx = 0;
    gbc.gridy = 1;
    panel.add(title, gbc);

    gbc.gridwidth = 1;
    gbc.gridy++;
    gbc.gridx = 0;
    panel.add(UIUtils.createTextLabel("Email", Fonts.BOLD_NFONT), gbc);

    gbc.gridx = 1;
    emailField = UIUtils.createTextField(Fonts.NORMAL_FONT);
    panel.add(emailField, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    panel.add(UIUtils.createTextLabel("Password", Fonts.BOLD_NFONT), gbc);

    gbc.gridx = 1;
    passwordField = UIUtils.createPasswordField();
    panel.add(passwordField, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    gbc.gridwidth = 2;

    ImageIcon loginIcon = AppIcons.USER_CHECK_ICON;
    ImageIcon registerIcon = AppIcons.USER_PLUS_ICON;

    JButton loginBtn = ButtonFactory.createPrimaryButton("Login", loginIcon, this::login);
    panel.add(loginBtn, gbc);

    gbc.gridy++;
    JButton regisBtn = ButtonFactory.createSecondaryButton("Register", registerIcon, () -> {
      SwingUtilities.invokeLater(() -> parent.showScreen("register"));
    });
    panel.add(regisBtn, gbc);

    UIUtils.styleFormFields(panel);

    add(panel, BorderLayout.CENTER);
  }

  private void login() {
    String email = emailField.getText().trim();
    String password = new String(passwordField.getPassword()).trim();

    if (email.isEmpty() || password.isEmpty()) {
        NotificationHandler.warning(this, "Please complete the required fields");
        return;
    }

    parent.showLoading("Logging in...");

    SwingUtilities.invokeLater(() -> {
        ApiResponse<User> response = userService.login(email, password);
        parent.hideLoading();

        if (response.isSuccess()) {
            NotificationHandler.success(
                this, response.getMessage() != null ? response.getMessage() : "Login successful"
            );
            if(Session.getInstance().getRole().equals("admin")) {
                parent.showScreen("admin-dashboard");
            } else {
                parent.showScreen("dashboard");        
            }
            parent.updateSidebarVisibility();
        } else {
            NotificationHandler.error(
                this, response.getMessage() != null ? response.getMessage() : "Login failed"
            );
        }
    });
}

}