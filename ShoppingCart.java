import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<Product> products;

    // Constructor for ShoppingCart
    public ShoppingCart() {
        products = new ArrayList<>();
    }

    // Method to add a product to the cart
    public void addProduct(Product product) {
        products.add(product);
    }

    // Method to remove a product from the cart
    public void removeProduct(Product product) {
        products.remove(product);
    }

    // Method to calculate the total cost of products in the cart
    public double calculateTotalCost() {
        double total = 0.0;
        for (Product product : products) {
            total += product.getPrice() * product.getAvailableItems();
        }
        return total;
    }

    // Additional methods can be added as per requirements
}