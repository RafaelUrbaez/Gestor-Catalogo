package com.rafael.catalogo.descuento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Aplica una cadena de descuentos sobre un precio base.
 *
 * <p>Los descuentos se aplican en el orden en que fueron registrados, tomando
 * cada uno el resultado del anterior. El orden importa: un 10% seguido de un
 * monto fijo no produce el mismo resultado que la secuencia inversa.</p>
 */
public class CalculadoraPrecio {

    private final List<Descuento> descuentos = new ArrayList<>();

    /**
     * Registra un descuento al final de la cadena.
     *
     * @param descuento estrategia a agregar
     * @return esta misma instancia, para encadenar llamadas
     * @throws IllegalArgumentException si el descuento es nulo
     */
    public CalculadoraPrecio agregar(Descuento descuento) {
        if (descuento == null) {
            throw new IllegalArgumentException("El descuento no puede ser nulo");
        }
        descuentos.add(descuento);
        return this;
    }

    /**
     * Aplica en cadena todos los descuentos registrados.
     *
     * @param precioBase monto inicial, no negativo
     * @return el precio final tras aplicar todos los descuentos
     * @throws IllegalArgumentException si el precio base es nulo o negativo
     */
    public BigDecimal calcular(BigDecimal precioBase) {
        if (precioBase == null || precioBase.signum() < 0) {
            throw new IllegalArgumentException("El precio base no puede ser negativo");
        }
        BigDecimal resultado = precioBase;
        for (Descuento descuento : descuentos) {
            resultado = descuento.aplicar(resultado);
        }
        return resultado;
    }

    /**
     * Devuelve los descuentos registrados, sin permitir modificarlos.
     *
     * @return lista inmutable de descuentos en orden de aplicación
     */
    public List<Descuento> getDescuentos() {
        return List.copyOf(descuentos);
    }

    /**
     * Elimina todos los descuentos registrados.
     */
    public void limpiar() {
        descuentos.clear();
    }
}