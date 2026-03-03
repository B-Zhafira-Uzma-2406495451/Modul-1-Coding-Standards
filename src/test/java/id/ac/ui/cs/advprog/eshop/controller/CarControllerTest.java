package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarServiceImpl;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarServiceImpl carService;
    @MockBean
    private ProductService productService;

    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setCarId("car-12345");
        car.setCarName("Honda Brio");
        car.setCarColor("Kuning");
        car.setCarQuantity(5);
    }

    @Test
    void testCreateCarPage() throws Exception {
        mockMvc.perform(get("/car/createCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("createCar"))
                .andExpect(model().attributeExists("car"));
    }

    @Test
    void testCreateCarPost() throws Exception {
        when(carService.create(any(Car.class))).thenReturn(car);

        mockMvc.perform(post("/car/createCar").flashAttr("car", car))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));
    }

    @Test
    void testCreateCarPage_WrongMethod() throws Exception {
        mockMvc.perform(post("/car/createCar"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testCarListPage() throws Exception {
        List<Car> carList = List.of(car);
        when(carService.findAll()).thenReturn(carList);

        mockMvc.perform(get("/car/listCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("carList"))
                .andExpect(model().attributeExists("cars"))
                .andExpect(model().attribute("cars", carList));
    }

    @Test
    void testCarListPage_EmptyList() throws Exception {
        when(carService.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/car/listCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("carList"))
                .andExpect(model().attribute("cars", new ArrayList<>()));
    }

    @Test
    void testEditCarPage() throws Exception {
        when(carService.findById(car.getCarId())).thenReturn(car);

        mockMvc.perform(get("/car/editCar/" + car.getCarId()))
                .andExpect(status().isOk())
                .andExpect(view().name("editCar"))
                .andExpect(model().attributeExists("car"));
    }

    @Test
    void testEditCarPage_NotFound() {
        when(carService.findById("id-palsu")).thenThrow(new IllegalArgumentException("Car not found"));

        try {
            mockMvc.perform(get("/car/editCar/id-palsu"));
        } catch (Exception e) {
            assert e.getCause() instanceof IllegalArgumentException;
        }
    }

    @Test
    void testEditCarPost() throws Exception {
        mockMvc.perform(post("/car/editCar").flashAttr("car", car))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));

        verify(carService, times(1)).update(car.getCarId(), car);
    }

    @Test
    void testDeleteCar() throws Exception {
        mockMvc.perform(post("/car/deleteCar")
                        .param("carId", car.getCarId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));

        verify(carService, times(1)).deleteCarById(car.getCarId());
    }

    @Test
    void testDeleteCar_WrongMethodGet() throws Exception {
        mockMvc.perform(get("/car/deleteCar").param("carId", car.getCarId()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testDeleteCar_MissingParam() throws Exception {
        mockMvc.perform(post("/car/deleteCar"))
                .andExpect(status().is4xxClientError());
    }
}