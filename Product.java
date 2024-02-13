// Abstract superclass Product
public abstract class Product {
    private String productId;     // Unique identifier for each product
    private String name;          // Name of the product
    private int availableItems;   // Number of items available in stock
    private double price;         // Price of the product

    public Product(String productId, String name, int availableItems, double price) {
        this.productId = productId; //set pID
        this.name = name;
        this.availableItems = availableItems;
        this.price = price;
    }

    // Getter and setter methods
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAvailableItems() { return availableItems; }
    public void setAvailableItems(int availableItems) { this.availableItems = availableItems; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    // You can add more methods or fields if needed
    @Override
    public String toString() {
        return "ID: " + getProductId() + ", Name: " + getName() + ", Available Items: " + getAvailableItems() + ", Price: " + getPrice();
    }
}


