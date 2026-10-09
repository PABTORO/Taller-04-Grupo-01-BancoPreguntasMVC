package com.bancopreguntas.presentation;

import com.bancopreguntas.domain.EstadoPregunta;
import com.bancopreguntas.domain.Question;
import com.bancopreguntas.domain.QuestionService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/*
 Capa de presentación.
 Ventana principal de "Gestión de preguntas" (Actividad 1 y 2).
 Cumple el papel de Vista + Controlador del micro patrón MVC:
 captura los eventos del usuario (seleccionar pregunta, cambiar
 estado) y delega toda la lógica de negocio en QuestionService
 (el Modelo), que es además el Subject del patrón Observer. */
public class GUIQuestions extends JFrame {

    private final QuestionService service;

    private final JComboBox<Question> comboPreguntas = new JComboBox<>();
    private final JButton btnCargar = new JButton("Cargar pregunta");

    private final JTextField txtId = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextArea txtEnunciado = new JTextArea(3, 20);
    private final DefaultListModel<String> modeloOpciones = new DefaultListModel<>();
    private final JList<String> listaOpciones = new JList<>(modeloOpciones);
    private final JLabel lblRespuestaCorrecta = new JLabel();
    private final JLabel lblEstadoActual = new JLabel();
    private final JComboBox<EstadoPregunta> comboNuevoEstado = new JComboBox<>(EstadoPregunta.values());
    private final JButton btnActualizarEstado = new JButton("Actualizar estado");

    private Question preguntaSeleccionada;

    public GUIQuestions(QuestionService service) {
        super("Banco de Preguntas Saber PRO — Gestión de preguntas");
        this.service = service;

        construirInterfaz();
        cargarPreguntasEnCombo();
        registrarEventos();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 620);
        setLocationByPlatform(true);
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(10, 10));

        // --- Panel selector ---
        JPanel panelSelector = new JPanel(new BorderLayout(5, 5));
        panelSelector.setBorder(BorderFactory.createTitledBorder("Seleccionar pregunta"));
        panelSelector.add(comboPreguntas, BorderLayout.CENTER);
        panelSelector.add(btnCargar, BorderLayout.EAST);

        // --- Panel formulario ---
        JPanel panelFormulario = new JPanel();
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Formulario de pregunta"));
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));

        txtId.setEditable(false);
        txtNombre.setEditable(false);
        txtEnunciado.setEditable(false);
        txtEnunciado.setLineWrap(true);
        txtEnunciado.setWrapStyleWord(true);
        listaOpciones.setEnabled(false);

        panelFormulario.add(campoConEtiqueta("Id:", txtId));
        panelFormulario.add(campoConEtiqueta("Nombre:", txtNombre));
        panelFormulario.add(new JLabel("Pregunta:"));
        panelFormulario.add(new JScrollPane(txtEnunciado));
        panelFormulario.add(new JLabel("Opciones:"));
        panelFormulario.add(new JScrollPane(listaOpciones));
        panelFormulario.add(campoConEtiqueta("Respuesta correcta:", lblRespuestaCorrecta));
        panelFormulario.add(campoConEtiqueta("Estado actual:", lblEstadoActual));
        panelFormulario.add(campoConEtiqueta("Nuevo estado:", comboNuevoEstado));

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(btnActualizarEstado);
        panelFormulario.add(panelBoton);

        add(panelSelector, BorderLayout.NORTH);
        add(new JScrollPane(panelFormulario), BorderLayout.CENTER);

        habilitarFormulario(false);
    }

    private JPanel campoConEtiqueta(String etiqueta, JComponent campo) {
        JPanel panel = new JPanel(new BorderLayout(5, 2));
        panel.add(new JLabel(etiqueta), BorderLayout.NORTH);
        panel.add(campo, BorderLayout.CENTER);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private void cargarPreguntasEnCombo() {
        comboPreguntas.removeAllItems();
        List<Question> preguntas = service.listarPreguntas();
        for (Question pregunta : preguntas) {
            comboPreguntas.addItem(pregunta);
        }
    }

    private void registrarEventos() {
        btnCargar.addActionListener(e -> cargarPreguntaSeleccionada());
        btnActualizarEstado.addActionListener(e -> actualizarEstadoPregunta());
    }

    private void cargarPreguntaSeleccionada() {
        Question seleccionada = (Question) comboPreguntas.getSelectedItem();
        if (seleccionada == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una pregunta del listado.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Se recarga desde el servicio para reflejar el estado más reciente.
        preguntaSeleccionada = service.buscarPorId(seleccionada.getId());

        txtId.setText(preguntaSeleccionada.getId());
        txtNombre.setText(preguntaSeleccionada.getNombre());
        txtEnunciado.setText(preguntaSeleccionada.getEnunciado());

        modeloOpciones.clear();
        for (String opcion : preguntaSeleccionada.getTodasLasOpciones()) {
            modeloOpciones.addElement(opcion);
        }

        lblRespuestaCorrecta.setText(preguntaSeleccionada.getRespuestaCorrecta());
        lblEstadoActual.setText(preguntaSeleccionada.getEstado().getEtiqueta());
        comboNuevoEstado.setSelectedItem(preguntaSeleccionada.getEstado());

        habilitarFormulario(true);
    }

    private void actualizarEstadoPregunta() {
        if (preguntaSeleccionada == null) {
            return;
        }
        EstadoPregunta nuevoEstado = (EstadoPregunta) comboNuevoEstado.getSelectedItem();

        // Toda la lógica de cambio de estado y la notificación a los
        // observadores vive en el Modelo (QuestionService); esta vista
        // solo dispara la acción.
        service.cambiarEstado(preguntaSeleccionada.getId(), nuevoEstado);

        preguntaSeleccionada = service.buscarPorId(preguntaSeleccionada.getId());
        lblEstadoActual.setText(preguntaSeleccionada.getEstado().getEtiqueta());

        JOptionPane.showMessageDialog(this,
                "Estado actualizado a: " + nuevoEstado.getEtiqueta(),
                "Actualización exitosa", JOptionPane.INFORMATION_MESSAGE);
    }

    private void habilitarFormulario(boolean habilitado) {
        comboNuevoEstado.setEnabled(habilitado);
        btnActualizarEstado.setEnabled(habilitado);
    }
}
