package com.rafael.catalogo.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Descomposición de un precio en su base imponible y el impuesto aplicado.
 *
 * <p>Se modela como {@code record} porque es un valor inmutable sin identidad
 * propia: dos instancias con la misma base y el mismo IVA son intercambiables.</p>
 *
 * @param base monto sin impuesto, nunca negativo
 * @param iva  monto del impuesto calculado sobre la base
 */
public record PrecioConIVA(BigDecimal base, BigDecimal iva) {

    /** Tasa de ITBIS vigente en República Dominicana. */
    private static final BigDecimal TASA_ITBIS = new BigDecimal("0.18");

    /** Cantidad de decimales usada en todos los montos monetarios. */
    private static final int ESCALA_MONETARIA = 2;

    /**
     * Constructor compacto que valida las invariantes del registro.
     *
     * @throws IllegalArgumentException si algún monto es nulo o la base es negativa
     */
    public PrecioConIVA {
        if (base == null || iva == null) {
            throw new IllegalArgumentException("Base e IVA no pueden ser nulos");
        }
        if (base.signum() < 0) {
            throw new IllegalArgumentException("La base no puede ser negativa");
        }
    }

    /**
     * Construye un precio aplicando la tasa de ITBIS sobre la base indicada.
     *
     * @param base monto sin impuesto
     * @return precio con su impuesto calculado y redondeado a dos decimales
     * @throws IllegalArgumentException si la base es nula o negativa
     */
    public static PrecioConIVA desdeBase(BigDecimal base) {
        if (base == null) {
            throw new IllegalArgumentException("La base no puede ser nula");
        }
        BigDecimal iva = base.multiply(TASA_ITBIS)
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);
        return new PrecioConIVA(base.setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP), iva);
    }

    /**
     * Calcula el monto final a pagar.
     *
     * @return la suma de la base y el impuesto
     */
    public BigDecimal total() {
        return base.add(iva);
    }
}