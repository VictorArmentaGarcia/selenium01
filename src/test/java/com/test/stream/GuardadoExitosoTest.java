package com.test.stream;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertEquals;

import java.time.Duration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Prueba automatizada para verificar la funcionalidad de guardado (creación de registro).
 */
class GuardadoExitosoTest {

    // Variable estática para el driver que se compartirá entre los métodos
    private static WebDriver driver;
    
    // URL de la página del formulario a probar (¡ADAPTAR A TU ENTORNO!)
    private static final String BASE_URL = "http://localhost:8080/formulario-usuario"; 

    /**
     * Método de configuración que se ejecuta una vez antes de todas las pruebas.
     */
    @Before
    static void setup() {
        // 1. Configurar el driver de Chrome automáticamente (WebDriverManager)
        WebDriverManager.chromedriver().setup();
        
        // 2. Inicializar el WebDriver
        driver = new ChromeDriver();
        
        // 3. Maximizar la ventana del navegador
        driver.manage().window().maximize();
        
        // 4. Configurar una espera implícita para que los elementos carguen
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    /**
     * El caso de prueba para verificar el guardado de un nuevo registro.
     */
    @Test
    void testGuardadoDeNuevoUsuario() {
        // --- PREPARACIÓN ---
        System.out.println("Navegando a la URL: " + BASE_URL);
        driver.get(BASE_URL);

        // Generar datos únicos para evitar conflictos
        long timestamp = System.currentTimeMillis();
        String usernameDePrueba = "testuser_" + timestamp;
        String emailDePrueba = "testuser" + timestamp + "@example.com";

        // --- ACCIÓN ---
        try {
            // 1. Localizar los campos de entrada
            WebElement campoUsername = driver.findElement(By.id("username")); // Suponiendo ID
            WebElement campoEmail = driver.findElement(By.name("email"));     // Suponiendo Name
            WebElement botonGuardar = driver.findElement(By.xpath("//button[text()='Guardar']")); // Suponiendo XPath por texto

            // 2. Ingresar los datos de prueba
            System.out.println("Ingresando Username: " + usernameDePrueba);
            campoUsername.sendKeys(usernameDePrueba);
            campoEmail.sendKeys(emailDePrueba);

            // 3. Hacer clic en el botón de Guardar/Enviar
            System.out.println("Haciendo clic en el botón Guardar...");
            botonGuardar.click();

            // --- ASERSIONES (Verificación) ---
            
            // 4. Usar una Espera Explícita para el mensaje de éxito
            // Es crucial para guardar, ya que la operación puede tardar unos segundos.
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            
            // ASUMPCIÓN: El mensaje de éxito aparece en un elemento con el ID 'successMessage'
            WebElement mensajeExito = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("successMessage"))
            );
            
            // 5. Afirmar que el mensaje de éxito contiene el texto esperado
            String mensajeEsperado = "Usuario guardado con éxito";
            System.out.println("Mensaje de éxito recibido: " + mensajeExito.getText());
            
            assertEquals(mensajeExito.getText().contains(mensajeEsperado), 
                "❌ FALLO DE GUARDADO: El mensaje de éxito esperado (" + mensajeEsperado + 
                ") no apareció. Mensaje actual: '" + mensajeExito.getText() + "'");
                
            System.out.println("✅ PRUEBA EXITOSA: El registro se guardó correctamente y se mostró el mensaje de éxito.");

        } catch (Exception e) {
            System.err.println("❌ ERROR DURANTE EL TEST: " + e.getMessage());
            // Re-lanzar la excepción para que el test falle
            throw new RuntimeException(e);
        }
    }

    /**
     * Método de limpieza que se ejecuta una vez al finalizar todas las pruebas.
     */
    @After
    static void teardown() {
        // Cerrar el navegador
        if (driver != null) {
            System.out.println("Cerrando el navegador...");
            driver.quit();
        }
    }
}