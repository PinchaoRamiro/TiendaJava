package com.tiendajava.ui.screens;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.tiendajava.model.Session;
import com.tiendajava.model.User;
import com.tiendajava.service.UserService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;
import com.tiendajava.utils.ApiResponse;

public class RegisterScreen extends JPanel {
    private final MainUI parent;
    private final UserService userService;

    // Campos del formulario
    private final JTextField nameField;
    private final JTextField lastNameField;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JPasswordField passwordConfirmField;
    private final JTextField typeDocumentField;
    private final JTextField numDocumentField;
    private final JTextField addressField;
    private final JTextField phoneField;

    public RegisterScreen(MainUI parent) {
        this.parent = parent;
        this.userService = new UserService();

        // Inicializar campos del formulario
        this.nameField = UIUtils.createTextField(Fonts.NORMAL_FONT);
        this.lastNameField = UIUtils.createTextField(Fonts.NORMAL_FONT);
        this.emailField = UIUtils.createTextField(Fonts.NORMAL_FONT);
        this.passwordField = UIUtils.createPasswordField();
        this.passwordConfirmField = UIUtils.createPasswordField();
        this.typeDocumentField = UIUtils.createTextField(Fonts.NORMAL_FONT);
        this.numDocumentField = UIUtils.createTextField(Fonts.NORMAL_FONT);
        this.addressField = UIUtils.createTextField(Fonts.NORMAL_FONT);
        this.phoneField = UIUtils.createTextField(Fonts.NORMAL_FONT);

        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(10, 40, 20, 40));
        formPanel.setBackground(UITheme.getPrimaryColor());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // === Título ===
        JLabel title = new JLabel("Create Account", AppIcons.USER_PLUS_ICON, SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 4;
        formPanel.add(title, gbc);

        gbc.gridwidth = 1;
        y += 2;

        addField(formPanel, gbc, y++, "Name:", nameField, "Lastname:", lastNameField);
        addField(formPanel, gbc, y++, "Email:", emailField, "Phone:", phoneField);
        addField(formPanel, gbc, y++, "Password:", passwordField, "Confirm Password:", passwordConfirmField);
        addField(formPanel, gbc, y++, "Document Type:", typeDocumentField, "Document No.:", numDocumentField);

        // Campo individual (Address, usa solo la mitad izquierda)
        gbc.gridx = 0;
        gbc.gridy = y++;
        formPanel.add(UIUtils.createTextLabel("Address:", Fonts.BOLD_NFONT), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(addressField, gbc);
        // Label derecha
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        formPanel.add(UIUtils.createTextLabel("", Fonts.BOLD_NFONT ), gbc);

        // === Botones de acción ===
        JButton registerBtn = ButtonFactory.createPrimaryButton("Register", AppIcons.USER_CHECK_ICON, this::register);

        gbc.gridy = y++;
        gbc.gridx = 1;
        formPanel.add(registerBtn, gbc);

        if(!Session.getInstance().isLogged()){
          JButton backButton = ButtonFactory.createSecondaryButton("Back", AppIcons.BACK_ICON, () -> parent.showScreen("login"));
          gbc.gridx = 2;
          formPanel.add(backButton, gbc);
        }

        add(formPanel, BorderLayout.CENTER);
    }

  private void addField(JPanel panel, GridBagConstraints gbc, int y, String labelLeft, JComponent fieldLeft,
      String labelRight, JComponent fieldRight) {

    // Label izquierda
    gbc.gridx = 0;
    gbc.gridy = y;
    panel.add(UIUtils.createTextLabel(labelLeft, Fonts.BOLD_NFONT), gbc);

    // Campo izquierda
    gbc.gridx = 1;
    gbc.weightx = 1.0;
    panel.add(fieldLeft, gbc);

    // Label derecha
    gbc.gridx = 2;
    gbc.weightx = 0.0;
    panel.add(UIUtils.createTextLabel(labelRight, Fonts.BOLD_NFONT), gbc);

    // Campo derecha
    gbc.gridx = 3;
    gbc.weightx = 1.0;
    panel.add(fieldRight, gbc);
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

    if("admin".equals(Session.getInstance().getRole())){
      NotificationHandler.info(this, "Successfully registered user");
      return;
    }

    if (response.isSuccess()) {
      NotificationHandler.success(this, "Successfully registered user");
      parent.showScreen("dashboard");
    } else {
      NotificationHandler.error(this, response.getMessage());
    }
  }

}
