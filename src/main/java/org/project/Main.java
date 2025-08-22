package org.project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.project.model.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        // Configura el driver del navegador automáticamente
        // con la librería WebDriverManager
        WebDriverManager.chromedriver().setup();

        // Crea una instancia del driver de Chrome
        WebDriver driver = new ChromeDriver();
        // Configura un tiempo de espera de 10 segundos
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Navega a la página de Mercado Libre
        String url = "https://www.mercadolibre.com.co/";

        List<Producto> listaProductos = new ArrayList<>();

        // Espera 5 segundos para que veas la página
        try {
            // 1. Navegar a la página principal
            driver.get(url);

            // 2. Localizar el campo de búsqueda (usando su atributo 'name')
            WebElement searchBox = driver.findElement(By.name("as_word"));

            // 3. Escribir el producto a buscar y presionar Enter
            searchBox.sendKeys("plancha para cabello");
            searchBox.submit(); // Esto es como presionar Enter

            System.out.println("Buscando productos...");
            System.out.println("--------------------");

            Thread.sleep(3000);

            // 4. Localizar todos los productos de la página (usando un selector CSS)
            List<WebElement> productos = driver.findElements(By.cssSelector("div.ui-search-result__wrapper"));

            System.out.println("Se encontraron " + productos.size() + " productos.");
            System.out.println("--------------------");

            // 5. Iterar sobre cada producto y extraer los datos
            for (int x = 0; x < productos.size(); x++) {
                try {

                    List<WebElement> freshProductos = driver.findElements(By.cssSelector("div.ui-search-result__wrapper"));
                    WebElement producto = freshProductos.get(x);

                    // El localizador busca un elemento dentro del 'product'
                    WebElement titleElement = producto.findElement(By.cssSelector("a.poly-component__title"));
                    String title = titleElement.getText();

                    WebElement priceElement = producto.findElement(By.cssSelector("span.andes-money-amount__fraction"));
                    String price = priceElement.getText();

                    listaProductos.add(new Producto(title, price));

//                    System.out.println("Título: " + title);
//                    System.out.println("Precio: $" + price);
//                    System.out.println("--------------------");

                } catch (StaleElementReferenceException e) {
                    // Ignoramos productos que no tienen título o precio por alguna razón
                    System.out.println("Producto no encontrado");
                    continue;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            // 1. Crea una instancia de Gson para convertir a JSON
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // 2. Convierte la lista de objetos a una cadena de texto JSON
            String jsonOutput = gson.toJson(listaProductos);

            // 3. Escribe la cadena JSON en un archivo
            try(FileWriter writer = new FileWriter("productos.json")) {
                writer.write(jsonOutput);
                System.out.println("Extracción y guardado completados en 'productos.json'.");
            }

        } catch (InterruptedException | IOException e) {
            System.out.println("hubo un error");
        } finally {
            // Asegurarse de que el navegador se cierre al final
            driver.quit();
        }
    }
}