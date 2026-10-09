package com.bancopreguntas.domain;

/**
 * Capa de dominio.
 * Estados posibles del ciclo de vida de una pregunta del banco,
 * tal como se describe en la Actividad 1 del taller.
 */
public enum EstadoPregunta {

    BORRADOR("Borrador"),
    PENDIENTE_REVISION("Pendiente de revisión"),
    ELIMINADA("Eliminada");

    private final String etiqueta;

    EstadoPregunta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
