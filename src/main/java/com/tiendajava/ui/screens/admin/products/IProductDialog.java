package com.tiendajava.ui.screens.admin.products;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.tiendajava.model.Category;
import com.tiendajava.model.Product;
import com.tiendajava.service.CategoryService;
import com.tiendajava.service.ProductService;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;
import com.tiendajava.utils.ApiResponse;

public abstract class IProductDialog extends JDialog {

    protected final JTextField nameField = new JTextField(20);
    protected final JTextField priceField = new JTextField(20);
    protected final JTextField stockField = new JTextField(20);
    protected final JTextField descriptionField = new JTextField(20);
    protected final JTextField imageField = new JTextField(20);
    protected final JButton selectImageBtn = new JButton("Select Image"); 

    protected final JPanel formPanel = new JPanel(new GridBagLayout()); 
    protected GridBagConstraints gbc = new GridBagConstraints();

    protected final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService(); 

    public Product product;
    protected Category category;
    protected final String categoryName;
    protected final Runnable onRunnable;

    protected File imageFile;

    private static final int DIALOG_WIDTH = 450;
    private static final int DIALOG_HEIGHT = 500;
    private static final Insets FIELD_INSETS = new Insets(8, 8, 8, 8);
    private static final Dimension FIELD_PREFERRED_SIZE = new Dimension(200, 30);
    private static final int GBC_WEIGHTX = 1;

    protected int currentRow = 0;

    public IProductDialog(Product product, String categoryName, Runnable onRunnable) {
        this.product = product;
        this.categoryName = categoryName;
        this.onRunnable = onRunnable;
        setModal(true);
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.getPrimaryColor());
        setLayout(new BorderLayout()); 

        nameField.setPreferredSize(FIELD_PREFERRED_SIZE);
        priceField.setPreferredSize(FIELD_PREFERRED_SIZE);
        stockField.setPreferredSize(FIELD_PREFERRED_SIZE);
        descriptionField.setPreferredSize(FIELD_PREFERRED_SIZE);
        imageField.setPreferredSize(FIELD_PREFERRED_SIZE);
        imageField.setEditable(false); 

        gbc.insets = FIELD_INSETS;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = GBC_WEIGHTX;

        add(formPanel, BorderLayout.CENTER);
        initializeCategoryAndUI();
        setTitle(getDialogTitle());
    }

    public Product getProduct(){
        return product;
    }

    public void setProduct(Product product){
        this.product = product;
    }

    protected void addLabelAndField(JPanel panel, GridBagConstraints gbc, String labelText, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = currentRow;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel label = new JLabel(labelText + ":");
        label.setForeground(UITheme.getTextColor());
        label.setFont(Fonts.NORMAL_FONT);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.gridy = currentRow;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(field, gbc);

        currentRow++;
    }

    protected void addLabelAndField(JPanel panel, GridBagConstraints gbc, String labelText, JComboBox<?> comboBox) {
        gbc.gridx = 0;
        gbc.gridy = currentRow;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel label = new JLabel(labelText + ":");
        label.setForeground(UITheme.getTextColor());
        label.setFont(Fonts.NORMAL_FONT);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.gridy = currentRow;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(comboBox, gbc);

        currentRow++;
    }

    private void initializeCategoryAndUI() {
        new Thread(() -> {
            ApiResponse<Category> response = categoryService.getCategoryByName(categoryName);
            SwingUtilities.invokeLater(() -> {
                if (response.isSuccess() && response.getData() != null) {
                    this.category = response.getData();
                    buildForm();
                    if (product != null) {
                        populateFields();
                    }
                } else {
                    NotificationHandler.error("Failed to load category '" + categoryName + "': " + response.getMessage());
                }
            });
        }).start();
    }

    protected void populateFields() {
        nameField.setText(product.getName());
        priceField.setText(String.valueOf(product.getPrice()));
        stockField.setText(String.valueOf(product.getStock()));
        descriptionField.setText(product.getDescription());
    }

    protected void chooseImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "jpeg", "gif"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            imageFile = chooser.getSelectedFile();
            imageField.setText(imageFile.getName());
        }
    }

    protected void buildForm() {
        formPanel.setBackground(UITheme.getPrimaryColor());
        formPanel.setBorder(UIUtils.getDefaultPadding());

        currentRow = 0; 

        addLabelAndField(formPanel, gbc, "Name", nameField);
        addLabelAndField(formPanel, gbc, "Price", priceField);
        addLabelAndField(formPanel, gbc, "Stock", stockField);
        addLabelAndField(formPanel, gbc, "Description", descriptionField);
        gbc.gridx = 0;
        gbc.gridy = currentRow;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel imageLabel = new JLabel("Image:");
        imageLabel.setForeground(UITheme.getTextColor());
        imageLabel.setFont(Fonts.NORMAL_FONT);
        formPanel.add(imageLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = currentRow;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel imageSelectionPanel = new JPanel(new BorderLayout(5, 0));
        imageSelectionPanel.setBackground(UITheme.getPrimaryColor());
        imageSelectionPanel.add(imageField, BorderLayout.CENTER);
        imageSelectionPanel.add(selectImageBtn, BorderLayout.EAST);
        formPanel.add(imageSelectionPanel, gbc);

        selectImageBtn.addActionListener(e -> chooseImage());

        currentRow++;
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            SwingUtilities.invokeLater(() -> super.setVisible(true));
        } else {
            SwingUtilities.invokeLater(() -> super.setVisible(false));
        }
    }

    protected abstract void onSave();
    protected abstract String getDialogTitle();
}