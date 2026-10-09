package com.bancopreguntas.access;

import com.bancopreguntas.domain.EstadoPregunta;
import com.bancopreguntas.domain.Question;
import com.bancopreguntas.domain.QuestionDistractors;
import com.bancopreguntas.domain.QuestionRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Capa de acceso a datos.
 * Implementación sencilla de QuestionRepository usando un mapa en
 * memoria (no requiere base de datos relacional, tal como lo permite
 * el punto 1 de la Actividad 2 del taller). Se precarga con datos de
 * ejemplo para poder probar la interfaz gráfica y el patrón Observer.
 */
public class QuestionImplRepository implements QuestionRepository {

    private final Map<String, Question> preguntas = new LinkedHashMap<>();

    public QuestionImplRepository() {
        cargarDatosDeEjemplo();
    }

    @Override
    public List<Question> findAll() {
        return new ArrayList<>(preguntas.values());
    }

    @Override
    public Question findById(String id) {
        return preguntas.get(id);
    }

    @Override
    public void update(Question question) {
        preguntas.put(question.getId(), question);
    }

    private void cargarDatosDeEjemplo() {
        agregar("P-001", "Pregunta sobre DDD",
                "¿Cuál es el objetivo principal de DDD?",
                Arrays.asList("Diseñar bases de datos", "Eliminar UML", "Crear interfaces gráficas"),
                "Modelar el dominio del negocio",
                EstadoPregunta.BORRADOR);

        agregar("P-002", "Pregunta sobre MVC",
                "¿Qué componente del patrón MVC se encarga de la lógica de negocio?",
                Arrays.asList("La Vista", "El Controlador", "La base de datos"),
                "El Modelo",
                EstadoPregunta.PENDIENTE_REVISION);

        agregar("P-003", "Pregunta sobre Observer",
                "¿Qué papel cumple el Subject en el patrón Observer?",
                Arrays.asList("Renderizar la interfaz", "Persistir los datos", "Validar formularios"),
                "Mantener y notificar a los observadores suscritos",
                EstadoPregunta.BORRADOR);

        agregar("P-004", "Pregunta sobre arquitectura en capas",
                "¿Qué responsabilidad tiene la capa de acceso a datos?",
                Arrays.asList("Presentar información al usuario", "Definir reglas de negocio", "Aplicar estilos visuales"),
                "Gestionar la persistencia de la información",
                EstadoPregunta.ELIMINADA);

        agregar("P-005", "Pregunta sobre pruebas Saber Pro",
                "¿Con qué propósito se construye el banco de preguntas?",
                Arrays.asList("Reemplazar las clases magistrales", "Calificar a los docentes", "Llevar asistencia"),
                "Apoyar la preparación de estudiantes para las pruebas Saber Pro",
                EstadoPregunta.PENDIENTE_REVISION);
    }

    private void agregar(String id, String nombre, String enunciado,
                          List<String> distractoresTexto, String respuestaCorrecta,
                          EstadoPregunta estado) {
        QuestionDistractors distractores = new QuestionDistractors(distractoresTexto);
        Question pregunta = new Question(id, nombre, enunciado, distractores, respuestaCorrecta, estado);
        preguntas.put(id, pregunta);
    }
}
