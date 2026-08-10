package com.rafael.catalogo.model;

/**
 * Categorías disponibles en el catálogo de productos.
 *
 * <p>Cada categoría conoce su descripción legible y si los productos que
 * pertenecen a ella manejan inventario físico. Los productos digitales,
 * por ejemplo licencias de software, no descuentan stock.</p>
 */
public enum Categoria {

    PERIFERICOS("Periféricos", true),
    COMPONENTES("Componentes", true),
    SOFTWARE("Software", false),
    ACCESORIOS("Accesorios", true);

    private final String descripcion;
    private final boolean requiereStockFisico;

    Categoria(String descripcion, boolean requiereStockFisico) {
        this.descripcion = descripcion;
        this.requiereStockFisico = requiereStockFisico;
    }

    /**
     * Devuelve el nombre legible de la categoría, apto para mostrar al usuario.
     *
     * @return descripción de la categoría
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Indica si los productos de esta categoría manejan inventario físico.
     *
     * @return {@code true} si la categoría requiere control de stock
     */
    public boolean requiereStockFisico() {
        return requiereStockFisico;
    }
}