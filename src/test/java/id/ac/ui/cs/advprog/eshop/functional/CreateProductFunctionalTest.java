package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateProductFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String baseUrl;

    private String url;

    @BeforeEach
    void setupTest() {
        url = String.format("%s:%d", baseUrl, serverPort);
    }

    @Test
    void createProduct_isSuccessful(ChromeDriver driver) throws Exception {
        driver.get(url + "/product/create");

        WebElement nameInput = driver.findElement(By.id("nameInput"));
        nameInput.clear();
        String testName = "Sampo Cap Bambang";
        nameInput.sendKeys(testName);

        WebElement quantityInput = driver.findElement(By.id("quantityInput"));
        quantityInput.clear();
        String testQuantity = "10";
        quantityInput.sendKeys(testQuantity);

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        String currentUrl = driver.getCurrentUrl();
        assertEquals(url + "/product/list", currentUrl);

        WebElement productTable = driver.findElement(By.tagName("table"));
        String tableContent = productTable.getText();

        assertTrue(tableContent.contains(testName), "Nama produk harus muncul di tabel");
        assertTrue(tableContent.contains(testQuantity), "Jumlah produk harus muncul di tabel");
    }
}