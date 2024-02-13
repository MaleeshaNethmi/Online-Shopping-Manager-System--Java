import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WestminsterShoppingManagerGUI {  // GUI Components and Data Fields
    private boolean discountMessageShown = false;  // Tracks if discount message is shown
    private JFrame frame;  // Main frame of the application
    private JComboBox<String> categoryComboBox;  // Dropdown for selecting product categories
    private JTable productTable;  // Table to display products
    private DefaultTableModel tableModel; // Model for the products table
    private JTextArea productDetailsTextArea; // Area to display selected product details
    private JPanel bottomPanel;  // Panel at the bottom of the GUI
    private Map<String, Integer> shoppingCart;  // Shopping cart storing product IDs and quantities
    private String selectedProductId;  // ID of the currently selected product
    private double total = 0;  // Total price of items in the shopping cart
    private String selectedCategory;  // Currently selected product category
    private double firstPurchaseDiscount = 0.0;  // Discount for first purchase
    private double quantityDiscount = 0.0;  // Discount based on quantity
    private String currentUser;  // Username of the current user
    private boolean firstPurchaseDiscountApplied = false; // Flag for first purchase discount application

    public WestminsterShoppingManagerGUI(String username, boolean isFirstPurchase) {
        currentUser = username; // Set the current user
        bottomPanel = new JPanel(new BorderLayout()); // Initialize the bottom panel
        initializeUI(); // Initialize the user interface
        filterProductList("All"); // Display all products initially
        shoppingCart = new HashMap<>(); // Initialize the shopping cart
        this.isFirstPurchase = isFirstPurchase; // Set the first purchase flag
        bottomPanel.setVisible(false); // Initially hide the bottom panel
    }
    private boolean isFirstPurchase;  // Flag to check if it's the user's first purchase

    private boolean checkFirstPurchase() {  // Checks if the current user is making their first purchase
        try {
            // Open the user login history file for reading
            Scanner scanner = new Scanner(new File("../user_login_history.txt"));

            // Read through the file line by line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                // Check if the line corresponds to the current user and indicates a first purchase
                if (parts.length == 2 && parts[0].equals(currentUser) && parts[1].equals("false")) {
                    return true; // It's the user's first purchase
                }
            }
        } catch (FileNotFoundException e) {
            // Print an error if the file is not found
            e.printStackTrace();
        }
        return false; // It's not the user's first purchase
    }

    public WestminsterShoppingManagerGUI() {  //Sets up the shopping manager GUI.
        initializeUI();  // Set up the user interface
        filterProductList("All"); // Show all products initially
        shoppingCart = new HashMap<>();  // Prepare an empty shopping cart
        isFirstPurchase = true;  // Assume it's the user's first purchase
    }

    private void initializeUI() {   //  Initializes the user interface for the application
        // Initialize bottomPanel at the start of the method
        bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setVisible(false); // Now it's safe to call setVisible on bottomPanel
        frame = new JFrame("Westminster Shopping Centre");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 680);
        frame.setLayout(new BorderLayout(20, 20));

        // Create the upper panel for category selection and shopping cart button
        JPanel upperPanel = new JPanel(new BorderLayout(30, 30));

        //  set up category selection panel
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        categoryComboBox = new JComboBox<>(new String[]{"All", "Electronics", "Clothing"});
        categoryPanel.add(new JLabel("<html><b>Select Product Category:</b></html>"));
        categoryPanel.add(categoryComboBox);
        upperPanel.add(categoryPanel, BorderLayout.WEST);

        // Set up the shopping cart button
        JButton shoppingCartButton = new JButton("Shopping Cart");

        JPanel cartPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        cartPanel.add(shoppingCartButton);
        upperPanel.add(cartPanel, BorderLayout.EAST);
        upperPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 20, 30));
        frame.add(upperPanel, BorderLayout.NORTH);

        // Create the center panel for displaying the product table
        JPanel centerPanel = new JPanel(new BorderLayout(20, 20));

        // Set up the product table
        String[] columnNames = {"Product ID", "Name", "Category", "Price", "Info"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);


        productTable.setPreferredScrollableViewportSize(new Dimension(500, 100));
        productTable.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // Customize cell rendering to highlight low-stock products
        productTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component component = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                // Assuming product ID is in the first column
                String productId = (String) table.getModel().getValueAt(row, 0);

                // Retrieve updated quantity from the file
                int updatedQuantity = getCurrentQuantityFromFile(productId);

                // Change background color if updated getCurrentQuantityFromFile is less than 3
                if (updatedQuantity < 3) {
                    component.setBackground(Color.RED);
                } else {
                    component.setBackground(table.getBackground());
                }

                return component;
            }
        });



        productTable.setRowHeight(30);

        JScrollPane tableScrollPane = new JScrollPane(productTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        tableScrollPane.setPreferredSize(new Dimension(productTable.getPreferredSize().width, productTable.getPreferredSize().height));


        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        // bottom panel for displaying product details
        bottomPanel = new JPanel(new BorderLayout());
        productDetailsTextArea = new JTextArea();
        productDetailsTextArea.setEditable(false);
        productDetailsTextArea.setPreferredSize(new Dimension(600, 265));
        productDetailsTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(new JScrollPane(productDetailsTextArea), BorderLayout.CENTER);

          // Create the panel with the 'Add to Shopping Cart' button
        JPanel addtoCartPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addToCartButton = new JButton("Add to Shopping Cart");
        addtoCartPanel.add(addToCartButton, BorderLayout.SOUTH);
        addtoCartPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        bottomPanel.setVisible(false);
        bottomPanel.add(addtoCartPanel, BorderLayout.SOUTH);
        addToCartButton.setPreferredSize(new Dimension(180, 30));
        // Add panels to the frame
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

            // Attach event listeners to components
        categoryComboBox.addActionListener((ActionEvent e) -> filterProductList((String) categoryComboBox.getSelectedItem()));
        productTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && productTable.getSelectedRow() != -1) {
                displayProductDetails(productTable.getSelectedRow());
            } else {
                bottomPanel.setVisible(false);  // Hide details if no selection
            }
        });
           // Add action listener to the shopping cart button
        shoppingCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showShoppingCart();
            } // Show the shopping cart when clicked
        });

        addToCartButton.addActionListener(new ActionListener() {  // Add action listener to the 'Add to Shopping Cart' button
            @Override
            public void actionPerformed(ActionEvent e) {
                addToShoppingCart();
            }  // Add selected product to the shopping cart
        });

        loadProductsFromFile("../products.txt", "All"); // Load products from file and display them in the table

        frame.setVisible(true);  // Make the main frame visible
        isFirstPurchase = checkFirstPurchase();  // Check if it's the first purchase for the user
    }

    private void displayProductDetails(int rowIndex) {  //Displays product details based on the selected row in a table.
        // Check if a valid row is selected
        if (rowIndex != -1) {
            // Retrieve the product ID and category from the selected row
            selectedProductId = (String) tableModel.getValueAt(rowIndex, 0);
            selectedCategory = (String) tableModel.getValueAt(rowIndex, 2);

            // Get product details from a file based on the product ID and category
            String[] details = getProductDetailsFromFile(selectedProductId, selectedCategory);

            // Ensure that the details array has the required number of elements
            if (details.length >= 6) {
                // Set padding for the bottom panel and make it visible
                bottomPanel.setBorder(new EmptyBorder(10, 30, 10, 30));
                bottomPanel.setVisible(true);

                // Get the updated product quantity from a file
                int updatedQuantity = getCurrentQuantityFromFile(selectedProductId);

                // Prepare the product details text with formatting
                String productDetailsText = "Selected Product Details\n" + "\n"
                        + "Product ID  :  " + details[1] + "\n"  + "\n"
                        + "Category  :  " + details[0] + "\n"  + "\n"
                        + "Name  :  " + details[2] + "\n"  + "\n"
                        + (selectedCategory.equals("Electronics") ? "Brand  :  " + details[5] +   "\n" + "\nWarranty Period  :  " + details[6] + " year/s \n"  + "\n" : "")
                        + (selectedCategory.equals("Clothing") ? "Size  :  " + details[5] +   "\n" +"\nColor  :  " + details[6] + "\n"  + "\n" : "")
                        + "Items Available  :  " + updatedQuantity + "\n"  + "\n"  // Display the updated quantity
                        + "Price  :  " + details[4] + "$"  + "\n" ;

                // Set the prepared text in the product details text area
                productDetailsTextArea.setText(productDetailsText);
            }
        } else {
            // Hide the bottom panel if no item is selected
            bottomPanel.setVisible(false);
        }
        // Set padding for the product details text area
        productDetailsTextArea.setBorder(BorderFactory.createEmptyBorder(10, 70, 0, 0));
    }


    private void filterProductList(String category) {   // Filters the product list based on the selected category
        tableModel.setRowCount(0); // Clear the existing rows in the table model

        // Load products from the file that match the selected category
        loadProductsFromFile("../products.txt", category);

        // Display details of the first product if the table is not empty
        if (tableModel.getRowCount() > 0) {
            displayProductDetails(0);
        } else {
            // Hide the bottom panel if no products are found
            bottomPanel.setVisible(false);
        }
    }

    private int loadProductsFromFile(String filename, String selectedCategory) {   //Loads products from a file and adds them to the product table
        int quantity = 0;
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println("line = " + line);
                String[] parts = line.split(",");
                System.out.println("parts = " + Arrays.toString(parts));
                System.out.println("selectedCategory = " + selectedCategory);
                if (parts.length >= 6) {
                    String category = parts[0].trim();

                    if ("All".equals(selectedCategory) || category.equals(selectedCategory)) {
                        String productId = parts[1].trim();
                        String productName = parts[2].trim();
                        int availableItems = Integer.parseInt(parts[3].trim());
                        double price = Double.parseDouble(parts[4].trim());
                        String brand = parts[5].trim();
                        String warranty = parts[6].trim();
                        quantity = Integer.parseInt(parts[3].trim()); // Assuming quantity is in the 5th column

                        String info = brand + ", " + warranty;
                        tableModel.addRow(new Object[]{productId, productName, category, price, info});
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return quantity; // Return the quantity of the last product processed
    }


    private String[] getProductDetailsFromFile(String productId, String category) {    //  get the details of a product from a file based on its product ID.

        try (Scanner scanner = new Scanner(new File("../products.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                // Check if the current line contains the specified product ID
                if (parts.length >= 7 && parts[1].trim().equals(productId)) {
                    // Return the product details if found
                    return parts;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // Log an error if the file is not found
        }
        // Return an array with empty strings if the product is not found
        return new String[]{"", "", "", "", "", "", ""};
    }


    private String getProductNameById(String productId) {
        try (Scanner scanner = new Scanner(new File("../products.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts[1].equals(productId)) { // Assuming the product ID is in the third column
                    return parts[2].trim(); // Assuming the product name is in the fourth column
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return "Product Name Not Found";
    }


    private void showShoppingCart() {
        JFrame cartFrame = new JFrame("Shopping Cart");
        cartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cartFrame.setSize(830, 500);
        cartFrame.setLayout(new BorderLayout(20, 20));

        // Create a table for the shopping cart
        String[] cartColumnNames = {"Product", "Quantity", "Price"};
        DefaultTableModel cartTableModel = new DefaultTableModel(cartColumnNames, 0);
        JTable cartTable = new JTable(cartTableModel);

        cartTable.setRowHeight(60); // Adjust the row height as needed
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        cartTable.setDefaultRenderer(Object.class, centerRenderer);
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartFrame.add(cartScrollPane, BorderLayout.CENTER);
        cartScrollPane.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));
        cartScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        cartScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Create labels for total, discounts, and final total
        JLabel totalLabel = new JLabel("Total        :");
        JLabel discountLabel1 = new JLabel("First Purchase Discount(10%): -$");
        JLabel discountLabel2 = new JLabel("Three Items in same Category Discount(20%): -$");
        JLabel finalTotalLabel = new JLabel("Final Total  :");

        double total = 0;
        double firstPurchaseDiscount = 0;  // Initialize the discount here
        double quantityDiscount = 0;

        JPanel summaryPanel = new JPanel(new GridLayout(6, 2));
        summaryPanel.add(new JLabel()); // Empty label for layout

        totalLabel.setHorizontalAlignment(JLabel.RIGHT);
        discountLabel1.setHorizontalAlignment(JLabel.RIGHT);
        discountLabel2.setHorizontalAlignment(JLabel.RIGHT);
        finalTotalLabel.setHorizontalAlignment(JLabel.RIGHT);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 80));
        totalPanel.add(totalLabel);

        JPanel discountPanel1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        discountLabel1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 80));
        discountPanel1.add(discountLabel1);

        JPanel discountPanel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        discountLabel2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 80));
        discountPanel2.add(discountLabel2);

        JPanel finalTotalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        finalTotalLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 80));
        finalTotalPanel.add(finalTotalLabel);

        summaryPanel.add(totalPanel);
        summaryPanel.add(new JLabel());
        summaryPanel.add(discountPanel1);
        summaryPanel.add(new JLabel());
        summaryPanel.add(discountPanel2);
        summaryPanel.add(new JLabel());
        summaryPanel.add(finalTotalPanel);
        summaryPanel.add(new JLabel());

        cartFrame.add(summaryPanel, BorderLayout.SOUTH);

        // Calculate total, discounts, and final total based on shopping cart
        for (Map.Entry<String, Integer> entry : shoppingCart.entrySet()) {
            String productId = entry.getKey();
            int quantity = entry.getValue();

            // Retrieve product details from the file
            String category = getProductCategoryById(productId);
            double price = getProductPriceById(productId);
            double itemTotal = price * quantity;

            total += itemTotal;

            // Apply discounts if applicable
            if (isFirstPurchase) {
                firstPurchaseDiscount = total * 0.1; // 10% first login discount
            }

            if (quantity >= 3) {
                quantityDiscount += total * 0.2; // 20% discount for purchasing 3 or more items
            }

            // Add a row to the shopping cart table
            String[] cartRow = new String[3];
            String[] productDetails = getProductDetailsFromFile(productId, category);

            cartRow[0] = "<html>" + productId + "<br>" + getProductNameById(productId) + "</html>";

            if (category.equals("Clothing")) {
                cartRow[0] += "<br>Size: " + productDetails[6] + "<br>Color: " + productDetails[7];
            } else if (category.equals("Electronics")) {
                cartRow[0] += "<br>Brand: " + productDetails[6] + "<br>Warranty Period: " + productDetails[7];
            }

            cartRow[1] = String.valueOf(quantity);
            cartRow[2] = "$" + String.format("%.2f", itemTotal);

            cartTableModel.addRow(cartRow);
        }

        double finalTotal = total - firstPurchaseDiscount - quantityDiscount;

        // Display calculated values
        totalLabel.setText("Total:       $" + String.format("%.2f", total));
        discountLabel1.setText("First Purchase Discount(10%):       -$" + String.format("%.2f", firstPurchaseDiscount));
        discountLabel2.setText("Three Items in same Category Discount(20%):       -$" + String.format("%.2f", quantityDiscount));
        finalTotalLabel.setText("Final Total:       $" + String.format("%.2f", finalTotal));

        cartFrame.setVisible(true);
    }



    private String getProductCategoryById(String productId) {
        return productId;
    }

    private double getProductPriceById(String productId) {  // get the price of a product based on its product ID
        try (Scanner scanner = new Scanner(new File("../products.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                // Check if the line contains the specified product ID
                if (parts[1].equals(productId)) {
                    // Extract and return the product price
                    String priceString = parts[4].replace("$", "");
                    return Double.parseDouble(priceString);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // Log an error if the file is not found
        }

        // Return 0 if product not found or in case of an error
        return 0;
    }


    private void addToShoppingCart() {
        String productId = getSelectedProductId();
        if (productId != null) {
            int rowIndex = productTable.getSelectedRow();

            // Retrieve current quantity from the file
            int currentQuantity = getCurrentQuantityFromFile(productId);

            if (currentQuantity > 0) {
                // Update quantity in the file
                updateQuantityInFile(productId, currentQuantity - 1);

                // Check if the user has already received the first purchase discount
                if (isFirstPurchase == true && discountMessageShown == false) {
                    // Apply first purchase discount if it's the user's first purchase
                    double price = getProductPriceById(productId);
                    double discount = price * 0.1;
                    JOptionPane.showMessageDialog(frame, "You've received a 10% discount on this purchase!");
                    discountMessageShown = true;

                    // Deduct the discount from the total
                    total -= discount;

                    // Set the flag to indicate that the discount message has been shown

                }

                // Add to shopping cart
                shoppingCart.put(productId, shoppingCart.getOrDefault(productId, 0) + 1);

                // Update purchase history when adding to the cart
                recordFirstLogin(currentUser);


                // Refresh the table model to update the rendering
                tableModel.fireTableRowsUpdated(rowIndex, rowIndex);

                // Display updated product details
                displayProductDetails(rowIndex);

                // Record the user's purchase in ../user_login.txt
                recordPurchaseInUserLogin();
            } else {
                // Display an error message if the quantity is already zero
                JOptionPane.showMessageDialog(frame, "Item out of stock!");
            }
        }
    }


    private void recordFirstLogin(String username) { // Records the first login of a user in the user login history file
        try (FileWriter writer = new FileWriter("../user_login_history.txt", true)) {
            // Append the user's first login information to the file
            writer.write(username + ",true\n");
            System.out.println("First login recorded for user: " + username);
        } catch (IOException e) {
            e.printStackTrace();
            // An exception is logged if there's an issue writing to the file
        }
    }


    private void recordPurchaseInUserLogin() {  // Records a purchase in the user login file
        try (FileWriter writer = new FileWriter("../user_login.txt", true)) {
            // Write the current user's purchase information to the file
            writer.write(currentUser + ",true\n");
            System.out.println("Purchase recorded for user: " + currentUser);
        } catch (IOException e) {
            e.printStackTrace();
            // Log an error if there's an issue writing to the file
            System.err.println("Error recording purchase for user: " + currentUser);
        }
    }

    private void recordPurchaseHistory(String username) {  // Records the purchase history for a given user
        try (FileWriter writer = new FileWriter("../purchase_history.txt", true)) {
            // Append the user's purchase information to the file
            writer.write(username + ",true\n");
            System.out.println("Purchase recorded for user: " + username);
        } catch (IOException e) {
            e.printStackTrace();
            // Log an error if there's an issue writing to the file
            System.err.println("Error recording purchase for user: " + username);
        }
    }


    private int getCurrentQuantityFromFile(String productId) {  // get current quantity of a specified product from the products file
        try (Scanner scanner = new Scanner(new File("../products.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                // Check if line contains the specified product ID
                if (parts.length >= 7 && parts[1].trim().equals(productId)) {
                    // Return the quantity (assumed to be in the 4th column)
                    return Integer.parseInt(parts[3].trim());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Return 0 if product not found or in case of an error
        return 0;
    }



    private void updateQuantityInFile(String productId, int newQuantity) {
        try {
            File inputFile = new File("../products.txt"); // Original file
            File tempFile = new File("temp.txt"); // Temporary file for updates

            // Reading from original file and writing updates to temporary file
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    // Check if line corresponds to the product to update
                    if (parts.length >= 7 && parts[1].trim().equals(productId)) {
                        parts[3] = " " + newQuantity; // Update quantity
                        line = String.join(",", parts);
                    }
                    writer.write(line + System.lineSeparator()); // Write updated line
                }
            }

            // Replace original file with updated temporary file
            if (inputFile.delete()) {
                if (!tempFile.renameTo(inputFile)) {
                    System.out.println("Error renaming temp file.");
                }
            } else {
                System.out.println("Error deleting original file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Gets the ID of the selected product from the product table.
    private String getSelectedProductId() {
        int rowIndex = productTable.getSelectedRow(); // Get index of selected row
        if (rowIndex != -1) {
            return (String) tableModel.getValueAt(rowIndex, 0); // Return product ID if a row is selected
        }
        return null; // Return null if no row is selected
    }

    // Main method to launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserLoginWindow loginWindow = new UserLoginWindow();
        });
    }
}