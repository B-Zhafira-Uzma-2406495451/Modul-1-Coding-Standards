package id.ac.ui.cs.advprog.eshop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class EshopApplicationTests {

    @Test
    void contextLoads() { // This method is intentionally empty to check if the Spring application context loads successfully.
    }

    @Test
    void mainMethodRunsSuccessfully() {
        assertDoesNotThrow(() -> {
            EshopApplication.main(new String[] {"--spring.main.web-application-type=none"});
        });
    }

}