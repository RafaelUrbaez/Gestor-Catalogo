package com.rafael.catalogo;

import com.rafael.catalogo.model.Categoria;
import com.rafael.catalogo.model.Producto;
import com.rafael.catalogo.service.CatalogoService;
import com.rafael.catalogo.ui.MenuConsola;

import java.math.BigDecimal;

/**
 * Punto de entrada de la aplicación.
 */
public class Main {

    /**
     * Arranca el gestor de catálogo con datos de ejemplo precargados.
     *
     * @param args no se utilizan
     */
    public static void main(String[] args) {
        CatalogoService catalogoService = new CatalogoService();
        cargarDatosDeEjemplo(catalogoService);

        MenuConsola menuConsola = new MenuConsola(catalogoService);
        menuConsola.iniciar();
    }

    private static void cargarDatosDeEjemplo(CatalogoService catalogoService) {
        catalogoService.registrar(new Producto(
                "SKU-001", "Teclado mecanico", new BigDecimal("4500.00"), 12, Categoria.PERIFERICOS));
        catalogoService.registrar(new Producto(
                "SKU-002", "Mouse inalambrico", new BigDecimal("1800.00"), 25, Categoria.PERIFERICOS));
        catalogoService.registrar(new Producto(
                "SKU-003", "Memoria RAM 16GB", new BigDecimal("6200.00"), 8, Categoria.COMPONENTES));
        catalogoService.registrar(new Producto(
                "SKU-004", "Licencia Windows 11", new BigDecimal("9500.00"), 0, Categoria.SOFTWARE));
        catalogoService.registrar(new Producto(
                "SKU-005", "Alfombrilla XL", new BigDecimal("950.00"), 40, Categoria.ACCESORIOS));
    }
}