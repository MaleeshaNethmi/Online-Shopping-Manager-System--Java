// Import necessary Java libraries
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.io.*;
import javax.swing.*;

public class WestminsterShoppingManager implements ShoppingManager {
    private List<Product> productList; // List to store products
    private Scanner scanner; // Scanner for user input

    public WestminsterShoppingManager() {  // Constructor of WestminsterShoppingManager
        productList = new ArrayList<>();  // Initialize the product list
        scanner = new Scanner(System.in); // Initialize the scanner
    }

    // Main method of the program
    public static void main(String[] args) {
        WestminsterShoppingManager manager = new WestminsterShoppingManager();
        String filename = "../products.txt";
        File file_p = new File(filename);

        // Load products from '../products.txt' at the start of the application
        manager.loadProductsFromFile(file_p.getAbsolutePath());

        // Main loop for the menu
        while (true) {
            // Display the main menu
            System.out.println("\nWestminster Shopping Manager Menu:");
            System.out.println("1. Add a new product");
            System.out.println("2. Delete a product");
            System.out.println("3. Display all products");
            System.out.println("4. Save products to file");
            System.out.println("5. Open GUI");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            String input = manager.scanner.nextLine(); // Get user input for menu choice
            int choice;

            try {  // Try to parse the input as an integer
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {  // If user input enter letter display error message
                System.out.println("Invalid input. Please enter a number.");
                continue; // Return to the start of the loop
            }

            // Process the user's menu choice
            switch (choice) {
                case 1:
                    manager.addProduct(); // Add a new products
                    break;
                case 2:
                    manager.deleteProduct(); // delete products
                    break;
                case 3:
                    manager.displayProducts(); // display products
                    break;
                case 4:
                    manager.saveProductsToFile(filename); // save to file
                    break;
                case 5:
                    manager.launchGUI(); // open GUI
                    break;
                case 6:
                    manager.saveProductsToFile(filename); // when user forgets to save
                    System.out.println("Exiting..."); // Exit the program
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");  // If the user selected a number not between 1 and 6 Display error message
            }
        }
    }

    // Method to add a new product
    public void addProduct() {
        // Check if the product list has reached its maximum capacity
        if (productList.size() >= 50) {
            System.out.println("Cannot add more products. Maximum limit of 50 products reached.");
            return;
        }

        // Prompt the user for the product type
        System.out.println("Enter product type (1 for Electronics, 2 for Clothing): ");
        int type;
        while (true) {
            if (scanner.hasNextInt()) {
                type = scanner.nextInt();
                if (type == 1 || type == 2) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 1 for Electronics or 2 for Clothing.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Clear invalid input
            }
        }
        scanner.nextLine();

        // Prompt for and validate the Product ID
        System.out.println("Enter Product ID ( For Electronics, start with 'A'. For Clothing, start with 'B': ");
        String productId = scanner.nextLine();
        while (!isValidProductId(productId, type) || isProductIdExists(productId)) {
            if (!isValidProductId(productId, type)) {
                System.out.println("Invalid Product ID. For Electronics, start with 'A'. For Clothing, start with 'B'.");
            } else {
                System.out.println("This Product ID already exists. Please enter a different Product ID.");
            }
            productId = scanner.nextLine();
        }
        while (isProductIdExists(productId)) {
            System.out.println("This Product ID already exists. Please enter a different Product ID."); //If user enter already exists pID display error message
            productId = scanner.nextLine();
        }


        // Prompt for the Product Name
        System.out.println("Enter Product Name: ");
        String name = scanner.nextLine();

        // Validate the number of available items
        int availableItems = -1;
        while (availableItems < 0) {
            System.out.println("Enter Number of Available Items: ");
            if (scanner.hasNextInt()) {
                availableItems = scanner.nextInt();
                if (availableItems < 0) {
                    System.out.println("Number of available items cannot be negative. Please enter a valid number.");
                }
            } else {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next(); // Clear invalid input
            }
        }

        // Validate the price
        double price = -1.0;
        while (price < 0) {
            System.out.println("Enter Price: ");
            if (scanner.hasNextDouble()) {
                price = scanner.nextDouble();
                if (price < 0) {
                    System.out.println("Price cannot be negative. Please enter a valid number.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Clear invalid input
            }
        }
        scanner.nextLine();

        // Create a Product instance based on the product type
        Product product = null;
        if (type == 1) {
            // Handle the case for Electronics products
            System.out.println("Enter Brand: ");
            String brand = scanner.nextLine();

            // Validate warranty period
            int warrantyPeriod = -1;
            while (warrantyPeriod < 0) {
                System.out.println("Enter Warranty Period (years): ");
                if (scanner.hasNextInt()) {
                    warrantyPeriod = scanner.nextInt();
                    if (warrantyPeriod < 0) {
                        System.out.println("Warranty period cannot be negative. Please enter a valid number.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter an integer.");
                    scanner.next(); // Clear invalid input
                }
            }
            scanner.nextLine();

            // Create an Electronics object
            product = new Electronics(productId, name, availableItems, price, brand, warrantyPeriod);
        } else if (type == 2) {
            // Handle the case for Clothing products
            System.out.println("Enter Size: ");
            String size = scanner.nextLine();

            System.out.println("Enter Color: ");
            String color = scanner.nextLine();

            // Create a Clothing object
            product = new Clothing(productId, name, availableItems, price, size, color);
        } else {
            // Handle invalid product type
            System.out.println("Invalid product type.");
            return;
        }

        // Add the created product to the product list
        productList.add(product);
        System.out.println("Product added successfully.");
    }

    // Method to validate Product ID based on type
    private boolean isValidProductId(String productId, int type) {
        if (productId == null || productId.isEmpty()) {
            return false;
        }
        return (type == 1 && productId.startsWith("A")) || (type == 2 && productId.startsWith("B"));
    }
    private boolean isProductIdExists(String productId) {    //Checks if a product ID already exists in the productList
        for (Product product : productList) {  // Check if the current product's ID matches the given productId
            if (product.getProductId().equals(productId)) {
                return true;   // If a match is found, return true indicating the ID already exists
            }
        }
        return false;  // If no match is found after checking all products, return false
    }

    public void deleteProduct() {   //Deletes a product
        if (productList.isEmpty()) {  // Check if the productList is empty before attempting to delete
            System.out.println("No products available to delete.");
            return;
        }
        System.out.println("Enter Product ID to delete: ");  //Prompt the user to enter the ID of the product  to delete
        String productId = scanner.next();

        Product toRemove = null; // Initialize a variable to hold the product to be removed
        for (Product product : productList) {
            if (product.getProductId().equals(productId)) {
                toRemove = product; // If a product with the matching ID is found, assign it to toRemove
                break;  // Break the loop as the product is found
            }
        }

        if (toRemove != null) { // Check if a product to remove was found
            productList.remove(toRemove); // Remove the product from the productList
            String productType = toRemove instanceof Electronics ? "Electronics" : "Clothing"; // Output a confirmation message along with the type of the deleted product
            System.out.println("Deleted " + productType + " Product: " + toRemove);
            System.out.println("Product deleted successfully.");
            System.out.println("Total products left: " + productList.size()); // Output the total number of products left after deletion
            scanner.nextLine();
        } else {
            System.out.println("Product not found.");// If the product was not found, inform the user
            scanner.nextLine();
        }
    }


    public void displayProducts() {  // display all products
        // Check if the productList is empty
        if (productList.isEmpty()) {
            System.out.println("No products available to display.");
            return; // Exit the method if the list is empty
        }

        // Sort the productList by Product ID for a consistent display order
        productList.sort(Comparator.comparing(Product::getProductId));

        // Iterate over each product in the productList
        for (Product product : productList) {
            // Use StringBuilder for efficient string concatenation
            StringBuilder productDetails = new StringBuilder();

            // Determine the type of product (Electronics or Clothing)
            String productType = product instanceof Electronics ? "Electronics" : "Clothing";
            productDetails.append(productType).append(" - ");

            // Append basic product details to the StringBuilder
            productDetails.append("ID: ").append(product.getProductId()).append(", ");
            productDetails.append("Name: ").append(product.getName()).append(", ");
            productDetails.append("Items: ").append(product.getAvailableItems()).append(", ");
            productDetails.append("Price: ").append(product.getPrice()).append(", ");

            // Append additional details specific to Electronics or Clothing
            if (product instanceof Electronics) {
                Electronics electronics = (Electronics) product;
                productDetails.append("Brand: ").append(electronics.getBrand()).append(", ");
                productDetails.append("Warranty: ").append(electronics.getWarrantyPeriod()).append(" years");
            } else if (product instanceof Clothing) {
                Clothing clothing = (Clothing) product;
                productDetails.append("Size: ").append(clothing.getSize()).append(", ");
                productDetails.append("Color: ").append(clothing.getColor());
            }

            // Print the complete product details
            System.out.println(productDetails.toString());
        }
    }

    public void saveProductsToFile(String filename) {  // save products to file method
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {  // Try-with-resources statement to handle file writing, ensures file is closed after operations
            // Iterate over each product in the productList
            for (Product product : productList) {
                String productType = product instanceof Electronics ? "Electronics" : "Clothing";  // Determine the product type (Electronics or Clothing)

                // Write product details to the file in a comma-separated format
                out.println(productType + "," + product.getProductId() + "," + product.getName() + ","
                        + product.getAvailableItems() + "," + product.getPrice()
                        + ((product instanceof Electronics)
                        ? "," + ((Electronics) product).getBrand() + "," + ((Electronics) product).getWarrantyPeriod()
                        : "," + ((Clothing) product).getSize() + "," + ((Clothing) product).getColor()));
            }
        } catch (IOException e) {
            e.printStackTrace();  // Handle potential IOException by printing the stack trace
            return; // Exit the method in case of an error
        }
        System.out.println("Products saved successfully to '" + filename + "'."); // Notify the user that products have been successfully saved
    }

    public void loadProductsFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 0;
    
            while ((line = reader.readLine()) != null) {
                lineNumber++;
    
                try {
                    // Split the line into parts using a comma as the separator
                    String[] parts = line.split(",");
                    String type = parts[0]; // Get the product type
                    String id = parts[1].trim();   // Get the product ID and trim any leading/trailing spaces
                    String name = parts[2].trim(); // Get the product name and trim any leading/trailing spaces
                    // Parse the number of available items and price from the string
                    int availableItems = Integer.parseInt(parts[3].trim()); // Trim spaces before parsing
                    double price = Double.parseDouble(parts[4].trim()); // Trim spaces before parsing
    
                    // Check the product type and create the appropriate product object
                    if ("Electronics".equals(type)) {
                        // Extract additional details specific to Electronics
                        String brand = parts[5].trim(); // Trim spaces before parsing
                        int warranty = Integer.parseInt(parts[6].trim()); // Trim spaces before parsing
                        productList.add(new Electronics(id, name, availableItems, price, brand, warranty));
                    } else if ("Clothing".equals(type)) {
                        // Extract additional details specific to Clothing
                        String size = parts[5].trim(); // Trim spaces before parsing
                        String color = parts[6].trim(); // Trim spaces before parsing
                        productList.add(new Clothing(id, name, availableItems, price, size, color));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing line " + lineNumber + ": " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void launchGUI() {
        try {
            // Set the class name for the GUI
            String className = "WestminsterShoppingManagerGUI";

            // Build the command to run the GUI class
            String command = "java -cp . " + className;

            // Create a process builder with the command
            ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", command);

            // Start the process
            Process process = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace(); // Print any exceptions that occur during the process launch
        }
    }
}
