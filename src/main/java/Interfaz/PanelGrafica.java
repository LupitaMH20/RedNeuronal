package Interfaz;

import javax.swing.*;
import java.awt.*;

public class PanelGrafica extends JPanel {

    private double[] datos;

    public PanelGrafica(double[] datos) {
        this.datos = datos;
        setPreferredSize(new Dimension(510, 230));
        setBackground(Color.BLACK); // fondo negro
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (datos == null || datos.length == 0) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        int margen = 20;

        // Buscar máximos y mínimos
        double max = datos[0];
        double min = datos[0];
        for (double d : datos) {
            if (d > max) {
                max = d;
            }
            if (d < min) {
                min = d;
            }
        }

        if (max == min) {
            max += 1;
            min -= 1;
        }

        int n = datos.length;
        double espacioX = (double) (width - 2 * margen) / Math.max(1, n - 1);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(margen, height - margen, width - margen, height - margen);
        g2.drawLine(margen, margen, margen, height - margen);

        g2.setColor(Color.red);
        g2.setStroke(new BasicStroke(4.0f));
        for (int i = 0; i < n - 1; i++) {
            int x1 = margen + (int) (i * espacioX);
            int x2 = margen + (int) ((i + 1) * espacioX);

            int y1 = (int) (height - margen - ((datos[i] - min) / (max - min)) * (height - 2 * margen));
            int y2 = (int) (height - margen - ((datos[i + 1] - min) / (max - min)) * (height - 2 * margen));

            g2.drawLine(x1, y1, x2, y2);
        }
    }
}
