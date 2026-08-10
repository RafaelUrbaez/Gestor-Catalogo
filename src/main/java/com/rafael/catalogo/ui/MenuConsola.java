package com.rafael.catalogo.ui;

import com.rafael.catalogo.descuento.CalculadoraPrecio;
import com.rafael.catalogo.descuento.Descuento;
import com.rafael.catalogo.descuento.DescuentoFijo;
import com.rafael.catalogo.descuento.DescuentoPorcentual;
import com.rafael.catalogo.model.Categoria;
import com.rafael.catalogo.model.PrecioConIVA;
import com.rafael.catalogo.model.Producto;
import com.rafael.catalogo.service.CatalogoService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * Interfaz de usuario por consola para el gestor de catálogo.
 *
 * <p>Es la única capa que interactúa con el usuario: lee entradas, invoca al
 * servicio y traduce las excepciones de negocio en mensajes legibles. No
 * contiene reglas de negocio.</p>
 */
public class MenuConsola {

    private static final String LINEA = "=".repeat(64);

    private final CatalogoService catalogoService;
    private final Scanner scanner;

    /**
     * Crea el menú asociado a un servicio de catálogo.
     *
     * @param catalogoService servicio que ejecuta las operaciones
     */
    public MenuConsola(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Arranca el ciclo principal del menú hasta que el usuario decida salir.
     */
    public void iniciar() {
        boolean activo = true;
        while (activo) {
            mostrarOpciones();
            String opcion = leerTexto("Opcion");
            try {
                activo = procesar(opcion);
            } catch (RuntimeException e) {
                System.out.println("\n[Error] " + e.getMessage());
            }
        }
        System.out.println("\nHasta luego.");
    }

    private void mostrarOpciones() {
        System.out.println("\n" + LINEA);
        System.out.println("  GESTOR DE CATALOGO");
        System.out.println(LINEA);
        System.out.println("  1. Agregar producto");
        System.out.println("  2. Listar productos");
        System.out.println("  3. Buscar por SKU");
        System.out.println("  4. Listar por categoria");
        System.out.println("  5. Registrar entrada de stock");
        System.out.println("  6. Registrar salida de stock");
        System.out.println("  7. Calcular precio con descuentos");
        System.out.println("  8. Reporte de inventario");
        System.out.println("  9. Eliminar producto");
        System.out.println("  0. Salir");
        System.out.println(LINEA);
    }

    private boolean procesar(String opcion) {
        switch (opcion) {
            case "1" -> agregarProducto();
            case "2" -> listarProductos();
            case "3" -> buscarPorSku();
            case "4" -> listarPorCategoria();
            case "5" -> registrarEntrada();
            case "6" -> registrarSalida();
            case "7" -> calcularPrecio();
            case "8" -> mostrarReporte();
            case "9" -> eliminarProducto();
            case "0" -> {
                return false;
            }
            default -> System.out.println("\nOpcion no valida.");
        }
        return true;
    }

    private void agregarProducto() {
        System.out.println("\n--- Nuevo producto ---");
        String sku = leerTexto("SKU");
        String nombre = leerTexto("Nombre");
        BigDecimal precio = leerDecimal("Precio");
        int stock = leerEntero("Stock inicial");
        Categoria categoria = seleccionarCategoria();

        catalogoService.registrar(new Producto(sku, nombre, precio, stock, categoria));
        System.out.println("\nProducto registrado correctamente.");
    }

    private void listarProductos() {
        mostrarTabla(catalogoService.listarOrdenados());
    }

    private void buscarPorSku() {
        String sku = leerTexto("\nSKU a buscar");
        Producto producto = catalogoService.obtenerPorSku(sku);
        PrecioConIVA precio = producto.precioConIVA();

        System.out.println("\n--- Detalle ---");
        System.out.println("SKU:        " + producto.getSku());
        System.out.println("Nombre:     " + producto.getNombre());
        System.out.println("Categoria:  " + producto.getCategoria().getDescripcion());
        System.out.println("Stock:      " + producto.getStock());
        System.out.println("Precio:     " + precio.base());
        System.out.println("ITBIS:      " + precio.iva());
        System.out.println("Total:      " + precio.total());
    }

    private void listarPorCategoria() {
        Categoria categoria = seleccionarCategoria();
        List<Producto> encontrados = catalogoService.listarPorCategoria(categoria);
        if (encontrados.isEmpty()) {
            System.out.println("\nNo hay productos en esa categoria.");
            return;
        }
        mostrarTabla(encontrados);
    }

    private void registrarEntrada() {
        String sku = leerTexto("\nSKU");
        int cantidad = leerEntero("Cantidad a ingresar");
        catalogoService.registrarEntrada(sku, cantidad);
        System.out.println("\nEntrada registrada. Stock actual: "
                + catalogoService.obtenerPorSku(sku).getStock());
    }

    private void registrarSalida() {
        String sku = leerTexto("\nSKU");
        int cantidad = leerEntero("Cantidad a retirar");
        catalogoService.registrarSalida(sku, cantidad);
        System.out.println("\nSalida registrada. Stock actual: "
                + catalogoService.obtenerPorSku(sku).getStock());
    }

    private void calcularPrecio() {
        String sku = leerTexto("\nSKU");
        Producto producto = catalogoService.obtenerPorSku(sku);

        CalculadoraPrecio calculadora = new CalculadoraPrecio();
        boolean agregando = true;

        while (agregando) {
            System.out.println("\n  1. Agregar descuento porcentual");
            System.out.println("  2. Agregar descuento fijo");
            System.out.println("  0. Calcular");
            String opcion = leerTexto("Opcion");

            switch (opcion) {
                case "1" -> calculadora.agregar(
                        new DescuentoPorcentual(leerDecimal("Porcentaje")));
                case "2" -> calculadora.agregar(
                        new DescuentoFijo(leerDecimal("Monto")));
                case "0" -> agregando = false;
                default -> System.out.println("Opcion no valida.");
            }
        }

        BigDecimal base = producto.getPrecio();
        BigDecimal conDescuento = calculadora.calcular(base);
        PrecioConIVA finalConIva = PrecioConIVA.desdeBase(conDescuento);

        System.out.println("\n--- Calculo de precio ---");
        System.out.println("Precio base:      " + base);
        for (Descuento descuento : calculadora.getDescuentos()) {
            System.out.println("  - " + descuento.descripcion());
        }
        System.out.println("Con descuentos:   " + conDescuento);
        System.out.println("ITBIS:            " + finalConIva.iva());
        System.out.println("TOTAL A PAGAR:    " + finalConIva.total());
    }

    private void mostrarReporte() {
        System.out.println("\n--- Reporte de inventario ---");
        System.out.println("Productos registrados: " + catalogoService.cantidadProductos());
        System.out.println("Valor del inventario:  " + catalogoService.valorTotalInventario());

        List<Producto> agotados = catalogoService.sinStock();
        if (agotados.isEmpty()) {
            System.out.println("Sin productos agotados.");
        } else {
            System.out.println("\nProductos agotados:");
            agotados.forEach(producto -> System.out.println("  - " + producto.getNombre()));
        }
    }

    private void eliminarProducto() {
        String sku = leerTexto("\nSKU a eliminar");
        catalogoService.eliminar(sku);
        System.out.println("\nProducto eliminado.");
    }

    private void mostrarTabla(List<Producto> productos) {
        if (productos.isEmpty()) {
            System.out.println("\nEl catalogo esta vacio.");
            return;
        }
        System.out.println("\n" + "%-10s %-25s %10s %6s  %s"
                .formatted("SKU", "NOMBRE", "PRECIO", "STOCK", "CATEGORIA"));
        System.out.println("-".repeat(70));
        productos.forEach(System.out::println);
    }

    private Categoria seleccionarCategoria() {
        Categoria[] categorias = Categoria.values();
        System.out.println("\nCategorias:");
        for (int i = 0; i < categorias.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, categorias[i].getDescripcion());
        }
        while (true) {
            int seleccion = leerEntero("Seleccione");
            if (seleccion >= 1 && seleccion <= categorias.length) {
                return categorias[seleccion - 1];
            }
            System.out.println("Seleccion fuera de rango.");
        }
    }

    private String leerTexto(String etiqueta) {
        System.out.print(etiqueta + ": ");
        return scanner.nextLine().trim();
    }

    private int leerEntero(String etiqueta) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(etiqueta));
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero entero.");
            }
        }
    }

    private BigDecimal leerDecimal(String etiqueta) {
        while (true) {
            try {
                return new BigDecimal(leerTexto(etiqueta));
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero valido.");
            }
        }
    }
}