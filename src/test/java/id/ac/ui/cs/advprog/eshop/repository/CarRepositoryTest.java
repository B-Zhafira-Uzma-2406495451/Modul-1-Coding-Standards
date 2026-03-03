package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarRepositoryTest {

    private CarRepository carRepository;

    @BeforeEach
    void setUp() {
        carRepository = new CarRepository();
    }

    @Test
    void testCreateAndFindById() {
        Car car = new Car();
        car.setCarName("Honda Civic");
        car.setCarColor("Putih");
        car.setCarQuantity(2);
        Car savedCar = carRepository.create(car);
        assertNotNull(savedCar.getCarId());
        Car foundCar = carRepository.findById(savedCar.getCarId());
        assertNotNull(foundCar);
        assertEquals("Honda Civic", foundCar.getCarName());
        assertEquals("Putih", foundCar.getCarColor());
        assertEquals(2, foundCar.getCarQuantity());
    }

    @Test
    void testUpdateNonExistentCar() {
        Car updatedInfo = new Car();
        updatedInfo.setCarName("Toyota Yaris");
        updatedInfo.setCarColor("Merah");
        updatedInfo.setCarQuantity(5);
        Car result = carRepository.update("id-palsu-12345", updatedInfo);
        assertNull(result);
        Car notFoundCar = carRepository.findById("id-palsu-12345");
        assertNull(notFoundCar);
    }

    @Test
    void testCreateCarWithExistingId() {
        Car car = new Car();
        car.setCarId("custom-id-999");
        car.setCarName("Suzuki Swift");
        car.setCarColor("Kuning");
        car.setCarQuantity(1);
        Car savedCar = carRepository.create(car);
        assertEquals("custom-id-999", savedCar.getCarId());
        Car foundCar = carRepository.findById("custom-id-999");
        assertNotNull(foundCar);
        assertEquals("Suzuki Swift", foundCar.getCarName());
    }
}