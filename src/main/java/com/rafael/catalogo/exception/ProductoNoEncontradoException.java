package com.rafael.catalogo.exception;

/**
 * Se lanza cuando se consulta un producto que no existe en el catálogo.
 */
public class ProductoNoEncontradoException extends RuntimeException {

    /**
     * Crea la excepción indicando el SKU buscado.
     *
     * @param sku identificador que no arrojó resultados
     */
    public ProductoNoEncontradoException(String sku) {
        super("No existe un producto con SKU: " + sku);
    }
}