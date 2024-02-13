public class Clothing extends Product {  // Clothing class, extending the Product class to represent clothing items
    private String size; // Variable to store the size of the clothing
    private String color; // Variable to store the color of the clothing

    // Constructor for Clothing class
    public Clothing(String productId, String name, int availableItems, double price, String size, String color) {
        super(productId, name, availableItems, price); // Call to superclass constructor
        this.size = size; // Set the size of the clothing
        this.color = color; // Set the color of the clothing
    }

    // Getter and setter for the size
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    // Getter and setter for the color
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    @Override
    public String toString() {
        return super.toString() + ", Size: " + getSize() + ", Color: " + getColor();
    }
}