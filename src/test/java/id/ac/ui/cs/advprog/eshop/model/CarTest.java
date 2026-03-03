package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarTest {

    private Car car;

    @BeforeEach
    void setUp() {
        this.car = new Car();
    }

    @Test
    void testValidCarSetup() {
        car.setCarId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        car.setCarName("Toyota Corolla");
        car.setCarColor("Hitam");
        car.setCarQuantity(10);

        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", car.getCarId());
        assertEquals("Toyota Corolla", car.getCarName());
        assertEquals("Hitam", car.getCarColor());
        assertEquals(10, car.getCarQuantity());
    }

    @Test
    void testCarWithNullValuesAndNegativeQuantity() {
        car.setCarId(null);
        car.setCarName(null);
        car.setCarColor(null);
        car.setCarQuantity(-5);
        assertNull(car.getCarId());
        assertNull(car.getCarName());
        assertNull(car.getCarColor());
        assertEquals(-5, car.getCarQuantity());
    }

    @Test
    void testCarWithEmptyStringsAndZeroQuantity() {
        car.setCarId("");
        car.setCarName("");
        car.setCarColor("");
        car.setCarQuantity(0);
        assertEquals("", car.getCarId());
        assertEquals("", car.getCarName());
        assertEquals("", car.getCarColor());
        assertEquals(0, car.getCarQuantity());
    }
}