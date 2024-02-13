public class User {
    private String username; // Variable to store username
    private String password; // Variable to store PW

    // Constructor for User class
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter and setter for the username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and setter for the password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Additional methods can be added as per requirements
}