package com.rafael.catalogo.model;

import com.rafael.catalogo.exception.StockInsuficienteException;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Artículo del catálogo, identificado de forma única por su SKU.
 *
 * <p>La identidad del producto es el SKU y solo el SKU: dos instancias con el
 * mismo SKU representan el mismo artículo aunque difieran en precio o stock.
 * Por eso {@code equals} y {@code hashCode} se basan únicamente en ese campo,
 * que además es inmutable para que el objeto no cambie de posición dentro de
 * colecciones basadas en hash.</p>
 *
 * <p>El orden natural es alfabético por nombre, sin distinguir mayúsculas.</p>
 */
public class Producto implements Comparable<Producto> {

    private final String sku;
    private String nombre;
    private BigDecimal precio;
    private int stock;
    private Categoria categoria;

    /**
     * Crea un producto validando sus invariantes.
     *
     * @param sku       identificador único, obligatorio y no vacío
     * @param nombre    nombre comercial del producto
     * @param precio    precio unitario sin impuesto, no negativo
     * @param stock     unidades disponibles, no negativo
     * @param categoria categoría a la que pertenece
     * @throws IllegalArgumentException si el SKU está vacío, el precio es
     *                                  negativo o el stock es negativo
     */
    public Producto(String sku, String nombre, BigDecimal precio, int stock, Categoria categoria) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("El SKU es obligatorio");
        }
        if (precio == null || precio.signum() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        this.sku = sku;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }

    /**
     * Incrementa el inventario disponible.
     *
     * @param cantidad unidades a ingresar, debe ser positiva
     * @throws IllegalArgumentException si la cantidad no es positiva
     */
    public void agregarStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        this.stock += cantidad;
    }

    /**
     * Descuenta unidades del inventario disponible.
     *
     * @param cantidad unidades a retirar, debe ser positiva
     * @throws IllegalArgumentException   si la cantidad no es positiva
     * @throws StockInsuficienteException si no hay unidades suficientes
     */
    public void retirarStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        if (cantidad > stock) {
            throw new StockInsuficienteException(sku, cantidad, stock);
        }
        this.stock -= cantidad;
    }

    /**
     * Calcula el precio del producto desglosado con su impuesto.
     *
     * @return la descomposición del precio en base e IVA
     */
    public PrecioConIVA precioConIVA() {
        return PrecioConIVA.desdeBase(precio);
    }

    /**
     * Devuelve el identificador único del producto.
     *
     * @return el SKU
     */
    public String getSku() {
        return sku;
    }

    /**
     * Devuelve el nombre comercial del producto.
     *
     * @return el nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Actualiza el nombre comercial del producto.
     *
     * @param nombre nuevo nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve el precio unitario sin impuesto.
     *
     * @return el precio base
     */
    public BigDecimal getPrecio() {
        return precio;
    }

    /**
     * Actualiza el precio unitario sin impuesto.
     *
     * @param precio nuevo precio, no negativo
     * @throws IllegalArgumentException si el precio es nulo o negativo
     */
    public void setPrecio(BigDecimal precio) {
        if (precio == null || precio.signum() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        this.precio = precio;
    }

    /**
     * Devuelve las unidades actualmente disponibles.
     *
     * @return el stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Devuelve la categoría del producto.
     *
     * @return la categoría
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * Compara por SKU: dos productos con el mismo SKU son el mismo artículo.
     *
     * @param o objeto a comparar
     * @return {@code true} si el otro objeto es un producto con idéntico SKU
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto otro)) return false;
        return sku.equals(otro.sku);
    }

    /**
     * Genera el hash a partir del SKU, en coherencia con {@link #equals(Object)}.
     *
     * @return el hash del producto
     */
    @Override
    public int hashCode() {
        return Objects.hash(sku);
    }

    /**
     * Define el orden natural: alfabético por nombre, ignorando mayúsculas.
     *
     * @param otro producto contra el cual comparar
     * @return negativo, cero o positivo según el orden relativo
     */
    @Override
    public int compareTo(Producto otro) {
        return this.nombre.compareToIgnoreCase(otro.nombre);
    }

    /**
     * Representación tabular de una línea, pensada para el listado por consola.
     *
     * @return una fila formateada con los datos del producto
     */
    @Override
    public String toString() {
        return "%-10s %-25s %10s %6d  %s"
                .formatted(sku, nombre, precio, stock, categoria.getDescripcion());
    }
}