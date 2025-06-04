package com.tiendajava.ui.screens;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

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

public class AccountSettingsScreen extends JPanel {

    private final MainUI parent;
    private final UserService userService = new UserService();

    private final JTextField nameField = new JTextField();
    private final JTextField lastNameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField typeDocumentField = new JTextField();
    private final JTextField documentField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JTextField addressField = new JTextField();
    private final JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Active", "Inactive"});

    public AccountSettingsScreen(MainUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());
        setBorder(new EmptyBorder(30, 30, 30, 30));

        initializeUI();
        loadCurrentUser();
    }

    private void initializeUI() {
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);

        JPanel formPanel = createTwoColumnFormPanel();
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUI(createCustomScrollBarUI());
        scrollPane.getViewport().setBackground(UITheme.getPrimaryColor());

        add(scrollPane, BorderLayout.CENTER);
    }

    private javax.swing.plaf.basic.BasicScrollBarUI createCustomScrollBarUI() {
        return new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = UITheme.getTertiaryColor();
                this.trackColor = UITheme.getSecondaryColor();
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        };
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Account Settings", AppIcons.USER_ICON, SwingConstants.LEFT);
        title.setFont(Fonts.TITLE_FONT.deriveFont(Font.BOLD, 24));
        title.setForeground(UITheme.getTextColor());

        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleContainer.setOpaque(false);
        titleContainer.add(title);

        titlePanel.add(titleContainer, BorderLayout.WEST);
        return titlePanel;
    }

    private JPanel createTwoColumnFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createFormField("Name:", nameField), gbc);

        gbc.gridy++;
        formPanel.add(createFormField("Email:", emailField), gbc);

        gbc.gridy++;
        formPanel.add(createFormField("Document Type:", typeDocumentField), gbc);

        gbc.gridy++;
        formPanel.add(createFormField("Address:", addressField), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(createFormField("Last Name:", lastNameField), gbc);

        gbc.gridy++;
        formPanel.add(createFormField("Phone:", phoneField), gbc);

        gbc.gridy++;
        formPanel.add(createFormField("Document No.:", documentField), gbc);

        gbc.gridx = 0;
        gbc.gridy += 2;
        gbc.gridwidth = 2;
        JPanel statusFieldPanel = createStatusField();
        statusFieldPanel.setPreferredSize(new Dimension(50, 55));
        formPanel.add(statusFieldPanel, gbc);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton saveButton = ButtonFactory.createPrimaryButton("Save Changes", null, this::saveChanges);
        saveButton.setPreferredSize(new Dimension(200, 40));
        formPanel.add(saveButton, gbc);

        return formPanel;
    }

    private JPanel createFormField(String labelText, JTextField textField) {
        JPanel fieldPanel = new JPanel(new BorderLayout(5, 5));
        fieldPanel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(Fonts.NORMAL_FONT);
        label.setForeground(UITheme.getTextColor());

        textField.setFont(Fonts.NORMAL_FONT);
        textField.setForeground(UITheme.getTextColor());
        textField.setBackground(UITheme.getBackgroundContrast());
        textField.setBorder(new MatteBorder(0, 0, 1, 0, UITheme.getBorderColor()));
        textField.setPreferredSize(new Dimension(250, 35));

        // Efecto de enfoque
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(new MatteBorder(0, 0, 1, 0, UITheme.getFocusColor()));
            }

            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(new MatteBorder(0, 0, 1, 0, UITheme.getBorderColor()));
            }
        });

        fieldPanel.add(label, BorderLayout.NORTH);
        fieldPanel.add(textField, BorderLayout.CENTER);

        return fieldPanel;
    }

    private JPanel createStatusField() {
        JPanel statusPanel = new JPanel(new BorderLayout(5, 5));
        statusPanel.setOpaque(false);

        JLabel statusLabel = new JLabel("Status");
        statusLabel.setFont(Fonts.NORMAL_FONT);
        statusLabel.setForeground(UITheme.getTextColor());

        UIUtils.styleComboBox(statusComboBox);

        statusPanel.add(statusLabel, BorderLayout.NORTH);
        statusPanel.add(statusComboBox, BorderLayout.CENTER);

        return statusPanel;
    }

    private void loadCurrentUser() {
        User user = Session.getInstance().getUser();
        if (user != null) {
            nameField.setText(user.getName());
            lastNameField.setText(user.getLastName());
            emailField.setText(user.getEmail());
            typeDocumentField.setText(user.getTypeDocument());
            documentField.setText(user.getNumDocument());
            phoneField.setText(user.getPhone());
            addressField.setText(user.getAddress());
            statusComboBox.setSelectedItem(user.getStatus() ? "Active" : "Inactive");
        } else {
            NotificationHandler.error("User not found in session.");
        }
    }

    private void saveChanges() {
        if (nameField.getText().trim().isEmpty() ||
            lastNameField.getText().trim().isEmpty() ||
            emailField.getText().trim().isEmpty()) {
            NotificationHandler.warning("Name, Last Name and Email are required fields.");
            return;
        }

        User updatedUser = new User();
        updatedUser.setId(Session.getInstance().getUser().getId());
        updatedUser.setName(nameField.getText().trim());
        updatedUser.setLastName(lastNameField.getText().trim());
        updatedUser.setEmail(emailField.getText().trim());
        updatedUser.setTypeDocument(typeDocumentField.getText().trim());
        updatedUser.setNumDocument(documentField.getText().trim());
        updatedUser.setPhone(phoneField.getText().trim());
        updatedUser.setAddress(addressField.getText().trim());
        updatedUser.setStatus(statusComboBox.getSelectedItem().equals("Active"));

        ApiResponse<User> response = userService.UpdateUser(updatedUser);

        if (response.isSuccess()) {
            NotificationHandler.success("Profile updated successfully!");
            Session.getInstance().setUser(updatedUser);
        } else {
            NotificationHandler.error("Failed to update profile: " + response.getMessage());
        }
    }
}
