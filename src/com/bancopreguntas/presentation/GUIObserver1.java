package com.bancopreguntas.presentation;

import com.bancopreguntas.domain.EstadoPregunta;
import com.bancopreguntas.domain.QuestionService;
import com.bancopreguntas.infra.Observer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

/*Capa de presentación.
 Vista de estadísticas: muestra cuántas preguntas hay por cada estado.
 Implementa Observer y se suscribe al QuestionService (Subject) para
 refrescarse automáticamente cada vez que cambia el estado de una
 pregunta, sin que GUIQuestions tenga que conocerla directamente.*/

public class GUIObserver1 extends JFrame implements Observer {

    private final QuestionService service;
    private final DefaultTableModel modeloTabla;

    public GUIObserver1(QuestionService service) {
        super("Vista de estadísticas");
        this.service = service;

        modeloTabla = new DefaultTableModel(new Object[]{"Estado", "Cantidad"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tabla = new JTable(modeloTabla);

        setLayout(new BorderLayout());
        add(new JLabel("Preguntas por estado", SwingConstants.CENTER), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(320, 220);
        setLocationByPlatform(true);

        refrescar();
    }

    @Override
    public void update() {
        refrescar();
    }

    private void refrescar() {
        modeloTabla.setRowCount(0);
        Map<EstadoPregunta, Integer> conteo = service.contarPorEstado();
        for (EstadoPregunta estado : EstadoPregunta.values()) {
            modeloTabla.addRow(new Object[]{estado.getEtiqueta(), conteo.get(estado)});
        }
    }
}
