package com.bancopreguntas.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Capa de dominio.
 * Entidad que representa una pregunta del banco de preguntas Saber Pro.
 */
public class Question {

    private String id;
    private String nombre;
    private String enunciado;
    private QuestionDistractors distractores;
    private String respuestaCorrecta;
    private EstadoPregunta estado;

    public Question(String id, String nombre, String enunciado,
                     QuestionDistractors distractores, String respuestaCorrecta,
                     EstadoPregunta estado) {
        this.id = id;
        this.nombre = nombre;
        this.enunciado = enunciado;
        this.distractores = distractores;
        this.respuestaCorrecta = respuestaCorrecta;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public QuestionDistractors getDistractores() {
        return distractores;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public EstadoPregunta getEstado() {
        return estado;
    }

    public void setEstado(EstadoPregunta estado) {
        this.estado = estado;
    }

    /**
     * Devuelve todas las opciones (distractores + respuesta correcta)
     * en un solo listado, útil para pintarlas en el formulario de la GUI.
     */
    public List<String> getTodasLasOpciones() {
        List<String> todas = new ArrayList<>(distractores.getOpciones());
        todas.add(respuestaCorrecta);
        return todas;
    }

    @Override
    public String toString() {
        // Se usa para mostrar la pregunta dentro del JComboBox del selector.
        return id + " - " + nombre;
    }
}
