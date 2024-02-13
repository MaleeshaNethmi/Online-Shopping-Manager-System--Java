public class Electronics extends Product {
    private String brand;
    private int warrantyPeriod;

    // Constructor for Electronics class
    public Electronics(String productId, String name, int availableItems, double price, String brand, int warrantyPeriod) {
        super(productId, name, availableItems, price); // Call to superclass constructor
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
    }

    // Getter and setter for the brand
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    // Getter and setter for the warranty period
    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    // Additional methods
    @Override
    public String toString() {
        return super.toString() + ", Brand: " + getBrand() + ", Warranty Period: " + getWarrantyPeriod();
    }

}