package com.rafael.catalogo.descuento;

import java.math.BigDecimal;

/**
 * Estrategia de descuento aplicable a un precio.
 *
 * <p>Cada implementación decide cómo transforma el monto recibido. Definirlo
 * como interfaz permite agregar nuevos tipos de descuento sin modificar el
 * código que los aplica.</p>
 */
public interface Descuento {

    /**
     * Aplica el descuento sobre el precio indicado.
     *
     * @param precio monto sobre el cual calcular, no negativo
     * @return el precio resultante, nunca menor que cero
     */
    BigDecimal aplicar(BigDecimal precio);

    /**
     * Describe el descuento en texto legible para el usuario.
     *
     * @return descripción del descuento
     */
    String descripcion();
}