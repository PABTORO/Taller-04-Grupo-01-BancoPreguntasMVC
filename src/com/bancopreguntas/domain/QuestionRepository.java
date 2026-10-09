package com.bancopreguntas.domain;

import java.util.List;

/**
 * Capa de dominio.
 * Puerto (interfaz) que define las operaciones de persistencia
 * necesarias sobre Question. La capa de dominio depende de esta
 * abstracción, no de una implementación concreta; la capa de acceso
 * (QuestionImplRepository) es quien la implementa.
 */
public interface QuestionRepository {

    List<Question> findAll();

    Question findById(String id);

    void update(Question question);
}
