import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class UserLoginWindow {
    private JFrame loginFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    // Constructor to initialize the login UI
    public UserLoginWindow() {
        initializeLoginUI();
    }

    // Set up the login window interface
    private void initializeLoginUI() {
        loginFrame = new JFrame("User Login"); // Create the main window
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation
        loginFrame.setSize(300, 150); // Window size
        loginFrame.setLayout(new BorderLayout()); // Layout manager

        // Panel to hold the components
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Labels and text fields for username and password
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);

        // Buttons for login and registration
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        // Adding components to the panel
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        // Add panel to the main window
        loginFrame.add(panel, BorderLayout.CENTER);

        // Event handlers for buttons
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegister());

        // Display the window
        loginFrame.setVisible(true);
    }

    // Handle login process
    private void handleLogin() {
        String username = usernameField.getText(); // Get username
        String password = new String(passwordField.getPassword()); // Get password

        // Check if it's the first login
        boolean isFirstLogin = checkFirstLogin(username);

        // Validate login credentials
        if (isValidLogin(username, password)) {
            loginFrame.dispose(); // Close login window
            // Open the main GUI and pass the first login flag
            SwingUtilities.invokeLater(() -> new WestminsterShoppingManagerGUI(username, isFirstLogin));
        } else {
            // Show error message on invalid credentials
            JOptionPane.showMessageDialog(loginFrame, "Invalid username or password. Please try again.");
        }
    }

    // Check if the user is logging in for the first time
    private boolean checkFirstLogin(String username) {
        try (Scanner scanner = new Scanner(new File("../user_login_history.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals("true")) {
                    return false; // Not the first login
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return true; // First login
    }

    // Handle user registration process
    private void handleRegister() {
        String username = usernameField.getText(); // Get username
        String password = new String(passwordField.getPassword()); // Get password

        // Attempt to register user
        if (registerUser(username, password)) {
            // Show success message
            JOptionPane.showMessageDialog(loginFrame, "Registration successful. You can now login.");
        } else {
            // Show error message if registration fails
            JOptionPane.showMessageDialog(loginFrame, "Registration failed. Please choose a different username.");
        }
    }

    // Validate login credentials against stored data
    private boolean isValidLogin(String username, String password) {
        try (Scanner scanner = new Scanner(new File("../users.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true; // Login is valid
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false; // Login is invalid
    }

    // Register a new user
    private boolean registerUser(String username, String password) {
        // Check if username exists
        try (Scanner scanner = new Scanner(new File("../users.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].equals(username)) {
                    return false; // Username exists
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Write new user to file
        try (FileWriter writer = new FileWriter("../users.txt", true)) {
            writer.write(username + "," + password + "\n");
            return true; // Registration successful
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Registration failed
        }
    }

    // Main method to launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(UserLoginWindow::new);
    }
}