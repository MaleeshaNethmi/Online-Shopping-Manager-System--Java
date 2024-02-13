import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void getProductId() {
        Product myProduct = new Electronics("A112", "jug", 10, 100,"LG",10);
        assertEquals("A112", myProduct.getProductId());
    }
    @Test
    void getName() {
        Product myProduct = new Electronics("A112", "jug", 10, 100,"LG",10);
        assertEquals("jug", myProduct.getName());
    }
    @Test
    void getAvailableItems() {
        Product myProduct = new Electronics("A112", "jug", 10, 100, "LG", 10);
        assertEquals(10, myProduct.getAvailableItems());
    }

    @Test
    void getBrand() {
        Product myProduct = new Electronics("A112", "jug", 10, 100, "LG", 10);
        assertEquals("LG", ((Electronics) myProduct).getBrand());
    }
    @Test
    void getWarrantyPeriod() {
        Product myProduct = new Electronics("A112", "jug", 10, 100, "LG", 10);
        assertEquals(10, ((Electronics) myProduct).getWarrantyPeriod());
    }
    @Test
    void getSize() {
        Product myProduct = new Clothing("B112", "T-shirt", 20, 1000, "M", "RED");
        assertEquals("M", ((Clothing) myProduct).getSize());
    }
    @Test
    void getColor() {
        Product myProduct = new Clothing("B112", "T-shirt", 20, 1000, "M", "RED");
        assertEquals("RED", ((Clothing) myProduct).getColor());
    }
}
