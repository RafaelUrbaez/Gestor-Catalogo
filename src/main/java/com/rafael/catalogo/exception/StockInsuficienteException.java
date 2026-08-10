package com.rafael.catalogo.exception;

/**
 * Se lanza al intentar retirar más unidades de las disponibles en inventario.
 *
 * <p>Extiende {@link RuntimeException} por tratarse de una violación de regla
 * de negocio: obligar a cada llamador a capturarla no aporta valor, y en una
 * capa superior se traduce a un mensaje de error para el usuario.</p>
 */
public class StockInsuficienteException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje que identifica el producto y las cantidades.
     *
     * @param sku        identificador del producto afectado
     * @param solicitado unidades que se intentaron retirar
     * @param disponible unidades realmente disponibles
     */
    public StockInsuficienteException(String sku, int solicitado, int disponible) {
        super("Stock insuficiente para %s: se solicitaron %d, hay %d"
                .formatted(sku, solicitado, disponible));
    }
}