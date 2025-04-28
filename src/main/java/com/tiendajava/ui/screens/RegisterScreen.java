package com.tiendajava.ui.screens;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.User;
import com.tiendajava.service.UserService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.NotificationHandler;
import com.tiendajava.ui.utils.UIUtils;

public class RegisterScreen extends JPanel {

  private final MainUI parent;

  private final JTextField nameField = UIUtils.createTextField(Fonts.NORMAL_FONT);
  private final JTextField lastNameField = UIUtils.createTextField(Fonts.NORMAL_FONT);
  private final JTextField emailField = UIUtils.createTextField(Fonts.NORMAL_FONT);
  private final JPasswordField passwordField = UIUtils.createPasswordField();
  private final JPasswordField passwordConfirmField = UIUtils.createPasswordField();
  private final JTextField typeDocumentField = UIUtils.createTextField(Fonts.NORMAL_FONT);
  private final JTextField numDocumentField = UIUtils.createTextField(Fonts.NORMAL_FONT);
  private final JTextField addressField = UIUtils.createTextField(Fonts.NORMAL_FONT);
  private final JTextField phoneField = UIUtils.createTextField(Fonts.NORMAL_FONT);
  private final UserService userService = new UserService();

  public RegisterScreen(MainUI mainFrame) {
    this.parent = mainFrame;
    setLayout(new BorderLayout());

    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    int y = 0;
    gbc.gridx = 0;
    gbc.gridy = y;
    gbc.gridwidth = 4;

    ImageIcon userIcon = UIUtils.LoadIcon("/icons/user.png");
    JLabel title = new JLabel("Create Account", userIcon, SwingConstants.CENTER);
    title.setFont(Fonts.TITLE_FONT);
    panel.add(title, gbc);

    y = 2;

    gbc.gridwidth = 1;
    addField(panel, gbc, ++y, 0, "Name:", nameField);
    addField(panel, gbc, y, 2, "Lastname:", lastNameField);

    addField(panel, gbc, ++y, 0, "Email:", emailField);
    addField(panel, gbc, y, 2, "Cel:", phoneField);

    addField(panel, gbc, ++y, 0, "Password:", passwordField);
    addField(panel, gbc, y, 2, "Confirm Password:", passwordConfirmField);

    addField(panel, gbc, ++y, 0, "Type of Document:", typeDocumentField);
    addField(panel, gbc, y, 2, "N° Document:", numDocumentField);
    addField(panel, gbc, ++y, 0, "Address:", addressField);

    ImageIcon backIcon = new ImageIcon(getClass().getResource("/icons/back.png"));
    ImageIcon registerIcon = new ImageIcon(getClass().getResource("/icons/user-check.png"));

    gbc.gridx = 1;
    gbc.gridy = y + 2;
    JButton registerBtn = ButtonFactory.createPrimaryButton("Register", registerIcon, this::register);
    panel.add(registerBtn, gbc);

    gbc.gridx = 2;
    JButton backBtn = ButtonFactory.createSecondaryButton("Back", backIcon, () -> {
      SwingUtilities.invokeLater(() -> parent.showScreen("login"));
    });
    panel.add(backBtn, gbc);

    add(panel, BorderLayout.CENTER);
  }

  private void addField(JPanel panel, GridBagConstraints gbc, int y, int x, String label, JComponent field) {
    gbc.gridx = x;
    gbc.gridy = y;
    panel.add(UIUtils.createTextLabel(label, Fonts.BOLD_NFONT), gbc);
    gbc.gridx = x + 1;
    panel.add(field, gbc);
  }

  private void register() {
    String name = nameField.getText().trim();
    String lastname = lastNameField.getText().trim();
    String email = emailField.getText().trim();
    String password = new String(passwordField.getPassword()).trim();
    String confirmPassword = new String(passwordConfirmField.getPassword()).trim();
    String typeDocument = typeDocumentField.getText().trim();
    String numDocument = numDocumentField.getText().trim();
    String address = addressField.getText().trim();
    String phone = phoneField.getText().trim();

    if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
        NotificationHandler.warning(this, "Please complete the required fields");
        return;
    }

    if (!password.equals(confirmPassword)) {
        NotificationHandler.error(this, "Passwords do not match");
        return;
    }

    User user = new User();
    user.setName(name);
    user.setLastName(lastname);
    user.setEmail(email);
    user.setPassword(password);
    user.setTypeDocument(typeDocument.isEmpty() ? "" : typeDocument);
    user.setNumDocument(numDocument.isEmpty() ? "" : numDocument);
    user.setAddress(address.isEmpty() ? "" : address);
    user.setPhone(phone.isEmpty() ? "" : phone);

    ApiResponse<User> response = userService.Register(user);

    if (response.isSuccess()) {
        NotificationHandler.success(this, "Successfully registered user");
        SwingUtilities.invokeLater(() -> parent.showScreen("dashboard"));
    } else {
        NotificationHandler.error(this, response.getMessage());
    }
}

}
