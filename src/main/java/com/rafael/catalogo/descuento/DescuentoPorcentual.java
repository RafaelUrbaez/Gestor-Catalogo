package com.rafael.catalogo.descuento;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Descuento que reduce el precio en un porcentaje dado.
 */
public class DescuentoPorcentual implements Descuento {

    private static final BigDecimal CIEN = new BigDecimal("100");

    private final BigDecimal porcentaje;

    /**
     * Crea el descuento con el porcentaje indicado.
     *
     * @param porcentaje valor entre 0 y 100
     * @throws IllegalArgumentException si el porcentaje está fuera de rango
     */
    public DescuentoPorcentual(BigDecimal porcentaje) {
        if (porcentaje == null
                || porcentaje.signum() < 0
                || porcentaje.compareTo(CIEN) > 0) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100");
        }
        this.porcentaje = porcentaje;
    }

    @Override
    public BigDecimal aplicar(BigDecimal precio) {
        BigDecimal factor = CIEN.subtract(porcentaje)
                .divide(CIEN, 4, RoundingMode.HALF_UP);
        return precio.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String descripcion() {
        return "Descuento del %s%%".formatted(porcentaje.stripTrailingZeros().toPlainString());
    }
}