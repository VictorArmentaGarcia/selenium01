package com.test.stream;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.util.List;

/**
 * Prueba automatizada para la consulta y verificación de datos en una tabla HTML.
 */
public class ConsultaTablaTest {

    private static WebDriver driver;
    // URL de la página con la tabla (¡ADAPTAR A TU ENTORNO!)
    private static final String BASE_URL = "http://localhost:8080/tabla-datos"; 

    @Before
    static void setup() {
        // Inicialización y configuración del driver
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    void testConsultaYVerificacionDeFilaEnTabla() {
        // --- 1. PREPARACIÓN: Navegar y esperar la carga de la tabla ---
        System.out.println("Navegando a la URL: " + BASE_URL);
        driver.get(BASE_URL);

        // Espera explícita para asegurar que la tabla principal esté visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement tabla = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("data-table"))
        );
        System.out.println("Tabla con ID 'data-table' encontrada.");


        // --- 2. ACCIÓN: Buscar la Fila por Contenido (Ejemplo: Nombre del Producto/Usuario) ---
        
        // Define el valor único que quieres buscar en la tabla
        String valorBuscado = "Laptop XYZ";
        
        // XPath para encontrar la Fila (<tr>) que contenga el valor buscado
        // Esto busca cualquier fila (tr) dentro del cuerpo de la tabla (tbody) 
        // que contenga una celda (td) con el texto 'Laptop XYZ'.
        String xpathFila = "//table[@id='data-table']/tbody/tr[td[contains(text(), '" + valorBuscado + "')]]";

        // Localizar la fila completa
        WebElement filaBuscada;
        try {
            filaBuscada = driver.findElement(By.xpath(xpathFila));
            System.out.println("Fila de datos para '" + valorBuscado + "' encontrada.");
        } catch (org.openqa.selenium.NoSuchElementException e) {
            // Asersión que verifica que la fila exista
            assertEquals(false, "❌ FALLO: La fila con el valor '" + valorBuscado + "' no se encontró en la tabla.");
            return; // Detener la ejecución si la fila no se encuentra
        }

        // --- 3. ASERSIONES: Verificar Contenido Específico en la Fila ---
        
        // Asumiendo el orden de las columnas: 
        // Columna 1: Nombre (ya verificado implícitamente)
        // Columna 2: Estado
        // Columna 3: Fecha
        
        // 3.1. Localizar todas las celdas (<td>) dentro de la fila encontrada
        List<WebElement> celdas = filaBuscada.findElements(By.tagName("td"));

        // 3.2. Verificar el Estado (Columna 2, índice 1 en la lista)
        String estadoEsperado = "Activo";
        String estadoActual = celdas.get(1).getText(); // Índice 1 para la segunda columna
        
        assertEquals(estadoEsperado, estadoActual, 
            "❌ FALLO DE DATO: El Estado para '" + valorBuscado + 
            "' es incorrecto. Esperado: " + estadoEsperado + ", Actual: " + estadoActual);
            
        // 3.3. Verificar la Fecha (Columna 3, índice 2 en la lista)
        String fechaEsperada = "2025-11-20";
        String fechaActual = celdas.get(2).getText(); // Índice 2 para la tercera columna
        
        assertEquals(fechaEsperada, fechaActual, 
            "❌ FALLO DE DATO: La Fecha para '" + valorBuscado + 
            "' es incorrecta. Esperado: " + fechaEsperada + ", Actual: " + fechaActual);

        System.out.println("✅ PRUEBA EXITOSA: La fila y los datos consultados (" + estadoActual + ", " + fechaActual + ") son correctos.");
    }

    @After
    static void teardown() {
        // Cerrar el navegador
        if (driver != null) {
            System.out.println("Cerrando el navegador...");
            driver.quit();
        }
    }
}