package com.rafael.catalogo.service;

import com.rafael.catalogo.exception.ProductoNoEncontradoException;
import com.rafael.catalogo.model.Categoria;
import com.rafael.catalogo.model.Producto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Operaciones de negocio sobre el catálogo de productos.
 *
 * <p>Los productos se almacenan en memoria indexados por SKU, lo que permite
 * búsquedas directas sin recorrer la colección. Esta clase no realiza ninguna
 * interacción con el usuario: devuelve datos o lanza excepciones, y es la capa
 * de presentación la que decide cómo comunicarlos.</p>
 */
public class CatalogoService {

    private final Map<String, Producto> productos = new LinkedHashMap<>();

    /**
     * Registra un producto nuevo en el catálogo.
     *
     * @param producto producto a registrar
     * @throws IllegalArgumentException si el producto es nulo o su SKU ya existe
     */
    public void registrar(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (productos.containsKey(producto.getSku())) {
            throw new IllegalArgumentException(
                    "Ya existe un producto con el SKU: " + producto.getSku());
        }
        productos.put(producto.getSku(), producto);
    }

    /**
     * Busca un producto sin garantizar que exista.
     *
     * @param sku identificador a buscar
     * @return el producto si está registrado, o vacío si no
     */
    public Optional<Producto> buscarPorSku(String sku) {
        return Optional.ofNullable(productos.get(sku));
    }

    /**
     * Obtiene un producto que se asume existente.
     *
     * @param sku identificador a buscar
     * @return el producto correspondiente
     * @throws ProductoNoEncontradoException si el SKU no está registrado
     */
    public Producto obtenerPorSku(String sku) {
        return buscarPorSku(sku)
                .orElseThrow(() -> new ProductoNoEncontradoException(sku));
    }

    /**
     * Lista todos los productos ordenados por su orden natural.
     *
     * @return lista ordenada alfabéticamente por nombre
     */
    public List<Producto> listarOrdenados() {
        List<Producto> lista = new ArrayList<>(productos.values());
        lista.sort(null);
        return lista;
    }

    /**
     * Filtra los productos que pertenecen a una categoría.
     *
     * @param categoria categoría por la cual filtrar
     * @return lista de productos de esa categoría, vacía si no hay ninguno
     */
    public List<Producto> listarPorCategoria(Categoria categoria) {
        List<Producto> resultado = new ArrayList<>();
        for (Producto producto : productos.values()) {
            if (producto.getCategoria() == categoria) {
                resultado.add(producto);
            }
        }
        return resultado;
    }

    /**
     * Registra una entrada de mercancía al inventario.
     *
     * @param sku      producto que recibe las unidades
     * @param cantidad unidades a ingresar, debe ser positiva
     * @throws ProductoNoEncontradoException si el SKU no está registrado
     * @throws IllegalArgumentException      si la cantidad no es positiva
     */
    public void registrarEntrada(String sku, int cantidad) {
        obtenerPorSku(sku).agregarStock(cantidad);
    }

    /**
     * Registra una salida de mercancía del inventario.
     *
     * @param sku      producto del cual se retiran unidades
     * @param cantidad unidades a retirar, debe ser positiva
     * @throws ProductoNoEncontradoException si el SKU no está registrado
     * @throws IllegalArgumentException      si la cantidad no es positiva
     * @throws com.rafael.catalogo.exception.StockInsuficienteException
     *         si no hay unidades suficientes
     */
    public void registrarSalida(String sku, int cantidad) {
        obtenerPorSku(sku).retirarStock(cantidad);
    }

    /**
     * Elimina un producto del catálogo.
     *
     * @param sku identificador del producto a eliminar
     * @throws ProductoNoEncontradoException si el SKU no está registrado
     */
    public void eliminar(String sku) {
        if (productos.remove(sku) == null) {
            throw new ProductoNoEncontradoException(sku);
        }
    }

    /**
     * Calcula el valor total del inventario a precio de costo.
     *
     * @return la suma de precio por stock de todos los productos
     */
    public BigDecimal valorTotalInventario() {
        BigDecimal total = BigDecimal.ZERO;
        for (Producto producto : productos.values()) {
            total = total.add(
                    producto.getPrecio().multiply(BigDecimal.valueOf(producto.getStock())));
        }
        return total;
    }

    /**
     * Identifica los productos que se quedaron sin existencias.
     *
     * @return lista de productos con stock en cero
     */
    public List<Producto> sinStock() {
        List<Producto> resultado = new ArrayList<>();
        for (Producto producto : productos.values()) {
            if (producto.getStock() == 0) {
                resultado.add(producto);
            }
        }
        return resultado;
    }

    /**
     * Indica cuántos productos hay registrados.
     *
     * @return cantidad de productos en el catálogo
     */
    public int cantidadProductos() {
        return productos.size();
    }

    /**
     * Devuelve todos los productos sin permitir modificar la colección interna.
     *
     * @return vista de solo lectura de los productos registrados
     */
    public Collection<Producto> todos() {
        return List.copyOf(productos.values());
    }
}