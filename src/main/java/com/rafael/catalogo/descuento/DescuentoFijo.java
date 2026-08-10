package com.rafael.catalogo.descuento;

import java.math.BigDecimal;

/**
 * Descuento que resta un monto fijo del precio.
 *
 * <p>El resultado nunca baja de cero: si el monto supera el precio, se
 * devuelve cero en lugar de un valor negativo.</p>
 */
public class DescuentoFijo implements Descuento {

    private final BigDecimal monto;

    /**
     * Crea el descuento con el monto a restar.
     *
     * @param monto cantidad a descontar, no negativa
     * @throws IllegalArgumentException si el monto es nulo o negativo
     */
    public DescuentoFijo(BigDecimal monto) {
        if (monto == null || monto.signum() < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        this.monto = monto;
    }

    @Override
    public BigDecimal aplicar(BigDecimal precio) {
        BigDecimal resultado = precio.subtract(monto);
        return resultado.signum() < 0 ? BigDecimal.ZERO : resultado;
    }

    @Override
    public String descripcion() {
        return "Descuento fijo de %s".formatted(monto.toPlainString());
    }
}