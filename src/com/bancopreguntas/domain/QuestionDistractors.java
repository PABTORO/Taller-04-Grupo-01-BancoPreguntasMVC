package com.bancopreguntas.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Capa de dominio.
 * Representa las opciones incorrectas (distractores) asociadas
 * a una pregunta de selección múltiple. Se modela como una entidad
 * separada de Question para aislar la responsabilidad de administrar
 * las opciones "señuelo" del resto de los datos de la pregunta.
 */
public class QuestionDistractors {

    private final List<String> opciones;

    public QuestionDistractors() {
        this.opciones = new ArrayList<>();
    }

    public QuestionDistractors(List<String> opciones) {
        this.opciones = new ArrayList<>(opciones);
    }

    public void agregar(String opcion) {
        opciones.add(opcion);
    }

    public List<String> getOpciones() {
        return Collections.unmodifiableList(opciones);
    }

    public int cantidad() {
        return opciones.size();
    }
}
