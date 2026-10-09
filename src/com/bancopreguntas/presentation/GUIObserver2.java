package com.bancopreguntas.presentation;

import com.bancopreguntas.domain.EstadoPregunta;
import com.bancopreguntas.domain.QuestionService;
import com.bancopreguntas.infra.Observer;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Capa de presentación.
 * Vista gráfica: muestra una gráfica de pastel con el porcentaje de
 * preguntas en cada estado. Al igual que GUIObserver1, implementa
 * Observer y se suscribe al QuestionService (Subject) para redibujarse
 * automáticamente cuando el estado de una pregunta cambia.
 */
public class GUIObserver2 extends JFrame implements Observer {

    private final QuestionService service;
    private final PieChartPanel panelGrafica;

    private static final Map<EstadoPregunta, Color> COLORES = new LinkedHashMap<>();
    static {
        COLORES.put(EstadoPregunta.BORRADOR, new Color(66, 133, 244));
        COLORES.put(EstadoPregunta.PENDIENTE_REVISION, new Color(251, 188, 5));
        COLORES.put(EstadoPregunta.ELIMINADA, new Color(234, 67, 53));
    }

    public GUIObserver2(QuestionService service) {
        super("Vista gráfica — distribución de preguntas");
        this.service = service;

        panelGrafica = new PieChartPanel();

        setLayout(new BorderLayout());
        add(new JLabel("Distribución de preguntas por estado", SwingConstants.CENTER), BorderLayout.NORTH);
        add(panelGrafica, BorderLayout.CENTER);
        add(construirLeyenda(), BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(360, 400);
        setLocationByPlatform(true);

        refrescar();
    }

    @Override
    public void update() {
        refrescar();
    }

    private void refrescar() {
        panelGrafica.setDatos(service.calcularPorcentajePorEstado());
    }

    private JPanel construirLeyenda() {
        JPanel leyenda = new JPanel(new GridLayout(EstadoPregunta.values().length, 1));
        for (EstadoPregunta estado : EstadoPregunta.values()) {
            JLabel etiqueta = new JLabel("■ " + estado.getEtiqueta());
            etiqueta.setForeground(COLORES.get(estado));
            leyenda.add(etiqueta);
        }
        return leyenda;
    }

    /** Panel que dibuja la gráfica de pastel a partir de los porcentajes por estado. */
    private static class PieChartPanel extends JPanel {

        private Map<EstadoPregunta, Double> datos = new LinkedHashMap<>();

        void setDatos(Map<EstadoPregunta, Double> datos) {
            this.datos = datos;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (datos.isEmpty()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int diametro = Math.min(getWidth(), getHeight()) - 40;
            int x = (getWidth() - diametro) / 2;
            int y = (getHeight() - diametro) / 2;

            double anguloInicial = 90;
            for (EstadoPregunta estado : EstadoPregunta.values()) {
                double porcentaje = datos.getOrDefault(estado, 0.0);
                double anguloBarrido = porcentaje * 3.6; // 100% == 360 grados
                g2.setColor(COLORES.get(estado));
                g2.fillArc(x, y, diametro, diametro, (int) Math.round(anguloInicial),
                        -(int) Math.round(anguloBarrido));
                anguloInicial -= anguloBarrido;
            }

            g2.setColor(Color.DARK_GRAY);
            g2.drawOval(x, y, diametro, diametro);
        }
    }
}
