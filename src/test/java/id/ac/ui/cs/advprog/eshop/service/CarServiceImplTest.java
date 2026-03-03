package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setCarId("car-123");
        car.setCarName("Pajero Sport");
        car.setCarColor("Hitam");
        car.setCarQuantity(3);
    }

    @Test
    void testCreateCarSuccessfully() {
        when(carRepository.create(car)).thenReturn(car);
        Car savedCar = carService.create(car);
        assertNotNull(savedCar);
        assertEquals(car.getCarId(), savedCar.getCarId());
        verify(carRepository, times(1)).create(car);
    }

    @Test
    void testCreateCarWithNullParameter() {
        when(carRepository.create(null)).thenReturn(null);
        Car savedCar = carService.create(null);
        assertNull(savedCar);
        verify(carRepository, times(1)).create(null);
    }

    @Test
    void testCreateCarNegative_NullCar() {
        when(carRepository.create(null)).thenReturn(null);
        Car result = carService.create(null);
        assertNull(result);
        verify(carRepository, times(1)).create(null);
    }

    @Test
    void testUpdateCarSuccessfully() {
        carService.update("car-123", car);
        verify(carRepository, times(1)).update("car-123", car);
    }

    @Test
    void testUpdateCarWithInvalidId() {
        String invalidId = "id-bodong-999";
        carService.update(invalidId, car);
        verify(carRepository, times(1)).update(invalidId, car);
    }

    @Test
    void testUpdateCarNegative_IdNotFound() {
        String invalidCarId = "id-bodong-123";
        Car updatedCar = new Car();
        updatedCar.setCarName("Mobil Fiktif");
        carService.update(invalidCarId, updatedCar);
        verify(carRepository, times(1)).update(invalidCarId, updatedCar);
    }

    @Test
    void testDeleteCarByIdSuccessfully() {
        carService.deleteCarById("car-123");
        verify(carRepository, times(1)).delete("car-123");
    }

    @Test
    void testDeleteCarByIdWithNull() {
        carService.deleteCarById(null);
        verify(carRepository, times(1)).delete(null);
    }

    @Test
    void testDeleteCarNegative_NullId() {
        carService.deleteCarById(null);
        verify(carRepository, times(1)).delete(null);
    }

    @Test
    void testFindAllCarsSuccessfully() {
        List<Car> carList = new ArrayList<>();
        carList.add(car);
        Iterator<Car> iterator = carList.iterator();
        when(carRepository.findAll()).thenReturn(iterator);
        List<Car> result = carService.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("car-123", result.get(0).getCarId());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdNotFound() {
        String invalidId = "id-tidak-terdaftar";
        when(carRepository.findById(invalidId)).thenReturn(null);
        Car result = carService.findById(invalidId);
        assertNull(result);
        verify(carRepository, times(1)).findById(invalidId);
    }

    @Test
    void testFindAllWhenDatabaseIsEmpty() {
        List<Car> emptyList = new ArrayList<>();
        when(carRepository.findAll()).thenReturn(emptyList.iterator());
        List<Car> result = carService.findAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(carRepository, times(1)).findAll();
    }
}