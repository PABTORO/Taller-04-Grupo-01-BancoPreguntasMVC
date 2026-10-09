package com.bancopreguntas;

import com.bancopreguntas.access.QuestionImplRepository;
import com.bancopreguntas.domain.QuestionRepository;
import com.bancopreguntas.domain.QuestionService;
import com.bancopreguntas.presentation.GUIObserver1;
import com.bancopreguntas.presentation.GUIObserver2;
import com.bancopreguntas.presentation.GUIQuestions;

import javax.swing.*;

/**
 * Punto de entrada de la aplicación.
 * Aquí se "cablean" las capas: se crea la capa de acceso a datos,
 * se inyecta en el servicio de dominio (que es el Subject), y se
 * suscriben las dos vistas (GUIObserver1 y GUIObserver2) al servicio
 * antes de mostrar la ventana principal.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            QuestionRepository repository = new QuestionImplRepository();
            QuestionService service = new QuestionService(repository);

            GUIObserver1 vistaEstadisticas = new GUIObserver1(service);
            GUIObserver2 vistaGrafica = new GUIObserver2(service);
            service.attach(vistaEstadisticas);
            service.attach(vistaGrafica);

            GUIQuestions ventanaPrincipal = new GUIQuestions(service);

            vistaEstadisticas.setVisible(true);
            vistaGrafica.setVisible(true);
            ventanaPrincipal.setVisible(true);
        });
    }
}
