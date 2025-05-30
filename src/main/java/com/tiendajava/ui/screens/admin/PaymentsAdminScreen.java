package com.tiendajava.ui.screens.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.User;
import com.tiendajava.model.orders.Order;
import com.tiendajava.service.OrderService;
import com.tiendajava.service.UserService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;
public class PaymentsAdminScreen extends JPanel {

    private final MainUI parent;
    private final OrderService orderService = new OrderService();
    private final UserService userService = new UserService();
    private final JPanel paymentsContainer = new JPanel();

    private List<Order> orders;

    // Colores para los diferentes estados
    private static final Color PENDING_COLOR = new Color(255, 215, 0); // Amarillo claro
    private static final Color APPROVED_COLOR = new Color(144, 238, 144); // Verde claro
    private static final Color REJECTED_COLOR = new Color(255, 182, 193); // Rojo claro
    private static final int CARD_WIDTH_PERCENT = 90; // Porcentaje del ancho de la pantalla
    private static final int CARD_HEIGHT = 120; // Altura fija para las tarjetas

    public PaymentsAdminScreen(MainUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initializeUI();
        loadPayments();
    }

    private void initializeUI() {

        headerPanel();

        // Panel de contenido con scroll y layout adaptable
        paymentsContainer.setLayout(new BoxLayout(paymentsContainer, BoxLayout.Y_AXIS));
        paymentsContainer.setBackground(UITheme.getPrimaryColor());

        JScrollPane scrollPane = new JScrollPane(paymentsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());
        scrollPane.getHorizontalScrollBar().setUI(UIUtils.createDarkScrollBar());
        scrollPane.getViewport().setBackground(UITheme.getPrimaryColor());

        add(scrollPane, BorderLayout.CENTER);
    }

    private void headerPanel(){
        // Panel de título

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);

        JPanel titlePanel = createTitlePanel();
        contentPanel.add(titlePanel, BorderLayout.NORTH);

        // Botón para filtrar por estado
        JLabel filterLabel = new JLabel("Filter by status:");
        filterLabel.setFont(Fonts.NORMAL_FONT);
        JComboBox<String> statusComboBox = filterButtonActionPerformed();
        statusComboBox.addActionListener(e -> {
            String selectedStatus = (String) statusComboBox.getSelectedItem();
            if (selectedStatus != null) {
                filterOrdersByStatus(selectedStatus);
            }
        });

        statusComboBox.setPreferredSize(new Dimension(100, 30));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setOpaque(false);
        filterPanel.add(filterLabel);
        filterPanel.add(statusComboBox);

        contentPanel.add(filterPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.NORTH);

    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("💳 Payment Management", null, SwingConstants.LEFT);
        title.setFont(Fonts.TITLE_FONT.deriveFont(Font.BOLD, 24));
        title.setForeground(UITheme.getTextColor());

        titlePanel.add(title, BorderLayout.WEST);
        titlePanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        return titlePanel;
    }

    private void loadPayments() {
        paymentsContainer.removeAll();

        ApiResponse<List<Order>> response = orderService.getAllOrders();
        if (!response.isSuccess()) {
            NotificationHandler.error("Failed to load payments: " + response.getMessage());
            return;
        }

        orders = response.getData();
        if (orders == null || orders.isEmpty()) {
            paymentsContainer.add(createEmptyStatePanel());
        } else {
            for (Order order : orders) {
                paymentsContainer.add(createPaymentCard(order));
                paymentsContainer.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        paymentsContainer.revalidate();
        paymentsContainer.repaint();
    }

    private JPanel createEmptyStatePanel() {
        JPanel emptyPanel = new JPanel(new BorderLayout());
        emptyPanel.setBackground(UITheme.getPrimaryColor());
        emptyPanel.setBorder(new EmptyBorder(40, 0, 40, 0));

        JLabel emptyLabel = new JLabel("No payments found", AppIcons.INFO_ICON, SwingConstants.CENTER);
        emptyLabel.setFont(Fonts.NORMAL_FONT);
        emptyLabel.setForeground(UITheme.getTextColor());

        emptyPanel.add(emptyLabel, BorderLayout.CENTER);
        return emptyPanel;
    }

    private JPanel createPaymentCard(Order order) {
        // Obtener usuario
        ApiResponse<User> userResponse = userService.findUserById(order.getUser_id());
        User user = userResponse.isSuccess() ? userResponse.getData() : null;

        // Calcular ancho de la tarjeta (90% del ancho del contenedor)
        int cardWidth = (int) (getWidth() * CARD_WIDTH_PERCENT / 100);

        // Panel principal de la tarjeta con layout adaptable
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UITheme.getSecondaryColor());
        card.setBorder(new MatteBorder(1, 1, 1, 1, getStatusColor(order.getStatus().toString())));
        card.setPreferredSize(new Dimension(cardWidth, CARD_HEIGHT));
        card.setMaximumSize(new Dimension(cardWidth, CARD_HEIGHT));

        // Panel de información con GridBagLayout para mejor adaptación
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false);
        infoPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new java.awt.Insets(2, 5, 2, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Añadir información básica
        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(createInfoLabel("Order ID: ", String.valueOf(order.getOrder_id())), gbc);

        gbc.gridy++;
        infoPanel.add(createInfoLabel("Customer: ", user != null ? user.getName() : "Unknown"), gbc);

        // Panel de acciones
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionsPanel.setOpaque(false);
        actionsPanel.setPreferredSize(new Dimension(120, 50));

        // Botón de detalles
        JButton detailsBtn = ButtonFactory.createSecondaryButton("Details", null, () -> {
            showOrderDetails(order, user);
        });
        detailsBtn.setPreferredSize(new Dimension(100, 30));

        // ComboBox de estado
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Pending", "Approved", "Rejected"});
        statusCombo.setSelectedItem(order.getStatus().toString());
        statusCombo.setFont(Fonts.NORMAL_FONT);
        statusCombo.setBackground(UITheme.getBackgroundContrast());
        statusCombo.setForeground(UITheme.getTextColor());
        statusCombo.setPreferredSize(new Dimension(100, 30));

        // Botón de actualización
        JButton updateBtn = ButtonFactory.createPrimaryButton("Update", null, () -> {
            updateOrderStatus(order, statusCombo);
        });
        updateBtn.setPreferredSize(new Dimension(100, 30));

        actionsPanel.add(detailsBtn);
        actionsPanel.add(statusCombo);
        actionsPanel.add(updateBtn);

        // Añadir componentes al panel principal
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(actionsPanel, BorderLayout.SOUTH);

        // Añadir efecto hover
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(UITheme.getSecondaryColor().brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(UITheme.getSecondaryColor());
            }
        });

        return card;
    }

    private JPanel createInfoLabel(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(Fonts.SMALL_FONT);
        labelComponent.setForeground(UITheme.getTextColor().darker());

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(Fonts.NORMAL_FONT);
        valueComponent.setForeground(UITheme.getTextColor());

        panel.add(labelComponent, BorderLayout.WEST);
        panel.add(valueComponent, BorderLayout.CENTER);

        return panel;
    }

    private Color getStatusColor(String status) {
        return switch (status.toLowerCase()) {
            case "approved" -> APPROVED_COLOR.darker();
            case "rejected" -> REJECTED_COLOR.darker();
            case "pending" -> PENDING_COLOR.darker();
            default -> UITheme.getTextColor();
        };
    }

    private void showOrderDetails(Order order, User user) {
        new OrderDetailsDialog(parent, order, user).setVisible(true);
    }

    private void updateOrderStatus(Order order, JComboBox<String> statusCombo) {
        String selectedStatus = (String) statusCombo.getSelectedItem();
        if (selectedStatus != null && !selectedStatus.equals(order.getStatus().toString())) {
            ApiResponse<Order> response = orderService.updateOrderStatus(order.getOrder_id(), selectedStatus);
            if (response.isSuccess()) {
                NotificationHandler.success("Payment status updated successfully!");
                loadPayments();
            } else {
                NotificationHandler.error("Failed to update status: " + response.getMessage());
            }
        }
    }

    // Boton para filtrar por estado
    private JComboBox<String> filterButtonActionPerformed() {
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"All", "Pending", "Approved", "Rejected"});
        return statusComboBox;
    }

    private void filterOrdersByStatus(String status) {
        paymentsContainer.removeAll();

        if( "All".equals(status) || status == null) {
            loadPayments();
            return;
        }

        for (Order order : orders) {
            if (order.getStatus().toString().equalsIgnoreCase(status)) {
                paymentsContainer.add(createPaymentCard(order));
            }
        }

        paymentsContainer.revalidate();
        paymentsContainer.repaint();
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        // Ajustar el ancho de las tarjetas cuando el tamaño del panel cambia
        if (paymentsContainer.getComponentCount() > 0) {
            int cardWidth = (int) (width * CARD_WIDTH_PERCENT / 100);
            for (Component comp : paymentsContainer.getComponents()) {
                if (comp instanceof JPanel) {
                    comp.setPreferredSize(new Dimension(cardWidth, CARD_HEIGHT));
                    comp.setMaximumSize(new Dimension(cardWidth, CARD_HEIGHT));
                }
            }
        }
    }
}
